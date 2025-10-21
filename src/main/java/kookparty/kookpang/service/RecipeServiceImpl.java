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
	public List<RecipeDTO> selectAll() throws Exception {
		List<RecipeDTO> list = recipeDAO.selectAll();
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
}
