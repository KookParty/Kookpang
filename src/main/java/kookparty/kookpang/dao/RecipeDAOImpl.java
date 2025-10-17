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
	private Properties proFile = new Properties();
	
	// singleton
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
					list.add(new RecipeDTO(
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
							rs.getString(10)
							));
				}
			}
		}
		
		return list;
	}
	
	@Override
	public int insertRecipe(RecipeDTO recipeDTO) throws SQLException {
		int result = 0;
		String sql = proFile.getProperty("recipe.insertRecipe");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, recipeDTO.getUserId());
			ps.setString(2, recipeDTO.getTitle());
			ps.setString(3, recipeDTO.getDescription());
			ps.setString(4, recipeDTO.getThumbnailUrl());
			ps.setString(5, recipeDTO.getWay());
			ps.setString(6, recipeDTO.getCategory());

			result = ps.executeUpdate();
		}
		
		return result;
	}
}
