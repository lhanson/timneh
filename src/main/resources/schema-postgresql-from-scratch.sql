/*
 * PostgreSQL schema for initializing our database schema.
 * Based on http://docs.spring.io/spring-security/site/docs/current/reference/html/appendix-schema.html
 */
drop table if exists authorities cascade;
drop table if exists users cascade;

create table users(
  username citext not null primary key,
  password varchar(200) not null,
  enabled boolean not null);

create table authorities (
  username citext not null,
  authority varchar(50) not null,
  constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);
