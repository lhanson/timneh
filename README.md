# timneh [![Build Status](https://travis-ci.org/lhanson/timneh.svg?branch=master)](https://travis-ci.org/lhanson/timneh) [![codecov](https://codecov.io/gh/lhanson/timneh/branch/master/graph/badge.svg)](https://codecov.io/gh/lhanson/timneh)

A squawky, dusty discussion forum for zygodactyl cringelords

## Testing

To run all unit tests, do:

    gradle clean check

To run integration tests, do:

    gradle clean integrationTest

## Running the app

Running the application locally depends on your desired datasource.

### In-memory database

The default Spring Boot profile is configured to run an in-memory HSQLDB database. Simply run:

    gradle bootRun

A default user is created with username `user` and password `password`.

### Local PostgreSQL database

First, make sure your database is running. That's easily done with [docker](https://www.docker.com/),
see the provided Dockerfile in `docker/postgresql` for configuration which is ready to use:

    cd docker/postgresql
    docker build --tag timneh-postgresql .
    docker run --publish 5432:5432 --rm timneh-postgresql

The `local` Spring Boot profile is configured to connect to this local database, wipe existing tables, and start with a clean set of data each time:

    gradle bootRun -Dspring.profiles.active=local

Again, you can log in for testing with username `user` and password `password`

The database is initialized by platform-specific scripts activated by Spring Boot profiles.
See the Spring Boot [database initialization docs](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html#howto-initialize-a-database-using-spring-jdbc) for details.
