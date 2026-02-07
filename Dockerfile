# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage - Use a stable Java image
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar crm-app.jar
EXPOSE 8080
# 👈 This line below is the key. It passes the Render PORT to Spring Boot.
ENTRYPOINT ["java", "-jar", "crm-app.jar", "--server.port=${PORT}"]
