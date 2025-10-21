package kookparty.kookpang.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.PayService;
import kookparty.kookpang.service.PayServiceImpl;

public class PayController implements Controller {
	PayService payService = new PayServiceImpl();
	
	public JsonObject payReady(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		JsonObject payReady = null;
		try {
			payReady = payService.payReady(user.getNickname(), user.getUserId(), response);
			
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return payReady;
	}
}
