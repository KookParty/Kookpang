package kookparty.kookpang.controller;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.service.UserService;
import kookparty.kookpang.dto.UserDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    private final UserService svc = new UserService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getPathInfo(); // /check-email, /check-nick, /me
        Map<String, Object> out = new HashMap<>();

        try {
            if ("/check-email".equals(path)) {
                String email = req.getParameter("email");
            if(!svc.isValidEmail(email)) {
                resp.setStatus(400);
                out.put("ok", false);   
                out.put("msg", "형식이 올바르지 않습니다");
            }else{
                out.put("ok", true);
                out.put("taken", svc.emailTaken(email));
            }
                out.put("taken", svc.emailTaken(email));
            } else if ("/check-nick".equals(path)) {
                String nick = req.getParameter("nick");
                out.put("taken", svc.nickTaken(nick));
            } else if ("/me".equals(path)) {
                HttpSession s = req.getSession(false);
                out.put("user", s == null ? null : s.getAttribute("user"));
            } else {
                resp.setStatus(404);
                out.put("ok", false);
                out.put("msg", "unknown endpoint");
            }
        } catch (Exception e) {
            resp.setStatus(400);
            out.put("ok", false);
            out.put("msg", e.getMessage());
        }

        try (PrintWriter w = resp.getWriter()) {
            w.print(gson.toJson(out));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getPathInfo(); // /register, /login, /logout
        Map<String, Object> out = new HashMap<>();

        try (PrintWriter w = resp.getWriter()) {
            if ("/register".equals(path)) {
                String email = t(req.getParameter("email"));
                String name = t(req.getParameter("name"));
                String nickname = t(req.getParameter("nickname"));
                String phone = t(req.getParameter("phone"));
                String address = t(req.getParameter("address"));

                UserDTO saved = svc.register(email, name, nickname, phone, address);

                // 세션 로그인
                HttpSession s = req.getSession(true);
                s.setAttribute("user", saved);

                out.put("ok", true);
                out.put("user", saved);
                w.print(gson.toJson(out));

            } else if ("/login".equals(path)) {
                String email = t(req.getParameter("email"));
                String phone = t(req.getParameter("phone"));

                UserDTO u = svc.loginByEmailPhone(email, phone);

                HttpSession s = req.getSession(true);
                s.setAttribute("user", u);

                out.put("ok", true);
                out.put("user", u);
                w.print(gson.toJson(out));

            } else if ("/logout".equals(path)) {
                HttpSession s = req.getSession(false);
                if (s != null) s.invalidate();
                out.put("ok", true);
                w.print(gson.toJson(out));

            } else {
                resp.setStatus(404);
                out.put("ok", false);
                out.put("msg", "unknown endpoint");
                w.print(gson.toJson(out));
            }

        } catch (IllegalArgumentException | IllegalStateException ex) {
            resp.setStatus(400);
            try (PrintWriter w2 = resp.getWriter()) {
                w2.print("{\"ok\":false,\"msg\":\"" + ex.getMessage() + "\"}");
            }
        } catch (RuntimeException ex) {
            resp.setStatus(400);
            String msg = (ex.getMessage() != null && ex.getMessage().contains("duplicate"))
                    ? "중복된 값이 있습니다." : "서버 오류";
            try (PrintWriter w2 = resp.getWriter()) {
                w2.print("{\"ok\":false,\"msg\":\"" + msg + "\"}");
            }
        }
    }

    private static String t(String s) { return s == null ? null : s.trim(); }
}
