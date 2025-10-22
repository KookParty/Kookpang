package kookparty.kookpang.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.ReviewDTO;

public interface ReviewDAO {

	/**
	 * 리뷰 검색
	 */
	List<ReviewDTO> selectByRecipeId(long recipeId) throws SQLException;
}
