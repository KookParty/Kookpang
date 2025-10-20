<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    const text = await res.json();
    console.log("cart header response:", text);
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
    console.log("cart count:", c);
    b.textContent = c > 0 ? String(c) : "";
    b.style.display = c > 0 ? "inline-flex" : "none";
  };
  (function () {
    ensureBadge();
    window.kpUpdateCartBadge = ensureBadge;
    document.addEventListener("DOMContentLoaded", ensureBadge);
    window.addEventListener("storage", ensureBadge);
  })();
</script>

<!-- 로그인/아웃-->
<script>
(function () {
  const BASE = '${path}';
  let IS_AUTH = false;

  async function callAjax(body) {
    const r = await fetch(BASE + '/ajax', {
      method: 'POST',
      credentials: 'include', // 로그인 유지
      headers: {'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},
      body: new URLSearchParams(body).toString()
    });
    const t = await r.text();
    try {
      const j = JSON.parse(t);
      if (!r.ok) throw new Error(j.msg || '요청 실패');
      return j;
    } catch {
      
      throw new Error('서버가 JSON이 아닌 응답을 반환했습니다:\n' + t.slice(0,120));
    }
  }

  // 로그인 상태에 따른 헤더
  function updateAuthUI(user) {
    const $nick = document.getElementById('js-nickname');
    const $login = document.getElementById('js-login-btn');
    const $logout = document.getElementById('js-logout-btn');
    const $mypage = document.getElementById('js-mypage-link');

    if (user) {
      // 로그인 상태
      IS_AUTH = true;
      if ($nick) { $nick.textContent = user.nickname || user.email || '사용자'; $nick.style.display = 'inline-block'; }
      if ($logout) $logout.style.display = 'inline-block';
      if ($login)  $login.style.display = 'none';
      if ($mypage) $mypage.href = BASE + '/users/mypage.jsp';
    } else {
      // 로그아웃 상태
      IS_AUTH = false;
      if ($nick) { $nick.textContent = '닉네임'; $nick.style.display = 'none'; }
      if ($logout) $logout.style.display = 'none';
      if ($login)  $login.style.display = 'inline-block';
      // 마이페이지 클릭 시 로그인 화면으로 리다이렉트 (redirect 파라미터 유지)
      if ($mypage) $mypage.href = BASE + '/users/login.jsp?redirect=' + encodeURIComponent(location.pathname + location.search);
    }
  }

  // 현재 세션 확인
  async function checkLogin() {
    try {
      //key=user, methodName=me 호출  로그인 중이면 user 정보 반환
      const j = await callAjax({ key: 'user', methodName: 'me' });
      updateAuthUI(j.user);
    } catch (e) {
      console.warn('로그인 상태 확인 실패:', e.message);
      updateAuthUI(null);
    }
  }

  // 로그아웃
function bindLogout() {
  const $logout = document.getElementById('js-logout-btn');
  if (!$logout) return;
  $logout.addEventListener('click', async (e) => {
    e.preventDefault();
    try {
      const res = await callAjax({ key: 'user', methodName: 'logout' });
      alert(res.msg || '로그아웃되었습니다.');
      location.reload(); 
    } catch (err) {
      alert(err.message || '로그아웃 중 오류가 발생했습니다.');
    }
  });
}

  // 로그인하지 않은 상태에서 인증 필요한 버튼(data-need-auth)을 누르면 로그인 페이지로 이동
  function guardAuthLinks() {
    document.querySelectorAll('[data-need-auth]').forEach(el => {
      el.addEventListener('click', (e) => {
        if (!IS_AUTH) {
          e.preventDefault();
          // 현재 페이지 경로를 redirect 파라미터로 넘겨서 로그인 후 복귀 가능
          location.href = BASE + '/users/login.jsp?redirect=' + encodeURIComponent(location.pathname + location.search);
        }
      });
    });
  }

  // 페이지 로드 시 한 번만 실행
  document.addEventListener('DOMContentLoaded', () => {
    checkLogin();   // 세션 존재 여부 확인
    bindLogout();   // 로그아웃 버튼 활성화
    guardAuthLinks(); // 인증 가드 등록
  });
})();
</script>

