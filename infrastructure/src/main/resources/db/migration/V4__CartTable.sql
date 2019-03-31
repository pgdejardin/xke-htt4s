create table cart
(
    id       uuid primary key,
    user_id  uuid not null,
    items    json,
    price    float default 0,
    discount float default 0
);
