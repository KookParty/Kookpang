<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>레시피</title>
    <link rel="stylesheet" href="${path}/css/styles.css" />
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
      <h1 style="text-align: center">레시피 모음</h1>
      <div
        style="
          display: flex;
          gap: 10px;
          justify-content: flex-end;
          margin: 8px 0;
        "
      >
        <input
          id="keyword"
          class="input"
          placeholder="식재료를 검색해보세요..."
          style="flex: 1; min-width: 240px"
        />
        <button id="sort" class="btn">최신순</button>
        <select id="category" class="btn" style="width: 120px">
          <option value="base" disabled selected>카테고리</option>
          <option value="all">전체 레시피</option>
          <option value="base">기본 레시피</option>
          <option value="variant">변형 레시피</option>
        </select>
      </div>
      <section class="grid cards" id="list">
        <!-- 레시피 목록 들어올 공간 -->
      </section>
    </main>
    <!-- footer 시작 -->
    <jsp:include page="../common/footer.jsp"></jsp:include>
    <!-- footer 끝 -->
    
    <script>
      let word = "";
      let category = "base";	// 기본레시피
      let order = "recent"; // 최신순
      
      onload = () => {
        printData();
      };

      const printData = async function () {
        body = new URLSearchParams({
          key: "recipe",
          methodName: "selectByOptions",
          word,
          category,
          order,
        });

        try {
          const response = await fetch(CONTEXT_PATH + "/ajax", {
            method: "POST",
            body,
          });

          if (response.ok == false) {
            throw new Error("서버 응답 에러: " + response.status);
          }

          const result = await response.json();
          console.log("recipe: " + result);
          
          // 데이터 출력
          let list = document.querySelector("#list");
          let str = "";
          result.forEach((recipe, index) => {
            str += `
            <article class="card tile">
              <div class="thumb">
                <img src="${"${recipe.ATT_FILE_NO_MAIN}"}" />
              </div>
              <div class="body">
                <div class="meta">
                  <span class="label">공식 레시피</span>
                  <span class="label green">쉬움</span>
                </div>
                <h3 style="margin: 8px 0">${"${recipe.RCP_NM}"}</h3>
                <p class="small">${"${recipe.RCP_NA_TIP}"}</p>
                <div class="meta">${"${recipe.RCP_PAT2}"} · ${"${recipe.RCP_WAY2}"} · ❤ 좋아요수TODO</div>
                <div style="display: flex; gap: 8px; align-items: center">
                  <a class="btn dark full" href='${path}/front?key=recipe&methodName=recipeDetail&recipeId=${"${recipe.recipeId}"}'>레시피 보기</a>
                  <button class="btn small" data-like="${"${recipe.recipeId}"}">♡</button>
                </div>
              </div>
            </article>`;
          });
          
          list.innerHTML = str;
          
        } catch (err) {
          console.error("에러 발생: " + err);
        }
      };
      
      // 검색어 입력
      document.querySelector("#keyword").onkeyup = async (e) => {
        word = e.target.value;
        printData();
      }
      
      // 카테고리 변경
      document.querySelector("#category").onchange = async (e) => {
        category = e.target.value;
        printData();
      }
      
      // 정렬 버튼 클릭 (토글)
      document.querySelector("#sort").onclick = async (e) => {
        e.target.textContent = (e.target.textContent === "최신순") ? "인기순" : "최신순";
        order = (order === "recent") ? "popular" : "recent";
        printData();
      }
    </script>
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
