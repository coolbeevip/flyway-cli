
在 `/schema/postgresql` 目录下创建数据脚本

V1.0.0.0__init.sql

```sql
CREATE TABLE test_table (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE
);
```

V1.0.0.1__add_column.sql

```sql
ALTER TABLE test_table ADD COLUMN age INT;
```

执行数据库初始化

```shell
java -jar flyway-cli-1.0.0.jar \
-jdbc_url jdbc:postgresql://127.0.0.1:5432/test_db \
-username hello \
-password 'hello' \
-location /schema/postgresql
```