FROM openjdk:21-slim
WORKDIR /app
COPY build/libs/kafka-app-1.0.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]