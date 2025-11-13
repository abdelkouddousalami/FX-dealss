.PHONY: help build test clean run stop logs restart deploy undeploy health

# Variables
COMPOSE_FILE = docker-compose.yml
APP_NAME = fxdeals

help: ## Show this help message
	@echo "FX Deals Data Warehouse - Available Commands:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

build: ## Build the application and Docker images
	@echo "Building application..."
	mvn clean package -DskipTests
	@echo "Building Docker images..."
	docker-compose -f $(COMPOSE_FILE) build

test: ## Run unit tests with coverage
	@echo "Running tests..."
	mvn clean test
	@echo "Test coverage report available at: target/site/jacoco/index.html"

clean: ## Clean build artifacts and Docker volumes
	@echo "Cleaning build artifacts..."
	mvn clean
	@echo "Removing Docker containers and volumes..."
	docker-compose -f $(COMPOSE_FILE) down -v

run: ## Start all services (PostgreSQL + WildFly)
	@echo "Starting services..."
	docker-compose -f $(COMPOSE_FILE) up -d
	@echo "Waiting for services to be ready..."
	@sleep 10
	@echo "Application is starting at http://localhost:8080"
	@echo "Health check: http://localhost:8080/api/fx-deals/health"

stop: ## Stop all services
	@echo "Stopping services..."
	docker-compose -f $(COMPOSE_FILE) down

logs: ## Show logs from all services
	docker-compose -f $(COMPOSE_FILE) logs -f

logs-app: ## Show logs from WildFly application only
	docker-compose -f $(COMPOSE_FILE) logs -f wildfly

logs-db: ## Show logs from PostgreSQL only
	docker-compose -f $(COMPOSE_FILE) logs -f postgres

restart: ## Restart all services
	@echo "Restarting services..."
	docker-compose -f $(COMPOSE_FILE) restart

health: ## Check application health status
	@echo "Checking application health..."
	@curl -s http://localhost:8080/api/fx-deals/health || echo "Application is not running"

deploy: build run ## Build and deploy the application
	@echo "Application deployed successfully!"
	@echo "API endpoint: http://localhost:8080/api/fx-deals"

undeploy: stop clean ## Stop and clean everything
	@echo "Application undeployed successfully!"

db-connect: ## Connect to PostgreSQL database
	docker exec -it fxdeals-postgres psql -U fxdeals_user -d fxdeals

sample-request: ## Send a sample FX deal request
	@echo "Sending sample FX deal request..."
	@curl -X POST http://localhost:8080/api/fx-deals \
		-H "Content-Type: application/json" \
		-d '{"dealUniqueId":"DEAL$(shell date +%s)","fromCurrencyIso":"USD","toCurrencyIso":"EUR","dealTimestamp":"2024-01-15T10:30:00","dealAmount":1000.50}'
	@echo ""

list-deals: ## List all FX deals
	@echo "Retrieving all FX deals..."
	@curl -s http://localhost:8080/api/fx-deals | json_pp || curl -s http://localhost:8080/api/fx-deals
	@echo ""

status: ## Show status of all services
	docker-compose -f $(COMPOSE_FILE) ps

dev: ## Start in development mode with live logs
	docker-compose -f $(COMPOSE_FILE) up
