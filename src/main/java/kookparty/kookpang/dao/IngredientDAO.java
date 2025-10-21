package kookparty.kookpang.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.IngredientDTO;

public interface IngredientDAO {

	/**
	 * 재료 조회
	 */
	List<IngredientDTO> selectByRecipeId(Connection con, long recipeId) throws SQLException;
	
	/**
	 * 재료 등록
	 */
	int[] insertIngredients(Connection con, long recipeId, List<IngredientDTO> ingredients) throws SQLException;
}
