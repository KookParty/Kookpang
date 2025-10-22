package kookparty.kookpang.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.dto.ReviewDTO;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;
import kookparty.kookpang.service.ReviewService;
import kookparty.kookpang.service.ReviewServiceImpl;

public class RecipeController implements Controller {
	private RecipeService recipeService = RecipeServiceImpl.getInstance();
	private ReviewService reviewService = ReviewServiceImpl.getInstance();
	
	public ModelAndView recipes(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return new ModelAndView("recipes/recipes.jsp");
	}
	
	public ModelAndView recipeDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long recipeId = Long.parseLong(request.getParameter("recipeId"));
		RecipeDTO recipeDTO = recipeService.selectById(recipeId);
		request.setAttribute("recipe", recipeDTO);
		//System.out.println("recipeDetail:recipe: " + recipeDTO);

		// 변형 레시피
		List<RecipeDTO> variants = recipeService.selectVariantsByParentId(recipeId);
		request.setAttribute("variants", variants);
		
		// 리뷰
		List<ReviewDTO> reviews = reviewService.selectByRecipeId(recipeId);
		request.setAttribute("reviews", reviews);
		
		return new ModelAndView("recipes/recipe-detail.jsp");
	}
	
	public ModelAndView variantWrite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return new ModelAndView("recipes/variant-write.jsp");
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
