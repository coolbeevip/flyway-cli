# Flyway CLI

一个用于执行 Flyway 数据库迁移的轻量命令行工具。

运行环境：`JDK 21`

## 快速开始

### 1. 下载代码

```shell
git clone https://github.com/coolbeevip/flyway-cli.git
cd flyway-cli
```

### 2. 编译、测试并打包

```shell
./mvnw -B package
```

### 3. 查看帮助

```shell
java -jar target/flyway-cli-1.1.0.jar --help
```

### 4. 执行数据库脚本升级

#### SQLite

```shell
java -jar target/flyway-cli-1.1.0.jar \
  -jdbc_url jdbc:sqlite:/data/sqlite/test.db \
  -username "" \
  -password "" \
  -location /data/migrations/sqlite
```

#### MySQL

```shell
export FLYWAY_DB_PASSWORD=hello
java -jar target/flyway-cli-1.1.0.jar \
  -jdbc_url jdbc:mysql://127.0.0.1:3306/test_db \
  -table test_schema_version \
  -username hello \
  -password_env FLYWAY_DB_PASSWORD \
  -location /data/migrations/mysql
```

#### PostgreSQL

```shell
export FLYWAY_DB_PASSWORD=hello
java -jar target/flyway-cli-1.1.0.jar \
  -jdbc_url jdbc:postgresql://127.0.0.1:5432/test_db \
  -table test_schema_version \
  -username hello \
  -password_env FLYWAY_DB_PASSWORD \
  -location /data/migrations/postgresql
```

## 参数说明

```text
-h|-help|--help           显示帮助
-baseline_on_migrate      显式开启 baselineOnMigrate
-baseline_version         仅在开启 baselineOnMigrate 时生效，默认 0.0.0
-password_env             从环境变量读取数据库密码
```

## Docker

```shell
docker run --rm \
  -e FLYWAY_DB_PASSWORD=hello \
  -v "/data/migrations/postgresql:/work/schema:ro" \
  coolbeevip/flyway-cli:latest \
  -jdbc_url jdbc:postgresql://host.docker.internal:5432/test_db \
  -table test_schema_version \
  -username hello \
  -password_env FLYWAY_DB_PASSWORD \
  -location /work/schema
```

## 测试示例

仓库中的示例 SQL 位于 `src/test/resources/schema/`，仅用于测试。

本地演示时可以直接复用：

```shell
export FLYWAY_DB_PASSWORD=hello
java -jar target/flyway-cli-1.1.0.jar \
  -jdbc_url jdbc:postgresql://127.0.0.1:5432/test_db \
  -table test_schema_version \
  -username hello \
  -password_env FLYWAY_DB_PASSWORD \
  -location src/test/resources/schema/postgresql
```
