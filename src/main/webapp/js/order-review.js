(function () {
          const KP_CART = 'kp_cart', KP_ORDERS = 'kp_orders', KP_USER = 'kp_user';
          const price = n => (n || 0).toLocaleString() + '원';
          const get = (k, d) => JSON.parse(localStorage.getItem(k) || d || '[]');
          const set = (k, v) => localStorage.setItem(k, JSON.stringify(v));

          const itemsEl = document.getElementById('ov-items');
          const countEl = document.getElementById('ov-count');
          const pEl = document.getElementById('ov-price');
          const tEl = document.getElementById('ov-total');
          const shipEl = document.getElementById('ov-shipinfo');

          function render() {
            const cart = get(KP_CART);
            countEl.textContent = '(' + cart.reduce((s, x) => s + (x.qty || 1), 0) + '개)';
            itemsEl.innerHTML = cart.map((c, i) => `
      <div class="ov-row">
        <div>
          <div style="font-weight:700">${c.title}</div>
          <div class="small" style="color:#6b7280">${price(c.price)} / 1포기</div>
        </div>
        <div class="ov-qty">
          <button data-minus="${i}">-</button>
          <span>${c.qty || 1}</span>
          <button data-plus="${i}">+</button>
          <span><b>${price((c.qty || 1) * c.price)}</b></span>
          <button class="ov-del" title="삭제" data-del="${i}">🗑</button>
        </div>
      </div>`).join('') || '<div class="small" style="color:#6b7280">장바구니가 비어 있습니다.</div>';

            itemsEl.querySelectorAll('[data-minus]').forEach(b => b.addEventListener('click', () => {
              const idx = Number(b.getAttribute('data-minus')); const cart = get(KP_CART);
              cart[idx].qty = Math.max(1, (cart[idx].qty || 1) - 1); set(KP_CART, cart); render();
            }));
            itemsEl.querySelectorAll('[data-plus]').forEach(b => b.addEventListener('click', () => {
              const idx = Number(b.getAttribute('data-plus')); const cart = get(KP_CART);
              cart[idx].qty = (cart[idx].qty || 1) + 1; set(KP_CART, cart); render();
            }));
            itemsEl.querySelectorAll('[data-del]').forEach(b => b.addEventListener('click', () => {
              const idx = Number(b.getAttribute('data-del')); const cart = get(KP_CART);
              cart.splice(idx, 1); set(KP_CART, cart); render();
            }));

            const subtotal = cart.reduce((s, x) => s + x.price * (x.qty || 1), 0);
            pEl.textContent = price(subtotal);
            tEl.textContent = price(subtotal + 3000);

            const user = JSON.parse(localStorage.getItem(KP_USER) || '{}');
            shipEl.innerHTML = `받는 분: <b>${user.name || '김테스트'}</b> &nbsp;/&nbsp; 연락처: <b>${user.phone || '010-1234-5678'}</b><br/>
                        주소: <b>${user.address || '서울시 강남구 테헤란로 123번길 45'}</b>`;
          }

          document.getElementById('ov-pay').addEventListener('click', () => {
            const cart = get(KP_CART);
            if (!cart.length) return alert('장바구니가 비어 있습니다.');
            const subtotal = cart.reduce((s, x) => s + x.price * (x.qty || 1), 0);
            const id = Date.now(); const orders = get(KP_ORDERS);
            orders.push({ id, items: cart, total: subtotal, created: Date.now(), paymethod: 'kakaoPay', status: 'paid' });
            set(KP_ORDERS, orders); localStorage.removeItem(KP_CART);
            location.href = CONTEXT_PATH + '/orders/order-result.jsp?id=' + id;
          });

          render();
        })();