#!/bin/bash

# Install citext module for case-insensitive column type
# (https://www.postgresql.org/docs/9.4/static/citext.html, https://github.com/theory/citext)
psql -q postgres -U postgres -c "CREATE EXTENSION citext;"
