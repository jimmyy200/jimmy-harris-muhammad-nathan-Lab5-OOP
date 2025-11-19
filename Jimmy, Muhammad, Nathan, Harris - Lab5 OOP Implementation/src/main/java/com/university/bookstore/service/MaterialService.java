package com.university.bookstore.service;

import java.util.List;
import java.util.Optional;

import com.university.bookstore.model.Material;
import com.university.bookstore.repository.MaterialRepository;

/**
 * Domain service that orchestrates business logic for material management.
 * Demonstrates hexagonal architecture by coordinating between domain logic and infrastructure.
 * 
 * <p>This service encapsulates business rules and coordinates between the domain layer
 * and the repository layer, maintaining clean separation of concerns.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialService {
    
    private final MaterialRepository repository;
    
    /**
     * Creates a new material service with the specified repository.
     * 
     * @param repository the material repository to use
     */
    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Adds a material to the system with business logic validation.
     * 
     * @param material the material to add
     * @throws InvalidMaterialException if the material is invalid
     */
    public void addMaterial(Material material) {
        validateMaterial(material);
        repository.save(material);
    }
    
    /**
     * Updates an existing material with business logic validation.
     * 
     * @param material the material to update
     * @throws InvalidMaterialException if the material is invalid
     * @throws MaterialNotFoundException if the material doesn't exist
     */
    public void updateMaterial(Material material) {
        validateMaterial(material);
        
        if (!repository.exists(material.getId())) {
            throw new MaterialNotFoundException("Material not found: " + material.getId());
        }
        
        repository.save(material);
    }
    
    /**
     * Finds a material by its ID.
     * 
     * @param id the material ID
     * @return the material if found
     * @throws MaterialNotFoundException if the material doesn't exist
     */
    public Material findMaterial(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new MaterialNotFoundException("Material not found: " + id));
    }
    
    /**
     * Finds a material by its ID, returning Optional.
     * 
     * @param id the material ID
     * @return Optional containing the material if found
     */
    public Optional<Material> findMaterialOptional(String id) {
        return repository.findById(id);
    }
    
    /**
     * Gets all materials in the system.
     * 
     * @return list of all materials
     */
    public List<Material> getAllMaterials() {
        return repository.findAll();
    }
    
    /**
     * Removes a material from the system.
     * 
     * @param id the material ID to remove
     * @return true if the material was removed, false if not found
     */
    public boolean removeMaterial(String id) {
        return repository.delete(id);
    }
    
    /**
     * Checks if a material exists in the system.
     * 
     * @param id the material ID
     * @return true if the material exists
     */
    public boolean materialExists(String id) {
        return repository.exists(id);
    }
    
    /**
     * Gets the total number of materials in the system.
     * 
     * @return the material count
     */
    public long getMaterialCount() {
        return repository.count();
    }
    
    /**
     * Clears all materials from the system.
     */
    public void clearAllMaterials() {
        repository.deleteAll();
    }
    
    /**
     * Validates a material according to business rules.
     * 
     * @param material the material to validate
     * @throws InvalidMaterialException if validation fails
     */
    private void validateMaterial(Material material) {
        if (material == null) {
            throw new InvalidMaterialException("Material cannot be null");
        }
        
        if (material.getId() == null || material.getId().trim().isEmpty()) {
            throw new InvalidMaterialException("Material ID cannot be null or empty");
        }
        
        if (material.getTitle() == null || material.getTitle().trim().isEmpty()) {
            throw new InvalidMaterialException("Material title cannot be null or empty");
        }
        
        if (material.getCreator() == null || material.getCreator().trim().isEmpty()) {
            throw new InvalidMaterialException("Material creator cannot be null or empty");
        }
        
        if (material.getPrice() < 0) {
            throw new InvalidMaterialException("Material price cannot be negative: " + material.getPrice());
        }
        
        if (material.getYear() < 1000 || material.getYear() > 2100) {
            throw new InvalidMaterialException("Material year must be between 1000 and 2100: " + material.getYear());
        }
    }
    
    /**
     * Exception thrown when a material is invalid according to business rules.
     */
    public static class InvalidMaterialException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        
        public InvalidMaterialException(String message) {
            super(message);
        }
        
        public InvalidMaterialException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Exception thrown when a requested material is not found.
     */
    public static class MaterialNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        
        public MaterialNotFoundException(String message) {
            super(message);
        }
        
        public MaterialNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
