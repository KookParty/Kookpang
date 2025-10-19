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
  list.forEach((item) => {
    const itemTotal = item.price * item.quantity;
    console.log(itemTotal, item.imageUrl);
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
                    <button>-</button>
                    <span>${item.count} </span>
                    <button>+</button>
                    <span><b>${item.price * item.count}원</b></span>
                    <button class="ov-del" title="삭제" data-del="">🗑</button>
                  </div>
                </div>
          `;
  });
};

  //ajax로 장바구니 데이터 가져오기
  const getCart = async function () {
    let list = [];
    try {
      const response = await fetch(conPath + "/ajax", {
        method: "POST",
        body: new URLSearchParams({
          key: "cart",
          methodName: "selectByUserId",
        })
      });
      if (response.ok) {
        list = await response.json();
      }else {
        console.error("Failed to fetch cart data:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching cart data:", error);
    }
  
  // const list = [
  //   {
  //     id: 1,
  //     name: "아이템1",
  //     price: 10000,
  //     unit: "1포기",
  //     quantity: 1,
  //     imageUrl: "../assets/img/beef.jpg",
  //   },
  //   {
  //     id: 2,
  //     name: "아이템2",
  //     price: 20000,
  //     unit: "1봉지",
  //     quantity: 2,
  //     imageUrl: "../assets/img/chili.jpg",
  //   },
  // ];
  showCart(list);
  console.log(list);
}
