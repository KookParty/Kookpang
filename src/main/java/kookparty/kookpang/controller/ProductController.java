package kookparty.kookpang.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.ProductService;
import kookparty.kookpang.service.ProductServiceImpl;
import kookparty.kookpang.util.FilePath;

public class ProductController implements Controller {
	ProductService productService = new ProductServiceImpl();
	
	public ModelAndView ingredients(HttpServletRequest request, HttpServletResponse response) {
		List<String> categoryList = null;
		try {
			categoryList = productService.selectCategory();
			request.setAttribute("categorys", categoryList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ModelAndView("/orders/ingredients.jsp");
	}
	
	public List<ProductDTO> selectAll(HttpServletRequest request, HttpServletResponse response){
		String pageNoStr = request.getParameter("page_no");
		System.out.println(pageNoStr);
		int pageNo = 0;
		if(pageNoStr == null || pageNoStr.trim().isEmpty()) {
			pageNo = 1;
		}else {
			pageNo = Integer.parseInt(pageNoStr);
		}
		System.out.println(pageNo);
		List<ProductDTO> list = null;
		try {
			list = productService.selectAll(pageNo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<ProductDTO> selectByOptions(HttpServletRequest request, HttpServletResponse response){
		String pageNoStr = request.getParameter("pageNo");
		System.out.println(pageNoStr);
		int pageNo = 0;
		if(pageNoStr == null || pageNoStr.trim().isEmpty()) {
			pageNo = 1;
		}else {
			pageNo = Integer.parseInt(pageNoStr);
		}
		String word = request.getParameter("word");
		String category = request.getParameter("category");
		String order = request.getParameter("sort");
		List<ProductDTO> list = null;
		try {
			list = productService.selectByOptions(word, category, order, pageNo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void insertProduct(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		if(user == null || !user.getRole().equals("admin")) {
			return;
		}
		try {
			Part part = request.getPart("image");
			String fileName = part.getSubmittedFileName();
			fileName = Paths.get(fileName).getFileName().toString();
			String savePath = FilePath.getSavePath(request);
			String imageUrl = null;
			if(fileName!=null) {
				imageUrl = savePath + "/" + fileName;
				part.write(imageUrl);
			}
			
			ProductDTO productDTO = new ProductDTO(request.getParameter("name"),
					Integer.parseInt(request.getParameter("price")),
					request.getParameter("description"),
					request.getParameter("category"),
					request.getParameter("brand"),
					imageUrl);
			int result = productService.insertProduct(productDTO);
			if(result == 0) {//insert 실패시
				
			}else {//성공시
				
			}
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public void updateProduct(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		if(user == null || !user.getRole().equals("admin")) {
			return;
		}
		try {
			Part part = request.getPart("image");
			String fileName = part.getSubmittedFileName();
			fileName = Paths.get(fileName).getFileName().toString();
			String savePath = FilePath.getSavePath(request);
			String imageUrl = null;
			if(fileName!=null) {
				imageUrl = savePath + "/" + fileName;
				part.write(imageUrl);
			}
			
			
			ProductDTO productDTO = new ProductDTO(Long.parseLong(request.getParameter("product_id")), 
					request.getParameter("name"),
					Integer.parseInt(request.getParameter("price")),
					request.getParameter("description"),
					request.getParameter("category"),
					request.getParameter("brand"),
					imageUrl);
			int result = productService.updateProduct(productDTO);
			if(result == 0) {//update 실패시
				
			}else {//성공시
				
			}
		} catch (IOException | ServletException | SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public void deleteProduct(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		if(user == null || !user.getRole().equals("admin")) {
			return;
		}
		String str = request.getParameter("product_id");
		long productId = Long.parseLong(str);
		try {
			int result = productService.deleteProduct(productId);
			if(result == 0) {//update 실패시
				
			}else {//성공시
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
