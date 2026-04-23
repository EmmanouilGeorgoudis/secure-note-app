DROP DATABASE IF EXISTS secure_notes;
CREATE DATABASE secure_notes;
USE secure_notes;

CREATE TABLE users(
	id int auto_increment primary key,
    username varchar(100) not null unique,
    password varchar(100) not null,
    role varchar(25) not null default "USER"
);
CREATE TABLE notes(
    id int auto_increment primary key,
    user_id int not null,
    content text not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    foreign key (user_id) references users(id) on delete cascade
);