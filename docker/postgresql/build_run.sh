#!/bin/bash
# Builds and runs our custom PostgreSQL Docker container

set -e

pwd=`pwd`
echo "Current directory: $pwd"
docker build --tag timneh-postgresql .
docker run --publish 5432:5432 --rm timneh-postgresql
