FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY build/libs/buyornot-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]