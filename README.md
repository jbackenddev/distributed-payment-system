# Distributed Payment System

## Overview
This project demonstrates an event-driven payment processing system using Kafka and microservices.

## Architecture
- Payment Service: receives payment requests and publishes events
- Fraud Service: consumes events and evaluates fraud risk

## Technologies
- Java
- Spring Boot
- Apache Kafka
- Docker
- PostgreSQL

## Design Decisions
- Event-driven architecture for decoupling services
- Asynchronous processing to improve scalability
- Idempotency to prevent duplicate transactions

## Trade-offs
- Eventual consistency instead of strong consistency
- Increased system complexity for scalability

## How to run

```bash
docker-compose up