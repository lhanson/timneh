/*
 * Populates a fresh version of the Timneh database for testing
 */
insert into users values(1, 'user', 'password', 'full name', 'email@address.tld', true, current_timestamp);
insert into authorities values(1, 'USER');
insert into discussions values (1, 1, 'Test Discussion', current_timestamp, null)
