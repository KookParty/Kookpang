<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="jakarta.tags.core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>KookPang – 맛있는 요리의 시작</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <style>
        .card.tile {
        display: flex;
          flex-direction: column;
          height: 100%; /* 카드 전체 높이 일정하게 */
        }
      
        .card.tile .body {
          display: flex;
          flex-direction: column;
          justify-content: space-between; /* 내용과 bottom을 위아래로 배치 */
          flex-grow: 1;
          padding: 12px;
        }
      
        .card.tile .bottom {
          margin-top: auto; /* body 안에서 맨 아래로 */
          display: flex;
          flex-direction: column;
          gap: 5px;
        }
      </style>
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
          <p>레시피 확인부터 식재료 구매까지, KookPang에서 한 번에 해결하세요! 전문 레시피와 신선한 재료로 집에서도 다양한 요리를 손쉽게!</p>
          <div class="row"><button class="btn dark" onclick="location.href='${path}/recipes/recipes.jsp'">레시피
              둘러보기</button><button class="btn" onclick="location.href='${path}/orders/ingredients.jsp'">식재료 쇼핑</button>
          </div>
        </section>

        <section class="section">
          <h3>인기 레시피</h3>
          <div id="base" class="grid cards">
            <!-- 카드 반복 -->
          </div>
        </section>

        <section class="section">
          <h3>인기 변형 레시피</h3>
          <div id="variant" class="grid cards">
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
      
      <script>
        const baseGrid = document.querySelector("#base");
        const variGrid = document.querySelector("#variant");
        
        onload = () => {
          printBase();
          printVari();
        };

        const printBase = async function () {
          try {
        	const response = await fetch(CONTEXT_PATH + "/ajax", {
              method: "POST",
              body: new URLSearchParams({
                  key: "recipe",
                  methodName: "selectByOptions",
                  word: "",
                  category: "base",
                  order: "popular",
                  pageNo: 1,
                  pageSize: 3,
                }),
            });
              

            if (response.ok == false) {
              throw new Error("서버 응답 에러: " + response.status);
            }

            const result = await response.json();
            
            // 데이터 출력
            let str = "";
            result.forEach((recipe, index) => {
              str += `<article class="card recipe">`;

              if (recipe.ATT_FILE_NO_MAIN.substring(0,2) == '..') {
                str += `<div class="thumb" style="background-image: url(${path}/${"${recipe.ATT_FILE_NO_MAIN}"}); background-repeat: no-repeat; background-size: cover; background-position: center;"></div>`;
              } else {
                str += `<div class="thumb" style="background-image: url(${"${recipe.ATT_FILE_NO_MAIN}"}); background-repeat: no-repeat; background-size: cover; background-position: center;"></div>`;
              }

              str += `<div class="body">
                  <div class="meta"><span class="label">공식 레시피</span></div>
                  <b>${"${recipe.RCP_NM}"}</b>`;
                  
              if (recipe.RCP_NA_TIP.length > 45) {
            	  let shortCut = recipe.RCP_NA_TIP.substring(0, 45) + "...";
            	  str += `<p class="small" style="margin:0;color:#6b7280">${"${shortCut}"}</p>`;
              } else {
            	  str += `<p class="small" style="margin:0;color:#6b7280">${"${recipe.RCP_NA_TIP}"}</p>`;
              }
              str += `<div class="bottom">
                  <div class="meta">${"${recipe.RCP_PAT2}"} · ${"${recipe.RCP_WAY2}"} · ❤ ${"${recipe.likeCnt}"}</div>
                  <div class="cta">
                    <a class="btn dark" href='${path}/front?key=recipe&methodName=recipeDetail&recipeId=${"${recipe.recipeId}"}'>레시피 보기</a>
                  </div></div>
                </div>
              </article>`;
            });
            
            baseGrid.innerHTML = str;
            
          } catch (err) {
            console.error("에러 발생: " + err);
          }
        }; // printBase end

        const printVari = async function () {
          try {
        	  const response = await fetch(CONTEXT_PATH + "/ajax", {
              method: "POST",
              body: new URLSearchParams({
                  key: "recipe",
                  methodName: "selectByOptions",
                  word: "",
                  category: "variant",
                  order: "popular",
                  pageNo: 1,
                  pageSize: 3,
                }),
            });
              

            if (response.ok == false) {
              throw new Error("서버 응답 에러: " + response.status);
            }

            const result = await response.json();
            console.log("vari recipes: ", result);
            
            // 데이터 출력
            let str = "";
            result.forEach((recipe, index) => {
              str += `<article class="card recipe">`;

              if (recipe.ATT_FILE_NO_MAIN.substring(0,2) == '..') {
                str += `<div class="thumb" style="background-image: url(${path}/${"${recipe.ATT_FILE_NO_MAIN}"}); background-repeat: no-repeat; background-size: cover; background-position: center;"></div>`;
              } else {
                str += `<div class="thumb" style="background-image: url(${"${recipe.ATT_FILE_NO_MAIN}"}); background-repeat: no-repeat; background-size: cover; background-position: center;"></div>`;
              }

              str += `<div class="body">
                  <div class="meta"><span class="label">유저 레시피</span></div>
                  <b>${"${recipe.RCP_NM}"}</b>`;
              
              if (recipe.RCP_NA_TIP.length > 35) {
            	  console.log("length: ", recipe.RCP_NA_TIP.length);
            	  let shortCut = recipe.RCP_NA_TIP.substring(0, 35) + "...";
            	  str += `<p class="small" style="margin:0;color:#6b7280">${shortCut}</p>`;
              } else {
            	  str += `<p class="small" style="margin:0;color:#6b7280">${"${recipe.RCP_NA_TIP}"}</p>`;
              }
              str += `<div class="bottom">
                  <div class="meta">${"${recipe.RCP_PAT2}"} · ${"${recipe.RCP_WAY2}"} · ❤ ${"${recipe.likeCnt}"}</div>
                  <div class="cta">
                    <a class="btn dark" href='${path}/front?key=recipe&methodName=recipeDetail&recipeId=${"${recipe.recipeId}"}'>레시피 보기</a>
                  </div></div>
                </div>
              </article>`;
            });
            
            variGrid.innerHTML = str;
            
          } catch (err) {
            console.error("에러 발생: " + err);
          }
        };
      </script>
    </body>

    </html>