package kookparty.kookpang.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.StepDTO;

public interface StepDAO {
	
	/**
	 * 조리법 조회
	 */

	List<StepDTO> selectByRecipeId(Connection con, long recipeId) throws SQLException;

	/**
	 * 조리법 등록
	 */
	int[] insertSteps(Connection con, long recipeId, List<StepDTO> steps) throws SQLException;
}
