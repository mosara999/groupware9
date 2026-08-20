#!/bin/bash
# docker-entrypoint-initdb.d runs *.sql files through `mysql --binary-mode`, which does not
# understand the client-side DELIMITER command that tables.sql needs for its function/procedure
# definitions. Invoke the mysql client directly here instead, which supports DELIMITER normally.
set -e

mysql -uroot -p"${MARIADB_ROOT_PASSWORD}" "${MARIADB_DATABASE}" < /docker-entrypoint-initdb.d/sql/tables.sql
mysql -uroot -p"${MARIADB_ROOT_PASSWORD}" "${MARIADB_DATABASE}" < /docker-entrypoint-initdb.d/sql/tableData.sql
