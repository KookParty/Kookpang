package kookparty.kookpang.controller;

import jakarta.servlet.http.*;
import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.UserService;
import kookparty.kookpang.dao.UserDAO;
import java.util.Map;
import java.util.Optional;

public class UserController implements Controller {
    private final UserService svc = new UserService();
    private final UserDAO dao = new UserDAO();

    private static String t(String s) { return s == null ? null : s.trim(); }

    public Object checkEmail(HttpServletRequest req, HttpServletResponse resp) {
        String email = t(req.getParameter("email"));
        if (email == null || email.isEmpty())
            return Map.of("ok", false, "msg", "이메일을 입력하세요.");
        boolean valid = svc.isValidEmail(email);
        boolean taken = valid && svc.emailTaken(email);
        return Map.of("ok", true, "valid", valid, "taken", taken);
    }

    public Object checkNick(HttpServletRequest req, HttpServletResponse resp) {
        String nick = t(req.getParameter("nick"));
        if (nick == null || nick.isEmpty())
            return Map.of("ok", false, "msg", "닉네임을 입력하세요.");
        boolean taken = svc.nickTaken(nick);
        return Map.of("ok", true, "taken", taken);
    }

    public Object checkPhone(HttpServletRequest req, HttpServletResponse resp) {
        String phone = t(req.getParameter("phone"));
        if (phone == null || phone.isEmpty())
            return Map.of("ok", false, "msg", "전화번호를 입력하세요.");
        boolean taken = svc.phoneTaken(phone);
        return Map.of("ok", true, "taken", taken);
    }

    public Object register(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            String email = t(req.getParameter("email"));
            String password = t(req.getParameter("password"));
            String name = t(req.getParameter("name"));
            String nickname = t(req.getParameter("nickname"));
            String phone = t(req.getParameter("phone"));
            String address = t(req.getParameter("address"));
            UserDTO u = svc.register(email, password, name, nickname, phone, address);
            req.getSession(true).setAttribute("loginUser", u);
            return Map.of("ok", true, "userId", u.getUserId(), "nickname", u.getNickname(), "email", u.getEmail());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("ok", false, "msg", e.getMessage());
        }
    }

    public Object login(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            String email = t(req.getParameter("email"));
            String password = t(req.getParameter("password"));
            if (email == null || password == null || email.isEmpty() || password.isEmpty())
                return Map.of("ok", false, "msg", "이메일과 비밀번호를 입력해주세요.");
            Optional<UserDTO> opt = dao.findByEmailAndPassword(email, password);
            if (opt.isEmpty())
                return Map.of("ok", false, "msg", "이메일 또는 비밀번호가 일치하지 않습니다.");
            UserDTO u = opt.get();
            req.getSession(true).setAttribute("loginUser", u);
            return Map.of("ok", true, "userId", u.getUserId(), "nickname", u.getNickname(), "email", u.getEmail());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("ok", false, "msg", e.getMessage());
        }
    }

    public Object logout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession s = req.getSession(false);
        if (s != null) s.invalidate();
        return Map.of("ok", true, "msg", "로그아웃되었습니다.");
    }

    public Object me(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession s = req.getSession(false);
        Object user = (s == null) ? null : s.getAttribute("loginUser");
        if(user == null){
            return Map.of("ok", false, "msg", "로그인 상태가 아닙니다.");
        }
        return Map.of("ok", true, "user", user);
    }
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) {
        
        return new ModelAndView("/users/login.jsp");
    }

    public ModelAndView registerForm(HttpServletRequest req, HttpServletResponse resp) {
       
        return new ModelAndView("/users/register.jsp");
    }

    public ModelAndView loginSubmit(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            String email = t(req.getParameter("email"));
            String password = t(req.getParameter("password"));
            UserDTO u = svc.login(email, password);
            req.getSession(true).setAttribute("loginUser", u);
            return new ModelAndView(req.getContextPath() + "/index.jsp", true);
        } catch (Exception e) {
            req.setAttribute("errorMsg", e.getMessage());
            return new ModelAndView("/users/login.jsp");
        }
    }

    public ModelAndView registerSubmit(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            String email    = t(req.getParameter("email"));
            String password = t(req.getParameter("password"));
            String name     = t(req.getParameter("name"));
            String nickname = t(req.getParameter("nickname"));
            String phone    = t(req.getParameter("phone"));
            String address  = t(req.getParameter("address"));
            UserDTO u = svc.register(email, password, name, nickname, phone, address);
            return new ModelAndView(req.getContextPath() + "/users/login.jsp", true);
        } catch (Exception e) {            
            req.setAttribute("errorMsg", e.getMessage());
            req.setAttribute("formEmail", emailParam(req));
            req.setAttribute("formName",  nameParam(req));
            req.setAttribute("formNick",  nickParam(req));
            req.setAttribute("formPhone", phoneParam(req));
            req.setAttribute("formAddr",  addrParam(req));
            return new ModelAndView("/users/register.jsp");
        }
    }

    private String emailParam(HttpServletRequest req){ return req.getParameter("email"); }
    private String nameParam (HttpServletRequest req){ return req.getParameter("name"); }
    private String nickParam (HttpServletRequest req){ return req.getParameter("nickname"); }
    private String phoneParam(HttpServletRequest req){ return req.getParameter("phone"); }
    private String addrParam (HttpServletRequest req){ return req.getParameter("address"); }
}