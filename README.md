# 🚀 BTG Pactual MS - Backend Service

Backend desarrollado en **Java 21 + Spring Boot 3**, orientado a arquitectura moderna con autenticación JWT, documentación OpenAPI y base de datos MongoDB Atlas.

---

## 🧠 Arquitectura del Proyecto

Este microservicio está construido bajo principios de:

- Arquitectura limpia (Clean Architecture)
- Separación por capas (Controller / Service / Repository)
- Principios SOLID
- Seguridad basada en JWT (falta confg var entorno)
- Documentación automática con Swagger/OpenAPI
- Despliegue en AWS (CloudFormation) (cuenta de aws cerrada, falta por desplegar)

---

## 🛠️ Tecnologías

- ☕ Java 21
- 🌱 Spring Boot 3.2.x
- 🍃 Spring Data MongoDB (Atlas)
- 🔐 Spring Security + JWT
- 📄 SpringDoc OpenAPI (Swagger)
- 🧪 JUnit 5
- 🧰 Gradle Kotlin DSL
- ☁️ AWS CloudFormation
- 🧩 Lombok

---

## 📦 Estructura del Proyecto

src/main/java/com/ceiba/btg

├── config

├── controller

├── dto

├── exception

├── repository

├── model

├── security

├── service


---

## ⚙️ Requisitos

- Java 21
- Gradle 8+
- MongoDB Atlas
- AWS CLI (opcional)

---

## 🚀 Ejecución Local

./gradlew clean bootRun

http://localhost:8080

---

## 📄 Variables de Entorno

SPRING_DATA_MONGODB_URI=mongodb+srv://user:pass@cluster0.042vwn7.mongodb.net/btg-db


---

## 🔐 Seguridad

- JWT Authentication
- Spring Security Filters
- Stateless sessions

Authorization: Bearer <token>

---

## 📚 Swagger

http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs

---

## ☁️ AWS Deploy

aws cloudformation deploy \
  --template-file infrastructure.yml \
  --stack-name btg-pactual-ms \
  --capabilities CAPABILITY_NAMED_IAM

---

## 🧪 Tests

./gradlew test

---

## 🔥 Mejoras futuras

- CI/CD GitHub Actions
- Docker + Kubernetes
- Redis cache
