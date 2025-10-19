package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.dto.ResponseCartDTO;

public interface CartService {
	/**
	 * userId로 user의 장바구니 목록 불러오기
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	List<ResponseCartDTO> selectByUserId(long userId) throws SQLException;
	
	/**
	 * 장바구니에 product 담기
	 * @param cartDTO
	 * @return
	 * @throws SQLException 
	 */
	int insertCart(CartDTO cartDTO) throws SQLException;
	
	/**
	 * 장바구니 목록 1개 삭제
	 * @param cartId
	 * @return
	 */
	int deleteCartByCardId(long cartId);
	
	/**
	 * 유저의 장바구니 목록 전체 삭제
	 * @param userId
	 * @return
	 */
	int deleteCartByUserId(long userId);
	
	/**
	 * 장바구니 물품 수량 수정
	 * @param cartDTO
	 * @return
	 */
	int updateCartCount(CartDTO cartDTO);
}
