package kookparty.kookpang.dao;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.RecipeDTO;

public interface RecipeDAO {

	/**
	 * 레시피 전체 검색
	 */
	List<RecipeDTO> selectByOptions(String word, String category, String order) throws SQLException;
	
	/**
	 * 레시피 단일 검색 (상세보기)
	 */
	RecipeDTO selectById(long recipeId) throws SQLException;
	
	/**
	 * 변형 레시피 전체 검색
	 */
	List<RecipeDTO> selectVariantsByParentId(long parentRecipeId) throws SQLException;
	
	/**
	 * 레시피 등록
	 */
	int insertRecipe(RecipeDTO recipeDTO) throws SQLException;
}
