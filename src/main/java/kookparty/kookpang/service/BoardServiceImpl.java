package kookparty.kookpang.service;

import kookparty.kookpang.dao.BoardDAO;
import kookparty.kookpang.dto.BoardDTO;
import kookparty.kookpang.dto.BoardDTO.Comment;
import kookparty.kookpang.dto.BoardDTO.Image;
import kookparty.kookpang.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public class BoardServiceImpl implements BoardService {
    
    private final BoardDAO boardDAO;
    
    public BoardServiceImpl() {
        try {
            System.out.println("[BoardServiceImpl] 초기화 시작...");
            this.boardDAO = new BoardDAO();
            System.out.println("[BoardServiceImpl] 초기화 완료!");
        } catch (Exception e) {
            System.err.println("[BoardServiceImpl] 초기화 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("BoardServiceImpl 초기화 실패", e);
        }
    }
    
    @Override
    public List<BoardDTO> getPostList(String category, String searchText, int page, int size, String sort) throws SQLException {
        try {
            return boardDAO.list(category, searchText, page, size, sort);
        } catch (Exception e) {
            System.err.println("[BoardService] 게시글 목록 조회 오류: " + e.getMessage());
            throw new SQLException("게시글 목록 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public int getTotalCount(String category, String searchText) throws SQLException {
        try {
            return boardDAO.count(category, searchText);
        } catch (Exception e) {
            throw new SQLException("게시글 개수 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public BoardDTO getPost(Long postId) throws SQLException {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }
        
        try {
            return boardDAO.findById(postId);
        } catch (Exception e) {
            throw new SQLException("게시글 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public Long createPost(BoardDTO post) throws SQLException {
        if (post == null) {
            throw new IllegalArgumentException("게시글 정보가 필요합니다.");
        }
        
        // 제목과 내용 검증
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        
        try {
            // 기본값 설정
            if (post.getCategory() == null || post.getCategory().trim().isEmpty()) {
                post.setCategory("free");
            }
            
            List<Image> images = post.getImages();
            return boardDAO.insertPost(post, images);
        } catch (Exception e) {
            throw new SQLException("게시글 작성 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public boolean updatePost(BoardDTO post) throws SQLException {
        if (post == null || post.getPostId() == null) {
            throw new IllegalArgumentException("수정할 게시글 정보가 올바르지 않습니다.");
        }
        
        // 제목과 내용 검증
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        
        try {
            List<Image> images = post.getImages();
            return boardDAO.updatePost(post, images);
        } catch (Exception e) {
            System.err.println("[BoardService] 게시글 수정 오류: " + e.getMessage());
            throw new SQLException("게시글 수정 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public boolean deletePost(Long postId, UserDTO user) throws SQLException {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }
        if (user == null) {
            throw new IllegalArgumentException("사용자 인증이 필요합니다.");
        }
        
        if(user.getRole().equals("admin")) {
        	return boardDAO.deletePost(postId);
        }
        
        try {
            return boardDAO.deletePost(postId, user.getUserId());
        } catch (Exception e) {
            throw new SQLException("게시글 삭제 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public void increaseViewCount(Long postId) throws SQLException {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }
        
        try {
            boardDAO.incView(postId);
        } catch (Exception e) {
            throw new SQLException("조회수 증가 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public List<Comment> getComments(Long postId) throws SQLException {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }
        
        try {
            return boardDAO.listComments(postId);
        } catch (Exception e) {
            throw new SQLException("댓글 목록 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public boolean addComment(Comment comment) throws SQLException {
        if (comment == null) {
            throw new IllegalArgumentException("댓글 정보가 필요합니다.");
        }
        if (comment.getPostId() == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }
        if (comment.getUserId() == null) {
            throw new IllegalArgumentException("사용자 인증이 필요합니다.");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }
        
        try {
            long commentId = boardDAO.addComment(comment.getPostId(), comment.getUserId(), comment.getContent());
            return commentId > 0;
        } catch (Exception e) {
            throw new SQLException("댓글 작성 중 오류가 발생했습니다.", e);
        }
    }
    
    @Override
    public boolean deleteComment(Long commentId, Long userId, Long postId) throws SQLException {
        if (commentId == null) {
            throw new IllegalArgumentException("댓글 ID가 필요합니다.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("사용자 인증이 필요합니다.");
        }
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }
        
        try {
            return boardDAO.delComment(commentId, userId, postId);
        } catch (Exception e) {
            throw new SQLException("댓글 삭제 중 오류가 발생했습니다.", e);
        }
    }

	@Override
	public List<BoardDTO> selectAll() throws SQLException {
		List<BoardDTO> list = boardDAO.selectAll();
		return list;
	}
    
    
}