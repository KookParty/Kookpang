package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.common.RecipeType;
import kookparty.kookpang.common.TargetType;
import kookparty.kookpang.dto.LikeDTO;
import kookparty.kookpang.dto.RecipeDTO;
import kookparty.kookpang.util.DbUtil;
import kookparty.kookpang.util.PageCnt;

public class RecipeDAOImpl implements RecipeDAO {
	private final int pageSize = 12;
	private IngredientDAO ingredientDAO = IngredientDAOImpl.getInstance();
	private StepDAO stepDAO = StepDAOImpl.getInstance();
	private LikeDAO likeDAO = LikeDAOImpl.getInstance();
	
	private Properties proFile = new Properties();
	private static RecipeDAO instance = new RecipeDAOImpl();
	
	private RecipeDAOImpl() {
		try {
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
	public List<RecipeDTO> selectByOptions(String word, String category, String order, int pageNo) throws SQLException {
		List<RecipeDTO> list = new ArrayList<>();
		StringBuilder baseSql = new StringBuilder(proFile.getProperty("recipe.selectByOptions"));
		String limitOffset = proFile.getProperty("page.limitOffset");
		int index = 1;
		
		// 검색어, 카테고리, 정렬 조건들
		boolean wordCond = word != null && word.isEmpty() == false;
		boolean cateCond = "base".equals(category) || "variant".equals(category);
		//boolean likeCond = ;	// TODO 좋아요수
				
		
		// 검색어
		if(wordCond) {
			baseSql.append(" where upper(replace(title, ' ', '')) like upper(replace(?, ' ', ''))");
		}
		
		// 카테고리(기본/변형 선택했을 경우)
		if (cateCond) {
			baseSql.append(wordCond == false ? " where " : " and ");
			baseSql.append("recipe_type = ? ");
		}
		
		// 정렬 기준 (최신순/인기순)
		if (order == null || "recent".equals(order)) baseSql.append(" order by created_at desc");
		// else if ("like".equals(order)) baseSql.append(" order by  desc ");
		
		// 페이징
		if (pageNo != 0) baseSql.append(" " + limitOffset);
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(baseSql.toString())) {
			if (wordCond) 
				ps.setString(index++, "%" + word + "%");
			if (cateCond) 
				ps.setString(index++, category);
			if (pageNo != 0) {
				int countAll = countByOptions(con, word, category);
				PageCnt page = new PageCnt(countAll, pageSize, pageNo);
				ps.setInt(index++, page.getLimit());
				ps.setInt(index++, page.getOffset());
			}
				
			
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
							stepDAO.selectByRecipeId(con, recipeId),
							likeDAO.selectLikeCnt(con, new LikeDTO(TargetType.RECIPE, recipeId))
							));
				}
			}
		}
		
		return list;
	}
	
	private int countByOptions(Connection con, String word, String category) throws SQLException {
		int result = 0;
		StringBuilder baseSql = new StringBuilder(proFile.getProperty("recipe.count"));
		int index = 1;
		
		// 검색어, 카테고리, 정렬 조건들
		boolean wordCond = word != null && word.isEmpty() == false;
		boolean cateCond = "base".equals(category) || "variant".equals(category);
		//boolean likeCond = ;	// TODO 좋아요수
		
		if(wordCond) {
			baseSql.append(" where upper(replace(title, ' ', '')) like upper(replace(?, ' ', ''))");
		}
		if (cateCond) {
			baseSql.append(wordCond == false ? " where " : " and ");
			baseSql.append("recipe_type = ? ");
		}
		
		try (PreparedStatement ps = con.prepareStatement(baseSql.toString())) {
			if (wordCond) 
				ps.setString(index++, "%" + word + "%");
			if (cateCond) 
				ps.setString(index++, category);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = rs.getInt(1);
				}
			}
		}
		return result;
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
							stepDAO.selectByRecipeId(con, recipeId),
							likeDAO.selectLikeCnt(con, new LikeDTO(TargetType.RECIPE, recipeId))
							);
					
				}
			}
		}
		
		return recipeDTO;
	}
	
	@Override
	public List<RecipeDTO> selectVariantsByParentId(long parentRecipeId) throws SQLException {
		List<RecipeDTO> list = new ArrayList<>();
		String sql = proFile.getProperty("recipe.selectVariantsByParentId");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, parentRecipeId);
			
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
							stepDAO.selectByRecipeId(con, recipeId),
							likeDAO.selectLikeCnt(con, new LikeDTO(TargetType.RECIPE, recipeId))
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
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			//RETURN_GENERATED_KEYS 옵션을 사용하여 AUTO_INCREMENT 값을 가져온다
			con.setAutoCommit(false);	// 자동 커밋 해지
			
			ps.setLong(1, recipeDTO.getUserId());
			ps.setString(2, recipeDTO.getTitle());
			ps.setString(3, recipeDTO.getDescription());
			ps.setString(4, recipeDTO.getThumbnailUrl());
			ps.setString(5, recipeDTO.getRecipeType().toString().toLowerCase());
			ps.setString(6, recipeDTO.getWay());
			ps.setString(7, recipeDTO.getCategory());
			if (recipeDTO.getParentRecipeId() != 0)
				ps.setLong(8, recipeDTO.getParentRecipeId());
			else
				ps.setNull(8, Types.BIGINT);

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
				if (results != null) {
					for (int re : results) {
						if (re != 1) {
							con.rollback();
							throw new SQLException("레시피-재료 등록 실패");
						}
					}					
				}
				
				results = stepDAO.insertSteps(con, recipeDTO.getRecipeId(), recipeDTO.getSteps());
				if (results != null) {
					for (int re : results) {
						if (re != 1) {
							con.rollback();
							throw new SQLException("레시피-조리법 등록 실패");
						}
					}					
				}
				
				con.commit();
			}
		}
		
		return result;
	}
	
	@Override
	public int deleteRecipeByRecipeId(long recipeId) throws SQLException {
		int result = 0;
		String sql = proFile.getProperty("recipe.deleteRecipeByRecipeId");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, recipeId);
			
			result = ps.executeUpdate();
		}
		
		return result;
	}
}
