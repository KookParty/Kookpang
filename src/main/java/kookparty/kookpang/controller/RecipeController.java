package kookparty.kookpang.controller;

import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kookparty.kookpang.dto.IngredientDTO;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.dto.ReviewDTO;
import kookparty.kookpang.dto.StepDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.ProductService;
import kookparty.kookpang.service.ProductServiceImpl;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;
import kookparty.kookpang.util.FilePath;

public class RecipeController implements Controller {
	private RecipeService recipeService = RecipeServiceImpl.getInstance();
	ProductService productService = new ProductServiceImpl();
	
	public ModelAndView recipes(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return new ModelAndView("recipes/recipes.jsp");
	}
	
	public ModelAndView recipeDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long recipeId = Long.parseLong(request.getParameter("recipeId"));
		RecipeDTO recipeDTO = recipeService.selectById(recipeId);
		request.setAttribute("recipe", recipeDTO);
		//System.out.println("recipeDetail:recipe: " + recipeDTO);

		// 변형 레시피
		List<RecipeDTO> variants = recipeService.selectVariantsByParentId(recipeId);
		request.setAttribute("variants", variants);
		
		return new ModelAndView("recipes/recipe-detail.jsp");
	}
	
	public ModelAndView variantWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long parentId = Long.parseLong(request.getParameter("parentId"));
		request.setAttribute("parentId", parentId);
		request.setAttribute("parentTitle", recipeService.selectById(parentId).getTitle());
		
		// 식재료 목록 가져오기
		List<ProductDTO> products = null;
		products = productService.selectAll();
		request.setAttribute("products", products);
		System.out.println(products);
		
		return new ModelAndView("recipes/variant-write.jsp");
	}
	
	public ModelAndView insertRecipe(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 유저 id 가져오기
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = user.getUserId();
		
		// 전송된 데이터 받기
		String title = request.getParameter("title");
		String description = request.getParameter("desc");
		String way = request.getParameter("way");
		way = way == null || way.isEmpty() ? "기타" : way;
		String category = request.getParameter("category");
		category = category == null || category.isEmpty() ? "기타" : category;
		
		String parentIdStr = request.getParameter("parentId");
		long parentId = 0;
		if (parentIdStr == null || parentIdStr.isEmpty()) parentId = 0;
		else parentId = Long.parseLong(parentIdStr);
		
		
		// 썸네일 이미지 업로드
		String thumbnailUrl = null;
		// form 속성에 enctype="multipart/form-data" 있는지 꼭 확인!
		Part part = request.getPart("thumb");
		if(part != null) {
			String fileName = part.getSubmittedFileName();
			if (fileName != null && !fileName.isEmpty()) {
				fileName = Paths.get(fileName).getFileName().toString();
				part.write(FilePath.getSavePath(request) + "/" + fileName);
				thumbnailUrl = "../upload/" + fileName;
			} else {
				thumbnailUrl = null;
			}
		}
		
		
		// 재료 및 조리법 데이터 받기
		String ingredientsJson = request.getParameter("ingredientsInput");
		String stepsJson = request.getParameter("stepsInput");
		
		Gson gson = new Gson();
		
		// 재료 파싱
		List<IngredientDTO> ingredients = gson.fromJson(
				ingredientsJson, new TypeToken<List<IngredientDTO>>() {}.getType());
		// 조리법 파싱
		List<StepDTO> steps = gson.fromJson(
				stepsJson, new TypeToken<List<StepDTO>>() {}.getType());
		
		for (int i = 0; i < steps.size(); i++) {
		    StepDTO step = steps.get(i);
		    Part stepPart = request.getPart("stepImg" + (i + 1)); // stepImg1, stepImg2, ...
		    if (stepPart != null && stepPart.getSize() > 0) {
		    	String fileName = part.getSubmittedFileName();
				if (fileName != null && !fileName.isEmpty()) {
					fileName = Paths.get(fileName).getFileName().toString();
					stepPart.write(FilePath.getSavePath(request) + "/" + fileName);
					step.setImageUrl("../upload/" + fileName);
				} else {
					step.setImageUrl(null);
				}
		    }
		}

		
		// 레시피 등록
		RecipeDTO recipeDTO = new RecipeDTO(userId, title, description, thumbnailUrl, way, category, parentId);
		recipeDTO.setIngredients(ingredients);
		recipeDTO.setSteps(steps);
		recipeService.insertRecipe(recipeDTO);
		
		return new ModelAndView("recipes/recipes.jsp", true);	// redirect 방식
	}
	
	/**
	 * 레시피 삭제
	 */
	public ModelAndView deleteRecipe(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long recipeId = Long.parseLong(request.getParameter("recipeId"));
		System.out.println("삭제 recipeId: " + recipeId);
		recipeService.deleteRecipeByRecipeId(recipeId);
		
		return new ModelAndView("recipes/recipes.jsp", true);
	}
	
	/**
	 * 카테고리(기본/변형), 키워드 포함 레시피 전체 검색
	 */
	public Object selectByOptions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String word = request.getParameter("word");
		String category = request.getParameter("category");
		String order = request.getParameter("sort");
		
		List<RecipeDTO> list = recipeService.selectByOptions(word, category, order);
		return list;
	}
	
}
