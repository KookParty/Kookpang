package kookparty.kookpang.service;

import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.dto.ResponseCartDTO;
import kookparty.kookpang.exception.AuthenticationException;

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
	 * 장바구니 목록 1개 삭제. 삭제하지 못했을 시 AuthenticationException 
	 * @param cartId
	 * @param userId
	 * @return
	 * @throws SQLException 
	 * @throws AuthenticationException 
	 */
	int deleteCartByCartId(long cartId, long userId) throws SQLException, AuthenticationException;
	
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
	 * 물품 중복체크
	 * @param userId
	 * @param productId
	 * @param count
	 * @return
	 * @throws SQLException
	 */
	int updateCartCount(long userId, long productId, int count) throws SQLException;
	
	/**
	 * header.jsp의 카트아이콘 위 숫자표시를 위해 carts 테이블에 들어있는 레코드 숫자를 가져오는 메서드
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	int countCart(long userId) throws SQLException;
	
	
	/**
	 * 중복체크를 위해 userId, productId로 cartDTO를 select하는 메서드. 중복이면 DTO반환... 메서드 좀 꼬임
	 * @param userId
	 * @param productId
	 * @return
	 * @throws SQLException
	 */
	CartDTO duplicateCheck(long userId, long productId) throws SQLException;
}
