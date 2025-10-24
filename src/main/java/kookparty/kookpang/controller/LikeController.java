package kookparty.kookpang.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.common.TargetType;
import kookparty.kookpang.dto.LikeDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.LikeService;
import kookparty.kookpang.service.LikeServiceImpl;

public class LikeController implements Controller {
	private LikeService likeService = LikeServiceImpl.getInstance();
	
	/**
	 * 좋아요가 되어있는지 확인
	 */
	public Object isLiked(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = user.getUserId();
		TargetType targetType = TargetType.valueOf(request.getParameter("targetType").toUpperCase());
		long targetId = Long.parseLong(request.getParameter("targetId"));
		
		LikeDTO likeDTO = new LikeDTO(userId, targetType, targetId);
		return likeService.isLiked(likeDTO);
	}
	
	/**
	 * 좋아요 등록/삭제 토글
	 */
	public void toggleLike(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO)session.getAttribute("loginUser");
		long userId = user.getUserId();
		TargetType targetType = TargetType.valueOf(request.getParameter("targetType").toUpperCase());
		long targetId = Long.parseLong(request.getParameter("targetId"));
		
		LikeDTO likeDTO = new LikeDTO(userId, targetType, targetId);
		likeService.toggleLike(likeDTO);
	}
	
}
