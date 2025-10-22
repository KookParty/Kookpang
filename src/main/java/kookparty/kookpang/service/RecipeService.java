package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.RecipeDTO;

public interface RecipeService {

	/**
	 * 레시피 전체 검색 (기본/변형, 정렬, 키워드 포함)
	 */
	List<RecipeDTO> selectByOptions(String word, String category, String order) throws Exception;
	
	/**
	 * 레시피 상세보기
	 */
	RecipeDTO selectById(long recipeId) throws Exception;
	
	/**
	 * 기본 레시피의 변형 레시피 전체 검색
	 */
	List<RecipeDTO> selectVariantsByParentId(long parentRecipeId) throws Exception;
	
	/**
	 * 레시피 등록
	 */
	void insertRecipe(RecipeDTO recipeDTO) throws Exception;
}
