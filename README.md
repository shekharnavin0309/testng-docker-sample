# testng-docker-sample

A sample Maven/TestNG project configured to run tests inside Docker and integrate with Jenkins CI.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Clone the Repository](#clone-the-repository)
  - [Run Tests Locally](#run-tests-locally)
  - [Run Tests in Docker](#run-tests-in-docker)
- [Dockerfile Explanation](#dockerfile-explanation)
- [Jenkins Pipeline Integration](#jenkins-pipeline-integration)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Features

- **Maven-based** build and dependency management
- **TestNG** test suite configuration
- **Dockerized** test execution for consistent environments
- **Jenkins Pipeline** ready with a `Jenkinsfile` for CI/CD

## Prerequisites

- Java JDK 17 installed
- Maven 3.6+ (for local runs)
- Docker 20.10+ (for containerized runs)
- Jenkins with Docker enabled (for CI)

## Project Structure

```
testng-docker-sample/
├── Dockerfile
├── Jenkinsfile
├── README.md
├── pom.xml
├── testng.xml
└── src
    └── test
        └── java
            └── com
                └── example
                    └── SampleTest.java
```

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/shekharnavin0309/testng-docker-sample.git
cd testng-docker-sample
```

### Run Tests Locally

Execute the TestNG suite using Maven:

```bash
mvn clean test
```

### Run Tests in Docker

Use the official Maven Docker image:

```bash
docker run --rm \
  -v "$PWD":/app \
  -v "$HOME/.m2":/root/.m2 \
  -w /app \
  maven:3.8.5-openjdk-17 \
  mvn clean test
```

This will:

1. Mount the project into the container (`/app`).
2. Reuse your local Maven cache (`~/.m2`).
3. Run `mvn clean test` inside a clean Maven + Java 17 environment.

## Dockerfile Explanation

```dockerfile
FROM maven:3.8.5-openjdk-17
WORKDIR /app

# 1. Copy POM and cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline --batch-mode

# 2. Copy test sources and TestNG config
COPY src ./src
COPY testng.xml .

# 3. Default command to run tests
CMD ["mvn", "clean", "test"]
```

- **Stage 1** downloads and caches all dependencies.
- **Stage 2** copies your test code and config into the image.
- **Stage 3** defines the default test command when the container runs.

## Jenkins Pipeline Integration

A sample `Jenkinsfile` is provided for CI:

```groovy
pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test in Docker') {
      agent {
        docker {
          image 'maven:3.8.5-openjdk-17'
          args  '-v $HOME/.m2:/root/.m2'
        }
      }
      steps {
        sh 'mvn clean test'
      }
      post {
        always { junit 'target/surefire-reports/*.xml' }
      }
    }
  }

  post {
    success { echo '✅ Tests passed!'}
    failure { echo '❌ Tests failed.' }
  }
}
```

- **Docker agent** ensures tests run in a consistent container.
- **JUnit publisher** archives TestNG results for tracking.

## Troubleshooting

- **Permission issues**: Ensure your user is in the `docker` group or prefix Docker commands with `sudo`.
- **Maven source/target errors**: Confirm `pom.xml` has Java 17 compiler settings:
  ```xml
  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
  </properties>
  ```
- **Dockerfile not found**: Verify you’re in the project root where `Dockerfile` resides.

## License

This project is licensed under the MIT License. Feel free to use and modify it for your needs.

