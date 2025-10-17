package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

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
			System.out.println("query.select = " +proFile.getProperty("recipe.select"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static RecipeDAO getInstance() {
		return instance;
	}
	
	@Override
	public int insertRecipe(RecipeDTO recipDTO) throws SQLException {
		int result = 0;
		String sql = proFile.getProperty("recipe.insertRecipe");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, recipDTO.getUserId());
			ps.setString(2, recipDTO.getTitle());
			ps.setString(3, recipDTO.getDescription());
			ps.setString(4, recipDTO.getWay());
			ps.setString(5, recipDTO.getCategory());
			
			result = ps.executeUpdate();
		}
		
		return result;
	}
}
