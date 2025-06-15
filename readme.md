# TicketPlaza Java API

## Overview
TicketPlaza Java API is a backend service built using a **Domain-Driven Design (DDD) Monolithic Structure**.

---

## DDD Monolithic Structure

### Structure Overview
The DDD layers are structured as follows (as depicted in the architecture diagram):

- **ticketplaza_domain**: The core of the application, containing business logic, entities, and domain services. This layer is independent of external frameworks and infrastructure.
- **ticketplaza_application**: Orchestrates the use cases and coordinates the domain logic with external systems (e.g., APIs, databases).
- **ticketplaza_controller**: Handles external communication, such as HTTP API.
- **ticketplaza_infrastructure**: Manages external dependencies like databases (MySQL), caching (Redis), message queues (RabbitMQ), and external services (AI censor service).
- **ticketplaza_start**: To start server.

### Purpose of DDD Structure
- **Separation of Concerns**: Each layer has a distinct responsibility, making the codebase easier to maintain and test.
- **Business Focus**: The domain layer ensures that business rules are central to the application, independent of infrastructure concerns.
- **Scalability**: While the application is monolithic, the layered structure allows for easier refactoring into microservices if needed.
- **Maintainability**: Changes in one layer (e.g., switching databases) have minimal impact on other layers.

---