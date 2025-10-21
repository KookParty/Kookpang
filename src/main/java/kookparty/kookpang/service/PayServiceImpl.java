package kookparty.kookpang.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletResponse;
import kookparty.kookpang.dao.CartDAO;
import kookparty.kookpang.dao.CartDAOImpl;
import kookparty.kookpang.dao.UserDAO;
import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.dto.ResponseCartDTO;

public class PayServiceImpl implements PayService {
	CartService cartService = new CartServiceImpl();
	@Override
	public JsonObject payReady(String userNickName, long userId, HttpServletResponse response) throws SQLException, IOException {
		Gson gson = new Gson();
		
		List<ResponseCartDTO> cartList = cartService.selectByUserId(userId);
		int amount = 0;
		for(ResponseCartDTO c : cartList) {
			amount += c.getCount() * c.getPrice();
		}
		
		String itemName = cartList.get(0).getName() + " 외 " + cartList.size() + "품목";
		
		System.out.println(amount);
		System.out.println(itemName);
		
		URL url = new URL("https://open-api.kakaopay.com/online/v1/payment/ready");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "SECRET_KEY DEV4CDDC760E95969323DFCDAF0FE067316D0CC1");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		
		JsonObject body = new JsonObject();
		body.addProperty("cid", "TC0ONETIME");
		body.addProperty("partner_order_id", "order-" + System.currentTimeMillis());
		body.addProperty("partner_user_id", userNickName);
		body.addProperty("item_name", itemName);
		body.addProperty("quantity", 1);
		body.addProperty("total_amount", amount);
		body.addProperty("tax_free_amount", 0);
		body.addProperty("approval_url", "http://localhost:8080/Kookpang/orders/order-result.jsp");
		body.addProperty("cancel_url", "http://localhost:8080/Kookpang/front?key=order&methodName=orderPage");
		body.addProperty("fail_url", "http://localhost:8080/Kookpang/front?key=order&methodName=orderPage");
		
		try (OutputStream os = conn.getOutputStream()){
			os.write(gson.toJson(body).getBytes("UTF-8"));
		}
		
		InputStream is = conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream();
		String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining());
		System.out.println(result);
		JsonObject kakaoRes = gson.fromJson(result, JsonObject.class);
		return kakaoRes;
	}
	
}
