package com.university.bookstore.repository;

import java.util.List;
import java.util.Optional;

import com.university.bookstore.model.Material;

/**
 * Port interface for material persistence operations in hexagonal architecture.
 * Defines the contract for material storage without specifying implementation details.
 * 
 * <p>This interface represents the "port" in the ports and adapters pattern,
 * allowing the domain layer to interact with persistence without being coupled
 * to specific storage technologies (JSON, database, etc.).</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public interface MaterialRepository {
    
    /**
     * Saves a material to the repository.
     * If a material with the same ID already exists, it will be updated.
     * 
     * @param material the material to save
     * @throws RepositoryException if the save operation fails
     */
    void save(Material material);
    
    /**
     * Finds a material by its unique identifier.
     * 
     * @param id the material ID
     * @return the material if found, empty Optional otherwise
     * @throws RepositoryException if the find operation fails
     */
    Optional<Material> findById(String id);
    
    /**
     * Retrieves all materials from the repository.
     * 
     * @return list of all materials
     * @throws RepositoryException if the retrieval operation fails
     */
    List<Material> findAll();
    
    /**
     * Deletes a material by its ID.
     * 
     * @param id the material ID to delete
     * @return true if the material was deleted, false if not found
     * @throws RepositoryException if the delete operation fails
     */
    boolean delete(String id);
    
    /**
     * Checks if a material with the given ID exists.
     * 
     * @param id the material ID
     * @return true if the material exists, false otherwise
     * @throws RepositoryException if the check operation fails
     */
    boolean exists(String id);
    
    /**
     * Counts the total number of materials in the repository.
     * 
     * @return the number of materials
     * @throws RepositoryException if the count operation fails
     */
    long count();
    
    /**
     * Deletes all materials from the repository.
     * 
     * @throws RepositoryException if the clear operation fails
     */
    void deleteAll();
}
