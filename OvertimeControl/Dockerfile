# ビルドステージ：MavenでSpring Bootアプリをビルド
FROM maven:3.8.6 AS build


WORKDIR /app

# POMファイルとソースをコピー
COPY pom.xml .
COPY src ./src

# JARファイルを生成（テストはスキップして高速化）
RUN mvn clean package -DskipTests

# 実行ステージ：軽量JDKでアプリを起動
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# 上でビルドした JAR をコピー（↓名前はこれに合わせてます）
COPY --from=build /app/target/overtime_control-0.0.1-SNAPSHOT.jar app.jar

# Spring Boot アプリを起動
ENTRYPOINT ["java", "-jar", "app.jar"]
