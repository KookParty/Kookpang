package kookparty.kookpang.controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dto.OrderDTO;
import kookparty.kookpang.service.OrderService;
import kookparty.kookpang.service.OrderServiceImpl;

public class OrderController implements Controller {
	OrderService orderService = new OrderServiceImpl();
	
	//임시 메서드. 마이페이지에서 써야할 메서드인듯..
	public ModelAndView order(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.getAttribute("login");
		long userId = 1;
		try {
			List<OrderDTO> list = orderService.selectByUserId(userId);
			request.setAttribute("orderList", list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ModelAndView();
	}
	
	public OrderDTO selectByOrderId(HttpServletRequest request, HttpServletResponse response) {
		long orderId = Long.parseLong(request.getParameter("orderId"));
		OrderDTO orderDTO = null;
		try {
			orderDTO = orderService.selectByOrderId(orderId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderDTO;
	}
	
	public void insertOrder(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.getAttribute("login");
		long userId = 1;
		int totlaPrice = Integer.parseInt(request.getParameter("totalPrice"));
		String shippingAddress = request.getParameter("shippingAddress");
		OrderDTO order = new OrderDTO(userId, totlaPrice, shippingAddress, null);
		try {
			int result = orderService.insertOrder(order);
			if(result==0) {//실패시
				
			}else {//성공시
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteOrder(HttpServletRequest request, HttpServletResponse response) {
		long orderId = Long.parseLong(request.getParameter("orderId"));
		try {
			int result = orderService.deleteOrder(orderId);
			if(result == 0) {
				
			}else {
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
