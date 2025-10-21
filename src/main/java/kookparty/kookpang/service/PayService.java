package kookparty.kookpang.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletResponse;

public interface PayService {
	JsonObject payReady(String userNickName, long user_id, HttpServletResponse response) throws SQLException, MalformedURLException, IOException;
}
