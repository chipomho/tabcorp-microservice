# 🚀 Java Technical Test 

This microservice accepts transactions in **JSON Format** The primary capability of the service is to accept large amount of transactions per second, and data needs to be stored in the database for reporting. 
To achieve a high-performance, a reactive microservice is built with **Spring Boot WebFlux**. It uses **Java 8**, **R2DBC** for non-blocking DB access, **OAuth2/JWT-based authentication tokens**, and supports **custom user details** and roles.

---

## 📦 Tech Stack

- Java 8 (minimum)
- Spring Boot WebFlux
- R2DBC + PostgreSQL
- OAuth2 with JWT (JJWT)
- Spring Security
- RabbitMQ
- Redis
- Actuator
- JUnit 5, Mockito, StepVerifier
- Apache JMeter for load testing

---

## 🔧 Getting Started

### 💅 Prerequisites

- Java 8
- Gradle 8.10+
- Postgres running locally or via Docker

### ⚙️ Configuration

The following PostgreSQL Database are used

```postgresql

CREATE TABLE IF NOT EXISTS customer (
    ID                  BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 10001 INCREMENT BY 1 ) NOT NULL,
    FIRST_NAME          VARCHAR(120),
    LAST_NAME           VARCHAR(120),
    EMAIL_ADDRESS       VARCHAR(120),
    LOCATION            VARCHAR(80),
    CONSTRAINT pk_customer PRIMARY KEY (ID),
    CONSTRAINT unique_customer UNIQUE (EMAIL_ADDRESS)
);

CREATE TABLE IF NOT EXISTS product (
    CODE               VARCHAR(16) NOT NULL ,
    PRODUCT_COST       NUMERIC(18,2),
    PRODUCT_STATUS     VARCHAR(10),
    CONSTRAINT pk_product PRIMARY KEY (CODE)
);

CREATE TABLE IF NOT EXISTS customer_transaction (
    ID                     BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 10001 INCREMENT BY 1 )  NOT NULL,
    TRANSACTION_DATETIME   TIMESTAMP NOT NULL,
    CUSTOMER_ID            BIGINT NOT NULL,
    PRODUCT_QUANTITY       INTEGER NOT NULL DEFAULT 0,
    PRODUCT_CODE           VARCHAR(16) NOT NULL,
    CONSTRAINT pk_customer_transaction PRIMARY KEY (ID),
    CONSTRAINT fk_customer_transaction_cust FOREIGN KEY (CUSTOMER_ID) REFERENCES  customer (ID) ON DELETE  CASCADE,
    CONSTRAINT fk_customer_transaction_prod FOREIGN KEY (PRODUCT_CODE) REFERENCES  product (CODE) ON DELETE  CASCADE
);

```
Initial Data used
```postgresql
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Tony', 'Stark', 'tony.stark@gmail.com', 'Australia') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Bruce', 'Banner', 'bruce.banner@gmail.com', 'US') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Steve', 'Rogers', 'steve.rogers@gmail.com', 'Australia') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Wanda', 'Maximoff', 'wanda.maximoff@gmail.com', 'US') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Natasha', 'Romanoff', 'natasha.romanoff@gmail.com', 'Canada') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;


INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_001', 50.00, 'ACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_002', 100.00, 'INACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_003', 200.00, 'ACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_004', 10.00, 'INACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_005', 500.00, 'ACTIVE') ON CONFLICT (CODE) DO NOTHING;

```

`application.yaml` default values

```yaml
tabcorp:
  security:
    #RSA keys used to set public/private keys to sign JWT tokens
    #keys should be stored in a secure storage
    rsa:
      #Public Key
      public-key: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuIq6rNofy9nKzhBo7jb2kVZKgTsXaanHvl9kZucRXWA8ZJuES+AVDNKyHjFb4w71pibomCdJ/pFD2TaHsvThc/hpE9AJiJvzutePCSbR4rh96XgucVV/vDirYtFjSso0QRYClo7Mjhx12DzrUPoVxJxlIVmBMvWG19LiPvD7WTd91OYq2bBWtFYCsNXb/W+5cyvktNgB5N7dkA3ehORy0tQEDbAhgS6HP+FyPYOiw4amvKuz7eaHzUmfjK9CHMnXBwNJiDcwKD7PEbSnGjJeuEAMr8cyMeG6ZH7GN6S5CCEDlDJWL6eIfRs/3WOS7e6sy26S7C/U+PR31mKxUBThcwIDAQAB
      #Private Key
      private-key: MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC4irqs2h/L2crOEGjuNvaRVkqBOxdpqce+X2Rm5xFdYDxkm4RL4BUM0rIeMVvjDvWmJuiYJ0n+kUPZNoey9OFz+GkT0AmIm/O6148JJtHiuH3peC5xVX+8OKti0WNKyjRBFgKWjsyOHHXYPOtQ+hXEnGUhWYEy9YbX0uI+8PtZN33U5irZsFa0VgKw1dv9b7lzK+S02AHk3t2QDd6E5HLS1AQNsCGBLoc/4XI9g6LDhqa8q7Pt5ofNSZ+Mr0IcydcHA0mINzAoPs8RtKcaMl64QAyvxzIx4bpkfsY3pLkIIQOUMlYvp4h9Gz/dY5Lt7qzLbpLsL9T49HfWYrFQFOFzAgMBAAECgf8LLFshHBPQw98Dmvq/cZR8OD1KlclVHHJ7lEr/7/2dKbZ+YOwhq++aVydxrItjtAQriy+KBtb0EteIz0RoEWuVGfVCvdXV75tSNLr0UuViVMlK2Xv2YLbbLpMx6Huai6OeTTba5sp70D1SWl3lCT9IKC/3VPP9oeAIMTyBkxWk0Rz7A2WBSgKq3l1qTG4kUFaODvj9/id5R8dmSrV5qgQ+MqWTc42WVEF9EdKhIS33ArtWu0kYKTeOXrPSWX/HLPPMitwqM0wTE+PGa73eZpUATWmkgKgAy/k0B7gbwpd6kH5SxTKL/PHeojJgNuOi1q1UxW67LKFSNMbImF4BpUUCgYEA22M3pjj18lv0n8QfysSqI3KhLwssIjMtRH1bN14v97QXxQtfy7PKCV2Ee9p99AKIclSqT2UEbF2KokLtkR7y9VcN2gTo2NidHo57f2VhENOxSp/c0aHS8puS65SHoezup6VDfqGSUfnbOOKt/FlSLXmKfKOPnCKeSu4Lz1Tu11UCgYEA11bRlNd4EntpHCwTdU2D2jwOYj7jQ1fvfVOYFlpZxvopbAjzXWw9yzfTsQj4UlrBJmBaLZpIUagd0NVIx6hfdsCPtmmIxmI27MAoXLq5V8VvJhFQy9Ll4I8OKu3HNDFkXcmLZvZfrHmyivPrBGePESl0YJEu5lTpNVXQQ2ZKxacCgYBDIM/FT8aJuvNV83fa3mRE5m1FbMC7Ujd2rni7r1k0DOn9A+by+HdIeFHyZ7+Ea0X+3cMi+gDpe5HQ5nO0pOiog1Ocs0XgHB+vWt+U8ptJv8yOze2OQ1q9mzbQWKlLtaisqVfGpn+UD8SxWkLsZRRND9R9ILrvhnQy7zWz4gQW5QKBgQCadQufV24o05xxOKw8V31b6/flQHaCt3Bw1KUiY2xB7O4JL+JgAGPSgBi3w1dauv4QKMyGKUP2TkAUquepTnwrEbQDg2833PrIloxc9xI6gsNW6tSpXnzEtdfTsvpP1Z3IJ+h10gKOFDycrpjEcAOOq81t9rVNQVqFhyf7MEQxwQKBgQC2RXLc1Tz1Y2Ogh0zK7SgHUS42AkND/Zb/ReyWo1KBhqsyEg2f1VfYwxTQv1Q1cRh5s7ibtpRGK5C5dS5Ix33zanhmDJAxNmCD7HmLaFpjeDeaKbsDupRHgpTCL2bEH2LR2ZvX8YPMxRbMF4bYG3t7QoMrfpYqe/1Dd2UYc2KOlg==
    #the following users are used for security for testing purposes only
    in-memory-users:
      #default expiration of the token is 30minutes
      expires-in: 1800
      users:
        - username: standard
          password: Standard$Pass2025
          roles: #adding roles the following are valid STANDARD, PRODUCT, CUSTOMER
            - STANDARD
        - username: admin
          password: Admin$Pass2025
          roles:
            - STANDARD
            - PRODUCT
            - CUSTOMER
        - username: product
          password: Product$Pass2025
          roles:
            - STANDARD
            - PRODUCT
        - username: customer
          password: Cust$Pass2025
          roles:
            - STANDARD
            - CUSTOMER
  app:
    messaging-mode: ${APPLICATION_MESSAGING_MODE}
    validation:
      transactions:
        maximum-cost: 5000
      messages:
        validator-prefix: ValidationMessages
    r2dbc:
      data:
        url: ${spring.r2dbc.url}
        user: ${spring.r2dbc.username}
        password: ${spring.r2dbc.password}


spring:
  redis:
    host: ${SPRING_REDIS_HOST}
    port: ${SPRING_REDIS_PORT}
  rabbitmq:
    #username: guest
    username: ${SPRING_RABBITMQ_USERNAME}
    #password: guest
    password: ${SPRING_RABBITMQ_PASSWORD}
    virtual-host: ${SPRING_RABBITMQ_VIRTUAL_HOST}
    #node-addresses: localhost
    node-addresses: ${SPRING_RABBITMQ_NODES}
    exchange: Tabcorp.TechnicalTask.Exchange
    queue: Tabcorp.TechnicalTask.Queue

  jackson:
    serialization:
      write-dates-as-timestamps: false
    date-format: yyyy-MM-dd'T'HH:mm:ss
  r2dbc:
    #url: r2dbc:pool:postgresql://localhost:5432/tabcorp
    url: ${R2DBC_DATASOURCE_URL}
    #username: tabcorp
    username: ${R2DBC_DATASOURCE_USERNAME}
    #password: tabcorp
    password: ${R2DBC_DATASOURCE_PASSWORD}
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://api.tabcorp.com.au/oauth2/default
r2dbc:
  pool:
    initial-size: 64
    max-size: 1024


#logging:
#  level:
#    io:
#      r2dbc:
#        postgresql:
#          QUERY: DEBUG
#          PARAM: DEBUG
#    reactor:
#      netty: DEBUG
#    org:
#      springframework:
#        data:
#          r2dbc:
#            core: DEBUG
#        r2dbc:
#          core: DEBUG
#        web:
#          reactive: DEBUG
#
#


```

---

## ▶️ Running the Application with Docker
First build in the main project folder build the Spring Boot archive
```bash
./gradlew :customer-transaction-toolkit:customer-transaction-toolkit-api:bootJar
```

### Start the application
```bash
docker-compose up --build 
```

App runs at: `http://localhost:8080`

---

## 🔐 Authentication

### Obtain Access Token

```http
POST /authentication/token
Content-Type: application/json

{
  "client-id": "standard",
  "client-secret": "Standard$Pass2025"
}
```

Response:
```json
{
  "token": "...",
  "refresh_token": "..."
}
```

Use `Authorization: Bearer <accessToken>` for authenticated requests.

---
## 📁 The service has the following main endpoint

| Method   | Endpoint               | Description                                                  | Authentication Required | Role        |
|----------|------------------------|--------------------------------------------------------------|-------------------------|-------------|
| POST     | /authentication/token  | Retrieve Authentication Token     |                         | `ANONYMOUS` |
| POST     | /transaction           | Accepts transaction and save it to the database              | ✅                       | `STANDARD` |
| GET      | /report/customer/{id}  | Retrieves the transaction cost for a given customer          | ✅                       | `STANDARD`  |
| GET      | /report/customers      | Retrieves the transaction cost for all customers             | ✅                       | `STANDARD`  |
| GET      | /report/product/{code} | Retrieves the transaction cost for a given product code      | ✅                       | `STANDARD`  |
| GET      | /report/products       | Retrieves the transaction cost for all products              | ✅                       | `STANDARD`  |
| GET      | /report/australia      | Retrieves the number of transactions for Australian location | ✅                       | `STANDARD`  |



## 📁 The service has the following extra for authentication, customer and product management endpoints

| Method | Endpoint               | Description                                           | Authentication Required | Role        |
|--------|------------------------|-------------------------------------------------------|-------------------------|-------------|
| POST   | /customer              | Create a new customer                                 | ✅                       | `CUSTOMER`  |
| PUT    | /customer              | Update customer                                       | ✅                       | `CUSTOMER`  |
| GET    | /customer/{customerId} | Retrieve Customer Details                             | ✅                       | `CUSTOMER`  |
| DELETE | /customer/{customerId} | Delete customer with specified id                     | ✅                       | `CUSTOMER`  |
| GET    | /product/{code}        | Retrieve Product Details for a specified **code**     | ✅                       | `PRODUCT`  |
| POST   | /product        | Adds a new Product                                    | ✅                       | `PRODUCT`  |
| PUT    | /product        | Updates an existing product                           | ✅                       | `PRODUCT`  |
| DELETE | /product/{code}        | Delete the existing Product with a specified **code** | ✅                       | `PRODUCT`  |


---

## ✅ Testing

### Unit Tests

```bash
./gradlew test
```
---

## 📊 Observability

### Actuator Endpoints

```http
GET /actuator/health
GET /actuator/metrics
```

---

## Application Mode
To achieve maximum efficiency and process large amount of transactions per second the application can be configured to run in 
two different mode
**APPLICATION_MESSAGING_MODE** if set to true, the received messages from the HTTP controller are placed on RabbitMQ exchange
and they are asynchronously processed into the database.

**APPLICATION_MESSAGING_MODE** if set to true the transactions received from the HTTP controller are 
directly saved into the database, a little bit slower.

the results below shows the results when running in messaging mode. **Maximum TPS in a Docker Swarm and multiple virtual machines clients topped 8500**


## 🔁 Load Testing Results (JMeter)

### Test Setup

- JMeter v5.5
- Using Docker Swarm and multiple virtual machine hosts the following tests were carried out: 500, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000 concurrent users
- Target: **POST** on `/transaction` to add a transaction.

### Results Summary

| Users | Peak TPS |  Errors|
|-------|----------|--------|
| 500   | 500/s    | 0%     |
| 1000  | 1000/s   | 0%     |
| 2000  | 2000/s   | <1%    |
| 3000  | 3000/s   | <1%    |
| 4000  | 4100/s   | <1%    |
| 5000  | 4983/s   | <1%    |
| 6000  | 5947/s   | <1%    |
| 7000  | 6982/s   | <1%    |
| 8000  | 7823/s   | <1.5%  |
| 9000  | 8877/s   | <2%    |
| 10000 | 8510/s   | <2%    |

✅ **Conclusion**: Service is reactive under pressure. Optimal TPS ~8500 for 10000 users, with tuned RabbitMQ server and PostgreSQL server for optimal performance.


---

## 🧑‍💻 Authors

- [Tinashe Chipomho](https://github.com/ultrasmartech/technical-test-reactive)

---

