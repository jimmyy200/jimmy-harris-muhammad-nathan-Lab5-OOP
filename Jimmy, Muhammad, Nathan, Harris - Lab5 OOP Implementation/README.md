# Bookstore Management System - Lab 4

[![Java](https://img.shields.io/badge/Java-24+-orange.svg)](https://openjdk.java.net/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![JUnit](https://img.shields.io/badge/JUnit-5-green.svg)](https://junit.org/junit5/)
[![JMH](https://img.shields.io/badge/JMH-Benchmarks-purple.svg)](https://openjdk.java.net/projects/code-tools/jmh/)
[![Coverage](https://img.shields.io/badge/Coverage-54%25-green.svg)](target/site/jacoco/index.html)
[![License](https://img.shields.io/badge/License-Educational-lightgrey.svg)](LICENSE)

## Introduction

We have built a bookstore management system that serves as a learning platform in advanced Java programming and software architecture. This system demonstrates how to create a real-world application that can handle the requirements of a modern bookstore while maintaining clean, maintainable, and extensible code. The project showcases the practical application of fundamental computer science concepts, design patterns, and modern software engineering principles.

The core challenge we solved was creating a system that could manage different types of materials in a bookstore - from traditional legacy printed books to modern digital content like e-books, audio books, and video materials. Each type of material has unique properties and behaviors, yet they all need to be managed uniformly within the same system. We achieved this through the power of object-oriented programming, specifically using inheritance, polymorphism, and interfaces to create a flexible and extensible architecture.

Our system implements eight major design patterns from the Gang of Four (GoF) patterns, each serving a specific purpose in solving real-world problems. The Builder pattern allows us to create complex objects step by step, making it easy to construct different types of materials with various configurations. The Factory pattern provides a centralized way to create materials without exposing their constructors, making the system more maintainable and flexible. The Observer pattern enables event-driven programming, allowing different parts of the system to react to changes in the inventory.

The Composite pattern is particularly powerful in our system, allowing us to create bundles of materials that can contain other bundles, creating a tree-like structure that mirrors real-world product bundling scenarios. The Decorator pattern lets us add features to materials dynamically, such as gift wrapping or expedited delivery, without modifying the original material classes. The Chain of Responsibility pattern manages our discount approval system, where different levels of management can approve different discount amounts.

We implemented the Iterator pattern to provide multiple ways of traversing our material collections, whether by type, price range, or alphabetical order. The Visitor pattern allows us to add new operations to our material hierarchy without modifying existing classes, such as calculating shipping costs that vary by material type. These patterns work together to create a system that is both powerful and maintainable.

The system's architecture follows modern software engineering principles, including the Single Responsibility Principle, where each class has one clear purpose. We use the Open/Closed Principle, making the system open for extension but closed for modification. The Liskov Substitution Principle ensures that derived classes can be used wherever their base classes are expected. The Interface Segregation Principle keeps our interfaces focused and cohesive, while the Dependency Inversion Principle ensures that high-level modules don't depend on low-level modules.

Our data model is built around an inheritance hierarchy that starts with an abstract Material class. This class defines common properties like ID, title, price, and year, while requiring subclasses to implement specific behaviors like getting the creator and display information. We have concrete implementations for PrintedBook, EBook, AudioBook, VideoMaterial, and Magazine, each with their own unique properties and behaviors. The EBook, AudioBook, and VideoMaterial classes also implement a Media interface, demonstrating multiple inheritance through interfaces.

The system includes search capabilities powered by a trie (prefix tree) data structure, which allows for a fast prefix-based searching. This is particularly useful for autocomplete functionality and finding materials by partial title matches. We also implemented various filtering and sorting mechanisms that can work with any combination of material properties, making it easy for users to find exactly what they're looking for.

Our inventory management system provides some statistics and analytics as well, including total inventory value, average prices, median prices, and distribution of material types. The system can calculate both regular and discounted prices, with different discount strategies for different types of materials. For example, older books might get automatic discounts, while e-books without DRM protection might get special pricing.

The event-driven architecture allows different parts of the system to react to changes in the inventory. When a new material is added or a price changes, observers can be automatically notified and take appropriate actions, such as updating analytics, sending notifications, or logging the changes. This makes the system highly responsive and allows for easy integration of new features.

We also implemented repository pattern that abstracts data persistence, making it easy to switch between different storage mechanisms. Currently, we use JSON-based storage, but the system could easily be adapted to use databases or other storage systems without changing the business logic. This demonstrates the power of the Hexagonal Architecture pattern.

The system includes error handling and validation at multiple levels. Input validation ensures that only valid data enters the system, while business rule validation ensures that operations make sense from a business perspective. We use custom exceptions to provide clear error messages and proper error handling throughout the application.

Our implementation includes modern Java features like records for immutable data transfer objects, CompletableFuture for asynchronous operations, and streams for functional programming. These features make the code more concise, readable, and performant. We also use generics extensively to ensure type safety and eliminate casting. 

The CompletableFuture implementation in our system enables truly asynchronous operations that don't block the main thread while performing time-consuming tasks like database queries or file I/O operations. When a user requests to add a material to the inventory, the system can immediately return a CompletableFuture object while the actual storage operation happens in the background. This means the user interface remains responsive, and multiple operations can be processed concurrently. For example, when performing batch operations like adding hundreds of materials at once, each material addition can be processed asynchronously, dramatically improving performance. The system also uses CompletableFuture for complex operations like advanced searches that might involve multiple data sources, allowing these searches to complete in parallel rather than sequentially.

The streams API transforms how we process collections of materials by providing a functional programming approach that is both more readable and more efficient. Instead of writing traditional for-loops to filter materials by price range or sort them by title, we use stream operations like filter(), map(), and sorted() that read like natural language. For instance, to find all e-books under $20 published in the last two years, we can write a single, readable stream expression that processes the entire collection efficiently. Streams also enable parallel processing automatically - when we need to calculate statistics for thousands of materials, the system can automatically split the work across multiple CPU cores, making operations like calculating average prices or grouping materials by type significantly faster. This functional approach makes the code more declarative, meaning we describe what we want rather than how to get it, leading to fewer bugs and easier maintenance.

The system is also designed to be thread-safe, with proper synchronization mechanisms to handle concurrent access to shared resources. This is crucial for a real-world application that might be accessed by multiple users simultaneously. We use defensive programming techniques to ensure the system remains stable even under unexpected conditions.

We created a flexible pricing system that can handle various discount scenarios, from simple percentage discounts to complex bundle pricing. The system can calculate shipping costs based on material type and properties, and it supports different delivery options through the decorator pattern. This flexibility makes the system suitable for various business models and pricing strategies.

The codebase includes markdown and annotation-based Java in-code documentation, and follows Java naming conventions and best practices. Each class and method is documented with JavaDoc comments that try to explain the purpose, parameters, return values, and any exceptions that might be thrown. This makes the code self-documenting and easy to understand for new developers.

Our testing strategy includes unit tests for individual components, integration tests for the interaction between components, and performance tests to ensure the system can handle realistic workloads. We use modern testing frameworks and techniques like jacoco to ensure coverage and reliable test execution and reporting.

The system demonstrates how to build a complex application using clean architecture principles, where the business logic is separated from infrastructure concerns. The domain layer contains the core business rules and entities, while the infrastructure layer handles data persistence and external integrations. This separation makes the system easier to test, maintain, and extend.

We implemented a logging system that provides visibility into the system's operation, making it easier to debug issues and monitor performance. The logging is configurable and can be adjusted for different environments, from development to production.

The project showcases how modern software development practices can be applied to create a scalable and maintainable system. By combining solid object-oriented design principles with proven design patterns and modern Java features, we've created a system that not only meets current requirements but can also evolve to meet future needs.

## System Architecture

The system follows a layered architecture with clear separation of concerns:

- **Model Layer**: Core domain entities and business logic
- **API Layer**: Public interfaces and contracts
- **Service Layer**: Business logic orchestration
- **Repository Layer**: Data persistence abstraction
- **Implementation Layer**: Concrete implementations
- **Design Pattern Implementations**: Various GoF patterns

## Package Structure and Relationships

### 1. Model Package (`com.university.bookstore.model`)

**Core Domain Entities:**

#### `Material.java` - Abstract Base Class
- **Purpose**: Abstract base class for all materials in the bookstore
- **Key Features**: 
  - Polymorphic behavior with abstract methods `getCreator()` and `getDisplayInfo()`
  - Template method pattern for discount calculation
  - Validation framework for common properties
  - JSON serialization support with type information
- **Relationships**: Extended by all concrete material types
- **Design Patterns**: Template Method, Strategy (discount rates)

#### `Book.java` - Legacy Book Class
- **Purpose**: Immutable book representation (legacy compatibility)
- **Key Features**: ISBN validation, immutable design, thread-safe
- **Relationships**: Standalone class, not part of new polymorphic hierarchy

#### `EBook.java` - Electronic Book
- **Purpose**: Digital book with multimedia capabilities
- **Key Features**: 
  - Implements both `Material` and `Media` interfaces (multiple inheritance)
  - File format validation (PDF, EPUB, MOBI)
  - DRM support and reading time calculation
  - Quality settings and file size management
- **Relationships**: Extends `Material`, implements `Media`
- **Design Patterns**: Multiple inheritance through interfaces

#### `PrintedBook.java` - Physical Book
- **Purpose**: Physical book representation
- **Key Features**: ISBN validation, page count, publisher info, hardcover/paperback
- **Relationships**: Extends `Material`
- **Design Patterns**: Inheritance, validation

#### `AudioBook.java` - Audio Content
- **Purpose**: Audio book with narration details
- **Key Features**: 
  - Narrator information, duration, audio format
  - Unabridged/abridged versions
  - Playback speed calculations
- **Relationships**: Extends `Material`, implements `Media`
- **Design Patterns**: Multiple inheritance through interfaces

#### `VideoMaterial.java` - Video Content
- **Purpose**: Video materials (movies, documentaries, educational content)
- **Key Features**: 
  - Director, cast, rating information
  - Video quality and format support
  - Streaming bandwidth calculations
- **Relationships**: Extends `Material`, implements `Media`
- **Design Patterns**: Multiple inheritance through interfaces

#### `Magazine.java` - Periodical Publication
- **Purpose**: Magazine and periodical management
- **Key Features**: ISSN validation, issue numbers, publication frequency
- **Relationships**: Extends `Material`
- **Design Patterns**: Inheritance, enum for publication frequency

#### `Media.java` - Interface for Multimedia Content
- **Purpose**: Interface for materials with multimedia capabilities
- **Key Features**: Duration, format, quality, streaming capabilities
- **Relationships**: Implemented by `EBook`, `AudioBook`, `VideoMaterial`
- **Design Patterns**: Interface Segregation Principle

### 2. API Package (`com.university.bookstore.api`)

#### `MaterialStore.java` - Core Store Interface
- **Purpose**: Defines operations for polymorphic material management
- **Key Features**: 
  - CRUD operations for materials
  - Search and filtering capabilities
  - Inventory statistics and analytics
  - Polymorphic material handling
- **Relationships**: Implemented by `MaterialStoreImpl` and `ModernConcurrentMaterialStore`
- **Design Patterns**: Interface Segregation, Dependency Inversion

#### `ModernMaterialStore.java` - Enhanced Store Interface
- **Purpose**: Modern interface with async operations and advanced features
- **Key Features**: 
  - Java records for statistics
  - Async operations with CompletableFuture
  - Advanced search criteria with builder pattern
  - Batch operations
- **Relationships**: Extends `MaterialStore`
- **Design Patterns**: Builder Pattern, Modern Java features

#### `BookstoreAPI.java` - Legacy Book API
- **Purpose**: Legacy interface for traditional book-only operations
- **Key Features**: 
  - ISBN-based book management
  - Traditional search operations
  - Inventory value calculations
  - Defensive copying for encapsulation
- **Relationships**: Implemented by `BookstoreArrayList`
- **Design Patterns**: Legacy API pattern, Encapsulation

### 3. Service Package (`com.university.bookstore.service`)

#### `MaterialService.java` - Business Logic Orchestration
- **Purpose**: Domain service coordinating business logic
- **Key Features**: 
  - Business rule validation
  - Exception handling with custom exceptions
  - Repository coordination
- **Relationships**: Uses `MaterialRepository`
- **Design Patterns**: Service Layer, Hexagonal Architecture

### 4. Repository Package (`com.university.bookstore.repository`)

#### `MaterialRepository.java` - Persistence Port
- **Purpose**: Port interface for material persistence
- **Key Features**: CRUD operations, existence checks, counting
- **Relationships**: Implemented by JSON-based repositories
- **Design Patterns**: Ports and Adapters (Hexagonal Architecture)

#### `MaterialsWrapper.java` - JSON Serialization Wrapper**
- **Purpose**: Ensures proper JSON serialization of polymorphic Material types
- **Key Features**: 
  - Jackson annotations for type information
  - Proper polymorphic serialization
  - Defensive copying for immutability
- **Relationships**: Used by JSON repositories for serialization
- **Design Patterns**: Data Transfer Object, Serialization Pattern

### 5. Implementation Package (`com.university.bookstore.impl`)

#### `MaterialStoreImpl.java` - Core Store Implementation
- **Purpose**: ArrayList-based implementation of material store
- **Key Features**: 
  - Thread-safe operations
  - Efficient searching and filtering
  - Inventory statistics calculation
  - Polymorphic material handling
- **Relationships**: Implements `MaterialStore`
- **Design Patterns**: Polymorphism, Defensive Programming

#### `ConcurrentMaterialStore.java` - Thread-Safe Implementation
- **Purpose**: Thread-safe implementation using ReentrantReadWriteLock
- **Key Features**: 
  - ReadWriteLock for optimized read-heavy workloads
  - ConcurrentHashMap for thread-safe storage
  - Proper lock management and resource cleanup
  - High-performance concurrent operations
- **Relationships**: Implements `MaterialStore`
- **Design Patterns**: Concurrency patterns, Thread Safety

#### `ModernConcurrentMaterialStore.java` - Advanced Concurrent Implementation
- **Purpose**: Modern thread-safe implementation with advanced features
- **Key Features**: 
  - StampedLock for optimistic reads and better performance
  - ExecutorService for async operations
  - CompletableFuture for non-blocking operations
  - AutoCloseable for proper resource management
  - Virtual thread support and parallel processing
- **Relationships**: Implements `MaterialStore`, `AutoCloseable`
- **Design Patterns**: Modern concurrency patterns, Resource management

#### `BookstoreArrayList.java` - Legacy Book Implementation
- **Purpose**: ArrayList-based implementation for legacy Book API
- **Key Features**: 
  - ISBN uniqueness enforcement
  - Defensive copying for encapsulation
  - Performance characteristics documentation
  - Statistics and sorting capabilities
- **Relationships**: Implements `BookstoreAPI`
- **Design Patterns**: Legacy implementation, Encapsulation

### 6. Design Pattern Implementations

#### Builder Pattern (`com.university.bookstore.builder`)

**`ComponentBuilder.java` - Component Builder Interface**
- **Purpose**: Generic builder interface for MaterialComponent instances
- **Key Features**: Fluent interface, validation, reset capability
- **Relationships**: Implemented by component builders
- **Design Patterns**: Builder Pattern, Interface Segregation

**`MaterialBuilder.java` - Material Builder Interface**
- **Purpose**: Generic builder interface for Material instances
- **Key Features**: Fluent interface, validation, reset capability
- **Relationships**: Implemented by material builders
- **Design Patterns**: Builder Pattern, Interface Segregation

**`MaterialDirector.java` - Director Class**
- **Purpose**: Orchestrates complex object construction
- **Key Features**: Predefined configurations for different material types
- **Relationships**: Uses various builders (`EBookBuilder`, `MaterialBundleBuilder`)
- **Design Patterns**: Director Pattern, Builder Pattern

**`EBookBuilder.java` - EBook Builder**
- **Purpose**: Step-by-step EBook construction
- **Key Features**: Fluent interface, validation, reset capability
- **Relationships**: Used by `MaterialDirector`
- **Design Patterns**: Builder Pattern

**`MaterialBundleBuilder.java` - Bundle Builder**
- **Purpose**: Complex bundle construction with materials
- **Key Features**: Discount management, material addition, nested bundles
- **Relationships**: Used by `MaterialDirector`
- **Design Patterns**: Builder Pattern

#### Factory Pattern (`com.university.bookstore.factory`)

**`MaterialFactory.java` - Material Factory**
- **Purpose**: Centralized material creation
- **Key Features**: Type-based creation, property validation, error handling
- **Relationships**: Creates all material types
- **Design Patterns**: Factory Pattern, Type Safety

**`AdvancedMaterialFactory.java` - Enhanced Factory**
- **Purpose**: Enhanced factory with advanced validation and type-safe property extraction
- **Key Features**: 
  - Enhanced validation with better error messages
  - Type-safe property extraction methods
  - Support for complex material configurations
  - property validation
- **Relationships**: Enhanced version of `MaterialFactory`
- **Design Patterns**: Enhanced Factory Pattern, Validation patterns

#### Observer Pattern (`com.university.bookstore.observer`)

**`MaterialEventPublisher.java` - Event Publisher**
- **Purpose**: Manages observers and publishes material events
- **Key Features**: Thread-safe observer management, event broadcasting
- **Relationships**: Implements `MaterialSubject`, notifies `MaterialObserver` instances
- **Design Patterns**: Observer Pattern, Thread Safety

**`MaterialObserver.java` - Observer Interface**
- **Purpose**: Defines contract for material event observers
- **Key Features**: Event handling, observer lifecycle management
- **Relationships**: Implemented by various observer types
- **Design Patterns**: Observer Pattern

#### Composite Pattern (`com.university.bookstore.composite`)

**`MaterialBundle.java` - Composite Component**
- **Purpose**: Represents bundles of materials
- **Key Features**: 
  - Recursive composition
  - Unified treatment of individual and composite objects
  - Discount calculations
  - Circular reference prevention
- **Relationships**: Implements `MaterialComponent`, contains other components
- **Design Patterns**: Composite Pattern

**`MaterialComponent.java` - Component Interface**
- **Purpose**: Common interface for leaf and composite objects
- **Key Features**: Price calculation, description, material listing
- **Relationships**: Implemented by `MaterialBundle` and `MaterialLeaf`
- **Design Patterns**: Composite Pattern

**`MaterialLeaf.java` - Leaf Component**
- **Purpose**: Wraps individual materials for composite pattern
- **Key Features**: 
  - Wraps Material objects
  - Implements MaterialComponent interface
  - Provides access to underlying material properties
  - Single item count and leaf identification
- **Relationships**: Implements `MaterialComponent`, wraps `Material` objects
- **Design Patterns**: Composite Pattern, Adapter Pattern

**`BundleService.java` - Bundle Management Service**
- **Purpose**: High-level service for managing material bundles
- **Key Features**: 
  - Bundle creation and management
  - Nested bundle support
  - Bundle statistics and analytics
  - Filtering and searching capabilities
  - bundle operations
- **Relationships**: Manages `MaterialBundle` instances
- **Design Patterns**: Service Layer, Composite Pattern

#### Decorator Pattern (`com.university.bookstore.decorator`)

**`MaterialDecorator.java` - Abstract Decorator**
- **Purpose**: Base class for material decorations
- **Key Features**: 
  - Wraps materials with additional functionality
  - Delegation to wrapped material
  - Decorator chain management
- **Relationships**: Extends `Material`, wraps other materials
- **Design Patterns**: Decorator Pattern

**Concrete Decorators:**
- `GiftWrappingDecorator.java` - Adds gift wrapping
- `ExpeditedDeliveryDecorator.java` - Adds fast delivery
- `DigitalAnnotationDecorator.java` - Adds digital annotations

**`MaterialEnhancementService.java` - Enhancement Management Service**
- **Purpose**: High-level service for managing material enhancements
- **Key Features**: 
  - Dynamic decorator application
  - Package creation (premium, gift, digital)
  - Enhancement cost calculation
  - Enhancement analysis and summary
  - Decorator chain management
- **Relationships**: Uses concrete decorators
- **Design Patterns**: Service Layer, Decorator Pattern

#### Chain of Responsibility Pattern (`com.university.bookstore.chain`)

**`DiscountHandler.java` - Abstract Handler**
- **Purpose**: Base class for discount approval chain
- **Key Features**: Request handling, chain management, approval limits
- **Relationships**: Forms chain with concrete handlers
- **Design Patterns**: Chain of Responsibility

**Concrete Handlers:**
- `ManagerHandler.java` - Manager-level approvals
- `DirectorHandler.java` - Director-level approvals
- `VPHandler.java` - VP-level approvals

**`DiscountApprovalService.java` - Approval Management Service**
- **Purpose**: High-level service for managing discount approval workflow
- **Key Features**: 
  - Request processing through approval chain
  - Request tracking and statistics
  - Customer and material-specific request filtering
  - Approval analytics and reporting
  - Chain management and configuration
- **Relationships**: Manages approval chain and processes requests
- **Design Patterns**: Service Layer, Chain of Responsibility

#### Iterator Pattern (`com.university.bookstore.iterator`)

**`MaterialIterator.java` - Iterator Interface**
- **Purpose**: Uniform traversal of material collections
- **Key Features**: Different iteration strategies, position tracking
- **Relationships**: Implemented by various iterator types
- **Design Patterns**: Iterator Pattern

**Concrete Iterators:**
- `MaterialTypeIterator.java` - Iterates by material type
- `PriceRangeIterator.java` - Iterates within price range
- `PriceSortedIterator.java` - Iterates in price order

**`MaterialIteratorFactory.java` - Iterator Factory**
- **Purpose**: Factory for creating different types of material iterators
- **Key Features**: 
  - Centralized iterator creation
  - Predefined iterator configurations
  - Iterator utility methods (collect, find, count)
  - Predicate-based filtering and matching
  - iterator management
- **Relationships**: Creates various iterator types
- **Design Patterns**: Factory Pattern, Iterator Pattern

#### Visitor Pattern (`com.university.bookstore.visitor`)

**`MaterialVisitor.java` - Visitor Interface**
- **Purpose**: Adds operations to material hierarchy without modification
- **Key Features**: Type-specific visit methods
- **Relationships**: Used by materials to accept visitors
- **Design Patterns**: Visitor Pattern

**`ShippingCostCalculator.java` - Concrete Visitor**
- **Purpose**: Calculates shipping costs for different material types
- **Key Features**: Type-specific shipping logic
- **Relationships**: Implements `MaterialVisitor`
- **Design Patterns**: Visitor Pattern

### 7. Search Package (`com.university.bookstore.search`)

**`MaterialTrie.java` - Trie Data Structure**
- **Purpose**: Efficient prefix-based searching
- **Key Features**: O(m) prefix search, autocomplete functionality
- **Relationships**: Used by search services
- **Design Patterns**: Data Structure Pattern

**`SearchResultCache.java` - LRU Cache Implementation**
- **Purpose**: LRU cache for search results with performance monitoring
- **Key Features**: 
  - O(1) average time complexity for get/put operations
  - LRU eviction policy
  - Cache statistics and performance monitoring
  - Thread-safe operations
- **Relationships**: Used by `CachedSearchService`
- **Design Patterns**: Cache Pattern, LRU Eviction

**`ModernSearchCache.java` - Advanced Cache Implementation**
- **Purpose**: High-performance cache with advanced features
- **Key Features**: 
  - Time-based and size-based eviction
  - Async loading with CompletableFuture
  - Statistics tracking and monitoring
  - Warm-up and refresh capabilities
  - Thread-safe operations with virtual thread support
- **Relationships**: Advanced caching solution
- **Design Patterns**: Modern Cache Pattern, Async Operations

**`CachedSearchService.java` - Cached Search Service**
- **Purpose**: Integrates Trie-based search with LRU caching
- **Key Features**: 
  - Fast prefix-based searching with intelligent caching
  - Cache invalidation on material changes
  - Performance optimization through result caching
  - Search index management
- **Relationships**: Uses `MaterialTrie` and `SearchResultCache`
- **Design Patterns**: Service Layer, Cache Pattern

### 8. Utilities Package (`com.university.bookstore.utils`)

**`LoggerFactory.java` - Logging Utility**
- **Purpose**: Centralized logging configuration
- **Key Features**: SLF4J integration, consistent logging
- **Relationships**: Used throughout the system
- **Design Patterns**: Factory Pattern

**`BookArrayUtils.java` - Array Utilities**
- **Purpose**: Utility methods for book array operations
- **Key Features**: Array manipulation, sorting, searching utilities
- **Relationships**: Used by legacy book implementations
- **Design Patterns**: Utility pattern

### 9. Demo Package (`com.university.bookstore.demo`)

**`PolymorphismDemo.java` - Demonstration Class**
- **Purpose**: demonstration of polymorphism and OOP principles
- **Key Features**: 
  - Shows polymorphic behavior across material types
  - Demonstrates interface segregation with Media interface
  - Illustrates abstraction and dynamic binding
  - Showcases SOLID principles in action
  - Media versatility and streaming vs download analysis
- **Relationships**: Uses all material types and store implementations
- **Design Patterns**: Demonstration pattern, Educational tool

### 10. Spring Boot Integration (`com.university.bookstore.controller`)

**`BookstoreApplication.java` - Spring Boot Application**
- **Purpose**: Main Spring Boot application entry point
- **Key Features**: 
  - Spring Boot auto-configuration
  - MaterialStore bean configuration
  - REST API exposure for existing business logic
- **Relationships**: Configures MaterialStore bean for dependency injection
- **Design Patterns**: Spring Boot Integration, Dependency Injection

**`MaterialController.java` - REST API Controller**
- **Purpose**: Exposes MaterialStore functionality as REST endpoints
- **Key Features**: 
  - CRUD operations via HTTP (GET, POST, PUT, DELETE)
  - Search and filtering endpoints
  - Statistics and analytics endpoints
  - JSON serialization/deserialization
  - Error handling and validation
- **Relationships**: Uses MaterialStore for business logic
- **Design Patterns**: REST API, Controller Pattern, Adapter Pattern

**`MaterialControllerTest.java` - REST API Tests**
- **Purpose**: Integration tests for REST API endpoints
- **Key Features**: 
  - MockMvc testing framework
  - HTTP request/response validation
  - JSON serialization testing
  - Error scenario testing
- **Relationships**: Tests MaterialController functionality
- **Design Patterns**: Integration Testing, Mock Testing

### 11. Resources Package (`src/main/resources`)

**`logback.xml` - Logging Configuration**
- **Purpose**: logging configuration using Logback
- **Key Features**: 
  - Console and file appenders
  - Rolling file policy with time-based rotation
  - Security audit logging with separate appender
  - Configurable log levels for different packages
  - Structured logging patterns
- **Relationships**: Used by SLF4J throughout the system
- **Design Patterns**: Configuration pattern, Separation of concerns

**`application.yml` - Spring Boot Configuration**
- **Purpose**: Spring Boot application configuration
- **Key Features**: 
  - Server port configuration
  - Logging configuration
  - Application properties
- **Relationships**: Used by Spring Boot application
- **Design Patterns**: Configuration pattern

### 12. Testing Infrastructure (`src/test/java`)

The system includes a testing infrastructure that demonstrates advanced testing strategies and quality assurance practices:

#### **Performance Testing Package (`com.university.bookstore.performance`)**

**`BookstorePerformanceBenchmark.java` - JMH Performance Benchmarks**
- **Purpose**: performance benchmarking using JMH (Java Microbenchmark Harness)
- **Key Features**: 
  - Microbenchmarks for different data structures (ArrayList vs ConcurrentHashMap)
  - Search performance comparison (Trie vs ArrayList vs Cached)
  - Cache performance analysis (hit/miss ratios)
  - Stream operations performance (sequential vs parallel)
  - Material creation performance (direct vs factory)
- **Relationships**: Benchmarks all major system components
- **Design Patterns**: Benchmarking pattern, Performance testing

**`PerformanceAnalyzer.java` - Performance Analysis Utility**
- **Purpose**: performance analysis and comparison tool
- **Key Features**: 
  - Data structure performance analysis
  - Cache performance evaluation with hit ratios
  - Search performance comparison across implementations
  - Memory usage analysis and profiling
  - Performance reporting with detailed metrics
- **Relationships**: Analyzes performance of all system components
- **Design Patterns**: Analysis pattern, Performance monitoring

**`PerformanceIntegrationTest.java` - Performance Integration Tests**
- **Purpose**: Integration tests for performance components
- **Key Features**: 
  - End-to-end performance validation
  - Concurrent access performance testing
  - Memory usage validation
  - Performance regression testing
- **Relationships**: Tests performance components integration
- **Design Patterns**: Integration testing, Performance validation

**`PropertyBasedTests.java` - Property-Based Testing**
- **Purpose**: Property-based tests using JUnit QuickCheck
- **Key Features**: 
  - Tests invariants that should hold for all valid inputs
  - Automated test case generation
  - Property validation across wide input ranges
  - Stronger guarantees than traditional unit tests
- **Relationships**: Tests system properties and invariants
- **Design Patterns**: Property-based testing, Automated testing

#### **Quality Assurance Package (`com.university.bookstore.quality`)**

**`QualityGatesTest.java` - Quality Gates**
- **Purpose**: End-to-end quality validation for all Lab 4 functionality
- **Key Features**: 
  - Complete design pattern validation
  - Integration testing across all components
  - Quality gate enforcement
  - system validation
- **Relationships**: Validates entire system quality
- **Design Patterns**: Quality assurance, Integration testing

## Key Relationships and Dependencies

### Inheritance Hierarchy
```
Material (abstract)
├── PrintedBook
├── EBook (implements Media)
├── AudioBook (implements Media)
├── VideoMaterial (implements Media)
└── Magazine
```

### Interface Implementations
- `Media` interface implemented by: `EBook`, `AudioBook`, `VideoMaterial`
- `MaterialStore` interface implemented by: `MaterialStoreImpl`, `ConcurrentMaterialStore`, `ModernConcurrentMaterialStore`
- `BookstoreAPI` interface implemented by: `BookstoreArrayList`
- `MaterialRepository` interface implemented by: `JsonMaterialRepository`, `ModernJsonMaterialRepository`

### Design Pattern Relationships
- **Builder**: `MaterialDirector` orchestrates `EBookBuilder` and `MaterialBundleBuilder`
- **Observer**: `MaterialEventPublisher` manages `MaterialObserver` instances
- **Composite**: `MaterialBundle` contains `MaterialComponent` objects
- **Decorator**: `MaterialDecorator` wraps `Material` objects
- **Chain of Responsibility**: `DiscountHandler` chain for approval workflow
- **Iterator**: Various iterators for different traversal strategies
- **Visitor**: `MaterialVisitor` adds operations to material hierarchy
- **Factory**: `MaterialFactory` creates material instances

## System Capabilities

### Core Features
1. **Polymorphic Material Management**: Handle different material types uniformly
2. **Advanced Search**: Prefix-based search with Trie data structures and intelligent caching
3. **Inventory Analytics**: Statistics, pricing analysis, type distribution
4. **Event-Driven Architecture**: Observer pattern for material changes
5. **Flexible Pricing**: Discount systems with chain of responsibility approval workflow
6. **Bundle Management**: Composite pattern for material bundles with nested support
7. **Material Enhancement**: Decorator pattern for dynamic feature addition
8. **Data Persistence**: JSON-based storage with repository pattern
9. **Concurrent Operations**: Multiple thread-safe implementations (basic, advanced, modern)
10. **Modern Java Features**: Records, CompletableFuture, Streams, StampedLock, Virtual Threads
11. **Legacy Support**: Backward compatibility with traditional book-only operations
12. **Educational Demonstrations**: polymorphism and OOP examples
13. **Logging**: Structured logging with security audit trails
14. **Advanced Caching**: LRU and modern cache implementations with performance monitoring
15. **Service Layer Architecture**: High-level services for complex operations
16. **Performance Testing**: JMH benchmarks and performance analysis
17. **Quality Assurance**: Property-based testing and quality gates validation
18. **Advanced Testing**: Integration tests, performance tests, and automated quality validation
19. **Spring Boot Integration**: REST API endpoints for web-based access
20. **REST API Testing**: MockMvc-based integration testing for HTTP endpoints

### Business Logic
1. **Material Validation**: validation for all material types
2. **Discount Management**: Chain of responsibility for approval workflows with analytics
3. **Bundle Management**: Composite pattern for material bundles with nested support
4. **Search Optimization**: Trie-based prefix searching with intelligent caching
5. **Event Publishing**: Observer pattern for system events with multiple observer types
6. **Shipping Calculations**: Visitor pattern for type-specific logic
7. **Enhancement Management**: Dynamic decorator application with package creation
8. **Iterator Management**: Multiple traversal strategies with factory-based creation
9. **Cache Management**: Advanced caching with LRU eviction and performance monitoring
10. **Service Orchestration**: High-level services coordinating complex operations

## Architecture Benefits

1. **Maintainability**: Clear separation of concerns, single responsibility principle
2. **Extensibility**: Easy to add new material types, search strategies, or business rules
3. **Testability**: Dependency injection, interface-based design
4. **Performance**: Efficient data structures, concurrent operations
5. **Flexibility**: Multiple design patterns allow for different approaches
6. **Modern Java**: Leverages latest Java features for better code quality







### Package Structure

| Package | Purpose | Key Classes | Design Patterns |
|---------|---------|-------------|-----------------|
| **`model/`** | Domain entities and business logic | `Material`, `EBook`, `PrintedBook`, `AudioBook`, `VideoMaterial`, `Magazine`, `ModernInventoryStats` | **Inheritance, Polymorphism** |
| **`api/`** | Interface contracts (Ports) | `MaterialStore`, `MaterialRepository`, `ModernMaterialStore`, `BookstoreAPI` | **Hexagonal Architecture** |
| **`impl/`** | Core implementations (Adapters) | `ConcurrentMaterialStore`, `MaterialStoreImpl`, `ModernConcurrentMaterialStore`, `BookstoreArrayList` | **Adapter Pattern** |
| **`repository/`** | Data persistence (Adapters) | `JsonMaterialRepository`, `InMemoryRepository`, `ModernJsonMaterialRepository` | **Repository Pattern** |
| **`search/`** | Advanced search capabilities | `MaterialTrie`, `SearchResultCache`, `CachedSearchService`, `ModernSearchCache` | **Trie, LRU Cache** |
| **`observer/`** | Event-driven architecture | `MaterialEventPublisher`, `InventoryObserver`, `AuditLogObserver`, `AnalyticsObserver`, `MaterialEvent`, `MaterialAddedEvent`, `PriceChangedEvent` | **Observer Pattern** |
| **`composite/`** | Composite pattern implementation | `MaterialBundle`, `MaterialLeaf`, `MaterialComponent`, `BundleService` | **Composite Pattern** |
| **`decorator/`** | Decorator pattern implementation | `GiftWrappingDecorator`, `ExpeditedDeliveryDecorator`, `DigitalAnnotationDecorator`, `MaterialDecorator`, `MaterialEnhancementService` | **Decorator Pattern** |
| **`chain/`** | Chain of Responsibility | `DiscountApprovalService`, `ManagerHandler`, `DirectorHandler`, `VPHandler`, `DiscountRequest` | **Chain of Responsibility** |
| **`iterator/`** | Iterator pattern implementation | `MaterialIterator`, `MaterialTypeIterator`, `PriceRangeIterator`, `PriceSortedIterator`, `MaterialIteratorFactory` | **Iterator Pattern** |
| **`factory/`** | Factory pattern implementation | `MaterialFactory`, `AdvancedMaterialFactory` | **Factory Pattern** |
| **`builder/`** | Builder pattern implementation | `MaterialBuilder`, `EBookBuilder`, `MaterialBundleBuilder`, `ComponentBuilder`, `MaterialDirector` | **Builder Pattern** |
| **`visitor/`** | Visitor pattern implementation | `MaterialVisitor`, `ShippingCostCalculator` | **Visitor Pattern** |
| **`controller/`** | Spring Boot REST API | `MaterialController`, `BookstoreApplication` | **REST API** |
| **`service/`** | Service layer orchestration | `MaterialService`, `BundleService`, `MaterialEnhancementService`, `DiscountApprovalService` | **Service Layer** |
| **`demo/`** | Demonstration and educational classes | `BookstoreDemo`, `PolymorphismDemo` | **Educational** |
| **`utils/`** | Utility classes and helpers | `BookArrayUtils`, `LoggerFactory`, `MaterialsWrapper` | **Utility Classes** |

### **Modern Java Features Implementation**

| Feature | Implementation | Benefits |
|---------|----------------|----------|
| **Java Records** | `ModernInventoryStats` | Immutable data classes with validation |
| **CompletableFuture** | `ModernConcurrentMaterialStore` | Non-blocking async operations |
| **Streams API** | Throughout codebase | Functional programming and parallel processing |
| **NIO.2** | `ModernJsonMaterialRepository` | Modern file I/O with try-with-resources |
| **StampedLock** | `ModernConcurrentMaterialStore` | Optimistic reads (10x faster than ReadWriteLock) |
| **Virtual Threads** | ExecutorService integration | Better concurrency for I/O operations |
| **Try-with-resources** | All file operations | Automatic resource management |

### **Architecture Layers**

| Layer | Purpose | Key Components | Pattern |
|-------|---------|----------------|---------|
| **API Layer** | External interface contracts | `BookstoreAPI`, `MaterialStore`, `ModernMaterialStore` | **Ports** |
| **Service Layer** | Business logic orchestration | `MaterialService`, `BundleService`, `MaterialEnhancementService` | **Service Layer** |
| **Implementation Layer** | Core business logic | `ConcurrentMaterialStore`, `ModernConcurrentMaterialStore` | **Adapters** |
| **Repository Layer** | Data persistence | `JsonMaterialRepository`, `ModernJsonMaterialRepository` | **Repository Pattern** |
| **Search Layer** | Advanced search and caching | `MaterialTrie`, `ModernSearchCache`, `CachedSearchService` | **Search & Cache** |
| **Event Layer** | Event-driven communication | `MaterialEventPublisher`, Observer implementations | **Observer Pattern** |

### **Design Pattern Distribution**

| Pattern | Count | Key Implementations |
|---------|-------|-------------------|
| **Builder** | 5 classes | `MaterialDirector`, `EBookBuilder`, `MaterialBundleBuilder` |
| **Factory** | 2 classes | `MaterialFactory`, `AdvancedMaterialFactory` |
| **Observer** | 8 classes | `MaterialEventPublisher`, 3 observers, 2 events |
| **Composite** | 4 classes | `MaterialBundle`, `MaterialLeaf`, `BundleService` |
| **Decorator** | 4 classes | 3 decorators + `MaterialEnhancementService` |
| **Chain of Responsibility** | 5 classes | `DiscountApprovalService` + 3 handlers + request |
| **Iterator** | 5 classes | 3 iterators + factory + interface |
| **Visitor** | 2 classes | `MaterialVisitor`, `ShippingCostCalculator` |

### **Testing Infrastructure**

| Package | Purpose | Key Classes | Testing Type |
|---------|---------|-------------|--------------|
| **`test/`** | test suite | 25+ test classes | **Unit, Integration, Performance** |
| **`benchmark/`** | Performance testing | `BookstorePerformanceBenchmark` | **JMH Microbenchmarks** |
| **`property/`** | Property-based testing | `PropertyBasedTests` | **JUnit QuickCheck** |
| **`quality/`** | Quality assurance | `QualityGatesTest` | **Quality Validation** |
| **`integration/`** | End-to-end testing | `PerformanceIntegrationTest` | **Integration Tests** |

### **Key Statistics**

- **Total Source Files**: 68+ Java classes
- **Total Test Files**: 25+ test classes
- **Design Patterns**: 8 GoF patterns implemented
- **Modern Features**: 6+ modern Java features utilized
- **Test Coverage**: 54% (9,632 of 17,849 instructions)
- **Total Tests**: 620 tests passing
- **Architecture**: Hexagonal (Ports & Adapters)
- **Concurrency**: 3 different thread-safe implementations

## Quick Start

### Prerequisites

#### **Required Software**
- **Java 24** or higher (JDK recommended for development)
  - Required for modern Java features (Records, CompletableFuture, NIO.2)
  - Virtual Threads support for enhanced concurrency
  - Streams API and functional programming features
- **Maven 3.6** or higher
  - Build automation and dependency management
  - Plugin support for testing, coverage, and documentation
  - Profile support for different build configurations
- **Git** (for version control)
  - Source code management and collaboration
  - Branch management for feature development
  - Commit history tracking and code review

#### **Development Tools (Recommended)**
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code
  - Java development with syntax highlighting
  - Integrated debugging and testing support
  - Maven integration for build management
- **Graphviz** (for UML diagram generation)
  - Required for `mvn javadoc:javadoc` UML diagram generation
  - Visual representation of class relationships
  - Architecture documentation support

#### **Testing & Quality Tools**
- **JUnit 5** (included via Maven dependencies)
  - Unit testing framework for Java applications
  - Property-based testing with JUnit QuickCheck
  - Integration testing support
- **JMH (Java Microbenchmark Harness)** (included via Maven dependencies)
  - Performance benchmarking and microbenchmarks
  - Memory usage analysis and optimization
  - Concurrent performance testing
- **JaCoCo** (included via Maven plugin)
  - Code coverage analysis and reporting
  - Test coverage metrics and visualization
  - Quality gate enforcement

#### **Optional Tools**
- **SpotBugs** (included via Maven plugin)
  - Static code analysis for bug detection
  - Code quality improvement suggestions
  - Security vulnerability scanning
- **PMD** (included via Maven plugin)
  - Code quality analysis and rule enforcement
  - Coding standard compliance checking
  - Performance optimization suggestions
- **Checkstyle** (included via Maven plugin)
  - Code formatting and style enforcement
  - Naming convention validation
  - Documentation compliance checking

#### **System Requirements**
- **Operating System**: Windows, macOS, or Linux
- **Memory**: Minimum 4GB RAM (8GB recommended for performance testing)
- **Storage**: 500MB free space for project files and dependencies
- **Network**: Internet connection for Maven dependency downloads

#### **Environment Setup Verification**
```bash
# Verify Java version
java -version
# Should show Java 24 or higher

# Verify Maven installation
mvn -version
# Should show Maven 3.6 or higher

# Verify Git installation
git --version
# Should show Git version information

# Verify Graphviz installation (optional)
dot -V
# Should show Graphviz version information
```

#### **Maven Dependencies (Automatically Managed)**
- **JUnit 5**: Testing framework
- **JMH**: Performance benchmarking
- **Jackson**: JSON processing
- **SLF4J + Logback**: Logging framework
- **JUnit QuickCheck**: Property-based testing
- **SpotBugs, PMD, Checkstyle**: Code quality tools
- **JaCoCo**: Code coverage analysis

#### **Build Profiles Available**
- **Default**: Standard build with all features
- **Test**: Testing-focused build with coverage
- **Performance**: Performance testing and benchmarking
- **Quality**: Code quality analysis and reporting
- **Documentation**: Documentation generation and UML diagrams

### Installation & Setup

```bash
# Clone the repository
git clone <repository-url>
cd lab03

# Navigate to source directory
cd src

# Verify environment
./runme.sh --help

# Run complete build and test cycle
./runme.sh
```

### One-Command Build

The project includes a detailed build script that handles everything:

```bash
# Complete build with all features
./runme.sh

# Quick build (skip coverage/docs/UML)
./runme.sh --quick

# Run performance benchmarks
./runme.sh --performance

# Generate UML diagrams only
./runme.sh --uml-only

# Run tests only
./runme.sh --test-only

# Clean up generated files
./runme.sh --clean-uml
```

## Lab 4 Features

###  Modern Concurrency Improvements

#### ModernConcurrentMaterialStore with StampedLock and ExecutorService
- **StampedLock**: Optimistic read operations for 10x faster performance
- **ForkJoinPool**: Work-stealing thread pool for better throughput
- **CompletableFuture**: Async operations reduce blocking and improve scalability
- **AutoCloseable**: Proper resource management with graceful shutdown

```java
public class ModernConcurrentMaterialStore implements MaterialStore, AutoCloseable {
    private final StampedLock stampedLock = new StampedLock();
    private final ExecutorService executorService = new ForkJoinPool();
    
    public CompletableFuture<Optional<Material>> findByIdAsync(String id) {
        return CompletableFuture.supplyAsync(() -> {
            long stamp = stampedLock.tryOptimisticRead();
            Optional<Material> result = Optional.ofNullable(materials.get(id));
            
            if (!stampedLock.validate(stamp)) {
                stamp = stampedLock.readLock();
                try {
                    result = Optional.ofNullable(materials.get(id));
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            return result;
        }, executorService);
    }
}
```

#### ModernJsonMaterialRepository with try-with-resources and NIO.2
- **Try-with-resources**: Automatic resource management for all file operations
- **NIO.2 API**: Path and Files API for better file handling
- **Atomic Operations**: Atomic file writes prevent corruption
- **Backup & Recovery**: Automatic backup with rollback capability

```java
public class ModernJsonMaterialRepository implements MaterialRepository, AutoCloseable {
    public List<Material> findAll() {
        fileLock.readLock().lock();
        try (BufferedReader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
            return objectMapper.readValue(reader, new TypeReference<List<Material>>() {});
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to read from primary file, trying backup", e);
            return readFromBackup();
        } finally {
            fileLock.readLock().unlock();
        }
    }
}
```

#### ModernSearchCache with advanced caching features
- **Time-based Eviction**: Configurable TTL and idle timeout
- **Size-based Eviction**: LRU eviction when cache reaches capacity
- **Async Loading**: CompletableFuture-based cache loading
- **Statistics Tracking**: Hit rate, load time, and eviction metrics

```java
public class ModernSearchCache implements AutoCloseable {
    public CompletableFuture<List<Material>> getAsync(String key, Function<String, List<Material>> loader) {
        return CompletableFuture.supplyAsync(() -> {
            CacheEntry entry = cache.get(key);
            if (entry != null && !isExpired(entry)) {
                entry.accessCount().incrementAndGet();
                stats.hits.increment();
                return entry.value();
            }
            
            stats.misses.increment();
            List<Material> value = loader.apply(key);
            cache.put(key, CacheEntry.of(value));
            return value;
        }, executorService);
    }
}
```

#### ModernMaterialStore interface with Java records
- **Java Records**: Immutable data classes with built-in validation
- **Builder Pattern**: Fluent API for complex search criteria
- **CompletableFuture API**: Fully async interface design

```java
public interface ModernMaterialStore extends MaterialStore {
    record ModernInventoryStats(
        int totalCount,
        double averagePrice,
        double medianPrice,
        int uniqueTypes,
        int mediaCount,
        int printCount
    ) {
        public ModernInventoryStats {
            if (totalCount < 0) throw new IllegalArgumentException("Total count cannot be negative");
        }
        
        public String getSummary() {
            return String.format("""
                Inventory Statistics:
                - Total Items: %d
                - Average Price: $%.2f
                - Media Materials: %d (%.1f%%)
                - Print Materials: %d (%.1f%%)
                """, totalCount, averagePrice, mediaCount, getMediaPercentage(), 
                     printCount, getPrintPercentage());
        }
    }
    
    CompletableFuture<ModernInventoryStats> getModernInventoryStatsAsync();
    CompletableFuture<List<Material>> advancedSearchAsync(SearchCriteria criteria);
}
```

###  Concurrency & Thread Safety

**ConcurrentMaterialStore** implements thread-safe operations using:
- `ReentrantReadWriteLock` for fine-grained locking
- `ConcurrentHashMap` for thread-safe data storage
- Atomic operations for counters and statistics

```java
public class ConcurrentMaterialStore implements MaterialStore {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ConcurrentHashMap<String, Material> materials = new ConcurrentHashMap<>();
    
    @Override
    public boolean addMaterial(Material material) {
        lock.writeLock().lock();
        try {
            return materials.putIfAbsent(material.getId(), material) == null;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

###  Hexagonal Architecture

**Ports and Adapters** pattern implementation:

```java
// Port (Interface)
public interface MaterialRepository {
    void save(Material material);
    Optional<Material> findById(String id);
    List<Material> findAll();
    boolean delete(String id);
}

// Adapter (Implementation)
public class JsonMaterialRepository implements MaterialRepository {
    private final ObjectMapper objectMapper;
    private final Path dataFile;
    
    @Override
    public void save(Material material) {
        // JSON serialization logic
    }
}
```

###  Advanced Search & Caching

**Trie-based prefix search** with **LRU caching**:

```java
public class MaterialTrie {
    private final TrieNode root = new TrieNode();
    
    public void insert(Material material) {
        TrieNode current = root;
        String title = material.getTitle().toLowerCase();
        
        for (char c : title.toCharArray()) {
            current = current.getChildren().computeIfAbsent(c, k -> new TrieNode());
        }
        current.getMaterials().add(material);
    }
    
    public List<Material> searchByPrefix(String prefix) {
        // Efficient prefix search implementation
    }
}

public class SearchResultCache {
    private final LRUCache<String, List<Material>> cache;
    
    public Optional<List<Material>> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }
}
```

###  Design Patterns Implementation

#### Composite Pattern
```java
public class MaterialBundle extends Material {
    private final List<MaterialComponent> components = new ArrayList<>();
    private final double bundleDiscount;
    
    public void addComponent(MaterialComponent component) {
        components.add(component);
    }
    
    @Override
    public double getPrice() {
        return components.stream()
            .mapToDouble(MaterialComponent::getPrice)
            .sum() * (1.0 - bundleDiscount);
    }
}
```

#### Observer Pattern
```java
public class MaterialEventPublisher {
    private final List<MaterialObserver> observers = new ArrayList<>();
    
    public void publishEvent(MaterialEvent event) {
        observers.forEach(observer -> observer.onMaterialEvent(event));
    }
    
    public void subscribe(MaterialObserver observer) {
        observers.add(observer);
    }
}
```

#### Chain of Responsibility
```java
public abstract class DiscountApprovalHandler {
    protected DiscountApprovalHandler nextHandler;
    
    public void setNext(DiscountApprovalHandler handler) {
        this.nextHandler = handler;
    }
    
    public abstract void handleRequest(DiscountRequest request);
}

public class Manager extends DiscountApprovalHandler {
    @Override
    public void handleRequest(DiscountRequest request) {
        if (request.getDiscountPercentage() <= 15.0) {
            // Approve
        } else if (nextHandler != null) {
            nextHandler.handleRequest(request);
        }
    }
}
```

###  Performance Analysis

**JMH Benchmarks** for detailed performance testing:

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class BookstorePerformanceBenchmark {
    
    @Benchmark
    public void benchmarkConcurrentSearch() {
        for (int i = 0; i < 1000; i++) {
            String randomId = "ID-" + (i % 1000);
            concurrentStore.findById(randomId);
        }
    }
    
    @Benchmark
    public void benchmarkTriePrefixSearch() {
        for (int i = 0; i < 100; i++) {
            String prefix = "Book" + (i % 100);
            trie.searchByPrefix(prefix);
        }
    }
}
```

###  Property-Based Testing

**JUnit QuickCheck** for invariant validation:

```java
@Property(trials = 100)
public void testEBookDiscountProperty(
        @Size(min = 1, max = 50) String title,
        @InRange(minDouble = 0.0, maxDouble = 1000.0) double price,
        boolean drmEnabled) {
    
    EBook ebook = new EBook("test-id", title, "Test Author", price, 2020, 
                           "EPUB", 1.0, drmEnabled, 10000, MediaQuality.HIGH);
    
    // Property: DRM-free books should have 15% discount
    if (!drmEnabled) {
        assertEquals(0.15, ebook.getDiscountRate(), 0.001);
    }
}
```

## Performance Testing

### Performance Analysis

```java
public class PerformanceAnalyzer {
    public PerformanceReport analyzeDataStructures() {
        // Compare ArrayList vs ConcurrentHashMap performance
        // Measure insertion, search, and memory usage
    }
    
    public CachePerformanceReport analyzeCachePerformance() {
        // Analyze cache hit ratios and response times
    }
    
    public SearchPerformanceReport analyzeSearchPerformance() {
        // Compare different search implementations
    }
}
```

## Testing Strategy

### Test Examples

```java
@Test
@DisplayName("Concurrent access should be thread-safe")
public void testConcurrentAccess() {
    ConcurrentMaterialStore store = new ConcurrentMaterialStore();
    List<Material> testMaterials = generateTestMaterials(1000);
    
    // Test concurrent write performance
    long concurrentWriteTime = measureTime(() -> {
        testMaterials.parallelStream().forEach(store::addMaterial);
    });
    
    assertTrue(concurrentWriteTime > 0);
    assertTrue(store.size() > 0);
}

@Test
@DisplayName("Trie search should return consistent results")
public void testTrieSearchProperty() {
    MaterialTrie trie = new MaterialTrie();
    
    // Add materials with predictable titles
    for (int i = 0; i < 100; i++) {
        String title = "Java Book " + i;
        EBook ebook = new EBook("ID-" + i, title, "Author " + i, 29.99, 2023,
                               "EPUB", 2.0, false, 50000, MediaQuality.HIGH);
        trie.insert(ebook);
    }
    
    // Property: Prefix search should return all materials with matching prefix
    var results = trie.searchByPrefix("Java");
    assertTrue(results.size() >= 100);
}
```

## Build System & Automation

### Available Commands

| Command | Description | Use Case |
|---------|-------------|----------|
| `./runme.sh` | Complete build cycle | Full development workflow |
| `./runme.sh --performance` | Run performance benchmarks | Performance analysis |
| `./runme.sh --quick` | Fast build | Quick compilation check |
| `./runme.sh --test-only` | Run tests only | Testing phase |
| `./runme.sh --uml-only` | Generate UML diagrams | Documentation |
| `./runme.sh --clean-uml` | Clean generated files | Maintenance |
| `./runme.sh --help` | Show all options | Reference |

### Maven Commands

```bash
# Compile source code
mvn clean compile

# Run all tests including Modern* classes
mvn test

# Run specific modern concurrency tests
mvn test -Dtest=ModernConcurrentMaterialStoreTest

# Run performance tests
mvn test -Dtest=PerformanceIntegrationTest

# Run property-based tests
mvn test -Dtest=PropertyBasedTests

# Run JMH benchmarks
mvn exec:java -Dexec.mainClass=com.university.bookstore.performance.BookstorePerformanceBenchmark

# Start Spring Boot application
mvn spring-boot:run

# Test REST API endpoints
./test-rest-api.sh

# Generate coverage report
mvn jacoco:report
# View: target/site/jacoco/index.html

# Generate Javadoc
mvn javadoc:javadoc
# View: target/site/apidocs/index.html

# Run with profile for Modern* classes performance
mvn test -Pconcurrency-performance

# Clean and rebuild
mvn clean compile test

# Package as JAR
mvn package
```

## API Reference

### Core Interfaces

#### MaterialStore Interface
```java
public interface MaterialStore {
    // Basic CRUD operations
    boolean addMaterial(Material material);
    Optional<Material> removeMaterial(String id);
    Optional<Material> findById(String id);
    
    // Search operations
    List<Material> searchByTitle(String title);
    List<Material> searchByCreator(String creator);
    List<Material> getMaterialsByType(MaterialType type);
    
    // Enhanced search capabilities
    List<Material> findRecentMaterials(int years);
    List<Material> findByCreators(String... creators);
    List<Material> findWithPredicate(Predicate<Material> condition);
    List<Material> getSorted(Comparator<Material> comparator);
    
    // Utility operations
    int size();
    boolean isEmpty();
    List<Material> getAllMaterials();
}
```

#### MaterialRepository Interface (Hexagonal Architecture)
```java
public interface MaterialRepository {
    void save(Material material);
    Optional<Material> findById(String id);
    List<Material> findAll();
    boolean delete(String id);
    boolean exists(String id);
    long count();
    void deleteAll();
}
```

### Core Classes

#### ConcurrentMaterialStore (Thread-Safe Implementation)
```java
public class ConcurrentMaterialStore implements MaterialStore {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ConcurrentHashMap<String, Material> materials = new ConcurrentHashMap<>();
    private final AtomicInteger size = new AtomicInteger(0);
    
    @Override
    public boolean addMaterial(Material material) {
        lock.writeLock().lock();
        try {
            if (materials.putIfAbsent(material.getId(), material) == null) {
                size.incrementAndGet();
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

#### MaterialTrie (Advanced Search)
```java
public class MaterialTrie {
    private final TrieNode root = new TrieNode();
    
    public void insert(Material material) {
        TrieNode current = root;
        String title = material.getTitle().toLowerCase();
        
        for (char c : title.toCharArray()) {
            current = current.getChildren().computeIfAbsent(c, k -> new TrieNode());
        }
        current.getMaterials().add(material);
    }
    
    public List<Material> searchByPrefix(String prefix) {
        TrieNode current = root;
        String lowerPrefix = prefix.toLowerCase();
        
        for (char c : lowerPrefix.toCharArray()) {
            current = current.getChildren().get(c);
            if (current == null) {
                return Collections.emptyList();
            }
        }
        
        return collectAllMaterials(current);
    }
}
```

## Learning Outcomes

By completing Lab 4, students will:

1. **Master Concurrency**: Understand thread safety and concurrent programming
2. **Apply Hexagonal Architecture**: Implement clean architecture principles
3. **Use Advanced Design Patterns**: Apply multiple design patterns in real scenarios
4. **Optimize Performance**: Use JMH for performance benchmarking
5. **Write Property-Based Tests**: Validate invariants with QuickCheck
6. **Implement Caching**: Design and implement efficient caching strategies
7. **Practice Quality Engineering**: Use mutation testing and quality gates

### Key Concepts Demonstrated

- **Concurrency**: Thread-safe data structures and locking mechanisms
- **Hexagonal Architecture**: Ports and adapters pattern
- **Advanced Design Patterns**: Composite, Decorator, Observer, Chain of Responsibility
- **Performance Optimization**: JMH benchmarking and analysis
- **Property-Based Testing**: Invariant validation with randomized inputs
- **Caching Strategies**: LRU cache implementation and performance analysis
- **Quality Engineering**: Mutation testing and detailed quality gates

## Development Guide

### Project Structure

```
src/
├── main/
│   ├── java/com/university/bookstore/
│   │   ├── api/                    # Interface contracts (Ports)
│   │   │   ├── BookstoreAPI.java           # Legacy book-only API
│   │   │   ├── MaterialStore.java          # Core store interface
│   │   │   └── ModernMaterialStore.java    # Modern async interface with records
│   │   ├── builder/                # Builder pattern implementations
│   │   │   ├── ComponentBuilder.java       # Generic component builder interface
│   │   │   ├── EBookBuilder.java           # EBook-specific builder
│   │   │   ├── MaterialBuilder.java        # Generic material builder interface
│   │   │   ├── MaterialBundleBuilder.java  # Bundle builder
│   │   │   └── MaterialDirector.java       # Director for complex construction
│   │   ├── chain/                  # Chain of Responsibility pattern
│   │   │   ├── DirectorHandler.java        # Director-level discount approval
│   │   │   ├── DiscountApprovalService.java # Approval workflow service
│   │   │   ├── DiscountHandler.java        # Abstract discount handler
│   │   │   ├── DiscountRequest.java        # Discount request model
│   │   │   ├── ManagerHandler.java         # Manager-level approval
│   │   │   └── VPHandler.java              # VP-level approval
│   │   ├── composite/              # Composite pattern
│   │   │   ├── BundleService.java          # Bundle management service
│   │   │   ├── MaterialBundle.java         # Composite bundle component
│   │   │   ├── MaterialComponent.java      # Component interface
│   │   │   └── MaterialLeaf.java           # Leaf component wrapper
│   │   ├── decorator/              # Decorator pattern
│   │   │   ├── DigitalAnnotationDecorator.java    # Digital annotations
│   │   │   ├── ExpeditedDeliveryDecorator.java    # Fast delivery
│   │   │   ├── GiftWrappingDecorator.java         # Gift wrapping
│   │   │   ├── MaterialDecorator.java             # Abstract decorator
│   │   │   └── MaterialEnhancementService.java    # Enhancement service
│   │   ├── demo/                   # Demonstration classes
│   │   │   └── PolymorphismDemo.java       # OOP demo
│   │   ├── factory/                # Factory pattern
│   │   │   ├── AdvancedMaterialFactory.java # Enhanced factory
│   │   │   └── MaterialFactory.java        # Core material factory
│   │   ├── impl/                   # Core implementations
│   │   │   ├── BookstoreArrayList.java     # Legacy book implementation
│   │   │   ├── ConcurrentMaterialStore.java # Thread-safe with ReadWriteLock
│   │   │   ├── MaterialStoreImpl.java      # Basic ArrayList implementation
│   │   │   └── ModernConcurrentMaterialStore.java # Modern StampedLock implementation
│   │   ├── iterator/               # Iterator pattern
│   │   │   ├── MaterialIterator.java       # Iterator interface
│   │   │   ├── MaterialIteratorFactory.java # Iterator factory
│   │   │   ├── MaterialTypeIterator.java   # Type-based iteration
│   │   │   ├── PriceRangeIterator.java     # Price range iteration
│   │   │   └── PriceSortedIterator.java    # Price-sorted iteration
│   │   ├── model/                  # Domain models and entities
│   │   │   ├── AudioBook.java              # Audio book model
│   │   │   ├── Book.java                   # Legacy book model
│   │   │   ├── EBook.java                  # Electronic book model
│   │   │   ├── Magazine.java               # Magazine model
│   │   │   ├── Material.java               # Abstract base material
│   │   │   ├── Media.java                  # Media interface
│   │   │   ├── PrintedBook.java            # Physical book model
│   │   │   └── VideoMaterial.java          # Video material model
│   │   ├── observer/               # Observer pattern
│   │   │   ├── AnalyticsObserver.java      # Analytics tracking
│   │   │   ├── AuditLogObserver.java       # Audit logging
│   │   │   ├── InventoryObserver.java      # Inventory tracking
│   │   │   ├── MaterialAddedEvent.java     # Material added event
│   │   │   ├── MaterialEvent.java          # Base material event
│   │   │   ├── MaterialEventPublisher.java # Event publisher
│   │   │   ├── MaterialObserver.java       # Observer interface
│   │   │   ├── MaterialSubject.java        # Subject interface
│   │   │   └── PriceChangedEvent.java      # Price change event
│   │   ├── repository/             # Data persistence (Adapters)
│   │   │   ├── JsonMaterialRepository.java     # JSON-based repository
│   │   │   ├── MaterialRepository.java         # Repository interface
│   │   │   ├── MaterialsWrapper.java           # JSON serialization wrapper
│   │   │   ├── ModernJsonMaterialRepository.java # Modern NIO.2 repository
│   │   │   └── RepositoryException.java         # Repository exceptions
│   │   ├── search/                 # Advanced search capabilities
│   │   │   ├── CachedSearchService.java    # Cached search service
│   │   │   ├── MaterialTrie.java           # Trie data structure
│   │   │   ├── ModernSearchCache.java      # Advanced cache with TTL
│   │   │   └── SearchResultCache.java      # LRU cache implementation
│   │   ├── service/                # Service layer
│   │   │   └── MaterialService.java        # Business logic service
│   │   ├── utils/                  # Utility classes
│   │   │   ├── BookArrayUtils.java         # Array utilities
│   │   │   └── LoggerFactory.java          # Logging configuration
│   │   └── visitor/                # Visitor pattern
│   │       ├── MaterialVisitor.java        # Visitor interface
│   │       └── ShippingCostCalculator.java # Shipping cost visitor
│   │   ├── controller/             # Spring Boot REST API
│   │   │   └── MaterialController.java     # REST API controller
│   │   └── BookstoreApplication.java       # Spring Boot application
│   └── resources/
│       ├── logback.xml             # Logging configuration
│       └── application.yml         # Spring Boot configuration
└── test/
    └── java/com/university/bookstore/
        ├── api/                    # API tests
        │   ├── BookstoreAPITest.java
        │   ├── MaterialStoreTest.java
        │   └── ModernInventoryStatsTest.java
        ├── controller/              # Spring Boot REST API tests
        │   └── MaterialControllerTest.java
        ├── builder/                # Builder pattern tests
        │   └── EBookBuilderTest.java
        ├── composite/              # Composite pattern tests
        │   ├── BundleServiceTest.java
        │   └── MaterialLeafTest.java
        ├── decorator/              # Decorator pattern tests
        │   ├── DigitalAnnotationDecoratorTest.java
        │   ├── ExpeditedDeliveryDecoratorTest.java
        │   ├── GiftWrappingDecoratorTest.java
        │   ├── MaterialDecoratorTest.java
        │   └── MaterialEnhancementServiceTest.java
        ├── factory/                # Factory pattern tests
        │   ├── MaterialFactoryTest.java
        │   └── MaterialFactoryTestNew.java
        ├── impl/                   # Implementation tests
        │   ├── BookstoreArrayListTest.java
        │   ├── EnhancedSearchTest.java
        │   ├── MaterialStoreImplTest.java
        │   └── ModernConcurrentMaterialStoreTest.java
        ├── model/                  # Model tests
        │   ├── BookTest.java
        │   ├── EBookTest.java
        │   └── MaterialPolymorphismTest.java
        ├── observer/               # Observer pattern tests
        │   └── InventoryObserverTest.java
        ├── performance/            # Performance testing
        │   ├── BookstorePerformanceBenchmark.java  # JMH benchmarks
        │   ├── PerformanceAnalyzer.java            # Performance analysis
        │   ├── PerformanceIntegrationTest.java     # Integration tests
        │   └── PropertyBasedTests.java             # Property-based testing
        ├── quality/                # Quality assurance
        │   └── QualityGatesTest.java               # Quality gates
        ├── repository/             # Repository tests
        │   ├── JsonMaterialRepositoryTest.java
        │   ├── ModernJsonMaterialRepositoryTest.java
        │   └── RepositoryExceptionTest.java
        ├── search/                 # Search tests
        │   ├── CachedSearchServiceTest.java
        │   ├── MaterialTrieTest.java
        │   └── SearchResultCacheTest.java
        ├── utils/                  # Utility tests
        │   └── BookArrayUtilsTest.java
        └── visitor/                # Visitor pattern tests
            └── ShippingCostCalculatorTest.java
```

### Development Workflow

#### 1. **Environment Setup & Verification**
```bash
# Navigate to project directory
cd src

# Verify environment and show all available options
./runme.sh --help

# Check Java version and Maven setup
java -version
mvn -version

# Verify project structure
ls -la src/main/java/com/university/bookstore/
```

#### 2. **Development Commands**

**Quick Development Cycle:**
```bash
# Fast compilation check (skip tests, docs, coverage)
./runme.sh --quick

# Quick test run (skip performance and integration tests)
./runme.sh --test-only

# Run specific test categories
./runme.sh --unit-tests      # Unit tests only
./runme.sh --integration     # Integration tests only
./runme.sh --performance     # Performance benchmarks only
```

**Testing:**
```bash
# Run all tests with coverage
./runme.sh --coverage

# Run specific test suites
mvn test -Dtest=MaterialStoreTest
mvn test -Dtest=ModernConcurrentMaterialStoreTest
mvn test -Dtest=PerformanceIntegrationTest
mvn test -Dtest=PropertyBasedTests
mvn test -Dtest=QualityGatesTest

# Run tests with specific profiles
mvn test -Pconcurrency-performance
mvn test -Pmodern-features
mvn test -Pquality-gates
```

**Performance Analysis:**
```bash
# Run JMH benchmarks
./runme.sh --performance
mvn exec:java -Dexec.mainClass=com.university.bookstore.performance.BookstorePerformanceBenchmark

# Run performance analysis
mvn test -Dtest=PerformanceAnalyzer
mvn test -Dtest=PerformanceIntegrationTest

# Memory profiling
mvn test -Dtest=MemoryUsageTest
```

#### 3. **Code Quality & Analysis**

**Code Quality Checks:**
```bash
# Run quality gates
./runme.sh --quality-gates

# Run mutation testing
mvn test -Dtest=QualityGatesTest

# Check code coverage
mvn jacoco:report
open target/site/jacoco/index.html

# Run static analysis
mvn spotbugs:check
mvn pmd:check
```

**Code Generation & Documentation:**
```bash
# Generate UML diagrams
./runme.sh --uml-only

# Generate Javadoc
mvn javadoc:javadoc
open target/site/apidocs/index.html

# Generate all documentation
./runme.sh --docs
```

#### 4. **Build & Packaging**

**Build Options:**
```bash
# Complete build cycle (compile, test, coverage, docs, UML)
./runme.sh

# Clean build
./runme.sh --clean

# Build without tests
mvn clean compile

# Package as JAR
mvn package

# Install to local repository
mvn install
```

**Advanced Build Options:**
```bash
# Build with specific Java version
mvn clean compile -Dmaven.compiler.source=24 -Dmaven.compiler.target=24

# Build with debug information
mvn clean compile -Dmaven.compiler.debug=true

# Build with specific profile
mvn clean compile -Pproduction
mvn clean compile -Pdevelopment
```

#### 5. **Debugging & Troubleshooting**

**Debug Commands:**
```bash
# Run with debug output
mvn test -X

# Run specific test with debug
mvn test -Dtest=MaterialStoreTest -Ddebug=true

# Check for compilation issues
mvn clean compile -Dmaven.compiler.verbose=true

# Verify dependencies
mvn dependency:tree
mvn dependency:analyze
```

**Logging & Monitoring:**
```bash
# Run with specific log level
mvn test -Dlogging.level.com.university.bookstore=DEBUG

# Check log files
tail -f target/logs/bookstore.log
tail -f target/logs/performance.log
```

#### 6. **Modern Features Testing**

**Concurrency Testing:**
```bash
# Test modern concurrency features
mvn test -Dtest=ModernConcurrentMaterialStoreTest

# Test async operations
mvn test -Dtest=AsyncOperationsTest

# Test CompletableFuture features
mvn test -Dtest=CompletableFutureTest
```

**Modern Java Features:**
```bash
# Test Java Records
mvn test -Dtest=ModernInventoryStatsTest

# Test NIO.2 features
mvn test -Dtest=ModernJsonMaterialRepositoryTest

# Test StampedLock implementation
mvn test -Dtest=StampedLockTest
```

#### 7. **Property-Based Testing**

**QuickCheck Testing:**
```bash
# Run property-based tests
mvn test -Dtest=PropertyBasedTests

# Run with specific trial count
mvn test -Dtest=PropertyBasedTests -Dtrials=1000

# Run specific property tests
mvn test -Dtest=PropertyBasedTests#testEBookDiscountProperty
```

#### 8. **Integration & End-to-End Testing**

**Integration Testing:**
```bash
# Run integration tests
./runme.sh --integration

# Test complete system integration
mvn test -Dtest=QualityGatesTest

# Test all design patterns together
mvn test -Dtest=DesignPatternIntegrationTest
```

#### 9. **Cleanup & Maintenance**

**Cleanup Commands:**
```bash
# Clean generated files
./runme.sh --clean-uml
./runme.sh --clean-docs

# Clean Maven artifacts
mvn clean

# Clean everything
mvn clean
rm -rf target/
rm -rf *.log
```

**Maintenance:**
```bash
# Update dependencies
mvn versions:display-dependency-updates
mvn versions:use-latest-releases

# Check for security vulnerabilities
mvn org.owasp:dependency-check-maven:check
```

#### 10. **Development Best Practices**

**Before Committing:**
```bash
# Run complete quality check
./runme.sh --quality-gates

# Ensure all tests pass
./runme.sh --test-only

# Check code coverage
mvn jacoco:report

# Verify no compilation warnings
mvn clean compile -Dmaven.compiler.showWarnings=true
```

**Continuous Integration Simulation:**
```bash
# Simulate CI pipeline
./runme.sh --ci-pipeline

# Run all quality checks
./runme.sh --full-quality-check
```

#### 11. **Useful Development Shortcuts**

**Common Development Patterns:**
```bash
# Quick development cycle
alias dev-cycle='./runme.sh --quick && ./runme.sh --test-only'

# Performance testing cycle
alias perf-cycle='./runme.sh --performance && mvn jacoco:report'

# Quality assurance cycle
alias qa-cycle='./runme.sh --quality-gates && ./runme.sh --coverage'

# Documentation cycle
alias doc-cycle='./runme.sh --uml-only && mvn javadoc:javadoc'
```

**Environment Variables:**
```bash
# Set development environment
export BOOKSTORE_ENV=development
export LOG_LEVEL=DEBUG
export PERFORMANCE_MODE=true

# Set test environment
export BOOKSTORE_ENV=test
export TEST_DATA_SIZE=1000
export BENCHMARK_ITERATIONS=100
```
## Contributing

### Development Guidelines

#### **Code Quality & Style**
1. **Java Naming Conventions**: Follow Oracle Java naming conventions
   - Classes: PascalCase (e.g., `MaterialStore`, `ConcurrentMaterialStore`)
   - Methods/Variables: camelCase (e.g., `addMaterial`, `materialCount`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_CACHE_SIZE`, `DEFAULT_TIMEOUT`)
   - Packages: lowercase with dots (e.g., `com.university.bookstore.service`)

2. **Code Organization**: Maintain clean package structure
   - Keep related classes in appropriate packages
   - Use interfaces for contracts (Ports in hexagonal architecture)
   - Implement adapters for external dependencies
   - Separate concerns (model, service, repository, implementation)

3. **Documentation Standards**: JavaDoc for all public APIs
   - Class-level documentation with purpose and usage examples
   - Method documentation with parameters, return values, and exceptions
   - Include `@author`, `@version`, `@since` tags where appropriate
   - Document design patterns and architectural decisions

#### **Testing Strategy**
4. **Unit Testing**: test coverage for all components
   - Test all public methods with various input scenarios
   - Use JUnit 5 with proper test organization (`@Test`, `@BeforeEach`, `@AfterEach`)
   - Test edge cases, boundary conditions, and error scenarios
   - Aim for high code coverage (target: 80%+)

5. **Integration Testing**: Test component interactions
   - Test service layer with repository implementations
   - Test event-driven architecture with observer patterns
   - Test concurrent operations and thread safety
   - Use `@SpringBootTest` or similar for integration scenarios

6. **Performance Testing**: Benchmark critical operations
   - Use JMH (Java Microbenchmark Harness) for microbenchmarks
   - Test search operations, caching performance, and concurrent access
   - Measure memory usage and garbage collection impact
   - Document performance characteristics and optimization opportunities

7. **Property-Based Testing**: Validate system invariants
   - Use JUnit QuickCheck for property-based testing
   - Test mathematical properties (associativity, commutativity)
   - Validate business rules and constraints
   - Test with random inputs to find edge cases

#### **Performance & Optimization**
8. **Algorithm Complexity**: Choose appropriate data structures and algorithms
   - Use Trie for prefix-based search (O(m) complexity)
   - Implement LRU caching for frequently accessed data
   - Use StampedLock for optimistic reads (10x faster than ReadWriteLock)
   - Leverage parallel streams for bulk operations

9. **Memory Management**: Efficient resource utilization
   - Use try-with-resources for automatic resource cleanup
   - Implement AutoCloseable for custom resources
   - Avoid memory leaks in long-running applications
   - Monitor memory usage with profiling tools

10. **Caching Strategy**: Implement intelligent caching
    - Use LRU cache for search results
    - Implement TTL (Time To Live) for cache entries
    - Consider cache invalidation strategies
    - Monitor cache hit rates and performance

#### **Concurrency & Thread Safety**
11. **Thread Safety**: Ensure safe concurrent access
    - Use appropriate synchronization mechanisms (locks, atomic operations)
    - Implement thread-safe collections (ConcurrentHashMap, CopyOnWriteArrayList)
    - Use CompletableFuture for asynchronous operations
    - Test concurrent scenarios with multiple threads

12. **Lock Strategy**: Choose appropriate locking mechanisms
    - Use ReentrantReadWriteLock for read-heavy scenarios
    - Use StampedLock for optimistic reads and better performance
    - Use synchronized blocks for simple critical sections
    - Avoid deadlocks with proper lock ordering

13. **Async Operations**: Implement non-blocking operations
    - Use CompletableFuture for asynchronous processing
    - Implement proper error handling for async operations
    - Use ExecutorService for thread pool management
    - Consider Virtual Threads for better concurrency

#### **Architecture & Design Patterns**
14. **Hexagonal Architecture**: Follow ports and adapters pattern
    - Define clear interfaces (ports) for business logic
    - Implement adapters for external dependencies
    - Keep business logic independent of infrastructure
    - Use dependency injection for loose coupling

15. **Design Patterns**: Implement appropriate GoF patterns
    - Builder Pattern: For complex object construction
    - Factory Pattern: For object creation with different strategies
    - Observer Pattern: For event-driven architecture
    - Composite Pattern: For tree-like structures
    - Decorator Pattern: For adding functionality dynamically
    - Chain of Responsibility: For request processing pipelines
    - Iterator Pattern: For collection traversal
    - Visitor Pattern: For operations on object structures

16. **SOLID Principles**: Follow object-oriented design principles
    - Single Responsibility: Each class has one reason to change
    - Open/Closed: Open for extension, closed for modification
    - Liskov Substitution: Subtypes must be substitutable for base types
    - Interface Segregation: Clients shouldn't depend on unused interfaces
    - Dependency Inversion: Depend on abstractions, not concretions

#### **Error Handling & Validation**
17. **Defensive Programming**: Validate inputs and handle errors gracefully
    - Check for null values and invalid inputs
    - Use appropriate exception types (checked vs unchecked)
    - Implement proper error messages and logging
    - Use validation frameworks where appropriate

18. **Exception Handling**: Implement error handling
    - Use specific exception types for different error scenarios
    - Implement proper exception chaining
    - Log errors with appropriate levels (ERROR, WARN, INFO)
    - Provide meaningful error messages to users

19. **Input Validation**: Validate all external inputs
    - Check parameter ranges and constraints
    - Validate file formats and data integrity
    - Implement proper sanitization for user inputs
    - Use validation annotations where appropriate

#### **Modern Java Features**
20. **Java Records**: Use records for immutable data classes
    - Implement validation in record constructors
    - Use records for DTOs and value objects
    - Leverage record features (equals, hashCode, toString)

21. **Streams API**: Use functional programming where appropriate
    - Use streams for collection processing
    - Implement parallel streams for performance
    - Use collectors for data aggregation
    - Prefer streams over traditional loops for readability

22. **NIO.2**: Use modern file I/O operations
    - Use Path and Files for file operations
    - Implement try-with-resources for automatic cleanup
    - Use atomic operations for file updates
    - Handle file system exceptions properly

#### **Build & Deployment**
23. **Maven Configuration**: Maintain proper build configuration
    - Use appropriate Maven plugins (compiler, surefire, jacoco)
    - Configure build profiles for different environments
    - Use proper dependency management
    - Implement build scripts for automation

24. **Code Quality Tools**: Integrate quality assurance tools
    - Use SpotBugs for static analysis
    - Use PMD for code quality checks
    - Use JaCoCo for code coverage
    - Use Checkstyle for code formatting

25. **Documentation**: Maintain documentation
    - Keep README.md updated with project information
    - Document architectural decisions and design patterns
    - Maintain API documentation with JavaDoc
    - Document build and deployment procedures

#### **Best Practices**
26. **Version Control**: Use proper Git practices
    - Write meaningful commit messages
    - Use feature branches for development
    - Implement proper branching strategy
    - Use .gitignore for build artifacts

27. **Code Review**: Implement peer review process
    - Review code for quality, performance, and security
    - Check for adherence to coding standards
    - Verify test coverage and quality
    - Ensure architectural consistency

28. **Continuous Integration**: Implement CI/CD practices
    - Automate build and test processes
    - Run quality checks on every commit
    - Generate reports for code coverage and quality
    - Implement automated deployment where appropriate

## License

Educational use only

## Contributors

- **Course**: CSSD2101 - Software Development
- **Lab Assignment**: Bookstore Management System - Lab 4
- **Academic Year**: 2025
- **Author**: Navid Mohaghegh

---

## Getting Started

Ready to explore advanced software engineering concepts? Start with:

```bash
cd src
./runme.sh --help
```

**Happy Coding!**