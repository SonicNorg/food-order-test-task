CREATE TABLE ${restaName}_ASSORTMENT
(
    food_id     SERIAL PRIMARY KEY,
    name        varchar(50) not null,
    description varchar(500) not null,
    price       real not null
);
CREATE TABLE ${restaName}_ORDERS
(
    order_id     SERIAL PRIMARY KEY,
    created      timestamp NOT NULL DEFAULT NOW()
);
CREATE TABLE ${restaName}_XREF
(
    x_id     SERIAL PRIMARY KEY,
    order_id      SERIAL NOT NULL references ${restaName}_ORDERS(order_id),
    food_id       SERIAL NOT NULL references ${restaName}_ASSORTMENT(food_id),
    count         integer NOT NULL default 1,
    UNIQUE(order_id, food_id)
);