package kookparty.kookpang.controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.dto.ResponseCartDTO;
import kookparty.kookpang.service.CartService;
import kookparty.kookpang.service.CartServiceImpl;

public class CartController implements Controller{
	private final CartService cartService = new CartServiceImpl();
	
	public ModelAndView cart(HttpServletRequest request, HttpServletResponse response) {
		
		return new ModelAndView();
	}
	
	public List<ResponseCartDTO> selectByUserId(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		session.getAttribute("users"); //세션에서 userId가지고 올 예비용 메서드
		long userId = 1; //임시로 등록한 userId 1번을 가지고 옴
		List<ResponseCartDTO> list = null;
		try {
			list = cartService.selectByUserId(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("test2");
		for (ResponseCartDTO responseCartDTO : list) {
			System.out.println(responseCartDTO);
		}
		
		return list;
	}
	
	public void insertCart(HttpServletRequest request, HttpServletResponse response) {
		long productId = Long.parseLong(request.getParameter("productId"));
		int count = Integer.parseInt(request.getParameter("count"));
		System.out.println(productId + count);
		HttpSession session = request.getSession();
		session.getAttribute("users"); //세션에서 userId가지고 올 예비용 메서드
		long userId = 1;
		CartDTO cartDTO = new CartDTO(userId, productId, count);
		try {
			int result = cartService.insertCart(cartDTO);
			if(result == 0) {//실패시
				
			}else {//성공시
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
