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

  private static final Set<String> PUBLIC_RECIPE_METHODS = Set.of(
      "recipes","recipeDetail","selectByOptions"
  );

  private static final Set<String> PUBLIC_PRODUCT_READ_METHODS = Set.of(
      "selectAll","selectByOptions","ingredients"
  );

  /** AJAX 요청 판별 강화: /ajax 경로거나, 헤더/파라미터로 JSON 의도 표기 시 */
  private static boolean isAjax(HttpServletRequest req) {
    String path   = req.getServletPath();        
    String uri    = req.getRequestURI();       
    String xhr    = req.getHeader("X-Requested-With");
    String accept = req.getHeader("Accept");
    boolean viaAjaxEndpoint = "/ajax".equals(path) || (uri != null && uri.contains("/ajax"));
    boolean xhrHeader       = "XMLHttpRequest".equalsIgnoreCase(xhr);
    boolean acceptJson      = accept != null && accept.contains("application/json");
    boolean ajaxParam       = "1".equals(req.getParameter("ajax")) || "true".equalsIgnoreCase(req.getParameter("ajax"));
    return viaAjaxEndpoint || xhrHeader || acceptJson || ajaxParam;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req  = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;

    String key        = req.getParameter("key");
    String methodName = req.getParameter("methodName");
    String httpMethod = req.getMethod(); // GET/POST...

    boolean needAuth = true;

    if ("user".equals(key)    && methodName != null && PUBLIC_USER_METHODS.contains(methodName))    needAuth = false;
    if ("recipe".equals(key)  && methodName != null && PUBLIC_RECIPE_METHODS.contains(methodName))  needAuth = false;

    if ("product".equals(key) && methodName != null) {
    
      if (PUBLIC_PRODUCT_READ_METHODS.contains(methodName)) {
   
        if ("ingredients".equals(methodName)) {
          needAuth = "GET".equalsIgnoreCase(httpMethod) ? false : true;
        } else {
          needAuth = false;
        }
      }
    }

    
    if ("cart".equals(key)) needAuth = true;

 
    if (needAuth) {
      HttpSession session = req.getSession(false);
      Object loginUser = (session == null) ? null : session.getAttribute("loginUser");

      if (loginUser == null) {
        if (isAjax(req)) {
          // 🔒 AJAX 요청: 반드시 JSON + 401 (HTML 리다이렉트 금지 → 파싱 에러 예방)
          resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          resp.setContentType("application/json; charset=UTF-8");
          resp.getWriter().print("{\"ok\":false,\"reason\":\"AUTH\",\"msg\":\"로그인이 필요합니다.\"}");
          return;
        } else {
          // 🌐 일반 요청: 로그인 페이지로
          resp.sendRedirect(req.getContextPath() + "/front?key=user&methodName=loginForm");
          return;
        }
      }
    }

    chain.doFilter(request, response);
  }
}
