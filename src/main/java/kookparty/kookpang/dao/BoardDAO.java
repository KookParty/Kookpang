package kookparty.kookpang.dao;

import kookparty.kookpang.dto.BoardDTO;
import kookparty.kookpang.dto.BoardDTO.Image;
import kookparty.kookpang.dto.BoardDTO.Comment;
import kookparty.kookpang.util.DbUtil;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/** 게시판 DAO - 프로젝트 공용 규약에 맞춰 dbQuery.properties를 사용 */
public class BoardDAO {

    private static final Properties proFile = new Properties();
    static {
        try {
            // 다른 DAO들과 동일: 클래스패스 루트의 dbQuery.properties
            InputStream is = BoardDAO.class.getClassLoader().getResourceAsStream("dbQuery.properties");
            if (is == null) throw new IllegalStateException("dbQuery.properties not found on classpath");
            proFile.load(is);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static String Q(String key) { return proFile.getProperty(key); }

    // ===== 목록/카운트 =====

    public List<BoardDTO> list(String category, String q, int page, int size) {
        List<BoardDTO> list = new ArrayList<>();
        String sql = Q("board.post.listPage");
        int off = (page - 1) * size;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            ps.setString(i++, nullToEmpty(category));   // ? = '' 체크용
            ps.setString(i++, nullToEmpty(category));
            ps.setString(i++, nullToEmpty(q));
            ps.setString(i++, nullToEmpty(q));
            ps.setString(i++, nullToEmpty(q));
            ps.setInt(i++, size);
            ps.setInt(i++, off);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BoardDTO d = new BoardDTO();
                    d.setPostId(rs.getLong("post_id"));
                    d.setTitle(rs.getString("title"));
                    d.setContent(rs.getString("content"));
                    d.setViewCount(rs.getLong("view_count"));
                    d.setCommentCount(rs.getLong("comment_count"));
                    d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    d.setNickname(rs.getString("nickname"));
                    d.setCategory(rs.getString("category"));
                    list.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int count(String category, String q) {
        String sql = Q("board.post.count");
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            ps.setString(i++, nullToEmpty(category));
            ps.setString(i++, nullToEmpty(category));
            ps.setString(i++, nullToEmpty(q));
            ps.setString(i++, nullToEmpty(q));
            ps.setString(i++, nullToEmpty(q));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ===== 조회수 증가 / 단건 조회 =====

    public void incView(long postId) {
        String sql = Q("board.post.incView");
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, postId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public BoardDTO findById(long postId) {
        String sql = Q("board.post.findById");
        BoardDTO d = null;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new BoardDTO();
                    d.setPostId(rs.getLong("post_id"));
                    d.setUserId(rs.getLong("user_id"));
                    d.setCategory(rs.getString("category"));
                    d.setTitle(rs.getString("title"));
                    d.setContent(rs.getString("content"));
                    d.setViewCount(rs.getLong("view_count"));
                    d.setCommentCount(rs.getLong("comment_count"));
                    d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    d.setNickname(rs.getString("nickname"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (d != null) {
            d.setImages(listImages(postId));
            d.setComments(listComments(postId));
        }
        return d;
    }

    // ===== 등록/수정/삭제 =====

    public long insertPost(BoardDTO d, List<Image> images) {
        String sql = Q("board.post.insert");
        long newId = 0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, d.getUserId());
            ps.setString(2, nullToEmpty(d.getCategory(), "free"));
            ps.setString(3, nullToEmpty(d.getTitle()));
            ps.setString(4, nullToEmpty(d.getContent()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) newId = rs.getLong(1);
            }
            if (newId > 0 && images != null && !images.isEmpty()) {
                insertImages(con, newId, images);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return newId;
    }

    public boolean updatePost(BoardDTO d, List<Image> images) {
        String sql = Q("board.post.update");
        int updated = 0;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nullToEmpty(d.getCategory(), "free"));
            ps.setString(2, nullToEmpty(d.getTitle()));
            ps.setString(3, nullToEmpty(d.getContent()));
            ps.setLong(4, d.getPostId());
            ps.setLong(5, d.getUserId());
            updated = ps.executeUpdate();

            // 이미지 교체
            try (PreparedStatement del = con.prepareStatement(Q("board.image.deleteByPost"))) {
                del.setLong(1, d.getPostId());
                del.executeUpdate();
            }
            if (images != null && !images.isEmpty()) {
                insertImages(con, d.getPostId(), images);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return updated > 0;
    }

    public boolean deletePost(long postId, long userId) {
        String sql = Q("board.post.delete");
        int r = 0;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, postId);
            ps.setLong(2, userId);
            r = ps.executeUpdate();
            // 이미지/댓글은 FK ON DELETE CASCADE가 아니면 별도 삭제 필요
            try (PreparedStatement delImg = con.prepareStatement(Q("board.image.deleteByPost"))) {
                delImg.setLong(1, postId);
                delImg.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); }
        return r > 0;
    }

    // ===== 이미지 =====

    private void insertImages(Connection con, long postId, List<Image> images) throws SQLException {
        String sqlImg = Q("board.image.insert");
        try (PreparedStatement ps = con.prepareStatement(sqlImg)) {
            int order = 0;
            for (Image im : images) {
                if (im == null || isEmpty(im.getImageUrl())) continue;
                ps.setLong(1, postId);
                ps.setString(2, im.getImageUrl());
                ps.setInt(3, im.getImageOrder() > 0 ? im.getImageOrder() : order++);
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        }
    }

    private List<Image> listImages(long postId) {
        List<Image> list = new ArrayList<>();
        String sql = Q("board.image.list");
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Image im = new Image();
                    im.setImageId(rs.getLong("image_id"));
                    im.setPostId(rs.getLong("post_id"));
                    im.setImageUrl(rs.getString("image_url"));
                    im.setImageOrder(rs.getInt("image_order"));
                    list.add(im);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ===== 댓글 =====

    public List<Comment> listComments(long postId) {
        List<Comment> list = new ArrayList<>();
        String sql = Q("board.comment.list");
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setCommentId(rs.getLong("comment_id"));
                    c.setPostId(rs.getLong("post_id"));
                    c.setUserId(rs.getLong("user_id"));
                    c.setContent(rs.getString("content"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) c.setCreatedAt(ts.toLocalDateTime());
                    c.setNickname(rs.getString("nickname"));
                    list.add(c);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public long addComment(long postId, long userId, String content) {
        String sql = Q("board.comment.insert");
        String inc = Q("board.comment.incOnAdd");
        long id = 0;

        try (Connection con = DbUtil.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, postId);
                ps.setLong(2, userId);
                ps.setString(3, content);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) id = rs.getLong(1);
                }
            }
            try (PreparedStatement ps2 = con.prepareStatement(inc)) {
                ps2.setLong(1, postId);
                ps2.executeUpdate();
            }
            con.commit();
        } catch (Exception e) { e.printStackTrace(); }
        return id;
    }

    public boolean delComment(long commentId, long userId, long postId) {
        String sql = Q("board.comment.delete");
        String dec = Q("board.comment.decOnDel");
        int r = 0;

        try (Connection con = DbUtil.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, commentId);
                ps.setLong(2, userId);
                r = ps.executeUpdate();
            }
            if (r > 0) {
                try (PreparedStatement ps2 = con.prepareStatement(dec)) {
                    ps2.setLong(1, postId);
                    ps2.executeUpdate();
                }
            }
            con.commit();
        } catch (Exception e) { e.printStackTrace(); }
        return r > 0;
    }

    // ===== utils =====
    private static boolean isEmpty(String s){ return s == null || s.isBlank(); }
    private static String nullToEmpty(String s){ return s == null ? "" : s; }
    private static String nullToEmpty(String s, String dv){ return isEmpty(s) ? dv : s; }
}
