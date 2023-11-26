-- CUSTOMER
INSERT INTO customer(is_deleted, submission_time, id, email, name, password)
VALUES (true, '2013-10-12', 'b2212e0f-8124-44ce-a8d6-31ac5cfb75cb', 'kapi@wp.pl', 'kapi', 'kapi'),
       (false, '2013-10-12', 'ec63f4d5-6964-49a9-822c-36b7349efc3a', 'mati@wp.pl', 'mati', 'mati'),
       (false, '2013-10-12', 'cbc22041-a58b-431f-8d26-7355fab4cb65', 'tomi@wp.pl', 'tomi', 'tomi');

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
VALUES ('ee508dd2-0589-42ba-bd72-462e63213a02', 'Department', 'Name'),
       ('c03f184d-c923-4c70-b862-5156db75bf51', 'Department', 'Name');

-- SUPPLIER
insert into supplier(id, description, name)
values ('f6a7810b-98bd-4688-9cc8-b168ce4ec3b1', 'Description', 'Name'),
       ('882a7827-b9cd-4046-9d89-6b1504c902a2', 'Description', 'Name');

-- PRODUCT
insert into product(price, id, currency, description, name)
VALUES (12.2, '5b338991-267d-4cff-b121-5161bdf8843b', 'PLN', 'Description', 'Name'),
       (10.2, 'a3e147f5-08c1-43c7-b81d-15be479ee466', 'PLN', 'Description', 'Name');

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