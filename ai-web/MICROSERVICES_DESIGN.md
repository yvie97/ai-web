# Microservices Architecture Design for ai-web

This document outlines a proposed microservices architecture for the `ai-web` project. The goal is to break down the existing monolith into a set of independently deployable, scalable, and maintainable services.

## 1. Proposed Microservices

The monolithic application's responsibilities will be divided into the following distinct services:

#### 1. `ai-service`
*   **Responsibilities**:
    *   Handles all AI-powered interactions, including chat sessions and processing user queries.
    *   Manages vector embeddings for documents and performs similarity searches.
    *   Integrates with external AI models (like DeepSeek).
*   **Exposed API (Examples)**: `POST /api/v1/chat`, `GET /api/v1/chat/history/{sessionId}`

#### 2. `document-service`
*   **Responsibilities**:
    *   Manages the lifecycle of documents, primarily PDFs.
    *   Handles file uploads, storage (e.g., on a local disk or cloud storage like S3), and retrieval.
    *   Extracts text content from documents for processing.
*   **Exposed API (Examples)**: `POST /api/v1/documents/upload`, `GET /api/v1/documents/{docId}`

#### 3. `core-business-service`
*   **Responsibilities**:
    *   Manages the core business domain logic: courses, schools, and reservations.
    *   Handles all CRUD (Create, Read, Update, Delete) operations for these entities.
    *   Manages user-facing features like customer service and games.
*   **Exposed API (Examples)**: `GET /api/v1/courses`, `POST /api/v1/reservations`

## 2. Architecture Design

We will adopt a standard cloud-native architecture pattern that emphasizes scalability and resilience.

#### System Diagram

```
+----------------+      +---------------+      +----------------------+
|   Client App   |----->|  API Gateway  |----->|   Service Registry   |
| (Web/Mobile)   |      +---------------+      +----------------------+
+----------------+             |
                               |
            +------------------+------------------+
            |                  |                  |
+-----------------------+ +---------------------+ +--------------------+
|     ai-service        | |  document-service   | | core-business-svc  |
| (Manages Chat, AI)    | | (Manages Files)     | | (Manages Courses)  |
+-----------------------+ +---------------------+ +--------------------+
            |                  |                  |
            |                  |                  |
 +--------------------+  +-------------------+  +-------------------+
 | Vector DB (Chroma) |  | File Store (S3)   |  |  SQL DB (MySQL)   |
 +--------------------+  +-------------------+  +-------------------+
```

#### Communication Patterns

1.  **API Gateway**: All external client requests first go through an API Gateway. The gateway is responsible for routing requests to the appropriate microservice, as well as handling cross-cutting concerns like authentication, rate limiting, and logging.

2.  **Service-to-Service Communication**:
    *   **Synchronous**: For direct request/response interactions (e.g., `core-business-service` asking `ai-service` for a course summary), services will communicate via RESTful APIs using an HTTP client.
    *   **Asynchronous**: For event-driven workflows (e.g., when a new document is uploaded to `document-service`, it notifies `ai-service` to begin processing it), services will communicate via a message queue.

## 3. Technology Stack

The technology stack is chosen to align with the existing project's foundation in Spring Boot while embracing modern cloud-native standards.

| Category                  | Technology                                     | Why We Chose It                                                                                                                              |
| ------------------------- | ---------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| **Backend Framework**     | **Spring Boot 3 & Spring Cloud**               | The project is already built on Spring Boot. Spring Cloud provides a mature, comprehensive suite of tools for building microservices (gateway, service discovery, etc.). |
| **API Gateway**           | **Spring Cloud Gateway**                       | A modern, non-blocking, and highly integrated solution within the Spring ecosystem for routing and filtering API requests.                   |
| **Service Registry**      | **Netflix Eureka**                             | Tightly integrated with Spring Cloud, making service discovery straightforward and easy to configure for dynamic service registration.       |
| **Async Communication**   | **RabbitMQ**                                   | A lightweight and reliable message broker that is perfect for event-driven communication and decoupling services. Simpler to set up than Kafka for this scale. |
| **Databases**             | 1. **PostgreSQL / MySQL** <br> 2. **ChromaDB / Milvus** | 1. **Relational DB**: For the structured data in the `core-business-service`. <br> 2. **Vector DB**: Essential for the `ai-service` to perform efficient similarity searches. |
| **Containerization**      | **Docker & Docker Compose**                    | The project already includes a Dockerfile. Docker ensures consistent environments, and Docker Compose simplifies multi-container local development. |
