package kookparty.kookpang.service;

import java.util.List;

import kookparty.kookpang.dao.ReviewDAO;
import kookparty.kookpang.dao.ReviewDAOImpl;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.dto.ReviewDTO;
import kookparty.kookpang.exception.DBAccessException;

public class ReviewServiceImpl implements ReviewService {
	private ReviewDAO reviewDAO = ReviewDAOImpl.getInstance();
	private static ReviewService instance = new ReviewServiceImpl();
	
	private ReviewServiceImpl() {}
	
	public static ReviewService getInstance() {
		return instance;
	}

	@Override
	public List<ReviewDTO> selectByRecipeId(long recipeId) throws Exception {
		List<ReviewDTO> list = reviewDAO.selectByRecipeId(recipeId);
		return list;
	}
	
	@Override
	public void insertReview(ReviewDTO reviewDTO) throws Exception {
		int result = reviewDAO.insertReview(reviewDTO);
		
		if (result == 0)
			throw new DBAccessException("리뷰가 등록되지 않았습니다.");
	}
	
	@Override
	public void deleteReview(long reviewId) throws Exception {
		int result = reviewDAO.deleteReview(reviewId);
		
		if (result == 0)
			throw new DBAccessException("리뷰가 삭제되지 않았습니다.");
	}
}
