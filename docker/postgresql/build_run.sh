#!/bin/bash
# Builds and runs our custom PostgreSQL Docker container.
# Takes a single argument, the port number for Postgres to listen on.

set -e
docker build --tag timneh-postgresql docker/postgresql
docker run --detach --publish $1:5432 timneh-postgresql
