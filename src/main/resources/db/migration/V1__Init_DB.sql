create table if not exists user_role
(
    id       int primary key auto_increment,
    role_name varchar(20) unique not null
);

create table if not exists base_user
(
    id         bigint primary key auto_increment,
    username   varchar(100) unique not null,
    password   varchar(255)        not null,
    first_name varchar(30)         not null,
    last_name  varchar(30)         not null,
    is_blocked boolean             not null,
    role_id    int                 not null,
    email           varchar(255) unique not null,
    activation_code varchar(255),
    phone_number    varchar(13)     not null,
    is_active       boolean         not null,
    balance         double          not null,
    loyalty_points  int             not null,

    foreign key (role_id) references user_role (id) on delete restrict on update cascade
);

alter table base_user
    add index (username);

create table if not exists product_type
(
    id        int primary key auto_increment,
    type_name varchar(30) unique not null,
    img_filename varchar(255)        not null
);

create table if not exists product
(
    id            int primary key auto_increment,
    product_name  varchar(255) unique not null,
    price         double              not null,
    img_filename  varchar(255)        not null,
    description   varchar(255)        not null,
    type_id       int                 not null,

    foreign key (type_id) references product_type (id) on delete cascade on update cascade
);

alter table product
    add index (product_name);

create table if not exists order_status
(
    id          int primary key auto_increment,
    status_name varchar(20) unique not null
);

create table if not exists payment_method(
    id int primary key auto_increment,
    method_name varchar(20) unique not null
);

create table if not exists cafe_order
(
    id            bigint primary key auto_increment,
    address       varchar(40)   not null,
    review_code   varchar(10)           ,
    cost          double        not null,
    create_date   timestamp     not null,
    delivery_date timestamp     not null,
    status_id     int           not null,
    method_id     int           not null,
    user_id       bigint        not null,

    foreign key (status_id) references order_status (id) on delete restrict on update cascade,
    foreign key (method_id) references payment_method(id) on delete restrict on update cascade,
    foreign key (user_id) references base_user (id) on delete cascade on update cascade
);

alter table cafe_order
    add index (user_id);

create table if not exists order_product
(
    order_id       bigint not null,
    product_id     int    not null,
    amount int            not null,

    primary key (order_id, product_id),
    foreign key (order_id) references cafe_order (id) on delete restrict on update cascade,
    foreign key (product_id) references product (id) on delete restrict on update cascade
);

create table if not exists review
(
    id       bigint primary key auto_increment,
    feedback varchar(2048),
    rate     int not null,
    order_id bigint not null unique,

    foreign key (order_id) references cafe_order (id) on delete cascade on update cascade
);