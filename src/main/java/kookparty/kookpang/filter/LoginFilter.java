package kookparty.kookpang.filter;

import java.io.IOException;
import java.util.Set;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter(urlPatterns = {"/ajax","/front"})

public class LoginFilter implements Filter {
  private static final Set<String> PUBLIC_USER_METHODS = Set.of(
      "loginForm","login","registerForm","register",
      "checkEmail","checkNick","checkPhone","me",
      "loginSubmit","registerSubmit"
  );

  private static final Set<String> PUBLIC_PRODUCT_METHODS = Set.of(
      "selectAll","selectByOptions"   // 조회만 공개
  );

  private static boolean isAjax(HttpServletRequest req) {
    String xhr = req.getHeader("X-Requested-With");
    String accept = req.getHeader("Accept");
    return "XMLHttpRequest".equalsIgnoreCase(xhr)
        || (accept != null && accept.contains("application/json"))
        || "1".equals(req.getParameter("ajax"));
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req  = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    String key        = req.getParameter("key");
    String methodName = req.getParameter("methodName");
    String httpMethod = req.getMethod();

    boolean needAuth = true;

    if ("user".equals(key) && methodName != null && PUBLIC_USER_METHODS.contains(methodName)) {
      needAuth = false;
    }

    if ("recipe".equals(key) && Set.of("selectAll","recipeDetail","recipes").contains(methodName)) {
      needAuth = false;
    }
    if ("product".equals(key) && methodName != null) {
      if (PUBLIC_PRODUCT_METHODS.contains(methodName)) {
        needAuth = false;
      } else if ("ingredients".equals(methodName)) {
        needAuth = !"GET".equalsIgnoreCase(httpMethod);
      }
    }

    if ("cart".equals(key)) {
      needAuth = true;
    }
    if (needAuth) {
      HttpSession session = req.getSession(false);
      if (session == null || session.getAttribute("loginUser") == null) {
        if (isAjax(req)) {
          resp.setContentType("application/json; charset=UTF-8");
          resp.setStatus(401);
          resp.getWriter().print("{\"ok\":false,\"error\":\"unauthorized\"}");
          return;
        } else {
          resp.sendRedirect(req.getContextPath()+"/front?key=user&methodName=loginForm");
          return;
        }
      }
    }

    chain.doFilter(request, response);
  }
}