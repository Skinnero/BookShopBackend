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

