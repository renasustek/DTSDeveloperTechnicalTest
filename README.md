Task Manager API - Backend
A simple RESTful API for managing tasks, built with Java and Spring Boot.a

Tech Stack
 Backend: Java 21, Spring Boot 3

Database: MySQL 8.0

Build Tool: Maven (or Gradle)

Containerization: Docker & Docker Composestg How to Run

1. Start the Database
This project requires Docker to be installed. The database can be started easily using the included docker-compose.yml file.u

From the project's root directory, run:

docker-compose up -d


This command will start a MySQL container in the background and make it available on port 3306.

2. Starteese steps to get the application running on your local machine.

### 1. Run the Database with Docker

The project uses a `docker-compose.yml` file to easily spin up a pre-configured MySQL database instance.

From the root directory of the project, run the following command in your terminal:

```bash
docker-compose up -d


This command will:

Download the MySQL 8.0 image if you don't have it.

Start a MySQL container named mysql_mindmap.

Create a database named test.

Expose the database on port 3306.

The -d flag runs the container in detached mode (in the background).

2. Configure the Application
The application.properties file (located in src/main/resources) should be configured to connect to the Dockerized database.

spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=renas
spring.datasource.password=ustek
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3. Build and Run the Spring Boot Application
Once the database is running, you can build and run the backend application.

Using Maven:
# Build the project and run tests
mvn clean install

# Run the application
mvn spring-boot:run

Using Gradle:
# Build the project and run tests
gradle build

# Run the application
gradle bootRun

The application will start up and connect to the database. By default, it will be accessible at http://localhost:8080.

API Endpoints
The following endpoints are available to interact with the Task API.

Method

Endpoint

Description

GET

/task/all

Retrieves all tasks.

GET

/task/get/{id}

Retrieves a single task by its UUID.

POST

/task/create

Creates a new task.

PUT

/task/update

Updates an existing task.

DELETE

/task/delete/{id}

Deletes a task by its UUID.

POST

/task/change-status/{id}

Changes the status of a task.

Example POST /task/create Body:
{
    "name": "My First Task",
    "description": "This is the description for the task.",
    "dueDate": "2025-12-31"
}

Running the Tests
To run the unit and integration tests for the project, use the following command:

Using Maven:
mvn test

Using Gradle:
gradle test
