/*
 * PostgreSQL schema for initializing our database schema.
 * Based on http://docs.spring.io/spring-security/site/docs/current/reference/html/appendix-schema.html
 */
drop table if exists discussions cascade;
drop table if exists authorities cascade;
drop table if exists users cascade;

create table users (
  id serial not null primary key,
  username citext not null unique,
  password varchar(200) not null,
  full_name varchar(200) not null,
  email citext not null unique,
  enabled boolean default true not null,
  created timestamp default current_timestamp not null
);

create table authorities (
  id integer not null,
  authority varchar(50) not null,
  constraint fk_authorities_users foreign key(id) references users(id)
);
create unique index ix_auth_id on authorities (id,authority);

create table discussions (
  id serial not null primary key,
  author_id integer not null,
  title varchar(50) not null,
  created timestamp default current_timestamp,
  modified timestamp,
  constraint fk_discussions_author foreign key(author_id) references users(id)
);
