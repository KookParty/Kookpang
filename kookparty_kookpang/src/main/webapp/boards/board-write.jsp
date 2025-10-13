<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>게시글 작성 - 자유게시판</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script>const PAGE_ACTIVE = 'board';</script>
      <script src="${path}/js/app.js"></script>
      <script src="${path}/js/seed.js"></script>
      <style>
        .editor-wrap {
          background: #fff;
          border: 1px solid #e5e7eb;
          border-radius: 12px;
          overflow: hidden
        }

        .editor-toolbar {
          display: flex;
          gap: 6px;
          align-items: center;
          padding: 8px 10px;
          border-bottom: 1px solid #eee;
          background: #fafafa
        }

        .tool-btn {
          border: 1px solid #e5e7eb;
          background: #fff;
          border-radius: 8px;
          padding: 6px 10px;
          cursor: pointer;
          font-size: 13px
        }

        .tool-btn:active {
          transform: translateY(1px)
        }

        .editor-area {
          min-height: 280px;
          padding: 14px;
          outline: none
        }

        .editor-area:empty:before {
          content: '내용을 입력하세요';
          color: #9ca3af
        }

        .form-row {
          margin: 12px 0
        }

        .input {
          width: 100%;
          padding: 12px 14px;
          border: 1px solid #e5e7eb;
          border-radius: 12px
        }

        .file-row label {
          display: inline-block;
          margin-right: 8px
        }

        .preview {
          max-width: 100%;
          border-radius: 12px;
          margin-top: 10px;
          display: none
        }
      </style>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <script>document.addEventListener('DOMContentLoaded', () => { if (typeof initHeader === 'function') initHeader('board'); });</script>

      <main class="container page" style="max-width:860px">
        <h2 style="margin:10px 0 14px">게시글 작성</h2>

        <article class="card" style="padding:16px">
          <div class="form-row">
            <input id="titleInput" class="input" placeholder="제목(*)">
          </div>

          <div class="editor-wrap">
            <div class="editor-toolbar">
              <select id="fontSize" class="input" style="width:auto;padding:6px 10px">
                <option value="3">9pt</option>
                <option value="4" selected>12pt</option>
                <option value="5">14pt</option>
                <option value="6">18pt</option>
              </select>
              <button type="button" class="tool-btn" data-cmd="bold">굵게</button>
              <button type="button" class="tool-btn" data-cmd="italic">기울임</button>
              <button type="button" class="tool-btn" data-cmd="underline">밑줄</button>
              <button type="button" class="tool-btn" data-cmd="insertUnorderedList">• 목록</button>
              <button type="button" class="tool-btn" data-cmd="insertOrderedList">1. 목록</button>
              <button type="button" class="tool-btn" id="btnLink">링크</button>
            </div>
            <div id="editor" class="editor-area" contenteditable="true"></div>
          </div>

          <div class="form-row file-row">
            <label>파일1 <input type="file" id="file1" accept="image/*"></label>
            <label>파일2 <input type="file" id="file2" accept="image/*"></label>
            <label>파일3 <input type="file" id="file3" accept="image/*"></label>
            <img id="preview" class="preview">
          </div>

          <div style="display:flex;justify-content:flex-end;gap:8px">
            <a class="btn" href="${path}/boards/board.jsp">취소</a>
            <button class="btn dark" id="saveBtn">확인</button>
          </div>
        </article>
      </main>
      <!-- footer 시작 -->
      <jsp:include page="../common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <script>
        (function () {
          const get = s => document.querySelector(s);
          const getAll = s => document.querySelectorAll(s);

          // toolbar
          getAll('.tool-btn').forEach(function (b) {
            b.addEventListener('click', function () {
              const cmd = b.getAttribute('data-cmd');
              document.execCommand(cmd, false, null);
            });
          });
          get('#fontSize').addEventListener('change', function (e) {
            document.execCommand('fontSize', false, e.target.value);
          });
          get('#btnLink').addEventListener('click', function () {
            const url = prompt('링크 URL을 입력하세요', 'https://');
            if (url) document.execCommand('createLink', false, url);
          });

          // preview (first img)
          ['file1', 'file2', 'file3'].forEach(function (id) {
            const input = document.getElementById(id);
            input.addEventListener('change', function (e) {
              const f = e.target.files && e.target.files[0];
              if (!f) return;
              const r = new FileReader();
              r.onload = function (ev) {
                const img = get('#preview');
                img.src = ev.target.result; img.style.display = 'block';
              };
              r.readAsDataURL(f);
            });
          });

          // save
          get('#saveBtn').addEventListener('click', function () {
            const title = (get('#titleInput').value || '').trim();
            const bodyHtml = get('#editor').innerHTML.trim();
            if (!title || !bodyHtml) { alert('제목과 내용을 입력하세요.'); return; }

            // nickname
            var nickname = '익명';
            try {
              var raw = localStorage.getItem('user') || localStorage.getItem('currentUser') || localStorage.getItem('authUser');
              if (raw) { var u = JSON.parse(raw); nickname = u.nickname || u.nick || nickname; }
            } catch (e) { { } }

            try {
              if (typeof KP_KEYS !== 'undefined' && typeof S !== 'undefined' && S.get && S.set) {
                var list = S.get(KP_KEYS.POSTS, []);
                list.push({ id: Date.now(), nickname: nickname, title: title, body: bodyHtml, created: Date.now(), likes: 0, comments: 0 });
                S.set(KP_KEYS.POSTS, list);
              }
            } catch (e) { { } }

            alert('게시글이 등록되었습니다.');
            location.href = '${path}/boards/board.jsp';
          });
        })();
      </script>
    </body>

    </html>