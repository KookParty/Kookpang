<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>레시피</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <main class="container page">
        <h1 style="text-align:center">레시피 모음</h1>
        <div style="display:flex;gap:10px;justify-content:flex-end;margin:8px 0"><button class="btn">인기순</button><button
            class="btn">모든 난이도 ▾</button></div>
        <section class="grid cols-2" id="list">
          <!-- 반복 시작 -->
          <article class="card tile">
            <div class="thumb">
              <img src="${path}/assets/img/bulgogi.jpg">
            </div>
            <div class="body">
              <div class="meta">
                <span class="label">공식 레시피</span>
                <span class="label green">쉬움</span>
              </div>
              <h3 style="margin:8px 0">불고기</h3>
              <p class="small">달콤짭짤한 한국 전통 불고기</p>
              <div class="meta">⏱️ 45분 · 👥 3인분 · ❤ 892</div>
              <div style="display:flex;gap:8px;align-items:center">
                <a class="btn dark full" href="recipe-detail.jsp?id=bulgogi">레시피 보기</a>
                <button class="btn" style="margin-left:8px" data-add-recipe="bulgogi">담기</button>
                <button class="btn small" data-like="bulgogi">♡</button>
              </div>
            </div>
          </article>
          <!-- 반복 끝 -->
          <article class="card tile">
            <div class="thumb">
              <img src="${path}/assets/img/bulgogi.jpg">
            </div>
            <div class="body">
              <div class="meta">
                <span class="label">공식 레시피</span>
                <span class="label green">쉬움</span>
              </div>
              <h3 style="margin:8px 0">불고기</h3>
              <p class="small">달콤짭짤한 한국 전통 불고기</p>
              <div class="meta">⏱️ 45분 · 👥 3인분 · ❤ 892</div>
              <div style="display:flex;gap:8px;align-items:center">
                <a class="btn dark full" href="recipe-detail.jsp?id=bulgogi">레시피 보기</a>
                <button class="btn" style="margin-left:8px" data-add-recipe="bulgogi">담기</button>
                <button class="btn small" data-like="bulgogi">♡</button>
              </div>
            </div>
          </article>
          <article class="card tile">
            <div class="thumb">
              <img src="${path}/assets/img/bulgogi.jpg">
            </div>
            <div class="body">
              <div class="meta">
                <span class="label">공식 레시피</span>
                <span class="label green">쉬움</span>
              </div>
              <h3 style="margin:8px 0">불고기</h3>
              <p class="small">달콤짭짤한 한국 전통 불고기</p>
              <div class="meta">⏱️ 45분 · 👥 3인분 · ❤ 892</div>
              <div style="display:flex;gap:8px;align-items:center">
                <a class="btn dark full" href="recipe-detail.jsp?id=bulgogi">레시피 보기</a>
                <button class="btn" style="margin-left:8px" data-add-recipe="bulgogi">담기</button>
                <button class="btn small" data-like="bulgogi">♡</button>
              </div>
            </div>
          </article>
          <article class="card tile">
            <div class="thumb">
              <img src="${path}/assets/img/bulgogi.jpg">
            </div>
            <div class="body">
              <div class="meta">
                <span class="label">공식 레시피</span>
                <span class="label green">쉬움</span>
              </div>
              <h3 style="margin:8px 0">불고기</h3>
              <p class="small">달콤짭짤한 한국 전통 불고기</p>
              <div class="meta">⏱️ 45분 · 👥 3인분 · ❤ 892</div>
              <div style="display:flex;gap:8px;align-items:center">
                <a class="btn dark full" href="recipe-detail.jsp?id=bulgogi">레시피 보기</a>
                <button class="btn" style="margin-left:8px" data-add-recipe="bulgogi">담기</button>
                <button class="btn small" data-like="bulgogi">♡</button>
              </div>
            </div>
          </article>
          <!-- 반복 -->
        </section>
      </main>
      <!-- footer 시작 -->
      <jsp:include page="../common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <!--
      <script>
        const likes = new Set(S.get(KP_KEYS.LIKES, [])), list = document.getElementById('list');
        RECIPES.forEach(r => {
          const el = document.createElement('article');
          el.className = 'card tile';
          el.innerHTML = `
          <div class="thumb">
            <img src='../${"${r.img}"}''>
          </div>
          <div class="body">
            <div class="meta">
              <span class="label">${"${r.label}"}</span>
              <span class="label" style="background:#10b981">${"${r.difficulty}"}</span>
            </div>
            <h3 style="margin:8px 0">${"${r.title}"}</h3>
            <p class="small">${"${r.desc}"}</p>
            <div class="meta">⏱️ ${"${r.time}"} · 👥 ${"${r.serves}"} · ❤ ${"${r.likes}"}</div>
            <div style="display:flex;gap:8px;align-items:center">
              <a class="btn dark full" href="recipe-detail.jsp?id=${"${r.id}"}">레시피 보기</a>
              <button class="btn" style="margin-left:8px" data-add-recipe='${"${r.id}"}'>담기</button>
              <button class="btn small" data-like='${"${r.id}"}'>${"${likes.has(r.id) ? '♥' : '♡'}"}</button>
            </div>
          </div>`;
          list.appendChild(el);
        });
        document.addEventListener('click', e => { const id = e.target?.dataset?.like; if (!id) return; const arr = S.get(KP_KEYS.LIKES, []); const i = arr.indexOf(id); if (i >= 0) { arr.splice(i, 1); e.target.textContent = '♡' } else { arr.push(id); e.target.textContent = '♥' } S.set(KP_KEYS.LIKES, arr) });
      </script>
      -->
    </body>

    </html>