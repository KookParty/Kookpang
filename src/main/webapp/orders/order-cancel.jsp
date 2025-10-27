<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="jakarta.tags.core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>주문 취소</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script>const PAGE_ACTIVE = '';</script>
      <script src="${path}/js/app.js"></script>
      <script src="${path}/js/seed.js"></script>
      <style>
        .wrap {
          max-width: 1040px;
          margin: 24px auto;
        }

        .grid-2 {
          display: grid;
          grid-template-columns: 1fr 320px;
          gap: 20px;
        }

        .card {
          background: #fff;
          border: 1px solid #e5e7eb;
          border-radius: 12px;
        }

        .card .card-head {
          padding: 16px 18px;
          font-weight: 700;
          border-bottom: 1px solid #eef1f4;
          display: flex;
          align-items: center;
          gap: 10px;
        }

        .card .card-body {
          padding: 16px 18px;
        }

        .muted {
          color: #6b7280;
          font-size: 14px
        }

        .pill {
          font-size: 12px;
          padding: 4px 8px;
          border-radius: 999px;
          background: #111827;
          color: #fff;
        }

        .pill.gray {
          background: #e5e7eb;
          color: #111;
        }

        .pill.red {
          background: #ef4444;
          color: #fff;
        }

        .row {
          display: flex;
          gap: 10px;
          align-items: center;
        }

        .col {
          display: flex;
          flex-direction: column;
          gap: 12px;
        }

        .meta {
          color: #6b7280;
          font-size: 13px
        }

        .kv {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 10px 0;
          border-bottom: 1px solid #f1f5f9
        }

        .kv:last-child {
          border-bottom: 0
        }

        .price {
          font-weight: 700
        }

        .btn.red {
          background: #ef4444;
          color: #fff;
        }

        .btn.red:hover {
          filter: brightness(0.95)
        }

        .status-box {
          background: #f6f7f9;
          border: 1px solid #e6e9ef;
          border-radius: 8px;
          padding: 14px;
        }

        .dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;
          display: inline-block;
          margin-right: 8px;
          background: #374151
        }

        .dot.gray {
          background: #c5cad4
        }

        .list {
          display: flex;
          flex-direction: column;
          gap: 10px;
        }

        .ghost-link {
          color: #111;
          text-decoration: none
        }

        .ghost-link:hover {
          text-decoration: underline
        }

        @media (max-width: 980px) {
          .grid-2 {
            grid-template-columns: 1fr;
          }
        }
      </style>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <script>document.addEventListener('DOMContentLoaded', () => { initHeader(''); });</script>

      <main class="wrap">
        <div class="row" style="justify-content:space-between; margin-bottom:14px;">
          <div class="row">

          </div>
          <div></div>
        </div>

        <div class="row" style="gap:10px; align-items:center; margin-bottom:8px;">
          <div style="font-weight:700; font-size:18px;">주문 상세</div>
          <span id="badge-cancel" class="pill">주문취소</span>
        </div>
        <div class="muted" id="order-id-line">주문번호: -</div>

        <div class="grid-2" style="margin-top:14px;">
          <div class="col">
            <section class="card">
              <div class="card-head"><span>🧾</span>주문 상품 (<span id="item-count">0</span>개)</div>
              <div class="card-body" id="items-box"></div>
            </section>

            <section class="card">
              <div class="card-head"><span>📍</span>배송 정보</div>
              <div class="card-body">
                <div class="row" style="gap:40px; margin-bottom:10px;">
                  <div>
                    <div class="muted">받는 분</div>
                    <div id="ship-name" style="margin-top:4px;">-</div>
                  </div>
                  <div>
                    <div class="muted">연락처</div>
                    <div id="ship-phone" style="margin-top:4px;">-</div>
                  </div>
                </div>
                <div style="margin-top:8px;">
                  <div class="muted">배송 주소</div>
                  <div id="ship-addr" style="margin-top:4px;">-</div>
                </div>

                <div style="margin-top:16px;">
                  <div class="muted" style="margin-bottom:8px;">배송 상태</div>
                  <div class="status-box list">
                    <div class="row"><span class="dot"></span><span>주문 접수</span></div>
                    <div class="row"><span class="dot gray"></span><span>배송 준비중</span></div>
                    <div class="row"><span class="dot gray"></span><span>배송 중</span></div>
                    <div class="row"><span class="dot gray"></span><span>배송 완료</span></div>
                  </div>
                </div>
              </div>
            </section>
          </div>

          <div class="col">
            <section class="card">
              <div class="card-head">주문 요약</div>
              <div class="card-body list">
                <div class="kv"><span class="muted">주문일자</span><span id="sum-date">-</span></div>
                <div class="kv"><span class="muted">상품 금액</span><span id="sum-products">-</span></div>
                <div class="kv"><span class="muted">배송비</span><span id="sum-ship">-</span></div>
                <div class="kv" style="font-weight:700"><span>총 결제 금액</span><span id="sum-total">-</span></div>
              </div>
            </section>

            <section class="card">
              <div class="card-head">결제 정보</div>
              <div class="card-body list">
                <div class="kv"><span class="muted">결제 수단</span><span id="pay-method">-</span></div>
                <div class="row" style="justify-content:flex-start; gap:10px;">
                  <span class="muted">결제 상태</span>
                  <span id="pay-state" class="pill gray">결제 완료</span>
                </div>
                <div class="row" style="margin-top:8px;">
                  <button id="btn-cancel" class="btn red small">결제 취소</button>
                </div>
              </div>
            </section>

            <section class="card">
              <div class="card-head">주문자 정보</div>
              <div class="card-body list">
                <div class="row" style="gap:8px;"><span>✉️</span><span id="ord-email">-</span></div>
                <div class="row" style="gap:8px;"><span>📞</span><span id="ord-phone">-</span></div>
              </div>
            </section>

            <section class="card">
              <div class="card-head">고객 지원</div>
              <div class="card-body list">
                <div class="kv"><span class="muted">배송 문의</span><span>1588-1234</span></div>
                <div class="kv"><span class="muted">교환/환불</span><span>1588-5678</span></div>
                <div class="kv"><span class="muted">운영시간</span><span>평일 9:00-18:00</span></div>
                <div class="row" style="margin-top:8px;">
                  <button class="btn" onclick="alert('고객센터로 연결됩니다.')">고객센터 문의</button>
                </div>
              </div>
            </section>
          </div>
        </div>
      </main>
      <!-- footer 시작 -->
      <jsp:include page="../common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <script>
        const qs = (sel, root = document) => root.querySelector(sel);
        const fmt = n => (Number(n) || 0).toLocaleString() + '원';

        function loadOrder() {
          const params = new URLSearchParams(location.search);
          const id = params.get('orderId');
          const orders = JSON.parse(localStorage.getItem('kp_orders') || '[]');
          let order = null;
          if (id) { order = orders.find(o => String(o.id) === String(id)); }
          if (!order) { order = JSON.parse(localStorage.getItem('kp_last_order') || 'null'); }
          if (!order) {
            order = {
              id: 'ORDER' + Date.now(),
              createdAt: new Date().toISOString(),
              items: [{ title: '대파', price: 1500, qty: 11, unit: '1단', img: 'assets/img/greenonion.jpg' }],
              receiver: { name: '김테스트', phone: '010-1234-5678', addr: '서울시 강남구 테헤란로 123번길 45' },
              payer: { email: 'test@example.com', phone: '010-1234-5678' },
              payment: { method: 'kakaoPay', state: 'paid' },
              shippingFee: 3000
            };
          }
          return order;
        }

        function renderOrder(order) {
          qs('#order-id-line').textContent = '주문번호: ' + order.id;

          const box = qs('#items-box'); box.innerHTML = '';
          let prodSum = 0;
          (order.items || []).forEach(it => {
            const line = document.createElement('div');
            line.className = 'kv';
            const qty = (it.qty || 1);
            prodSum += (it.price || 0) * qty;
            line.innerHTML = `
          <div class="row" style="gap:10px;align-items:center;">
            <div class="muted">${"${it.title}"}</div>
            <div class="muted">${"${it.unit}"}</div>
          </div>
          <div class="row" style="gap:12px;">
            <span class="muted">수량: ${"${qty}"}</span>
            <span class="price">${"${it.price}"}</span>
          </div>`;
            box.appendChild(line);
          });
          qs('#item-count').textContent = (order.items || []).length;

          qs('#ship-name').textContent = order.receiver?.name || '-';
          qs('#ship-phone').textContent = order.receiver?.phone || '-';
          qs('#ship-addr').textContent = order.receiver?.addr || '-';

          const d = new Date(order.createdAt || Date.now());
          qs('#sum-date').textContent = `${d.getFullYear()}. ${d.getMonth() + 1}. ${d.getDate()}.`;
          qs('#sum-products').textContent = fmt(prodSum);
          qs('#sum-ship').textContent = fmt(order.shippingFee || 0);
          qs('#sum-total').textContent = fmt(prodSum + (order.shippingFee || 0));

          qs('#pay-method').textContent = order.payment?.method || '-';
          const payState = qs('#pay-state');
          if (order.payment?.state === 'canceled') {
            payState.textContent = '결제 취소';
            payState.classList.remove('gray'); payState.classList.add('red');
            qs('#badge-cancel').classList.add('red');
          }
          qs('#ord-email').textContent = order.payer?.email || '-';
          qs('#ord-phone').textContent = order.payer?.phone || '-';
        }

        function hookCancel(order) {
          qs('#btn-cancel').addEventListener('click', () => {
            if (!confirm('정말 결제를 취소하시겠습니까?')) return;
            order.payment = order.payment || {};
            order.payment.state = 'canceled';

            let orders = JSON.parse(localStorage.getItem('kp_orders') || '[]');
            const idx = orders.findIndex(o => String(o.id) === String(order.id));
            if (idx >= 0) orders[idx] = order; else orders.push(order);
            localStorage.setItem('kp_orders', JSON.stringify(orders));
            localStorage.setItem('kp_last_order', JSON.stringify(order));

            renderOrder(order);
            alert('결제가 취소되었습니다.');
          });
        }

        document.addEventListener('DOMContentLoaded', () => {
          const order = loadOrder();
          renderOrder(order);
          hookCancel(order);
        });
      </script>
    </body>

    </html>