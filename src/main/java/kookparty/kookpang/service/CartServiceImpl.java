package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kookparty.kookpang.dao.CartDAO;
import kookparty.kookpang.dao.CartDAOImpl;
import kookparty.kookpang.dao.ProductDAO;
import kookparty.kookpang.dao.ProductDAOImpl;
import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.dto.ResponseCartDTO;
import kookparty.kookpang.exception.AuthenticationException;

public class CartServiceImpl implements CartService {
	CartDAO cartDAO = new CartDAOImpl();
	ProductDAO productDAO = new ProductDAOImpl();

	@Override
	public List<ResponseCartDTO> selectByUserId(long userId) throws SQLException {
		List<CartDTO> cartList = cartDAO.selectByUserId(userId);
		List<ResponseCartDTO> list = new ArrayList<ResponseCartDTO>();
		for (CartDTO c : cartList) {
			long productId = c.getProductId();
			ProductDTO productDTO = productDAO.selectByProductId(productId);
			ResponseCartDTO rcDto = new ResponseCartDTO(c.getCartId(), productDTO.getProductId(), productDTO.getName(),
					productDTO.getPrice(), c.getCount(), productDTO.getImageUrl());
			list.add(rcDto);
		}
		return list;
	}

	@Override
	public int insertCart(CartDTO cartDTO) throws SQLException {
		int result = cartDAO.insertCart(cartDTO);
		return result;
	}

	@Override
	public int deleteCartByCartId(long cartId, long userId) throws SQLException, AuthenticationException {
		int result = cartDAO.deleteCartByCartId(cartId, userId);
		if(result == 0) {
			throw new AuthenticationException("허가되지 않은 접근입니다.");
		}
		return result;
	}

	@Override
	public int deleteCartByUserId(long userId) throws SQLException {
		int result = cartDAO.deleteCartByUserId(userId);
		return result;
	}

	@Override
	public int updateCartCount(CartDTO cartDTO) throws SQLException {
		int result = cartDAO.updateCartCount(cartDTO);
		return result;
	}

	@Override
	public int updateCartCount(long userId, long productId, int count) throws SQLException {
		CartDTO cart = cartDAO.selectByUserIdAndProductId(userId, productId);
		cart.setCount(count);
		int result = cartDAO.updateCartCount(cart);
		return result;
	}

	@Override
	public int countCart(long userId) throws SQLException {
		int result = cartDAO.countCart(userId);
		return result;
	}

	public CartDTO duplicateCheck(long userId, long productId) throws SQLException {

		CartDTO cart = cartDAO.selectByUserIdAndProductId(userId, productId);

		return cart;
	}

}
