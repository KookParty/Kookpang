package kookparty.kookpang.dao;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import kookparty.kookpang.dto.ChartDataDTO;
import kookparty.kookpang.dto.OrderDTO;
import kookparty.kookpang.dto.OrderItemDTO;
import kookparty.kookpang.dto.PaymentDTO;
import kookparty.kookpang.util.DbUtil;

public class OrderDAOImpl implements OrderDAO {
	private Properties proFile = new Properties();

	public OrderDAOImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
			proFile.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<OrderDTO> selectByUserId(long userId) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderDTO> list = new ArrayList<OrderDTO>();
		String sql = proFile.getProperty("order.selectByUserId");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, userId);
			rs = ps.executeQuery();
			OrderDTO order = null;
			while (rs.next()) {
				order = new OrderDTO(rs.getLong("order_id"),
						rs.getLong("user_id"), 
						rs.getString("created_at"),
						rs.getInt("total_price"), 
						rs.getInt("delivery_fee"),
						rs.getString("shipping_address"), 
						rs.getString("order_name"),
						rs.getString("cid"),
						rs.getString("tid"),
						rs.getString("partner_order_id"),
						rs.getString("partner_user_id"),
						rs.getString("pg_token"),
						rs.getInt("status") == 1, null);
				list.add(order);
			}
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return list;
	}

	@Override
	public List<OrderDTO> selectByUserIdLimit(long userId, int limit, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderDTO selectByOrderId(long orderId) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OrderDTO order = null;
		String sql = proFile.getProperty("order.selectByOrderId");
		try {
			con = DbUtil.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
			ps.setLong(1, orderId);
			rs = ps.executeQuery();
			if (rs.next()) {
				order = new OrderDTO(rs.getLong("order_id"),
						rs.getLong("user_id"), 
						rs.getString("created_at"),
						rs.getInt("total_price"), 
						rs.getInt("delivery_fee"),
						rs.getString("shipping_address"), 
						rs.getString("order_name"),
						rs.getString("cid"),
						rs.getString("tid"),
						rs.getString("partner_order_id"),
						rs.getString("partner_user_id"),
						rs.getString("pg_token"),
						rs.getInt("status") == 1, null);
				List<OrderItemDTO> itemlist = selectItemsByOrderId(con, orderId);
				order.setItemList(itemlist);
			}
			con.commit();
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return order;
	}

	@Override
	public long insertOrder(OrderDTO order, PaymentDTO paymentDTO) throws SQLException {
		Connection con = null;
		long pk = 0;
		try {
			con = DbUtil.getConnection();
			pk = insertOrder(order, paymentDTO, con);
		} finally {
			DbUtil.dbClose(con, null);
		}
		return pk;
	}
	
	

	@Override
	public long insertOrder(OrderDTO order, PaymentDTO paymentDTO, Connection con) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		long pk = 0;
		String sql = proFile.getProperty("order.insertOrder");
		
		try {
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, order.getUserId());
			ps.setInt(2, order.getTotalPrice());
			ps.setInt(3, order.getDeliveryFee());
			ps.setString(4, order.getShippingAddress());
			ps.setString(5, paymentDTO.getOrderName());
			ps.setString(6, paymentDTO.getCid());
			ps.setString(7, paymentDTO.getTid());
			ps.setString(8, paymentDTO.getPartnerOrderId());
			ps.setString(9, paymentDTO.getPartnerUserId());
			ps.setString(10, paymentDTO.getPgToken());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next()) {
				pk = rs.getLong(1);
			}
		} finally {
			DbUtil.dbClose(null, ps, rs);
		}
		return pk;
	}

	@Override
	public int deleteOrder(long orderId) throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("order.deleteOrder");

		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, orderId);
			result = ps.executeUpdate();
		} finally {
			DbUtil.dbClose(con, ps);
		}
		return result;
	}

	private List<OrderItemDTO> selectItemsByOrderId(Connection con, long orderId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OrderItemDTO> list = new ArrayList<OrderItemDTO>();
		String sql = proFile.getProperty("order.selectItemsByOrderId");

		try {
			ps=con.prepareStatement(sql);
			ps.setLong(1, orderId);
			rs = ps.executeQuery();
			OrderItemDTO item = null;
			while (rs.next()) {
				item = new OrderItemDTO(rs.getLong("id"), rs.getLong("order_id"), rs.getLong("product_id"),
						rs.getInt("count"), rs.getInt("price"));
				list.add(item);
			}
		} finally {
			DbUtil.dbClose(null, ps);
		}

		return list;
	}
	
	public int insertOrderItems(Connection con, List<OrderItemDTO> list) throws SQLException {
		PreparedStatement ps = null;
		int result = 0;
		String sql = proFile.getProperty("order.insertOrderItem");
		
		try {
			ps = con.prepareStatement(sql);	
			for(OrderItemDTO item : list) {
				ps.setLong(1, item.getOrderId());
				ps.setLong(2, item.getProductId());
				ps.setInt(3, item.getCount());
				ps.setInt(4, item.getPrice());
				ps.addBatch();
			}
			int[] executeBatch = ps.executeBatch();
			for(int i : executeBatch) {
				result += i;
			}
		} finally {
			DbUtil.dbClose(null, ps);
		}
		
		
		return result;
	}

	@Override
	public ChartDataDTO getDailySales() throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ChartDataDTO chartData = new ChartDataDTO();
		String sql = proFile.getProperty("order.selectDailySales");
		
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			List<String> chartLabels = new ArrayList<String>();
			List<Integer> chartDatas = new ArrayList<Integer>();
			while(rs.next()) {
				chartLabels.add(rs.getString("order_date"));
				chartDatas.add(rs.getInt("daily_sales"));
			}
			chartData.setChartDatas(chartDatas);
			chartData.setChartLabels(chartLabels);
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		
		return chartData;
	}

	@Override
	public ChartDataDTO getBestItems() throws SQLException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ChartDataDTO chartData = new ChartDataDTO();
		String sql = proFile.getProperty("order.countBestItems");
		
		try {
			con = DbUtil.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			List<String> chartLabels = new ArrayList<String>();
			List<Integer> chartDatas = new ArrayList<Integer>();
			while(rs.next()) {
				chartLabels.add(rs.getString("name"));
				chartDatas.add(rs.getInt("total_sold"));
			}
			chartData.setChartDatas(chartDatas);
			chartData.setChartLabels(chartLabels);
		} finally {
			DbUtil.dbClose(con, ps, rs);
		}
		return chartData;
	}
	
	
}
