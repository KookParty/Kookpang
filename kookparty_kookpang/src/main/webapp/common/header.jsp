<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <header class="header">
      <div class="container bar wide">
        <div style="display:flex;align-items:center">
          <div class="brand">
            <div class="logo">🍳</div><a href="${path}/index.jsp"><strong>KookPang</strong></a>
          </div>
          <nav class="nav">
            <a class="active" href="${path}/index.jsp">홈</a>
            <a href="${path}/recipes/recipes.jsp">레시피</a>
            <a href="${path}/orders/ingredients.jsp">식재료</a>
            <a href="${path}/boards/board.jsp">자유게시판</a>
          </nav>
        </div>
        <div class="actions">
          <a class="iconbtn" id="cartBtn" href="${path}/orders/order-review.jsp">🛒</a>
          <a id="js-mypage-link" class="iconbtn" href="${path}/users/mypage.jsp" data-need-auth
            style="display:none">👤</a>
          <span id="js-nickname" class="badge" style="display:none">닉네임</span>
          <button id="js-login-btn" class="btn" onclick="location.href='${path}/users/login.jsp'">로그인</button>
          <button id="js-logout-btn" class="btn" style="display:none">로그아웃</button>
        </div>
      </div>
    </header>