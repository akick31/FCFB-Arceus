# Use a lightweight JDK 17 image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the Gradle build directory into the container
COPY ./build/libs/*.jar app.jar

# Expose the application port (based on your project configuration)
EXPOSE 1212

# Run the Kotlin Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
