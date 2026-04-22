# syntax=docker/dockerfile:1.7
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -q -DskipTests dependency:go-offline

COPY src src
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-alpine-3.23

WORKDIR /app

COPY --from=builder /workspace/target/flyway-cli-*.jar /app/flyway-cli.jar

ENTRYPOINT ["java", "-jar", "/app/flyway-cli.jar"]
