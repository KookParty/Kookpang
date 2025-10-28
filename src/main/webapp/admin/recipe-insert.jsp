<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@taglib
uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>Kookpang Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
    <link href="${path}/admin/css/styles.css" rel="stylesheet" />
    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
  </head>
  <body class="sb-nav-fixed">
    <jsp:include page="common/header.jsp"></jsp:include>
    <div id="layoutSidenav">
      <jsp:include page="common/left.jsp"></jsp:include>
      <div id="layoutSidenav_content">
        <main>
          <div class="container-fluid px-4">
            <h1 class="mt-4">레시피 등록</h1>
            <ol class="breadcrumb mb-4">
              <li class="breadcrumb-item"><a href="${path}/front?key=admin&methodName=adminPage">Dashboard</a></li>
              <li class="breadcrumb-item active">레시피 등록</li>
            </ol>
            <div class="card mb-4">
              <div class="card-body">레시피를 등록할 수 있는 페이지</div>
            </div>
            <div class="card mb-4">
              <div class="card-header">
                <i class="fas fa-table me-1"></i>
                레시피 등록
              </div>
              <form
                action="${path}/front?key=admin&methodName=insertRecipe"
                method="post"
                onsubmit="return submitForm()"
              >
                
                <input type="hidden" name="ingredients" id="ingredientsInput">
                <input type="hidden" name="steps" id="stepsInput">

                <div class="card-body row">
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label for="recipeImage" class="form-label">이미지</label>
                      <input type="file" class="form-control" id="recipeImage" accept="image/*" required />
                    </div>

                    <div class="mb-3">
                      <label class="form-label">재료 검색 및 추가</label>
                      <div class="row">
                        <input list="productList" id="ingInput" class="form-control col" placeholder="재료를 검색해보세요">
                        <datalist id="productList">
                          <c:forEach items="${ingredientList}" var="product">
                            <option value="${product.name}" data-id="${product.productId}"></option>
                          </c:forEach>
                        </datalist>
                        <button type="button" id="addIng" class="col-auto">추가</button>
                      </div>
                      <ul id="ingList" class="form-text" style="margin:10px 0 0; padding-left:18px"></ul>
                    </div>

                    <div class="mb-3">
                      <label class="form-label">조리법</label>
                      <div id="steps"></div>
                      <button type="button" class="button" id="addStep">+ 단계 추가</button>
                    </div>
                  </div>

                  <div class="col-md-6">
                    <input type="hidden" name="thumbnailUrl" id="imageUrl" />
                    <div class="mb-3">
                      <label for="recipeTitle" class="form-label">제목</label>
                      <input type="text" class="form-control" id="recipeTitle" name="title" required />
                    </div>
                    
                    <div class="mb-3">
                      <label for="recipeDescription" class="form-label">설명</label>
                      <textarea
                      class="form-control"
                      id="recipeDescription"
                      name="description"
                      rows="5"
                      required
                      ></textarea>
                    </div>

                    <div class="mb-3">
                      <label for="pattern" class="form-label">요리 종류</label>
                      <input type="text" class="form-control" id="pattern" name="category" required />
                    </div>
                    <div class="mb-3">
                      <label for="way" class="form-label">조리 방법</label>
                      <input type="text" class="form-control" id="way" name="way" required />
                    </div>

                  </div>
                    <div class="col-md-12 text-end">
                        <button type="submit" class="btn btn-primary">등록</button>
                </div>
              </form>
            </div>
          </div>
        </main>
        <jsp:include page="common/footer.jsp"></jsp:include>
      </div>
    </div>
    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
      crossorigin="anonymous"
    ></script>
    <script src="${path}/admin/js/scripts.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
    <script src="${path}/admin/assets/demo/chart-area-demo.js"></script>
    <script src="${path}/admin/assets/demo/chart-bar-demo.js"></script>
    <script
      src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js"
      crossorigin="anonymous"
    ></script>
    <script src="${path}/admin/js/datatables-simple-demo.js"></script>
    <script>
      document.getElementById("recipeImage").addEventListener("change", function (event) {
        const file = event.target.files[0];
        console.log(file);
        let imgUrl = "";
        async function uploadImage(file) {
          const formData = new FormData();
          formData.append("image", file);

          try {
            const response = await fetch("${path}/ajax?key=admin&methodName=uploadRecipeImage", {
              method: "POST",
              body: formData,
            });

            if (!response.ok) {
              throw new Error("이미지 업로드 실패");
            }

            const data = await response.json();
            imgUrl = data;
            console.log("Uploaded Image URL:", imgUrl);
            document.getElementById("imageUrl").value = imgUrl;
          } catch (error) {
            console.error("Error uploading image:", error);
            alert("이미지 업로드 중 오류가 발생했습니다.");
            return null;
          }
        }
        if (file) {
          uploadImage(file);
        }
      });

      // 재료 추가
      const ingredientsName = [];
      const ingredientsPid = [];
      let productId = null;

      document.getElementById('ingInput').onchange = function() {
        const datalist = document.getElementById('productList');
        for (const option of datalist.options) {
          if (option.value === this.value) {
            productId = option.dataset.id;
            break;
          }
        }
      }

      document.getElementById('addIng').onclick = () => {
        const name = document.getElementById('ingInput').value; 
        if (!name) return;
        ingredientsName.push(name);
        ingredientsPid.push(productId);
        
        //비우기
        document.getElementById('ingInput').value = '';
        productId = null;

        renderIng();
      };

      // 추가된 재료 렌더링
      function renderIng() {
        const ul = document.getElementById('ingList');
        ul.innerHTML = '';
        ingredientsName.forEach((ing, i) => { 
          const li = document.createElement('li');
          li.textContent = ing; 
          ul.appendChild(li);
        });
      }

      // 단계 추가
      let index = 0;
      function addStep() {
        const i = ++index;
        const box = document.createElement('div'); 
        box.className = 'card'; 
        box.style.padding = '12px'; 
        box.style.marginTop = '8px';
        box.innerHTML = `<b class="small">단계 ${"${i}"}</b>
          <div class="form-row"><textarea class="step-text form-control" rows="3" placeholder="이 단계의 조리 방법을 자세히 설명해주세요"></textarea></div>
          <div class="row"><input type="file" class="form-control" onchange="getImgUrl(this, ${"${i}"})" placeholder="이미지 URL을 입력해주세요"></div>
          <input type="hidden" id="stepImg${"${i}"}" />`;
        document.getElementById('steps').appendChild(box);
      }
      document.getElementById('addStep').onclick = addStep;
      
      const getImgUrl = function (input, i) {
        const file = input.files[0];
        console.log(file);
        let imgUrl = "";
        async function uploadStepImage(file) {
          const formData = new FormData();
          formData.append("image", file);

          try {
            const response = await fetch("${path}/ajax?key=admin&methodName=uploadRecipeImage", {
              method: "POST",
              body: formData,
            });

            if (!response.ok) {
              throw new Error("이미지 업로드 실패");
            }

            const data = await response.json();
            imgUrl = data;
            console.log("Uploaded Image URL:", imgUrl);
            document.getElementById("stepImg" + i).value = imgUrl;
          } catch (error) {
            console.error("Error uploading image:", error);
            alert("이미지 업로드 중 오류가 발생했습니다.");
            return null;
          }
        }
        if (file) {
          uploadStepImage(file);
        }
      };
      
      // form 제출
      function submitForm() {
        // 재료 배열을 JSON 문자열로 변환
        const ingredients = ingredientsName.map((name, i) => ({
            name: name,
            productId: ingredientsPid[i],
        }));

        // 조리법 배열을 JSON 문자열로 변환
        const steps = Array.from(document.querySelectorAll('.step-text')).map((el, i) => ({
      	  description: el.value,
      	  imageUrl: document.querySelector(`#stepImg${i + 1}`)?.value || ''
        }));

        document.getElementById('ingredientsInput').value = JSON.stringify(ingredients);
        document.getElementById('stepsInput').value = JSON.stringify(steps);

        return true;
      }
    </script>
  </body>
</html>
