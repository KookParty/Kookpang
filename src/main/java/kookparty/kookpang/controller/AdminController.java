package kookparty.kookpang.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kookparty.kookpang.dao.BoardDAO;
import kookparty.kookpang.dto.BoardDTO;
import kookparty.kookpang.dto.ChartDataDTO;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.dto.BoardDTO.Image;
import kookparty.kookpang.service.BoardService;
import kookparty.kookpang.service.BoardServiceImpl;
import kookparty.kookpang.service.OrderService;
import kookparty.kookpang.service.OrderServiceImpl;
import kookparty.kookpang.service.ProductService;
import kookparty.kookpang.service.ProductServiceImpl;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;
import kookparty.kookpang.util.FilePath;

public class AdminController implements Controller {
	ProductService productService = new ProductServiceImpl();
	OrderService orderService = new OrderServiceImpl();
	RecipeService recipeService = RecipeServiceImpl.getInstance();
	BoardService boardService = new BoardServiceImpl();
	BoardDAO dao = new BoardDAO(); 
	private static String t(String s){ return (s==null) ? "" : s.trim(); }
	private static Long parseLongObj(String s){ try { return Long.parseLong(s); } catch(Exception e){ return null; } }
	
	public ModelAndView adminPage(HttpServletRequest request, HttpServletResponse response) {
		
		return new ModelAndView("/admin/admin-main.jsp");
	}
	
	public ModelAndView productList(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ProductDTO> productList = productService.selectAll();
			request.setAttribute("list", productList);
		} catch (SQLException e) {
			request.setAttribute("errMsg", "잘못된 접근입니다?");
			e.printStackTrace();
		}
		return new ModelAndView("/admin/products.jsp");
	}
	
	public ModelAndView productInsertPage(HttpServletRequest request, HttpServletResponse response) {
		
		return new ModelAndView("/admin/product-insert.jsp");
	}
	public ModelAndView insertProduct(HttpServletRequest request, HttpServletResponse response) {
		String imageUrl = request.getParameter("imageUrl");
		String name = request.getParameter("name");
		int price = Integer.parseInt(request.getParameter("price"));
		String category = request.getParameter("category");
		String description = request.getParameter("description");
		String contextPath = request.getContextPath();
		
		ProductDTO productDTO = new ProductDTO(name, price, description, category, "test", imageUrl);
		try {
			int result = productService.insertProduct(productDTO);
			if(result == 0) {
				request.setAttribute("errMsg", "등록되지 않았습니다.");
				return new ModelAndView("/common/error.jsp");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "등록되지 않았습니다.");
			return new ModelAndView("/common/error.jsp");
			
		}
		
		return new ModelAndView(contextPath+"/front?key=admin&methodName=productList", true);
	}
	
	
	public String uploadImage(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("test");
		String saveUrl = null;
		String imageUrl = null;
		try {
			Part part = request.getPart("image");
			String fileName = part.getSubmittedFileName();
			fileName = UUID.randomUUID() + Paths.get(fileName).getFileName().toString();
			String savePath = FilePath.getSavePath(request, "product_image");
			
			if(fileName!=null) {
				saveUrl = savePath + "/" + fileName;
				part.write(saveUrl);
				imageUrl = "../product_image/" + fileName;
			}
		} catch (Exception e) {
			return null;
		} 
		return imageUrl;
	}
	
	public ModelAndView deleteProducts(HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getContextPath();
		String[] items = request.getParameterValues("selectedItems");
		if(items == null || items.length == 0) {
			request.setAttribute("errMsg", "선택된 항목이 없습니다.");
		    return new ModelAndView("/common/error.jsp");
		}
		try {
			int result = productService.deleteProduct(items);
			if(result == 0) {
				request.setAttribute("errMsg", "삭제에 실패했습니다.");
				return new ModelAndView("/common/error.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "삭제에 실패했습니다.");
			return new ModelAndView("/common/error.jsp");
		}
		
		return new ModelAndView(contextPath + "/front?key=admin&methodName=productList", true);
	}
	
	public ChartDataDTO getDailySales(HttpServletRequest request, HttpServletResponse response) {
		ChartDataDTO dailySales = null;
		System.out.println("getDailySales 들어옴");
		try {
			dailySales = orderService.getDailySales();
			for(int i : dailySales.getChartDatas()) {
				System.out.println(i);
			}
			for(String s : dailySales.getChartLabels()) {
				System.out.println(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dailySales;
	}
	
	public ChartDataDTO getBestItems(HttpServletRequest request, HttpServletResponse response) {
		ChartDataDTO bestItems = null;
		System.out.println("???");
		try {
			bestItems = orderService.getBestItems();
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return bestItems;
	}
	
	
	public ModelAndView recipeList(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<RecipeDTO> list = recipeService.selectByOptions(null, null, null, 0);
			request.setAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "오류");
			return new ModelAndView("/common/error.jsp");
		}
		return new ModelAndView("/admin/recipes.jsp");
	}
	
	public ModelAndView boardList(HttpServletRequest request, HttpServletResponse response) {
		List<BoardDTO> list = null;
		try {
			list = boardService.selectAll();
			request.setAttribute("list", list);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "오류");
			return new ModelAndView("/common/error.jsp");
		}
		return new ModelAndView("/admin/boards.jsp");
	}
	
	public ModelAndView boardInsertPage(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("/admin/board-insert.jsp");
	}
	
	public ModelAndView insertBoard(HttpServletRequest req, HttpServletResponse response) {
		HttpSession session = req.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		String contextPath = req.getContextPath();
		long userId = user.getUserId();

        String postIdStr = req.getParameter("postId");
        String category  = "notice";
        String title     = req.getParameter("title");
        String contentH  = req.getParameter("content");          // HTML
        String contentT  = t(req.getParameter("contentText"));   // 순수 텍스트 (선택)

        // NPE 방지 및 로깅
        String safeTitle = (title == null) ? "" : title;
        String safeContentH = (contentH == null) ? "" : contentH;
        System.out.println("Board.save >> title=" + safeTitle + " | content.len=" + safeContentH.length());

        // 텍스트로 검증
        String contentForCheck;
        if (contentT != null && !contentT.isBlank()) {
            contentForCheck = contentT;
        } else {
            // contentH가 null일 수 있으니 안전 처리
            String raw = (contentH == null) ? "" : contentH;
            contentForCheck = raw.replaceAll("\\<[^>]*>", "")
                                 .replace('\u00A0',' ')
                                 .trim();
        }

        if (safeTitle.isBlank() || contentForCheck.isBlank()) {
            req.setAttribute("errMsg", "제목과 내용이 비어있음");
            return new ModelAndView("/common/error.jsp");
        }

        BoardDTO d = new BoardDTO();
        d.setUserId(userId);
        d.setCategory(category.isBlank() ? "free" : category);
        d.setTitle(safeTitle);
        d.setContent(safeContentH); // 저장은 HTML 그대로

        // imageUrl 여러 개 수신
        List<Image> images = parseImages(req.getParameterValues("imageUrl"));

        if (postIdStr == null || postIdStr.isBlank()){
            long newId = dao.insertPost(d, images);
        } else {
            Long pid = parseLongObj(postIdStr);
            d.setPostId(pid);
            boolean ok = dao.updatePost(d, images);
        }
        return new ModelAndView(contextPath + "/front?key=admin&methodName=boardList", true);
	}
	
	public ModelAndView deleteBoards(HttpServletRequest req, HttpServletResponse res) throws SQLException {
		HttpSession session = req.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		String contextPath = req.getContextPath();
		long userId = user.getUserId();
		
		List<Long> postIdList = new ArrayList<Long>();
		
		String[] items = req.getParameterValues("selectedItems");
		
		if(items == null || items.length == 0) {
			req.setAttribute("errMsg", "선택된 항목이 없습니다.");
		    return new ModelAndView("/common/error.jsp");
		}
		
		for(String s : items) {
			postIdList.add(Long.parseLong(s));	
		}
		
		for(long id : postIdList) {
			boardService.deletePost(id, user);
		}
		return new ModelAndView(contextPath + "/front?key=admin&methodName=boardList", true);
	}
	
	
	// ====================== boardController 재사용메서드 가지고 오기...
	private static List<Image> parseImages(String[] urls){
        List<Image> list = new ArrayList<>();
        if(urls == null) return list;
        int order = 0;
        for(String u : urls){
            if(u == null || u.isBlank()) continue;
            Image im = new Image();
            im.setImageUrl(u.trim());
            im.setImageOrder(order++);
            list.add(im);
        }
        return list;
    }
}
