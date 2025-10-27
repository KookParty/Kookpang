<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@taglib
uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>로그인</title>
    <link rel="stylesheet" href="${path}/css/styles.css" />
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

    <main class="container page">
      <section class="card" style="max-width: 520px; margin: 80px auto; padding: 24px">
        <h3 style="text-align: center; margin-top: 0">로그인</h3>
        <div class="form-row"><input id="email" class="input" placeholder="이메일을 입력해주세요" /></div>
        <div class="form-row">
          <input id="pass" type="password" class="input" placeholder="비밀번호를 입력해주세요" />
        </div>
        <button id="doLogin" class="btn dark full">로그인</button>
        <p class="small" style="text-align: center; margin: 12px 0 0">
          회원이 아니세요? <a href="${path}/users/register.jsp">회원가입하러 가기</a>
        </p>
      </section>
    </main>

    <!-- footer 시작 -->
    <jsp:include page="../common/footer.jsp"></jsp:include>
    <!-- footer 끝 -->

    <script>
    document.getElementById('doLogin').addEventListener('click', async (e) => {
      e.preventDefault();
      const base = (typeof CONTEXT_PATH !== 'undefined') ? CONTEXT_PATH : '';
      const email = document.getElementById('email').value.trim();
      const pass  = document.getElementById('pass').value.trim();

      if (!email || !pass) {
        alert('이메일과 비밀번호를 입력하세요.');
        return;
      }

      try {
        const r = await fetch(base + '/ajax?key=user&methodName=login', {
          method: 'POST',
          credentials: 'include',
          headers: {'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'
          },
          body: new URLSearchParams({ email, password: pass }).toString()
        });

        const text = await r.text();
        const j = JSON.parse(text);
        if (!r.ok || !j.ok) throw new Error(j.msg || '로그인 실패');
        alert('로그인되었습니다.')
        const to = new URL(location.href).searchParams.get('redirect') || (base + '/index.jsp');
        location.href = to;

      } catch (err) {
        alert(err.message);
      }
    });

    document.addEventListener('DOMContentLoaded', () => {
      const msg = localStorage.getItem('flashNext');
      if (msg) {
        alert(msg);
        localStorage.removeItem('flashNext');
      }
    });
    </script>
  </body>
</html>
