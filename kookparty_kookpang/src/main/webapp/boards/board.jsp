<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>자유게시판</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script>const PAGE_ACTIVE = 'board'</script>
      <script src="${path}/js/app.js"></script>
      <script src="${path}/js/seed.js"></script>
    </head>

    <body>
      <!-- header include -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header include -->

      <script>document.addEventListener('DOMContentLoaded', () => { initHeader(PAGE_ACTIVE); });</script>
      <main class="container page">
        <h2>자유게시판</h2>
        <p class="small">요리 팁과 경험을 나누어보세요</p>
        <div class="form-row"><input class="input" id="search" placeholder="게시글을 검색하세요..."></div>
        <div style="display:flex;justify-content:flex-end"><button class="btn">최신순 ▾</button><button class="btn dark"
            id="newBtn" style="margin-left:8px">+ 글쓰기</button></div>
        <section id="posts" class="grid" style="margin-top:16px"></section>

      </main>
      <!-- footer 시작 -->
      <jsp:include page="../common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <script>
        const posts = (S.get(KP_KEYS.POSTS, []));
        function render() {
          const wrap = document.getElementById('posts');
          wrap.innerHTML = '';
          (posts.slice().reverse()).forEach(p => {
            const el = document.createElement('article');
            el.className = 'card';
            el.style.padding = '16px';
            el.innerHTML = `
            <div style="display:flex;justify-content:space-between;align-items:center">
              <h3 style="margin:0">\${p.title}</h3>
              <span class="small">\${new Date(p.created).toLocaleDateString()}</span>
            </div>
            <p class="small" style="margin:6px 0 8px">👤 \${p.nickname || '익명'}</p>
            <p>\${p.body.replace(/\\n/g, '<br>')}</p>
            <div class="meta" style="justify-content:flex-end">
              <span>❤ \${p.likes || 89}</span>
              <span>💬 \${p.comments || 15}</span>
            </div>
          `;
            wrap.appendChild(el);
          });
        }
        render();
        document.getElementById('save').addEventListener('click', () => { const nickname = isAuthed() ? getUser().nickname : '손님'; const title = document.getElementById('title').value.trim(); const body = document.getElementById('body').value.trim(); if (!title || !body) { alert('제목과 내용을 입력하세요.'); return; } posts.push({ id: Date.now(), nickname, title, body, created: Date.now() }); S.set(KP_KEYS.POSTS, posts); title.value = ''; body.value = ''; render(); });

      </script>

      <!-- ✅ 추가: 글쓰기 버튼을 board-write.html로 이동 (다른 건 건드리지 않음) -->
      <script>
        document.addEventListener('DOMContentLoaded', () => {
          const newBtn = document.getElementById('newBtn');
          if (newBtn) {
            newBtn.addEventListener('click', () => {
              location.href = 'board-write.jsp';
            });
          }
        });
      </script>
    </body>

    </html>