<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>레시피</title>
    <link rel="stylesheet" href="${path}/css/styles.css" />
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
      const grid = document.querySelector("#list");
      
      onload = () => {
    	grid.innerHTML = "";
    	pageNo = 1;
        printData(false);
      };

      const printData = async function (append = false) {
        body = new URLSearchParams({
          key: "recipe",
          methodName: "selectByOptions",
          word,
          category,
          order,
          pageNo,
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
          let str = "";
          result.forEach((recipe, index) => {
            str += `<article class="card tile">`;
            if (recipe.ATT_FILE_NO_MAIN.substring(0,2) == '..') {
              str += `<div class="thumb" style="background-image: url(${path}/${"${recipe.ATT_FILE_NO_MAIN}"}); background-repeat: no-repeat; background-size: cover; background-position: center;"></div>`;
            } else {
              str += `<div class="thumb" style="background-image: url(${"${recipe.ATT_FILE_NO_MAIN}"}); background-repeat: no-repeat; background-size: cover; background-position: center;"></div>`;
            }
            
            str += `<div class="body">
                <div class="meta">
                  <span class="label">${"${recipe.recipeType.toString().toLowerCase() == 'base' ? '공식 레시피' : '유저 레시피'}"}</span>
                </div>
                <h3 style="margin: 8px 0">${"${recipe.RCP_NM}"}</h3>
                <p class="small">${"${recipe.RCP_NA_TIP}"}</p>
                <div class="bottom">
                <div class="meta">${"${recipe.RCP_PAT2}"} · ${"${recipe.RCP_WAY2}"} · ❤ ${"${recipe.likeCnt}"}</div>
                <div style="display: flex; gap: 8px; align-items: center">
                  <a class="btn dark full" href='${path}/front?key=recipe&methodName=recipeDetail&recipeId=${"${recipe.recipeId}"}'>레시피 보기</a>
                </div>
                </div>
              </div>
            </article>`;
          });
          
          if (append) grid.innerHTML += str;
          else grid.innerHTML = str;
          
        } catch (err) {
          console.error("에러 발생: " + err);
        }
      };
      
      // 검색어 입력
      document.querySelector("#keyword").onkeyup = async (e) => {
    	grid.innerHTML = "";
        word = e.target.value;
        pageNo = 1;
        printData(false);
      }
      
      // 카테고리 변경
      document.querySelector("#category").onchange = async (e) => {
    	grid.innerHTML = "";
        category = e.target.value;
        pageNo = 1;
        printData(false);
      }
      
      // 정렬 버튼 클릭 (토글)
      document.querySelector("#sort").onclick = async (e) => {
    	grid.innerHTML = "";
        e.target.textContent = (e.target.textContent === "최신순") ? "인기순" : "최신순";
        order = (order === "recent") ? "popular" : "recent";
        pageNo = 1;
        printData(false);
      }
      
      // 페이징
      let pageNo = 1;
      let isLoading = false;
      
      window.addEventListener("scroll", async () => {
    	  if (isLoading) return;
    	  
    	  const nearBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - 100;
    	  if (nearBottom) {
    		  isLoading = true;
    		  await loadNextPage();
    		  isLoading = false;
    	  }
      });
      
      const loadNextPage = async function() {
    	  pageNo++;
    	  await printData(true);
      }
      
    </script>
  </body>
</html>
