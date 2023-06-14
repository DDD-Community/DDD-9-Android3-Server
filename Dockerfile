FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY build/libs/github-action-EB-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]