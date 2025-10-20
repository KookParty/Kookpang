package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.IngredientDTO;
import kookparty.kookpang.util.DbUtil;

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
