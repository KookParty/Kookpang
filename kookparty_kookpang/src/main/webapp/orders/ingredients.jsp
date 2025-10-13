<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>식재료 쇼핑</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script>const PAGE_ACTIVE = 'ingredients'</script>
      <script src="${path}/js/app.js"></script>
      <script src="${path}/js/data.js"></script>
      <script src="${path}/js/seed.js"></script>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <script>
        document.addEventListener('DOMContentLoaded', () => { try { initHeader('ingredients'); } catch (_) { } });
      </script>

      <main class="container page">
        <h1 style="text-align:center;margin:0 0 6px">식재료 쇼핑</h1>
        <p class="small" style="text-align:center;margin:0 0 12px">신선하고 좋은 품질의 식재료를 만나보세요</p>

        <!-- 필터 -->
        <div class="section-head" style="display:flex;align-items:center;gap:8px;flex-wrap:wrap">
          <input id="q" class="input" placeholder="식재료를 검색해보세요..." style="flex:1;min-width:240px">
          <select id="cat" class="input" style="width:160px">
            <option value="">카테고리 전체</option>
            <option value="육류">육류</option>
            <option value="채소">채소</option>
            <option value="양념">양념</option>
            <option value="김치류">김치류</option>
          </select>
          <select id="sort" class="input" style="width:140px">
            <option value="popular">인기순</option>
            <option value="price-asc">낮은 가격</option>
            <option value="price-desc">높은 가격</option>
          </select>
          <button id="reset" class="btn">초기화</button>
        </div>

        <!-- 그리드 -->
        <section id="grid" class="grid cols-3"></section>
      </main>
      <!-- footer 시작 -->
      <jsp:include page="../common/footer.jsp"></jsp:include>
      <!-- footer 끝 -->
      <script>
        (function () {
          const KP_CART = 'kp_cart';
          const KP_ING = 'kp_ingredients';
          const price = n => (Number(n) || 0).toLocaleString() + '원';

          // 1) 기본 더미(없을 때만 사용)
          const INGREDIENTS_SEED = [
            { id: 'ing_pork', name: '돼지고기 (삼겹살)', category: '육류', label: '보통', price: 15000, unit: '500g', img: 'assets/img/pork.jpg' },
            { id: 'ing_beef', name: '소고기 (등심)', category: '육류', label: '보통', price: 22000, unit: '300g', img: 'assets/img/beef.jpg' },
            { id: 'ing_chicken', name: '닭가슴살', category: '육류', label: '쉬움', price: 7800, unit: '500g', img: 'assets/img/chicken.jpg' },
            { id: 'ing_kimchi', name: '김치', category: '김치류', label: '보통', price: 8000, unit: '1kg', img: 'assets/img/kimchi_ing.jpg' },
            { id: 'ing_tofu', name: '두부', category: '채소', label: '쉬움', price: 2500, unit: '1모', img: 'assets/img/tofu.jpg' },
            { id: 'ing_greenonion', name: '대파', category: '채소', label: '쉬움', price: 1500, unit: '2대', img: 'assets/img/greenonion.jpg' },
            { id: 'ing_onion', name: '양파', category: '채소', label: '쉬움', price: 3000, unit: '1망', img: 'assets/img/onion.jpg' },
            { id: 'ing_garlic', name: '마늘', category: '양념', label: '쉬움', price: 4000, unit: '500g', img: 'assets/img/garlic.jpg' },
            { id: 'ing_chili', name: '청양고추', category: '양념', label: '쉬움', price: 3500, unit: '200g', img: 'assets/img/chili.jpg' },
            { id: 'ing_soy', name: '진간장', category: '양념', label: '쉬움', price: 4500, unit: '500ml', img: 'assets/img/soy.jpg' },
            { id: 'ing_paste', name: '고추장', category: '양념', label: '쉬움', price: 6500, unit: '500g', img: 'assets/img/gochujang.jpg' },
            { id: 'ing_sesame', name: '참기름', category: '양념', label: '초보', price: 8000, unit: '330ml', img: 'assets/img/sesame.jpg' }
          ];

          // 2) 데이터 소스 결정: localStorage -> window.INGREDIENTS -> SEED
          function loadIngredients() {
            try {
              const saved = JSON.parse(localStorage.getItem(KP_ING) || '[]');
              if (Array.isArray(saved) && saved.length) return saved;
            } catch (_) { }

            if (Array.isArray(window.INGREDIENTS) && window.INGREDIENTS.length) {
              localStorage.setItem(KP_ING, JSON.stringify(window.INGREDIENTS));
              return window.INGREDIENTS.slice();
            }

            localStorage.setItem(KP_ING, JSON.stringify(INGREDIENTS_SEED));
            return INGREDIENTS_SEED.slice();
          }

          let ITEMS = loadIngredients();

          // 3) 렌더
          const grid = document.getElementById('grid');
          const FALLBACK = window.FALLBACK || {};

          function render(list) {
            if (!Array.isArray(list)) list = [];
            if (!list.length) {
              grid.innerHTML = '<div class="muted">표시할 식재료가 없습니다.</div>';
              return;
            }
            grid.innerHTML = list.map(it => `
      <article class="card tile">
        <div class="thumb">
          <img src='${path}/${"${it.img}"}'>
        </div>
        <div class="body">
          <div class="meta">
            <span class="label">${"${it.category}"}</span>
            <span class="label" style="background:#10b981">${"${it.label}"}</span>
          </div>
          <b>${"${it.name || ''}"}</b>
          <div class="meta"><span>${"${price(it.price)}"}</span><span>${"${it.unit || ''}"}</span></div>
          <div class="row"><button class="btn dark full" data-add="${'${it.id}'}">담기</button></div>
        </div>
      </article>
    `).join('');
          }

          // 4) 필터
          function filter() {
            const q = (document.getElementById('q').value || '').trim().toLowerCase();
            const cat = document.getElementById('cat').value;
            const sort = document.getElementById('sort').value;
            let list = ITEMS.filter(i =>
              (!q || (i.name || '').toLowerCase().includes(q)) &&
              (!cat || i.category === cat)
            );
            if (sort === 'price-asc') list.sort((a, b) => a.price - b.price);
            if (sort === 'price-desc') list.sort((a, b) => b.price - a.price);
            render(list);
          }

          // 5) 이벤트
          document.getElementById('q').addEventListener('input', filter);
          document.getElementById('cat').addEventListener('change', filter);
          document.getElementById('sort').addEventListener('change', filter);
          document.getElementById('reset').addEventListener('click', () => {
            document.getElementById('q').value = '';
            document.getElementById('cat').value = '';
            document.getElementById('sort').value = 'popular';
            filter();
          });

          // 장바구니 담기 (위임)
          document.addEventListener('click', (e) => {
            const id = e.target?.dataset?.add; if (!id) return;
            const item = ITEMS.find(x => x.id === id); if (!item) return;

            let cart = []; try { cart = JSON.parse(localStorage.getItem(KP_CART) || '[]'); } catch (_) { }
            const idx = cart.findIndex(c => c.id === id);
            if (idx > -1) cart[idx].qty = (cart[idx].qty || 1) + 1;
            else cart.push({ id: item.id, title: item.name, price: Number(item.price || 0), qty: 1, img: item.img });
            localStorage.setItem(KP_CART, JSON.stringify(cart));

            // 배지 갱신 + 토스트
            try { window.kpUpdateCartBadge && window.kpUpdateCartBadge(); } catch (_) { }
            const n = document.createElement('div');
            n.textContent = '장바구니에 담겼습니다.';
            n.style.cssText = 'position:fixed;right:16px;bottom:16px;background:#111;color:#fff;padding:10px 14px;border-radius:10px;z-index:9999';
            document.body.appendChild(n); setTimeout(() => n.remove(), 900);
          });

          // 6) 초기 진입
          document.addEventListener('DOMContentLoaded', () => {
            filter();
            try { window.kpUpdateCartBadge && window.kpUpdateCartBadge(); } catch (_) { }
          });
        })();
      </script>
    </body>

    </html>