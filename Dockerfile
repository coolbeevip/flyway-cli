FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw -B -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /workspace/target/flyway-cli-*.jar /app/flyway-cli.jar

ENTRYPOINT ["java", "-jar", "/app/flyway-cli.jar"]
