/*
 * Populates a fresh version of the Timneh database for testing
 */
insert into users (id, username, password, full_name, email, enabled, created)
  values(1, 'user', 'password', 'full name', 'email@address.tld', true, current_timestamp);
insert into authorities (id, authority)
  values(1, 'USER');
insert into discussions (author_id, title)
  values (1, 'Test Discussion');
insert into comments (author_id, discussion_id, text)
  values (1, 1, 'First post!');
