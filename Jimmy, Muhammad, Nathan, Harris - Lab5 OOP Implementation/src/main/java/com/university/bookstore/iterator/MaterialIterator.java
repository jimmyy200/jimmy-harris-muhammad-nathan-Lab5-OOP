package com.university.bookstore.iterator;

import com.university.bookstore.model.Material;

/**
 * Iterator interface for traversing collections of materials.
 * Provides a uniform way to iterate over materials with different traversal strategies.
 * 
 * <p>This interface demonstrates the Iterator pattern by encapsulating traversal logic
 * and allowing different iteration strategies to be implemented.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public interface MaterialIterator {
    
    /**
     * Checks if there are more materials to iterate over.
     * 
     * @return true if there are more materials
     */
    boolean hasNext();
    
    /**
     * Gets the next material in the iteration.
     * 
     * @return the next material
     * @throws java.util.NoSuchElementException if there are no more materials
     */
    Material next();
    
    /**
     * Resets the iterator to the beginning.
     */
    void reset();
    
    /**
     * Gets the current position in the iteration.
     * 
     * @return the current position (0-based)
     */
    int getCurrentPosition();
    
    /**
     * Gets the total number of materials that can be iterated.
     * 
     * @return the total count
     */
    int getTotalCount();
    
    /**
     * Gets the number of materials remaining to be iterated.
     * 
     * @return the remaining count
     */
    int getRemainingCount();
    
    /**
     * Checks if the iterator is at the beginning.
     * 
     * @return true if at the beginning
     */
    boolean isAtBeginning();
    
    /**
     * Checks if the iterator is at the end.
     * 
     * @return true if at the end
     */
    boolean isAtEnd();
}
