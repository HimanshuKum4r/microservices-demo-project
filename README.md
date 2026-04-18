# AI Fitness Microservices Project

This is a learning project built to explore and implement a microservices architecture using Spring Boot.
The system manages users, tracks fitness activities, and generates AI-based recommendations using event-driven communication.



## Tech Stack

* Java, Spring Boot
* Microservices Architecture (Spring Cloud)
* Spring Cloud Gateway
* Apache Kafka (event-driven communication)
* WebClient (service-to-service communication)
* Keycloak (authentication & authorization)
* Eureka Server (service discovery)
* Config Server (centralized configuration)
* PostgreSQL, MongoDB



## Services

* **User Service** – handles user registration, profile, and validation
* **Activity Service** – tracks activities and publishes events to Kafka
* **AI Service** – processes events and generates recommendations
* **API Gateway** – routes external requests to services
* **Eureka Server** – enables service discovery
* **Config Server** – manages centralized configuration



## What I Learned

* Designing and structuring microservices
* Service-to-service communication using WebClient
* Event-driven architecture with Kafka
* API Gateway routing and request handling
* Service discovery using Eureka
* Centralized configuration with Config Server
* Securing APIs with Keycloak


## Run Order

1. Config Server
2. Eureka Server
3. All Microservices
4. API Gateway


