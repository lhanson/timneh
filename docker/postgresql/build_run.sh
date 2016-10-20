#!/bin/bash
# Builds and runs our custom PostgreSQL Docker container

set -e
docker build --tag timneh-postgresql docker/postgresql
docker run --publish 9876:5432 --rm timneh-postgresql
