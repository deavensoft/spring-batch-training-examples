create or replace table product
(
    product_id int not null,
    name varchar(200) not null,
    price decimal null,
    unit int null,
    description varchar(4000) null,
    constraint product_pk
    primary key (product_id)
);

INSERT INTO product (product_id, name, price, unit, description)
VALUES (101, 'S10', 500, 2, 'Samsung cell phone');

INSERT INTO product (product_id, name, price, unit, description)
VALUES (102, 'HP ZBook', 2100, 10, 'HP Laptop');

INSERT INTO product (product_id, name, price, unit, description)
VALUES (103, 'Jabra Speak 510', 150, 3, 'Communication device');

INSERT INTO product (product_id, name, price, unit, description)
VALUES (104, 'Logitech', 30, 90, 'Headphones');
