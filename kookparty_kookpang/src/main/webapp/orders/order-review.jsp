<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>주문 확인</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <style>
      
        .ov-wrap {
          max-width: 1100px;
          margin: 0 auto;
          padding: 24px
        }

        .ov-grid {
          display: grid;
          grid-template-columns: 2fr 1fr;
          gap: 16px
        }

        .ov-card {
          background: #fff;
          border: 1px solid #e5e7eb;
          border-radius: 12px;
          padding: 16px
        }

        .ov-title {
          font-size: 22px;
          font-weight: 800;
          margin: 6px 0 14px 0
        }

        .ov-sec-title {
          font-size: 16px;
          font-weight: 700;
          margin: 4px 0 10px 0
        }

        .ov-row {
          display: flex;
          align-items: center;
          justify-content: space-between;
          padding: 12px 0;
          border-bottom: 1px solid #f2f3f5
        }

        .ov-row:last-child {
          border-bottom: none
        }

        .ov-qty {
          display: flex;
          align-items: center;
          gap: 10px
        }

        .ov-qty button {
          width: 28px;
          height: 28px;
          border: 1px solid #e5e7eb;
          border-radius: 8px;
          background: #fff;
          cursor: pointer
        }

        .ov-del {
          border: none;
          background: #fff;
          cursor: pointer;
          font-size: 18px
        }

        .ov-badge {
          display: inline-flex;
          align-items: center;
          padding: 10px 14px;
          border-radius: 999px;
          background: #0b0f1a;
          color: #fff;
          font-weight: 700
        }

        .ov-aside .ov-row {
          padding: 8px 0
        }

        .ov-note {
          background: #f9fafb;
          border: 1px dashed #e5e7eb;
          border-radius: 8px;
          padding: 10px;
          margin-top: 10px;
          color: #6b7280;
          font-size: 14px
        }

        @media (max-width:900px) {
          .ov-grid {
            grid-template-columns: 1fr
          }
        }
      </style>
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script src="${path}/js/order-review.js"></script>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->

      <div class="ov-wrap">
        <div class="ov-title">주문 확인</div>

        <!-- 🛒 장바구니 비었을 때 보이는 영역 -->
        <section id="emptyState" class="ov-card"
          style="display:none; padding:48px 24px; text-align:center; margin-bottom:16px;">
          <div style="display:flex;align-items:center;gap:8px;margin-bottom:12px;justify-content:flex-start">
            <a href="${path}/recipes/recipes.jsp" class="btn" style="background:#f3f4f6;color:#111">← 쇼핑 계속하기</a>
          </div>
          <div style="max-width:520px;margin:0 auto">
            <div style="font-size:56px;opacity:.5;line-height:1.1">🛒</div>
            <h2 style="margin:10px 0 6px;font-size:22px">장바구니가 비어있습니다</h2>
            <p class="muted" style="margin:0 0 16px">맛있는 요리를 위한 재료들을 담아보세요</p>
            <div style="display:flex;gap:8px;justify-content:center">
              <a href="${path}/recipes/recipes.jsp" class="btn">레시피 보러가기</a>
              <a href="${path}/orders/ingredients.jsp" class="btn"
                style="background:#fff;color:#111;border:1px solid #e5e7eb">식재료
                보러가기</a>
            </div>
          </div>
        </section>

        <!-- ✅ 장바구니가 있을 때만 보이는 주문/결제/배송/혜택 -->
        <section id="cartState">
          <div class="ov-grid">
            <section class="ov-card">
              <div class="ov-sec-title">주문 상품 <span id="ov-count" style="color:#6b7280;font-weight:600"></span></div>
              <div id="ov-items"></div>
            </section>
            <aside class="ov-card ov-aside">
              <div class="ov-sec-title">결제 정보</div>
              <div class="ov-row"><span>상품 금액</span><b id="ov-price">0원</b></div>
              <div class="ov-row"><span>배송비</span><b id="ov-ship">3,000원</b></div>
              <div class="ov-row" style="border-top:1px solid #e5e7eb"></div>
              <div class="ov-row"><span style="font-weight:800">총 결제 금액</span><b id="ov-total"
                  style="font-size:18px">0원</b>
              </div>
              <button id="ov-pay" class="ov-badge"
                style="margin-top:12px;width:100%;justify-content:center">결제하기</button>
            </aside>
          </div>

          <div class="ov-grid" style="margin-top:16px">
            <section class="ov-card">
              <div class="ov-sec-title">배송 정보</div>
              <div class="ov-row" style="border-bottom:none;display:block">
                <div class="ov-ship" id="ov-shipinfo" style="line-height:1.8"></div>
                <div class="ov-note">
                  • 신선식품 특성상 주문 후 취소/교환/환불이 어려울 수 있습니다.<br />
                  • 배송은 주문일 기준 1-2일 소요됩니다.
                </div>
              </div>
            </section>
            <aside class="ov-card">
              <div class="ov-sec-title">주문 혜택</div>
              <div class="ov-row" style="border-bottom:none;display:block;color:#6b7280">
                • 50,000원 이상 무료배송<br />• 신선식품 당일 발송<br />• 레시피 기반 맞춤 포장
              </div>
            </aside>
          </div>
        </section>
      </div>
      <!-- footer 시작 -->
      <jsp:include page="../common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <script src="../js/order-review.js"></script>

      <script>
        (function () {
          const CART_KEY = 'kp_cart';
          function getCart() { try { return JSON.parse(localStorage.getItem(CART_KEY) || '[]') } catch (_) { return [] } }
          function toggleEmptyState() {
            const has = getCart().length > 0;
            const empty = document.getElementById('emptyState');
            const cart = document.getElementById('cartState');
            if (empty) { empty.style.display = has ? 'none' : 'block'; }
            if (cart) { cart.style.display = has ? '' : 'none'; }
            try { window.kpUpdateCartBadge && window.kpUpdateCartBadge(); } catch (_) { }
          }
          document.addEventListener('DOMContentLoaded', toggleEmptyState);
          window.addEventListener('storage', toggleEmptyState);
        })();
      </script>
    </body>

    </html>