package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.ReviewDTO;
import kookparty.kookpang.util.DbUtil;

public class ReviewDAOImpl implements ReviewDAO {
	private Properties proFile = new Properties();
	private static ReviewDAO instance = new ReviewDAOImpl();
	
	private ReviewDAOImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ReviewDAO getInstance() {
		return instance;
	}
	
	@Override
	public List<ReviewDTO> selectByRecipeId(long recipeId) throws SQLException {
		List<ReviewDTO> reviews = new ArrayList<>();
		String sql = proFile.getProperty("review.selectByRecipeId");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, recipeId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					reviews.add(new ReviewDTO(
							rs.getLong(1), 
							rs.getLong(2),
							rs.getLong(3),
							rs.getInt(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7),
							rs.getString(8)
							));
				}
			}
		}
		return reviews;
	}
}
