package kookparty.kookpang.dao;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.RecipeDTO;

public interface RecipeDAO {

	/**
	 * 레시피 전체 검색
	 */
	List<RecipeDTO> selectAll() throws SQLException;
	
	RecipeDTO selectById(long recipeId) throws SQLException;
	
	/**
	 * 레시피 등록
	 */
	int insertRecipe(RecipeDTO recipeDTO) throws SQLException;
}
