# Step 1: Build stage (Use a stable Maven image)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run stage (CHANGE THIS LINE)
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar crm-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "crm-app.jar"]
