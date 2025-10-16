// KookPang Board Enhance (no header changes)
// - Hides bottom inline editor
// - Makes '+ 글쓰기' button go to board-write.html
// - Wires sort & search to render using KP_KEYS.POSTS if available

(function(){
  function exists(v){ return typeof v !== 'undefined' && v !== null; }
  function $(s){ return document.querySelector(s); }
  function $all(s){ return document.querySelectorAll(s); }

  // 1) hide inline editor (do not delete DOM)
  function hideInlineEditor(){
    var containers = document.querySelectorAll('main form, main .write, main .write-form, main .editor, main .post-form');
    containers.forEach(function(el){
      var hasTextarea = el.querySelector && el.querySelector('textarea');
      var hasSubmit   = /등록|글쓰기|작성/.test((el.textContent||''));
      if(hasTextarea && hasSubmit){
        el.style.display = 'none';
      }
    });
  }

  // 2) wire write button (+ 글쓰기)
  function wireWriteLink(){
    // id=newBtn 우선, 없으면 텍스트로 탐지
    var btn = document.getElementById('newBtn');
    if(!btn){
      $all('a,button').forEach(function(el){
        if(/\+\s*글쓰기/.test(el.textContent||'')) btn = btn || el;
      });
    }
    if(btn){
      if(btn.tagName.toLowerCase() === 'a'){
        // anchor라면 href만 보강
        if(!btn.getAttribute('href')) btn.setAttribute('href',CONTEXT_PATH + '/boards/board-write.jsp');
      }else{
        // button이면 클릭시 이동
        btn.addEventListener('click', function(e){
          e.preventDefault();
          location.href = CONTEXT_PATH + '/boards/board-write.jsp';
        });
      }
    }
  }

  // 3) render list using KP store if present
  function escapeHtml(s){ return String(s).replace(/[&<>"']/g, function(c){ return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]); }); }
  function fmtDate(ts){ var d=new Date(ts);var y=d.getFullYear(),m=('0'+(d.getMonth()+1)).slice(-2),da=('0'+d.getDate()).slice(-2);return y+'. '+m+'. '+da+'.'; }

  function attachListController(){
    var listWrap = document.getElementById('posts') || document.querySelector('.posts, #postList');
    if(!listWrap) return;

    var searchEl = document.getElementById('search');
    var sortSel  = document.getElementById('sortSelect');
    var useStore = (exists(window.KP_KEYS) && exists(window.S) && typeof S.get === 'function');
    if(!useStore) return; // 앱이 자체 렌더를 하면 간섭하지 않음

    var posts = [];
    try { posts = S.get(KP_KEYS.POSTS, []); } catch(e){ posts = []; }

    function render(){
      var q = (searchEl? searchEl.value : '').trim().toLowerCase();
      var list = posts.slice();
      var sort = (sortSel && sortSel.value) || 'newest';

      if(q){
        list = list.filter(function(p){
          return String(p.title||'').toLowerCase().includes(q) ||
                 String(p.body||'').toLowerCase().includes(q) ||
                 String(p.nickname||'').toLowerCase().includes(q);
        });
      }
      if(sort==='newest') list.sort(function(a,b){return b.created-a.created;});
      else if(sort==='oldest') list.sort(function(a,b){return a.created-b.created;});
      else if(sort==='likes') list.sort(function(a,b){return (b.likes||0)-(a.likes||0);});

      listWrap.innerHTML = '';
      list.forEach(function(p){
        var el = document.createElement('article');
        el.className = 'card';
        el.style.padding = '16px';
        el.innerHTML = '<div style="display:flex;justify-content:space-between;align-items:center">'
          + '<h3 style="margin:0">'+escapeHtml(p.title)+'</h3>'
          + '<span class="small">'+fmtDate(p.created)+'</span>'
          + '</div>'
          + '<p class="small" style="margin:6px 0 8px">👤 '+escapeHtml(p.nickname||'익명')+'</p>'
          + (p.image? '<img src="'+p.image+'" style="max-width:100%;border-radius:10px;margin:8px 0;">':'' )
          + '<p>'+escapeHtml(p.body).replace(/\n/g,'<br>')+'</p>'
          + '<div class="meta" style="justify-content:flex-end"><span>❤ '+(p.likes||0)+'</span><span>💬 '+(p.comments||0)+'</span></div>';
        listWrap.appendChild(el);
      });
    }

    if(sortSel) sortSel.addEventListener('change', render);
    if(searchEl) searchEl.addEventListener('input', render);
    render();
  }

  document.addEventListener('DOMContentLoaded', function(){
    hideInlineEditor();
    wireWriteLink();
    attachListController();
  });
})();
