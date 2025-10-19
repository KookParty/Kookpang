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
import kookparty.kookpang.dto.ProductDTO;
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CartDTO> cartList = new ArrayList<CartDTO>();
		String sql = proFile.getProperty("cart.selectByUserId");
		System.out.println("test");
		
		try {
			con = DbUtil.getConnection();
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
			DbUtil.dbClose(con, ps, rs);
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
	public int deleteCartByCardId(long cartId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteCartByUserId(long userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateCartCount(CartDTO cartDTO) {
		// TODO Auto-generated method stub
		return 0;
	}

}
