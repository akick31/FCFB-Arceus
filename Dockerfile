# Use a lightweight JDK 17 image
FROM openjdk:17-jdk-slim

# Install font-related packages for Java to handle fonts
RUN apt-get update && apt-get install -y \
    fontconfig \
    fonts-dejavu-core \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the Gradle build directory into the container
COPY ./build/libs/*.jar app.jar

# Copy SSL certificates
COPY /etc/letsencrypt/live/fakecollegefootball.com/fullchain.pem /app/certs/fullchain.pem
COPY /etc/letsencrypt/live/fakecollegefootball.com/privkey.pem /app/certs/privkey.pem

# Expose the application port (based on your project configuration)
EXPOSE 1212

# Run the Kotlin Spring Boot application with headless mode enabled
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]
