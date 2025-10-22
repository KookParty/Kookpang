package kookparty.kookpang.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kookparty.kookpang.dao.CartDAO;
import kookparty.kookpang.dao.CartDAOImpl;
import kookparty.kookpang.dao.OrderDAO;
import kookparty.kookpang.dao.OrderDAOImpl;
import kookparty.kookpang.dao.ProductDAO;
import kookparty.kookpang.dao.ProductDAOImpl;
import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.dto.OrderDTO;
import kookparty.kookpang.dto.OrderItemDTO;
import kookparty.kookpang.dto.PaymentDTO;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.util.DbUtil;

public class OrderServiceImpl implements OrderService {
	OrderDAO orderDAO = new OrderDAOImpl();
	ProductDAO productDAO = new ProductDAOImpl();
	CartDAO cartDAO = new CartDAOImpl();

	@Override
	public List<OrderDTO> selectByUserId(long userId) throws SQLException {
		List<OrderDTO> list = orderDAO.selectByUserId(userId);
		return list;
	}

	@Override
	public List<OrderDTO> selectByUserIdLimit(long userId, int limit, int offset) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderDTO selectByOrderId(long orderId) throws SQLException {
		OrderDTO order = orderDAO.selectByOrderId(orderId);
		List<OrderItemDTO> itemList = order.getItemList();
		for(OrderItemDTO i : itemList) {
			long productId = i.getProductId();
			ProductDTO product = productDAO.selectByProductId(productId);
			i.setName(product.getName());
		}
		return order;
	}

	@Override
	public long insertOrder(OrderDTO order, PaymentDTO paymentDTO) throws SQLException {
		Connection con = null;
		long pk = 0;
		try {
			con = DbUtil.getConnection();
			con.setAutoCommit(false);
			pk = orderDAO.insertOrder(order, paymentDTO, con);
			List<CartDTO> cartList = cartDAO.selectByUserId(order.getUserId(), con);
			List<OrderItemDTO> itemList = new ArrayList<OrderItemDTO>();
			for(CartDTO c : cartList) {
				ProductDTO product = productDAO.selectByProductId(c.getProductId(), con);
				OrderItemDTO itemDTO = new OrderItemDTO(pk, 
						product.getProductId(), 
						c.getCount(), 
						product.getPrice());
				itemList.add(itemDTO);
			}
			orderDAO.insertOrderItems(con, itemList);
			cartDAO.deleteCartByUserId(order.getUserId());
			
			con.commit();
		} finally {
			DbUtil.dbClose(con, null);
		}
		return pk;
	}

	@Override
	public int deleteOrder(long orderId) throws SQLException {
		int result = orderDAO.deleteOrder(orderId);
		return result;
	}
	

}
