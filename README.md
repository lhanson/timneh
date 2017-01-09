# timneh [![Build Status](https://travis-ci.org/lhanson/timneh.svg?branch=master)](https://travis-ci.org/lhanson/timneh) [![codecov](https://codecov.io/gh/lhanson/timneh/branch/master/graph/badge.svg)](https://codecov.io/gh/lhanson/timneh) [![Dependency Status](https://www.versioneye.com/user/projects/58012e8fa23d520045b212c5/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58012e8fa23d520045b212c5) [![Dependency Status](https://dependencyci.com/github/lhanson/timneh/badge)](https://dependencyci.com/github/lhanson/timneh)


A squawky, dusty discussion forum for zygodactyl cringelords

## Testing

To run all unit tests, do:

    gradle clean check

To run integration tests, do:

    gradle clean integrationTest

## Running the app

Running the application locally depends on your desired datasource. For testing or development, the default
configuration is fine; however, if deploying to serve real user data you *MUST* provide your own `application.yml`
with a unique private `jwt.token.secret` string which will be used to sign all user authentication tokens.

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

## Authentication

Timneh uses [JSON Web Tokens](https://jwt.io/) for stateless client authentication. The only database lookup
required is upon initial authentication; subsequent state is stored in the token itself, which is presented
upon each client request and validated by the server.

A client makes an initial request to `/login` using [HTTP Basic Authentication](https://tools.ietf.org/html/rfc2617).
Upon successful authentication, the server will return the token in the body of the response. Subsequent
client requests include an `Authorization` header using the `Bearer` schema:

    Authorization: Bearer <token>

## API

For comprehensive API documentation generated right from the source, check out the interactive
SwaggerUI documentation at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html).
Click the 'Authorize' button at the top of the page to authenticate using Basic auth for each request.

As an overview, here's a brief listing of some API endpoints available, and examples to invoke them with `curl`:

    * POST /login (see above)
      curl -u user:password localhost/login
      
    * GET /discussions
      curl --header "Authorization: Bearer [TOKEN]" localhost/discussions/1
      
    * POST /discussions
      curl --header "Authorization: Bearer [TOKEN]" -X POST -H "Content-Type: application/json" -d 'NEW TOPIC' localhost/discussions
    
    * GET /comments (for discussion 1)
      curl --header "Authorization: Bearer [TOKEN]" localhost/comments/1
      
    * POST /comments (for dicussion 1)
      curl --header "Authorization: Bearer [TOKEN]" -X POST -H "Content-Type: application/json" -d 'First post...' localhost/comments/1
