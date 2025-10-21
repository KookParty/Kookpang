<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="ko">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1">
  <title>회원가입</title>
  <link rel="stylesheet" href="${path}/css/styles.css">
  <script type="text/javascript">
    const CONTEXT_PATH = "${pageContext.request.contextPath}";
  </script>
  <script src="${path}/js/config.js"></script>
  <script src="${path}/js/app.js"></script>
  <script src="${path}/js/seed.js"></script>
</head>

<body>
  <!-- header시작 -->
  <jsp:include page="../common/header.jsp"></jsp:include>
  <!-- header끝 -->
  <script>document.addEventListener('DOMContentLoaded', () => { initHeader(''); });</script>

  <main class="container page">
    <section class="card" style="max-width: 520px; margin: 60px auto; padding: 24px">
      <h3 style="text-align: center; margin-top: 0">회원가입</h3>

      <!-- 서버 검증 실패 메시지 -->
      <c:if test="${not empty errorMsg}">
        <div class="alert danger" style="margin-bottom:12px">${errorMsg}</div>
      </c:if>

      <form id="registerForm" method="post" action="${path}/front?key=user&methodName=registerSubmit" novalidate>
        <label class="small">이메일</label>
        <div class="row">
          <input id="email" name="email" type="email" class="input" placeholder="이메일을 입력해주세요" style="flex:1"
                 value="${empty formEmail ? param.email : formEmail}" required>
          <button class="btn" id="checkEmail" type="button">중복확인</button>
        </div>

        <label class="small">이름</label>
        <div class="form-row">
          <input id="name" name="name" class="input" placeholder="이름을 입력해주세요"
                 value="${empty formName ? param.name : formName}" required>
        </div>

        <label class="small">닉네임</label>
        <div class="row">
          <input id="nick" name="nickname" class="input" placeholder="닉네임을 입력해주세요" style="flex:1"
                 value="${empty formNick ? param.nickname : formNick}" required>
          <button class="btn" id="checkNick" type="button">중복확인</button>
        </div>

        <label class="small">비밀번호</label>
        <div class="form-row">
          <input id="pass" name="password" type="password" class="input" placeholder="비밀번호를 입력해주세요" required>
        </div>

        <label class="small">비밀번호 확인</label>
        <div class="form-row">
          <input id="pass2" type="password" class="input" placeholder="비밀번호를 다시 입력해주세요" required>
        </div>

        <label class="small">주소</label>
        <div class="form-row">
          <input id="addr" name="address" class="input" placeholder="주소를 입력해주세요"
                 value="${empty formAddr ? param.address : formAddr}">
        </div>

        <label class="small">전화번호</label>
        <div class="form-row">
          <input id="phone" name="phone" class="input" placeholder="전화번호를 입력해주세요"
                 value="${empty formPhone ? param.phone : formPhone}" required>
        </div>

        <div class="form-row" style="margin-top: 16px">
          <button class="btn dark full" type="submit">회원가입</button>
        </div>
      </form>

      <p class="small" style="text-align: center; margin: 12px 0 0">
        이미 회원이신가요?<br>
        <a href="${path}/front?key=user&methodName=loginForm">로그인하러 가기</a>
      </p>
    </section>
  </main>

<script>
(function () {
  const $ = s => document.querySelector(s);
  const base = (typeof CONTEXT_PATH !== 'undefined') ? CONTEXT_PATH : '';
  const EMAIL_RE = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

  let emailChecked = false;
  let nickChecked = false;
  let lastCheckedEmail = '';
  let lastCheckedNick = '';

  async function getJSON(url){
    const r = await fetch(url, { credentials: 'include' });
    const text = await r.text();
    try {
      const j = JSON.parse(text);
      if (!r.ok) throw new Error(j.msg || '요청 실패');
      return j;
    } catch {
      throw new Error('서버가 JSON이 아닌 응답을 반환했습니다:\n' + text.slice(0,120));
    }
  }

  // -----------------------------
  // ✅ 이메일 중복확인
  // -----------------------------
  $('#checkEmail')?.addEventListener('click', async () => {
    const v = $('#email').value.trim();
    if (!v) return alert('이메일을 입력해주세요.');
    if (!EMAIL_RE.test(v)) return alert('이메일 형식이 올바르지 않습니다.');
    try {
      const j = await getJSON(base + '/ajax?key=user&methodName=checkEmail&email=' + encodeURIComponent(v));
      if (!j.ok) return alert(j.msg || '이메일 확인 실패');
      if (j.taken) {
        alert('이미 사용 중인 이메일입니다.');
        emailChecked = false;
      } else {
        alert('사용 가능한 이메일입니다.');
        emailChecked = true;
        lastCheckedEmail = v; // ✅ 입력값 기억
      }
    } catch(e){ alert(e.message); }
  });

  // -----------------------------
  // ✅ 닉네임 중복확인
  // -----------------------------
  $('#checkNick')?.addEventListener('click', async () => {
    const v = $('#nick').value.trim();
    if (!v) return alert('닉네임을 입력해주세요.');
    try {
      const j = await getJSON(base + '/ajax?key=user&methodName=checkNick&nick=' + encodeURIComponent(v));
      if (!j.ok) return alert(j.msg || '닉네임 확인 실패');
      if (j.taken) {
        alert('이미 사용 중인 닉네임입니다.');
        nickChecked = false;
      } else {
        alert('사용 가능한 닉네임입니다.');
        nickChecked = true;
        lastCheckedNick = v;
      }
    } catch(e){ alert(e.message); }
  });
  document.getElementById('registerForm')?.addEventListener('submit', (e) => {
    const email = $('#email').value.trim();
    const name = $('#name').value.trim();
    const nick = $('#nick').value.trim();
    const phone = $('#phone').value.trim();
    const pass1 = $('#pass').value.trim();
    const pass2 = $('#pass2').value.trim();

    if (!email || !name || !nick || !phone || !pass1 || !pass2) {
      e.preventDefault();
      alert('필수 항목을 모두 입력해주세요.');
      return;
    }
    if (!EMAIL_RE.test(email)) {
      e.preventDefault();
      alert('이메일 형식이 올바르지 않습니다.');
      return;
    }
    if (pass1 !== pass2) {
      e.preventDefault();
      alert('비밀번호가 일치하지 않습니다.');
      return;
    }

    if (!emailChecked || lastCheckedEmail !== email) {
      e.preventDefault();
      alert('이메일 중복확인을 해주세요.');
      return;
    }
    if (!nickChecked || lastCheckedNick !== nick) {
      e.preventDefault();
      alert('닉네임 중복확인을 해주세요.');
      return;
    }

    localStorage.setItem('flashNext', '회원가입이 완료되었습니다. 로그인해 주세요.');
  });
})();
</script>

</body>
</html>
