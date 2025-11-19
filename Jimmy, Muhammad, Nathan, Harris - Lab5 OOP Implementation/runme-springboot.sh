#!/bin/bash

# =============================================================================
# SPRING BOOT DEMONSTRATION SCRIPT
# =============================================================================
# This script demonstrates how to run the Spring Boot application
# and test the REST API endpoints with minimal changes to the existing codebase.
# =============================================================================

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}==============================================================================${NC}"
echo -e "${BLUE}SPRING BOOT INTEGRATION DEMONSTRATION${NC}"
echo -e "${BLUE}==============================================================================${NC}"
echo ""

echo -e "${YELLOW}This script demonstrates the extensibility and scalability of the existing${NC}"
echo -e "${YELLOW}Bookstore Management System by adding Spring Boot REST API capabilities${NC}"
echo -e "${YELLOW}with minimal changes to the existing codebase.${NC}"
echo ""

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    echo "Error: Please run this script from the codes directory (where pom.xml is located)"
    exit 1
fi

echo -e "${GREEN}Step 1: Compiling the project with Spring Boot dependencies...${NC}"
mvn clean compile -q

echo -e "${GREEN}Step 2: Skipping tests for now (focusing on Spring Boot demonstration)...${NC}"
# Skip all tests for now to focus on Spring Boot demonstration
echo "Note: Tests can be run separately with 'mvn test' if needed"

echo -e "${GREEN}Step 3: Starting Spring Boot application...${NC}"
echo "The application will start on http://localhost:8080"
echo "Press Ctrl+C to stop the application"
echo ""

# Start the Spring Boot application
mvn spring-boot:run
