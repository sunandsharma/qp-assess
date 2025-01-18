# Use official OpenJDK image as the base image
FROM openjdk:8-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the host to the container
COPY target/booking-service-1.0.0.jar app.jar

# Expose the port that your application will run on
EXPOSE 8080

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
