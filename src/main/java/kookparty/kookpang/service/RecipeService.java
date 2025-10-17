package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.RecipeDTO;

public interface RecipeService {

	/**
	 * 레시피 전체 검색
	 */
	List<RecipeDTO> selectAll() throws Exception;
	
	/**
	 * 레시피 등록
	 */
	void insertRecipe(RecipeDTO recipeDTO) throws Exception;
}
