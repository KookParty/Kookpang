package kookparty.kookpang.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import kookparty.kookpang.dto.LikeDTO;
import kookparty.kookpang.util.DbUtil;

public class LikeDAOImpl implements LikeDAO {
	private Properties proFile = new Properties();
	private static LikeDAO instance = new LikeDAOImpl();
	
	private LikeDAOImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static LikeDAO getInstance() {
		return instance;
	}
	
	@Override
	public boolean isLiked(LikeDTO likeDTO) throws SQLException {
		boolean result = false;
		String sql = proFile.getProperty("like.isLiked");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, likeDTO.getUserId());
			ps.setString(2, likeDTO.getTargetType().toString());
			ps.setLong(3, likeDTO.getTargetId());
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = true;
				}
			}
		}
		return result;
	}
	
	@Override
	public int insertLike(LikeDTO likeDTO) throws SQLException {
		int result = 0;
		String sql = proFile.getProperty("like.insertLike");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, likeDTO.getUserId());
			ps.setString(2, likeDTO.getTargetType().toString());
			ps.setLong(3, likeDTO.getTargetId());
			
			result = ps.executeUpdate();
		}
		return result;
	}
	
	@Override
	public int deleteLike(LikeDTO likeDTO) throws SQLException {
		int result = 0;
		String sql = proFile.getProperty("like.deleteLike");
		
		try (Connection con = DbUtil.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, likeDTO.getUserId());
			ps.setString(2, likeDTO.getTargetType().toString());
			ps.setLong(3, likeDTO.getTargetId());
			
			result = ps.executeUpdate();
		}
		return result;
	}
	
	@Override
	public int selectLikeCnt(Connection con, LikeDTO likeDTO) throws SQLException {
		int result = 0;
		String sql = proFile.getProperty("like.selectLikeCnt");
		
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, likeDTO.getTargetType().toString());
			ps.setLong(2, likeDTO.getTargetId());
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = rs.getInt(1);
				}
			}
		}
		return result;
	}
}
