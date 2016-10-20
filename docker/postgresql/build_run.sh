#!/bin/bash
# Builds and runs our custom PostgreSQL Docker container

set -e
docker build --tag timneh-postgresql docker/postgresql
docker run --publish 5432:5432 --rm timneh-postgresql
