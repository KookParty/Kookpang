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

    // ===================== 마이페이지 =====================
    
    /** GET /front?key=user&methodName=mypage */
    public ModelAndView mypage(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return new ModelAndView("/users/login.jsp", true);
        }
        
        Object loginUser = session.getAttribute("loginUser");
        req.setAttribute("user", loginUser);
        return new ModelAndView("/users/mypage.jsp");
    }
    
    /** GET /ajax?key=user&methodName=profile */
    public Object profile(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return Map.of("ok", false, "msg", "로그인이 필요합니다.");
        }
        
        Object loginUser = session.getAttribute("loginUser");
        return Map.of("ok", true, "user", loginUser);
    }
    
    /** POST /ajax?key=user&methodName=updateProfile */
    public Object updateProfile(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return Map.of("ok", false, "msg", "로그인이 필요합니다.");
        }
        
        try {
            Object loginUser = session.getAttribute("loginUser");
            if (!(loginUser instanceof UserDTO)) {
                return Map.of("ok", false, "msg", "사용자 정보를 찾을 수 없습니다.");
            }
            
            UserDTO user = (UserDTO) loginUser;
            String nickname = t(req.getParameter("nickname"));
            String phone = t(req.getParameter("phone"));
            String address = t(req.getParameter("address"));
            
            // phone이 전달되지 않았으면 기존 값 유지
            if (phone == null || phone.isEmpty()) {
                phone = user.getPhone();
            }
            
            // 입력값 검증
            if (nickname == null || nickname.isEmpty()) {
                return Map.of("ok", false, "msg", "닉네임을 입력해주세요.");
            }
            
            // 현재 사용자가 아닌 다른 사용자가 같은 닉네임을 사용 중인지 확인
            if (!nickname.equals(user.getNickname()) && svc.nickTaken(nickname)) {
                return Map.of("ok", false, "msg", "이미 사용 중인 닉네임입니다.");
            }
            
            if (phone != null && !phone.isEmpty() && 
                !phone.equals(user.getPhone()) && svc.phoneTaken(phone)) {
                return Map.of("ok", false, "msg", "이미 등록된 전화번호입니다.");
            }
            
            // 사용자 정보 업데이트
            boolean updated = dao.updateProfile(user.getUserId(), nickname, phone, address);
            
            if (updated) {
                // 세션 정보도 업데이트
                user.setNickname(nickname);
                user.setPhone(phone);
                user.setAddress(address);
                session.setAttribute("loginUser", user);
                
                return Map.of("ok", true, "msg", "프로필이 수정되었습니다.");
            } else {
                return Map.of("ok", false, "msg", "프로필 수정에 실패했습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", "서버 오류가 발생했습니다.");
        }
    }
    
    /** POST /ajax?key=user&methodName=changePassword */
    public Object changePassword(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return Map.of("ok", false, "msg", "로그인이 필요합니다.");
        }
        
        try {
            Object loginUser = session.getAttribute("loginUser");
            if (!(loginUser instanceof UserDTO)) {
                return Map.of("ok", false, "msg", "사용자 정보를 찾을 수 없습니다.");
            }
            
            UserDTO user = (UserDTO) loginUser;
            String currentPassword = req.getParameter("currentPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");
            
            // 입력값 검증
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                return Map.of("ok", false, "msg", "현재 비밀번호를 입력해주세요.");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return Map.of("ok", false, "msg", "새 비밀번호를 입력해주세요.");
            }
            if (!newPassword.equals(confirmPassword)) {
                return Map.of("ok", false, "msg", "새 비밀번호가 일치하지 않습니다.");
            }
            
            // 현재 비밀번호 확인
            Optional<UserDTO> loginResult = Optional.ofNullable(svc.login(user.getEmail(), currentPassword));
            if (loginResult.isEmpty()) {
                return Map.of("ok", false, "msg", "현재 비밀번호가 올바르지 않습니다.");
            }
            
            // 비밀번호 변경
            boolean updated = dao.updatePassword(user.getUserId(), newPassword);
            
            if (updated) {
                return Map.of("ok", true, "msg", "비밀번호가 변경되었습니다.");
            } else {
                return Map.of("ok", false, "msg", "비밀번호 변경에 실패했습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", "서버 오류가 발생했습니다.");
        }
    }
    
    /** POST /ajax?key=user&methodName=deleteAccount */
    public Object deleteAccount(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            return Map.of("ok", false, "msg", "로그인이 필요합니다.");
        }
        
        try {
            Object loginUser = session.getAttribute("loginUser");
            if (!(loginUser instanceof UserDTO)) {
                return Map.of("ok", false, "msg", "사용자 정보를 찾을 수 없습니다.");
            }
            
            UserDTO user = (UserDTO) loginUser;
            String password = req.getParameter("password");
            
            // 비밀번호 확인
            if (password == null || password.trim().isEmpty()) {
                return Map.of("ok", false, "msg", "비밀번호를 입력해주세요.");
            }
            
            Optional<UserDTO> loginResult = Optional.ofNullable(svc.login(user.getEmail(), password));
            if (loginResult.isEmpty()) {
                return Map.of("ok", false, "msg", "비밀번호가 올바르지 않습니다.");
            }
            
            // 회원 탈퇴 처리
            boolean deleted = dao.deleteUser(user.getUserId());
            
            if (deleted) {
                // 세션 무효화
                session.invalidate();
                return Map.of("ok", true, "msg", "회원탈퇴가 완료되었습니다.");
            } else {
                return Map.of("ok", false, "msg", "회원탈퇴 처리에 실패했습니다.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", "서버 오류가 발생했습니다.");
        }
    }
}