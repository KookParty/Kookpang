<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="ko">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1">
  <title>게시글 - 자유게시판</title>
  <link rel="stylesheet" href="${path}/css/styles.css">
  <!-- 문자열 누락 버그 수정 -->
  <script type="text/javascript">const CONTEXT_PATH = "${path}";</script>
  <script>const BASE="${pageContext.request.contextPath}";const PAGE_ACTIVE="board";</script>
  <script src="${path}/js/config.js"></script>
  <script src="${path}/js/app.js"></script>
  <script src="${path}/js/seed.js"></script>
  <style>
    .editor-wrap{background:#fff;border:1px solid #e5e7eb;border-radius:12px;overflow:hidden}
    .editor-toolbar{display:flex;gap:6px;align-items:center;padding:8px 10px;border-bottom:1px solid #eee;background:#fafafa}
    .tool-btn{border:1px solid #e5e7eb;background:#fff;border-radius:8px;padding:6px 10px;cursor:pointer;font-size:13px}
    .tool-btn:active{transform:translateY(1px)}
    .editor-area{min-height:280px;padding:14px;outline:none}
    .editor-area:empty:before{content:'내용을 입력하세요';color:#9ca3af}
    .form-row{margin:12px 0}
    .input{width:100%;padding:12px 14px;border:1px solid #e5e7eb;border-radius:12px}
    .file-row label{display:inline-block;margin-right:8px}
    .preview{max-width:100%;border-radius:12px;margin-top:10px;display:none}
    .meta{color:#6b7280;font-size:14px;display:flex;gap:10px}
    .view-title{font-size:22px;font-weight:700;margin:0 0 8px}
    .view-body{white-space:normal;line-height:1.6}
    .img-list img{max-width:100%;border-radius:12px;display:block;margin:10px 0}
    .cmt-wrap{margin-top:18px}
    .cmt-wrap ul{list-style:none;padding:0;margin:0}
    .cmt-wrap li{border-top:1px solid #eee;padding:10px 0}
    .cmt-head{display:flex;gap:8px;color:#6b7280;font-size:14px}
    .cmt-del{border:1px solid #e5e7eb;background:#fff;border-radius:8px;padding:4px 8px;cursor:pointer}
  </style>
</head>
<body>
  <jsp:include page="../common/header.jsp"></jsp:include>
  <script>document.addEventListener('DOMContentLoaded',()=>{ if(typeof initHeader==='function') initHeader('board'); });</script>

  <main class="container page" style="max-width:860px">
    <!-- 보기 -->
    <article id="kp-view" class="card" style="padding:16px; display:none">
      <h2 class="view-title" data-title></h2>
      <div class="meta">
        <span data-nick>익명</span>
        <span data-date></span>
        <span>조회 <b data-views>0</b></span>
        <span>댓글 <b data-cmts>0</b></span>
      </div>
      <div class="img-list" data-images></div>
      <!-- HTML 내용은 그대로 보여줘야 하므로 escape 제거 -->
      <div class="view-body" data-content style="margin-top:10px"></div>

      <div style="display:flex;justify-content:flex-end;gap:8px;margin-top:14px">
        <a class="btn" href="${path}/front?key=board&methodName=list">목록</a>
        <button class="btn" id="kp-edit">수정</button>
        <button class="btn dark" id="kp-delete">삭제</button>
      </div>

      <section class="cmt-wrap">
        <h3 style="margin:16px 0 10px">댓글</h3>
        <ul id="kp-cmt-list"></ul>
        <form id="kp-cmt-form" class="form-row" style="display:flex;gap:8px">
          <input class="input" name="content" placeholder="댓글을 입력하세요(로그인 필요)" maxlength="200" />
          <button class="btn dark" type="submit">등록</button>
        </form>
      </section>
    </article>

    <!-- 작성/수정 -->
    <article id="kp-write-form" class="card" style="padding:16px; display:none">
      <form>
        <input type="hidden" name="postId" id="postId" />
        <div class="form-row">
          <select name="category" class="input" style="width:auto">
            <option value="free" selected>자유</option>
            <option value="notice">공지</option>
          </select>
        </div>
        <div class="form-row"><input id="titleInput" name="title" class="input" placeholder="제목(*)"></div>

        <div class="editor-wrap">
          <div class="editor-toolbar">
            <button type="button" class="tool-btn" data-cmd="bold">굵게</button>
            <button type="button" class="tool-btn" data-cmd="italic">기울임</button>
            <button type="button" class="tool-btn" data-cmd="underline">밑줄</button>
            <button type="button" class="tool-btn" data-cmd="insertUnorderedList">• 목록</button>
            <button type="button" class="tool-btn" data-cmd="insertOrderedList">1. 목록</button>
          </div>
          <div id="editor" class="editor-area" contenteditable="true"></div>
        </div>

        <div class="form-row file-row">
          <label>파일1 <input type="file" id="file1" accept="image/*"></label>
          <label>파일2 <input type="file" id="file2" accept="image/*"></label>
          <label>파일3 <input type="file" id="file3" accept="image/*"></label>
          <img id="preview" class="preview">
        </div>

        <div class="form-row">
          <div style="display:flex;justify-content:space-between;align-items:center">
            <b>이미지 URL</b>
            <button id="kp-add-image" class="btn" style="padding:6px 10px">추가</button>
          </div>
          <div id="kp-image-box" style="margin-top:8px"></div>
        </div>

        <div style="display:flex;justify-content:flex-end;gap:8px">
          <a class="btn" href="${path}/front?key=board&methodName=list">취소</a>
          <button class="btn dark" id="saveBtn" type="submit">확인</button>
        </div>
      </form>
    </article>
  </main>

  <jsp:include page="../common/footer.jsp"></jsp:include>

<script>
if(!window.__KP_BOARD_INIT__){ window.__KP_BOARD_INIT__=true;
(function(){
  var qs=function(s){return document.querySelector(s);};
  var qsa=function(s){return document.querySelectorAll(s);};
  var URLQ=new URLSearchParams(location.search);
  var postId=Number(URLQ.get("postId")||0);

  qsa(".tool-btn").forEach(function(b){ b.addEventListener("click",function(){ document.execCommand(b.getAttribute("data-cmd"),false,null); }); });

  ["file1","file2","file3"].forEach(function(id){
    var input=document.getElementById(id);
    input&&input.addEventListener("change",function(e){
      var f=e.target.files&&e.target.files[0]; if(!f)return;
      var r=new FileReader();
      r.onload=function(ev){ var img=qs("#preview"); img.src=ev.target.result; img.style.display="block"; };
      r.readAsDataURL(f);
    });
  });

  var esc=function(s){return (s||"").replace(/[&<>\"']/g,function(m){return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"}[m];});};
  var fmt=function(iso){return (iso||"").replace("T"," ").slice(0,16);};

  function addImageInput(val){
    var box=qs("#kp-image-box");
    var inp=document.createElement("input");
    inp.type="text"; inp.placeholder="https://example.com/image.jpg";
    inp.className="input kp-image-url"; inp.style.marginTop="6px"; inp.value=val||"";
    box.appendChild(inp);
  }
  qs("#kp-add-image")&&qs("#kp-add-image").addEventListener("click",function(e){e.preventDefault();addImageInput("");});

  var $formWrap=qs("#kp-write-form");
  var $viewWrap=qs("#kp-view");

  document.addEventListener("DOMContentLoaded",init);

  async function init(){
    if(!postId){ $formWrap.style.display="block"; $viewWrap.style.display="none"; return; }
    await loadPost(postId);
    $formWrap.style.display="none"; $viewWrap.style.display="block";
  }

  async function loadPost(id){
    var res=await fetch(BASE+"/ajax?key=board&methodName=postData&postId="+id);
    var j=await res.json();
    if(!j.ok||!j.post){ alert("게시글을 불러오지 못했습니다."); return; }
    var p=j.post;

    qs("[data-title]").textContent=p.title;
    qs("[data-nick]").textContent=p.nickname||"익명";
    qs("[data-date]").textContent=fmt(p.createdAt);
    qs("[data-views]").textContent=p.viewCount;
    qs("[data-cmts]").textContent=p.commentCount;

    // 보기 페이지에서는 HTML 그대로 그려주기 (XSS 방어는 서버/에디터에서 별도 처리 권장)
    qs("[data-content]").innerHTML = p.content || "";

    var imgBox=qs("[data-images]");
    imgBox.innerHTML=(p.images||[]).map(function(im){return '<img src="'+esc(im.imageUrl)+'" alt="">' }).join("");

    renderComments(p.comments||[]);

    qs("#kp-edit")&&qs("#kp-edit").addEventListener("click",function(){
      $formWrap.style.display="block"; $viewWrap.style.display="none";
      qs("#postId").value=p.postId;
      qs("select[name=category]").value=p.category||"free";
      qs("#titleInput").value=p.title;
      qs("#editor").innerHTML=p.content||"";
      qs("#kp-image-box").innerHTML="";
      (p.images||[]).forEach(function(im){ addImageInput(im.imageUrl||""); });
    });

    qs("#kp-delete")&&qs("#kp-delete").addEventListener("click",async function(){
      if(!confirm("삭제하시겠습니까?")) return;
      var fd=new FormData(); fd.append("postId",p.postId);
      var r=await fetch(BASE+"/ajax?key=board&methodName=delete",{method:"POST",body:fd});
      var jj=await r.json();
      if(jj.ok) location.href=BASE+"/front?key=board&methodName=list";
      else alert(jj.error==="login-required"?"로그인이 필요합니다.":"삭제 권한이 없습니다.");
    });
  }

  function renderComments(cs){
    var $list=qs("#kp-cmt-list");
    var html="";
    for(var i=0;i<cs.length;i++){
      var c=cs[i];
      html+=
        '<li>'+
          '<div class="cmt-head"><b>'+esc(c.nickname||"익명")+'</b><span>'+fmt(c.createdAt)+'</span></div>'+
          '<div style="margin:4px 0 8px">'+esc(c.content)+'</div>'+
          '<button class="cmt-del" data-cid="'+c.commentId+'">삭제</button>'+
        '</li>';
    }
    $list.innerHTML=html;

    $list.querySelectorAll(".cmt-del").forEach(function(btn){
      btn.addEventListener("click",async function(){
        if(!confirm("댓글을 삭제할까요?")) return;
        var fd=new FormData();
        fd.append("commentId",btn.getAttribute("data-cid"));
        fd.append("postId",postId);
        var r=await fetch(BASE+"/ajax?key=board&methodName=delComment",{method:"POST",body:fd});
        var jj=await r.json();
        if(jj.ok) renderComments(jj.comments);
        else alert(jj.error==="login-required"?"로그인이 필요합니다.":"삭제 권한이 없습니다.");
      });
    });
  }

  // ✅ 글쓰기/수정: URLSearchParams로 모든 필드 전송 (서버는 getParameter로 안전하게 수신)
  qs("#kp-write-form form")&&qs("#kp-write-form form").addEventListener("submit",async function(e){
    e.preventDefault();
    var title=(qs("#titleInput").value||"").trim();
    var content=(qs("#editor").innerHTML||"").trim();
    if(!title||!content){ alert("제목과 내용을 입력하세요."); return; }

    var params = new URLSearchParams();
    params.set("title", title);
    params.set("content", content);
    params.set("contentText", (qs('#editor').innerText || '').replace(/\u00A0/g,' ').trim());
    params.set("category", qs('select[name="category"]')?.value || "free");

    var pid = (qs("#postId")?.value || "").trim();
    if (pid) params.set("postId", pid);

    document.querySelectorAll(".kp-image-url").forEach(function(inp){
      var v=(inp.value||"").trim(); if(v) params.append("imageUrl", v);
    });

    var r=await fetch(BASE+"/ajax?key=board&methodName=save",{
      method:"POST",
      headers:{ "Content-Type":"application/x-www-form-urlencoded;charset=UTF-8" },
      body: params
    });
    var j=await r.json();
    if(j.ok){ location.href=BASE+"/front?key=board&methodName=view&postId="+j.postId; }
    else alert(j.error==="login-required"?"로그인이 필요합니다.":"저장 실패");
  });

})();
}
</script>

</body>
</html>
