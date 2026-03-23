# 🚀 Distributed Payment System

Production-grade distributed payment processing system built with **Java**, **Spring Boot 3**, **Kafka**, and **event-driven microservices architecture**.

Designed to handle **high throughput**, **fault tolerance**, and **real-world distributed systems challenges**, including **idempotency**, **saga orchestration**, **observability**, and **reliable event publishing**.

---

## 📌 Overview

This project simulates a real-world payment processing pipeline where transactions are asynchronously validated through a fraud detection service.

The system embraces **event-driven communication** and **eventual consistency**, ensuring resilience and scalability under failure scenarios.

---

## 🏗 Architecture

```
Client
   ↓
Payment Service
   ↓
Outbox Table → CDC Publisher
   ↓
Kafka (payment-topic)
   ↓
Fraud Service (Saga Step)
   ↓
Kafka (fraud-result)
   ↓
Payment Orchestrator (Saga)
   ↓
PostgreSQL
```

### Key Characteristics:
- Event-driven microservices
- Saga orchestration for distributed transactions
- Outbox Pattern for reliable publishing
- Strong consistency boundaries + eventual consistency

---

## 🧠 Design Decisions

### Event-Driven Architecture
Kafka enables decoupling, scalability, and asynchronous communication between services.

### Saga Orchestration
Distributed transactions are coordinated via saga orchestration, ensuring consistency across services without relying on 2PC.

### Outbox Pattern
Guarantees **atomicity between database writes and event publishing**, avoiding message loss.

### Idempotency + Pessimistic Locking
- Prevents duplicate processing
- Ensures data integrity under concurrent consumption
- Handles reprocessing safely

### Schema Registry
Event contracts are versioned and validated, ensuring compatibility between producers and consumers.

---

## 🔍 Observability

The system includes **end-to-end distributed tracing**:

- Micrometer + Spring Boot 3 Observability
- Correlation ID propagation across services
- Trace continuity across Kafka topics
- Structured logging aligned with trace context

👉 Enables full visibility of:
**Payment → Fraud → Result lifecycle**

---

## ⚠️ Challenges Solved

- Duplicate message processing (idempotency)
- Reliable event publishing (Outbox Pattern)
- Distributed transaction consistency (Saga)
- Traceability across async boundaries
- Concurrent processing with data integrity
- Event schema evolution (Schema Registry)

---

## 🔧 Engineering Highlights

- Distributed tracing with Micrometer
- Correlation ID propagation across async flows
- Saga orchestration for payment lifecycle
- Outbox Pattern with transactional guarantees
- Idempotent consumers with database safeguards
- Pessimistic locking to prevent race conditions
- Schema Registry for event evolution
- Clean separation of bounded contexts (Payment / Fraud)

---

## 🛠 Tech Stack

- **Java + Spring Boot 3**
- **Apache Kafka**
- **PostgreSQL**
- **Docker / Docker Compose**
- **Micrometer (Observability)**
- **Schema Registry (Avro/Protobuf)**
- **GitHub Actions (CI/CD automation)**

---

## 🔁 Message Flow

1. Payment is created and stored
2. Event is written to Outbox table
3. Outbox publisher sends event to Kafka
4. Fraud service consumes and evaluates
5. Fraud result is published
6. Saga orchestrator updates payment status

---

## 🔐 Reliability Guarantees

- **At-least-once delivery**
- **Idempotent processing**
- **Atomic event publishing (Outbox)**
- **Safe retries without side effects**
- **Consistency via Saga orchestration**

---

## ⚙️ CI/CD

- Automated Pull Request pipeline using **GitHub Actions**
- Continuous integration for build and validation
- Ready for extension into full deployment pipelines

---

## ▶️ Running the Project

### Prerequisites
- Docker
- Docker Compose

### Run

```bash
docker-compose up --build
```

---

## 📡 Topics

| Topic           | Description              |
|----------------|--------------------------|
| payment-topic  | Payment events           |
| fraud-result   | Fraud analysis results   |

---

## 🚧 Future Improvements

- Rate limiting (API gateway level)
- Backpressure strategies (consumer lag control)
- Circuit breakers for external dependencies
- Load testing scenarios

---

## 📚 What This Project Demonstrates

This project reflects real-world backend engineering practices:

- Distributed systems design
- Event-driven architecture
- Data consistency strategies
- Observability and tracing
- High reliability patterns used in production systems

---

## 👨‍💻 Author

**Marcos Tadeu Silva**  
Senior Backend Engineer — Distributed Systems | Kafka | AWS

---

## ⭐ Why This Matters

Modern systems demand **resilience**, **scalability**, and **observability**.

This project demonstrates how to design and implement **production-grade distributed systems**, applying patterns used by high-scale companies.
