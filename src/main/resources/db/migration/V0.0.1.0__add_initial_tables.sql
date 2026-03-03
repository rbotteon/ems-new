create table employees
(
    id bigint identity not null,
    name varchar(255) not null,
    email varchar(255),
    departement varchar(255),
    salary  double precision not null,
    hiredate date not null default sysdate,
    primary key (id)
);