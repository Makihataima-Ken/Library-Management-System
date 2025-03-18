# Library-Management-System

## Overview

The Library Management System is a web-based application that allows patrons to borrow and return books. The application supports the management of books, borrowing records, and patrons.

---

## Prerequisites

Before you begin, ensure that you have the following installed:

- **Java 23**
- **Maven/Gradle** (for dependency management and project building)
- **MySQL/PostgreSQL** or any relational database for persistence
- **IDE**: IntelliJ IDEA, Eclipse, or any Java-supported IDE

---

## Getting Started

Follow these steps to get your development environment set up:

### 1. Clone the Repository

Clone this repository to your local machine

### 2. Install Dependencies

If you are using Maven, run the following command to install dependencies:
mvn install
For Gradle:
./gradlew build

### 3.  Configure the Database

spring.application.name=demo

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

### 4. Running the Application

To run the application in development mode:
mvn spring-boot:run

## Interacting with API Endpoints

The following are the API endpoints available in the application. If authentication is required, the documentation for each endpoint will specify it.

Base URL
The base URL of the API is:
http://localhost:8080/api

## Available Endpoints

### 1. GET /api/books
Description: Retrieves all books in the library.
Method: GET
Response:
200 OK: A list of books in the library.

### 2. POST /api/books
Description: Creates a new book record.
Method: POST
Request Body:
json
Copy
Edit
{
  "title": "New Book Title",
  "author": "New Author",
  "publicationYear": 2021,
  "isbn": "123456789"
}
Response:
201 Created: Book created successfully.

### 3. POST /api/borrow/{bookId}/patron/{patronId}
Description: Borrow a book for a patron.
Method: POST
Response:
200 OK: Book borrowed successfully.
400 Bad Request: If the book is already borrowed.

### 4. PUT /api/return/{bookId}/patron/{patronId}
Description: Return a borrowed book for a patron.
Method: PUT
Response:
200 OK: Book returned successfully.
