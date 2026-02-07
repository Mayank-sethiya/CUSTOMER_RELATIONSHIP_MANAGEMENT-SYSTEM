# Build Stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage - Use 'jre' instead of 'jdk' to save image space (and RAM)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar crm-app.jar

# IMPORTANT: Set Java memory limits for Render's 512MB RAM
# -Xmx300m: Limits the 'Heap' memory to 300MB
# -Xss512k: Reduces memory used per thread (Saves a lot of RAM)
# -XX:+UseSerialGC: Use a lighter garbage collector for low-RAM environments
ENV JAVA_OPTS="-Xmx300m -Xss512k -XX:+UseSerialGC"

EXPOSE 8080

# 🚀 Use the 'Shell Form' (no brackets) so $PORT and $JAVA_OPTS are correctly read
ENTRYPOINT java $JAVA_OPTS -jar crm-app.jar --server.port=$PORT
