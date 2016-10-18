/*
 * HSQLDB specific commands to be run before generic DDL is executed.
 * This allows us to use a single set of DDL between both HSQLDB and PostgreSQL.
 */
SET DATABASE SQL SYNTAX PGS TRUE;
