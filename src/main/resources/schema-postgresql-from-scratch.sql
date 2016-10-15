/*
 * PostgreSQL schema for initializing our database schema.
 * Based on http://docs.spring.io/spring-security/site/docs/current/reference/html/appendix-schema.html
 */
drop table if exists authorities cascade;
drop table if exists users cascade;

create table users (
  id serial not null primary key,
  username citext not null unique,
  password varchar(200) not null,
  enabled boolean not null,
  created timestamp not null default current_timestamp
);

create table authorities (
  id integer not null,
  authority varchar(50) not null,
  constraint fk_authorities_users foreign key(id) references users(id)
);
create unique index ix_auth_id on authorities (id,authority);
