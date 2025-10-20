package kookparty.kookpang.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.util.DbUtil;

public class CartDAOImpl implements CartDAO {
	private Properties proFile = new Properties();
	
	public CartDAOImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<CartDTO> selectByUserId(long userId) throws SQLException{
		Connection con = null;
		List<CartDTO> cartList = null;
		
		try {
			con = DbUtil.getConnection();
			cartList = selectByUserId(userId, con);
		} finally {
			DbUtil.dbClose(con, null, null);
		}
		
		return cartList;
	}
	
	
	
	@Override
	public List<CartDTO> selectByUserId(long userId, Connection con) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CartDTO> cartList = new ArrayList<CartDTO>();
		String sql = proFile.getProperty("cart.selectByUserId");
		try {
			ps = con.prepareStatement(sql);
			ps.setLong(1, userId);
			rs = ps.executeQuery();
			while(rs.next()) {
				CartDTO cartDTO = new CartDTO(rs.getLong("cart_id"),
						rs.getLong("user_id"),
						rs.getLong("product_id"),
						rs.getInt("count"),
						rs.getString("created_at"));
				cartList.add(cartDTO);
			}
		} finally {
			DbUtil.dbClose(null, ps, rs);
		}
		
		return cartList;
	}

	@Override
	public int insertCart(CartDTO cartDTO) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("cart.insertCart");
		
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, cartDTO.getUserId());
			ps.setLong(2, cartDTO.getProductId());
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	@Override
	public int deleteCartByCartId(long cartId) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("cart.deleteCartByCartId");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, cartId);
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	@Override
	public int deleteCartByUserId(long userId) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("cart.deleteCartByCardId");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, userId);
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
		
	}

	@Override
	public int updateCartCount(CartDTO cartDTO) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("cart.updateCartCount");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cartDTO.getCount());
			ps.setLong(2, cartDTO.getCartId());
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
		
	}

	@Override
	public int countCart(long userId) throws SQLException{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		String sql = proFile.getProperty("cart.countCart");
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, userId);
			rs = ps.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			System.out.println(result);
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return result;
	}

}
