<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>Kookpang Admin</title>
<link
	href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css"
	rel="stylesheet" />
<link href="${path}/admin/css/styles.css" rel="stylesheet" />
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
	crossorigin="anonymous"></script>
</head>
<body class="sb-nav-fixed">
	<jsp:include page="common/header.jsp"></jsp:include>
	<div id="layoutSidenav">
		<jsp:include page="common/left.jsp"></jsp:include>
		<div id="layoutSidenav_content">
			<main>
				<div class="container-fluid px-4">
					<h1 class="mt-4">게시글 조회</h1>
					<ol class="breadcrumb mb-4">
						<li class="breadcrumb-item"><a
							href="${path}/front?key=admin&methodName=adminPage">Dashboard</a></li>
						<li class="breadcrumb-item active">게시글 조회</li>
					</ol>
					<div class="card mb-4">
						<div class="card-body">게시글을 조회, 삭제할 수 있는 페이지</div>
					</div>
					<div class="card mb-4">
						<div class="card-header row">
							<div class="col-md-6">
								<i class="fas fa-table me-1"></i> 게시글 목록
							</div>
							<div class="col-md-6 text-end">
								<button type="button" class="btn btn-danger btn-sm"
									id="deleteBtn">선택 삭제</button>
							</div>
						</div>
						<div class="card-body">
							<form action="${path}/front?key=admin&methodName=deleteBoards"
								method="post" id="deleteForm">
								<table id="datatablesSimple">
									<thead>
										<tr>
											<th>체크박스</th>
											<th>작성자</th>
											<th>제목</th>
											<th>카테고리</th>
											<th>조회수</th>
											<th>등록일</th>
										</tr>
									</thead>
									<tfoot>
										<tr>
											<th>체크박스</th>
											<th>작성자</th>
											<th>제목</th>
											<th>카테고리</th>
											<th>조회수</th>
											<th>등록일</th>
										</tr>
									</tfoot>
									<tbody>
										<c:forEach var="item" items="${list}">
											<tr>
												<td><input type="checkbox" name="selectedItems"
													value="${item.postId}"></td>
												<td>${item.nickname}</td>
												<td>${item.title}</td>
												<td>${item.category}</td>
												<td>${item.viewCount}</td>
												<td>${item.createdAt}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</form>
						</div>
					</div>
				</div>
			</main>
			<jsp:include page="common/footer.jsp"></jsp:include>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>
	<script src="${path}/admin/js/scripts.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
		crossorigin="anonymous"></script>
	<script src="${path}/admin/assets/demo/chart-area-demo.js"></script>
	<script src="${path}/admin/assets/demo/chart-bar-demo.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.min.js"
		crossorigin="anonymous"></script>
	<script src="${path}/admin/js/datatables-simple-demo.js"></script>
  <script>
    document.getElementById("deleteBtn").addEventListener("click", function () {
      if (confirm("선택한 게시글을 삭제하시겠습니까?")) {
        document.getElementById("deleteForm").submit();
      }
    });
  </script>
</body>
</html>
