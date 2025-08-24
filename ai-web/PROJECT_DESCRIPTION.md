# Project: ai-web

This document provides a detailed analysis of the `ai-web` project structure.

## Project Overview

`ai-web` is a Java-based web application built using the Spring Boot framework and managed with Maven. Based on the file structure and dependencies, the application includes functionalities for AI-powered chat, PDF interaction, and a course/school management system. It is designed to be deployed using Docker.

## High-Level Architecture

The project follows a standard multi-layered architecture common in Spring Boot applications:

-   **Controller Layer**: Handles incoming HTTP requests and delegates to the service layer.
-   **Service Layer**: Contains the core business logic.
-   **Data Access Layer**: Manages data persistence using MyBatis mappers and repositories.
-   **Entity/Model Layer**: Defines the data structures (PO, VO, Query objects).

## Key Technologies

-   **Backend**: Java, Spring Boot
-   **Build Tool**: Maven
-   **Data Access**: MyBatis
-   **Deployment**: Docker

## Directory and File Breakdown

### Root Directory

-   `Dockerfile`: Configuration file to build a Docker image for the application, enabling containerized deployment.
-   `HELP.md`: A Markdown file likely containing project documentation or setup instructions.
-   `chat-pdf.json`, `chat-pdf.properties`: Configuration files, possibly for a chat or PDF processing feature.
-   `mvnw`, `mvnw.cmd`: Maven wrapper scripts, allowing the project to be built without a system-wide Maven installation.
-   `pom.xml`: The Maven Project Object Model file. It defines project dependencies, build configurations, and plugins.

### `src/main/java/com/yvie/ai`

This is the root package for the application's Java source code.

-   **`YvieAiApplication.java`**: The main entry point of the Spring Boot application.
-   **`/config`**: Contains Spring configuration classes.
    -   `CommonConfiguration.java`: For common, application-wide beans.
    -   `MvcConfiguration.java`: For web-related configurations like interceptors and view resolvers.
-   **`/constants`**:
    -   `SystemConstants.java`: Defines application-wide constants.
-   **`/controller`**: Handles API endpoints.
    -   `ChatController.java`: Endpoints for chat functionalities.
    -   `ChatHistoryController.java`: Endpoints for managing chat history.
    -   `CustomerServiceController.java`: Endpoints related to customer service features.
    -   `GameController.java`: Endpoints for game-related features.
    -   `PdfController.java`: Endpoints for PDF upload, processing, or interaction.
-   **`/entity`**: Defines the data models.
    -   `/po`: Plain Old Java Objects (Persistent Objects) that map to database tables (`Course`, `School`, etc.).
    -   `/query`: Objects used to encapsulate request query parameters (`CourseQuery.java`).
    -   `/vo`: View Objects, tailored for the data sent to the client/frontend (`MessageVO`, `Result`).
-   **`/mapper`**: MyBatis mapper interfaces for database operations.
-   **`/repository`**: Data access layer components.
    -   `ChatHistoryRepository.java`: Interface for chat history persistence.
    -   `InMemoryChatHistoryRepository.java`: An in-memory implementation of chat history, useful for testing or simple use cases.
    -   `FileRepository.java`, `LocalPdfFileRepository.java`: Abstractions for file storage, specifically for PDFs.
-   **`/service`**: Contains business logic.
    -   Interfaces (`ICourseService`, etc.) define the contracts.
    -   `/impl` contains the concrete implementations of the service interfaces.
-   **`/tools`**:
    -   `CourseTools.java`: Contains tool or utility methods specific to course-related logic.
-   **`/utils`**:
    -   `VectorDistanceUtils.java`: Utility for vector calculations, likely for AI features like semantic search or embeddings.

### `src/main/resources`

Contains non-Java resources for the application.

-   `application.yaml`: The primary configuration file for the Spring Boot application (database connection, server port, etc.).
-   `/mapper`: Contains the MyBatis XML files with SQL queries corresponding to the mapper interfaces.
-   `/static`: For static web assets like CSS, JavaScript, and images.
-   `/templates`: For server-side rendering templates (e.g., Thymeleaf, Freemarker).

### `src/test/java`

Contains unit and integration tests for the application.

-   `YvieAiApplicationTests.java`: A sample test class for the Spring Boot application context.
