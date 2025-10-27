package kookparty.kookpang.service;

import kookparty.kookpang.dto.BoardDTO;
import kookparty.kookpang.dto.BoardDTO.Comment;

import java.sql.SQLException;
import java.util.List;

public interface BoardService {
    
    /**
     * 게시글 목록 조회 (페이징, 검색, 정렬)
     * @param category 카테고리 (notice, free 등)
     * @param searchText 검색어
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param sort 정렬 방식 (latest, views, likes)
     * @return 게시글 목록
     * @throws SQLException
     */
    List<BoardDTO> getPostList(String category, String searchText, int page, int size, String sort) throws SQLException;
    
    /**
     * 게시글 전체 개수 조회 (검색 조건 포함)
     * @param category 카테고리
     * @param searchText 검색어
     * @return 게시글 개수
     * @throws SQLException
     */
    int getTotalCount(String category, String searchText) throws SQLException;
    
    /**
     * 게시글 상세 조회
     * @param postId 게시글 ID
     * @return 게시글 상세 정보
     * @throws SQLException
     */
    BoardDTO getPost(Long postId) throws SQLException;
    
    /**
     * 게시글 작성
     * @param post 게시글 정보
     * @return 작성된 게시글 ID
     * @throws SQLException
     */
    Long createPost(BoardDTO post) throws SQLException;
    
    /**
     * 게시글 수정
     * @param post 수정할 게시글 정보
     * @return 수정 성공 여부
     * @throws SQLException
     */
    boolean updatePost(BoardDTO post) throws SQLException;
    
    /**
     * 게시글 삭제
     * @param postId 게시글 ID
     * @param userId 작성자 ID (권한 확인용)
     * @return 삭제 성공 여부
     * @throws SQLException
     */
    boolean deletePost(Long postId, Long userId) throws SQLException;
    
    /**
     * 게시글 조회수 증가
     * @param postId 게시글 ID
     * @throws SQLException
     */
    void increaseViewCount(Long postId) throws SQLException;
    
    /**
     * 댓글 목록 조회
     * @param postId 게시글 ID
     * @return 댓글 목록
     * @throws SQLException
     */
    List<Comment> getComments(Long postId) throws SQLException;
    
    /**
     * 댓글 작성
     * @param comment 댓글 정보
     * @return 작성 성공 여부
     * @throws SQLException
     */
    boolean addComment(Comment comment) throws SQLException;
    
    /**
     * 댓글 삭제
     * @param commentId 댓글 ID
     * @param userId 작성자 ID (권한 확인용)
     * @param postId 게시글 ID
     * @return 삭제 성공 여부
     * @throws SQLException
     */
    boolean deleteComment(Long commentId, Long userId, Long postId) throws SQLException;
    
    List<BoardDTO> selectAll() throws SQLException;
}