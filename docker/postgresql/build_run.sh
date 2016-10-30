#!/bin/bash
# Builds and runs our custom PostgreSQL Docker container.
# Takes a single argument, the port number for Postgres to listen on.

set -e
SCRIPTDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Use port 5432 unless specified
postgres_port="5432"
if [ -n "$1" ]; then
	postgres_port="$1"
fi

docker build --tag timneh-postgresql $SCRIPTDIR
docker run --detach --publish ${postgres_port}:5432 --name timneh-postgresql timneh-postgresql
