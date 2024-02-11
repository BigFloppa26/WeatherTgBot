FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/WeatherTgBot-0.0.2-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar", "WeatherTgBot-0.0.2-SNAPSHOT.jar"]

