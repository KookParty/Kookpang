<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header class="header">
  <div class="container bar wide">
    <div style="display: flex; align-items: center">
      <div class="brand">
        <div class="logo">🍳</div>
        <a href="${path}/index.jsp"><strong>KookPang</strong></a>
      </div>
      <nav class="nav">
        <a class="active" href="${path}/index.jsp">홈</a>
        <a href="${path}/recipes/recipes.jsp">레시피</a>
        <a href="${path}/front?key=product&methodName=ingredients">식재료</a>
        <a href="${path}/boards/board.jsp">자유게시판</a>
      </nav>
    </div>
    <div class="actions">
      <a class="iconbtn" id="cartBtn" href="${path}/orders/cart.jsp">🛒</a>
      <a id="js-mypage-link" class="iconbtn" href="${path}/users/mypage.jsp" data-need-auth>👤</a>
      <span id="js-nickname" class="badge">닉네임</span>
      <button id="js-login-btn" class="btn" onclick="location.href='${path}/users/login.jsp'">로그인</button>
      <button id="js-logout-btn" class="btn">로그아웃</button>
    </div>
  </div>
</header>
<script>
  (function () {
    const getCartHeader = async function () {
      const res = await fetch("${path}/ajax", {
        method: "POST",
        body: new URLSearchParams({
          key: "cart",
          methodName: "countCart",
        }),
      });
      if (!res.ok) {
        console.error("Failed to fetch cart header:", res.status, res.statusText);
        return 0;
      }
      const text = await res.text();
      try {
        return JSON.parse(text);
      } catch (e) {
        return Number(text) || 0;
      }
    };
    const cartCount = async function () {
      const data = await getCartHeader();
      return data;
    };
    const ensureBadge = async function () {
      var a = document.getElementById("cartBtn");
      if (!a) return;
      if (getComputedStyle(a).position === "static") {
        a.style.position = "relative";
      }
      var b = document.getElementById("js-cart-badge");
      if (!b) {
        b = document.createElement("span");
        b.id = "js-cart-badge";
        b.className = "cart-badge";
        a.appendChild(b);
      }
      var c = await cartCount();
      b.textContent = c > 0 ? String(c) : "";
      b.style.display = c > 0 ? "inline-flex" : "none";
    };
    window.kpUpdateCartBadge = ensureBadge;
    document.addEventListener("DOMContentLoaded", ensureBadge);
    window.addEventListener("storage", ensureBadge);
  })();
</script>
