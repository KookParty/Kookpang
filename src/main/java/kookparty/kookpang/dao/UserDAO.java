package kookparty.kookpang.dao;

import java.sql.*;
import java.util.Optional;

import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.util.DbUtil;

public class UserDAO {

    // 중복체크
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public boolean existsByNickname(String nick) {
        String sql = "SELECT 1 FROM users WHERE nickname=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nick);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public boolean existsByPhone(String phone) {
        String sql = "SELECT 1 FROM users WHERE phone=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** 회원가입: 비밀번호 포함 */
    public UserDTO insert(UserDTO u) {
        String sql = """
            INSERT INTO users(email, password, name, nickname, phone, address, role, status)
            VALUES(?,?,?,?,?,?,'user',1)
        """;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getName());
            ps.setString(4, u.getNickname());
            ps.setString(5, u.getPhone());
            ps.setString(6, u.getAddress());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setUserId(rs.getLong(1));
            }
            // 기본값들 채워서 반환
            u.setRole("user");
            u.setStatus(1);
            return u;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** 로그인: 이메일 + 비밀번호 + 활성회원(status=1) */
    public Optional<UserDTO> findByEmailAndPassword(String email, String password) {
        String sql = """
            SELECT user_id, email, name, nickname, phone, address, role, status
            FROM users
            WHERE email=? AND password=? AND status=1
        """;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    
    /** 프로필 업데이트 (닉네임, 전화번호, 주소) */
    public boolean updateProfile(Long userId, String nickname, String phone, String address) {
        String sql;
        if (phone == null || phone.trim().isEmpty()) {
            sql = "UPDATE users SET nickname=?, address=? WHERE user_id=?";
        } else {
            sql = "UPDATE users SET nickname=?, phone=?, address=? WHERE user_id=?";
        }
        
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nickname);
            
            if (phone == null || phone.trim().isEmpty()) {
                ps.setString(2, address);
                ps.setLong(3, userId);
            } else {
                ps.setString(2, phone);
                ps.setString(3, address);
                ps.setLong(4, userId);
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false;
        }
    }
    
    /** 비밀번호 변경 */
    public boolean updatePassword(Long userId, String newPassword) {
        String sql = "UPDATE users SET password=? WHERE user_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false;
        }
    }
    
    /** 회원 탈퇴 */
    public boolean deleteUser(Long userId) {
        String sql = "UPDATE users SET status=0 WHERE user_id=?";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false;
        }
    }
}
