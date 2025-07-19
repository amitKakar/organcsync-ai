#!/bin/bash

# OrganSync AI Scoring Service - API Test Script
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Configuration
BASE_URL="http://localhost:8086/ai-scoring"
API_BASE="$BASE_URL/api/v1/scoring"

print_info "Starting API tests for OrganSync AI Scoring Service"
print_info "Base URL: $BASE_URL"

# Function to test endpoint
test_endpoint() {
    local method=$1
    local endpoint=$2
    local description=$3
    local data=$4

    print_info "Testing: $description"

    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" "$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" \
            -H "Content-Type: application/json" \
            -d "$data" "$endpoint")
    fi

    # Extract HTTP status code
    http_code=$(echo "$response" | tail -n 1)
    response_body=$(echo "$response" | sed '$d')

    if [ "$http_code" -eq 200 ] || [ "$http_code" -eq 201 ]; then
        print_success "$description - HTTP $http_code"
        echo "$response_body" | jq . 2>/dev/null || echo "$response_body"
    else
        print_error "$description - HTTP $http_code"
        echo "$response_body"
    fi

    echo ""
}

# Test 1: Health Check
test_endpoint "GET" "$API_BASE/health" "Health Check"

# Test 2: Service Info
test_endpoint "GET" "$API_BASE/info" "Service Information"

# Test 3: Statistics
test_endpoint "GET" "$API_BASE/statistics" "Scoring Statistics"

# Test 4: Model Performance
test_endpoint "GET" "$API_BASE/model-performance" "Model Performance"

# Test 5: Calculate Compatibility Score
scoring_request='{
  "donorPairId": "550e8400-e29b-41d4-a716-446655440000",
  "recipientPairId": "550e8400-e29b-41d4-a716-446655440001",
  "donorBloodType": "A+",
  "donorAge": 35,
  "donorGender": "M",
  "recipientBloodType": "B+",
  "recipientAge": 42,
  "recipientGender": "F",
  "hlaMismatches": 2,
  "urgencyLevel": "HIGH",
  "calculationMethod": "HYBRID"
}'

test_endpoint "POST" "$API_BASE/calculate" "Calculate Compatibility Score" "$scoring_request"

# Test 6: Batch Scoring
batch_request='[
  {
    "donorPairId": "550e8400-e29b-41d4-a716-446655440002",
    "recipientPairId": "550e8400-e29b-41d4-a716-446655440003",
    "donorBloodType": "O-",
    "donorAge": 28,
    "recipientBloodType": "A+",
    "recipientAge": 35,
    "hlaMismatches": 1,
    "urgencyLevel": "MODERATE",
    "calculationMethod": "COX"
  },
  {
    "donorPairId": "550e8400-e29b-41d4-a716-446655440004",
    "recipientPairId": "550e8400-e29b-41d4-a716-446655440005",
    "donorBloodType": "AB+",
    "donorAge": 45,
    "recipientBloodType": "O+",
    "recipientAge": 38,
    "hlaMismatches": 4,
    "urgencyLevel": "LOW",
    "calculationMethod": "MCDA"
  }
]'

test_endpoint "POST" "$API_BASE/calculate-batch" "Batch Scoring" "$batch_request"

# Test 7: Cached Score (will likely return 404 for new service)
test_endpoint "GET" "$API_BASE/cached/550e8400-e29b-41d4-a716-446655440000/550e8400-e29b-41d4-a716-446655440001" "Cached Score"

# Test 8: Swagger UI availability
print_info "Checking Swagger UI availability"
swagger_response=$(curl -s -w "%{http_code}" -o /dev/null "$BASE_URL/swagger-ui.html")

if [ "$swagger_response" -eq 200 ]; then
    print_success "Swagger UI available at $BASE_URL/swagger-ui.html"
else
    print_error "Swagger UI not available - HTTP $swagger_response"
fi

# Test 9: Actuator endpoints
print_info "Checking Actuator endpoints"
actuator_response=$(curl -s -w "%{http_code}" -o /dev/null "$BASE_URL/actuator/health")

if [ "$actuator_response" -eq 200 ]; then
    print_success "Actuator endpoints available at $BASE_URL/actuator/"
else
    print_error "Actuator endpoints not available - HTTP $actuator_response"
fi

print_info "API testing completed"
print_info "Available endpoints:"
print_info "  - Health: $API_BASE/health"
print_info "  - Info: $API_BASE/info"
print_info "  - Statistics: $API_BASE/statistics"
print_info "  - Model Performance: $API_BASE/model-performance"
print_info "  - Calculate Score: $API_BASE/calculate"
print_info "  - Batch Scoring: $API_BASE/calculate-batch"
print_info "  - Swagger UI: $BASE_URL/swagger-ui.html"
print_info "  - Actuator: $BASE_URL/actuator/"
