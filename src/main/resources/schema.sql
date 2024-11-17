drop table if exists film_likes;
drop table if exists film_genres;
drop table if exists genres;
drop table if exists films;
drop table if exists friends;
drop table if exists rating_mpa;
drop table if exists users;

create table if not exists users(
	id 			bigint generated always as identity primary key,
	name 		varchar(128) not null,
	email 		varchar(255) not null,
	login 		varchar(128) not null,
	birthday	date
);

create table if not exists friends(
	id 			bigint generated always as identity primary key,
	users_id	bigint references users(id),
	friends_id 	bigint references users(id),
	is_approved	boolean default false
);

create table if not exists rating_mpa(
	id 		int primary key,
	name	varchar(50) not null
);

create table if not exists films(
	id 				bigint generated always as identity primary key,
	name 			varchar(128) not null,
	description 	varchar(200),
	release_date 	date,
	duration 		int,
	rating_mpa_id   int references rating_mpa(id)
);

create table if not exists genres(
	id		int primary key,
	name	varchar(50) not null
);

create table if not exists film_genres(
	id 			bigint generated always as identity primary key,
	films_id	bigint references films(id),
	genres_id	int references genres(id)
);

create table if not exists film_likes(
	id 			bigint generated always as identity primary key,
	films_id	bigint references films(id),
	users_id	bigint references users(id)
);