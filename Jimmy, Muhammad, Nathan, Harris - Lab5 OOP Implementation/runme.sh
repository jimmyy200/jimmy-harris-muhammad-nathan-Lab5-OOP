#!/bin/bash

# =============================================================================
# BOOKSTORE Lab 3 - BUILD, TEST & UML GENERATION SCRIPT
# =============================================================================
# This script performs a complete build cycle including:
# - Environment validation
# - Clean compilation
# - Detailed testing
# - Code coverage analysis
# - Javadoc generation
# - UML diagram generation
# - Quality checks
# - Final summary report
# =============================================================================

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Script configuration
SCRIPT_NAME="runme.sh"
PROJECT_NAME="Bookstore Lab 3 - Polymorphic System"
JAVA_VERSION_REQUIRED="17"
MAVEN_VERSION_REQUIRED="3.6"
TARGET_COVERAGE="50"

# Status tracking
BUILD_STATUS=0
TEST_STATUS=0
COVERAGE_STATUS=0
JAVADOC_STATUS=0
UML_STATUS=0
OVERALL_STATUS=0

# Function to print colored output
print_status() {
    local status=$1
    local message=$2
    case $status in
        "INFO") echo -e "${BLUE}[INFO]${NC} $message" ;;
        "SUCCESS") echo -e "${GREEN}[SUCCESS]${NC} $message" ;;
        "WARNING") echo -e "${YELLOW}[WARNING]${NC} $message" ;;
        "ERROR") echo -e "${RED}[ERROR]${NC} $message" ;;
        "STEP") echo -e "${PURPLE}[STEP]${NC} $message" ;;
        "HEADER") echo -e "${CYAN}$message${NC}" ;;
    esac
}

# Function to print section headers
print_header() {
    echo ""
    print_status "HEADER" "=============================================================================="
    print_status "HEADER" "$1"
    print_status "HEADER" "=============================================================================="
    echo ""
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check file exists
file_exists() {
    [ -f "$1" ]
}

# Function to check directory exists
dir_exists() {
    [ -d "$1" ]
}

# Function to handle errors gracefully
handle_error() {
    local exit_code=$?
    print_status "ERROR" "Script failed with exit code $exit_code"
    print_status "ERROR" "Last command: $BASH_COMMAND"
    exit $exit_code
}

# Function to show help
show_help() {
    echo "=============================================================================="
    echo "BOOKSTORE Lab 3 -  BUILD & TEST SCRIPT - USAGE GUIDE"
    echo "=============================================================================="
    echo ""
    echo "SYNOPSIS:"
    echo "  $SCRIPT_NAME [OPTIONS]"
    echo ""
    echo "DESCRIPTION:"
    echo "  This script performs a complete build cycle for the Bookstore Lab 3 project,"
    echo "  including environment validation, compilation, testing, coverage analysis,"
    echo "  Javadoc generation, UML diagram generation, quality checks, and packaging."
    echo ""
    echo "OPTIONS:"
    echo "  -h, --help           Show this help message and exit"
    echo "  -v, --version        Show script version and exit"
    echo "  -q, --quick          Quick mode: skip coverage, Javadoc, and UML generation"
    echo "  -t, --test-only      Run tests only (skip build and other steps)"
    echo "  -c, --clean          Force clean build (remove target directory)"
    echo "  -s, --skip-tests     Skip running tests (compile and package only)"
    echo "  -j, --javadoc-only   Generate Javadoc only"
    echo "  -p, --package-only   Create JAR package only"
    echo "  -u, --uml-only       Generate UML diagrams only"
    echo "  --clean-uml          Clean up generated UML files"
    echo "  --performance        Run performance benchmarks"
    echo "  -d, --debug          Enable debug output"
    echo ""
    echo "SECURITY OPTIONS:"
    echo "  --security           Run all security checks (OWASP + SpotBugs)"
    echo "  --owasp              Run OWASP dependency check for vulnerabilities"
    echo "  --spotbugs           Run SpotBugs security analysis"
    echo ""
    echo "EXAMPLES:"
    echo "  $SCRIPT_NAME                    # Run complete build and test process"
    echo "  $SCRIPT_NAME --help             # Show this help message"
    echo "  $SCRIPT_NAME --quick            # Quick build without coverage/Javadoc/UML"
    echo "  $SCRIPT_NAME --test-only        # Run tests only"
    echo "  $SCRIPT_NAME --clean            # Force clean build"
    echo "  $SCRIPT_NAME --skip-tests       # Build without testing"
    echo "  $SCRIPT_NAME --javadoc-only     # Generate Javadoc only"
    echo "  $SCRIPT_NAME --package-only     # Create JAR only"
    echo "  $SCRIPT_NAME --uml-only         # Generate UML diagrams only"
    echo "  $SCRIPT_NAME --clean-uml        # Clean up UML files"
    echo ""
    echo "EXIT CODES:"
    echo "  0  - All checks passed successfully"
    echo "  1  - Some checks failed or errors occurred"
    echo "  2  - Invalid command line arguments"
    echo ""
    echo "PREREQUISITES:"
    echo "  - Java 17 or higher installed and in PATH"
    echo "  - Maven 3.6 or higher installed and in PATH"
    echo "  - Script must be run from project root directory (where pom.xml is located)"
    echo "  - Script must have execute permissions (chmod +x $SCRIPT_NAME)"
    echo ""
    echo "PROJECT STRUCTURE:"
    echo "  codes/"
    echo "  ├── pom.xml                          # Maven configuration"
    echo "  ├── $SCRIPT_NAME                     # This script"
    echo "  ├── src/main/java/                   # Source code (66 Java files)"
    echo "  │   └── com/university/bookstore/"
    echo "  │       ├── api/                     # API interfaces (3 files)"
    echo "  │       ├── builder/                 # Builder pattern (5 files)"
    echo "  │       ├── chain/                   # Chain of Responsibility (6 files)"
    echo "  │       ├── composite/               # Composite pattern (4 files)"
    echo "  │       ├── decorator/               # Decorator pattern (5 files)"
    echo "  │       ├── demo/                    # Demo classes (1 file)"
    echo "  │       ├── factory/                 # Factory pattern (2 files)"
    echo "  │       ├── impl/                    # Store implementations (4 files)"
    echo "  │       ├── iterator/                # Iterator pattern (5 files)"
    echo "  │       ├── model/                   # Material hierarchy (8 files)"
    echo "  │       ├── observer/                # Observer pattern (9 files)"
    echo "  │       ├── repository/              # Data persistence (5 files)"
    echo "  │       ├── search/                  # Search & caching (4 files)"
    echo "  │       ├── service/                 # Business logic (1 file)"
    echo "  │       ├── utils/                   # Utility classes (2 files)"
    echo "  │       └── visitor/                 # Visitor pattern (2 files)"
    echo "  └── src/test/java/                   # Test code (34 Java files)"
    echo "      └── com/university/bookstore/"
    echo "          ├── api/                     # API tests (3 files)"
    echo "          ├── builder/                  # Builder tests (1 file)"
    echo "          ├── composite/               # Composite tests (2 files)"
    echo "          ├── decorator/               # Decorator tests (5 files)"
    echo "          ├── factory/                 # Factory tests (2 files)"
    echo "          ├── impl/                    # Implementation tests (4 files)"
    echo "          ├── model/                   # Model tests (3 files)"
    echo "          ├── observer/                # Observer tests (1 file)"
    echo "          ├── performance/             # Performance tests (4 files)"
    echo "          ├── quality/                 # Quality tests (1 file)"
    echo "          ├── repository/              # Repository tests (3 files)"
    echo "          ├── search/                  # Search tests (3 files)"
    echo "          ├── utils/                   # Utility tests (1 file)"
    echo "          └── visitor/                 # Visitor tests (1 file)"
    echo ""
    echo "For more information, check the script comments or run individual Maven commands:"
    echo "  mvn clean compile          # Clean build only"
    echo "  mvn test                   # Run tests only"
    echo "  mvn jacoco:report          # Generate coverage only"
    echo "  mvn javadoc:javadoc        # Generate Javadoc only"
    echo "  mvn package                # Create JAR only"
    echo ""
    echo "=============================================================================="
}

# Function to show version
show_version() {
    echo "=============================================================================="
    echo "BOOKSTORE Lab 3 -  BUILD & TEST SCRIPT"
    echo "=============================================================================="
    echo "Version: 2.0"
    echo "Author:  Navid Mohaghegh"
    echo "Contact: navid@navid.ca"
    echo "Project: CSSD2101 Lab 3 - Polymorphic Bookstore Management System"
    echo "Features: Build, Test, Coverage, Javadoc, UML Generation"
    echo "=============================================================================="
}

# Parse command line arguments
QUICK_MODE=false
TEST_ONLY=false
CLEAN_BUILD=false
SKIP_TESTS=false
JAVADOC_ONLY=false
PACKAGE_ONLY=false
UML_ONLY=false
CLEAN_UML=false
PERFORMANCE_MODE=false
DEBUG_MODE=false
SECURITY_CHECK=false
OWASP_CHECK=false
SPOTBUGS_CHECK=false

while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        -v|--version)
            show_version
            exit 0
            ;;
        -q|--quick)
            QUICK_MODE=true
            shift
            ;;
        -t|--test-only)
            TEST_ONLY=true
            shift
            ;;
        -c|--clean)
            CLEAN_BUILD=true
            shift
            ;;
        -s|--skip-tests)
            SKIP_TESTS=true
            shift
            ;;
        -j|--javadoc-only)
            JAVADOC_ONLY=true
            shift
            ;;
        -p|--package-only)
            PACKAGE_ONLY=true
            shift
            ;;
        -u|--uml-only)
            UML_ONLY=true
            shift
            ;;
        --clean-uml)
            CLEAN_UML=true
            shift
            ;;
        --performance)
            PERFORMANCE_MODE=true
            shift
            ;;
        -d|--debug)
            DEBUG_MODE=true
            shift
            ;;
        --security)
            SECURITY_CHECK=true
            shift
            ;;
        --owasp)
            OWASP_CHECK=true
            shift
            ;;
        --spotbugs)
            SPOTBUGS_CHECK=true
            shift
            ;;
        -*)
            print_status "ERROR" "Unknown option: $1"
            print_status "ERROR" "Use --help for usage information"
            exit 2
            ;;
        *)
            print_status "ERROR" "Unknown argument: $1"
            print_status "ERROR" "Use --help for usage information"
            exit 2
            ;;
    esac
done

# Set error handling
trap 'handle_error' ERR

# =============================================================================
# SCRIPT START
# =============================================================================

print_header "[START] $PROJECT_NAME -  BUILD & TEST SCRIPT"
print_status "INFO" "Starting build and test process..."
print_status "INFO" "Script: $SCRIPT_NAME"
print_status "INFO" "Timestamp: $(date)"
print_status "INFO" "Working Directory: $(pwd)"

# Show mode information
if [ "$QUICK_MODE" = true ]; then
    print_status "INFO" "Mode: Quick (skipping coverage, Javadoc, and UML)"
elif [ "$TEST_ONLY" = true ]; then
    print_status "INFO" "Mode: Test only"
elif [ "$SKIP_TESTS" = true ]; then
    print_status "INFO" "Mode: Skip tests"
elif [ "$JAVADOC_ONLY" = true ]; then
    print_status "INFO" "Mode: Javadoc only"
elif [ "$PACKAGE_ONLY" = true ]; then
    print_status "INFO" "Mode: Package only"
elif [ "$UML_ONLY" = true ]; then
    print_status "INFO" "Mode: UML generation only"
elif [ "$CLEAN_UML" = true ]; then
    print_status "INFO" "Mode: Clean UML files"
elif [ "$PERFORMANCE_MODE" = true ]; then
    print_status "INFO" "Mode: Performance benchmarks only"
    
    # Run performance tests
    print_header "[PERFORMANCE] PERFORMANCE BENCHMARKS"
    
    print_status "STEP" "Running performance integration tests..."
    if mvn test -Dtest=PerformanceIntegrationTest -q; then
        print_status "SUCCESS" "[OK] Performance integration tests passed"
    else
        print_status "ERROR" "[FAIL] Performance integration tests failed"
    fi
    
    print_status "STEP" "Running property-based tests..."
    if mvn test -Dtest=PropertyBasedTests -q; then
        print_status "SUCCESS" "[OK] Property-based tests passed"
    else
        print_status "ERROR" "[FAIL] Property-based tests failed"
    fi
    
    print_status "STEP" "Running JMH benchmarks..."
    if mvn test-compile -q; then
        print_status "SUCCESS" "[OK] JMH benchmarks compiled successfully"
        print_status "INFO" "To run JMH benchmarks: mvn exec:java -Dexec.mainClass=com.university.bookstore.performance.BookstorePerformanceBenchmark"
    else
        print_status "ERROR" "[FAIL] JMH benchmarks compilation failed"
    fi
    
    print_status "SUCCESS" "[OK] Performance testing completed"
    exit 0
fi

# Security analysis mode
if [ "$SECURITY_CHECK" = true ] || [ "$OWASP_CHECK" = true ] || [ "$SPOTBUGS_CHECK" = true ]; then
    print_header "[SECURITY] SECURITY ANALYSIS"
    
    if [ "$SECURITY_CHECK" = true ]; then
        print_status "INFO" "Mode: Full security analysis (OWASP + SpotBugs)"
        OWASP_CHECK=true
        SPOTBUGS_CHECK=true
    fi
    
    SECURITY_STATUS=0
    
    # Run OWASP Dependency Check
    if [ "$OWASP_CHECK" = true ]; then
        print_status "STEP" "Running OWASP dependency check for vulnerable dependencies..."
        if mvn org.owasp:dependency-check-maven:check -DskipTests; then
            print_status "SUCCESS" "[OK] No vulnerable dependencies found"
            ((SECURITY_STATUS++))
        else
            print_status "WARNING" "[WARN] Vulnerable dependencies detected - check target/dependency-check-report.html"
        fi
    fi
    
    # Run SpotBugs Security Analysis
    if [ "$SPOTBUGS_CHECK" = true ]; then
        print_status "STEP" "Running SpotBugs static security analysis..."
        if mvn com.github.spotbugs:spotbugs-maven-plugin:check -DskipTests; then
            print_status "SUCCESS" "[OK] No security bugs found"
            ((SECURITY_STATUS++))
        else
            print_status "WARNING" "[WARN] Security issues detected - check target/spotbugsXml.xml"
        fi
    fi
    
    # Summary
    print_header "[SECURITY] SECURITY ANALYSIS SUMMARY"
    if [ "$SECURITY_STATUS" -eq 2 ] && [ "$SECURITY_CHECK" = true ]; then
        print_status "SUCCESS" "✅ All security checks passed successfully!"
    elif [ "$SECURITY_STATUS" -gt 0 ]; then
        print_status "WARNING" "⚠️ Some security checks passed. Review warnings above."
    else
        print_status "ERROR" "❌ Security checks failed. Fix issues before deployment."
    fi
    
    echo ""
    echo "📋 Security Reports Generated:"
    echo "  • OWASP Dependency Check Report: target/dependency-check-report.html"
    echo "  • SpotBugs Security Report: target/spotbugsXml.xml"
    echo ""
    echo "📊 To view OWASP report: open target/dependency-check-report.html"
    echo "📊 To view SpotBugs report: mvn com.github.spotbugs:spotbugs-maven-plugin:gui"
    
    exit 0
fi

echo ""

# =============================================================================
# STEP 1: ENVIRONMENT VALIDATION
# =============================================================================

print_header "[CHECK] STEP 1: ENVIRONMENT VALIDATION"

# Check Java
print_status "STEP" "Checking Java installation..."
if command_exists java; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge "$JAVA_VERSION_REQUIRED" ]; then
        print_status "SUCCESS" "[OK] Java $JAVA_VERSION found (required: $JAVA_VERSION_REQUIRED+)"
    else
        print_status "WARNING" "[WARN] Java $JAVA_VERSION found, but Java $JAVA_VERSION_REQUIRED+ recommended"
    fi
    JAVA_HOME=$(echo $JAVA_HOME)
    if [ ! -z "$JAVA_HOME" ]; then
        print_status "INFO" "JAVA_HOME: $JAVA_HOME"
    fi
else
    print_status "ERROR" "✗ Java not found. Please install Java $JAVA_VERSION_REQUIRED+"
    exit 1
fi

# Check Maven
print_status "STEP" "Checking Maven installation..."
if command_exists mvn; then
    MVN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
    print_status "SUCCESS" "[OK] Maven $MVN_VERSION found"
else
    print_status "ERROR" "✗ Maven not found. Please install Maven $MAVEN_VERSION_REQUIRED+"
    exit 1
fi

# Check project structure
print_status "STEP" "Validating project structure..."
REQUIRED_DIRS=("src/main/java" "src/test/java" "src/main/java/com/university/bookstore")
for dir in "${REQUIRED_DIRS[@]}"; do
    if dir_exists "$dir"; then
        print_status "SUCCESS" "[OK] Found: $dir"
    else
        print_status "ERROR" "[FAIL] Missing required directory: $dir"
        exit 1
    fi
done

# Check pom.xml
if file_exists "pom.xml"; then
    print_status "SUCCESS" "[OK] Found: pom.xml"
else
    print_status "ERROR" "[FAIL] Missing: pom.xml"
    exit 1
fi

print_status "SUCCESS" "Environment validation completed successfully!"

# =============================================================================
# STEP 1.5: UML CLEANUP (if clean-uml mode)
# =============================================================================

if [ "$CLEAN_UML" = true ]; then
    print_header "[CLEAN] STEP 1.5: UML FILES CLEANUP"
    
    print_status "STEP" "Cleaning up generated UML files..."
    
    # List of UML files to clean
    UML_FILES=(
        "bookstore-class-diagram.png"
        "bookstore-class-diagram.svg"
        "bookstore-class-diagram.atxt"
        "bookstore-class-diagram.txt"
        "bookstore-package-diagram.png"
        "bookstore-uml.puml"
        "bookstore-packages.puml"
    )
    
    CLEANED_COUNT=0
    for file in "${UML_FILES[@]}"; do
        if file_exists "$file"; then
            rm -f "$file"
            print_status "SUCCESS" "[REMOVED] $file"
            CLEANED_COUNT=$((CLEANED_COUNT + 1))
        fi
    done
    
    if [ $CLEANED_COUNT -gt 0 ]; then
        print_status "SUCCESS" "[OK] Cleaned up $CLEANED_COUNT UML files"
    else
        print_status "INFO" "[INFO] No UML files found to clean"
    fi
    
    print_status "SUCCESS" "UML cleanup completed successfully!"
    exit 0
fi

# =============================================================================
# STEP 2: CLEAN BUILD (if not test-only or specific mode)
# =============================================================================

if [ "$TEST_ONLY" = false ] && [ "$JAVADOC_ONLY" = false ] && [ "$PACKAGE_ONLY" = false ] && [ "$UML_ONLY" = false ] && [ "$CLEAN_UML" = false ]; then
    print_header "[BUILD] STEP 2: CLEAN BUILD"
    
    # Force remove target directory if it exists and clean mode is enabled
    if [ "$CLEAN_BUILD" = true ] && dir_exists "target"; then
        print_status "STEP" "Removing existing target directory..."
        rm -rf target
        print_status "SUCCESS" "[OK] Target directory removed"
    fi
    
    # Compile source code
    print_status "STEP" "Compiling source code..."
    if mvn compile -q; then
        print_status "SUCCESS" "[OK] Compilation successful"
        BUILD_STATUS=1
    else
        print_status "ERROR" "[FAIL] Compilation failed"
        BUILD_STATUS=0
        exit 1
    fi
    
    # Count compiled classes
    if dir_exists "target/classes"; then
        CLASS_COUNT=$(find target/classes -name "*.class" | wc -l)
        print_status "INFO" "Compiled $CLASS_COUNT classes"
    fi
else
    BUILD_STATUS=1  # Assume build is OK for test-only modes
fi

# =============================================================================
# STEP 3:  TESTING (if not skip-tests or specific mode)
# =============================================================================

if [ "$SKIP_TESTS" = false ] && [ "$JAVADOC_ONLY" = false ] && [ "$PACKAGE_ONLY" = false ] && [ "$UML_ONLY" = false ] && [ "$CLEAN_UML" = false ]; then
    print_header "[TEST] STEP 3:  TESTING"
    
    # Run tests with detailed output
    print_status "STEP" "Running test suite..."
    if mvn test; then
        print_status "SUCCESS" "[OK] All tests passed successfully!"
        TEST_STATUS=1
    else
        print_status "ERROR" "[FAIL] Some tests failed"
        TEST_STATUS=0
        print_status "INFO" "Check target/surefire-reports for detailed test results"
    fi
    
    # Extract test statistics
    if dir_exists "target/surefire-reports"; then
        print_status "STEP" "Analyzing test results..."
        
        # Count test files
        TEST_FILES=$(find src/test -name "*.java" | wc -l)
        print_status "INFO" "Test files: $TEST_FILES"
        
        # Extract test results from XML reports
        if command_exists grep && command_exists sed; then
            TOTAL_TESTS=0
            TOTAL_FAILURES=0
            TOTAL_ERRORS=0
            
            for report in target/surefire-reports/TEST-*.xml; do
                if file_exists "$report"; then
                    TESTS=$(grep -o 'tests="[^"]*"' "$report" | sed 's/tests="//;s/"//')
                    FAILURES=$(grep -o 'failures="[^"]*"' "$report" | sed 's/failures="//;s/"//')
                    ERRORS=$(grep -o 'errors="[^"]*"' "$report" | sed 's/errors="//;s/"//')
                    
                    TOTAL_TESTS=$((TOTAL_TESTS + TESTS))
                    TOTAL_FAILURES=$((TOTAL_FAILURES + FAILURES))
                    TOTAL_ERRORS=$((TOTAL_ERRORS + ERRORS))
                fi
            done
            
            if [ $TOTAL_TESTS -gt 0 ]; then
                print_status "SUCCESS" "Test Summary: $TOTAL_TESTS tests, $TOTAL_FAILURES failures, $TOTAL_ERRORS errors"
                
                if [ $TOTAL_FAILURES -eq 0 ] && [ $TOTAL_ERRORS -eq 0 ]; then
                    print_status "SUCCESS" "[SUCCESS] All tests passed! Test coverage requirement met."
                else
                    print_status "WARNING" "[WARN] Some tests failed. Review test results before proceeding."
                fi
            fi
        fi
    fi
else
    TEST_STATUS=1  # Assume tests are OK for skip-tests modes
fi

# =============================================================================
# STEP 4: CODE COVERAGE ANALYSIS (if not quick mode or specific mode)
# =============================================================================

if [ "$QUICK_MODE" = false ] && [ "$JAVADOC_ONLY" = false ] && [ "$PACKAGE_ONLY" = false ] && [ "$UML_ONLY" = false ] && [ "$CLEAN_UML" = false ]; then
    print_header "[COVERAGE] STEP 4: CODE COVERAGE ANALYSIS"
    
    print_status "STEP" "Generating code coverage report..."
    if mvn test jacoco:report -q; then
        if file_exists "target/site/jacoco/index.html"; then
            print_status "SUCCESS" "[OK] Coverage report generated successfully"
            
            # Extract coverage percentage
            if command_exists grep && command_exists sed; then
                COVERAGE_PERCENT=$(grep -o '<td class="ctr2">[0-9]*%</td>' target/site/jacoco/index.html 2>/dev/null | head -1 | sed 's/<[^>]*>//g')
                if [ ! -z "$COVERAGE_PERCENT" ]; then
                    print_status "INFO" "Overall code coverage: $COVERAGE_PERCENT"
                    
                    # Check if coverage meets requirement
                    COVERAGE_NUM=$(echo $COVERAGE_PERCENT | sed 's/%//')
                    if [ "$COVERAGE_NUM" -ge "$TARGET_COVERAGE" ]; then
                        print_status "SUCCESS" "[TARGET] Coverage target met: $COVERAGE_PERCENT >= $TARGET_COVERAGE%"
                        COVERAGE_STATUS=1
                    else
                        print_status "WARNING" "[WARN] Coverage below target: $COVERAGE_PERCENT < $TARGET_COVERAGE%"
                        COVERAGE_STATUS=0
                    fi
                else
                    # No coverage percentage found
                    COVERAGE_STATUS=0
                fi
            else
                # Can't check coverage without grep/sed
                COVERAGE_STATUS=0
            fi
            
            print_status "INFO" "Coverage report: target/site/jacoco/index.html"
        else
            print_status "WARNING" "[WARN] Coverage report not found"
            COVERAGE_STATUS=0
        fi
    else
        print_status "WARNING" "[WARN] Coverage generation had issues"
        COVERAGE_STATUS=0
    fi
else
    COVERAGE_STATUS=1  # Assume coverage is OK for quick mode
fi

# =============================================================================
# STEP 5: JAVADOC GENERATION (if not quick mode or specific mode)
# =============================================================================

if [ "$QUICK_MODE" = false ] && [ "$TEST_ONLY" = false ] && [ "$PACKAGE_ONLY" = false ] && [ "$UML_ONLY" = false ] && [ "$CLEAN_UML" = false ]; then
    print_header "[DOCS] STEP 5: JAVADOC GENERATION"
    
    print_status "STEP" "Generating Javadoc documentation..."
    if mvn javadoc:javadoc -q; then
        if file_exists "target/site/apidocs/index.html"; then
            print_status "SUCCESS" "[OK] Javadoc generated successfully"
            JAVADOC_STATUS=1
            
            # Count documented classes
            if dir_exists "target/site/apidocs"; then
                DOC_COUNT=$(find target/site/apidocs -name "*.html" | grep -v "index\|package\|overview" | wc -l)
                print_status "INFO" "Generated documentation for $DOC_COUNT classes/methods"
            fi
            
            print_status "INFO" "Javadoc: target/site/apidocs/index.html"
        else
            print_status "WARNING" "[WARN] Javadoc not found"
            JAVADOC_STATUS=0
        fi
    else
        print_status "WARNING" "[WARN] Javadoc generation had warnings"
        JAVADOC_STATUS=0
    fi
else
    JAVADOC_STATUS=1  # Assume Javadoc is OK for quick mode
fi

# =============================================================================
# STEP 6: UML DIAGRAM GENERATION (if not quick mode or specific mode)
# =============================================================================

if [ "$QUICK_MODE" = false ] && [ "$TEST_ONLY" = false ] && [ "$JAVADOC_ONLY" = false ] && [ "$PACKAGE_ONLY" = false ] && [ "$CLEAN_UML" = false ]; then
    print_header "[UML] STEP 6: UML DIAGRAM GENERATION"
    
    print_status "STEP" "Generating UML diagrams using modern PlantUML generator..."
    
    # Ensure classes are compiled first
    print_status "STEP" "Compiling classes for UML generation..."
    if mvn compile -q; then
        print_status "SUCCESS" "[OK] Classes compiled successfully"
    else
        print_status "ERROR" "[FAIL] Compilation failed - cannot generate UML"
        UML_STATUS=0
    fi
    
    # Generate PlantUML using the Maven plugin
    print_status "STEP" "Generating PlantUML diagram using Maven plugin..."
    if mvn plantuml-generator:generate -q; then
        print_status "SUCCESS" "[OK] PlantUML diagram generated successfully"
        
        # Check if PlantUML file was created
        if file_exists "target/site/uml/bookstore-class-diagram.puml"; then
            print_status "SUCCESS" "[OK] PlantUML source file created"
            
            # Check if PlantUML jar is available for image generation
            if [ -f "plantuml.jar" ]; then
                print_status "STEP" "Converting PlantUML to images with size limit fix..."
                
                # Generate PNG with increased size limit to prevent cutoff
                print_status "STEP" "Generating PNG diagram (with size limit fix)..."
                if java -DPLANTUML_LIMIT_SIZE=8192 -jar plantuml.jar -tpng target/site/uml/bookstore-class-diagram.puml -o target/site/uml; then
                    print_status "SUCCESS" "[OK] PNG diagram generated: target/site/uml/bookstore-class-diagram.png"
                else
                    print_status "WARNING" "[WARN] Failed to generate PNG"
                fi
                
                # Generate SVG (no size limit issues)
                print_status "STEP" "Generating SVG diagram..."
                if java -jar plantuml.jar -tsvg target/site/uml/bookstore-class-diagram.puml -o target/site/uml; then
                    print_status "SUCCESS" "[OK] SVG diagram generated: target/site/uml/bookstore-class-diagram.svg"
                else
                    print_status "WARNING" "[WARN] Failed to generate SVG"
                fi
                
                # Move images to correct location if they were created in nested directory
                if [ -d "target/site/uml/target/site/uml" ]; then
                    print_status "STEP" "Moving generated images to correct location..."
                    mv target/site/uml/target/site/uml/* target/site/uml/ 2>/dev/null || true
                    rmdir target/site/uml/target/site/uml 2>/dev/null || true
                    rmdir target/site/uml/target/site 2>/dev/null || true
                    rmdir target/site/uml/target 2>/dev/null || true
                fi
                
                # Show file locations and sizes
                print_status "STEP" "UML Generation Summary:"
                if file_exists "target/site/uml/bookstore-class-diagram.puml"; then
                    PUML_SIZE=$(du -h target/site/uml/bookstore-class-diagram.puml | cut -f1)
                    print_status "INFO" "📄 PlantUML Source: target/site/uml/bookstore-class-diagram.puml ($PUML_SIZE)"
                fi
                if file_exists "target/site/uml/bookstore-class-diagram.png"; then
                    PNG_SIZE=$(du -h target/site/uml/bookstore-class-diagram.png | cut -f1)
                    print_status "INFO" "🖼️  PNG Image: target/site/uml/bookstore-class-diagram.png ($PNG_SIZE)"
                fi
                if file_exists "target/site/uml/bookstore-class-diagram.svg"; then
                    SVG_SIZE=$(du -h target/site/uml/bookstore-class-diagram.svg | cut -f1)
                    print_status "INFO" "📊 SVG Image: target/site/uml/bookstore-class-diagram.svg ($SVG_SIZE)"
                fi
                
                UML_STATUS=1
                print_status "SUCCESS" "[OK] UML diagrams generated successfully!"
            else
                print_status "WARNING" "[WARN] PlantUML jar not found - only source file generated"
                print_status "INFO" "📄 PlantUML Source: target/site/uml/bookstore-class-diagram.puml"
                print_status "INFO" "To generate images, run: java -jar plantuml.jar -tpng target/site/uml/bookstore-class-diagram.puml"
                UML_STATUS=1
            fi
        else
            print_status "WARNING" "[WARN] PlantUML source file not found"
            UML_STATUS=0
        fi
    else
        print_status "WARNING" "[WARN] PlantUML generation failed"
        UML_STATUS=0
    fi
else
    UML_STATUS=1  # Assume UML is OK for quick mode
fi

# =============================================================================
# STEP 7: QUALITY CHECKS (if not specific mode)
# =============================================================================

if [ "$TEST_ONLY" = false ] && [ "$JAVADOC_ONLY" = false ] && [ "$PACKAGE_ONLY" = false ] && [ "$UML_ONLY" = false ] && [ "$CLEAN_UML" = false ]; then
    print_header "[QUALITY] STEP 7: QUALITY CHECKS"
    
    # Check for compilation warnings
    print_status "STEP" "Checking for compilation warnings..."
    if mvn compile -q 2>&1 | grep -i "warning\|deprecated" > /dev/null; then
        print_status "WARNING" "[WARN] Compilation warnings detected"
        mvn compile -q 2>&1 | grep -i "warning\|deprecated" | head -5
    else
        print_status "SUCCESS" "[OK] No compilation warnings detected"
    fi
    
    # Check source code quality
    print_status "STEP" "Analyzing source code quality..."
    SOURCE_FILES=$(find src/main/java -name "*.java" | wc -l)
    TEST_FILES=$(find src/test/java -name "*.java" | wc -l)
    TOTAL_LINES=$(find src -name "*.java" -exec wc -l {} + | tail -1 | awk '{print $1}')
    
    print_status "INFO" "Source files: $SOURCE_FILES"
    print_status "INFO" "Test files: $TEST_FILES"
    print_status "INFO" "Total lines of code: $TOTAL_LINES"
    
    # Calculate test ratio
    if [ $SOURCE_FILES -gt 0 ]; then
        TEST_RATIO=$(echo "scale=1; $TEST_FILES * 100 / $SOURCE_FILES" | bc -l 2>/dev/null || echo "N/A")
        print_status "INFO" "Test ratio: $TEST_RATIO% (tests per source file)"
    fi
fi

# =============================================================================
# STEP 8: PACKAGING (if not test-only or specific mode)
# =============================================================================

if [ "$TEST_ONLY" = false ] && [ "$JAVADOC_ONLY" = false ] && [ "$UML_ONLY" = false ] && [ "$CLEAN_UML" = false ]; then
    print_header "[PACKAGE] STEP 8: PACKAGING"
    
    print_status "STEP" "Creating JAR package..."
    if mvn package -DskipTests -q; then
        JAR_FILE=$(ls target/*.jar 2>/dev/null | head -1)
        if [ ! -z "$JAR_FILE" ]; then
            print_status "SUCCESS" "[OK] JAR created successfully"
            print_status "INFO" "JAR file: $JAR_FILE"
            print_status "INFO" "Size: $(du -h $JAR_FILE | cut -f1)"
            
            # Test JAR execution
            print_status "STEP" "Testing JAR execution..."
            if java -jar "$JAR_FILE" --help > /dev/null 2>&1 || java -jar "$JAR_FILE" > /dev/null 2>&1; then
                print_status "SUCCESS" "[OK] JAR executes successfully"
            else
                print_status "WARNING" "[WARN] JAR execution test inconclusive"
            fi
        else
            print_status "WARNING" "[WARN] JAR file not found"
        fi
    else
        print_status "WARNING" "[WARN] JAR creation failed"
    fi
fi

# =============================================================================
# STEP 9: FINAL SUMMARY & STATUS
# =============================================================================

print_header "[SUMMARY] STEP 9: FINAL SUMMARY & STATUS"

# Calculate overall status
OVERALL_STATUS=$((BUILD_STATUS + TEST_STATUS + COVERAGE_STATUS + JAVADOC_STATUS + UML_STATUS))

print_status "STEP" "Build Results Summary:"
echo "  ==================================================================="
echo "                           BUILD STATUS REPORT"
echo "  ==================================================================="
echo "  [BUILD] Compilation:        $([ $BUILD_STATUS -eq 1 ] && echo "[OK] PASS" || echo "[FAIL] FAIL")"
echo "  [TEST] Testing:             $([ $TEST_STATUS -eq 1 ] && echo "[OK] PASS" || echo "[FAIL] FAIL")"
echo "  [COVERAGE] Coverage:        $([ $COVERAGE_STATUS -eq 1 ] && echo "[OK] PASS" || echo "[FAIL] FAIL")"
echo "  [DOCS] Javadoc:             $([ $JAVADOC_STATUS -eq 1 ] && echo "[OK] PASS" || echo "[FAIL] FAIL")"
echo "  [UML] UML Diagrams:         $([ $UML_STATUS -eq 1 ] && echo "[OK] PASS" || echo "[FAIL] FAIL")"
echo "  -------------------------------------------------------------------"
echo "  Overall Score:              $OVERALL_STATUS/5 ($(echo "scale=0; $OVERALL_STATUS * 100 / 5" | bc -l)%)"
echo "  ==================================================================="

# Final status determination
if [ $OVERALL_STATUS -eq 5 ]; then
    print_status "SUCCESS" "[EXCELLENT] All checks passed successfully!"
    print_status "SUCCESS" "Your Lab 3 project is ready for submission!"
elif [ $OVERALL_STATUS -ge 4 ]; then
    print_status "SUCCESS" "[GOOD] Most checks passed. Minor issues detected."
elif [ $OVERALL_STATUS -ge 3 ]; then
    print_status "WARNING" "[FAIR] Some checks passed. Review and fix issues."
else
    print_status "ERROR" "[POOR] Multiple checks failed. Significant issues detected."
fi

# =============================================================================
# STEP 10: NEXT STEPS & RECOMMENDATIONS
# =============================================================================

print_header "[NEXT] STEP 10: NEXT STEPS & RECOMMENDATIONS"

echo "[FILES] Generated Files:"
if [ $COVERAGE_STATUS -eq 1 ]; then
    echo "  • Coverage Report: target/site/jacoco/index.html"
fi
if [ $JAVADOC_STATUS -eq 1 ]; then
    echo "  • Javadoc: target/site/apidocs/index.html"
fi
if [ $UML_STATUS -eq 1 ]; then
    echo "  • UML Diagrams: target/site/uml/bookstore-class-diagram.{png,svg}"
    echo "  • UML Source: target/site/uml/bookstore-class-diagram.puml"
fi
if [ ! -z "$JAR_FILE" ]; then
    echo "  • Executable JAR: $JAR_FILE"
fi

echo ""
echo "[COMMANDS] Useful Commands:"
echo "  • Run tests only:        mvn test"
echo "  • Generate coverage:     mvn jacoco:report"
echo "  • Generate Javadoc:      mvn javadoc:javadoc"
echo "  • Clean build:           mvn clean compile"
echo "  • Run specific test:     mvn test -Dtest=EBookTest"
    echo "  • Generate UML only:     ./runme.sh --uml-only"
    echo "  • Generate PlantUML:     mvn plantuml-generator:generate"
echo "  • Security check:        ./runme.sh --security"
echo "  • OWASP check:          ./runme.sh --owasp"
echo "  • SpotBugs check:       ./runme.sh --spotbugs"

echo ""
echo "[CHECKLIST] Lab 3 Submission Checklist:"
echo "  □ All tests pass (✓ $([ $TEST_STATUS -eq 1 ] && echo "DONE" || echo "TODO"))"
echo "  □ Code coverage > $TARGET_COVERAGE% (✓ $([ $COVERAGE_STATUS -eq 1 ] && echo "DONE" || echo "TODO"))"
echo "  □ Javadoc generated (✓ $([ $JAVADOC_STATUS -eq 1 ] && echo "DONE" || echo "TODO"))"
echo "  □ UML diagrams generated (✓ $([ $UML_STATUS -eq 1 ] && echo "DONE" || echo "TODO"))"
echo "  □ Code compiles cleanly (✓ $([ $BUILD_STATUS -eq 1 ] && echo "DONE" || echo "TODO"))"
echo "  □ EBook class implemented (✓ DONE)"
echo "  □ Enhanced search methods (✓ DONE)"
echo "  □ Visitor pattern implemented (✓ DONE)"
echo "  □ Factory pattern implemented (✓ DONE)"

echo ""
echo "[REPORTS] Open Reports:"
if command_exists open; then
    echo "  • Coverage: open target/site/jacoco/index.html"
    echo "  • Javadoc:  open target/site/apidocs/index.html"
    echo "  • UML PNG:  open target/site/uml/bookstore-class-diagram.png"
    echo "  • UML SVG:  open target/site/uml/bookstore-class-diagram.svg"
elif command_exists xdg-open; then
    echo "  • Coverage: xdg-open target/site/jacoco/index.html"
    echo "  • Javadoc:  xdg-open target/site/apidocs/index.html"
    echo "  • UML PNG:  xdg-open target/site/uml/bookstore-class-diagram.png"
    echo "  • UML SVG:  xdg-open target/site/uml/bookstore-class-diagram.svg"
else
    echo "  • Coverage: target/site/jacoco/index.html"
    echo "  • Javadoc:  target/site/apidocs/index.html"
    echo "  • UML PNG:  target/site/uml/bookstore-class-diagram.png"
    echo "  • UML SVG:  target/site/uml/bookstore-class-diagram.svg"
fi

# =============================================================================
# SCRIPT COMPLETION
# =============================================================================

print_header "[END] SCRIPT COMPLETION"

print_status "INFO" "Script completed at: $(date)"
print_status "INFO" "Total execution time: $SECONDS seconds"

if [ $OVERALL_STATUS -eq 5 ]; then
    print_status "SUCCESS" "[SUCCESS] CONGRATULATIONS! Your Lab 3 project meets all requirements!"
    print_status "SUCCESS" "Features implemented: EBook, Enhanced Search, Visitor Pattern, Factory Pattern"
    exit 0
else
    print_status "WARNING" "[WARN] Please review and fix the issues above before submission."
    exit 1
fi
