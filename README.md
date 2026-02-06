# NovaCart - eCommerce Backend Platform

Spring Boot backend for a generic online store. It provides secure REST APIs for products, carts, checkout, and orders, and runs locally with Docker.

## Requirements
- Docker and Docker Compose
- Java 17 and Maven (optional for local runs)

## Run with Docker
```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger`

## Default admin account
- Email: `admin@novacart.local`
- Password: `admin123`

## Key endpoints
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products` (admin)
- `PUT /api/products/{id}` (admin)
- `PATCH /api/products/{id}/active` (admin)
- `GET /api/cart`
- `POST /api/cart/items`
- `PUT /api/cart/items/{productId}`
- `DELETE /api/cart/items/{productId}`
- `POST /api/checkout`
- `GET /api/orders`
- `GET /api/orders/{orderId}`

## Local development
```bash
mvn spring-boot:run
```

Set environment variables if not using Docker:
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`