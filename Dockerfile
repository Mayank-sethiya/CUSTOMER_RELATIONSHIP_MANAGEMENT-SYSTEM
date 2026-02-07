# Build Stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage - Use 'jre' to save RAM and disk space
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar crm-app.jar

# IMPORTANT: Set memory limits for Render's 512MB RAM
# -Xmx300m: Prevents Java from using more than 300MB of RAM
ENV JAVA_OPTS="-Xmx300m -Xss512k -XX:+UseSerialGC"

EXPOSE 8080

# 🚀 Use Shell Form (No brackets) so $PORT and $JAVA_OPTS are correctly read
ENTRYPOINT java $JAVA_OPTS -jar crm-app.jar --server.port=$PORT
