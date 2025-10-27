<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="ko">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1">
  <title>자유게시판</title>
  <link rel="stylesheet" href="${path}/css/styles.css">
  <script>const BASE="${pageContext.request.contextPath}";const PAGE_ACTIVE="board";</script>
  <script src="${path}/js/config.js"></script>
  <script src="${path}/js/app.js"></script>
  <style>
    /* pinned notices stay visible while scrolling on desktop */
    #kp-board-pinned { position: sticky; top:72px; z-index:1200; }
    #kp-board-pinned .card.pinned { margin-bottom:8px; padding:12px; border-radius:8px; max-width:720px; border:2px solid #f5c542; background:#fffbe6 }
    #kp-board-pinned .card.pinned h3{ font-size:18px; margin:0 }
    #kp-board-pinned .card.pinned .small{ font-size:13px }
    #kp-board-pinned .kp-pinned-header{ display:flex; justify-content:space-between; align-items:center; gap:8px; margin-bottom:8px }
    /* small screens: disable sticky to avoid layout issues */
    @media (max-width: 720px){ #kp-board-pinned { position: static; } }
  </style>
</head>
<body>
  <jsp:include page="../common/header.jsp"></jsp:include>
  <script>document.addEventListener('DOMContentLoaded',()=>{ if(typeof initHeader==='function') initHeader('board'); });</script>

  <main class="container page">
    <h2>자유게시판</h2>
    <p class="small">요리 팁과 경험을 나누어보세요</p>

    <div class="form-row">
      <input class="input" id="kp-board-search" placeholder="게시글을 검색하세요...">
    </div>

    <div style="display:flex;justify-content:flex-end;position:relative">
      <div class="kp-sort">
        <button class="btn" id="kp-sort-btn">최신순 ▾</button>
        <div id="kp-sort-menu" style="display:none;position:absolute;right:0;top:36px;background:#fff;border:1px solid #ddd;padding:6px;box-shadow:0 2px 6px rgba(0,0,0,.08)">
          <button class="btn" data-sort="latest">최신순</button>
          <button class="btn" data-sort="views">조회순</button>
          <button class="btn" data-sort="likes">좋아요순</button>
        </div>
      </div>
      <!-- a태그로 fallback 제공 -->
      <a class="btn dark" id="kp-board-write" href="${path}/front?key=board&methodName=writeForm" style="margin-left:8px">+ 글쓰기</a>
    </div>

  <section id="kp-board-pinned" class="grid" style="margin-top:12px;display:none"></section>
  <section id="kp-board-list" class="grid" style="margin-top:16px"></section>
  </main>

  <jsp:include page="../common/footer.jsp"></jsp:include>

  <script>
  if(!window.__KP_BOARD_LIST_INIT__){ window.__KP_BOARD_LIST_INIT__=true;
  (function(){
  var page=1, size=10;
  var params=new URLSearchParams(location.search);
  var category=params.get("category")||"";

  var $pinned=document.querySelector("#kp-board-pinned");
  var $list=document.querySelector("#kp-board-list");
  var $search=document.querySelector("#kp-board-search");
  var $writeBtn=document.querySelector("#kp-board-write")||document.querySelector("#newBtn");

    function escapeHtml(s){return (s||"").replace(/[&<>\"']/g,function(m){return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"}[m];});}
    function fmtDate(iso){return (iso||"").replace("T"," ").slice(0,16);}

  var isLoading = false;
  var reachedEnd = false;
  var sort = (new URLSearchParams(location.search)).get('sort') || 'latest';
  var $sortBtn = document.querySelector('#kp-sort-btn');
  var $sortMenu = document.querySelector('#kp-sort-menu');
  function setSortLabel(s){ if(s==='views') $sortBtn.textContent='조회수 ▾'; else $sortBtn.textContent='최신순 ▾'; }
  setSortLabel(sort);
  // sort menu toggle
  $sortBtn && $sortBtn.addEventListener('click', function(e){ e.stopPropagation(); $sortMenu.style.display = $sortMenu.style.display === 'none' ? 'block' : 'none'; });
  document.addEventListener('click', function(e){ if(e.target && e.target.closest && e.target.closest('#kp-sort-menu')) return; $sortMenu.style.display='none'; });
  $sortMenu && $sortMenu.addEventListener('click', function(e){ var b = e.target.closest('button[data-sort]'); if(!b) return; sort = b.getAttribute('data-sort'); setSortLabel(sort); page=1; reachedEnd=false; $list.innerHTML=''; if($pinned) $pinned.innerHTML=''; var params=new URLSearchParams(location.search); params.set('sort', sort); history.replaceState(null,'',location.pathname + '?' + params.toString()); fetchList(); });

     

      function renderPinned(rows){
        if(!$pinned) return;
        if(!rows||!rows.length){ $pinned.innerHTML=''; $pinned.style.display='none'; return; }
        var html='';
        html += '<div class="kp-pinned-header"><div style="font-weight:600">공지</div></div>';
        for(var i=0;i<rows.length;i++){
          var r=rows[i];
          html+=
            '<article class="card pinned" style="border:2px solid #f5c542;background:#fffbe6">'+
              '<a class="kp-post-link" href="'+BASE+'/front?key=board&methodName=view&postId='+r.postId+'" style="text-decoration:none;color:inherit">'+
                '<div style="display:flex;justify-content:space-between;align-items:center">'+
                  '<h3 style="margin:0">'+escapeHtml(r.title)+'</h3>'+
                  '<span class="small">'+fmtDate(r.createdAt)+'</span>'+
                '</div>'+
              '</a>'+
            '</article>';
        }
        $pinned.innerHTML = html;
        $pinned.style.display = 'block';
      }

      function renderAppend(rows){
      if(!rows||!rows.length){
        if(page===1) $list.innerHTML="<p>게시글이 없습니다.</p>";
        reachedEnd = true;
        return;
      }
      var html = '';
      for(var i=0;i<rows.length;i++){
        var r=rows[i];
        html+=
          '<article class="card" style="padding:16px">'+
            '<a class="kp-post-link" href="'+BASE+'/front?key=board&methodName=view&postId='+r.postId+'" style="text-decoration:none;color:inherit">'+
              '<div style="display:flex;justify-content:space-between;align-items:center">'+
                '<h3 style="margin:0">'+escapeHtml(r.title)+'</h3>'+
                '<span class="small">'+fmtDate(r.createdAt)+'</span>'+
              '</div>'+
              '<p class="small" style="margin:6px 0 8px">👤 '+escapeHtml(r.nickname||"익명")+'</p>'+
              '<p>'+escapeHtml((r.content||"").slice(0,140))+'</p>'+
              '<div class="meta" style="justify-content:flex-end">'+
                '<span>조회수 '+r.viewCount+'</span>'+
                '<span>💬 '+r.commentCount+'</span>'+
              '</div>'+
            '</a>'+
          '</article>';
      }
      if(page===1) $list.innerHTML = html; else $list.insertAdjacentHTML('beforeend', html);
    }

    async function fetchList(){
      if(isLoading || reachedEnd) return;
      isLoading = true;
      // 간단 로딩 인디케이터
      var loader = document.getElementById('kp-loader');
      if(!loader){ loader = document.createElement('div'); loader.id='kp-loader'; loader.textContent='로딩 중...'; loader.style.textAlign='center'; loader.style.padding='12px'; $list.parentNode.appendChild(loader); }
      loader.style.display='block';

      var q=($search.value||"").trim();
  var url=BASE+"/ajax?key=board&methodName=listData&category="+encodeURIComponent(category)+"&q="+encodeURIComponent(q)+"&page="+page+"&size="+size+"&sort="+encodeURIComponent(sort);
      try{
        var res=await fetch(url,{headers:{'Accept':'application/json'}});
  var j=await res.json();
  console.log('AJAX listData response:', j);
        if(j.ok){
          // If server provided pinned, render it at top and remove duplicates from rows
          if(page===1 && j.pinned && Array.isArray(j.pinned) && j.pinned.length){
            renderPinned(j.pinned);
            // remove pinned IDs from j.rows to avoid duplication
            var pinnedIds = new Set(j.pinned.map(function(p){ return p.postId; }));
            if(Array.isArray(j.rows) && j.rows.length){
              j.rows = j.rows.filter(function(r){ return !pinnedIds.has(r.postId); });
            }
          }
          // fallback: if server did not send pinned but there are 'notice' rows in j.rows on first page, extract them
          else if(page===1 && Array.isArray(j.rows)){
            var extracted = [];
            for(var i = j.rows.length - 1; i >= 0; i--){
              if(j.rows[i] && j.rows[i].category === 'notice'){
                extracted.unshift(j.rows.splice(i,1)[0]);
              }
            }
            if(extracted.length) renderPinned(extracted);
          }
          renderAppend(j.rows);
          // 결과가 size보다 작으면 끝 도달
          if(!j.rows || j.rows.length < size) reachedEnd = true;
          else page++;
        } else {
          if(page===1) $list.innerHTML="<p>게시글을 불러오지 못했습니다.</p>";
          reachedEnd = true;
        }
      }catch(e){
        console.error(e);
        if(page===1) $list.innerHTML="<p>게시글을 불러오지 못했습니다.</p>";
        reachedEnd = true;
      } finally {
        isLoading = false;
        if(loader) loader.style.display = reachedEnd ? 'none' : 'block';
      }
    }

    function debounce(fn,ms){var t;return function(){var a=arguments;clearTimeout(t);t=setTimeout(function(){fn.apply(null,a);},ms);};}
  $search.addEventListener("input",debounce(function(){ page=1; reachedEnd=false; if($pinned) $pinned.innerHTML=''; fetchList(); },250));
    document.addEventListener("DOMContentLoaded",fetchList);

    // infinite scroll: near bottom -> fetch next page
    window.addEventListener('scroll', debounce(function(){
      if(reachedEnd || isLoading) return;
      var scTop = window.scrollY || window.pageYOffset;
      var vh = window.innerHeight;
      var docH = document.documentElement.scrollHeight;
      if(scTop + vh + 200 >= docH){ // 200px 전부터 로드
        fetchList();
      }
    }, 150));

    // a태그라 JS 없어도 이동되지만, 버튼과 호환되게 보강
    $writeBtn&&$writeBtn.addEventListener("click",function(e){
      if($writeBtn.tagName==="A") return;
      e.preventDefault();
      location.href=BASE+"/front?key=board&methodName=writeForm";
    });

    // (공지 롤백) 숨기기/복원 기능 제거 - 관련 로컬스토리지 플래그 무시됨.
  })();
  }
  </script>
</body>
</html>
