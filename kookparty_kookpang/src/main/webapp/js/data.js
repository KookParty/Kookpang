const RECIPES = [
  { id:'kimchi', title:'김치찌개', time:'30분', serves:'4인분', likes:1247, rating:4.8, label:'공식 레시피', difficulty:'쉬움',
    img:'assets/img/kimchi.jpg', desc:'한국인의 소울푸드, 얼큰하고 시원한 김치찌개', items:['ing_pork','ing_kimchi','ing_tofu','ing_greenonion','ing_onion','ing_soy','ing_chili'] },
  { id:'bulgogi', title:'불고기', time:'45분', serves:'3인분', likes:892, rating:4.7, label:'공식 레시피', difficulty:'보통',
    img:'assets/img/bulgogi.jpg', desc:'달콤짭짤한 한국 전통 불고기', items:['ing_beef','ing_onion','ing_garlic','ing_soy','ing_sesame'] },
  { id:'bibimbap', title:'비빔밥', time:'25분', serves:'2인분', likes:1712, rating:4.9, label:'공식 레시피', difficulty:'보통',
    img:'assets/img/bibimbap.jpg', desc:'다양한 나물과 고추장을 비벼 먹는 영양 만점 비빔밥', items:['ing_beef','ing_onion','ing_chili','ing_paste','ing_sesame'] },
  { id:'sundubu', title:'순두부찌개', time:'20분', serves:'2인분', likes:642, rating:4.6, label:'쉬운 레시피', difficulty:'쉬움',
    img:'assets/img/sundubu.jpg', desc:'부드러운 순두부로 얼큰하게 끓인 찌개', items:['ing_tofu','ing_garlic','ing_greenonion','ing_chili','ing_soy'] },
  { id:'doenjang', title:'된장찌개', time:'28분', serves:'3인분', likes:984, rating:4.7, label:'공식 레시피', difficulty:'쉬움',
    img:'assets/img/doenjang.jpg', desc:'구수한 된장향이 일품인 찌개', items:['ing_tofu','ing_onion','ing_greenonion','ing_garlic','ing_soy'] },
  { id:'naengmyeon', title:'냉면', time:'15분', serves:'1인분', likes:1310, rating:4.8, label:'여름 추천', difficulty:'쉬움',
    img:'assets/img/naengmyeon.jpg', desc:'시원한 육수와 쫄깃한 면발의 하모니', items:['ing_beef','ing_onion','ing_chili','ing_sesame'] }
];
const FALLBACK = {};


// 쇼핑 카탈로그 (식재료) - 레시피 담기에서 참조
const INGREDIENTS = [
  {id:'ing_pork',   name:'돼지고기 (삼겹살)', price:15000, unit:'500g', img:'assets/img/pork.jpg'},
  {id:'ing_beef',   name:'소고기 (등심)',     price:22000, unit:'300g', img:'assets/img/beef.jpg'},
  {id:'ing_chicken',name:'닭가슴살',         price:7800,  unit:'500g', img:'assets/img/chicken.jpg'},
  {id:'ing_kimchi', name:'김치',             price:8000,  unit:'1kg',  img:'assets/img/kimchi_ing.jpg'},
  {id:'ing_tofu',   name:'두부',             price:2500,  unit:'1모',  img:'assets/img/tofu.jpg'},
  {id:'ing_greenonion', name:'대파',         price:1500,  unit:'2대',  img:'assets/img/greenonion.jpg'},
  {id:'ing_onion',  name:'양파',             price:3000,  unit:'1망',  img:'assets/img/onion.jpg'},
  {id:'ing_garlic', name:'마늘',             price:4000,  unit:'500g', img:'assets/img/garlic.jpg'},
  {id:'ing_chili',  name:'청양고추',         price:3500,  unit:'200g', img:'assets/img/chili.jpg'},
  {id:'ing_soy',    name:'진간장',           price:4500,  unit:'500ml',img:'assets/img/soy.jpg'},
  {id:'ing_paste',  name:'고추장',           price:6500,  unit:'500g', img:'assets/img/gochujang.jpg'},
  {id:'ing_sesame', name:'참기름',           price:8000,  unit:'330ml',img:'assets/img/sesame.jpg'}
];
