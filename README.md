# FX Deals Data Warehouse API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue.svg)](https://jakarta.ee/)
[![WildFly](https://img.shields.io/badge/WildFly-30.0.0-red.svg)](https://www.wildfly.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/license-Proprietary-lightgrey.svg)](LICENSE)

Enterprise-grade RESTful API for managing Bloomberg FX (Foreign Exchange) deal transactions with comprehensive validation, duplicate prevention, and MySQL persistence.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Error Handling](#error-handling)
- [Monitoring and Logging](#monitoring-and-logging)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Overview

The FX Deals Data Warehouse is a robust Jakarta EE application designed to persist and manage foreign exchange deal information. It provides a RESTful API for creating, retrieving, and managing FX deals with enterprise-level validation, error handling, and data integrity guarantees.

### Key Capabilities

- **Deal Creation**: Create FX deals with comprehensive validation
- **Duplicate Prevention**: Automatic detection and rejection of duplicate deals
- **Data Retrieval**: Query deals by ID, unique identifier, or retrieve paginated lists
- **Validation**: Multi-layer validation ensuring data integrity at DTO, service, and database levels
- **Transaction Management**: Full ACID compliance with JPA/Hibernate transactions
- **Production Ready**: Docker deployment with WildFly and MySQL

## Features

### Core Features

- Create FX deals with unique identifiers
- Retrieve deals by database ID or unique deal identifier
- List all deals with pagination support
- Comprehensive input validation (Jakarta Bean Validation)
- Duplicate deal detection and prevention
- Business rule validation (same currency check, future date prevention)
- RESTful API following best practices
- JSON request/response format
- Detailed error messages with HTTP status codes

### Enterprise Features

- Jakarta EE 10 dependency injection (CDI)
- JPA/Hibernate ORM with MySQL
- Transaction management
- Structured exception handling
- SLF4J/Logback logging
- Docker containerization
- Health check endpoint
- Unit and integration tests (32 tests)
- Maven build automation

## Technology Stack

### Backend

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Programming language |
| **Jakarta EE** | 10.0.0 | Enterprise framework |
| **WildFly** | 30.0.0.Final | Application server |
| **Hibernate** | 6.2.13.Final | ORM framework |
| **MySQL** | 8.0 | Relational database |
| **MySQL Connector/J** | 8.2.0 | JDBC driver |
| **Maven** | 3.9+ | Build tool |

### Testing & Quality

| Technology | Version | Purpose |
|------------|---------|---------|
| **JUnit 5** | 5.10.0 | Unit testing framework |
| **Mockito** | 5.5.0 | Mocking framework |
| **JaCoCo** | 0.8.10 | Code coverage |
| **H2 Database** | 2.2.224 | In-memory testing database |

### DevOps

- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **Logback** - Logging framework
- **SLF4J** - Logging facade

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────────┐
│         REST API Layer (JAX-RS)         │
│         FxDealController.java           │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│           Service Layer (CDI)           │
│         FxDealService.java              │
│      - Business Logic                   │
│      - Validation                       │
│      - Transaction Management           │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│      Repository Layer (JPA/CDI)         │
│       FxDealRepository.java             │
│      - Data Access                      │
│      - Query Operations                 │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│        Persistence Layer (JPA)          │
│           FxDeal.java                   │
│      - Entity Mapping                   │
│      - Constraints                      │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│          MySQL Database 8.0             │
│         fxdeals.fx_deals table          │
└─────────────────────────────────────────┘
```

### Data Flow

```
Client Request (JSON)
    ↓
JAX-RS Controller (Validation)
    ↓
Service Layer (Business Logic + Validation)
    ↓
Repository (Data Access)
    ↓
MySQL Database
    ↓
Response (JSON)
```

## Prerequisites

### Required Software

- **Java Development Kit (JDK) 17** or higher
- **Maven 3.9+** for building
- **Docker** and **Docker Compose** for containerized deployment
- **Git** for version control

### Optional Tools

- **Postman** or **curl** for API testing
- **MySQL Workbench** or **DBeaver** for database management
- **IntelliJ IDEA** or **Eclipse** for development

### System Requirements

- **OS**: Windows 10+, macOS 10.15+, or Linux (Ubuntu 20.04+)
- **RAM**: Minimum 4GB (8GB recommended)
- **Disk Space**: 2GB free space
- **Network**: Ports 8080 (WildFly) and 3306 (MySQL) available

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/fxdeals.git
cd fxdeals
```

### 2. Build the Application

```bash
mvn clean install
```

This will:
- Download dependencies
- Compile source code
- Run unit tests (32 tests)
- Generate test coverage report
- Package WAR file: `target/fxdeals.war`

### 3. Verify Build

```bash
# Check test results
cat target/surefire-reports/TEST-*.xml

# Check code coverage (optional)
open target/site/jacoco/index.html
```

## Configuration

### Application Configuration

**File**: `src/main/resources/application.properties`

```properties
# Database Configuration
database.url=jdbc:mysql://localhost:3306/fxdeals
database.username=fxdeals_user
database.password=fxdeals_password

# Application Configuration
app.name=FX Deals Data Warehouse
app.version=1.0.0
```

### JPA Configuration

**File**: `src/main/resources/META-INF/persistence.xml`

```xml
<persistence-unit name="fxdealsPU" transaction-type="JTA">
    <jta-data-source>java:jboss/datasources/FxDealsDS</jta-data-source>
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
        <property name="hibernate.hbm2ddl.auto" value="update"/>
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
    </properties>
</persistence-unit>
```

### Docker Configuration

**File**: `docker-compose.yml`

```yaml
services:
  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_DATABASE: fxdeals
      MYSQL_USER: fxdeals_user
      MYSQL_PASSWORD: fxdeals_password
      
  wildfly:
    build: .
    ports:
      - "8080:8080"
      - "9990:9990"
    depends_on:
      - mysql
```

## Running the Application

### Option 1: Docker Compose (Recommended)

```bash
# Start all services (MySQL + WildFly)
docker-compose up -d

# View logs
docker-compose logs -f wildfly

# Stop services
docker-compose down
```

### Option 2: Local WildFly

1. **Start MySQL:**
```bash
docker run -d \
  --name fxdeals-mysql \
  -e MYSQL_DATABASE=fxdeals \
  -e MYSQL_USER=fxdeals_user \
  -e MYSQL_PASSWORD=fxdeals_password \
  -e MYSQL_ROOT_PASSWORD=root_password \
  -p 3306:3306 \
  mysql:8.0
```

2. **Configure WildFly datasource** (see Dockerfile for CLI commands)

3. **Deploy WAR:**
```bash
cp target/fxdeals.war $WILDFLY_HOME/standalone/deployments/
```

### Verify Deployment

```bash
# Health check
curl http://localhost:8080/fxdeals/api/fx-deals/health

# Expected response: "FX Deals API is running"
```

## API Documentation

### Base URL

```
http://localhost:8080/fxdeals/api/fx-deals
```

### Endpoints

#### 1. Health Check

```http
GET /api/fx-deals/health
```

**Response**: `200 OK`
```
FX Deals API is running
```

---

#### 2. Create FX Deal

```http
POST /api/fx-deals
Content-Type: application/json
```

**Request Body**:
```json
{
  "dealUniqueId": "DEAL-2025-001",
  "fromCurrencyIso": "USD",
  "toCurrencyIso": "EUR",
  "dealTimestamp": "2025-11-13T10:30:00",
  "dealAmount": 1000000.50
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "dealUniqueId": "DEAL-2025-001",
  "fromCurrencyIso": "USD",
  "toCurrencyIso": "EUR",
  "dealTimestamp": "2025-11-13T10:30:00",
  "dealAmount": 1000000.50,
  "createdAt": "2025-11-13T10:30:15.123"
}
```

**Validation Rules**:
- `dealUniqueId`: Required, must be unique
- `fromCurrencyIso`: Required, 3-letter uppercase ISO code (e.g., USD)
- `toCurrencyIso`: Required, 3-letter uppercase ISO code (e.g., EUR)
- `dealTimestamp`: Required, cannot be in the future
- `dealAmount`: Required, > 0.01, max 15 integer digits, 2 decimal places
- From and To currencies must be different

**Error Responses**:

`400 Bad Request` - Validation error:
```json
{
  "error": "Validation failed",
  "message": "Deal Unique ID is required",
  "timestamp": "2025-11-13T10:30:00"
}
```

`409 Conflict` - Duplicate deal:
```json
{
  "error": "Duplicate deal",
  "message": "Deal with unique ID 'DEAL-2025-001' already exists",
  "timestamp": "2025-11-13T10:30:00"
}
```

---

#### 3. Get All Deals (Paginated)

```http
GET /api/fx-deals?offset=0&limit=10
```

**Query Parameters**:
- `offset` (optional): Starting position, default = 0
- `limit` (optional): Max results, default = 100, max = 1000

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "dealUniqueId": "DEAL-2025-001",
    "fromCurrencyIso": "USD",
    "toCurrencyIso": "EUR",
    "dealTimestamp": "2025-11-13T10:30:00",
    "dealAmount": 1000000.50,
    "createdAt": "2025-11-13T10:30:15.123"
  }
]
```

**Response Headers**:
```
X-Total-Count: 150
X-Offset: 0
X-Limit: 10
```

---

#### 4. Get Deal by ID

```http
GET /api/fx-deals/{id}
```

**Response**: `200 OK` (same structure as create response)

---

#### 5. Get Deal by Unique ID

```http
GET /api/fx-deals/unique/{dealUniqueId}
```

**Response**: `200 OK` (same structure as create response)

---

### OpenAPI/Swagger Specification

Full OpenAPI 3.0 specification available at: **`openapi.yaml`**

View with Swagger UI:
1. Copy content from `openapi.yaml`
2. Visit [Swagger Editor](https://editor.swagger.io/)
3. Paste and explore

### Postman Collection

Import the comprehensive Postman collection: **`postman_collection.json`**

Contains 21 pre-configured requests:
- Health check
- 7 valid deal scenarios
- 7 invalid scenarios (validation tests)
- 7 retrieval operations

## Testing

### Run All Tests

```bash
mvn test
```

**Test Results**: 32 tests passing

### Test Coverage

```bash
mvn clean test jacoco:report
open target/site/jacoco/index.html
```

### Test Structure

```
src/test/java/com/bloomberg/fxdeals/
├── controller/
│   └── FxDealControllerTest.java      # API endpoint tests
├── service/
│   └── FxDealServiceTest.java         # Business logic tests
└── repository/
    └── FxDealRepositoryTest.java      # Data access tests
```

### Manual Testing with curl

```bash
# Health check
curl http://localhost:8080/fxdeals/api/fx-deals/health

# Create deal
curl -X POST http://localhost:8080/fxdeals/api/fx-deals \
  -H "Content-Type: application/json" \
  -d '{
    "dealUniqueId": "DEAL-2025-001",
    "fromCurrencyIso": "USD",
    "toCurrencyIso": "EUR",
    "dealTimestamp": "2025-11-13T10:30:00",
    "dealAmount": 1000000.50
  }'

# Get all deals
curl http://localhost:8080/fxdeals/api/fx-deals

# Get deal by ID
curl http://localhost:8080/fxdeals/api/fx-deals/1
```

## Project Structure

```
fxdeals/
├── src/
│   ├── main/
│   │   ├── java/com/bloomberg/fxdeals/
│   │   │   ├── controller/
│   │   │   │   └── FxDealController.java       # REST endpoints
│   │   │   ├── dto/
│   │   │   │   ├── FxDealRequest.java          # Request DTO
│   │   │   │   ├── FxDealResponse.java         # Response DTO
│   │   │   │   └── ErrorResponse.java          # Error DTO
│   │   │   ├── entity/
│   │   │   │   └── FxDeal.java                 # JPA entity
│   │   │   ├── exception/
│   │   │   │   ├── DealNotFoundException.java
│   │   │   │   ├── DuplicateDealException.java
│   │   │   │   └── GlobalExceptionMapper.java  # Error handler
│   │   │   ├── mapper/
│   │   │   │   └── FxDealMapper.java           # DTO/Entity mapper
│   │   │   ├── repository/
│   │   │   │   └── FxDealRepository.java       # Data access
│   │   │   └── service/
│   │   │       └── FxDealService.java          # Business logic
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   ├── persistence.xml             # JPA config
│   │       │   └── beans.xml                   # CDI config
│   │       └── application.properties          # App config
│   └── test/
│       └── java/com/bloomberg/fxdeals/         # 32 unit tests
├── docker/
│   └── init.sql                                # MySQL init script
├── target/
│   └── fxdeals.war                             # Deployable WAR
├── Dockerfile                                   # Docker image
├── docker-compose.yml                           # Multi-container setup
├── pom.xml                                      # Maven config
├── openapi.yaml                                 # API specification
├── postman_collection.json                      # API tests
└── README.md                                    # This file
```

## Database Schema

### Table: `fx_deals`

```sql
CREATE TABLE fx_deals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    deal_unique_id VARCHAR(255) NOT NULL UNIQUE,
    from_currency_iso VARCHAR(3) NOT NULL,
    to_currency_iso VARCHAR(3) NOT NULL,
    deal_timestamp DATETIME(6) NOT NULL,
    deal_amount DECIMAL(19,2) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    
    INDEX idx_deal_unique_id (deal_unique_id),
    INDEX idx_deal_timestamp (deal_timestamp),
    INDEX idx_from_currency (from_currency_iso),
    INDEX idx_to_currency (to_currency_iso)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Constraints

- **Primary Key**: `id` (auto-increment)
- **Unique Constraint**: `deal_unique_id`
- **Indexes**: Optimized for common queries

## Error Handling

### Error Response Format

All errors return standardized JSON:

```json
{
  "error": "Error category",
  "message": "Human-readable error message",
  "timestamp": "2025-11-13T10:30:00"
}
```

### HTTP Status Codes

| Code | Description | When Used |
|------|-------------|-----------|
| `200` | OK | Successful GET |
| `201` | Created | Successful POST |
| `400` | Bad Request | Validation error |
| `404` | Not Found | Deal not found |
| `409` | Conflict | Duplicate deal |
| `500` | Internal Server Error | Unexpected error |

## Monitoring and Logging

### View Logs

```bash
# Docker container logs
docker logs -f fxdeals-wildfly

# Application logs (if volume mounted)
tail -f logs/fxdeals.log
```

### Log Format

```
2025-11-13 10:30:15.123 [INFO ] [FxDealController] Received request to create FX deal
2025-11-13 10:30:15.456 [DEBUG] [SQL] insert into fx_deals (...)
2025-11-13 10:30:15.789 [INFO ] [FxDealService] Deal created successfully
```

## Troubleshooting

### Common Issues

#### 1. Port Already in Use

**Error**: `Port 8080 is already allocated`

**Solution** (Windows):
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

#### 2. MySQL Connection Failed

**Solutions**:
- Verify MySQL is running: `docker ps | grep mysql`
- Check credentials in `application.properties`
- Test connection: `docker exec -it fxdeals-mysql mysql -u fxdeals_user -p`

#### 3. 404 Not Found

**Solutions**:
- Verify context path: `/fxdeals/api/fx-deals`
- Check health: `curl http://localhost:8080/fxdeals/api/fx-deals/health`

### Reset Everything

```bash
# Stop and remove containers
docker-compose down -v

# Rebuild from scratch
docker-compose up -d --build

# Verify health
curl http://localhost:8080/fxdeals/api/fx-deals/health
```

## License

This project is proprietary and confidential.  
**© 2025 Bloomberg L.P. All rights reserved.**

---

## Support

For questions or issues:
- **Email**: support@bloomberg.com
- **Documentation**: See `openapi.yaml` for complete API specification

---

**Made by the Bloomberg Engineering Team**
