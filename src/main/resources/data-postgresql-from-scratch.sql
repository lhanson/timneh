/*
 * Populates a fresh version of the Timneh database for testing
 */
insert into users values('user', 'password', true);
insert into authorities values('user', 'USER');
