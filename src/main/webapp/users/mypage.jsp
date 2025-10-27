<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="jakarta.tags.core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>마이페이지</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script>const PAGE_ACTIVE = "mypage"</script>
      <script src="${path}/js/app.js"></script>
      <script src="${path}/js/seed.js"></script>
      <style>
        /* Normalize MyPage layout width to other pages */
        #mypage.container.page {
          max-width: 1040px;
        }

        #mypage .mpp-grid {
          display: grid;
          grid-template-columns: 260px 1fr;
          gap: 16px;
        }

        #mypage .card {
          background: #fff;
          border: 1px solid #e5e7eb;
          border-radius: 12px;
        }

        #mypage .profile {
          padding: 16px;
        }

        #mypage .profile .avatar {
          width: 64px;
          height: 64px;
          border-radius: 999px;
          background: #111;
          color: #fff;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 800;
          margin-bottom: 8px;
        }

        #mypage .section {
          padding: 0;
        }

        #mypage section .section-head {
          padding: 0px 20px;
          border-bottom: 1px solid #eef1f4;
          font-weight: 700;
          display: flex;
          align-items: center;
          justify-content: space-between
        }

        #mypage .section .section-body {
          padding: 12px 14px;
        }

        #mypage .btn.write {
          padding: 6px 10px;
          font-size: 14px
        }

    /* Modal Styles */
    .modal-overlay {
      display: none;
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      z-index: 1000;
      align-items: center;
      justify-content: center;
    }

    .modal-overlay.active {
      display: flex;
    }

    .modal-content {
      background: #fff;
      border-radius: 12px;
      padding: 24px;
      max-width: 500px;
      width: 90%;
      max-height: 90vh;
      overflow-y: auto;
    }

    .modal-content h3 {
      margin: 0 0 16px 0;
    }

    .form-group {
      margin-bottom: 16px;
    }

    .form-group label {
      display: block;
      margin-bottom: 4px;
      font-weight: 600;
    }

    .form-group input {
      width: 100%;
      padding: 8px 12px;
      border: 1px solid #e5e7eb;
      border-radius: 6px;
      font-size: 14px;
      box-sizing: border-box;
    }

    .form-actions {
      display: flex;
      gap: 8px;
      justify-content: flex-end;
      margin-top: 20px;
    }

    .post-item {
      padding: 12px;
      border-bottom: 1px solid #eef1f4;
      cursor: pointer;
    }

    .post-item:hover {
      background: #f9fafb;
    }

    .post-item:last-child {
      border-bottom: none;
    }

    .post-title {
      font-weight: 700;
      margin-bottom: 4px;
    }

    .post-meta {
      font-size: 13px;
      color: #6b7280;
    }

    .order-item {
      padding: 12px;
      border-bottom: 1px solid #eef1f4;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .order-item:last-child {
      border-bottom: none;
    }

    .order-left {
      flex: 1;
    }

    .order-title {
      font-weight: 800;
    }

    .order-meta {
      font-size: 13px;
      color: #6b7280;
      margin-top: 4px;
    }

    .recipe-card {
      cursor: pointer;
      transition: transform 0.2s;
    }

    .recipe-card:hover {
      transform: translateY(-2px);
    }

    .empty-state {
      text-align: center;
      padding: 40px 20px;
      color: #9ca3af;
    }
    
    .muted {
        color: #9ca3af;
        font-size: 12px;
      }
      /* 상태 배지 */
      .status-badge {
        display: inline-block;
        padding: 4px 8px;
        border-radius: 999px;
        font-size: 12px;
        font-weight: 700;
        margin-left: 8px;
      }
      .status-paid { background: #DCFCE7; color: #064E3B; }
      .status-cancel { background: #FEE2E2; color: #991B1B; }
      .status-default { background: #E5E7EB; color: #111; }
      </style>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <script>document.addEventListener("DOMContentLoaded", () => { initHeader("mypage"); });</script>
      <main id="mypage" class="container page">
        <div class="mpp-grid">
          <!-- 왼쪽: 프로필 카드 -->
          <aside class="card profile">
        <div class="avatar" id="userAvatar">?</div>
        <div class="name" style="font-weight:800;" id="userName">로딩중...</div>
        <div class="email muted" id="userEmail">loading...</div>
            <div class="meta" style="margin:10px 0;">
          <span class="muted">보유 포인트 <b style="color:#f39c12;" id="userPoint">0P</b></span>
          <span class="muted">작성한 게시글 <b id="postCount">0</b></span>
          <span class="muted">주문내역 <b id="orderCount">0</b></span>
            </div>

            <div class="actions" style="display:flex;gap:8px;flex-wrap:wrap;justify-content:center;margin-top:8px;">
          <button class="btn edit-profile" onclick="openEditModal()">정보수정</button>
          <button class="btn logout" onclick="handleLogout()">로그아웃</button>
            </div>
          </aside>

          <!-- 오른쪽: 콘텐츠 -->
          <section class="list-stack">
        <!-- 좋아요한 레시피 -->
            <article class="card liked">
              <div class="section-head">
                <h4>좋아요한 레시피</h4>
              </div>
          <div class="grid cols-3" id="likedRecipes">
            
              </div>
            </article>

        <!-- 내가 작성한 게시글 -->
            <article class="card posts">
              <div class="section-head">
                <h4>내가 작성한 게시글</h4>
            <a href="${path}/boards/board-write.jsp" class="muted">게시물 쓰기</a>
          </div>
          <div id="myPosts">
            <div class="empty-state">작성한 글이 없습니다.</div>
              </div>
            </article>

        <!-- 주문 내역 -->
            <article class="card orders">
              <div class="section-head">
                <h4>주문 내역</h4>
              </div>
          <div id="myOrders">
            <div class="empty-state">주문 내역이 없습니다.</div>
              </div>
            </article>
          </section>
        </div>
      </main>

  <!-- 프로필 수정 모달 (통합) -->
  <div class="modal-overlay" id="editModal">
    <div class="modal-content">
      <h3>정보 수정</h3>
      
      <div class="form-group">
        <label>닉네임</label>
        <input type="text" id="editNickname" placeholder="닉네임 입력" />
      </div>
      
      <div class="form-group">
        <label>주소</label>
        <input type="text" id="editAddress" placeholder="주소 입력" />
      </div>
      
      <hr style="margin: 20px 0; border: none; border-top: 1px solid #e5e7eb;" />
      
      <h4 style="margin: 0 0 12px 0; font-size: 16px;">비밀번호 변경 (선택)</h4>
      
      <div class="form-group">
        <label>현재 비밀번호</label>
        <input type="password" id="currentPassword" placeholder="현재 비밀번호" />
      </div>
      
      <div class="form-group">
        <label>새 비밀번호</label>
        <input type="password" id="newPassword" placeholder="새 비밀번호 (6자 이상)" />
      </div>
      
      <div class="form-group">
        <label>새 비밀번호 확인</label>
        <input type="password" id="confirmPassword" placeholder="새 비밀번호 확인" />
      </div>
      
      <div class="form-actions">
        <button class="btn" onclick="closeEditModal()">취소</button>
        <button class="btn primary" onclick="saveProfile()">저장</button>
      </div>
    </div>
  </div>

      <script>
    const BASE = CONTEXT_PATH || '';
    let currentUser = null;

    // 페이지 로드 시 데이터 가져오기
    document.addEventListener('DOMContentLoaded', async () => {
      await loadUserData();
      await loadMyPageData();
    });

    // 사용자 정보 로드
    async function loadUserData() {
      try {
        const res = await fetch(BASE + '/ajax?key=user&methodName=getMyPageData', {
          credentials: 'include'
        });
        const data = await res.json();
        
        if (data.ok) {
          currentUser = data;
          displayUserProfile(data);
        } else {
          alert('로그인이 필요합니다.');
          location.href = BASE + '/users/login.jsp';
        }
      } catch (err) {
        console.error('사용자 정보 로드 실패:', err);
      }
    }

    // 마이페이지 데이터 로드
    async function loadMyPageData() {
      try {
        // 좋아요한 레시피 ajax...로 가져오기
        body = new URLSearchParams({
            key: "mypage",
            methodName: "getLikedRecipes",
          });
        
        const response = await fetch(CONTEXT_PATH + "/ajax", {
            method: "POST",
            body,
          });

        
        
        if (response.ok == false) {
          throw new Error("서버 응답 에러: " + response.status);
        }

        const result = await response.json();
        console.log(result);
        displayLikedRecipes(result);
    	  
    	  
        const [posts, orders] = await Promise.all([
          fetch(BASE + '/ajax?key=mypage&methodName=getMyPosts&page=1&size=10', {
            credentials: 'include'
          }).then(r => r.json()),
          fetch(BASE + '/ajax?key=mypage&methodName=getMyOrders', {
            credentials: 'include'
          }).then(r => r.json())
        ]);

        if (posts.ok) displayMyPosts(posts.posts, posts.total);
        if (orders && orders.ok) displayMyOrders(orders.orders);
        
        
      } catch (err) {
        console.error('마이페이지 데이터 로드 실패:', err);
      }
    }

    // 프로필 표시
    function displayUserProfile(data) {
      const initial = (data.nickname || data.name || '?')[0].toUpperCase();
      document.getElementById('userAvatar').textContent = initial;
      document.getElementById('userName').textContent = data.nickname || data.name || '사용자';
      document.getElementById('userEmail').textContent = data.email || '';
      
      // 포인트 표시
      const pointElement = document.getElementById('userPoint');
      if (pointElement) {
        pointElement.textContent = (data.point || 0).toLocaleString();
      }
    }

    // 내가 작성한 게시글 표시
    function displayMyPosts(posts, total) {
      const container = document.getElementById('myPosts');
      document.getElementById('postCount').textContent = total || 0;
      
      if (!posts || posts.length === 0) {
        container.innerHTML = '<div class="empty-state">작성한 글이 없습니다.</div>';
        return;
      }

      container.innerHTML = posts.map(p => 
        '<div class="post-item" onclick="location.href=\'' + BASE + '/boards/board-write.jsp?postId=' + p.postId + '\'">' +
          '<div class="post-title">' + escapeHtml(p.title) + '</div>' +
          '<div class="post-meta">' +
            (p.category === 'notice' ? '[공지] ' : '') + 
            '조회 ' + p.viewCount + ' · 댓글 ' + p.commentCount + ' · 좋아요 ' + p.likeCount +
            '<span style="margin-left:8px;">' + formatDate(p.createdAt) + '</span>' +
          '</div>' +
        '</div>'
      ).join('');
    }

    // 좋아요한 레시피 표시
    function displayLikedRecipes(recipes) {
    	console.log("display: ", recipes);
      const container = document.getElementById('likedRecipes');
      
      if (!recipes || recipes.length === 0) {
        container.innerHTML = '<div class="empty-state">좋아요한 레시피가 없습니다.</div>';
        return;
      }

      let str = "";
      recipes.forEach((recipe, index) => {
    	  str += `<div class="card tile recipe-card">
    		  <a href="${path}/front?key=recipe&methodName=recipeDetail&recipeId=${"${recipe.recipeId}"}">
    	    <div class="thumb">`;
    	  
    	  if (recipe.ATT_FILE_NO_MAIN.substring(0,2) == '..') {
    	    str += `<img src="${path}/${"${recipe.ATT_FILE_NO_MAIN}"}" />`;
          } else {
            str += `<img src="${"${recipe.ATT_FILE_NO_MAIN}"}" />`;
          }
    	    
    	  str += `</div>
    	    <div class="body">
    	      <div class="title" style="font-weight:700;">${"${recipe.RCP_NM}"}</div>
    	      <div class="muted">${"${recipe.RCP_NA_TIP}"}</div>
    	    </div></a>
    	  </div>`;
      });
      container.innerHTML += str;
    }

    // 주문 내역 표시
    function displayMyOrders(orders) {
      const container = document.getElementById('myOrders');
      document.getElementById('orderCount').textContent = orders ? orders.length : 0;
      
      if (!orders || orders.length === 0) {
        container.innerHTML = '<div class="empty-state">주문 내역이 없습니다.</div>';
        return;
      }

      container.innerHTML = orders.map(o => {
    	const subtotal = Number(o.totalPrice || 0);
        const delivery = Number(o.deliveryFee || 0);
        const totalWithDelivery = subtotal + delivery;

        const rawStatus = o.status;
        let statusClass = 'status-default';
        let statusLabel = '';
        if (rawStatus === true) {
          statusClass = 'status-paid';
          statusLabel = '결제완료';
        } else if (rawStatus === false) {
          statusClass = 'status-cancel';
          statusLabel = '취소';
        } else if (typeof rawStatus === 'string' && rawStatus.trim() !== '') {
          
          const s = rawStatus.toString().toLowerCase();
          if (s.indexOf('paid') !== -1 || s.indexOf('결제') !== -1) { statusClass = 'status-paid'; statusLabel = '결제완료'; }
          else if (s.indexOf('cancel') !== -1 || s.indexOf('취소') !== -1) { statusClass = 'status-cancel'; statusLabel = '취소'; }
          else { statusLabel = rawStatus; }
        }

        if (!statusLabel) statusLabel = rawStatus ? '결제완료' : '취소';

        return '<div class="order-item">' +
          '<div class="order-left">' +
            '<div class="order-title">주문번호 ' + o.orderId + '<span class="status-badge ' + statusClass + '">' + statusLabel + '</span>' + '</div>' +
            '<div class="order-meta">' +
              '합계 ' + Number(totalWithDelivery).toLocaleString() + '원 · ' + formatDate(o.createdAt) +
            '</div>' +
          '</div>' +
          '<div class="order-right">' +
            '<a class="link" href="' + BASE + '/front?key=order&methodName=orderResult&order_id=' + o.orderId + '">상세 보기</a>' +
          '</div>' +
        '</div>';
      }).join('');
    }

    // 프로필 수정 모달 열기
    function openEditModal() {
      if (!currentUser) return;
      document.getElementById('editNickname').value = currentUser.nickname || '';
      document.getElementById('editAddress').value = currentUser.address || '';
      document.getElementById('currentPassword').value = '';
      document.getElementById('newPassword').value = '';
      document.getElementById('confirmPassword').value = '';
      document.getElementById('editModal').classList.add('active');
    }

    function closeEditModal() {
      document.getElementById('editModal').classList.remove('active');
    }

    // 프로필 저장 (통합)
    async function saveProfile() {
      const nickname = document.getElementById('editNickname').value.trim();
      const address = document.getElementById('editAddress').value.trim();
      const currentPassword = document.getElementById('currentPassword').value.trim();
      const newPassword = document.getElementById('newPassword').value.trim();
      const confirmPassword = document.getElementById('confirmPassword').value.trim();

      if (!nickname) {
        alert('닉네임을 입력하세요.');
        return;
      }

      const changePassword = currentPassword || newPassword || confirmPassword;
      if (changePassword) {
        if (!currentPassword) {
          alert('현재 비밀번호를 입력하세요.');
          return;
        }
        if (!newPassword) {
          alert('새 비밀번호를 입력하세요.');
          return;
        }
        if (newPassword.length < 6) {
          alert('새 비밀번호는 6자 이상이어야 합니다.');
          return;
        }
        if (newPassword !== confirmPassword) {
          alert('새 비밀번호가 일치하지 않습니다.');
          return;
        }
      }

      try {
        let updated = false;

        if (nickname !== currentUser.nickname) {
          const nickRes = await fetch(BASE + '/ajax?key=user&methodName=updateNickname', {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ nickname })
          });
          const nickData = await nickRes.json();
          if (!nickData.ok) {
            alert(nickData.msg);
            return;
          }
          updated = true;
        }

        if (address !== currentUser.address) {
          const addrRes = await fetch(BASE + '/ajax?key=user&methodName=updateAddress', {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ address })
          });
          const addrData = await addrRes.json();
          if (!addrData.ok) {
            alert(addrData.msg);
            return;
          }
          updated = true;
        }

        if (changePassword) {
          const pwRes = await fetch(BASE + '/ajax?key=user&methodName=updatePassword', {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ currentPassword, newPassword, confirmPassword })
          });
          const pwData = await pwRes.json();
          if (!pwData.ok) {
            alert(pwData.msg);
            return;
          }
          updated = true;
        }

        if (updated) {
          alert('정보가 수정되었습니다.');
          closeEditModal();
          location.reload();
          } else {
          alert('변경된 내용이 없습니다.');
        }
      } catch (err) {
        alert('정보 수정 중 오류가 발생했습니다.');
        console.error(err);
      }
    }

    async function handleLogout() {
      if (!confirm('로그아웃 하시겠습니까?')) return;

      try {
        await fetch(BASE + '/ajax?key=user&methodName=logout', {
          method: 'POST',
          credentials: 'include'
        });
        location.href = BASE + '/index.jsp';
      } catch (err) {
        console.error('로그아웃 실패:', err);
        location.href = BASE + '/index.jsp';
      }
    }

    function escapeHtml(text) {
      const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
      };
      return (text || '').replace(/[&<>"']/g, m => map[m]);
    }

    function formatDate(dateStr) {
      if (!dateStr) return '';
      try {
        const d = new Date(dateStr);
        return d.toLocaleDateString('ko-KR', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit'
        }).replace(/\. /g, '.').replace(' ', ' ');
            } catch (e) {
        return dateStr;
      }
    }

    document.getElementById('editModal')?.addEventListener('click', (e) => {
      if (e.target.id === 'editModal') closeEditModal();
    });
      </script>

    </body>

    </html>
