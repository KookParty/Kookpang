<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
          const usedEmails = new Set((JSON.parse(localStorage.getItem('kp_users') || '[]')).map(u => u.email));
          const usedNicks = new Set((JSON.parse(localStorage.getItem('kp_users') || '[]')).map(u => u.nickname));

          document.getElementById('checkEmail').addEventListener('click', function () {
            const v = document.getElementById('email').value.trim();
            if (!v) return alert('이메일을 입력해주세요.');
            alert(usedEmails.has(v) ? '이미 사용 중인 이메일입니다.' : '사용 가능한 이메일입니다.');
          });

          document.getElementById('checkNick').addEventListener('click', function () {
            const v = document.getElementById('nick').value.trim();
            if (!v) return alert('닉네임을 입력해주세요.');
            alert(usedNicks.has(v) ? '이미 사용 중인 닉네임입니다.' : '사용 가능한 닉네임입니다.');
          });

          document.getElementById('doReg').addEventListener('click', function (e) {
            e.preventDefault();
            const u = {
              email: document.getElementById('email').value.trim(),
              name: document.getElementById('name').value.trim(),
              nickname: document.getElementById('nick').value.trim(),
              password: document.getElementById('pass').value.trim(),
              address: document.getElementById('addr').value.trim(),
              phone: document.getElementById('phone').value.trim()
            };
            const pass2 = document.getElementById('pass2').value.trim();

            if (!u.email || !u.name || !u.nickname || !u.password) return alert('필수 항목을 입력해주세요.');
            if (u.password !== pass2) return alert('비밀번호가 일치하지 않습니다.');
            const users = JSON.parse(localStorage.getItem('kp_users') || '[]');
            if (users.find(x => x.email === u.email)) return alert('이미 가입된 이메일입니다.');

            users.push(u);
            localStorage.setItem('kp_users', JSON.stringify(users));
            // 세션 로그인까지 처리
            localStorage.setItem('kp_user', JSON.stringify(u));
            alert('가입 및 로그인 완료!');
            location.href = '${path}/index.jsp';
          });
        });
      </script>
    </body>

    </html>