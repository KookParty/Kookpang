package kookparty.kookpang.controller;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import kookparty.kookpang.common.RecipeType;
import kookparty.kookpang.common.TargetType;
import kookparty.kookpang.dto.ChartDataDTO;
import kookparty.kookpang.dto.IngredientDTO;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.dto.StepDTO;
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
	
	
	/* 레시피 */
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
	
	public ModelAndView recipeInsertPage(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ProductDTO> products = productService.selectAll();
			request.setAttribute("ingredientList", products);
			System.out.println("products: " + products);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "오류");
			return new ModelAndView("/common/error.jsp");
		}
		return new ModelAndView("/admin/recipe-insert.jsp");
	}
	
	public ModelAndView insertRecipe(HttpServletRequest request, HttpServletResponse response) {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String thumbnailUrl = request.getParameter("thumbnailUrl");
		String way = request.getParameter("way");
		String category = request.getParameter("category");
		String contextPath = request.getContextPath();

		
		// 재료 및 조리법 데이터 받기
		String ingredientsJson = request.getParameter("ingredients");
		String stepsJson = request.getParameter("steps");
		
		Gson gson = new Gson();
		
		// 재료 파싱
		List<IngredientDTO> ingredients = gson.fromJson(
				ingredientsJson, new TypeToken<List<IngredientDTO>>() {}.getType());
		// 조리법 파싱
		List<StepDTO> steps = gson.fromJson(
				stepsJson, new TypeToken<List<StepDTO>>() {}.getType());
		
		RecipeDTO recipeDTO = new RecipeDTO(1, title, description, thumbnailUrl, RecipeType.BASE, way, category, 0);
		recipeDTO.setIngredients(ingredients);
		recipeDTO.setSteps(steps);
		
		try {
			int result = recipeService.insertRecipe(recipeDTO);
			System.out.println("recipe: " + recipeDTO);
			if(result == 0) {
				request.setAttribute("errMsg", "레시피가 등록되지 않았습니다.");
				return new ModelAndView("/common/error.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "레시피가 등록되지 않았습니다.");
			return new ModelAndView("/common/error.jsp");
			
		}
		
		return new ModelAndView(contextPath+"/front?key=admin&methodName=recipeList", true);
	}
	
	public String uploadRecipeImage(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("test");
		String saveUrl = null;
		String imageUrl = null;
		try {
			Part part = request.getPart("image");
			String fileName = part.getSubmittedFileName();
			fileName = UUID.randomUUID() + Paths.get(fileName).getFileName().toString();
			String savePath = FilePath.getSavePath(request, "recipe_image");
			
			if(fileName!=null) {
				saveUrl = savePath + "/" + fileName;
				part.write(saveUrl);
				imageUrl = "../recipe_image/" + fileName;
			}
		} catch (Exception e) {
			return null;
		} 
		return imageUrl;
	}
	
	public ModelAndView deleteRecipes(HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getContextPath();
		String[] items = request.getParameterValues("selectedItems");
		if(items == null || items.length == 0) {
			request.setAttribute("errMsg", "선택된 항목이 없습니다.");
		    return new ModelAndView("/common/error.jsp");
		}
		try {
			for(String s : items) {
				int result = recipeService.deleteRecipeByRecipeId(Long.parseLong(s));
				if(result == 0) {
					request.setAttribute("errMsg", "삭제에 실패했습니다.");
					return new ModelAndView("/common/error.jsp");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "삭제에 실패했습니다.");
			return new ModelAndView("/common/error.jsp");
		}
		
		return new ModelAndView(contextPath + "/front?key=admin&methodName=recipeList", true);
	}
}
