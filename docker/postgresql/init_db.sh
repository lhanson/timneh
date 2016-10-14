#!/bin/bash

# Add database and user for Timneh
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER timneh;
    CREATE DATABASE timneh;
    GRANT ALL PRIVILEGES ON DATABASE timneh TO timneh;
EOSQL

# Install citext module for case-insensitive column type
# (https://www.postgresql.org/docs/9.4/static/citext.html, https://github.com/theory/citext)
psql -q timneh -U "$POSTGRES_USER" -c "CREATE EXTENSION citext;"
