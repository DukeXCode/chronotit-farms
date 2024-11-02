# Stage 1: Build the application
FROM gradle:8.0.2-jdk17 AS builder
WORKDIR /app

# Copy Gradle files to cache dependencies
COPY build.gradle.kts settings.gradle.kts /app/
COPY gradle /app/gradle

# Download dependencies
RUN gradle build -x test --no-daemon || return 0

# Copy the rest of the source code
COPY . /app

# Build the application
RUN gradle build -x test --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
