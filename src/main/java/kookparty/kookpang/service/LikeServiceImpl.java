package kookparty.kookpang.service;

import kookparty.kookpang.dao.LikeDAO;
import kookparty.kookpang.dao.LikeDAOImpl;
import kookparty.kookpang.dto.LikeDTO;
import kookparty.kookpang.exception.DBAccessException;

public class LikeServiceImpl implements LikeService {
	private LikeDAO likeDAO = LikeDAOImpl.getInstance();
	private static LikeService instance = new LikeServiceImpl();
	
	private LikeServiceImpl() {}
	
	public static LikeService getInstance() {
		return instance;
	}
	
	@Override
	public boolean isLiked(LikeDTO likeDTO) throws Exception {
		return likeDAO.isLiked(likeDTO);
	}
	
	@Override
	public void toggleLike(LikeDTO likeDTO) throws Exception {
		int result = 0;
		
		if (likeDAO.isLiked(likeDTO)) // 좋아요가 되어있으면 좋아요 삭제
			result = likeDAO.deleteLike(likeDTO);
		else
			result = likeDAO.insertLike(likeDTO);
		
		if (result == 0)
			throw new DBAccessException("좋아요 토글 실패!!");
	}
}
