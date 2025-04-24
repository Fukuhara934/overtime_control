# OpenJDKの最新版（Java 21）を使用
FROM openjdk:21-jdk-slim

# JARファイルをコンテナにコピーする
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 必要なポートを公開（Spring Boot のデフォルトポートは8080）
EXPOSE 8080

# アプリケーションを起動
ENTRYPOINT ["java", "-jar", "/app.jar"]
