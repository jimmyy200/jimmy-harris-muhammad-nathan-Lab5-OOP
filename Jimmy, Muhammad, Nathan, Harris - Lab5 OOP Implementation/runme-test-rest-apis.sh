#!/bin/bash

# =============================================================================
# SIMPLE REST API TESTING SCRIPT
# =============================================================================
# This script demonstrates checking REST API endpoints
# including CRUD operations, search, filtering, and statistics.
# =============================================================================

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080/api/materials"

# Test counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Helper function to run a test and track results
run_test() {
    local test_name="$1"
    local test_command="$2"
    local expected_status="${3:-200}"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -e "${BLUE}Test $TOTAL_TESTS: $test_name${NC}"
    
    # Run the test command and capture both response and status code
    local temp_file=$(mktemp)
    local response=$(eval "$test_command" -w "%{http_code}" -o "$temp_file" 2>/dev/null)
    local status_code=$(echo "$response" | tail -1)
    local actual_response=$(cat "$temp_file" 2>/dev/null)
    rm -f "$temp_file"
    
    if [ "$status_code" = "$expected_status" ]; then
        echo -e "${GREEN} PASSED${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        echo "$actual_response" | jq '.' 2>/dev/null || echo "$actual_response"
    else
        echo -e "${RED} FAILED (Status: $status_code, Expected: $expected_status)${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
        echo "$actual_response" | jq '.' 2>/dev/null || echo "$actual_response"
    fi
    echo ""
}

echo -e "${BLUE}==============================================================================${NC}"
echo -e "${BLUE} REST API TESTING DEMONSTRATION${NC}"
echo -e "${BLUE}==============================================================================${NC}"
echo ""

# Check if the application is running
echo -e "${YELLOW}Checking if Spring Boot application is running...${NC}"
if ! curl -s -f "$BASE_URL" > /dev/null 2>&1; then
    echo -e "${RED}Error: Spring Boot application is not running on localhost:8080${NC}"
    echo -e "${YELLOW}Please start the application first with: ./runme-springboot.sh${NC}"
    exit 1
fi

echo -e "${GREEN} Spring Boot application is running${NC}"
echo ""

# Clear any existing data
echo -e "${CYAN}Clearing existing data for clean testing...${NC}"
curl -s -X DELETE "$BASE_URL/test-book-1" > /dev/null
curl -s -X DELETE "$BASE_URL/ebook-spring-demo" > /dev/null
curl -s -X DELETE "$BASE_URL/9781234567890" > /dev/null
echo ""

# =============================================================================
# REST API TESTING
# =============================================================================

echo -e "${PURPLE} Starting REST API Testing...${NC}"
echo ""

# =============================================================================
# SECTION 1: BASIC CRUD OPERATIONS
# =============================================================================
echo -e "${CYAN} SECTION 1: BASIC CRUD OPERATIONS${NC}"
echo ""

# Test 1: Get all materials (should be empty initially)
run_test "GET /api/materials - Retrieve all materials (initially empty)" \
    "curl -s '$BASE_URL'"

# Test 2: Create a PrintedBook (with correct JSON format)
run_test "POST /api/materials - Create a PrintedBook" \
    "curl -s -X POST '$BASE_URL' -H 'Content-Type: application/json' -d '{
        \"@class\": \"PrintedBook\",
        \"id\": \"9781234567890\",
        \"title\": \"Spring Boot Integration Demo\",
        \"author\": \"Navid Mohaghegh\",
        \"price\": 29.99,
        \"year\": 2024,
        \"pages\": 300,
        \"publisher\": \"University Press\",
        \"hardcover\": true
    }'" "201"

# Test 3: Create an EBook
run_test "POST /api/materials - Create an EBook" \
    "curl -s -X POST '$BASE_URL' -H 'Content-Type: application/json' -d '{
        \"@class\": \"EBook\",
        \"id\": \"ebook-spring-demo\",
        \"title\": \"Spring Boot Best Practices\",
        \"author\": \"Navid Mohaghegh\",
        \"price\": 19.99,
        \"year\": 2024,
        \"fileFormat\": \"EPUB\",
        \"fileSize\": 2.5,
        \"drmEnabled\": false,
        \"wordCount\": 50000,
        \"quality\": \"HIGH\"
    }'" "201"

# Test 4: Create another EBook for testing
run_test "POST /api/materials - Create another EBook" \
    "curl -s -X POST '$BASE_URL' -H 'Content-Type: application/json' -d '{
        \"@class\": \"EBook\",
        \"id\": \"test-book-1\",
        \"title\": \"Test EBook\",
        \"author\": \"Test Author\",
        \"price\": 15.99,
        \"year\": 2024,
        \"fileFormat\": \"PDF\",
        \"fileSize\": 1.5,
        \"drmEnabled\": false,
        \"wordCount\": 30000,
        \"quality\": \"HIGH\"
    }'" "201"

# Test 5: Get all materials (should now have 3 items)
run_test "GET /api/materials - Retrieve all materials (after creation)" \
    "curl -s '$BASE_URL'"

# =============================================================================
# SECTION 2: INDIVIDUAL MATERIAL OPERATIONS
# =============================================================================
echo -e "${CYAN} SECTION 2: INDIVIDUAL MATERIAL OPERATIONS${NC}"
echo ""

# Test 6: Get material by ID
run_test "GET /api/materials/{id} - Get PrintedBook by ID" \
    "curl -s '$BASE_URL/9781234567890'"

# Test 7: Get EBook by ID
run_test "GET /api/materials/{id} - Get EBook by ID" \
    "curl -s '$BASE_URL/ebook-spring-demo'"

# Test 8: Get non-existent material (should return 404)
run_test "GET /api/materials/{id} - Get non-existent material (404 expected)" \
    "curl -s '$BASE_URL/non-existent-id'" "404"

# =============================================================================
# SECTION 3: SEARCH FUNCTIONALITY
# =============================================================================
echo -e "${CYAN} SECTION 3: SEARCH FUNCTIONALITY${NC}"
echo ""

# Test 9: Search by title
run_test "GET /api/materials/search/title?q=Spring - Search by title" \
    "curl -s '$BASE_URL/search/title?q=Spring'"

# Test 10: Search by creator
run_test "GET /api/materials/search/creator?q=Navid - Search by creator" \
    "curl -s '$BASE_URL/search/creator?q=Navid'"

# Test 11: Search for non-existent content
run_test "GET /api/materials/search/title?q=Nonexistent - Search non-existent title" \
    "curl -s '$BASE_URL/search/title?q=Nonexistent'"

# =============================================================================
# SECTION 4: FILTERING AND CATEGORIZATION
# =============================================================================
echo -e "${CYAN} SECTION 4: FILTERING AND CATEGORIZATION${NC}"
echo ""

# Test 12: Get materials by type (EBook)
run_test "GET /api/materials/type/E_BOOK - Get EBooks by type" \
    "curl -s '$BASE_URL/type/E_BOOK'"

# Test 13: Get materials by type (Book)
run_test "GET /api/materials/type/BOOK - Get Books by type" \
    "curl -s '$BASE_URL/type/BOOK'"

# Test 14: Get recent materials
run_test "GET /api/materials/recent?years=5 - Get recent materials" \
    "curl -s '$BASE_URL/recent?years=5'"

# Test 15: Get materials by price range
run_test "GET /api/materials/price-range?min=10&max=30 - Get materials by price range" \
    "curl -s '$BASE_URL/price-range?min=10&max=30'"

# Test 16: Get materials by price range (no matches)
run_test "GET /api/materials/price-range?min=100&max=200 - Get materials by price range (no matches)" \
    "curl -s '$BASE_URL/price-range?min=100&max=200'"

# =============================================================================
# SECTION 5: STATISTICS AND ANALYTICS
# =============================================================================
echo -e "${CYAN} SECTION 5: STATISTICS AND ANALYTICS${NC}"
echo ""

# Test 17: Get inventory statistics
run_test "GET /api/materials/stats - Get inventory statistics" \
    "curl -s '$BASE_URL/stats'"

# Test 18: Get material count
run_test "GET /api/materials/count - Get material count" \
    "curl -s '$BASE_URL/count'"

# =============================================================================
# SECTION 6: UPDATE OPERATIONS
# =============================================================================
echo -e "${CYAN} SECTION 6: UPDATE OPERATIONS${NC}"
echo ""

# Test 19: Update a PrintedBook
run_test "PUT /api/materials/{id} - Update PrintedBook" \
    "curl -s -X PUT '$BASE_URL/9781234567890' -H 'Content-Type: application/json' -d '{
        \"@class\": \"PrintedBook\",
        \"id\": \"9781234567890\",
        \"title\": \"Updated Spring Boot Integration Demo\",
        \"author\": \"Navid Mohaghegh\",
        \"price\": 34.99,
        \"year\": 2024,
        \"pages\": 350,
        \"publisher\": \"University Press\",
        \"hardcover\": true
    }'"

# Test 20: Update an EBook
run_test "PUT /api/materials/{id} - Update EBook" \
    "curl -s -X PUT '$BASE_URL/ebook-spring-demo' -H 'Content-Type: application/json' -d '{
        \"@class\": \"EBook\",
        \"id\": \"ebook-spring-demo\",
        \"title\": \"Updated Spring Boot Best Practices\",
        \"author\": \"Navid Mohaghegh\",
        \"price\": 24.99,
        \"year\": 2024,
        \"fileFormat\": \"EPUB\",
        \"fileSize\": 3.0,
        \"drmEnabled\": false,
        \"wordCount\": 60000,
        \"quality\": \"HIGH\"
    }'"

# Test 21: Update non-existent material (should return 404)
run_test "PUT /api/materials/{id} - Update non-existent material (404 expected)" \
    "curl -s -X PUT '$BASE_URL/non-existent-id' -H 'Content-Type: application/json' -d '{
        \"@class\": \"EBook\",
        \"id\": \"non-existent-id\",
        \"title\": \"Test\",
        \"author\": \"Test\",
        \"price\": 10.0,
        \"year\": 2024,
        \"fileFormat\": \"PDF\",
        \"fileSize\": 1.0,
        \"drmEnabled\": false,
        \"wordCount\": 1000,
        \"quality\": \"HIGH\"
    }'" "404"

# =============================================================================
# SECTION 7: DELETE OPERATIONS
# =============================================================================
echo -e "${CYAN} SECTION 7: DELETE OPERATIONS${NC}"
echo ""

# Test 22: Delete a material
run_test "DELETE /api/materials/{id} - Delete EBook" \
    "curl -s -X DELETE '$BASE_URL/test-book-1'" "204"

# Test 23: Delete non-existent material (should return 404)
run_test "DELETE /api/materials/{id} - Delete non-existent material (404 expected)" \
    "curl -s -X DELETE '$BASE_URL/non-existent-id'" "404"

# Test 24: Verify deletion worked
run_test "GET /api/materials - Verify deletion (should have 2 items now)" \
    "curl -s '$BASE_URL'"

# =============================================================================
# SECTION 8: FINAL VERIFICATION AND STATISTICS
# =============================================================================
echo -e "${CYAN} SECTION 8: FINAL VERIFICATION AND STATISTICS${NC}"
echo ""

# Test 25: Final statistics
run_test "GET /api/materials/stats - Final inventory statistics" \
    "curl -s '$BASE_URL/stats'"

# Test 26: Final count
run_test "GET /api/materials/count - Final material count" \
    "curl -s '$BASE_URL/count'"

# Test 27: Final search to verify remaining materials
run_test "GET /api/materials/search/title?q=Spring - Final search for Spring materials" \
    "curl -s '$BASE_URL/search/title?q=Spring'"

# =============================================================================
# TEST RESULTS SUMMARY
# =============================================================================
echo -e "${BLUE}==============================================================================${NC}"
echo -e "${BLUE}TEST RESULTS SUMMARY${NC}"
echo -e "${BLUE}==============================================================================${NC}"
echo ""

echo -e "${YELLOW}Total Tests: $TOTAL_TESTS${NC}"
echo -e "${GREEN}Passed: $PASSED_TESTS${NC}"
echo -e "${RED}Failed: $FAILED_TESTS${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN} ALL TESTS PASSED! ${NC}"
    echo ""
    echo -e "${GREEN}==============================================================================${NC}"
    echo -e "${GREEN}REST API TESTING COMPLETED SUCCESSFULLY!${NC}"
    echo -e "${GREEN}==============================================================================${NC}"
else
    echo -e "${RED}  SOME TESTS FAILED ${NC}"
    echo ""
    echo -e "${RED}==============================================================================${NC}"
    echo -e "${RED}REST API TESTING COMPLETED WITH ISSUES${NC}"
    echo -e "${RED}==============================================================================${NC}"
fi

echo ""
echo -e "${YELLOW}This demonstration shows how the existing MaterialStore interface${NC}"
echo -e "${YELLOW}can be easily extended with REST API capabilities using Spring Boot${NC}"
echo -e "${YELLOW}without modifying any existing business logic or design patterns.${NC}"
echo ""
echo -e "${BLUE}Key Benefits Demonstrated:${NC}"
echo -e "   ${GREEN}Extensibility:${NC} Added REST API without changing existing code"
echo -e "   ${GREEN}Scalability:${NC} Can easily switch between MaterialStore implementations"
echo -e "   ${GREEN}Maintainability:${NC} REST layer is isolated and focused"
echo -e "   ${GREEN}Testability:${NC} REST API can be tested independently"
echo -e "   ${GREEN}Completeness:${NC} Full CRUD operations with search and filtering"
echo -e "   ${GREEN}Robustness:${NC} Proper error handling and status codes"
echo ""
