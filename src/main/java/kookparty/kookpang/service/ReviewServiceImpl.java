package kookparty.kookpang.service;

import java.util.List;

import kookparty.kookpang.dao.ReviewDAO;
import kookparty.kookpang.dao.ReviewDAOImpl;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.dto.ReviewDTO;

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
}
