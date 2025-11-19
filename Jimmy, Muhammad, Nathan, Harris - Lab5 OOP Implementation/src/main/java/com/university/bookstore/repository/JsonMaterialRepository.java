package com.university.bookstore.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.bookstore.model.Material;

/**
 * JSON-based implementation of MaterialRepository (Adapter in hexagonal architecture).
 * Persists materials to a JSON file using Jackson for serialization.
 * 
 * <p>This adapter implements the MaterialRepository port, providing JSON file-based
 * persistence without the domain layer knowing about the storage mechanism.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class JsonMaterialRepository implements MaterialRepository {
    
    private static final String SAFE_BASE_DIR = System.getProperty("user.dir") + "/data";
    private static final int MAX_PATH_LENGTH = 255;
    
    private final String filePath;
    private final ObjectMapper objectMapper;
    private final File dataFile;
    
    /**
     * Creates a new JSON material repository.
     * 
     * @param filePath the path to the JSON file for persistence
     */
    public JsonMaterialRepository(String filePath) {
        // Validate and sanitize the file path to prevent path traversal
        this.filePath = validateAndSanitizePath(filePath);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
        this.dataFile = new File(this.filePath);
        
        // Ensure the directory exists
        File parentDir = dataFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
    
    /**
     * Validates and sanitizes the file path to prevent path traversal attacks.
     * 
     * @param filePath the file path to validate
     * @return the validated and sanitized file path
     * @throws SecurityException if the path is invalid or attempts path traversal
     */
    private String validateAndSanitizePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        // Check for suspicious patterns BEFORE cleaning
        if (filePath.contains("../") || filePath.contains("..\\") || 
            filePath.contains("%2e%2e") || filePath.contains("%252e")) {
            throw new SecurityException("Invalid file path: potential path traversal detected");
        }
        
        // Remove any path traversal attempts
        String cleanPath = filePath.replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        
        // Validate path length
        if (cleanPath.length() > MAX_PATH_LENGTH) {
            throw new IllegalArgumentException("File path exceeds maximum length");
        }
        
        try {
            // Normalize the path and ensure it's within safe directory
            Path normalizedPath = Paths.get(cleanPath).normalize();
            Path safePath = Paths.get(SAFE_BASE_DIR).normalize();
            
            // If the path is not absolute, make it relative to safe directory
            if (!normalizedPath.isAbsolute()) {
                normalizedPath = safePath.resolve(normalizedPath).normalize();
            }
            
            // Ensure the normalized path is within the safe directory
            if (!normalizedPath.startsWith(safePath)) {
                throw new SecurityException("File path must be within the safe directory: " + SAFE_BASE_DIR);
            }
            
            return normalizedPath.toString();
        } catch (Exception e) {
            if (e instanceof SecurityException) {
                throw (SecurityException) e;
            }
            throw new IllegalArgumentException("Invalid file path: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void save(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        try {
            List<Material> materials = loadAll();
            
            // Remove existing material with same ID if it exists
            materials.removeIf(m -> m.getId().equals(material.getId()));
            
            // Add the new/updated material
            materials.add(material);
            
            // Save to file using wrapper to ensure polymorphic serialization
            MaterialsWrapper wrapper = new MaterialsWrapper(materials);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, wrapper);
            
        } catch (IOException e) {
            throw new RepositoryException("Failed to save material: " + material.getId(), e);
        }
    }
    
    @Override
    public Optional<Material> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            List<Material> materials = loadAll();
            return materials.stream()
                .filter(m -> id.equals(m.getId()))
                .findFirst();
                
        } catch (IOException e) {
            throw new RepositoryException("Failed to find material by ID: " + id, e);
        }
    }
    
    @Override
    public List<Material> findAll() {
        try {
            return loadAll();
        } catch (IOException e) {
            throw new RepositoryException("Failed to load all materials", e);
        }
    }
    
    @Override
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        try {
            List<Material> materials = loadAll();
            boolean removed = materials.removeIf(m -> id.equals(m.getId()));
            
            if (removed) {
                MaterialsWrapper wrapper = new MaterialsWrapper(materials);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, wrapper);
            }
            
            return removed;
            
        } catch (IOException e) {
            throw new RepositoryException("Failed to delete material: " + id, e);
        }
    }
    
    @Override
    public boolean exists(String id) {
        return findById(id).isPresent();
    }
    
    @Override
    public long count() {
        try {
            return loadAll().size();
        } catch (IOException e) {
            throw new RepositoryException("Failed to count materials", e);
        }
    }
    
    @Override
    public void deleteAll() {
        try {
            MaterialsWrapper wrapper = new MaterialsWrapper(new ArrayList<Material>());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, wrapper);
        } catch (IOException e) {
            throw new RepositoryException("Failed to clear all materials", e);
        }
    }
    
    /**
     * Loads all materials from the JSON file.
     * 
     * @return list of materials
     * @throws IOException if file reading fails
     */
    private List<Material> loadAll() throws IOException {
        if (!dataFile.exists()) {
            return new ArrayList<>();
        }
        
        if (dataFile.length() == 0) {
            return new ArrayList<>();
        }
        
        try {
            MaterialsWrapper wrapper = objectMapper.readValue(dataFile, MaterialsWrapper.class);
            return wrapper.getMaterials() != null ? wrapper.getMaterials() : new ArrayList<>();
        } catch (IOException e) {
            // Log the error for debugging
            System.err.println("Failed to load materials from " + dataFile + ": " + e.getMessage());
            // If JSON parsing fails, return empty list
            return new ArrayList<>();
        }
    }
    
    /**
     * Gets the file path used for persistence.
     * 
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * Checks if the data file exists.
     * 
     * @return true if the file exists
     */
    public boolean dataFileExists() {
        return dataFile.exists();
    }
    
    /**
     * Gets the size of the data file in bytes.
     * 
     * @return file size in bytes
     */
    public long getDataFileSize() {
        return dataFile.length();
    }
}
