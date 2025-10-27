package kookparty.kookpang.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dto.OrderDTO;
import kookparty.kookpang.dto.PaymentDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.OrderService;
import kookparty.kookpang.service.OrderServiceImpl;
import kookparty.kookpang.service.PayService;
import kookparty.kookpang.service.PayServiceImpl;

public class PayController implements Controller {
	PayService payService = new PayServiceImpl();
	OrderService orderService = new OrderServiceImpl();

	public JsonObject payReady(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("loginUser");
		String shippingAddress = request.getParameter("address");

		JsonObject payReady = null;
		PaymentDTO paymentDTO = null;
		try {
			Map<String, Object> resultMap = payService.payReady(user.getNickname(), user.getUserId());
			payReady = (JsonObject) resultMap.get("result");
			paymentDTO = (PaymentDTO) resultMap.get("payment");
			String tid = payReady.get("tid").getAsString();
			paymentDTO.setTid(tid);
			session.setAttribute("payment", paymentDTO);
			session.setAttribute("address", shippingAddress);

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return payReady;
	}

	public void paySuccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String pgToken = request.getParameter("pg_token");
		HttpSession session = request.getSession();
		PaymentDTO paymentDTO = (PaymentDTO) session.getAttribute("payment");
		paymentDTO.setPgToken(pgToken);
		payService.payApprove(paymentDTO);

		UserDTO user = (UserDTO) session.getAttribute("loginUser");
		long userId = user.getUserId();
		String shippingAddress = (String) session.getAttribute("address");
		int totalAmount = paymentDTO.getTotalAmount();
		OrderDTO order = new OrderDTO(userId, paymentDTO.getTotalAmount(), paymentDTO.getDeliveryFee(), shippingAddress, null);
		System.out.println("order : " + order);
		long pk = 0;
		try {
			pk = orderService.insertOrder(order, paymentDTO);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html><head></head><body>");
		out.println("<script>");
		out.println("window.opener.postMessage('"+pk+"', '*');");
		out.println("window.close();");
		out.println("</script>");
		out.println("</body></html>");
	}

}
