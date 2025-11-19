package com.university.bookstore.repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.university.bookstore.model.Material;

/**
 * Modern JSON-based implementation of MaterialRepository using best practices.
 * Features:
 * - Try-with-resources for automatic resource management
 * - NIO.2 for better file operations
 * - Thread-safe file operations with ReadWriteLock
 * - Better error handling and logging
 * - Atomic file operations to prevent corruption
 * 
 * @author Navid Mohaghegh
 * @version 4.0
 * @since 2024-09-15
 */
public class ModernJsonMaterialRepository implements MaterialRepository, AutoCloseable {
    
    private static final Logger LOGGER = Logger.getLogger(ModernJsonMaterialRepository.class.getName());
    private static final String SAFE_BASE_DIR = System.getProperty("user.dir") + "/data";
    private static final int MAX_PATH_LENGTH = 255;
    
    private final Path dataFile;
    private final Path backupFile;
    private final ObjectMapper objectMapper;
    private final ReadWriteLock fileLock;
    private volatile boolean closed = false;
    
    /**
     * Creates a new modern JSON material repository.
     * 
     * @param filePath the path to the JSON file for persistence
     */
    public ModernJsonMaterialRepository(String filePath) {
        Path validatedPath = validateAndSanitizePath(filePath);
        this.dataFile = validatedPath;
        this.backupFile = Paths.get(validatedPath.toString() + ".backup");
        this.fileLock = new ReentrantReadWriteLock();
        
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.findAndRegisterModules(); // For Java 8 time support
        
        initializeStorage();
    }
    
    private void initializeStorage() {
        try {
            // Ensure the directory exists
            Path parentDir = dataFile.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
                LOGGER.info("Created directory: " + parentDir);
            }
            
            // Create empty file if it doesn't exist
            if (!Files.exists(dataFile)) {
                saveAtomic(new ArrayList<>());
                LOGGER.info("Created new data file: " + dataFile);
            }
        } catch (IOException e) {
            throw new RepositoryException("Failed to initialize storage", e);
        }
    }
    
    /**
     * Validates and sanitizes the file path to prevent path traversal attacks.
     * 
     * @param filePath the file path to validate
     * @return the validated and sanitized Path
     * @throws SecurityException if the path is invalid or attempts path traversal
     */
    private Path validateAndSanitizePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        // Remove any path traversal attempts
        String cleanPath = filePath.replaceAll("\\.\\./", "").replaceAll("\\.\\.", "");
        
        // Check for suspicious patterns
        if (cleanPath.contains("../") || cleanPath.contains("..\\") || 
            cleanPath.contains("%2e%2e") || cleanPath.contains("%252e")) {
            throw new SecurityException("Invalid file path: potential path traversal detected");
        }
        
        // Validate path length
        if (cleanPath.length() > MAX_PATH_LENGTH) {
            throw new IllegalArgumentException("File path exceeds maximum length");
        }
        
        try {
            // Normalize the path
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
            
            return normalizedPath;
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
        ensureNotClosed();
        
        fileLock.writeLock().lock();
        try {
            List<Material> materials = loadAllInternal();
            
            // Remove existing material with same ID if it exists
            materials.removeIf(m -> m.getId().equals(material.getId()));
            
            // Add the new/updated material
            materials.add(material);
            
            // Save atomically using wrapper
            saveAtomic(materials);
            
            LOGGER.fine("Saved material: " + material.getId());
        } catch (IOException e) {
            throw new RepositoryException("Failed to save material: " + material.getId(), e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }
    
    /**
     * Saves multiple materials in batch for better performance.
     * 
     * @param materialsToSave collection of materials to save
     */
    public void saveAll(Collection<Material> materialsToSave) {
        Objects.requireNonNull(materialsToSave, "Materials collection cannot be null");
        ensureNotClosed();
        
        if (materialsToSave.isEmpty()) {
            return;
        }
        
        fileLock.writeLock().lock();
        try {
            List<Material> materials = loadAllInternal();
            
            // Create a map for efficient lookup
            Map<String, Material> materialMap = new HashMap<>();
            for (Material m : materials) {
                materialMap.put(m.getId(), m);
            }
            
            // Update or add new materials
            for (Material material : materialsToSave) {
                if (material != null) {
                    materialMap.put(material.getId(), material);
                }
            }
            
            // Save atomically
            saveAtomic(new ArrayList<>(materialMap.values()));
            
            LOGGER.fine("Saved " + materialsToSave.size() + " materials in batch");
        } catch (IOException e) {
            throw new RepositoryException("Failed to save materials batch", e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }
    
    @Override
    public Optional<Material> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        ensureNotClosed();
        
        fileLock.readLock().lock();
        try {
            List<Material> materials = loadAllInternal();
            return materials.stream()
                .filter(m -> id.equals(m.getId()))
                .findFirst();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to find material by ID: " + id, e);
            return Optional.empty();
        } finally {
            fileLock.readLock().unlock();
        }
    }
    
    @Override
    public List<Material> findAll() {
        ensureNotClosed();
        
        fileLock.readLock().lock();
        try {
            return new ArrayList<>(loadAllInternal());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to load all materials", e);
            return new ArrayList<>();
        } finally {
            fileLock.readLock().unlock();
        }
    }
    
    @Override
    public boolean delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        ensureNotClosed();
        
        fileLock.writeLock().lock();
        try {
            List<Material> materials = loadAllInternal();
            boolean removed = materials.removeIf(m -> id.equals(m.getId()));
            
            if (removed) {
                saveAtomic(materials);
                LOGGER.fine("Deleted material: " + id);
            }
            
            return removed;
        } catch (IOException e) {
            throw new RepositoryException("Failed to delete material: " + id, e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }
    
    /**
     * Deletes multiple materials in batch.
     * 
     * @param ids collection of IDs to delete
     * @return number of materials deleted
     */
    public int deleteAll(Collection<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        ensureNotClosed();
        
        fileLock.writeLock().lock();
        try {
            List<Material> materials = loadAllInternal();
            Set<String> idSet = new HashSet<>(ids);
            int originalSize = materials.size();
            
            materials.removeIf(m -> idSet.contains(m.getId()));
            int deleted = originalSize - materials.size();
            
            if (deleted > 0) {
                saveAtomic(materials);
                LOGGER.fine("Deleted " + deleted + " materials in batch");
            }
            
            return deleted;
        } catch (IOException e) {
            throw new RepositoryException("Failed to delete materials batch", e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean exists(String id) {
        return findById(id).isPresent();
    }
    
    @Override
    public long count() {
        ensureNotClosed();
        
        fileLock.readLock().lock();
        try {
            return loadAllInternal().size();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to count materials", e);
            return 0;
        } finally {
            fileLock.readLock().unlock();
        }
    }
    
    @Override
    public void deleteAll() {
        ensureNotClosed();
        
        fileLock.writeLock().lock();
        try {
            saveAtomic(new ArrayList<>());
            LOGGER.info("Cleared all materials from repository");
        } catch (IOException e) {
            throw new RepositoryException("Failed to clear all materials", e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }
    
    /**
     * Loads all materials from the JSON file using try-with-resources.
     * 
     * @return list of materials
     * @throws IOException if file reading fails
     */
    private List<Material> loadAllInternal() throws IOException {
        if (!Files.exists(dataFile) || Files.size(dataFile) == 0) {
            return new ArrayList<>();
        }
        
        // Use try-with-resources for automatic resource management
        try (BufferedReader reader = Files.newBufferedReader(dataFile)) {
            MaterialsWrapper wrapper = objectMapper.readValue(reader, MaterialsWrapper.class);
            return wrapper.getMaterials() != null ? wrapper.getMaterials() : new ArrayList<>();
        } catch (IOException e) {
            // Try to restore from backup if main file is corrupted
            if (Files.exists(backupFile)) {
                LOGGER.warning("Main file corrupted, attempting to restore from backup");
                try (BufferedReader backupReader = Files.newBufferedReader(backupFile)) {
                    MaterialsWrapper wrapper = objectMapper.readValue(backupReader, MaterialsWrapper.class);
                    // Restore the main file from backup
                    Files.copy(backupFile, dataFile, 
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    return wrapper.getMaterials() != null ? wrapper.getMaterials() : new ArrayList<>();
                } catch (IOException backupError) {
                    LOGGER.log(Level.SEVERE, "Failed to restore from backup", backupError);
                    return new ArrayList<>();
                }
            }
            LOGGER.log(Level.WARNING, "Failed to parse JSON, returning empty list", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Saves materials atomically to prevent corruption.
     * 
     * @param materials list of materials to save
     * @throws IOException if saving fails
     */
    private void saveAtomic(List<Material> materials) throws IOException {
        // Create backup of current file if it exists
        if (Files.exists(dataFile)) {
            Files.copy(dataFile, backupFile, 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Write to temporary file first
        Path tempFile = Files.createTempFile(dataFile.getParent(), "temp", ".json");
        
        try {
            // Use try-with-resources for writing with wrapper
            try (BufferedWriter writer = Files.newBufferedWriter(tempFile, 
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                MaterialsWrapper wrapper = new MaterialsWrapper(materials);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, wrapper);
            }
            
            // Atomic move (rename) - this is atomic on most file systems
            Files.move(tempFile, dataFile, 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            
        } catch (IOException e) {
            // Clean up temp file if operation failed
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException deleteError) {
                LOGGER.log(Level.WARNING, "Failed to delete temp file", deleteError);
            }
            throw e;
        }
    }
    
    /**
     * Creates a backup of the current data file.
     * 
     * @return true if backup was created successfully
     */
    public boolean createBackup() {
        ensureNotClosed();
        
        fileLock.readLock().lock();
        try {
            if (Files.exists(dataFile)) {
                Path backupPath = Paths.get(dataFile + "." + System.currentTimeMillis() + ".backup");
                Files.copy(dataFile, backupPath);
                LOGGER.info("Created backup: " + backupPath);
                return true;
            }
            return false;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to create backup", e);
            return false;
        } finally {
            fileLock.readLock().unlock();
        }
    }
    
    /**
     * Restores data from a backup file.
     * 
     * @param backupPath path to the backup file
     * @return true if restore was successful
     */
    public boolean restoreFromBackup(String backupPath) {
        Objects.requireNonNull(backupPath, "Backup path cannot be null");
        ensureNotClosed();
        
        Path backup = Paths.get(backupPath);
        if (!Files.exists(backup)) {
            LOGGER.warning("Backup file does not exist: " + backupPath);
            return false;
        }
        
        fileLock.writeLock().lock();
        try {
            // Validate backup file can be read
            MaterialsWrapper wrapper;
            try (BufferedReader reader = Files.newBufferedReader(backup)) {
                wrapper = objectMapper.readValue(reader, MaterialsWrapper.class);
            }
            
            // If validation successful, replace current file
            Files.copy(backup, dataFile, 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            
            LOGGER.info("Restored from backup: " + backupPath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to restore from backup: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            fileLock.writeLock().unlock();
        }
    }
    
    /**
     * Gets the file path used for persistence.
     * 
     * @return the file path
     */
    public String getFilePath() {
        return dataFile.toString();
    }
    
    /**
     * Checks if the data file exists.
     * 
     * @return true if the file exists
     */
    public boolean dataFileExists() {
        return Files.exists(dataFile);
    }
    
    /**
     * Gets the size of the data file in bytes.
     * 
     * @return file size in bytes
     */
    public long getDataFileSize() {
        try {
            return Files.size(dataFile);
        } catch (IOException e) {
            return 0;
        }
    }
    
    /**
     * Performs maintenance operations like cleanup and optimization.
     */
    public void performMaintenance() {
        ensureNotClosed();
        
        fileLock.writeLock().lock();
        try {
            // Clean up old backup files
            Path parent = dataFile.getParent();
            if (parent != null) {
                long cutoffTime = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000); // 7 days
                
                Files.list(parent)
                    .filter(path -> path.toString().endsWith(".backup"))
                    .filter(path -> {
                        try {
                            return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                            LOGGER.fine("Deleted old backup: " + path);
                        } catch (IOException e) {
                            LOGGER.warning("Failed to delete old backup: " + path);
                        }
                    });
            }
            
            // Compact the JSON file by rewriting it
            List<Material> materials = loadAllInternal();
            saveAtomic(materials);
            
            LOGGER.info("Maintenance completed");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Maintenance failed", e);
        } finally {
            fileLock.writeLock().unlock();
        }
    }
    
    private void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException("Repository has been closed");
        }
    }
    
    @Override
    public void close() {
        if (!closed) {
            closed = true;
            LOGGER.info("Repository closed");
        }
    }
}