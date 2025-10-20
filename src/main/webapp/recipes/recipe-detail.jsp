<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        align-items: center;
        gap: 10px;
        padding: 12px 14px;
        border-top: 1px solid #f2f3f5;
      }

      .row:first-child {
        border-top: none;
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
    </style>
    <script type="text/javascript">
      const CONTEXT_PATH = "${pageContext.request.contextPath}";
      console.log(${recipe});
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
        <img src="${recipe.thumbnailUrl}" alt="${recipe.title}" />
        <div style="flex: 1">
          <div class="title">${recipe.title}</div>
          <div class="meta"><span>${recipe.category}</span><span>${recipe.way}</span><span>❤️ 좋아요수TODO</span></div>
          <div class="row" style="gap: 8px; margin-top: 8px">
            <button class="btn" id="likeBtn" style="padding: 8px 12px">♡ 좋아요</button>
            <button class="btn" id="variantBtn" style="padding: 8px 12px; background: #eef1f4; color: #111">
              + 변형 레시피 추가
            </button>
          </div>
          <div class="muted2" style="text-align: center; margin-top: 6px">
            * 조미료, 신선식품에 따라 가격은 변동/품절될 수 있습니다
          </div>
        </div>
      </div>

      <div class="tabs">
        <div class="tab active" data-tab="ingredients">재료 목록</div>
        <div class="tab" data-tab="steps">조리법</div>
        <div class="tab" data-tab="variants">변형 레시피</div>
        <div class="tab" data-tab="reviews">리뷰</div>
      </div>

      <div id="panel-ingredients" class="panel active">
        <div class="card ing">
          <div class="head"><span>🧾</span> 필요한 재료 <span class="muted">(필요한 만큼 선택)</span></div>
          <!--재료 반복 시작-->
          <c:choose>
            <c:when test="${empty recipe.ingredients}">
              <h5>재료가 등록되지 않았습니다.</h5>
            </c:when>
          <c:otherwise>
            <c:forEach items="${recipe.ingredients}" var="ingredientDTO">
            <div class="row" data-id="${ingredientDTO.ingredientId}" data-title="${ingredientDTO.name} (${ingredientDTO.quantity})" data-price="5000">
              <input type="checkbox" />
              <div class="name">
                ${ingredientDTO.name} <span class="badge">필수</span>
                <!-- <div class="sub">300g</div> -->
              </div>
              <div class="right">5,000원</div>
            </div>
            </c:forEach>
          </c:otherwise>
          </c:choose>

          <!--
          <div class="row" data-id="pork" data-title="돼지고기 (삼겹살)" data-price="15000">
            <input type="checkbox" />
            <div class="name">
              돼지고기 (삼겹살) <span class="badge">필수</span>
              <div class="sub">300g</div>
            </div>
            <div class="right">15,000원</div>
          </div>
          -->
          <!--재료 반복-->
          <div class="footer">
            <div class="total">총 금액 <span id="sum">0원</span></div>
            <div class="btn" onclick="addSelected()">🛒 선택한 재료 모두 장바구니에 담기</div>
          </div>

          <div class="muted2" style="text-align: center; margin-top: 6px">
            * 조미료, 신선식품에 따라 가격은 변동/품절될 수 있습니다
          </div>
        </div>
      </div>

      <div id="panel-steps" class="panel">
        <div class="card">
          <h3 style="margin: 0 0 10px">조리 방법</h3>
          <c:choose>
            <c:when test="${empty requestScope.recipe.steps}">
              <h5>조리 방법이 등록되지 않았습니다.</h5>
            </c:when>
          <c:otherwise>
            <ol style="display: flex; flex-direction: column; gap: 12px; margin-left: 18px">
              <c:forEach items="${recipe.steps}" var="stepDTO">
                <img src="${stepDTO.imageUrl}" alt="${stepDTO.description}" />
                <li>${stepDTO.description}</li>
              </c:forEach>
            </ol>
          </c:otherwise>
          </c:choose>
        </div>
      </div>

      <div id="panel-variants" class="panel">
        <div class="card">
          <div class="muted" style="margin-bottom: 8px">다른 사용자의 변형 레시피들입니다</div>
          <div class="grid" style="gap: 12px">
            <div class="card" style="padding: 14px">
              <div style="font-weight: 700">치즈 김치찌개</div>
              <div class="muted2">치즈를 추가한 더 묵직한 맛</div>
              <div class="actions-row">
                <button class="btn" style="padding: 8px 12px">변형 레시피 보기</button>
              </div>
            </div>
            <div class="card" style="padding: 14px">
              <div style="font-weight: 700">참치 김치찌개</div>
              <div class="muted2">참치를 더해 감칠맛 업</div>
              <div class="actions-row">
                <button class="btn" style="padding: 8px 12px">변형 레시피 보기</button>
              </div>
            </div>
          </div>
          <div class="muted2" style="text-align: center; margin-top: 6px">
            * 조미료, 신선식품에 따라 가격은 변동/품절될 수 있습니다
          </div>
        </div>
      </div>

      <div id="panel-reviews" class="panel">
        <div class="card">
          <h3 style="margin: 0 0 10px">리뷰 작성하기</h3>
          <div class="stars" id="starBox" aria-label="평점 선택">
            <button data-v="1">★</button>
            <button data-v="2">★</button>
            <button data-v="3">★</button>
            <button data-v="4">★</button>
            <button data-v="5">★</button>
          </div>
          <textarea
            id="reviewText"
            rows="4"
            placeholder="이 레시피에 대한 경험을 공유해주세요..."
            class="input"
            style="margin: 10px 0"
          ></textarea>
          <input id="photoUrl" class="input" placeholder="이미지 URL을 입력하세요 (선택)" />
          <div class="actions-row" style="margin-top: 10px">
            <button class="btn" id="submitReview">리뷰 등록하기</button>
          </div>
        </div>
        <div id="reviewList"></div>
      </div>
    </div>
    <!-- footer 시작 -->
    <jsp:include page="../common/footer.jsp"></jsp:include>
    <!-- footer 끝 -->

    <script>
      const KP_CART = "kp_cart";
      const qs = (s, r = document) => r.querySelector(s);
      const qsa = (s, r = document) => Array.from(r.querySelectorAll(s));
      function price(n) {
        return (Number(n) || 0).toLocaleString() + "원";
      }
      function sum() {
        let s = 0;
        qsa(".ing .row input[type=checkbox]:checked").forEach((cb) => {
          const r = cb.closest(".row");
          s += Number(r?.dataset?.price || 0);
        });
        const el = qs("#sum");
        if (el) el.textContent = price(s);
      }

      document.addEventListener("DOMContentLoaded", () => {
        try {
          initHeader && initHeader("recipes");
        } catch (e) {}

        // Tabs
        document.addEventListener("click", (e) => {
          const t = e.target.closest(".tab");
          if (!t) return;
          qsa(".tab").forEach((el) => el.classList.remove("active"));
          qsa(".panel").forEach((el) => el.classList.remove("active"));
          t.classList.add("active");
          const id = "panel-" + t.dataset.tab;
          qs("#" + id)?.classList.add("active");
        });

        // Like
        const likeBtn = qs("#likeBtn");
        likeBtn?.addEventListener("click", () => {
          likeBtn.classList.toggle("active");
          likeBtn.textContent = likeBtn.classList.contains("active") ? "♥ 좋아요" : "♡ 좋아요";
        });

        // Variant write
        const vb = qs("#variantBtn");
        vb?.addEventListener("click", () => {
          location.href = "${path}/recipes/variant-write.jsp";
        });

        // Checkboxes -> sum
        document.addEventListener("change", (e) => {
          if (e.target.matches(".ing [type=checkbox]")) sum();
        });
        sum();

        // Add to cart //변형시켜서 Ajax로 insertCart...
        window.addSelected = function () {
          const checked = qsa(".ing [type=checkbox]:checked");
          if (!checked.length) {
            alert("재료를 선택해주세요.");
            return;
          }
          let cart = [];
          try {
            cart = JSON.parse(localStorage.getItem(KP_CART) || "[]");
          } catch (_) {}
          checked.forEach((ch) => {
            const row = ch.closest(".row");
            const id = row.dataset.id,
              title = row.dataset.title,
              price = Number(row.dataset.price || 0);
            const i = cart.findIndex((x) => x.id === id);
            if (i > -1) cart[i].qty = (cart[i].qty || 1) + 1;
            else cart.push({ id, title, price, qty: 1 });
          });
          localStorage.setItem(KP_CART, JSON.stringify(cart));
          try {
            window.kpUpdateCartBadge && window.kpUpdateCartBadge();
          } catch (_) {}
          try {
            const n = document.createElement("div");
            n.textContent = `${"${checked.length}"}개 재료가 장바구니에 추가되었습니다.`;
            n.style.cssText =
              "position:fixed;right:16px;bottom:16px;background:#111;color:#fff;padding:10px 14px;border-radius:10px;z-index:9999";
            document.body.appendChild(n);
            setTimeout(() => {
              n.remove();
              location.href = "${path}/orders/cart.jsp";
            }, 900);
          } catch (_) {
            location.href = "${path}/orders/cart.jsp";
          }
        };
      });
    </script>
  </body>
</html>
