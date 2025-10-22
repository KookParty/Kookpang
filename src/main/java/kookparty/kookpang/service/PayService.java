package kookparty.kookpang.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletResponse;
import kookparty.kookpang.dto.PaymentDTO;

public interface PayService {
	Map<String, Object> payReady(String userNickName, long user_id) throws SQLException, MalformedURLException, IOException;
	
	void payApprove(PaymentDTO paymentDTO) throws IOException;
}
