package kookparty.kookpang.dao;

import kookparty.kookpang.dto.RecipeDTO;

public interface RecipeDAO {

	/**
	 * 레시피 등록
	 */
	int insertRecipe(RecipeDTO recipeDTO) throws Exception;
}
