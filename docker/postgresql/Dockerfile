FROM postgres:9.4

#RUN apt-get update && \
#    apt-get install postgresql-contrib-9.4
COPY install_extensions.sh /docker-entrypoint-initdb.d
