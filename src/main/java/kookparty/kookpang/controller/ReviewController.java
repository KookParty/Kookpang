package kookparty.kookpang.controller;

import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kookparty.kookpang.dto.ReviewDTO;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.ReviewService;
import kookparty.kookpang.service.ReviewServiceImpl;
import kookparty.kookpang.util.FilePath;

public class ReviewController implements Controller {
	private ReviewService reviewService = ReviewServiceImpl.getInstance();
	
	/**
	 * 리뷰 전체 검색
	 */
	public Object selectByRecipeId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long recipeId = Long.parseLong(request.getParameter("recipeId"));
		List<ReviewDTO> reviews = reviewService.selectByRecipeId(recipeId);
		request.setAttribute("reviews", reviews);
		return reviews;
	}
	
	/**
	 * 리뷰 등록
	 */
	public void insertReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserDTO userDTO = (UserDTO)session.getAttribute("loginUser");
		long userId = userDTO.getUserId();

		long recipeId = Long.parseLong(request.getParameter("recipeId"));
		int rating = Integer.parseInt(request.getParameter("rating"));
		String content = request.getParameter("content");
		
		// 리뷰 이미지 업로드
		String imageUrl = null;
		// form 속성에 enctype="multipart/form-data" 있는지 꼭 확인!
		Part part = request.getPart("imageUrl");
		if(part != null) {
			String fileName = part.getSubmittedFileName();
			if (fileName != null && !fileName.isEmpty()) {
				fileName = Paths.get(fileName).getFileName().toString();
				part.write(FilePath.getSavePath(request) + "/" + fileName);
				imageUrl = "../upload/" + fileName;
			} else {
				imageUrl = null;
			}
		}
		
		
		ReviewDTO reviewDTO = new ReviewDTO(recipeId, userId, rating, content, imageUrl);
		reviewService.insertReview(reviewDTO);
	}
	
	/**
	 * 리뷰 삭제
	 */
	public void deleteReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long reviewId = Long.parseLong(request.getParameter("reviewId"));
		reviewService.deleteReview(reviewId);
	}
}
