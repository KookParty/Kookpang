// auth-guard.js — guest restriction (view-only for guests)
(function () {
  window.auth = {
    isLoggedIn() {
      try {
        const u = JSON.parse(localStorage.getItem("kp_user") || "{}");
        return !!(u && (u.id || u.token));
      } catch {
        return false;
      }
    },
    require(e, next) {
      if (this.isLoggedIn()) return true;
      if (e && e.preventDefault) e.preventDefault();
      alert("로그인 후 이용해주세요.");
      const dest = next || location.pathname + location.search;
      location.href =
        CONTEXT_PATH + "users/login.jsp?next=" + encodeURIComponent(dest);
      return false;
    },
  };
  document.addEventListener(
    "click",
    (e) => {
      const t = e.target.closest("[data-need-auth]");
      if (!t) return;
      if (!auth.isLoggedIn()) {
        e.preventDefault();
        const href =
          t.getAttribute("href") || location.pathname + location.search;
        alert("로그인 후 이용해주세요.");
        location.href =
          CONTEXT_PATH + "/users/login.jsp?next=" + encodeURIComponent(href);
      }
    },
    { capture: true }
  );
  function attach() {
    const path = CONTEXT_PATH;
    document
      .querySelectorAll(`a[href*="${path}/orders/order-review.jsp"]`)
      .forEach((a) => a.setAttribute("data-need-auth", ""));
    document
      .querySelectorAll(`a[href*="${path}/boards/board-write.jsp"], #newBtn`)
      .forEach((el) => el.setAttribute("data-need-auth", ""));
    const vb =
      document.getElementById("variantBtn") ||
      document.querySelector("[data-variant-write]");
    if (vb) vb.setAttribute("data-need-auth", "");
    document.querySelectorAll("[data-add]").forEach((btn) => {
      btn.addEventListener(
        "click",
        (e) => {
          if (!auth.isLoggedIn())
            auth.require(e, path + "/orders/ingredients.jsp");
        },
        { capture: true }
      );
    });
    const sel = document.getElementById("btn-add-selected");
    if (sel)
      sel.addEventListener(
        "click",
        (e) => {
          if (!auth.isLoggedIn())
            auth.require(e, location.pathname + location.search);
        },
        { capture: true }
      );
  }
  function hard() {
    const cPath = CONTEXT_PATH;
    const path = location.pathname.replace(/^\//, "").toLowerCase();
    const protect = [
      "order-review.jsp",
      "order.jsp",
      "board-write.jsp",
      "variant-write.jsp",
    ];
    if (protect.some((p) => path.endsWith(p)) && !auth.isLoggedIn()) {
      const box =
        document.querySelector(".page") ||
        document.querySelector(".ov-wrap") ||
        document.body;
      if (box) {
        box.innerHTML = `
          <section class="card" style="padding:48px 24px;max-width:760px;margin:40px auto;text-align:center;border:1px solid #e5e7eb;border-radius:16px;background:#fff">
            <h2 style="margin:0 0 10px">로그인이 필요합니다</h2>
            <p class="muted" style="margin:0 0 16px">이 기능은 로그인 후 이용하실 수 있어요.</p>
            <div style="display:flex;gap:8px;justify-content:center">
              <a class="btn" href="${cPath}/users/login.jsp?next=${encodeURIComponent(
          location.pathname + location.search
        )}">로그인하기</a>
              <a class="btn" style="background:#fff;color:#111;border:1px solid #e5e7eb" href="${cPate}/recipes/recipes.jsp">레시피 보러가기</a>
            </div>
          </section>`;
      } else {
        location.href =
          cPath +
          "users/login.jsp?next=" +
          encodeURIComponent(location.pathname + location.search);
      }
    }
  }
  document.addEventListener("DOMContentLoaded", () => {
    attach();
    hard();
    try {
      window.kpUpdateCartBadge && window.kpUpdateCartBadge();
    } catch {}
  });
  new MutationObserver(attach).observe(document.documentElement, {
    subtree: true,
    childList: true,
  });
})();
