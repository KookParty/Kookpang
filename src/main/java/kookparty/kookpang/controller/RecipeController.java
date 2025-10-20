package kookparty.kookpang.controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;

public class RecipeController implements Controller {
	private RecipeService recipeService = RecipeServiceImpl.getInstance();
	
	public ModelAndView recipes(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<RecipeDTO> recipes = null;
		
		recipes = recipeService.selectAll();
		request.setAttribute("recipes", recipes);
		
		return new ModelAndView("recipes/recipes.jsp");
	}
	
	/**
	 * 전체 검색
	 */
	public Object selectAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<RecipeDTO> list = recipeService.selectAll();
		return list;
	}
}
