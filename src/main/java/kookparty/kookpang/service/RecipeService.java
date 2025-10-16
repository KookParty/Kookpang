package kookparty.kookpang.service;

import kookparty.kookpang.dto.RecipeDTO;

public interface RecipeService {

	/**
	 * 레시피 등록
	 */
	void insertRecipe(RecipeDTO recipeDTO) throws Exception;
}
