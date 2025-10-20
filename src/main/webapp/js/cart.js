const order = function () {
  location.href = conPath + "/orders/order-review.jsp";
};
const showCart = function (list) {
  const emptyState = document.getElementById("emptyState");
  const cartState = document.getElementById("cartState");
  if (list && list.length > 0) {
    emptyState.style.display = "none";
    cartState.style.display = "block";
  } else {
    emptyState.style.display = "block";
    cartState.style.display = "none";
  }
  const ovItems = document.getElementById("ov-items");
  ovItems.innerHTML = "";
  let totalPrice = 0;
  let deliveryFee = 3000;
  list.forEach((item) => {
    const itemTotal = item.price * item.count;
    totalPrice += itemTotal;

    ovItems.innerHTML += `
            <div class="ov-row2">
                  <div class="cart-img">
                    <img src="${conPath}/${item.imageUrl}" alt="${item.name}" />
                  </div>
                  <div>
                    <div style="font-weight: 700">${item.name}</div>
                    <div class="small" style="color: #6b7280">${item.price.toLocaleString()}원 / ${item.count}</div>
                  </div>
                  <div class="ov-qty">
                    <button onclick="minusItemCount(${item.cartId})">-</button>
                    <span id="item-count-${item.cartId}">${item.count}</span>
                    <button onclick="plusItemCount(${item.cartId})">+</button>
                    <span><b>${item.price * item.count}원</b></span>
                    <button class="ov-del" title="삭제" onclick="removeFromCart(${item.cartId})">🗑</button>
                  </div>
                </div>
          `;
  });
  if (totalPrice >= 50000) {
    deliveryFee = 0;
  }

  document.getElementById("ov-price").textContent = totalPrice.toLocaleString();
  document.getElementById("ov-ship").textContent = deliveryFee.toLocaleString();
  document.getElementById("ov-total").textContent = (totalPrice + deliveryFee).toLocaleString();
};

//ajax로 장바구니 데이터 가져오기
const getCart = async function () {
  let list = [];
  console.log("Fetching cart data...");
  try {
    const response = await fetch(conPath + "/ajax", {
      method: "POST",
      body: new URLSearchParams({
        key: "cart",
        methodName: "selectByUserId",
      }),
    });
    if (response.ok) {
      list = await response.json();
    } else {
      console.error("Failed to fetch cart data:", response.statusText);
    }
  } catch (error) {
    console.error("Error fetching cart data:", error);
  }
  showCart(list);
  console.log(list);
};

const removeFromCart = async function (cartId) {
  try {
    const response = await fetch(conPath + "/ajax", {
      method: "POST",
      body: new URLSearchParams({
        key: "cart",
        methodName: "deleteCartByCartId",
        cartId: cartId,
      }),
    });
    if (response.ok) {
      getCart();
    } else {
      console.error("Failed to remove item from cart:", response.statusText);
    }
  } catch (error) {
    console.error("Error removing item from cart:", error);
  }
};

const plusItemCount = function (cartId) {
  const countSpan = document.getElementById(`item-count-${cartId}`);
  let currentCount = parseInt(countSpan.textContent, 10);
  const newCount = currentCount + 1;
  updateCartCount(cartId, newCount);
};
const minusItemCount = function (cartId) {
  const countSpan = document.getElementById(`item-count-${cartId}`);
  let currentCount = parseInt(countSpan.textContent, 10);
  const newCount = currentCount - 1;
  if (newCount === 0) {
    removeFromCart(cartId);
    return;
  } else {
    updateCartCount(cartId, newCount);
  }
};

const updateCartCount = async function (cartId, newCount) {
  try {
    const response = await fetch(conPath + "/ajax", {
      method: "POST",
      body: new URLSearchParams({
        key: "cart",
        methodName: "updateCartCount",
        cartId: cartId,
        newCount: newCount,
      }),
    });
    if (response.ok) {
      getCart();
    } else {
      console.error("Failed to update cart count:", response.statusText);
    }
  } catch (error) {
    console.error("Error updating cart count:", error);
  }
};
