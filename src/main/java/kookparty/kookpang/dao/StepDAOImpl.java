package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.IngredientDTO;
import kookparty.kookpang.dto.StepDTO;

public class StepDAOImpl implements StepDAO {
	private Properties proFile = new Properties();
	private static StepDAO instance = new StepDAOImpl();
	
	private StepDAOImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static StepDAO getInstance() {
		return instance;
	}
	
	@Override
	public int[] insertSteps(Connection con, long recipeId, List<StepDTO> steps) throws SQLException {
		int[] result = null;
		String sql = proFile.getProperty("step.insertSteps");
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			for (int i = 0; i < steps.size(); i++) {
				ps.setLong(1, recipeId);
				ps.setInt(2, i);
				ps.setString(3, steps.get(i).getDescription());
				ps.setString(4, steps.get(i).getImageUrl());

				//ps.executeUpdate();
				ps.addBatch();
				ps.clearParameters();
			}
			result = ps.executeBatch();	// 일괄처리
		}
		
		return result;
	}
}
