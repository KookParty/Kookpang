package kookparty.kookpang.service;

import java.sql.SQLException;

import kookparty.kookpang.dto.LikeDTO;

public interface LikeService {
	/**
	 * 좋아요가 되어있는지 확인
	 */
	boolean isLiked(LikeDTO likeDTO) throws Exception;
	
	/**
	 * 좋아요 등록/삭제 토글
	 */
	void toggleLike(LikeDTO likeDTO) throws Exception;
}
