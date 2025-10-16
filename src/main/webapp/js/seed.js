(function(){
  try{
    if(!localStorage.getItem('kp_seed_v2')){
      // Likes pre-seed
      localStorage.setItem('kp_likes', JSON.stringify(['kimchi','bulgogi','bibimbap']));

      // Posts pre-seed
      const posts=[
        {id:Date.now()-86400000*3, nickname:'쿠크팡', title:'초보도 성공하는 김치찌개 꿀팁', body:'돼지고기 먼저 볶으면 깊은 맛!\n김치는 시어야 맛있다!', created: Date.now()-86400000*3, likes:132, comments:12},
        {id:Date.now()-86400000*2, nickname:'오리왕', title:'비빔밥 나물 준비 요령', body:'시금치/고사리 데치는 시간만 잘 지켜도 반은 성공', created: Date.now()-86400000*2, likes:98, comments:7},
        {id:Date.now()-86400000, nickname:'홍길동', title:'불고기 양념 비율 공유', body:'간장4 설탕2 맛술2 배즙1 참기름1', created: Date.now()-86400000, likes:210, comments:33}
      ];
      localStorage.setItem('kp_posts', JSON.stringify(posts));

      // Demo user (if none)
      const users = JSON.parse(localStorage.getItem('kp_users')||'[]');
      if(users.length===0){
        const demo={email:'demo@kookpang.com', name:'데모', nickname:'쿠크팡', password:'1234', address:'서울 강남구', phone:'010-0000-0000'};
        localStorage.setItem('kp_users', JSON.stringify([demo]));
        localStorage.setItem('kp_user', JSON.stringify(demo)); // auto-login for demo
      }

      // Cart sample
      if(!localStorage.getItem('kp_cart')){
        localStorage.setItem('kp_cart', JSON.stringify([
          {id:'ing_pork', title:'돼지고기 (삼겹살)', price:15000, qty:1, img:'https://images.unsplash.com/photo-1598514982846-1f3a52f4762a?q=80&w=1200&auto=format&fit=crop'},
          {id:'ing_kimchi', title:'김치', price:8000, qty:1, img:'https://images.unsplash.com/photo-1589301760014-d929f3979dbc?q=80&w=1200&auto=format&fit=crop'}
        ]));
      }

      // One sample order (optional)
      if(!localStorage.getItem('kp_orders')){
        const items=[{title:'김치찌개 재료세트', price:22000, qty:1},{title:'비빔밥 재료세트', price:18000, qty:1}];
        const total=items.reduce((s,x)=>s+x.price*x.qty,0);
        localStorage.setItem('kp_orders', JSON.stringify([{id:Date.now()-3600*1000, items, total, created: Date.now()-3600*1000}]));
      }

      localStorage.setItem('kp_seed_v2','1');
      console.log('KookPang demo seed applied');
    }
  }catch(e){ console.warn('Seed failed', e); }
})();
