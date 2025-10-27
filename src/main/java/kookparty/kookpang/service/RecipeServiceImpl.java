package kookparty.kookpang.service;

import java.util.List;

import kookparty.kookpang.dao.RecipeDAO;
import kookparty.kookpang.dao.RecipeDAOImpl;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.exception.DBAccessException;

public class RecipeServiceImpl implements RecipeService {
	private RecipeDAO recipeDAO = RecipeDAOImpl.getInstance();
	private static RecipeService instance = new RecipeServiceImpl();
	
	private RecipeServiceImpl() {}
	
	public static RecipeService getInstance() {
		return instance;
	}
	
	@Override
	public List<RecipeDTO> selectByOptions(String word, String category, String order, int pageNo) throws Exception {
		List<RecipeDTO> list = recipeDAO.selectByOptions(word, category, order, pageNo);
		return list;
	}
	
	@Override
	public RecipeDTO selectById(long recipeId) throws Exception {
		RecipeDTO recipeDTO = recipeDAO.selectById(recipeId);
		return recipeDTO;
	}
	
	@Override
	public List<RecipeDTO> selectVariantsByParentId(long parentRecipeId) throws Exception {
		List<RecipeDTO> list = recipeDAO.selectVariantsByParentId(parentRecipeId);
		return list;
	}
	
	@Override
	public void insertRecipe(RecipeDTO recipeDTO) throws Exception {
		int result = recipeDAO.insertRecipe(recipeDTO);
		
		if (result == 0)
			throw new DBAccessException("레시피가 등록되지 않았습니다.");
	}
	
	@Override
	public void deleteRecipeByRecipeId(long recipeId) throws Exception {
		int result = recipeDAO.deleteRecipeByRecipeId(recipeId);
		
		if (result == 0)
			throw new DBAccessException("레시피가 삭제되지 않았습니다.");
	}
	
	@Override
	public List<RecipeDTO> selectByUserIdAndLike(long userId) throws Exception {
		List<RecipeDTO> list = recipeDAO.selectByUserIdAndLike(userId);
		return list;
	}
}
