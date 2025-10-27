<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%@taglib uri="jakarta.tags.core"
prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>주문 확인</title>
    <link rel="stylesheet" href="${path}/css/styles.css" />
    <style>
      .ov-wrap {
        max-width: 1100px;
        margin: 0 auto;
        padding: 24px;
      }

      .ov-grid {
        display: grid;
        grid-template-columns: 2fr 1fr;
        gap: 16px;
      }

      .ov-card {
        background: #fff;
        border: 1px solid #e5e7eb;
        border-radius: 12px;
        padding: 16px;
      }

      .ov-title {
        font-size: 22px;
        font-weight: 800;
        margin: 6px 0 14px 0;
      }

      .ov-sec-title {
        font-size: 16px;
        font-weight: 700;
        margin: 4px 0 10px 0;
      }

      .ov-row {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 12px 0;
        border-bottom: 1px solid #f2f3f5;
      }

      .ov-row:last-child {
        border-bottom: none;
      }

      .ov-qty {
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .ov-qty button {
        width: 28px;
        height: 28px;
        border: 1px solid #e5e7eb;
        border-radius: 8px;
        background: #fff;
        cursor: pointer;
      }

      .ov-del {
        border: none;
        background: #fff;
        cursor: pointer;
        font-size: 18px;
      }

      .ov-badge {
        display: inline-flex;
        align-items: center;
        padding: 10px 14px;
        border-radius: 999px;
        background: #0b0f1a;
        color: #fff;
        font-weight: 700;
      }

      .ov-aside .ov-row {
        padding: 8px 0;
      }

      .ov-note {
        background: #f9fafb;
        border: 1px dashed #e5e7eb;
        border-radius: 8px;
        padding: 10px;
        margin-top: 10px;
        color: #6b7280;
        font-size: 14px;
      }

      @media (max-width: 900px) {
        .ov-grid {
          grid-template-columns: 1fr;
        }
      }
    </style>
    <script type="text/javascript">
      const CONTEXT_PATH = "${pageContext.request.contextPath}";
    </script>
    <script src="${path}/js/config.js"></script>
    <script src="${path}/js/cart.js"></script>
  </head>

  <body>
    <!-- header시작 -->
    <jsp:include page="../common/header.jsp"></jsp:include>
    <!-- header끝 -->
    <main class="container wide page">
      <div class="ov-wrap">
        <div class="ov-title">주문 확인</div>
        <section id="cartState">
          <div class="ov-grid">
            <section class="ov-card">
              <div class="ov-sec-title">
                주문 상품 <span id="ov-count" style="color: #6b7280; font-weight: 600"></span>
              </div>
              <div id="ov-items">
                <!-- 주문 상품 목록 반복 -->
                <c:forEach var="item" items="${list}">
                  <div class="ov-row">
                    <div>
                      <div style="font-weight: 700">${item.name}</div>
                      <div class="small" style="color: #6b7280">${item.price}원 / 1</div>
                    </div>
                    <div class="ov-qty">
                      <span>${item.count}</span> <span><b>${item.count * item.price}원</b></span>
                    </div>
                  </div>
                </c:forEach>
                <!-- 주문 상품 목록 반복 끝 -->
              </div>
            </section>
            <aside class="ov-card ov-aside">
              <div class="ov-sec-title">결제 정보</div>
              <div class="ov-row"><span>상품 금액</span><b id="ov-price">${price}원</b></div>
              <div class="ov-row"><span>배송비</span><b id="ov-ship">${deliveryFee}원</b></div>
              <div class="ov-row" style="border-top: 1px solid #e5e7eb"></div>
              <div class="ov-row"><span>보유 포인트</span><b id="ov-point">${point}</b></div>
              <div class="ov-row"><span>사용 포인트</span><input type="text" id="used_point" value="0" /></div>
              <div class="ov-row">
                <span style="font-weight: 800">총 결제 금액</span
                ><b id="ov-total" style="font-size: 18px">${totalPrice}원</b>
              </div>
              <button id="ov-pay" class="ov-badge" style="margin-top: 12px; width: 100%; justify-content: center">
                결제하기
              </button>
            </aside>
          </div>

          <div class="ov-grid" style="margin-top: 16px">
            <section class="ov-card">
              <div class="ov-sec-title">배송 정보</div>
              <div class="ov-row" style="border-bottom: none; display: block">
                <div class="ov-ship" id="ov-shipinfo" style="line-height: 1.8">
                  <!-- 선택형 주소: 하드코딩된 주소 목록과 '직접 입력' 옵션 -->
                  <form id="ship-select-form">
                    <div style="margin-bottom: 8px">
                      받는 분: <b>${name}</b> / 연락처: <b>${phone}</b><br />
                      <div id="normal-addr-area" style="margin-top: 8px">
                        <label style="display: block; margin-bottom: 6px">
                          <input type="radio" name="ship_choice" value="addr1" id="normal" checked /> 기본 주소:</label
                        ><b id="addr">${address}</b>
                      </div>

                      <label style="display: block; margin-bottom: 6px"
                        ><input type="radio" name="ship_choice" value="custom" id="custom" /> 직접 입력</label
                      >
                      <div style="margin-bottom: 8px; display: none" id="custom-addr-area">
                        <textarea
                          id="custom-address"
                          class="input"
                          placeholder="배송지 주소를 입력하세요"
                          style="width: 100%; height: 76px"
                        ></textarea>
                      </div>
                    </div>
                  </form>
                </div>
                <div class="ov-note">
                  • 신선식품 특성상 주문 후 취소/교환/환불이 어려울 수 있습니다.<br />
                  • 배송은 주문일 기준 1-2일 소요됩니다.
                </div>
              </div>
            </section>
            <aside class="ov-card">
              <div class="ov-sec-title">주문 혜택</div>
              <div class="ov-row" style="border-bottom: none; display: block; color: #6b7280">
                • 50,000원 이상 무료배송<br />• 신선식품 당일 발송<br />• 레시피 기반 맞춤 포장
              </div>
            </aside>
          </div>
        </section>
      </div>
    </main>
    <!-- footer 시작 -->
    <jsp:include page="../common/footer.jsp"></jsp:include>
    <!-- footer 끝 -->
    <script>
      document.getElementById("used_point").addEventListener("keyup", (e) => {
        const maxPoint = document.getElementById("ov-point").innerText;
        let usedPoint = parseInt(e.target.value) || 0;

        if (usedPoint < 0) usedPoint = 0;
        if (usedPoint > maxPoint) usedPoint = maxPoint;

        e.target.value = usedPoint;

        const itemPrice = ${price};
        const itemDeliveryFee = ${deliveryFee};
        const totalPrice = itemPrice + itemDeliveryFee - usedPoint;

        document.getElementById("ov-total").innerText = totalPrice + "원";
      });
    </script>
    <script>
      const customRadio = document.getElementById("custom");
      const normalRadio = document.getElementById("normal");
      const customArea = document.getElementById("custom-addr-area");
      const normalArea = document.getElementById("addr");

      const toggleCustomArea = function () {
        if (customRadio.checked) {
          customArea.style.display = "block";
          normalArea.style.display = "none";
        } else {
          customArea.style.display = "none";
          normalArea.style.display = "block";
        }
      };

      toggleCustomArea();

      customRadio.addEventListener("change", toggleCustomArea);
      normalRadio.addEventListener("change", toggleCustomArea);

      const shipSelect = function () {
        let addr = "";
        if (document.getElementById("normal").checked) {
          addr = "${address}";
        } else if (document.getElementById("custom").checked) {
          addr = document.getElementById("custom-address").value;
        }

        return addr;
      };

      const payReady = async function () {
        const addr = shipSelect();
        if (addr.trim() === "") {
          alert("배송지 주소를 입력해주세요.");
          return;
        }
        const point = document.getElementById("used_point").value || "0";
        const r = await fetch(CONTEXT_PATH + "/ajax", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
          },
          body: new URLSearchParams({
            key: "pay",
            methodName: "payReady",
            address: addr,
            point: point,
          }),
        });
        const json = await r.json();
        const popupWidth = 500;
        const popupHeight = 500;
        const left = window.screen.width / 2 - popupWidth / 2;
        const top = window.screen.height / 2 - popupHeight / 2;

        window.open(
          json.next_redirect_pc_url,
          "kakaoPayPopup",
          `width=${"${popupWidth}"},height=${"${popupHeight}"},left=${"${left}"},top=${"${top}"},resizable=no,scrollbars=yes`
        );

        window.addEventListener("message", (event) => {
          if (event.data) {
            console.log("결제 완료, 주문번호: " + event.data);
            window.location.href = CONTEXT_PATH + "/front?key=order&methodName=orderResult&order_id=" + event.data;
          }
        });
      };

      document.getElementById("ov-pay").addEventListener("click", () => {
        //결제 api 호출(ajax)
        //결제 성공시 주문 pk받아서 다음 페이지로
        payReady();
      });
    </script>
  </body>
</html>
