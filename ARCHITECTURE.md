# System Architecture

## Overview

The FX Deals Data Warehouse is built using Jakarta EE 10, following enterprise Java best practices and design patterns. This document describes the system architecture, design decisions, and technical implementation.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                          │
│          (REST Clients, Web Browsers, Applications)         │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP/REST
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    Presentation Layer                        │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │         JAX-RS Resources (Controllers)                │  │
│  │  - FxDealController: REST endpoints                   │  │
│  │  - RestApplication: JAX-RS configuration              │  │
│  └──────────────┬───────────────────────────────────────┘  │
│                 │                                            │
│  ┌──────────────▼───────────────────────────────────────┐  │
│  │         Exception Mappers                             │  │
│  │  - DuplicateDealExceptionMapper                       │  │
│  │  - ValidationExceptionMapper                          │  │
│  │  - DealNotFoundExceptionMapper                        │  │
│  │  - GenericExceptionMapper                             │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                     Business Layer                           │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Service Components                       │  │
│  │  - FxDealService: Business logic & validation        │  │
│  │  - Transaction Management (@Transactional)           │  │
│  │  - Duplicate Detection                               │  │
│  │  - Bean Validation Integration                       │  │
│  └──────────────┬───────────────────────────────────────┘  │
│                 │                                            │
│  ┌──────────────▼───────────────────────────────────────┐  │
│  │              DTOs & Mappers                           │  │
│  │  - FxDealRequest, FxDealResponse, ErrorResponse      │  │
│  │  - FxDealMapper: Entity-DTO conversion               │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   Persistence Layer                          │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │            Repository Components                      │  │
│  │  - FxDealRepository: Data access operations          │  │
│  │  - JPA EntityManager for database interactions       │  │
│  └──────────────┬───────────────────────────────────────┘  │
│                 │                                            │
│  ┌──────────────▼───────────────────────────────────────┐  │
│  │                JPA Entities                           │  │
│  │  - FxDeal: Domain model with validation              │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                     Data Layer                               │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              PostgreSQL Database                      │  │
│  │  - fx_deals table                                     │  │
│  │  - Indexes for performance                            │  │
│  │  - ACID transactions                                  │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### 1. Presentation Layer

**Purpose**: Handle HTTP requests and responses

**Components**:
- `FxDealController`: JAX-RS REST resource exposing API endpoints
- `RestApplication`: JAX-RS application configuration
- Exception Mappers: Convert exceptions to appropriate HTTP responses

**Technologies**:
- Jakarta RESTful Web Services (JAX-RS) 3.1
- JSON-B for JSON processing

**Key Features**:
- RESTful API design
- Content negotiation
- HTTP status code management
- CORS support (if needed)

### 2. Business Layer

**Purpose**: Implement business logic and validation

**Components**:
- `FxDealService`: Core business logic for FX deals
- `FxDealMapper`: DTO-Entity conversion
- DTOs: Data Transfer Objects for API communication

**Technologies**:
- Jakarta Enterprise Beans (CDI) for dependency injection
- Jakarta Bean Validation 3.0
- Jakarta Transactions (JTA)

**Key Features**:
- Business rule enforcement
- Input validation
- Duplicate detection
- Transaction management
- Logging and auditing

### 3. Persistence Layer

**Purpose**: Manage data access and persistence

**Components**:
- `FxDealRepository`: Database operations
- `FxDeal` entity: JPA entity mapping

**Technologies**:
- Jakarta Persistence API (JPA) 3.1
- Hibernate 6.2 as JPA provider
- PostgreSQL JDBC Driver

**Key Features**:
- CRUD operations
- Query optimization
- Connection pooling
- Transaction support

### 4. Data Layer

**Purpose**: Store and retrieve data

**Components**:
- PostgreSQL database
- Database schema
- Indexes and constraints

**Key Features**:
- ACID compliance
- Data integrity constraints
- Performance optimization
- Backup and recovery

## Design Patterns

### 1. Layered Architecture

The application is organized into distinct layers with clear responsibilities:
- Separation of concerns
- High cohesion within layers
- Low coupling between layers
- Easy to test and maintain

### 2. Repository Pattern

`FxDealRepository` abstracts data access logic:
- Encapsulates database queries
- Provides a collection-like interface
- Enables easy testing with mocks
- Centralizes data access logic

### 3. Data Transfer Object (DTO)

DTOs separate internal entities from API contracts:
- `FxDealRequest`: Incoming data
- `FxDealResponse`: Outgoing data
- Prevents exposing internal structure
- Allows API evolution

### 4. Dependency Injection

Jakarta CDI manages component lifecycle:
- `@ApplicationScoped`: Single instance per application
- `@Inject`: Automatic dependency injection
- Loose coupling between components
- Easy testing with mocks

### 5. Exception Handling Strategy

Custom exception hierarchy with global exception mappers:
- Business exceptions: `DuplicateDealException`, `ValidationException`
- Technical exceptions: `DealNotFoundException`
- Generic fallback: `GenericExceptionMapper`

## Technology Stack Details

### Jakarta EE 10

**Why Jakarta EE?**
- Enterprise-grade framework
- Standardized APIs
- Vendor-neutral
- Rich ecosystem
- Production-ready features

**Key APIs Used**:
- **JAX-RS 3.1**: RESTful web services
- **JPA 3.1**: Object-relational mapping
- **CDI 4.0**: Dependency injection
- **Bean Validation 3.0**: Data validation
- **JTA 2.0**: Transaction management

### WildFly 30

**Application Server**:
- Full Jakarta EE 10 implementation
- Lightweight and fast
- Easy configuration
- Built-in clustering support
- Excellent development experience

### PostgreSQL 16

**Database Choice**:
- ACID compliance
- Excellent performance
- JSON support (for future extensions)
- Mature and stable
- Great tooling

### Hibernate 6.2

**ORM Framework**:
- JPA 3.1 implementation
- Powerful query language (HQL)
- Second-level caching
- Lazy loading
- Automatic schema generation

## Data Flow

### Creating an FX Deal

```
1. Client sends POST request
   ↓
2. JAX-RS deserializes JSON to FxDealRequest
   ↓
3. Controller calls FxDealService.createDeal()
   ↓
4. Service validates request using Bean Validation
   ↓
5. Service checks for duplicates via Repository
   ↓
6. Service maps DTO to Entity using Mapper
   ↓
7. Repository persists Entity using EntityManager
   ↓
8. JPA/Hibernate generates SQL INSERT
   ↓
9. PostgreSQL stores data and returns generated ID
   ↓
10. Entity flows back through layers
    ↓
11. Mapper converts Entity to Response DTO
    ↓
12. JAX-RS serializes DTO to JSON
    ↓
13. Client receives HTTP 201 with created deal
```

### Error Handling Flow

```
1. Exception thrown in any layer
   ↓
2. Transaction is rolled back (if active)
   ↓
3. Exception bubbles up to JAX-RS layer
   ↓
4. Exception Mapper catches exception
   ↓
5. Mapper creates ErrorResponse DTO
   ↓
6. JAX-RS serializes to JSON
   ↓
7. Client receives appropriate HTTP status code
```

## Validation Strategy

### Bean Validation (Jakarta Validation 3.0)

**Entity Level**:
```java
@NotBlank(message = "Deal Unique ID is required")
private String dealUniqueId;

@Pattern(regexp = "^[A-Z]{3}$", message = "Must be 3-letter ISO code")
private String fromCurrencyIso;
```

**DTO Level**:
- Same validations for consistency
- Additional constraints if needed

**Service Level**:
- Programmatic validation using `Validator`
- Custom business rule validation

### Validation Layers

1. **Syntax Validation**: Bean Validation annotations
2. **Semantic Validation**: Business rules in service
3. **Database Constraints**: Unique constraints, foreign keys

## Transaction Management

### JTA (Jakarta Transactions)

**Transaction Boundaries**:
- `@Transactional` on service methods
- Automatic commit/rollback
- ACID guarantees

**Key Principles**:
- One transaction per service method
- No rollback policy: validated deals are always saved
- Exceptions trigger automatic rollback

## Logging Strategy

### SLF4J with Logback

**Log Levels**:
- **DEBUG**: Development details, SQL queries
- **INFO**: Business events, API calls
- **WARN**: Recoverable issues
- **ERROR**: Exceptions and failures

**Log Locations**:
- Console: For development and Docker
- File: `/opt/jboss/wildfly/standalone/log/fxdeals/`
- Rolling policy: Daily rotation, 30-day retention

**Logged Information**:
- Request/response details
- Validation errors
- Database operations
- Business events
- Exception stack traces

## Security Considerations

### Current Implementation

- Input validation to prevent injection attacks
- Bean Validation for data integrity
- Unique constraints prevent duplicates

### Future Enhancements

- Authentication (JWT, OAuth2)
- Authorization (Role-based access)
- Rate limiting
- API key management
- Audit logging
- TLS/SSL encryption

## Performance Optimization

### Database

- Indexes on frequently queried columns
- Connection pooling (HikariCP via WildFly)
- Prepared statements to prevent SQL injection
- Batch inserts for bulk operations

### Application

- CDI for singleton services
- Lazy loading for entities
- Pagination for large result sets
- HTTP caching headers

### Monitoring

- JMX for runtime metrics
- Database connection pool monitoring
- Response time tracking
- Error rate monitoring

## Scalability

### Horizontal Scaling

- Stateless application design
- Database connection pooling
- Session management via database
- Load balancer ready

### Vertical Scaling

- JVM tuning options
- Database optimization
- Connection pool sizing
- Thread pool configuration

## Deployment Architecture

```
┌─────────────────────────────────────────────────────┐
│                  Load Balancer                       │
└──────┬──────────────────────┬───────────────────────┘
       │                      │
┌──────▼──────┐      ┌───────▼──────┐
│  WildFly 1  │      │  WildFly 2   │
│  (Docker)   │      │  (Docker)    │
└──────┬──────┘      └───────┬──────┘
       │                     │
       └──────────┬──────────┘
                  │
        ┌─────────▼─────────┐
        │   PostgreSQL      │
        │   (Docker)        │
        └───────────────────┘
```

## Testing Strategy

### Unit Tests

- Service layer: Business logic validation
- Repository layer: Data access testing
- Mapper layer: DTO-Entity conversion
- Controller layer: Endpoint testing

### Integration Tests

- Database integration
- End-to-end API testing
- Transaction behavior verification

### Test Coverage

- Target: >80% code coverage
- JaCoCo for coverage reporting
- Critical paths: 100% coverage

## Future Enhancements

### Phase 1: Core Features

- [x] RESTful API
- [x] Data validation
- [x] Duplicate prevention
- [x] PostgreSQL integration
- [x] Docker deployment

### Phase 2: Advanced Features

- [ ] Batch import API
- [ ] Export to CSV/Excel
- [ ] Advanced search and filtering
- [ ] Real-time FX rate integration
- [ ] Audit trail

### Phase 3: Enterprise Features

- [ ] Authentication & Authorization
- [ ] Rate limiting
- [ ] Caching layer (Redis)
- [ ] Message queue integration
- [ ] Microservices architecture
- [ ] Kubernetes deployment

## Conclusion

This architecture provides:
- ✅ Clear separation of concerns
- ✅ Maintainable and testable code
- ✅ Scalable design
- ✅ Production-ready implementation
- ✅ Enterprise-grade features
- ✅ Standards-based approach

The system is designed to be extensible, allowing for future enhancements without major refactoring.
