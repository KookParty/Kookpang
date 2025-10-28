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
            <h1 class="mt-4">식재료 등록</h1>
            <ol class="breadcrumb mb-4">
              <li class="breadcrumb-item"><a href="${path}/front?key=admin&methodName=adminPage">Dashboard</a></li>
              <li class="breadcrumb-item active">식재료 등록</li>
            </ol>
            <div class="card mb-4">
              <div class="card-body">식재료를 등록할 수 있는 페이지</div>
            </div>
            <div class="card mb-4">
              <div class="card-header">
                <i class="fas fa-table me-1"></i>
                식재료 등록
              </div>
              <form
                action="${path}/front?key=admin&methodName=insertProduct"
                method="post"
              >
                <div class="card-body row">
                  <div class="col-md-6">
                    <div class="mb-3">
                      <label for="productImage" class="form-label">이미지</label>
                      <input type="file" class="form-control" id="productImage" accept="image/*" required />
                    </div>
                    <div class="mb-3">
                      <!--이미지 미리보기-->
                      <img
                        id="imagePreview"
                        src="#"
                        alt="이미지 미리보기"
                        style="max-width: 100%; height: auto; display: none"
                      />
                    </div>
                  </div>
                  <div class="col-md-6">
                    <input type="hidden" name="imageUrl" id="imageUrl" />
                    <div class="mb-3">
                      <label for="productName" class="form-label">이름</label>
                      <input type="text" class="form-control" id="productName" name="name" required />
                    </div>
                    <div class="mb-3">
                      <label for="productPrice" class="form-label">가격</label>
                      <input type="number" class="form-control" id="productPrice" name="price" required />
                    </div>
                    <div class="mb-3">
                      <label for="productCategory" class="form-label">카테고리</label>
                      <input type="text" class="form-control" id="productCategory" name="category" required />
                    </div>
                    <div class="mb-3">
                      <label for="productDescription" class="form-label">설명</label>
                      <textarea
                        class="form-control"
                        id="productDescription"
                        name="description"
                        rows="5"
                        required
                      ></textarea>
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
    <script
      src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js"
      crossorigin="anonymous"
    ></script>
    <script src="${path}/admin/js/datatables-simple-demo.js"></script>
    <script>
      document.getElementById("productImage").addEventListener("change", function (event) {
        const file = event.target.files[0];
        console.log(file);
        let imgUrl = "";
        async function uploadImage(file) {
          const formData = new FormData();
          formData.append("image", file);

          try {
            const response = await fetch("${path}/ajax?key=admin&methodName=uploadImage", {
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
            document.getElementById("imagePreview").src = imgUrl;
            document.getElementById("imagePreview").style.display = "block";
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
    </script>
  </body>
</html>
