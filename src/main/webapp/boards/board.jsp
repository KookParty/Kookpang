<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    <div style="display:flex;justify-content:flex-end">
      <button class="btn">최신순 ▾</button>
      <!-- a태그로 fallback 제공 -->
      <a class="btn dark" id="kp-board-write" href="${path}/front?key=board&methodName=writeForm" style="margin-left:8px">+ 글쓰기</a>
    </div>

    <section id="kp-board-list" class="grid" style="margin-top:16px"></section>
  </main>

  <jsp:include page="../common/footer.jsp"></jsp:include>

  <script>
  if(!window.__KP_BOARD_LIST_INIT__){ window.__KP_BOARD_LIST_INIT__=true;
  (function(){
    var page=1, size=10;
    var params=new URLSearchParams(location.search);
    var category=params.get("category")||"";

    var $list=document.querySelector("#kp-board-list");
    var $search=document.querySelector("#kp-board-search");
    var $writeBtn=document.querySelector("#kp-board-write")||document.querySelector("#newBtn");

    function escapeHtml(s){return (s||"").replace(/[&<>\"']/g,function(m){return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"}[m];});}
    function fmtDate(iso){return (iso||"").replace("T"," ").slice(0,16);}

    function render(rows){
      if(!rows||!rows.length){ $list.innerHTML="<p>게시글이 없습니다.</p>"; return; }
      var html="";
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
                '<span>❤ '+r.viewCount+'</span>'+
                '<span>💬 '+r.commentCount+'</span>'+
              '</div>'+
            '</a>'+
          '</article>';
      }
      $list.innerHTML=html;
    }

    async function fetchList(){
      var q=($search.value||"").trim();
      var url=BASE+"/ajax?key=board&methodName=listData&category="+encodeURIComponent(category)+"&q="+encodeURIComponent(q)+"&page="+page+"&size="+size;
      var res=await fetch(url,{headers:{'Accept':'application/json'}});
      var j=await res.json();
      if(j.ok) render(j.rows); else $list.innerHTML="<p>게시글을 불러오지 못했습니다.</p>";
    }

    function debounce(fn,ms){var t;return function(){var a=arguments;clearTimeout(t);t=setTimeout(function(){fn.apply(null,a);},ms);};}
    $search.addEventListener("input",debounce(fetchList,250));
    document.addEventListener("DOMContentLoaded",fetchList);

    // a태그라 JS 없어도 이동되지만, 버튼과 호환되게 보강
    $writeBtn&&$writeBtn.addEventListener("click",function(e){
      if($writeBtn.tagName==="A") return;
      e.preventDefault();
      location.href=BASE+"/front?key=board&methodName=writeForm";
    });
  })();
  }
  </script>
</body>
</html>
