# wallet-application


## Overview

This application is built using Java Spring Boot and includes Swagger for API documentation.

## Technologies Used

- **Java**: 21
- **Spring Boot**: 3.x
- **Spring Security**: For authentication and authorization
- **Swagger/OpenAPI**: For API documentation
- **Postgres database**  for database
- **Maven**: For project management and build
- **JUnit/Mockito**: For testing
- **Testcontainers**: For integration testing with Docker containers

## Prerequisites

- JDK 21
- Maven 3.6 or later
- Docker (required for Testcontainers)

## Building the Application

1. Clone the repository:


```bash
bash  bash  git clone https://github.com/Kaykay007/wallet-application.git
cd wallet-application
```
2. Build the application using Maven:
```bash
bash mvn clean install
```

3. Testing the Application
To run the tests, execute the following command:
```bash
bash mvn test
```

This will execute all unit tests and integration tests defined in the project. 
Make sure Docker is running for Testcontainers to work properly.

4. Accessing the Application
API Documentation: [http://localhost:8080/swagger-ui.html]


