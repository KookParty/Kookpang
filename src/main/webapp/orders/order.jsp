<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="jakarta.tags.core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>주문</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script>const PAGE_ACTIVE = 'order'</script>

    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <script>document.addEventListener('DOMContentLoaded', () => { initHeader(PAGE_ACTIVE); });</script>
      <main class="container page">
        <section class="card" style="padding:16px"><b>주문 페이지</b> — 로그인한 사용자만 접근 가능</section>
        <section class="grid cols-2" id="cart" style="margin-top:16px"></section>
        <div style="margin-top:12px"><button class="btn dark" id="orderBtn">주문하기</button></div>
      </main>
      <!-- footer 시작 -->
      <jsp:include page="../common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <script>
        requireAuth();
        const items = RECIPES.map(r => (
          {
            id: r.id,
            title: r.title,
            img: r.img, qty: 1,
            price: r.id === 'kimchi' ? 8000 : 12000
          }
        ));
        const cart = document.getElementById('cart');
        if (items.length === 0) {
          document.getElementById('cart').innerHTML =
            '<div class="card" style="padding:16px">장바구니가 비어 있습니다.</div>';
        }
        items.forEach(it => {
          const el = document.createElement('article');
          el.className = 'card';
          el.style.padding = '14px';
          el.innerHTML =
            '<div style="display:flex;gap:12px;align-items:center"><img src="' + ('${path}/assets/img/' + it.img) + '" style="width:120px;height:90px;object-fit:cover;border-radius:10px"><div><b>' + it.title + '</b><div class="small">수량 ' + it.qty + ' · ' + (it.price * it.qty).toLocaleString() + '원</div></div></div>'; cart.appendChild(el);
        });
        orderBtn.onclick = () => {
          const orders = S.get(KP_KEYS.ORDERS, []);
          const total = items.reduce((s, x) => s + x.price * x.qty, 0);
          orders.push({ id: Date.now(), items, total, created: Date.now() });
          S.set(KP_KEYS.ORDERS, orders);
          location.href = 'order-result.html?id=' + orders[orders.length - 1].id
        };
      </script>
      <script>
        /*! KP force review – nuclear inline patch */
        (function () {
          const REVIEW = 'order-review.html';
          function rebind(el) {
            const clone = el.cloneNode(true);
            clone.addEventListener('click', function (ev) {
              ev.preventDefault(); ev.stopPropagation(); ev.stopImmediatePropagation();
              location.href = REVIEW;
            }, true);
            el.replaceWith(clone);
          }
          function sweep() {
            document.querySelectorAll('a,button,input[type=button],input[type=submit]').forEach(el => {
              if (el.dataset.kpBound) return;
              const t = (el.innerText || el.value || '').trim();
              const href = (el.getAttribute && el.getAttribute('href')) || '';
              if (/주문하기|결제|구매/i.test(t) || /order-result\.html|checkout|pay/i.test(href)) {
                el.dataset.kpBound = '1';
                rebind(el);
              }
            });
          }
          window.addEventListener('click', function (ev) {
            const btn = ev.target.closest('a,button,input[type=button],input[type=submit]');
            if (!btn) return;
            const t = (btn.innerText || btn.value || '').trim();
            const href = (btn.getAttribute && btn.getAttribute('href')) || '';
            if (/주문하기|결제|구매/i.test(t) || /order-result\.html|checkout|pay/i.test(href)) {
              ev.preventDefault(); ev.stopPropagation(); ev.stopImmediatePropagation();
              location.href = REVIEW;
            }
          }, true);
          const mo = new MutationObserver(sweep);
          mo.observe(document.documentElement, { childList: true, subtree: true });
          sweep();
          const _push = history.pushState;
          history.pushState = function (a, b, url) { if (url && /order-result\.html/i.test(url)) url = REVIEW; return _push.call(this, a, b, url); };
          const _replace = history.replaceState;
          history.replaceState = function (a, b, url) { if (url && /order-result\.html/i.test(url)) url = REVIEW; return _replace.call(this, a, b, url); };
          const _assign = window.location.assign.bind(window.location);
          window.location.assign = function (url) { if (/order-result\.html/i.test(url)) url = REVIEW; return _assign(url); };
          try {
            const desc = Object.getOwnPropertyDescriptor(Location.prototype, 'href');
            if (desc && desc.set) {
              Object.defineProperty(window.location, 'href', {
                configurable: true,
                get() { return desc.get.call(window.location); },
                set(v) { if (/order-result\.html/i.test(String(v))) v = REVIEW; return desc.set.call(window.location, v); }
              });
            }
          } catch (_) { }
        })();
      </script>

    </body>

    </html>