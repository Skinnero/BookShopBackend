create table customer
(
    is_deleted      boolean,
    submission_time date,
    id              uuid not null
        constraint customer_pkey
            primary key,
    email           varchar(255),
    name            varchar(255),
    password        varchar(255)
);

create table address
(
    zip_code        bigint not null,
    customer_id     uuid   not null
        constraint address_customer_id_key
            unique
        constraint fk93c3js0e22ll1xlu21nvrhqgg
            references customer,
    id              uuid   not null
        constraint address_pkey
            primary key,
    additional_info varchar(255),
    city            varchar(255),
    street          varchar(255),
    street_number   varchar(255)
);

create table basket
(
    is_complete boolean,
    customer_id uuid not null
        constraint fk5nd9j0bbjcxwom670q1qdummd
            references customer,
    id          uuid not null
        constraint basket_pkey
            primary key
);

create table order_transaction
(
    submission_time timestamp(6),
    basket_id       uuid not null
        constraint order_transaction_basket_id_key
            unique
        constraint fk5g8rafoltuhglfwc3f2uc58i5
            references basket,
    customer_id     uuid not null
        constraint fklv5c53sc5n0oq81ojpncub45i
            references customer,
    id              uuid not null
        constraint order_transaction_pkey
            primary key
);

create table product
(
    price       numeric(12, 2),
    id          uuid not null
        constraint product_pkey
            primary key,
    currency    varchar(255),
    description varchar(255),
    name        varchar(255)
);

create table basket_product
(
    basket_id  uuid not null
        constraint fk28vw328vteqyvaq1oq2giy96l
            references basket,
    product_id uuid not null
        constraint fk3vpwhut7g8c5fm5748b2v42ei
            references product
);

create table product_category
(
    id         uuid not null
        constraint product_category_pkey
            primary key,
    department varchar(255),
    name       varchar(255)
);

create table product_category_product
(
    product_category_id uuid not null
        constraint fk66ul5lfm90ujhahulnnwmootw
            references product_category,
    product_id          uuid not null
        constraint fkooj7nttwg6qcja3iiejtyt1k3
            references product,
    constraint product_category_product_pkey
        primary key (product_category_id, product_id)
);

create table refresh_token
(
    expiry_date timestamp(6) with time zone,
    customer_id uuid
        constraint refresh_token_customer_id_key
            unique
        constraint fkthlbvd34s3un1d4pxxqv2ni6c
            references customer,
    id          uuid not null
        constraint refresh_token_pkey
            primary key,
    token       varchar(255)
);

create table role
(
    id   uuid not null
        constraint role_pkey
            primary key,
    customer_role varchar(255)
);

create table customer_role
(
    customer_id uuid not null
        constraint fkipr3etk2mjwkv6ahb4x4vwqy3
            references customer,
    role_id     uuid not null
        constraint fkmwml8muyov9xhw4423lp88n2u
            references role,
    constraint customer_role_pkey
        primary key (customer_id, role_id)
);

create table supplier
(
    id          uuid not null
        constraint supplier_pkey
            primary key,
    description varchar(255),
    name        varchar(255)
);

create table supplier_product
(
    product_id  uuid not null
        constraint fkpa7c04lsyqaxrge5h8amxgj1m
            references product,
    supplier_id uuid not null
        constraint fkielp9e4tvmqu204v3s6odp1tc
            references supplier,
    constraint supplier_product_pkey
        primary key (product_id, supplier_id)
);

-- CUSTOMER
INSERT INTO customer(is_deleted, submission_time, id, email, name, password)
VALUES (true, '2013-10-12', 'b2212e0f-8124-44ce-a8d6-31ac5cfb75cb', 'kapi@wp.pl', 'kapi', 'kapi'),
       (false, '2013-10-12', 'ec63f4d5-6964-49a9-822c-36b7349efc3a', 'mati@wp.pl', 'mati', 'mati'),
       (false, '2013-10-12', 'cbc22041-a58b-431f-8d26-7355fab4cb65', 'tomi@wp.pl', 'tomi', 'tomi');

-- ROLE
INSERT INTO role(id, customer_role)
VALUES ('df662349-8ac1-4e4e-8abf-1297be4bfad0', 'ROLE_USER'),
       ('cd6bc1f2-00b2-4b9f-9b12-ab51d970168b', 'ROLE_ADMIN');

-- ROLE <> CUSTOMER
INSERT INTO customer_role(customer_id, role_id)
VALUES ('b2212e0f-8124-44ce-a8d6-31ac5cfb75cb', 'df662349-8ac1-4e4e-8abf-1297be4bfad0'),
       ('b2212e0f-8124-44ce-a8d6-31ac5cfb75cb', 'cd6bc1f2-00b2-4b9f-9b12-ab51d970168b');

-- ADDRESS
insert into address(zip_code, customer_id, id, additional_info, city, street, street_number)
VALUES (123, 'ec63f4d5-6964-49a9-822c-36b7349efc3a', 'a5ad0a45-d3c9-4867-9b06-6e6d27d20e2d', '', 'City1', 'Street1',
        '11A'),
       (321, 'cbc22041-a58b-431f-8d26-7355fab4cb65', 'e2cb5e73-3637-4614-9067-49fde8406969', '', 'City2', 'Street2',
        '22A');

-- BASKET
insert into basket(is_complete, customer_id, id)
values (false, 'cbc22041-a58b-431f-8d26-7355fab4cb65', '39332501-f73c-4788-a690-bd05364870c0'),
       (true, 'cbc22041-a58b-431f-8d26-7355fab4cb65', '70c55042-f82d-4689-9a39-6679ac56e265');

-- ORDER TRANSACTION
insert into order_transaction(submission_time, basket_id, customer_id, id)
VALUES ('2023-10-01 15:30:45.123456', '39332501-f73c-4788-a690-bd05364870c0', 'cbc22041-a58b-431f-8d26-7355fab4cb65',
        '9cd7ccd4-a211-4e83-b506-4a329ad383a8'),
       ('2013-11-11 11:11:11.923000', '70c55042-f82d-4689-9a39-6679ac56e265', 'cbc22041-a58b-431f-8d26-7355fab4cb65',
        '53abb680-893e-489a-84ba-0f1e5771db00');

-- PRODUCT CATEGORY
insert into product_category(id, department, name)
VALUES ('ee508dd2-0589-42ba-bd72-462e63213a02', 'Fiction', 'Fantasy'),
       ('c03f184d-c923-4c70-b862-5156db75bf51', 'Science Fiction', 'Time Travel');

-- SUPPLIER
insert into supplier(id, description, name)
values ('f6a7810b-98bd-4688-9cc8-b168ce4ec3b1', 'American author', 'Brandon Sanderson'),
       ('882a7827-b9cd-4046-9d89-6b1504c902a2', 'American writer', 'Isaac Asimov');

-- PRODUCT
insert into product(price, id, currency, description, name)
VALUES (12.2, '5b338991-267d-4cff-b121-5161bdf8843b', 'PLN', 'The Way of Kings is an epic fantasy novel', 'The Way of Kings'),
       (10.2, 'a3e147f5-08c1-43c7-b81d-15be479ee466', 'PLN', 'Foundation is a science fiction novel', 'Foundation');

-- PRODUCT CATEGORY <> PRODUCT
insert into product_category_product(product_category_id, product_id)
values ('ee508dd2-0589-42ba-bd72-462e63213a02', '5b338991-267d-4cff-b121-5161bdf8843b'),
       ('c03f184d-c923-4c70-b862-5156db75bf51', 'a3e147f5-08c1-43c7-b81d-15be479ee466');

-- SUPPLIER <> PRODUCT
insert into supplier_product(product_id, supplier_id)
values ('5b338991-267d-4cff-b121-5161bdf8843b', 'f6a7810b-98bd-4688-9cc8-b168ce4ec3b1'),
       ('a3e147f5-08c1-43c7-b81d-15be479ee466', '882a7827-b9cd-4046-9d89-6b1504c902a2');

-- BASKET <> PRODUCT
insert into basket_product(basket_id, product_id)
VALUES ('39332501-f73c-4788-a690-bd05364870c0', '5b338991-267d-4cff-b121-5161bdf8843b'),
       ('39332501-f73c-4788-a690-bd05364870c0', '5b338991-267d-4cff-b121-5161bdf8843b'),
       ('39332501-f73c-4788-a690-bd05364870c0', '5b338991-267d-4cff-b121-5161bdf8843b'),
       ('70c55042-f82d-4689-9a39-6679ac56e265', '5b338991-267d-4cff-b121-5161bdf8843b');

-- REFRESH TOKEN
insert into refresh_token(expiry_date, customer_id, id)
VALUES ('2023-11-25 23:57:56.793226 +00:00', 'ec63f4d5-6964-49a9-822c-36b7349efc3a',
        'f81a56a8-3363-431e-b875-01185791d39f');