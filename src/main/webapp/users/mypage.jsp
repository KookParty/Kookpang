<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

        #mypage .section .section-head {
          padding: 10px 14px;
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
            <div class="avatar">${user.nickname.substring(0,1).toUpperCase()}</div>
            <div class="name" style="font-weight:800;" id="mypageNickname">${user.nickname}</div>
            <div class="email muted">${user.email}</div>
            <div class="meta" style="margin:10px 0;">
              <span class="muted">작성한 게시글 <b>4</b></span>
              <span class="muted">주문내역 <b>12</b></span>
            </div>

            <div class="actions" style="display:flex;gap:8px;flex-wrap:wrap;justify-content:center;margin-top:8px;">
              <button class="btn edit-profile">정보수정</button>
              <button class="btn logout">로그아웃</button>
              <button class="btn danger delete-account">회원탈퇴</button>
            </div>
          </aside>

          <!-- 오른쪽: 콘텐츠 -->
          <section class="list-stack">
            <article class="card liked">
              <div class="section-head">
                <h4>좋아요한 레시피</h4>
                <a href="#" class="muted"></a>
              </div>
              <div class="grid cols-3">
                <div class="card tile">
                  <div class="thumb">
                    <img src="https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=800&auto=format&fit=crop"
                      alt="" />
                  </div>
                  <div class="body">
                    <div class="title" style="font-weight:700;">김치찌개</div>
                    <div class="muted">⏱ 30분 · 👥 4인분 · ♥ 1247</div>
                  </div>
                </div>
                <div class="card tile">
                  <div class="thumb">
                    <img
                      src="https://images.unsplash.com/photo-1604908553729-01ba09a7e7be?q=80&w=800&auto=format&fit=crop"
                      alt="" />
                  </div>
                  <div class="body">
                    <div class="title" style="font-weight:700;">불고기</div>
                    <div class="muted">⏱ 45분 · 👥 3인분 · ♥ 892</div>
                  </div>
                </div>
                <div class="card tile">
                  <div class="thumb">
                    <img
                      src="https://images.unsplash.com/photo-1625944528108-4ab1ab696648?q=80&w=800&auto=format&fit=crop"
                      alt="" />
                  </div>
                  <div class="body">
                    <div class="title" style="font-weight:700;">비빔밥</div>
                    <div class="muted">⏱ 25분 · 👥 2인분 · ♥ 1712</div>
                  </div>
                </div>
              </div>
            </article>

            <article class="card posts">
              <div class="section-head">
                <h4>내가 작성한 게시글</h4>
                <a href="#" class="muted">게시물 쓰기</a>
              </div>
              <div class="empty muted">작성한 글이 없습니다.</div>
            </article>

            <article class="card orders">
              <div class="section-head">
                <h4>주문 내역</h4>
              </div>
              <div class="list">
                <!-- 주문 아이템 예시 -->
                <div class="item">
                  <div class="left">
                    <div style="font-weight:800;">주문번호 1759595615292</div>
                    <div class="meta">합계 24,000원 · 2025. 10. 5 13:05</div>
                  </div>
                  <div class="right">
                    <a class="link" href="${path}/orders/order-detail.jsp">상세 보기</a>
                  </div>
                </div>
                <div class="item">
                  <div class="left">
                    <div style="font-weight:800;">주문번호 1759620398828</div>
                    <div class="meta">합계 11,000원 · 2025. 10. 5 11:03</div>
                  </div>
                  <div class="right">
                    <a class="link" href="${path}/orders/order-detail.jsp">상세 보기</a>
                  </div>
                </div>
              </div>
            </article>
          </section>
        </div>
      </main>

      <script src="../js/mypage.js"></script>
      
      <!-- 마이페이지 서버 연동 기능 -->
      <script>
        // 로그아웃 기능
        document.querySelector('.btn.logout').addEventListener('click', function() {
          if (confirm('로그아웃 하시겠습니까?')) {
            fetch(CONTEXT_PATH + '/ajax?key=user&methodName=logout', {
              method: 'POST'
            })
            .then(response => response.json())
            .then(data => {
              alert('로그아웃 되었습니다.');
              window.location.href = CONTEXT_PATH + '/users/login.jsp';
            })
            .catch(error => {
              console.error('Logout error:', error);
              // 세션 만료 등으로 서버 오류가 있어도 로그인 페이지로 이동
              window.location.href = CONTEXT_PATH + '/users/login.jsp';
            });
          }
        });

        // 회원탈퇴 기능
        document.querySelector('.btn.danger.delete-account').addEventListener('click', function() {
          if (confirm('정말로 회원탈퇴를 하시겠습니까?\n탈퇴 후 복구할 수 없습니다.')) {
            const password = prompt('비밀번호를 입력해주세요:');
            if (password) {
              fetch(CONTEXT_PATH + '/ajax?key=user&methodName=deleteAccount', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                  password: password
                })
              })
              .then(response => response.json())
              .then(data => {
                alert(data.msg);
                if (data.ok) {
                  window.location.href = CONTEXT_PATH + '/index.jsp';
                }
              })
              .catch(error => {
                console.error('Delete account error:', error);
                alert('회원탈퇴 중 오류가 발생했습니다.');
              });
            }
          }
        });
      </script>
      
      <script>
        (function () {
          function getUserKey() {
            return ['user', 'currentUser', 'authUser'].find(k => localStorage.getItem(k)) || 'user';
          }
          function readUser() {
            try {
              const k = getUserKey();
              const u = JSON.parse(localStorage.getItem(k) || '{}'); u.__key = k;
              return u;
            } catch (e) {
              return { __key: getUserKey() };
            }
          }
          const u = readUser();
          const loginLink = document.querySelector('.login-link');
          const logoutBtn = document.querySelector('.logout-link');
          const navNick = document.getElementById('navNickname');
          if (u && (u.nickname || u.nick)) {
            loginLink?.classList.add('hidden');
            logoutBtn?.classList.remove('hidden');
            navNick && (navNick.textContent = u.nickname || u.nick);
          } else {
            loginLink?.classList.remove('hidden');
            logoutBtn?.classList.add('hidden');
            navNick && (navNick.textContent = '마이페이지');
          }
          logoutBtn?.addEventListener('click', function () {
            const k = u.__key;
            try {
              localStorage.removeItem(k);
            } catch (e) {

            }
            location.href = '${path}/index.jsp';
          });
          const path = (location.pathname.split('/').pop() || '${path}/index.jsp').toLowerCase();
          document.querySelectorAll('.kp-header .menu a').forEach(a => {
            const href = (a.getAttribute('href') || '').toLowerCase();
            if (href === path) {
              a.style.color = '#0b0f1a';
              a.style.fontWeight = '800';
            }
          });
        })();
      </script>

    </body>

    </html>