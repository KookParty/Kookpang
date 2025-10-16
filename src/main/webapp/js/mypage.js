/* KookPang mypage modal + inline editor removal (no CSS deletions) */
(function () {
  function getUserKey() {
    const keys = ["user", "currentUser", "authUser"];
    for (const k of keys) {
      try {
        if (localStorage.getItem(k)) return k;
      } catch (e) {}
    }
    return "user";
  }
  function readUser() {
    const k = getUserKey();
    try {
      const u = JSON.parse(localStorage.getItem(k) || "{}");
      u.__key = k;
      return u;
    } catch (e) {
      return { __key: k };
    }
  }
  function saveUser(u) {
    if (!u || !u.__key) return;
    const copy = { ...u };
    delete copy.__key;
    localStorage.setItem(u.__key, JSON.stringify(copy));
  }

  // 1) Build modal once (no HTML changes needed)
  if (!document.getElementById("kpEditModal")) {
    const wrap = document.createElement("div");
    wrap.id = "kpEditModal";
    wrap.className = "hidden";
    wrap.innerHTML = [
      '<div class="kp-modal-backdrop"></div>',
      '<div class="kp-modal-wrap" role="dialog" aria-modal="true">',
      '  <div class="kp-card">',
      '    <div class="kp-head"><span>정보 수정</span><button class="kp-close" aria-label="닫기">✕</button></div>',
      '    <div class="kp-body">',
      '      <label class="kp-field"><span>닉네임</span><input id="kpNick" type="text" placeholder="닉네임" /></label>',
      '      <label class="kp-field"><span>주소</span><input id="kpAddr" type="text" placeholder="주소" /></label>',
      '      <div style="height:1px;background:#e5e7eb;margin:6px 0;"></div>',
      '      <p style="margin:0;color:#6b7280;font-size:12px;">비밀번호 변경 (선택)</p>',
      '      <label class="kp-field"><span>현재 비밀번호</span><input id="kpPwOld" type="password" placeholder="현재 비밀번호" /></label>',
      '      <label class="kp-field"><span>새 비밀번호</span><input id="kpPwNew" type="password" placeholder="새 비밀번호 (6자 이상)" /></label>',
      '      <label class="kp-field"><span>새 비밀번호 확인</span><input id="kpPwCon" type="password" placeholder="새 비밀번호 확인" /></label>',
      "    </div>",
      '    <div class="kp-foot">',
      '      <button class="kp-btn ghost" id="kpCancel">취소</button>',
      '      <button class="kp-btn primary" id="kpSave">저장</button>',
      "    </div>",
      "  </div>",
      "</div>",
    ].join("");
    document.body.appendChild(wrap);
  }

  const modal = document.getElementById("kpEditModal");
  const $nick = document.getElementById("kpNick");
  const $addr = document.getElementById("kpAddr");
  const $old = document.getElementById("kpPwOld");
  const $new = document.getElementById("kpPwNew");
  const $con = document.getElementById("kpPwCon");

  function openModal() {
    const u = readUser();
    $nick.value = u.nickname || u.nick || "";
    $addr.value = u.address || "";
    $old.value = $new.value = $con.value = "";
    modal.classList.remove("hidden");
  }
  function closeModal() {
    modal.classList.add("hidden");
  }

  // Open triggers: 버튼 텍스트 "정보수정" 또는 프로필 카드 내 버튼
  document.querySelectorAll("button, a").forEach(function (el) {
    if (/정보\s*수정/.test(el.textContent || ""))
      el.addEventListener("click", function (e) {
        e.preventDefault();
        openModal();
      });
  });

  // Close events
  modal.querySelector(".kp-close").addEventListener("click", closeModal);
  modal.querySelector("#kpCancel").addEventListener("click", closeModal);
  modal.addEventListener("click", function (e) {
    if (e.target.classList.contains("kp-modal-backdrop")) closeModal();
  });
  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") closeModal();
  });

  // Save handler
  document.getElementById("kpSave").addEventListener("click", function () {
    const u = readUser();
    const newNick = ($nick.value || "").trim();
    if (!newNick) return alert("닉네임을 입력하세요.");

    const newAddr = ($addr.value || "").trim();
    const oldPw = $old.value || "";
    const newPw = $new.value || "";
    const conPw = $con.value || "";

    // password change if any field filled
    if (oldPw || newPw || conPw) {
      const stored = u.password || u.pw || "";
      if (!stored) {
        return alert("저장된 비밀번호가 없어 변경할 수 없습니다.");
      }
      if (oldPw !== stored) {
        return alert("현재 비밀번호가 일치하지 않습니다.");
      }
      if (newPw.length < 6) {
        return alert("새 비밀번호는 6자 이상이어야 합니다.");
      }
      if (newPw !== conPw) {
        return alert("새 비밀번호 확인이 일치하지 않습니다.");
      }
      u.password = newPw;
      u.pw = newPw;
    }

    u.nickname = newNick;
    u.nick = newNick;
    u.address = newAddr;
    saveUser(u);

    // 화면에 닉네임 노출 영역 갱신
    document
      .querySelectorAll("#userNickname, #mypageNickname, .profile .name")
      .forEach(function (n) {
        n.textContent = newNick;
      });

    closeModal();
    alert("정보가 수정되었습니다.");
  });

  // 2) 기존 하단 인라인 회원정보수정 폼 "숨김" (삭제 없이 화면에서만 제거)
  try {
    // 후보: mypage 컨테이너 내부에서 input 여러개 모여 있는 block
    var candidates = Array.from(
      document.querySelectorAll(
        "#mypage form, #mypage section, #mypage .card, #mypage .box, #mypage .panel"
      )
    );
    candidates.forEach(function (el) {
      var inputs = el.querySelectorAll(
        'input[type="text"], input[type="password"]'
      );
      var hasLabels = /닉네임|주소|비밀번호/.test(el.textContent || "");
      if (inputs.length >= 3 && hasLabels) {
        // 버튼 "저장"이 있거나, 하단 영역일 확률이 높을 때
        if (/저장|확인|수정/.test(el.textContent || "")) {
          el.style.display = "none";
        }
      }
    });
  } catch (e) {}
})();
