package com.university.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.university.bookstore.api.MaterialStore;
import com.university.bookstore.impl.ModernConcurrentMaterialStore;

/**
 * Spring Boot application entry point for the Bookstore Management System.
 * 
 * <p>This class demonstrates how the existing design can be easily extended
 * with Spring Boot to provide REST API capabilities without modifying the
 * core business logic or existing implementations.</p>
 * 
 * <p>The application leverages the existing MaterialStore interface and
 * implementations, showing the extensibility and scalability of the
 * hexagonal architecture design.</p>
 * 
 * @author Navid Mohaghegh
 * @version 1.0
 * @since 2024-12-19
 */
@SpringBootApplication
@Configuration
public class BookstoreApplication {

    /**
     * Main entry point for the Spring Boot application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    /**
     * Configures the MaterialStore bean using the existing ModernConcurrentMaterialStore.
     * This demonstrates how the existing implementations can be easily integrated
     * with Spring Boot dependency injection.
     * 
     * @return configured MaterialStore instance
     */
    @Bean
    public MaterialStore materialStore() {
        return new ModernConcurrentMaterialStore();
    }
}

