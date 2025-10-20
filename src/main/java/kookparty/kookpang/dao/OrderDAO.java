package kookparty.kookpang.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kookparty.kookpang.dto.OrderDTO;
import kookparty.kookpang.dto.OrderItemDTO;

public interface OrderDAO {
	/**
	 * 한 유저의 결제내역 불러오기
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	List<OrderDTO> selectByUserId(long userId) throws SQLException;
	
	/**
	 * 한 유저의 결제내역을 특정개수만 불러오기
	 * @param userId
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<OrderDTO> selectByUserIdLimit(long userId, int limit, int offset);
	
	/**
	 * 결제번호로 하나의 주문내역 불러오기
	 * @param orderId
	 * @return
	 * @throws SQLException 
	 */
	OrderDTO selectByOrderId(long orderId) throws SQLException;
	
	/**
	 * userId로 장바구니와 유저정보를 불러오고 장바구니 id로 product정보를 불러와서
	 * order테이블과, order-items테이블에 정보를 insert함
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	long insertOrder(OrderDTO order) throws SQLException;
	
	/**
	 * 트랜잭션용 메서드
	 * @param order
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	long insertOrder(OrderDTO order, Connection con) throws SQLException;
	
	/**
	 * orderId로 주문을 삭제. 삭제하면 on delete cascade로 order-items의 레코드도 삭제
	 * @param orderId
	 * @return
	 * @throws SQLException 
	 */
	int deleteOrder(long orderId) throws SQLException;
	
	int insertOrderItems(Connection con, List<OrderItemDTO> list) throws SQLException;
}
