const conPath = CONTEXT_PATH;

const API_PATHS = {
    // ---board---
    moveBoard: conPath + "/boards/board.jsp", //board페이지로 이동
    moveWriteBoard: conPath + "/boards/board-write.jsp", //글쓰기 페이지로 이동
    writeBoard: "", //글 쓰기(백엔드로 전송, db저장시 사용)
    deleteBoard: "", //글 삭제(백엔드로 전송, db저장시 사용)
    updateBoard: "", //글 수정(백엔드로 전송, db저장시 사용)

    // ---product, cart---
    moveProduct: conPath + "/orders/ingredients.jsp", //식재료페이지로 이동
    moveCart: conPath + "/orders/order-review.jsp", //장바구니 페이지로 이동
    moveOrder: conPath + "", //주문하기 페이지 이동(현재 없음)
    moveOrderList: "", //내 주문 내역보기(현재 없음? myPage에 있나..?)
    moveOrderResult: conPath + "/orders/order-result.jsp", //결제 완료 후 주문완료(주문결과) 페이지로 이동
    selectCart: "", //한명의 장바구니를 조회시 사용할 api 주소
    insertCart: "", //장바구니에 등록시 사용할 api 주소
    deleteCart: "", //장바구니 삭제시 사용할 api 주소
    updateCart: "", //장바구니 수량 수정시 사용할 api 주소
    order : "", // 주문하기(백엔드로 전송, db저장시 사용)
    cancleOrder : conPath + "/orders/order-cancle.jsp", //주문취소(백엔드로 전송, db저장시 사용)(페이지 수정 필요)
    
    // ---recipes---
    moveRecipe: conPath + "/recipes/recipes.jsp", //레시피 페이지 이동
    moveRecipeDetail: conPath + "recipes/recipe-detail.jsp", //레시피 디테일 페이지 이동
    moveWriteRecipe: conPath + "/recipes/variant-write.jsp", //변형 레시피 등록페이지 이동
    selectRecipeAll: "", //레시피 전체 리스트 select시 사용할 api 주소
    selectRecipe: "", //레시피 하나 select시 사용할 api 주소
    insertRecipe: "", //레시피 등록시 사용할 api 주소
    deleteRecipe: "", //레시피 삭제시 사용할 api 주소
    updateRecipe: "", //레시피 수정시 사용할 api 주소
    
    // ---users---
    moveLogin: conPath + "/users/login.jsp", //로그인 페이지로 이동
    moveMyPage: conPath + "/users/mypage.jsp", //마이페이지 이동
    moveRegister: conPath + "/users/register.jsp", //회원 가입 페이지로 이동
    login: "", //로그인 시 사용할 api 주소
    selectUser: "", //회원의 정보를 가져올때 사용할 api 주소
    insertUser: "", //회원가입시 사용할 api 주소
    updateUser: "", //회원정보 수정시 사용할 api 주소
    deleteUser: "", //회원정보 삭제?시 사용할 api 주소
}