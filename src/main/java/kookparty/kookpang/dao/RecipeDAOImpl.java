package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.common.RecipeType;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.util.DbUtil;

public class RecipeDAOImpl implements RecipeDAO {
	private IngredientDAO ingredientDAO = IngredientDAOImpl.getInstance();
	private StepDAO stepDAO = StepDAOImpl.getInstance();
	
	private Properties proFile = new Properties();
	private static RecipeDAO instance = new RecipeDAOImpl();
	
	private RecipeDAOImpl() {
		try {
			//dbQuery를 준비한 ~.properties파일을 로딩해서 Properties 자료구조에 저장한다.
			//현재 프로젝트가 런타임(실행)될 때, 즉 서버가 실행될때 classes폴더의 위치를
			//동적으로 가져와서 경로를 설정해야한다.
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static RecipeDAO getInstance() {
		return instance;
	}
	
	@Override
	public List<RecipeDTO> selectAll() throws SQLException {
		List<RecipeDTO> list = new ArrayList<>();
		String sql = proFile.getProperty("recipe.selectAll");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long recipeId = rs.getLong(1);
					list.add(new RecipeDTO(
							recipeId, 
							rs.getLong(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6).toLowerCase().equals("base") ? 
									RecipeType.BASE : RecipeType.VARIANT,
							rs.getString(7),
							rs.getString(8),
							rs.getInt(9),
							rs.getString(10),
							ingredientDAO.selectByRecipeId(con, recipeId),
							stepDAO.selectByRecipeId(con, recipeId)
							));
					
				}
			}
		}
		
		return list;
	}
	
	@Override
	public RecipeDTO selectById(long recipeId) throws SQLException {
		RecipeDTO recipeDTO = null;
		String sql = proFile.getProperty("recipe.selectById");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, recipeId);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					recipeDTO = new RecipeDTO(
							rs.getLong(1), 
							rs.getLong(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6).toLowerCase().equals("base") ? 
									RecipeType.BASE : RecipeType.VARIANT,
							rs.getString(7),
							rs.getString(8),
							rs.getInt(9),
							rs.getString(10),
							ingredientDAO.selectByRecipeId(con, recipeId),
							stepDAO.selectByRecipeId(con, recipeId)
							);
					
				}
			}
		}
		
		return recipeDTO;
	}
	
	@Override
	public int insertRecipe(RecipeDTO recipeDTO) throws SQLException {
		int result = 0;
		String sql = proFile.getProperty("recipe.insertRecipe");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			//RETURN_GENERATED_KEYS 옵션을 사용하여 AUTO_INCREMENT 값을 가져온다
			con.setAutoCommit(false);	// 자동 커밋 해지
			
			ps.setLong(1, recipeDTO.getUserId());
			ps.setString(2, recipeDTO.getTitle());
			ps.setString(3, recipeDTO.getDescription());
			ps.setString(4, recipeDTO.getThumbnailUrl());
			ps.setString(5, recipeDTO.getWay());
			ps.setString(6, recipeDTO.getCategory());

			result = ps.executeUpdate();
			
			if(result == 0) {
				con.rollback();
				throw new SQLException("등록 실패...");
			}
			else {
				try (ResultSet rs = ps.getGeneratedKeys()) {	//ps.getGeneratedKeys()를 사용해 recipe_id 값을 획득한다.
					if(rs.next()) {
						recipeDTO.setRecipeId(rs.getLong(1));
					} else {
						con.rollback();
						throw new SQLException("recipe_id 생성 실패");
					}
				}
				
				int[] results = ingredientDAO.insertIngredients(con, recipeDTO.getRecipeId(), recipeDTO.getIngredients());
				for (int re : results) {
					if (re != 1) {
						con.rollback();
						throw new SQLException("레시피-재료 등록 실패");
					}
				}
				
				results = stepDAO.insertSteps(con, recipeDTO.getRecipeId(), recipeDTO.getSteps());
				for (int re : results) {
					if (re != 1) {
						con.rollback();
						throw new SQLException("레시피-조리법 등록 실패");
					}
				}
				
				con.commit();
			}
		}
		
		return result;
	}
}
