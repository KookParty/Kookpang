package kookparty.kookpang.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.CartDTO;

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
	public List<CartDTO> selectByUserId(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertCart(CartDTO cartDTO) {
		
		return 0;
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
