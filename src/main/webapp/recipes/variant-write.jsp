<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@taglib
uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>변형 레시피 작성</title>
    <link rel="stylesheet" href="${path}/css/styles.css" />
    <script type="text/javascript">
      const CONTEXT_PATH = "${pageContext.request.contextPath}";
    </script>
  </head>

  <body>
    <!-- header시작 -->
    <jsp:include page="../common/header.jsp"></jsp:include>
    <!-- header끝 -->

    <main class="container page" style="max-width: 980px; padding: 16px">
      <h2 style="margin: 0 0 6px">변형 레시피 작성</h2>
      <p class="small">
        “<span id="fromTitle">${parentTitle}</span>” 레시피를 바탕으로 나만의 변형 레시피를 만들어보세요.
      </p>

      <form
        id="writeForm"
        name="writeForm"
        method="post"
        action="${path}/front"
        onsubmit="return submitForm()"
        enctype="multipart/form-data"
      >
        <input type="hidden" name="key" value="recipe" />
        <input type="hidden" name="methodName" value="insertRecipe" />
        <input type="hidden" name="parentId" value="${parentId}" />

        <input type="hidden" name="ingredientsInput" id="ingredientsInput" />
        <input type="hidden" name="stepsInput" id="stepsInput" />

        <section class="card" style="padding: 16px">
          <h3 style="margin: 0 0 12px">기본 정보</h3>
          <label class="small">레시피 이름</label>
          <div class="form-row">
            <input type="text" name="title" id="title" class="input" placeholder="변형 레시피 이름을 입력해주세요" />
          </div>

          <label class="small">설명</label>
          <div class="form-row">
            <textarea
              name="desc"
              id="desc"
              class="input"
              rows="3"
              placeholder="레시피에 대한 설명을 입력해주세요"
            ></textarea>
          </div>

          <label class="small">썸네일 이미지</label>
          <div class="row">
            <input
              type="file"
              name="thumb"
              id="thumb"
              class="input"
              placeholder="이미지 URL을 입력해주세요"
              style="flex: 1"
            />
          </div>

          <div class="row">
            <div style="flex: 1">
              <label class="small">요리 종류</label>
              <input
                type="text"
                name="pattern"
                id="pattern"
                class="input"
                placeholder="요리 종류를 입력해주세요 (반찬/국&찌개/일품/후식)"
              />
            </div>
            <div style="flex: 1">
              <label class="small">조리 방법</label>
              <input
                type="text"
                name="way"
                id="way"
                class="input"
                placeholder="조리 방법을 입력해주세요 (굽기/찌기/끓이기/기타)"
              />
            </div>
          </div>
        </section>

        <section class="card" style="padding: 16px; margin-top: 16px">
          <h3 style="margin: 0 0 12px">재료</h3>
          <label class="small">재료 검색 및 추가</label>
          <div class="row">
            <input list="productList" id="ingInput" class="input" placeholder="재료를 검색해보세요" style="flex: 1" />
            <datalist id="productList">
              <c:forEach items="${products}" var="product">
                <option value="${product.name}" data-id="${product.productId}"></option>
              </c:forEach>
            </datalist>
            <button type="button" class="btn" id="addIng">추가</button>
          </div>
          <ul id="ingList" class="small" style="margin: 10px 0 0; padding-left: 18px"></ul>
        </section>

        <section class="card" style="padding: 16px; margin-top: 16px">
          <h3 style="margin: 0 0 12px">조리법</h3>
          <div id="steps"></div>
          <button type="button" class="btn" id="addStep" style="margin-top: 8px">+ 단계 추가</button>
        </section>

        <div style="display: flex; gap: 8px; justify-content: flex-end; margin-top: 16px">
          <button type="submit" class="btn dark">변형 레시피 등록</button>
          <button type="button" class="btn" onclick="history.back()">취소</button>
        </div>
      </form>
    </main>

    <script>
      // 재료 추가
      const ingredientsName = [];
      const ingredientsPid = [];
      let productId = null;

      document.getElementById("ingInput").onchange = function () {
        const datalist = document.getElementById("productList");
        for (const option of datalist.options) {
          if (option.value === this.value) {
            productId = option.dataset.id;
            break;
          }
        }
      };

      document.getElementById("addIng").onclick = () => {
        const name = document.getElementById("ingInput").value;
        if (!name) return;
        ingredientsName.push(name);
        ingredientsPid.push(productId);

        //비우기
        document.getElementById("ingInput").value = "";
        productId = null;

        renderIng();
      };

      // 추가된 재료 렌더링
      function renderIng() {
        const ul = document.getElementById("ingList");
        ul.innerHTML = "";
        ingredientsName.forEach((ing, i) => {
          const li = document.createElement("li");
          li.textContent = ing;
          ul.appendChild(li);
        });
      }

      // 단계 추가
      let index = 0;
      function addStep() {
        const i = ++index;
        const box = document.createElement("div");
        box.className = "card";
        box.style.padding = "12px";
        box.style.marginTop = "8px";
        box.innerHTML = `<b class="small">단계 ${"${i}"}</b>
            <div class="form-row"><textarea class="input step-text" rows="3" placeholder="이 단계의 조리 방법을 자세히 설명해주세요"></textarea></div>
            <div class="row"><input type="file" name="stepImg${"${i}"}" class="input step-img" placeholder="이미지 URL을 입력해주세요" style="flex:1"></div>`;
        document.getElementById("steps").appendChild(box);
      }
      document.getElementById("addStep").onclick = addStep;

      // form 제출
      function submitForm() {
        if (!checkValid) return false;

        // 재료 배열을 JSON 문자열로 변환
        const ingredients = ingredientsName.map((name, i) => ({
          name: name,
          productId: ingredientsPid[i],
        }));

        // 조리법 배열을 JSON 문자열로 변환
        const steps = Array.from(document.querySelectorAll(".step-text")).map((el, i) => ({
          description: el.value,
          imageUrl: document.querySelector(`.step-img:nth-of-type(${"${i + 1}"})`)?.value || "",
        }));

        document.getElementById("ingredientsInput").value = JSON.stringify(ingredients);
        document.getElementById("stepsInput").value = JSON.stringify(steps);

        return true;
      }

      // 입력 유효성 검사
      function checkValid() {
        var f = window.document.writeForm;

        if (f.title.value == "") {
          alert("레시피 이름을 입력해주세요.");
          f.title.focus();
          return false;
        }
        if (f.desc.value == "") {
          alert("설명을 입력해주세요.");
          f.desc.focus();
          return false;
        }
        if (!f.thumb.files || f.thumb.files.length == 0) {
          alert("썸네일 이미지를 업로드 해주세요.");
          f.thumb.focus();
          return false;
        }

        return true;
      }
    </script>
  </body>
</html>
