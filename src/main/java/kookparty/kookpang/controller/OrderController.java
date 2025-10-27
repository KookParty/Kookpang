package kookparty.kookpang.controller;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dto.OrderDTO;
import kookparty.kookpang.dto.OrderItemDTO;
import kookparty.kookpang.dto.ResponseCartDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.exception.AuthenticationException;
import kookparty.kookpang.service.CartService;
import kookparty.kookpang.service.CartServiceImpl;
import kookparty.kookpang.service.OrderService;
import kookparty.kookpang.service.OrderServiceImpl;


public class OrderController implements Controller {
	OrderService orderService = new OrderServiceImpl();
	CartService cartService = new CartServiceImpl();
	
	//임시 메서드. 마이페이지에서 써야할 메서드인듯..
	public ModelAndView order(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = 0;
		if(user == null) {
			userId = 1;
		}else {
			userId = user.getUserId();
		}
		try {
			List<OrderDTO> list = orderService.selectByUserId(userId);
			request.setAttribute("orderList", list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ModelAndView();
	}
	
	public List<OrderDTO> getOrderListLimit(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = user.getUserId();
		
		return null;
	}
	
	
	public ModelAndView orderPage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		try {
			long userId = user.getUserId();
			List<ResponseCartDTO> cartList = cartService.selectByUserId(userId);
			int price = 0;
			int deliveryFee = 3000;
			for(ResponseCartDTO c : cartList) {
				price += c.getPrice() * c.getCount();
			}
			if(price >= 50000) {
				deliveryFee = 0;
			}
			
			int totalPrice = price + deliveryFee;
			request.setAttribute("list", cartList);
			request.setAttribute("name", user.getName());
			request.setAttribute("phone", user.getPhone());
			request.setAttribute("address", user.getAddress());
			request.setAttribute("point", user.getPoint());
			request.setAttribute("price", price);
			request.setAttribute("deliveryFee", deliveryFee);
			request.setAttribute("totalPrice", totalPrice);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ModelAndView("/orders/order-review.jsp");
	}
	
	public ModelAndView orderResult(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		
		
		long orderId = Long.parseLong(request.getParameter("order_id"));
		OrderDTO order = null;
		List<OrderItemDTO> list = null;
		try {
			long userId = user.getUserId();
			order = orderService.selectByOrderId(orderId);
			list = order.getItemList();
			if(userId != order.getUserId()) {
				throw new AuthenticationException("허가되지 않은 접근입니다.");
			}
			boolean timeout = false;
			String createdAtStr = order.getCreatedAt();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			LocalDateTime createdAt = LocalDateTime.parse(createdAtStr, formatter);
			LocalDateTime now = LocalDateTime.now();
			
			Duration duration = Duration.between(createdAt, now);
			
			if(duration.toHours() <= 3) {
				timeout = true;
			}
			
			int count = list.size();
			request.setAttribute("order", order);
			request.setAttribute("list", list);
			request.setAttribute("count", count);
			request.setAttribute("name", user.getNickname());
			request.setAttribute("phone", user.getPhone());
			request.setAttribute("timeout", timeout);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (AuthenticationException e) {
			e.printStackTrace();
			request.setAttribute("errMsg", e.getMessage());
			return new ModelAndView("/common/error.jsp");
		}
		return new ModelAndView("/orders/order-result.jsp");	
	}
	
	/*더이상 쓰지 않는 api. ajax로 비동기 처리해서 js로 렌더링 하려 했으나 orderResult라는 메서드로 jsp반환하게 변경
	public OrderDTO selectByOrderId(HttpServletRequest request, HttpServletResponse response) {
		long orderId = Long.parseLong(request.getParameter("orderId"));
		OrderDTO orderDTO = null;
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		try {
			long userId = user.getUserId();
			orderDTO = orderService.selectByOrderId(orderId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderDTO;
	}
	*/
	
	/* 더이상 쓰지 않는 api, payController로 기능 옮겨짐
	public void insertOrder(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = 0;
		if(user == null) {
			userId = 1;
		}else {
			userId = user.getUserId();
		}
		int totalPrice = Integer.parseInt(request.getParameter("totalPrice"));
		String shippingAddress = request.getParameter("shippingAddress");
		OrderDTO order = new OrderDTO(userId, totalPrice, shippingAddress, null);
		try {
			int result = orderService.insertOrder(order);
			if(result==0) {//실패시
				
			}else {//성공시
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/
	
	public ModelAndView deleteOrder(HttpServletRequest request, HttpServletResponse response) {
		long orderId = Long.parseLong(request.getParameter("order_id"));
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		try {
			long userId = user.getUserId();//검증메서드 추가 예정
			int result = orderService.deleteOrder(user, orderId);
			if(result == 0) {
				
			}else {
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView(request.getContextPath() + "/front?key=order&methodName=orderResult&order_id=" + orderId, true);
	}
}
