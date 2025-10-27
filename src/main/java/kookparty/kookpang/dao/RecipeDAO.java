package kookparty.kookpang.dao;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.RecipeDTO;

public interface RecipeDAO {

	/**
	 * 레시피 전체 검색
	 * 파라미터 ("", "", "", 0)으로 줄 시, 전체 레시피 검색
	 */
	List<RecipeDTO> selectByOptions(String word, String category, String order, int pageNo) throws SQLException;
	
	/**
	 * 레시피 전체 검색 (페이지 사이즈 크기 직접 지정)
	 */
	List<RecipeDTO> selectByOptions(String word, String category, String order, int pageNo, int size) throws SQLException;
	
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
	
	/**
	 * 레시피 삭제
	 */
	int deleteRecipeByRecipeId(long recipeId) throws SQLException;
	
	/**
	 * 현재 사용자가 좋아요 한 레시피 검색 (페이징X)
	 */
	List<RecipeDTO> selectByUserIdAndLike(long userId) throws SQLException;
}
