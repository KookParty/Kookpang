package kookparty.kookpang.dao;

import java.sql.SQLException;

import kookparty.kookpang.dto.LikeDTO;

public interface LikeDAO {
	/**
	 * 좋아요가 되어있는지 확인
	 */
	boolean isLiked(LikeDTO likeDTO) throws SQLException;
	
	/**
	 * 좋아요 등록
	 */
	int insertLike(LikeDTO likeDTO) throws SQLException;
	
	/**
	 * 좋아요 삭제
	 */
	int deleteLike(LikeDTO likeDTO) throws SQLException;
}
