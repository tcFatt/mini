drop schema if exists carpark;

create schema carpark;

use carpark;

create table user (
    user_id int not null auto_increment,
    username varchar(64) not null,
    name varchar(128) not null,
    email varchar(128) not null,
    password varchar(128) not null,
    
    primary key(user_id)
);

create table cp_info (
    car_park_id int not null auto_increment,
    car_park_no varchar(16),
    address varchar(256),
    car_park_type enum('SURFACE CAR PARK', 'BASEMENT CAR PARK', 'MULTI-STOREY CAR PARK'),
    type_of_parking_system enum('ELECTRONIC PARKING', 'COUPON PARKING'),
    short_term_parking enum('NO', 'WHOLE DAY', '7AM-10.30PM'),
    free_parking enum('NO', 'SUN & PH FR 7AM-10.30PM'),
    night_parking enum('YES', 'NO'),
    car_park_decks int,
    gantry_height double(10,2),
    car_park_basement enum('Y','N'),

    primary key(car_park_id)
);

create table favourite (
    favourite_id int not null auto_increment,
    user_id int,
    car_park_id int,
    
    primary key(favourite_id),

    constraint fk_user_id
        foreign key(user_id)
        references user(user_id),
    
    constraint fk_car_park_id
        foreign key(car_park_id)
        references cp_info(car_park_id)
);