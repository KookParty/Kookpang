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

  /** ✅ 게시판: 비회원 공개 메서드 (페이지 & AJAX 데이터) */
  private static final Set<String> PUBLIC_BOARD_READ_METHODS = Set.of(
      "list",       // /front?key=board&methodName=list
      "view",       // /front?key=board&methodName=view&postId=...
      "listData",   // /ajax?key=board&methodName=listData
      "postData"    // /ajax?key=board&methodName=postData&postId=...
  );

  /** AJAX 요청 판별 (헤더/Accept/파라미터 포함) */
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

    String key        = n(req.getParameter("key"));
    String methodName = n(req.getParameter("methodName"));
    String httpMethod = req.getMethod(); // GET/POST...

    boolean needAuth = true;

    // ----- 공개 경로 판정 -----
    if ("user".equals(key) && PUBLIC_USER_METHODS.contains(methodName)) {
      needAuth = false;
    }

    if ("recipe".equals(key) && PUBLIC_RECIPE_METHODS.contains(methodName)) {
      needAuth = false;
    }
    
    if ("review".equals(key) && Set.of("selectByRecipeId").contains(methodName)) {
      needAuth = false;
    }

    if ("product".equals(key)) {
      if (PUBLIC_PRODUCT_READ_METHODS.contains(methodName)) {
        // ingredients 만 GET 허용, 나머지 읽기 메서드는 모두 공개
        needAuth = "ingredients".equals(methodName) ? !"GET".equalsIgnoreCase(httpMethod) : false;
      }
    }

    // ✅ 게시판: 목록/보기(페이지 & 데이터)는 비회원 허용
    if ("board".equals(key) && PUBLIC_BOARD_READ_METHODS.contains(methodName)) {
      needAuth = false;
    }

    // 장바구니 등은 로그인 필수
    if ("cart".equals(key)) {
      needAuth = true;
    }
    
    // ----- 인증 확인 -----
    if (needAuth) {
      HttpSession session = req.getSession(false);
      Object loginUser = (session == null) ? null : session.getAttribute("loginUser");

      if (loginUser == null) {
        if (isAjax(req)) {
          // AJAX 요청은 JSON 401로 응답 (리다이렉트 금지)
          resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          resp.setContentType("application/json; charset=UTF-8");
          resp.getWriter().print("{\"ok\":false,\"reason\":\"AUTH\",\"msg\":\"로그인이 필요합니다.\"}");
          return;
        } else {
          // 일반 요청은 로그인 페이지로
          resp.sendRedirect(req.getContextPath() + "/front?key=user&methodName=loginForm");
          return;
        }
      }
    }

    chain.doFilter(request, response);
  }

  private static String n(String s){ return (s==null) ? "" : s; }
}
