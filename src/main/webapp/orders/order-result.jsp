<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>주문 상세</title>
    <link rel="stylesheet" href="${path}/css/styles.css" />
    <script type="text/javascript">
      const CONTEXT_PATH = "${pageContext.request.contextPath}";
    </script>
    <script src="${path}/js/config.js"></script>
    <script src="${path}/js/app.js"></script>
    <script src="${path}/js/seed.js"></script>
  </head>

  <body>
    <!-- header시작 -->
    <jsp:include page="../common/header.jsp"></jsp:include>
    <!-- header끝 -->
    <main class="container wide page" id="root">
      <div class="section-head">
        <div>
          <span class="small">주문번호: ORDER1</span>
        </div>
        <a class="btn" href="#">주문 취소</a>
      </div>
      <div class="grid cols-2">
        <section class="grid" style="gap: 16px">
          <article class="card" style="padding: 16px">
            <h3 style="margin: 0 0 8px">주문 상품 (${count}개)</h3>
			<!-- 반복 -->
			<c:forEach var="item" items="${list}">
            <div class="list-row">
              <span>${item.name} / ${item.count}</span>
              <b>${item.price}원</b>
            </div>
            </c:forEach>
            <!-- 반복 -->
          </article>
          <article class="card" style="padding: 16px">
            <h3 style="margin: 0 0 8px">배송 정보</h3>
            <p class="small">받는 분: ${name } · 연락처 ${phone }</p>
            <p class="small">배송 주소: ${order.shippingAddress }</p>
            <div class="card" style="padding: 12px; margin-top: 10px; background: #f3f4f6; border-style: dashed">
              <!-- 
              <b>배송 상태</b>
              <ul class="small">
                <li>● 주문 접수</li>
                <li>○ 배송 준비중</li>
                <li>○ 배송 중</li>
                <li>○ 배송 완료</li>
              </ul>
               -->
            </div>
          </article>
        </section>
        <section class="grid" style="gap: 16px">
          <article class="card" style="padding: 16px">
            <h3 style="margin: 0 0 8px">주문 요약</h3>
            <div class="list-row">
              <span>상품 금액</span>
              <b>${order.totalPrice }원</b>
            </div>
            <div class="list-row">
              <span>배송비</span>
              <b>${order.deliveryFee }원</b>
            </div>
            <div class="list-row" style="font-weight: 700">
              <span>총 결제 금액</span>
              <span>${order.totalPrice + order.deliveryFee}원</span>
            </div>
          </article>
          <article class="card" style="padding: 16px">
            <h3 style="margin: 0 0 8px">결제 정보</h3>
            <p class="small">결제 수단 kakaoPay</p>
            <span class="badge">결제 완료</span>
          </article>
          <article class="card" style="padding: 16px">
            <h3 style="margin: 0 0 8px">주문자 정보</h3>
            <p class="small">${name } · ${phone }</p>
          </article>
          <article class="card" style="padding: 16px">
            <h3 style="margin: 0 0 8px">고객 지원</h3>
            <p class="small">
              배송 문의: 1588-1234<br />
              교환/환불: 1588-5678<br />
              운영시간: 평일 9:00-18:00
            </p>
            <button class="btn full">고객센터 문의</button>
          </article>
        </section>
      </div>
    </main>
    <!-- 
    <script>
      requireAuth();
      const id = +q("id");
      const orders = S.get(KP_KEYS.ORDERS, []);
      const o = orders.find((x) => x.id === id) || orders[orders.length - 1];
      let itemsHtml = o.items
        .map(
          (it) => `
            <div class='list-row'>
              <span>${"${it.title}"}</span>
              <b>${"${(it.price * it.qty).toLocaleString()}"}원</b>
            </div>`
        )
        .join("");
      root.innerHTML = `
      <div class="section-head">
        <div>
          <span class="small">주문번호: ORDER${"${id}"}</span>
        </div>
        <a class="btn" href="order-cancel.html?orderId=${"${id}"}">주문 취소</a>
      </div>
      <div class="grid cols-2">
        <section class="grid" style="gap:16px">
          <article class="card" style="padding:16px">
            <h3 style="margin:0 0 8px">주문 상품 (${"${o.items.length}"}개)</h3>
            ${"${itemsHtml}"}
          </article>
          <article class="card" style="padding:16px">
            <h3 style="margin:0 0 8px">배송 정보</h3>
            <p class="small">받는 분: 김테스트 · 연락처 010-1234-5678</p>
            <p class="small">배송 주소: 서울시 강남구 테헤란로 123번길 45</p>
            <div class="card" style="padding:12px;margin-top:10px;background:#f3f4f6;border-style:dashed">
              <b>배송 상태</b>
              <ul class="small">
                <li>● 주문 접수</li>
                <li>○ 배송 준비중</li>
                <li>○ 배송 중</li>
                <li>○ 배송 완료</li>
              </ul>
            </div>
          </article>
        </section>
        <section class="grid" style="gap:16px">
          <article class="card" style="padding:16px">
            <h3 style="margin:0 0 8px">주문 요약</h3>
            <div class="list-row">
              <span>상품 금액</span>
              <b>${"${(o.total - 3000).toLocaleString()}"}원</b>
            </div>
            <div class="list-row">
              <span>배송비</span>
              <b>3,000원</b>
            </div>
            <div class="list-row" style="font-weight:700">
                <span>총 결제 금액</span>
                <span>${"${o.total.toLocaleString()}"}원</span>
            </div>
          </article>
          <article class="card" style="padding:16px">
            <h3 style="margin:0 0 8px">결제 정보</h3>
            <p class="small">결제 수단 kakaoPayt</p>
            <span class="badge">결제 완료</span>
          </article>
          <article class="card" style="padding:16px">
            <h3 style="margin:0 0 8px">주문자 정보</h3>
            <p class="small">test@example.com · 010-1234-5678</p>
          </article>
          <article class="card" style="padding:16px">
            <h3 style="margin:0 0 8px">고객 지원</h3>
            <p class="small">배송 문의: 1588-1234<br>
              교환/환불: 1588-5678<br>
              운영시간: 평일 9:00-18:00
            </p>
            <button class="btn full">고객센터 문의</button>
          </article>
        </section>
      </div>`;
    </script>
     -->
  </body>
</html>
