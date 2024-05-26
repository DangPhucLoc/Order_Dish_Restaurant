FROM openjdk:17-slim

WORKDIR /app

# Copy the Maven wrapper and configuration files first
COPY .mvn/wrapper/maven-wrapper.properties .mvn/wrapper/maven-wrapper.properties
COPY mvnw .
COPY pom.xml .

# Ensure the Maven Wrapper script has the correct permissions
RUN chmod +x mvnw

# Verify that the maven-wrapper.properties file exists and is correct
RUN cat .mvn/wrapper/maven-wrapper.properties

# Download dependencies and go offline
RUN ./mvnw dependency:go-offline

# Copy the entire project
COPY . .
# Package the application
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
CMD ["./mvnw", "spring-boot:run"]
