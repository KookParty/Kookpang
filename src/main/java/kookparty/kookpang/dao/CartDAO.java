package kookparty.kookpang.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.CartDTO;

public interface CartDAO {
	/**
	 * userId로 user의 장바구니 목록 불러오기
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	List<CartDTO> selectByUserId(long userId) throws SQLException;
	
	/**
	 * 트랜잭션용 메서드
	 * @param userId
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	List<CartDTO> selectByUserId(long userId, Connection con) throws SQLException;
	
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
	 * @throws SQLException 
	 */
	int deleteCartByCartId(long cartId, long userId) throws SQLException;
	
	/**
	 * 유저의 장바구니 목록 전체 삭제
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	int deleteCartByUserId(long userId) throws SQLException;
	
	/**
	 * 장바구니 물품 수량 수정
	 * @param cartDTO
	 * @return
	 * @throws SQLException 
	 */
	int updateCartCount(CartDTO cartDTO) throws SQLException;
	
	/**
	 * 장바구니 수량 구하는 메서드
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	int countCart(long userId) throws SQLException;
	
	/**
	 * userId와 productId로 carts테이블의 레코드 하나를 select하는 메서드
	 * @param userId
	 * @param productId
	 * @return
	 * @throws SQLException
	 */
	CartDTO selectByUserIdAndProductId(long userId, long productId) throws SQLException;
}
