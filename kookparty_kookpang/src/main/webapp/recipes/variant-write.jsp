<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!doctype html>
    <html lang="ko">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width,initial-scale=1">
      <title>변형 레시피 작성</title>
      <link rel="stylesheet" href="${path}/css/styles.css">
      <script type="text/javascript">
        const CONTEXT_PATH = "${pageContext.request.contextPath}";
      </script>
      <script src="${path}/js/config.js"></script>
      <script src="${path}/js/app.js"></script>
      <script src="${path}/js/seed.js"></script>
    </head>

    <body>
      <!-- header시작 -->
      <jsp:include page="../common/header.jsp"></jsp:include>
      <!-- header끝 -->
      <script>document.addEventListener('DOMContentLoaded', () => { initHeader('recipes'); requireAuth(); });</script>

      <main class="container page">
        <h2 style="margin:0 0 6px">변형 레시피 작성</h2>
        <p class="small">“<span id="fromTitle">원본</span>” 레시피를 바탕으로 나만의 변형 레시피를 만들어보세요.</p>

        <section class="card" style="padding:16px">
          <h3 style="margin:0 0 12px">기본 정보</h3>
          <label class="small">레시피 이름</label>
          <div class="form-row"><input id="name" class="input" placeholder="변형 레시피 이름을 입력해주세요"></div>

          <label class="small">설명</label>
          <div class="form-row"><textarea id="desc" class="input" rows="3" placeholder="레시피에 대한 설명을 입력해주세요"></textarea>
          </div>

          <label class="small">썸네일 이미지 URL</label>
          <div class="row">
            <input id="thumb" class="input" placeholder="이미지 URL을 입력해주세요" style="flex:1">
            <button class="btn">업로드</button>
          </div>

          <div class="row">
            <div style="flex:1">
              <label class="small">난이도</label>
              <select id="diff" class="input">
                <option>쉬움</option>
                <option>보통</option>
                <option>어려움</option>
              </select>
            </div>
            <div style="flex:1">
              <label class="small">조리시간 (분)</label>
              <input id="time" class="input" placeholder="조리시간을 입력해주세요">
            </div>
          </div>
        </section>

        <section class="card" style="padding:16px;margin-top:16px">
          <h3 style="margin:0 0 12px">재료</h3>
          <label class="small">재료 검색 및 추가</label>
          <div class="row">
            <input id="ingInput" class="input" placeholder="재료를 검색해보세요" style="flex:1">
            <button class="btn" id="addIng">추가</button>
          </div>
          <ul id="ingList" class="small" style="margin:10px 0 0; padding-left:18px"></ul>
        </section>

        <section class="card" style="padding:16px;margin-top:16px">
          <h3 style="margin:0 0 12px">조리법</h3>
          <div id="steps"></div>
          <button class="btn" id="addStep" style="margin-top:8px">+ 단계 추가</button>
        </section>

        <div style="display:flex;gap:8px;justify-content:flex-end;margin-top:16px">
          <button class="btn dark" id="submit">변형 레시피 등록</button>
          <a class="btn" href="recipe-detail.html">취소</a>
        </div>
      </main>

      <script>
        // 페이지 초기화
        const rid = new URL(location.href).searchParams.get('from') || 'kimchi';
        const r = (window.RECIPES || []).find(x => x.id === rid) || { title: '원본' };
        document.getElementById('fromTitle').textContent = r.title;

        // 재료 추가
        const ing = [];
        document.getElementById('addIng').onclick = () => {
          const v = document.getElementById('ingInput').value.trim(); if (!v) return;
          ing.push(v); document.getElementById('ingInput').value = '';
          renderIng();
        };
        function renderIng() {
          const ul = document.getElementById('ingList'); ul.innerHTML = '';
          ing.forEach((x, i) => { const li = document.createElement('li'); li.textContent = x; ul.appendChild(li); });
        }

        // 단계 추가
        const steps = [];
        function addStep() {
          const i = steps.length + 1;
          const box = document.createElement('div'); box.className = 'card'; box.style.padding = '12px'; box.style.marginTop = '8px';
          box.innerHTML = `<b class="small">단계 ${i}</b>
      <div class="form-row"><textarea class="input step-text" rows="3" placeholder="이 단계의 조리 방법을 자세히 설명해주세요"></textarea></div>
      <div class="row"><input class="input step-img" placeholder="이미지 URL을 입력해주세요" style="flex:1"><button class="btn">업로드</button></div>`;
          document.getElementById('steps').appendChild(box);
          steps.push(box);
        }
        document.getElementById('addStep').onclick = addStep;
        addStep(); // 기본 1개

        // 제출
        document.getElementById('submit').onclick = () => {
          const data = {
            baseId: rid,
            name: document.getElementById('name').value.trim(),
            desc: document.getElementById('desc').value.trim(),
            thumb: document.getElementById('thumb').value.trim(),
            diff: document.getElementById('diff').value,
            time: document.getElementById('time').value.trim(),
            ingredients: ing,
            steps: steps.map(b => ({ text: b.querySelector('.step-text').value, img: b.querySelector('.step-img').value })),
            created: Date.now(),
          };
          const key = 'kp_variants';
          const list = JSON.parse(localStorage.getItem(key) || '[]');
          list.push(data); localStorage.setItem(key, JSON.stringify(list));
          alert('변형 레시피가 등록되었습니다!');
          location.href = '${path}/recipes/recipe-detail.jsp?id=' + rid;
        };
      </script>
    </body>

    </html>