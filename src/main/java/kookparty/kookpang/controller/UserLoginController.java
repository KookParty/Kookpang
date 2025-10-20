package kookparty.kookpang.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dao.UserDAO;
import kookparty.kookpang.dto.UserDTO;

import java.util.Map;
import java.util.Optional;

/**
 * 로그인 / 로그아웃 전용 컨트롤러
 * 요청 경로: /api/ajax?key=userLogin&methodName=login|logout
 */
public class UserLoginController implements Controller {
    private final UserDAO dao = new UserDAO();

    /** POST /api/ajax?key=userLogin&methodName=login */
    public Object login(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            String email = trim(req.getParameter("email"));
            String password = trim(req.getParameter("password"));

            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                return Map.of("ok", false, "msg", "이메일과 비밀번호를 입력해주세요.");
            }

            // DAO로 로그인 시도
            Optional<UserDTO> optUser = dao.findByEmailAndPassword(email, password);
            if (optUser.isEmpty()) {
                return Map.of("ok", false, "msg", "이메일 또는 비밀번호가 일치하지 않습니다.");
            }

            UserDTO user = optUser.get();

            // 로그인 성공 시 세션 저장
            HttpSession session = req.getSession(true);
            session.setAttribute("loginUser", user);

            return Map.of(
                "ok", true,
                "userId", user.getUserId(),
                "nickname", user.getNickname(),
                "email", user.getEmail()
            );

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("ok", false, "msg", e.getMessage());
        }
    }

    /** POST /api/ajax?key=userLogin&methodName=logout */
    public Object logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        return Map.of("ok", true, "msg", "로그아웃되었습니다.");
    }

    /** GET /api/ajax?key=userLogin&methodName=me */
    public Object me(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession s = req.getSession(false);
        Object u = (s == null) ? null : s.getAttribute("loginUser");
        return Map.of("ok", true, "user", u);
    }

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }
}
