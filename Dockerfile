# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven build output (JAR file) to the container
COPY target/library-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]