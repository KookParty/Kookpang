SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

create table users(
	user_id bigint primary key auto_increment, -- id
    email varchar(100) not null unique, -- email(id 대용)
    password varchar(100) not null,
    name varchar(50) not null, -- 이름
    nickname varchar(50) not null unique, -- 닉네임
    phone varchar(30) not null unique, -- 전화번호
    address varchar(200), -- 주소
    role enum("admin","user") not null, -- 권한
    point int default 2000,
    status tinyint not null default 1, -- 회원상태(활동중, 탈퇴)
    created_at datetime not null default now(), -- 가입일
    CONSTRAINT chk_email_format CHECK (
        email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
    )
);

create table recipes(
	recipe_id bigint primary key auto_increment, -- 레시피ID
    user_id bigint not null, -- 외래키(users 테이블 pk)
    title varchar(100) not null, -- 레시피 제목
    description varchar(200) not null, -- 레시피 간단 설명
    thumbnail_url varchar(200) not null, -- url
    recipe_type enum("base", "variant") not null, -- 레시피 타입(기본/변형)
    way varchar(20), -- 조리방법
    category varchar(20), -- 요리종류
    parent_recipe_id bigint, -- 부모레시피 번호(기본 레시피면 null, 변형이면 기본 레시피번호)
    created_at datetime not null default now(),
    foreign key(user_id) references users(user_id)
);

create table products(
	product_id bigint primary key auto_increment, -- 식재료ID
    name varchar(100) not null, -- 제목에 수, 단위 포함 ex- 파 1단, 간장 500ml
    price int not null, -- 가격
    description varchar(200) not null, -- 재료 설명
    category varchar(100) not null, -- 카테고리
    brand varchar(100), -- 브랜드명
    image_url varchar(200), -- 이미지 주소
    created_at datetime not null default now()
);

create table likes(
	like_id bigint primary key auto_increment, -- 좋아요 ID
    user_id bigint not null, -- user_id(fk)
    target_type enum("recipe", "review", "post") not null, -- 어느 곳의 좋아요인지 
    target_id bigint not null, -- 타겟 타입의 pk
    created_at datetime not null default now(), -- 좋아요 등록일
    foreign key(user_id) references users(user_id)
);

create table posts(
	post_id bigint primary key auto_increment, -- ID
    user_id bigint not null, -- user_id(fk)
    category enum("notice", "free") not null default "free", -- 게시판 카테고리(공지, 자유)
    title varchar(100) not null, -- 제목
    content text not null, -- 내용
    view_count bigint not null default 0, -- 조회수
    comment_count bigint not null default 0, -- 댓글수
    like_count bigint not null default 0, -- 좋아요수
    created_at datetime not null default now(), -- 등록일
	foreign key(user_id) references users(user_id)
);

create table post_images(
	image_id bigint primary key auto_increment, -- ID
    post_id bigint not null, -- 게시글id(fk)
    image_url varchar(200), -- 이미지 주소
    image_order int not null, -- 이미지 순서
    foreign key(post_id) references posts(post_id)
);

create table comments(
	comment_id bigint primary key auto_increment, -- ID
    post_id bigint not null, -- 게시글 id(fk)
    user_id bigint not null, -- user_id(fk)
    content varchar(200) not null, -- 댓글 내용
    created_at datetime not null default now(), -- 등록일
	foreign key(user_id) references users(user_id),
    foreign key(post_id) references posts(post_id) on delete cascade
);

create table reviews(
	review_id bigint primary key auto_increment, -- ID
    recipe_id bigint not null, -- 레시피id(fk)
    user_id bigint not null, -- user_id(fk)
    rating int not null, -- 별점(1~5)까지
    content varchar(500) not null, -- 리뷰내용
    image_url varchar(200), -- 이미지 주소
    created_at datetime not null default now(),
    foreign key(recipe_id) references recipes(recipe_id) on delete cascade,
    foreign key(user_id) references users(user_id)
);

create table steps(
	step_id bigint primary key auto_increment, -- ID
    recipe_id bigint not null, -- 레시피id(fk), 어느 레시피인지
    step_order int not null, -- 조리순서
    description varchar(200), -- 조리방법에 대한 설명
    image_url varchar(200), -- 이미지 주소
    foreign key(recipe_id) references recipes(recipe_id) on delete cascade
);

create table ingredients(
	ingredients_id bigint primary key auto_increment, -- ID
    recipe_id bigint not null, -- 레시피id(fk), 어느 레시피인지
    product_id bigint, -- 식재료id(fk), 무슨 재료인지
    name varchar(100) not null,
    quantity varchar(100),
    foreign key(recipe_id) references recipes(recipe_id) on delete cascade,
    foreign key(product_id) references products(product_id) on delete cascade
);

create table orders(
	order_id bigint primary key auto_increment, -- ID
    user_id bigint not null, -- user_id(fk)
    created_at datetime not null default now(),
    total_price int not null,
    delivery_fee int not null,
    used_point int default 0,
    shipping_address varchar(200) not null,
    order_name varchar(40),
    cid varchar(30),
    tid varchar(30),
    partner_order_id varchar(40),
    partner_user_id varchar(40),
    pg_token varchar(30),
    status tinyint not null default 1, -- 1(true) 결제완료상태, 0(false) 결제취소상태
    foreign key(user_id) references users(user_id)
);

create table carts(
	cart_id bigint primary key auto_increment, -- ID
    user_id bigint not null, -- user_id(fk)
    product_id bigint not null, -- 식재료id(fk), 무슨 재료인지
    count int not null default 1,
    created_at datetime not null default now(),
    foreign key(user_id) references users(user_id),
    foreign key(product_id) references products(product_id) on delete cascade
);

create table orders_items(
	id bigint primary key auto_increment, -- ID
    order_id bigint not null, -- 주문번호(fk)
    product_id bigint not null, -- 식재료id(fk), 무슨 재료인지
    count int not null default 1, -- 수량
    price int not null, -- 구매당시 가격저장용
    foreign key(order_id) references orders(order_id),
    foreign key(product_id) references products(product_id)
);