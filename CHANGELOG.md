# Changelog

All notable changes to the FX Deals Data Warehouse project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-01-15

### Added
- Initial release of FX Deals Data Warehouse
- Jakarta EE 10 implementation (pure Jakarta, no Spring Boot)
- RESTful API with JAX-RS for FX deal management
- PostgreSQL 16 database integration
- Complete CRUD operations for FX deals
- Bean Validation for input validation
- Duplicate deal detection and prevention
- Custom exception handling with proper HTTP status codes
- Comprehensive logging with SLF4J and Logback
- JPA/Hibernate for ORM
- CDI for dependency injection
- Docker containerization with multi-stage build
- Docker Compose for service orchestration
- WildFly 30.0.0 as application server
- Makefile for streamlined operations
- Comprehensive unit tests with JUnit 5 and Mockito
- Test coverage reporting with JaCoCo (>80% coverage)
- Health check endpoint
- Pagination support for listing deals
- Complete documentation (7 MD files)
- GitHub Actions CI/CD pipeline
- MIT License

### API Endpoints
- `POST /api/fx-deals` - Create new FX deal
- `GET /api/fx-deals` - List all deals with pagination
- `GET /api/fx-deals/{id}` - Get deal by database ID
- `GET /api/fx-deals/unique/{dealUniqueId}` - Get deal by unique ID
- `GET /api/fx-deals/health` - Health check

### Database Schema
- `fx_deals` table with the following fields:
  - `id` (BIGSERIAL, PRIMARY KEY)
  - `deal_unique_id` (VARCHAR(100), UNIQUE, NOT NULL)
  - `from_currency_iso` (VARCHAR(3), NOT NULL)
  - `to_currency_iso` (VARCHAR(3), NOT NULL)
  - `deal_timestamp` (TIMESTAMP, NOT NULL)
  - `deal_amount` (NUMERIC(19,2), NOT NULL)
  - `created_at` (TIMESTAMP, NOT NULL)
  - `updated_at` (TIMESTAMP)
- Indexes on `deal_unique_id` and `deal_timestamp`

### Validation Rules
- Deal Unique ID: Required, unique
- Currency codes: 3-letter ISO format (e.g., USD, EUR)
- From and To currencies must be different
- Deal timestamp: Cannot be in the future
- Deal amount: Positive decimal with max 15 integer digits, 2 decimal places

### Documentation
- README.md - Main project documentation
- ARCHITECTURE.md - System architecture and design
- DEPLOYMENT.md - Deployment guide for all environments
- API_TESTING_GUIDE.md - Complete API testing guide
- SAMPLE_REQUESTS.md - Sample API requests
- CONTRIBUTING.md - Contribution guidelines
- PROJECT_SUMMARY.md - Project overview and summary
- This CHANGELOG.md

### Testing
- 30+ unit tests across all layers
- Repository layer tests
- Service layer tests (with validation scenarios)
- Controller layer tests
- Mapper tests
- Test coverage >80%

### Technical Stack
- Java 17
- Jakarta EE 10.0.0
- WildFly 30.0.0.Final
- PostgreSQL 16
- Hibernate 6.2.13.Final
- Maven 3.9+
- JUnit 5.10.0
- Mockito 5.5.0
- Docker & Docker Compose
- SLF4J 2.0.9 & Logback 1.4.11

### Features
- No rollback policy: Validated deals are always persisted
- Duplicate prevention at both service and database levels
- Comprehensive error handling with custom exception mappers
- Structured logging for debugging and monitoring
- Health checks for monitoring
- Pagination for large datasets
- Docker-first approach for easy deployment
- Production-ready configuration

## [Unreleased]

### Planned Features
- Batch import API for multiple deals
- Export functionality (CSV, Excel)
- Advanced search and filtering
- Real-time FX rate integration
- Audit trail for all operations
- Authentication and authorization
- Rate limiting
- Redis caching layer
- Kubernetes deployment configuration
- Performance monitoring dashboard

---

## Version History

### Version 1.0.0 (2024-01-15)
- Initial production release
- All core features implemented
- Comprehensive documentation
- Full test coverage
- Docker deployment ready
- GitHub CI/CD pipeline

---

## Notes

- This project follows semantic versioning
- Each version is tagged in Git
- Breaking changes are clearly marked
- See README.md for upgrade instructions

## Links

- [GitHub Repository](https://github.com/yourusername/fxdeals)
- [Issue Tracker](https://github.com/yourusername/fxdeals/issues)
- [Documentation](https://github.com/yourusername/fxdeals/tree/main/docs)

---

*For more information about specific changes, see the commit history.*
