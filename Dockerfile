# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Create a volume for temporary files
VOLUME /tmp

# Argument for the JAR file
ARG JAR_FILE=target/erwebsocket-0.0.1-SNAPSHOT.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Copy the public folder into the container
COPY public /app/public

# Set the default command to run your application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
