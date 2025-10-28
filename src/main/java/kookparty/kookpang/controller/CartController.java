package kookparty.kookpang.controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dto.CartDTO;
import kookparty.kookpang.dto.ResponseCartDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.CartService;
import kookparty.kookpang.service.CartServiceImpl;

public class CartController implements Controller {
	private final CartService cartService = new CartServiceImpl();

	public ModelAndView cart(HttpServletRequest request, HttpServletResponse response) {
		
		return new ModelAndView("/orders/cart.jsp");
	}
	
	/**
	 * 세션에 저장된 userId를 통해 cartDTO
	 * @param request
	 * @param response
	 * @return
	 */
	public List<ResponseCartDTO> selectByUserId(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		List<ResponseCartDTO> list = null;
		try {
			long userId = user.getUserId();
			list = cartService.selectByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int countCart(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		int result = 0;

		try {
			long userId = user.getUserId();
			result = cartService.countCart(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void insertCart(HttpServletRequest request, HttpServletResponse response) {
		long productId = Long.parseLong(request.getParameter("productId"));
		int count = Integer.parseInt(request.getParameter("count"));
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		try {
			long userId = user.getUserId();
			CartDTO cartDTO = new CartDTO(userId, productId, count);
			int result = cartService.insertCart(cartDTO);
			if (result == 0) {// 실패시

			} else {// 성공시

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteCartByCartId(HttpServletRequest request, HttpServletResponse response) {
		long cartId = Long.parseLong(request.getParameter("cartId"));
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		try {
			long userId = user.getUserId();
			int result = cartService.deleteCartByCartId(cartId, userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	public void deleteCartByUserId(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		try {
			long userId = user.getUserId();
			int result = cartService.deleteCartByUserId(userId);
			if (result == 0) {// 실패시

			} else {// 성공시

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateCartCount(HttpServletRequest request, HttpServletResponse response) {
		long cartId = Long.parseLong(request.getParameter("cartId"));
		int count = Integer.parseInt(request.getParameter("newCount"));
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		try {
			long userId = user.getUserId();
			CartDTO cartDTO = new CartDTO(cartId, count);
			cartDTO.setUserId(userId);
			int result = cartService.updateCartCount(cartDTO);
			if (result == 0) {// 실패시

			} else {// 성공시

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void duplicatedCartCount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = 0;
		if(user == null) {
			userId = 1;
		}else {
			userId = user.getUserId();
		}
		long productId = Long.parseLong(request.getParameter("productId"));
		int count = Integer.parseInt(request.getParameter("newCount"));
		try {
			int result = cartService.updateCartCount(userId, productId, count);
			if (result == 0) {// 실패시

			} else {// 성공시

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public CartDTO duplicateCheck(HttpServletRequest request, HttpServletResponse response) {
		CartDTO cartDTO = null;
		long productId = Long.parseLong(request.getParameter("productId"));
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = 0;
		if(user == null) {
			userId = 1;
		}else {
			userId = user.getUserId();
		}
		try {
			cartDTO = cartService.duplicateCheck(userId, productId);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return cartDTO;
	}
	
	

}
