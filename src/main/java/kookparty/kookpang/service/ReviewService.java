package kookparty.kookpang.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.ReviewDTO;

public interface ReviewService {

	/**
	 * 리뷰 검색
	 * 파라미터 ("", "", "", 0)으로 줄 시, 전체 레시피 검색
	 */
	List<ReviewDTO> selectByRecipeId(long recipeId) throws Exception;
	
	/**
	 * 리뷰 등록
	 */
	void insertReview(ReviewDTO reviewDTO) throws Exception;
	
	/**
	 * 리뷰 삭제
	 */
	void deleteReview(long reviewId) throws Exception;
}
