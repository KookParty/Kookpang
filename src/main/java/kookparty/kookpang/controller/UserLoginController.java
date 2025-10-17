package kookparty.kookpang.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import kookparty.kookpang.dto.UserDTO;
import kookparty.kookpang.service.UserService;

@WebServlet("/api/users/login")
public class UserLoginController extends HttpServlet {
    private final UserService svc = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            UserDTO u = svc.login(email, password);
            req.getSession(true).setAttribute("loginUser", u);
            resp.getWriter().write("{\"ok\":true}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String msg = e.getMessage().replace("\"", "\\\"");
            resp.getWriter().write("{\"ok\":false,\"msg\":\"" + msg + "\"}");
        }
    }
}
