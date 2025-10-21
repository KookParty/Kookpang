package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.IngredientDTO;

public class IngredientDAOImpl implements IngredientDAO {
	private Properties proFile = new Properties();
	private static IngredientDAO instance = new IngredientDAOImpl();
	
	private IngredientDAOImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static IngredientDAO getInstance() {
		return instance;
	}

	@Override
	public List<IngredientDTO> selectByRecipeId(Connection con, long recipeId) throws SQLException {
		List<IngredientDTO> ingredients = new ArrayList<>();
		String sql = proFile.getProperty("ingredient.selectByRecipeId");
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, recipeId);
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ingredients.add(new IngredientDTO(
							rs.getLong(1), 
							rs.getLong(2), 
							rs.getLong(3), 
							rs.getString(4),
							rs.getString(5),
							rs.getInt(6)
							));
				}
			}
		}
		
		return ingredients;
	}
	
	@Override
	public int[] insertIngredients(Connection con, long recipeId, List<IngredientDTO> ingredients) throws SQLException {
		int[] result = null;
		String sql = proFile.getProperty("ingredient.insertIngredients");
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			for (IngredientDTO ingredientDTO : ingredients) {
				ps.setLong(1, recipeId);
				ps.setString(2, ingredientDTO.getName());
				ps.setString(3, ingredientDTO.getQuantity());

				//ps.executeUpdate();
				ps.addBatch();
				ps.clearParameters();
			}
			result = ps.executeBatch();	// 일괄처리
		}
		
		return result;
	}
}
