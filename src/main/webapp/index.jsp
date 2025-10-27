<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="jakarta.tags.core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>KookPang – 맛있는 요리의 시작</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <main class="container wide page">
        <section class="hero">
          <h1>맛있는 요리의 시작</h1>
          <p>레시피부터 식재료까지, KookPang에서 한 번에 해결하세요. 전문가 레시피와 신선한 재료로 집에서도 레스토랑 수준의 요리를!</p>
          <div class="row"><button class="btn dark" onclick="location.href='${path}/recipes/recipes.jsp'">레시피
              둘러보기</button><button class="btn" onclick="location.href='${path}/orders/ingredients.jsp'">식재료 쇼핑</button>
          </div>
        </section>

        <section class="section">
          <h3>인기 레시피</h3>
          <div id="popular" class="grid cards">
            <!-- 카드 반복 -->
            <article class="card recipe">
              <div class="thumb"><img src="${path}/assets/img/bulgogi.jpg" alt="불고기"></div>
              <div class="body">
                <div class="meta"><span class="label">공식레시피</span><span class="label red">어려움</span></div>
                <b>불고기</b>
                <p class="small" style="margin:0;color:#6b7280">달콤짭짤한 한국 전통 불고기</p>
                <div class="meta">⏱️ 45분 · 👥 3인분 · ❤ 892</div>
                <div class="cta">
                  <a class="btn dark" href="${path}/recipes/recipe-detail.jsp?id=bulgogi">레시피 보기</a>
                </div>
              </div>
            </article>
            <article class="card recipe">
              <div class="thumb"><img src="${path}/assets/img/bulgogi.jpg" alt="불고기"></div>
              <div class="body">
                <div class="meta"><span class="label">공식레시피</span><span class="label red">어려움</span></div>
                <b>불고기</b>
                <p class="small" style="margin:0;color:#6b7280">달콤짭짤한 한국 전통 불고기</p>
                <div class="meta">⏱️ 45분 · 👥 3인분 · ❤ 892</div>
                <div class="cta">
                  <a class="btn dark" href="${path}/recipes/recipe-detail.jsp?id=bulgogi">레시피 보기</a>
                </div>
              </div>
            </article>
            <article class="card recipe">
              <div class="thumb"><img src="${path}/assets/img/bulgogi.jpg" alt="불고기"></div>
              <div class="body">
                <div class="meta"><span class="label">공식레시피</span><span class="label red">어려움</span></div>
                <b>불고기</b>
                <p class="small" style="margin:0;color:#6b7280">달콤짭짤한 한국 전통 불고기</p>
                <div class="meta">⏱️ 45분 · 👥 3인분 · ❤ 892</div>
                <div class="cta">
                  <a class="btn dark" href="${path}/recipes/recipe-detail.jsp?id=bulgogi">레시피 보기</a>
                </div>
              </div>
            </article>
            <!-- 카드 반복 -->
          </div>
        </section>

        <section class="section">
          <h3>인기 변형 레시피</h3>
          <div id="variants" class="grid cards">
            <!-- 카드 반복 -->
            <article class="card recipe">
              <div class="thumb"><img src="${path}/assets/img/kimchi.jpg" alt="김치찌개"></div>
              <div class="body">
                <div class="meta"><span class="label">변형레시피</span><span class="label green">쉬움</span></div>
                <b>김치찌개</b>
                <p class="small" style="margin:0;color:#6b7280">한국인의 소울푸드, 얼큰하고 시원한 김치찌개</p>
                <div class="meta">⏱️ 30분 · 👥 4인분 · ❤ 1247</div>
                <div class="cta">
                  <a class="btn dark" href="${path}/recipes/recipe-detail.jsp?id=kimchi">레시피 보기</a>
                </div>
              </div>
            </article>
            <!-- 카드 반복 -->
          </div>
        </section>
      </main>
      <!-- footer 시작 -->
      <jsp:include page="common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <!--
      <script>
        function card(r) {
          return `
  <article class="card recipe">
    <div class="thumb"><img src="${'${r.img}'}" alt="${'${r.title}'}"></div>
    <div class="body">
      <div class="meta"><span class="label">${"${r.label || '추천'}"}</span><span class="label green">${"${r.difficulty || '쉬움'}"}</span></div>
      <b>${r.title}</b>
      <p class="small" style="margin:0;color:#6b7280">${"${r.desc || ''}"}</p>
      <div class="meta">⏱️ ${"${r.time || '-'}"} · 👥 ${"${r.serves || '-'}"} · ❤ ${"${r.likes || 0}"}</div>
      <div class="cta">
        <a class="btn dark" href="${path}/recipes/recipe-detail.jsp?id=${"${r.id}"}">레시피 보기</a>
      </div>
    </div>
  </article>`;
        }

        (function render() {
          const pop = document.getElementById('popular');
          pop.innerHTML = RECIPES.slice(0, 6).map(card).join('');
          const v = document.getElementById('variants');
          // 변형 레시피는 더미로 인기 3개 재사용
          v.innerHTML = RECIPES.slice(0, 3).map(card).join('');
        })();
      </script>
      -->
    </body>

    </html>