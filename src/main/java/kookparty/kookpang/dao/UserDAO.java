package kookparty.kookpang.controller.dao;

import kookparty.kookpang.dto.User;
import kookparty.kookpang.util.DbUtil;

import java.sql.*;
import java.util.Optional;

public class UserDao {

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email=?";
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.dbClose(conn, ps, rs);
        }
    }

    public boolean existsByNickname(String nick) {
        String sql = "SELECT 1 FROM users WHERE nickname=?";
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nick);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.dbClose(conn, ps, rs);
        }
    }

    public User insert(User u) {
        String sql = """
            INSERT INTO users(email, name, nickname, phone, address, role, status)
            VALUES(?,?,?,?,?,'user',1)
        """;
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
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
            if (rs.next()) u.setUserId(rs.getLong(1));
            return u;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtil.dbClose(conn, ps, rs);
        }
    }

    public Optional<User> findByEmailAndPhone(String email, String phone) {
        String sql = """
            SELECT user_id, email, name, nickname, phone, address, role, status
            FROM users WHERE email=? AND phone=? AND status=1
        """;
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DbUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, phone);
            rs = ps.executeQuery();
            if (!rs.next()) return Optional.empty();

            User u = new User();
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
            e.printStackTrace();
            return Optional.empty();
        } finally {
            DbUtil.dbClose(conn, ps, rs);
        }
    }
}
