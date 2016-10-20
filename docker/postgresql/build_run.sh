#!/bin/bash
# Builds and runs our custom PostgreSQL Docker container

set -e
docker build --tag timneh-postgresql docker/postgresql
docker run --detach --publish 6666:5432 timneh-postgresql
