package kookparty.kookpang.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kookparty.kookpang.dto.PaymentDTO;
import kookparty.kookpang.dto.ResponseCartDTO;

public class PayServiceImpl implements PayService {
	CartService cartService = new CartServiceImpl();

	private Properties proFile = new Properties();

	public PayServiceImpl() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("kakaoPayMapping.properties");
			proFile.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> payReady(String userNickName, long userId)
			throws SQLException, IOException {
		Gson gson = new Gson();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ResponseCartDTO> cartList = cartService.selectByUserId(userId);
		int amount = 0;
		int deliveryFee = 3000;
		for (ResponseCartDTO c : cartList) {
			amount += c.getCount() * c.getPrice();
		}
		if(amount >= 50000) {
			deliveryFee = 0;
		}
		String secretKey = proFile.getProperty("secretKey");
		String itemName = cartList.get(0).getName() + " 외 " + (cartList.size()-1) + "품목";
		String cid = "TC0ONETIME";
		String partnerOrderId = "order-" + System.currentTimeMillis();
		String partnerUserId = userNickName;
		
		URL url = new URL("https://open-api.kakaopay.com/online/v1/payment/ready");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "SECRET_KEY "+secretKey);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		JsonObject body = new JsonObject();
		body.addProperty("cid", cid);
		body.addProperty("partner_order_id", partnerOrderId);
		body.addProperty("partner_user_id", partnerUserId);
		body.addProperty("item_name", itemName);
		body.addProperty("quantity", 1);
		body.addProperty("total_amount", amount + deliveryFee);
		body.addProperty("tax_free_amount", 0);
		body.addProperty("approval_url", "http://localhost:8080/Kookpang/ajax?key=pay&methodName=paySuccess");
		body.addProperty("cancel_url", "http://localhost:8080/Kookpang/front?key=order&methodName=orderPage");
		body.addProperty("fail_url", "http://localhost:8080/Kookpang/front?key=order&methodName=orderPage");

		try (OutputStream os = conn.getOutputStream()) {
			os.write(gson.toJson(body).getBytes("UTF-8"));
		}

		InputStream is = conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream();
		String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining());
		System.out.println(result);
		JsonObject kakaoRes = gson.fromJson(result, JsonObject.class);
		PaymentDTO paymentDTO = new PaymentDTO(itemName, cid, partnerOrderId, partnerUserId, amount, deliveryFee);
		resultMap.put("payment", paymentDTO);
		resultMap.put("result", kakaoRes);
		return resultMap;
	}

	@Override
	public void payApprove(PaymentDTO paymentDTO) throws IOException {
		Gson gson = new Gson();
		
		String secretKey = proFile.getProperty("secretKey");
		URL url = new URL("https://open-api.kakaopay.com/online/v1/payment/approve");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "SECRET_KEY "+secretKey);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		
		JsonObject body = new JsonObject();
		body.addProperty("cid", paymentDTO.getCid());
		body.addProperty("tid", paymentDTO.getTid());
		body.addProperty("partner_order_id", paymentDTO.getPartnerOrderId());
		body.addProperty("partner_user_id", paymentDTO.getPartnerUserId());
		body.addProperty("pg_token", paymentDTO.getPgToken());
		
		try (OutputStream os = conn.getOutputStream()) {
			os.write(gson.toJson(body).getBytes("UTF-8"));
		}
		InputStream is = conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream();
		String result = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining());
		System.out.println(result);
	}

}
