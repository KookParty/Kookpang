<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> 
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>레시피 상세 – 김치찌개</title>
    <link rel="stylesheet" href="${path}/css/styles.css" />
    <style>
      body {
        background: #f7f8fa;
        font-family: ui-sans-serif, system-ui, AppleSDGothicNeo, "Noto Sans KR", Arial;
      }

      .wrap {
        max-width: 980px;
        margin: 0 auto;
        padding: 16px;
      }

      .hero {
        display: flex;
        gap: 16px;
        align-items: center;
      }

      .hero img {
        width: 280px;
        height: 180px;
        object-fit: cover;
        border-radius: 12px;
      }

      .title {
        font-size: 24px;
        font-weight: 800;
        margin-bottom: 6px;
      }

      .meta {
        color: #6b7280;
        font-size: 12px;
        display: flex;
        gap: 12px;
        align-items: center;
      }

      .bars {
        margin-top: 10px;
        display: flex;
        flex-direction: column;
        gap: 6px;
        width: 380px;
      }

      .tabs {
        display: flex;
        background: #eef2f7;
        padding: 4px;
        border-radius: 999px;
        gap: 14px;
        margin-top: 16px;
        border-bottom: 1px solid #e5e7eb;
      }

      .tab {
        padding: 8px 10px;
        font-weight: 700;
        color: #6b7280;
        border-bottom: 2px solid transparent;
        cursor: pointer;
      }

      .tab.active {
        color: #111;
        border-color: #111;
      }

      .card {
        background: #fff;
        border: 1px solid #e5e7eb;
        border-radius: 12px;
        padding: 16px;
        margin-top: 10px;
      }

      .row {
        display: flex;
        align-items: center; /* row 안 전체 수직 정렬 */
        gap: 10px;
        padding: 10px;
        border-top: 1px solid #f2f3f5;
      }

      .row:first-child {
        border-top: none;
      }
      
      label {
        display: flex;
        align-items: center; /* 체크박스와 텍스트 수직 정렬 */
        gap: 5px;
        
      }

      .name {
        font-weight: 700;
      }

      .right {
        margin-left: auto;
        color: #111;
        font-weight: 700;
      }

      .badge {
        font-size: 10px;
        padding: 2px 6px;
        border-radius: 999px;
        background: #ef4444;
        color: #fff;
        font-weight: 700;
      }

      .sub {
        font-size: 11px;
        color: #6b7280;
      }

      .footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 12px 14px;
        border-top: 1px solid #f2f3f5;
      }

      .btn {
        background: #0b0f1a;
        color: #fff;
        padding: 12px;
        border-radius: 8px;
        font-weight: 800;
        text-align: center;
        cursor: pointer;
      }

      .total {
        font-weight: 800;
      }

      .tabs {
        display: flex;
        background: #eef2f7;
        padding: 4px;
        border-radius: 999px;
        gap: 0;
        background: #eef1f4;
        border-radius: 8px;
        padding: 4px;
        margin: 16px 0;
      }

      .tab {
        flex: 1;
        font-size: 14px;
        text-align: center;
        padding: 10px;
        font-weight: 700;
        border-radius: 8px;
        cursor: pointer;
      }

      .tab.active {
        background: #fff;
        border: 1px solid #e5e7eb;
        box-shadow: 0 1px 0 rgba(0, 0, 0, 0.03);
      }

      .panel {
        display: none;
      }

      .panel.active {
        display: block;
      }

      #panel-steps img {
        max-width: 350px;
      }

      .review-card {
        background: #fff;
        border: 1px solid #e5e7eb;
        border-radius: 12px;
        padding: 14px;
        margin-top: 12px;
      }

      .stars button {
        border: 0;
        background: none;
        font-size: 20px;
        cursor: pointer;
      }

      .muted2 {
        color: #9ca3af;
        font-size: 12px;
      }

      .input,
      textarea {
        width: 100%;
        border: 1px solid #e5e7eb;
        border-radius: 8px;
        padding: 12px;
        background: #f9fafb;
      }

      .actions-row {
        display: flex;
        gap: 8px;
        align-items: center;
      }
      
      .variant {
        display: flex;
        gap: 16px;
        align-items: center;
      }

      .variant img {
        width: 140px;
        height: 90px;
        object-fit: cover;
        border-radius: 12px;
        flex-shrink: 0; /* 텍스트 때문에 이미지 줄어듦 방지 */
      }
      
      h5 {
        margin: 10px;
      }
      
      .rv {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
      }
      
      .rv-del {
        border: none;
        background: #fff;
        cursor: pointer;
        font-size: 18px;
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
    <div class="wrap">
      <div style="margin-bottom: 10px">
        <a class="ghost-link muted2" href="${path}/front?key=recipe&methodName=recipes">← 목록으로</a>
      </div>
      <div class="hero">
        <c:choose>
          <c:when test="${recipe.thumbnailUrl != null and recipe.thumbnailUrl.substring(0,2) == '..'}">
            <img src="${path}/${recipe.thumbnailUrl}" alt="thumbnail" />
          </c:when>
          <c:otherwise>
            <img src="${recipe.thumbnailUrl}" alt="thumbnail" />
          </c:otherwise>
        </c:choose>
        
        <div style="flex: 1">
          <div class="title">${recipe.title}</div>
          <div class="meta"><span>${recipe.category}</span><span>${recipe.way}</span><span>❤️</span><span id="likeCnt">${recipe.likeCnt}</span></div>
          <div class="row" style="gap: 8px; margin-top: 8px">
            <button class="btn" id="likeBtn" style="padding: 8px 12px">${likeStatus ? "❤️" : "🤍"} 좋아요</button>
            <c:if test="${recipe.recipeType.toString().toLowerCase() == 'base'}">
              <button class="btn" id="writeBtn" style="padding: 8px 12px; background: #eef1f4; color: #111">
                + 변형 레시피 추가
              </button>
            </c:if>
            <c:if test="${recipe.userId == loginUser.userId}">
              <button class="btn" id="deleteBtn" style="padding: 8px 12px; background: #eef1f4; color: #a11">
                레시피 삭제
              </button>
            </c:if>
          </div>
          <div class="muted2" style="text-align: center; margin-top: 6px">
            * 조미료, 신선식품에 따라 가격은 변동/품절될 수 있습니다
          </div>
        </div>
      </div>

      <div class="tabs">
        <div class="tab active" data-tab="ingredients">재료 목록</div>
        <div class="tab" data-tab="steps">조리법</div>
        <c:if test="${recipe.recipeType.toString().toLowerCase() == 'base'}">
          <div class="tab" data-tab="variants">변형 레시피</div>
	    </c:if>
        <div class="tab" data-tab="reviews">리뷰</div>
      </div>

	  <!-- 재료 목록 -->
      <div id="panel-ingredients" class="panel active">
        <div class="card ing">
          <div class="head row muted2"><span>🧾</span> 필요한 재료 </div>
          <c:choose>
            <c:when test="${empty recipe.ingredients}">
              <h5>재료가 등록되지 않았습니다.</h5>
            </c:when>
          <c:otherwise>
            <c:forEach items="${recipe.ingredients}" var="ingredientDTO">
            <div class="row" data-id="${ingredientDTO.ingredientId}" data-title="${ingredientDTO.name} (${ingredientDTO.quantity})" data-price="${ingredientDTO.price}">
              <c:choose>
                <c:when  test="${ingredientDTO.productId != null and ingredientDTO.productId != 0}">
                  <label>
                    <input type="checkbox" data-product-id="${ingredientDTO.productId}" />              
                    <div class="name">
                      ${ingredientDTO.name}
                      <c:if test="${not ingredientDTO.seasoning}">
                        <span class="badge">필수</span>
                      </c:if>
                      <div class="sub">${ingredientDTO.quantity}</div>
                    </div>
                  </label>
                  <div class="right">${ingredientDTO.price}원</div>
                </c:when>
                <c:otherwise>
                  <div class="name">
                    ${ingredientDTO.name} 
                    <!-- <span class="badge">필수</span> -->
                    <div class="sub">${ingredientDTO.quantity}</div>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
            </c:forEach>
          </c:otherwise>
          </c:choose>
          
          <div class="footer">
            <div class="total">총 금액 <span id="sum">0원</span></div>
            <div class="btn" onclick="addSelected()">🛒 선택한 재료 모두 장바구니에 담기</div>
          </div>

          <div class="muted2" style="text-align: center; margin-top: 6px">
            * 조미료, 신선식품에 따라 가격은 변동/품절될 수 있습니다
          </div>
        </div>
      </div>

      <!-- 조리법 -->
      <div id="panel-steps" class="panel">
        <div class="card">
          <h3 style="margin: 10px">조리 방법</h3>
          <c:choose>
            <c:when test="${empty requestScope.recipe.steps}">
              <h5>조리 방법이 등록되지 않았습니다.</h5>
            </c:when>
          <c:otherwise>
            <ol style="display: flex; flex-direction: column; gap: 12px;">
              <c:forEach items="${recipe.steps}" var="stepDTO">
              
                <c:choose>
		          <c:when test="${stepDTO.imageUrl != null and stepDTO.imageUrl.substring(0,2) == '..'}">
		            <img src="${path}/${stepDTO.imageUrl}" alt="thumbnail" />
		          </c:when>
		          <c:otherwise>
		            <img src="${stepDTO.imageUrl}" alt="thumbnail" />
		          </c:otherwise>
		        </c:choose>
		        
                <li>${stepDTO.description}</li>
              </c:forEach>
            </ol>
          </c:otherwise>
          </c:choose>
        </div>
      </div>

      <!-- 변형 레시피 -->
      <div id="panel-variants" class="panel">
        <div class="card">
          <div class="muted2" style="margin: 10px; margin-bottom: 20px">다른 사용자의 변형 레시피입니다</div>
          
          <c:choose>
            <c:when test="${empty variants}">
              <h5>등록된 변형 레시피가 없습니다.</h5>
            </c:when>
            <c:otherwise>
              <c:forEach items="${variants}" var="variant">
                <div class="card variant">
                  <img src="${variant.thumbnailUrl}" alt="thumbnail"/>
                  <div style="flex: 1">
                    <div style="font-weight: 700">${variant.title}</div>
                    <div class="muted2">${variant.description}</div>
                    <div class="actions-row">
                      <a class="ghost-link muted2" href="${path}/front?key=recipe&methodName=recipeDetail&recipeId=${variant.recipeId}">
                        <button class="btn" style="padding: 8px 12px; margin-top: 10px">변형 레시피 보기</button>
                      </a>
                    </div>
                  </div>
                </div>
              </c:forEach>
            </c:otherwise>
          </c:choose>
          
        </div>
      </div>

      <!-- 리뷰 -->
      <div id="panel-reviews" class="panel">
        <div class="card" style="padding: 16px">
          <form>
            <h3 style="margin: 10px">리뷰 작성하기</h3>
            <div style="padding: 10px">
            <div>평점</div>
            <div class="stars" id="rating" aria-label="평점 선택" data-rating="5">
              <button type="button" data-star="1">★</button>
              <button type="button" data-star="2">★</button>
              <button type="button" data-star="3">★</button>
              <button type="button" data-star="4">★</button>
              <button type="button" data-star="5">★</button>
            </div>
            <div>리뷰 내용</div>
            <textarea
              id="content"
              rows="4"
              placeholder="이 레시피에 대한 경험을 공유해주세요..."
              class="input"
              style="margin: 10px 0"
            ></textarea>
            <input type="file" id="imageUrl" class="input" placeholder="이미지를 업로드하세요 (선택)" />
            <div class="actions-row" style="margin-top: 10px">
              <button id="insertReviewBtn" class="btn" style="width: 100%; padding: 8px">리뷰 등록하기</button>
            </div>
            </div>
          </form>
        </div>
        <div id="reviewList" class="card" data-rid="${recipe.recipeId}">
          
        </div>
      </div>
    </div>
    <!-- footer 시작 -->
    <jsp:include page="../common/footer.jsp"></jsp:include>
    <!-- footer 끝 -->

    <script>
      function price(n) {
        return (Number(n) || 0).toLocaleString() + "원";
      }
      
      function sum() {
        let s = 0;
        document.querySelectorAll(".ing .row input[type=checkbox]:checked").forEach((cb) => {
          const r = cb.closest(".row");
          s += Number(r?.dataset?.price || 0);
        });
        const el = document.querySelector("#sum");
        if (el) el.textContent = price(s);
      }

      document.addEventListener("DOMContentLoaded", () => {
        // Tabs
        document.addEventListener("click", (e) => {
          const t = e.target.closest(".tab");
          if (!t) return;
          document.querySelectorAll(".tab").forEach((el) => el.classList.remove("active"));
          document.querySelectorAll(".panel").forEach((el) => el.classList.remove("active"));
          t.classList.add("active");
          const id = "panel-" + t.dataset.tab;
          const panel = document.querySelector("#" + id)
          if (panel) panel.classList.add("active");
        });

        // Checkboxes -> sum
        document.addEventListener("change", (e) => {
          if (e.target.matches(".ing [type=checkbox]")) sum();
        });
        sum();

        
      }); // DOMContentLoaded end

      onload = () => {
        printReviews();
      }

      /* 리뷰 전체 검색 */
      const recipeId = document.querySelector("#reviewList").dataset.rid;
      const printReviews = async function () {
        body = new URLSearchParams({
          key: "review",
          methodName: "selectByRecipeId",
          recipeId,
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
          console.log("reviews: ", result);
          
          // 데이터 출력
          let list = document.querySelector("#reviewList");
          let str = "";
          
          if (result.length === 0) {
            str = `<div class="muted2" style="margin: 10px">
            아직 리뷰가 없습니다 <br>
            첫 번째 리뷰를 작성해보세요!
            </div>`
          } else {        	  
            result.forEach((review, index) => {
              let originUser = review.userId;
              let curUser = ${loginUser != null ? loginUser.userId : 'null'};
              // 별
              let stars = "★".repeat(review.rating || 0);
              
              str += `<div class="card variant">`;
              // 이미지가 있을 때만 img 태그
              if (review.imageUrl) {
                if (review.imageUrl.substring(0,2) == '..')
                  str += `<img src="${path}/${"${review.imageUrl}"}" alt="thumbnail"/>`;
                else
                  str += `<img src="${"${review.imageUrl}"}" alt="thumbnail"/>`;
              }
              str += `
                <div class="rv">
                  <div>
                    <div class="muted2">${"${review.nickname}"} &nbsp; ${"${stars}"}</div>
                    <div>${"${review.content}"}</div>               
                  </div>`;
              if (originUser == curUser) str += `<button class="rv-del" title="삭제" onclick="deleteReview(${"${review.reviewId}"})">🗑</button>`;
              str += `</div></div>`;
            });
          }
          
          list.innerHTML = str;
          
        } catch (err) {
          console.error("에러 발생: " + err);
        }
      };

      // insert recipe
      const writeBtn = document.querySelector("#writeBtn");
      writeBtn?.addEventListener("click", (e) => {
        e.preventDefault();
        location.href = "${path}/front?key=recipe&methodName=variantWrite&parentId=${recipe.recipeId}";
      });
      
      // delete recipe
      const deleteBtn = document.querySelector("#deleteBtn");
      deleteBtn?.addEventListener("click", (e) => {
        e.preventDefault();
        if (confirm("정말 레시피를 삭제하시겠습니까?")) {
          location.href = "${path}/front?key=recipe&methodName=deleteRecipe&recipeId=${recipe.recipeId}";      	  
        }
      });

      // 평점
      const rating = document.querySelector("#rating");
      const starBtns = rating.querySelectorAll("button");
      
      starBtns.forEach(btn => {
        btn.addEventListener("click", () => {
          const value = btn.dataset.star;
          rating.dataset.rating = value;	// 평점 세팅
          
          starBtns.forEach(b => {
            b.textContent = b.dataset.star <= value ? "★" : "☆";	// 별 색깔 바꾸기
          })
        })
      });
      
      
      /* 변형 레시피 작성 */
      // insert review
      const insertReviewBtn = document.querySelector("#insertReviewBtn");
      insertReviewBtn?.addEventListener("click", async (e) => {
        e.preventDefault();
        
        let rid = document.querySelector("#reviewList").dataset.rid;
        console.log("insert reveiw btn > rid: ", rid);
        let content = document.querySelector("#content");
        let imageUrl = document.querySelector("#imageUrl");
        
        // URLSearchParams는 텍스트 데이터 전송 전용이라 이미지 파일 보내면 null 나올 것...그래도 FormData로 변경
        const form = new FormData();
        form.append("key", "review");
        form.append("methodName", "insertReview");
        form.append("recipeId", rid);
        form.append("rating", rating.dataset.rating);
        form.append("content", content.value);
        form.append("imageUrl", imageUrl.files[0]);

        try {
          const response = await fetch(CONTEXT_PATH + "/ajax", {
            method: "POST",
            //body,
            body: form,
          });

          if (!response.ok) {
            throw new Error("서버 응답 에러: " + response.status);
          }
          
          // 값 초기화
          content.value = "";
          imageUrl.value = "";
          rating.dataset.rating = 5;	// 평점 세팅
          starBtns.forEach(b => {
            b.textContent = "★";
          })
          
          
        } catch (err) {
          console.error("리뷰 등록 실패: " + err);
        }
        // 리뷰 목록 다시 불러오기
        printReviews();
      });
      
      // delete review
      const deleteReview = async function (id) {
        try {
          const response = await fetch(CONTEXT_PATH + "/ajax", {
        	  method: "POST",
        	  body: new URLSearchParams({
        		  key: "review",
        		  methodName: "deleteReview",
        		  reviewId: id,
        	  }),
          });
        
          if (!response.ok) {
            throw new Error("서버 응답 에러: " + response.status);
          }
        } catch (err) {
        	console.error("리뷰 삭제 실패: " + err);
        }
          
	    printReviews();
      }
      
      
      /* 좋아요 */
      const likeBtn = document.querySelector("#likeBtn");
      const likeCntEl = document.querySelector("#likeCnt");
      likeBtn?.addEventListener("click", async () => {
        let count = parseInt(likeCntEl.textContent);
    	  
        // UI 반영
        if (likeBtn.classList.contains("active")) {
          likeBtn.textContent = "🤍 좋아요";
          likeCntEl.textContent = count - 1;
        } else {
          likeBtn.textContent = "❤️ 좋아요";
          likeCntEl.textContent = count + 1;
        }
        likeBtn.classList.toggle("active");
        
        // 서버에 좋아요 등록/삭제
        try {
         const response = await fetch(CONTEXT_PATH + "/ajax", {
           method: "POST",
           body: new URLSearchParams({
             key: "like",
   		     methodName: "toggleLike",
             targetType: "RECIPE",
             targetId: recipeId,
           }),
         });
         
         if (response.status === 401) {
        	 alert("로그인이 필요합니다.");
        	 location.href = CONTEXT_PATH + "/front?key=user&methodName=loginForm";
        	 return;
         }
         
         if (!response.ok) {
           throw new Error("서버 응답 에러: " + response.status);
         }
         
        } catch (err) {
       	   console.error("좋아요 토글 실패: " + err);
        }
      });
      
      
      

      /* 장바구니 */
      // Add to cart // Ajax로 insertCart
      window.addSelected = async function () {
        const checked = document.querySelectorAll(".ing [type=checkbox]:checked");
        if (!checked.length) {
          alert("재료를 선택해주세요.");
          return;
        }
        
        // ajax 서버 요청
        for (const ch of checked) {
          const row = ch.closest(".row");
          const id = ch.dataset.productId;
          const count = 1;

          if (id === null || id === 0) continue;  // productId 없는 경우 건너뜀
          
          try {
            const duplicateChk = await fetch(CONTEXT_PATH + "/ajax", {
              method: "POST",
              body: new URLSearchParams({
                key: "cart",
                methodName: "duplicateCheck",
                productId: id,
              }),
            });
            const exists = await duplicateChk.json();
            if (exists) {
              const response = await fetch(conPath + "/ajax", {
                method: "POST",
                body: new URLSearchParams({
                  key: "cart",
                  methodName: "duplicatedCartCount",
                  productId: id,
                  newCount: exists.count + count,
                }),
              });
              
              if (response.status === 401) {
             	 alert("로그인이 필요합니다.");
             	 location.href = CONTEXT_PATH + "/front?key=user&methodName=loginForm";
             	 return;
              }
              
              if (response.ok) {
                console.log("Cart count updated successfully");
              } else {
                console.error("Failed to update cart count:", response.statusText);
              }
            } else {
              const res = await fetch(CONTEXT_PATH + "/ajax", {
                method: "POST",
                body: new URLSearchParams({
                  key: "cart",
                  methodName: "insertCart",
                  productId: id,
                  count: count,
                }),
              });
              
              if (response.status === 401) {
             	 alert("로그인이 필요합니다.");
             	 location.href = CONTEXT_PATH + "/front?key=user&methodName=loginForm";
             	 return;
              }
              
              if (!res.ok) {
                console.error("Failed to add to cart:", res.status, res.statusText);
                return;
              }
            }
          } catch (err) {
            console.error("recipe-detail > insertCart 오류: ", err);
          }
        }
        
        ensureBadge(); // 장바구니 새로고침
        // 장바구니 추가 알림 (우측 하단)
        try {
          const n = document.createElement("div");
          n.textContent = `${"${checked.length}"}개 재료가 장바구니에 추가되었습니다.`;
          n.style.cssText =
            "position:fixed;right:16px;bottom:16px;background:#111;color:#fff;padding:10px 14px;border-radius:10px;z-index:9999";
          document.body.appendChild(n);
          setTimeout(() => {
            n.remove();
          }, 1000);
        } catch (_) { }
      };
    </script>
  </body>
</html>
