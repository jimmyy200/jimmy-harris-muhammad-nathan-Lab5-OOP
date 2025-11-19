package com.university.bookstore.builder;

import com.university.bookstore.model.Material;

/**
 * Builder interface for creating Material instances.
 * Provides a fluent interface for constructing complex material objects.
 * 
 * <p>This interface demonstrates the Builder pattern by providing a way to
 * construct complex objects step by step with a fluent interface.</p>
 * 
 * @param <T> the type of material being built
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public interface MaterialBuilder<T extends Material> {
    
    /**
     * Builds and returns the material instance.
     * 
     * @return the built material
     * @throws IllegalStateException if required fields are missing or invalid
     */
    T build();
    
    /**
     * Validates that all required fields are set.
     * 
     * @throws IllegalStateException if validation fails
     */
    void validate();
    
    /**
     * Resets the builder to its initial state.
     */
    void reset();
}
