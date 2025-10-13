const KP_KEYS = {
  USER: "kp_user",
  LIKES: "kp_likes",
  POSTS: "kp_posts",
  ORDERS: "kp_orders",
  CART: "kp_cart",
};
const S = {
  get: (k, d = null) => {
    try {
      return JSON.parse(localStorage.getItem(k)) ?? d;
    } catch (e) {
      return d;
    }
  },
  set: (k, v) => localStorage.setItem(k, JSON.stringify(v)),
  remove: (k) => localStorage.removeItem(k),
};
function isAuthed() {
  return !!S.get(KP_KEYS.USER);
}
function getUser() {
  return S.get(KP_KEYS.USER);
}
function setUser(u) {
  S.set(KP_KEYS.USER, u);
}
function logout() {
  S.remove(KP_KEYS.USER);
}
function loginByEmail(e, p) {
  const u = getUser();
  return !!u && u.email === e && u.password === p;
}
function initHeader(active) {
  document
    .querySelectorAll(".nav a")
    .forEach((a) => a.classList.toggle("active", a.dataset.page === active));
  const n = document.querySelector("#js-nickname"),
    lb = document.querySelector("#js-login-btn"),
    lo = document.querySelector("#js-logout-btn"),
    mp = document.querySelector("#js-mypage-link");
  if (isAuthed()) {
    const u = getUser();
    n.textContent = u.nickname || u.name || "사용자";
    n.style.display = "inline-flex";
    lb.style.display = "none";
    lo.style.display = "inline-flex";
    mp.style.display = "inline-flex";
  } else {
    n.style.display = "none";
    lb.style.display = "inline-flex";
    lo.style.display = "none";
    mp.style.display = "none";
  }
  lo?.addEventListener("click", () => {
    logout();
    location.href = CONTEXT_PATH + "/index.jsp";
  });
  document.querySelectorAll("[data-need-auth]").forEach((a) =>
    a.addEventListener("click", (e) => {
      if (!isAuthed()) {
        e.preventDefault();
        alert("로그인 후 이용 가능합니다.");
        location.href =
          CONTEXT_PATH +
          "/users/login.jsp?redirect=" +
          encodeURIComponent(a.getAttribute("href"));
      }
    })
  );
}
function requireAuth() {
  if (!isAuthed()) {
    alert("로그인 후 이용 가능합니다.");
    location.href =
      CONTEXT_PATH +
      "/users/login.jsp?redirect=" +
      encodeURIComponent(location.pathname.split("/").pop());
  }
}
function q(n) {
  const u = new URL(location.href);
  return u.searchParams.get(n);
}

/* KP Cart Badge addon */
(function () {
  function getCart() {
    try {
      return JSON.parse(localStorage.getItem("kp_cart") || "[]");
    } catch (_) {
      return [];
    }
  }
  function cartCount() {
    return getCart().reduce((s, x) => s + (x.qty || 1), 0);
  }
  function ensureBadge() {
    var a = document.getElementById("cartBtn");
    if (!a) return;
    if (getComputedStyle(a).position === "static") {
      a.style.position = "relative";
    }
    var b = document.getElementById("js-cart-badge");
    if (!b) {
      b = document.createElement("span");
      b.id = "js-cart-badge";
      b.className = "cart-badge";
      a.appendChild(b);
    }
    var c = cartCount();
    b.textContent = c > 0 ? String(c) : "";
    b.style.display = c > 0 ? "inline-flex" : "none";
  }
  window.kpUpdateCartBadge = ensureBadge;
  document.addEventListener("DOMContentLoaded", ensureBadge);
  window.addEventListener("storage", ensureBadge);
})();
