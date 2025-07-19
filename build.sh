#!/bin/bash

# OrganSync AI Scoring Service - Build and Deployment Script
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Project information
PROJECT_NAME="OrganSync AI Scoring Service"
SERVICE_NAME="organsync-ai-scoring"
VERSION="1.0.0"

print_info "Starting build process for $PROJECT_NAME v$VERSION"

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check prerequisites
check_prerequisites() {
    print_info "Checking prerequisites..."

    if ! command_exists java; then
        print_error "Java is not installed. Please install Java 21 or higher."
        exit 1
    fi

    if ! command_exists mvn && ! command_exists ./mvnw; then
        print_error "Maven is not installed and Maven wrapper is not found."
        exit 1
    fi

    if ! command_exists docker; then
        print_warning "Docker is not installed. Some operations may not be available."
    fi

    print_success "Prerequisites check completed"
}

# Function to clean project
clean() {
    print_info "Cleaning project..."

    if [ -f "./mvnw" ]; then
        ./mvnw clean
    else
        mvn clean
    fi

    # Clean Docker containers and images
    if command_exists docker; then
        docker system prune -f || true
    fi

    print_success "Project cleaned"
}

# Function to build project
build() {
    print_info "Building project..."

    if [ -f "./mvnw" ]; then
        ./mvnw compile
    else
        mvn compile
    fi

    print_success "Project built successfully"
}

# Function to run tests
test() {
    print_info "Running tests..."

    if [ -f "./mvnw" ]; then
        ./mvnw test
    else
        mvn test
    fi

    print_success "Tests completed"
}

# Function to package application
package() {
    print_info "Packaging application..."

    if [ -f "./mvnw" ]; then
        ./mvnw package -DskipTests
    else
        mvn package -DskipTests
    fi

    print_success "Application packaged successfully"
}

# Function to build Docker image
build_docker() {
    print_info "Building Docker image..."

    if ! command_exists docker; then
        print_error "Docker is not installed"
        exit 1
    fi

    docker build -t $SERVICE_NAME:$VERSION .
    docker tag $SERVICE_NAME:$VERSION $SERVICE_NAME:latest

    print_success "Docker image built successfully"
}

# Function to run application locally
run_local() {
    print_info "Running application locally..."

    if [ -f "./mvnw" ]; then
        ./mvnw spring-boot:run
    else
        mvn spring-boot:run
    fi
}

# Function to run with Docker
run_docker() {
    print_info "Running application with Docker..."

    if ! command_exists docker; then
        print_error "Docker is not installed"
        exit 1
    fi

    docker run -p 8086:8086 --name $SERVICE_NAME $SERVICE_NAME:latest
}

# Function to run full stack
run_stack() {
    print_info "Starting full infrastructure stack..."

    if ! command_exists docker-compose && ! command_exists docker; then
        print_error "Docker Compose is not installed"
        exit 1
    fi

    # Create necessary directories
    mkdir -p logs
    mkdir -p docker/prometheus
    mkdir -p docker/grafana/provisioning
    mkdir -p docker/grafana/dashboards
    mkdir -p docker/nginx
    mkdir -p docker/mongodb/init-scripts

    # Start infrastructure
    docker-compose up -d

    print_success "Full stack started successfully"
    print_info "Services available at:"
    print_info "  - AI Scoring Service: http://localhost:8086/ai-scoring"
    print_info "  - Swagger UI: http://localhost:8086/ai-scoring/swagger-ui.html"
    print_info "  - MongoDB Express: http://localhost:8081"
    print_info "  - Redis Commander: http://localhost:8082"
    print_info "  - Kafka UI: http://localhost:8080"
    print_info "  - Prometheus: http://localhost:9090"
    print_info "  - Grafana: http://localhost:3000 (admin/admin)"
    print_info "  - Jaeger: http://localhost:16686"
    print_info "  - Kibana: http://localhost:5601"
}

# Function to stop stack
stop_stack() {
    print_info "Stopping infrastructure stack..."

    if command_exists docker-compose; then
        docker-compose down
    elif command_exists docker; then
        docker stop $(docker ps -q) || true
    fi

    print_success "Infrastructure stack stopped"
}

# Function to show logs
show_logs() {
    print_info "Showing application logs..."

    if command_exists docker-compose; then
        docker-compose logs -f ai-scoring-service
    else
        print_warning "Docker Compose not available. Check logs manually."
    fi
}

# Function to run development setup
dev_setup() {
    print_info "Setting up development environment..."

    # Build the application
    build

    # Start only essential services
    docker-compose up -d mongodb redis kafka zookeeper

    # Wait for services to be ready
    sleep 30

    # Run application locally
    run_local
}

# Function to run production setup
prod_setup() {
    print_info "Setting up production environment..."

    # Build and package
    clean
    build
    test
    package

    # Build Docker image
    build_docker

    # Run full stack
    run_stack

    print_success "Production setup completed"
}

# Function to show help
show_help() {
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  clean          Clean project and Docker resources"
    echo "  build          Build the project"
    echo "  test           Run tests"
    echo "  package        Package the application"
    echo "  build-docker   Build Docker image"
    echo "  run-local      Run application locally"
    echo "  run-docker     Run application with Docker"
    echo "  run-stack      Run full infrastructure stack"
    echo "  stop-stack     Stop infrastructure stack"
    echo "  logs           Show application logs"
    echo "  dev-setup      Setup development environment"
    echo "  prod-setup     Setup production environment"
    echo "  help           Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 dev-setup   # Quick start for development"
    echo "  $0 prod-setup  # Complete production setup"
    echo "  $0 build-docker && $0 run-stack  # Build and run"
}

# Main script logic
case "$1" in
    clean)
        check_prerequisites
        clean
        ;;
    build)
        check_prerequisites
        build
        ;;
    test)
        check_prerequisites
        test
        ;;
    package)
        check_prerequisites
        package
        ;;
    build-docker)
        check_prerequisites
        build_docker
        ;;
    run-local)
        check_prerequisites
        run_local
        ;;
    run-docker)
        check_prerequisites
        run_docker
        ;;
    run-stack)
        check_prerequisites
        run_stack
        ;;
    stop-stack)
        stop_stack
        ;;
    logs)
        show_logs
        ;;
    dev-setup)
        check_prerequisites
        dev_setup
        ;;
    prod-setup)
        check_prerequisites
        prod_setup
        ;;
    help|--help|-h)
        show_help
        ;;
    "")
        print_info "No command specified. Running development setup..."
        check_prerequisites
        dev_setup
        ;;
    *)
        print_error "Unknown command: $1"
        show_help
        exit 1
        ;;
esac

print_success "Script completed successfully"
