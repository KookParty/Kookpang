package kookparty.kookpang.dao;

import java.sql.*;
import java.util.Optional;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.util.DbUtil;

public class UserDAO {

    /**
     * 회원가입용 INSERT
     */
    public UserDTO insert(UserDTO u) {
        String sql = 
            "INSERT INTO users(email, name, nickname, phone, address, role, status) " +
            "VALUES(?,?,?,?,?,'user',1)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, u.getEmail());
            ps.setString(2, u.getName());
            ps.setString(3, u.getNickname());
            ps.setString(4, u.getPhone());
            ps.setString(5, u.getAddress());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                u.setUserId(rs.getLong(1));
            }
            return u;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtil.dbClose(conn, ps, rs);
        }
    }

    /**
     * 이메일 중복확인
     */
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 닉네임 중복확인
     */
    public boolean existsByNickname(String nick) {
        String sql = "SELECT COUNT(*) FROM users WHERE nickname=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nick);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 전화번호 중복확인 (선택)
     */
    public boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 로그인 (email + phone)
     */
    public Optional<UserDTO> findByEmailAndPhone(String email, String phone) {
        String sql =
            "SELECT user_id, email, name, nickname, phone, address, role, status " +
            "FROM users WHERE email=? AND phone=? AND status=1";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, phone);
            rs = ps.executeQuery();

            if (!rs.next()) return Optional.empty();

            UserDTO u = new UserDTO();
            u.setUserId(rs.getLong("user_id"));
            u.setEmail(rs.getString("email"));
            u.setName(rs.getString("name"));
            u.setNickname(rs.getString("nickname"));
            u.setPhone(rs.getString("phone"));
            u.setAddress(rs.getString("address"));
            u.setRole(rs.getString("role"));
            u.setStatus(rs.getInt("status"));
            return Optional.of(u);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtil.dbClose(conn, ps, rs);
        }
    }
}
