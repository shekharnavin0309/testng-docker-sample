# testng-docker-sample/Dockerfile
FROM maven:3.8.5-openjdk-17

WORKDIR /app

# Copy only the POM first to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline --batch-mode

# Copy sources and TestNG suite descriptor
COPY src ./src
COPY testng.xml .

# Run the tests
CMD ["mvn", "clean", "test"]
