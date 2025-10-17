<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
          <label class="small">이메일</label>
          <div class="row">
            <input id="email" class="input" placeholder="이메일을 입력해주세요" style="flex: 1">
            <button class="btn" id="checkEmail" type="button">중복확인</button>
          </div>
          <label class="small">이름</label>
          <div class="form-row">
            <input id="name" class="input" placeholder="이름을 입력해주세요">
          </div>
          <label class="small">닉네임</label>
          <div class="row">
            <input id="nick" class="input" placeholder="닉네임을 입력해주세요" style="flex: 1">
            <button class="btn" id="checkNick" type="button">중복확인</button>
          </div>
          <label class="small">비밀번호</label>
          <div class="form-row">
            <input id="pass" type="password" class="input" placeholder="비밀번호를 입력해주세요">
          </div>
          <label class="small">비밀번호 확인</label>
          <div class="form-row">
            <input id="pass2" type="password" class="input" placeholder="비밀번호를 다시 입력해주세요">
          </div>
          <label class="small">주소</label>
          <div class="form-row">
            <input id="addr" class="input" placeholder="주소를 입력해주세요">
          </div>
          <label class="small">전화번호</label>
          <div class="form-row">
            <input id="phone" class="input" placeholder="전화번호를 입력해주세요">
          </div>
          <div class="form-row" style="margin-top: 16px">
            <button id="doReg" class="btn dark full" type="button">회원가입</button>
          </div>
          <p class="small" style="text-align: center; margin: 12px 0 0">
            이미 회원이신가요?<br>
            <a href="${path}/users/login.jsp">로그인하러 가기</a>
          </p>
        </section>
      </main>

<script>
document.addEventListener('DOMContentLoaded', function () {
  const $ = s => document.querySelector(s);
  const base = (typeof CONTEXT_PATH !== 'undefined') ? CONTEXT_PATH : '';
  const EMAIL_RE = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

  async function getJSON(url){
    const r = await fetch(url, { credentials: 'include' });
    const text = await r.text(); // 방어: 응답이 JSON인지 확인
    try {
      const j = JSON.parse(text);
      if (!r.ok) throw new Error(j.msg || '요청 실패');
      return j;
    } catch {
      throw new Error('서버가 JSON이 아닌 응답을 반환했습니다:\n' + text.slice(0,120));
    }
  }

  async function postForm(url, data){
    const r = await fetch(url, {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
      body: new URLSearchParams(data).toString()
    });
    const text = await r.text();
    try {
      const j = JSON.parse(text);
      if (!r.ok) throw new Error(j.msg || '요청 실패');
      return j;
    } catch {
      throw new Error('서버가 JSON이 아닌 응답을 반환했습니다:\n' + text.slice(0,120));
    }
  }

  // 이메일 중복확인
  $('#checkEmail').addEventListener('click', async () => {
    const v = $('#email').value.trim();
    if (!v) return alert('이메일을 입력해주세요.');
    if (!EMAIL_RE.test(v)) return alert('이메일 형식이 올바르지 않습니다.');
    try {
      const j = await getJSON(base + '/api/users/check-email?email=' + encodeURIComponent(v));
      alert(j.taken ? '이미 사용 중인 이메일입니다.' : '사용 가능한 이메일입니다.');
    } catch(e){ alert(e.message); }
  });

  // 닉네임 중복확인
  $('#checkNick').addEventListener('click', async () => {
    const v = $('#nick').value.trim();
    if (!v) return alert('닉네임을 입력해주세요.');
    try {
      const j = await getJSON(base + '/api/users/check-nick?nick=' + encodeURIComponent(v));
      alert(j.taken ? '이미 사용 중인 닉네임입니다.' : '사용 가능한 닉네임입니다.');
    } catch(e){ alert(e.message); }
  });

  // 회원가입
  $('#doReg').addEventListener('click', async (e) => {
    e.preventDefault();

    const email = $('#email').value.trim();
    const name = $('#name').value.trim();
    const nickname = $('#nick').value.trim();
    const phone = $('#phone').value.trim();
    const address = $('#addr').value.trim();
    const pass1 = $('#pass').value.trim();
    const pass2 = $('#pass2').value.trim();

    if (!email || !name || !nickname || !phone) return alert('필수 항목을 입력해주세요.');
    if (!EMAIL_RE.test(email)) return alert('이메일 형식이 올바르지 않습니다.');
    if (pass1 !== pass2) return alert('비밀번호가 일치하지 않습니다.');

    try {
    	await postForm(base + '/api/users/register', { email, password: pass1, name, nickname, phone, address });

      alert('가입 및 로그인 완료!');
      location.href = base + '/user/login.jsp'; // '/users/login.jsp'
    } catch(e){ alert(e.message); }
  });
});
</script>

    </body>

    </html>