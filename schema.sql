drop schema if exists usersdb;

create schema usersdb;

use usersdb;

create table users (
    user_id int not null auto_increment
    username varchar(128) not null,
    name varchar(128) not null,
    phone varchar(16), 
    dob date,
    password varchar(128) not null,
    
    primary key(username)
);