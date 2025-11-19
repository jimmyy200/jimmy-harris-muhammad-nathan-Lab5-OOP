package com.university.bookstore.impl;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.university.bookstore.api.MaterialStore;
import com.university.bookstore.model.Magazine;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;
import com.university.bookstore.model.PrintedBook;

/**
 * Modern thread-safe implementation of MaterialStore using best practices.
 * Features:
 * - StampedLock for optimized read performance
 * - ExecutorService for async operations
 * - CompletableFuture for non-blocking operations
 * - Proper resource management with AutoCloseable
 * - Virtual thread support (when available)
 * 
 * @author Navid Mohaghegh
 * @version 4.0
 * @since 2024-09-15
 */
public class ModernConcurrentMaterialStore implements MaterialStore, AutoCloseable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ModernConcurrentMaterialStore.class);
    
    private final Map<String, Material> materials;
    private final StampedLock stampedLock;
    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutor;
    private volatile boolean closed = false;
    
    /**
     * Creates a new modern thread-safe material store.
     */
    public ModernConcurrentMaterialStore() {
        this.materials = new ConcurrentHashMap<>();
        this.stampedLock = new StampedLock();
        
        // Use ForkJoinPool for better work-stealing behavior
        this.executorService = new ForkJoinPool(
            Runtime.getRuntime().availableProcessors(),
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null, 
            true // Enable async mode for better throughput
        );
        
        this.scheduledExecutor = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "MaterialStore-Scheduler");
            t.setDaemon(true);
            return t;
        });
        
        // Schedule periodic cleanup tasks
        scheduleMaintenanceTasks();
    }
    
    /**
     * Creates a material store with initial materials.
     * 
     * @param initialMaterials materials to add initially
     */
    public ModernConcurrentMaterialStore(Collection<Material> initialMaterials) {
        this();
        if (initialMaterials != null) {
            // Parallel addition for better performance
            initialMaterials.parallelStream().forEach(this::addMaterial);
        }
    }
    
    private void scheduleMaintenanceTasks() {
        // Example: periodic cache cleanup or metrics collection
        scheduledExecutor.scheduleAtFixedRate(
            this::performMaintenance, 
            1, 1, TimeUnit.HOURS
        );
    }
    
    private void performMaintenance() {
        if (!closed) {
            // Maintenance tasks like clearing old data, collecting metrics, etc.
            // This is a placeholder for actual maintenance logic
        }
    }
    
    @Override
    public boolean addMaterial(Material material) {
        Objects.requireNonNull(material, "Material cannot be null");
        ensureNotClosed();
        
        long stamp = stampedLock.writeLock();
        try {
            return materials.putIfAbsent(material.getId(), material) == null;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
    
    /**
     * Adds material asynchronously.
     * 
     * @param material the material to add
     * @return CompletableFuture with the result
     */
    public CompletableFuture<Boolean> addMaterialAsync(Material material) {
        return CompletableFuture.supplyAsync(
            () -> addMaterial(material), 
            executorService
        );
    }
    
    /**
     * Adds multiple materials in batch asynchronously.
     * 
     * @param materials collection of materials to add
     * @return CompletableFuture with results map
     */
    public CompletableFuture<Map<String, Boolean>> addMaterialsBatchAsync(Collection<Material> materials) {
        List<CompletableFuture<Map.Entry<String, Boolean>>> futures = materials.stream()
            .map(material -> CompletableFuture.supplyAsync(
                () -> Map.entry(material.getId(), addMaterial(material)),
                executorService
            ))
            .toList();
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue
                )));
    }
    
    @Override
    public Optional<Material> removeMaterial(String id) {
        if (id == null) {
            return Optional.empty();
        }
        ensureNotClosed();
        
        long stamp = stampedLock.writeLock();
        try {
            return Optional.ofNullable(materials.remove(id));
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
    
    @Override
    public Optional<Material> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        ensureNotClosed();
        
        // Try optimistic read first for better performance
        long stamp = stampedLock.tryOptimisticRead();
        Material material = materials.get(id);
        
        if (!stampedLock.validate(stamp)) {
            // Optimistic read failed, acquire read lock
            stamp = stampedLock.readLock();
            try {
                material = materials.get(id);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        
        return Optional.ofNullable(material);
    }
    
    /**
     * Finds material by ID asynchronously.
     * 
     * @param id the material ID
     * @return CompletableFuture with the result
     */
    public CompletableFuture<Optional<Material>> findByIdAsync(String id) {
        return CompletableFuture.supplyAsync(
            () -> findById(id), 
            executorService
        );
    }
    
    /**
     * Finds multiple materials by IDs asynchronously.
     * 
     * @param ids list of material IDs
     * @return CompletableFuture with results map
     */
    public CompletableFuture<Map<String, Material>> findByIdsAsync(List<String> ids) {
        List<CompletableFuture<Map.Entry<String, Optional<Material>>>> futures = ids.stream()
            .map(id -> CompletableFuture.supplyAsync(
                () -> Map.entry(id, findById(id)),
                executorService
            ))
            .toList();
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .filter(entry -> entry.getValue().isPresent())
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().get()
                )));
    }
    
    @Override
    public List<Material> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>();
        }
        ensureNotClosed();
        
        String searchTerm = title.toLowerCase().trim();
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(m -> m.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    /**
     * Searches by title asynchronously.
     * 
     * @param title the title to search for
     * @return CompletableFuture with the results
     */
    public CompletableFuture<List<Material>> searchByTitleAsync(String title) {
        return CompletableFuture.supplyAsync(
            () -> searchByTitle(title), 
            executorService
        );
    }
    
    @Override
    public List<Material> searchByCreator(String creator) {
        if (creator == null || creator.trim().isEmpty()) {
            return new ArrayList<>();
        }
        ensureNotClosed();
        
        String searchTerm = creator.toLowerCase().trim();
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(m -> m.getCreator().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> getMaterialsByType(Material.MaterialType type) {
        if (type == null) {
            return new ArrayList<>();
        }
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(m -> m.getType() == type)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Media> getMediaMaterials() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(Media.class::isInstance)
                .map(Media.class::cast)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> filterMaterials(Predicate<Material> predicate) {
        Objects.requireNonNull(predicate, "Predicate cannot be null");
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(predicate)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> getMaterialsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            return new ArrayList<>();
        }
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(m -> m.getPrice() >= minPrice && m.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> getMaterialsByYear(int year) {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(m -> m.getYear() == year)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> getAllMaterialsSorted() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().stream()
                .sorted()
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> getAllMaterials() {
        ensureNotClosed();
        
        // Use optimistic read for better performance
        long stamp = stampedLock.tryOptimisticRead();
        List<Material> result = new ArrayList<>(materials.values());
        
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                result = new ArrayList<>(materials.values());
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        
        return result;
    }
    
    @Override
    public double getTotalInventoryValue() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .mapToDouble(Material::getPrice)
                .sum();
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    /**
     * Gets total inventory value asynchronously.
     * 
     * @return CompletableFuture with the total value
     */
    public CompletableFuture<Double> getTotalInventoryValueAsync() {
        return CompletableFuture.supplyAsync(
            this::getTotalInventoryValue, 
            executorService
        );
    }
    
    @Override
    public double getTotalDiscountedValue() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .mapToDouble(Material::getDiscountedPrice)
                .sum();
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public InventoryStats getInventoryStats() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            if (materials.isEmpty()) {
                return new InventoryStats(0, 0, 0, 0, 0, 0);
            }
            
            List<Double> prices = materials.values().stream()
                .map(Material::getPrice)
                .sorted()
                .collect(Collectors.toList());
            
            double averagePrice = prices.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
            
            double medianPrice = prices.size() % 2 == 0
                ? (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2
                : prices.get(prices.size() / 2);
            
            int uniqueTypes = (int) materials.values().stream()
                .map(Material::getType)
                .distinct()
                .count();
            
            int mediaCount = (int) materials.values().stream()
                .filter(Media.class::isInstance)
                .count();
            
            int printCount = (int) materials.values().stream()
                .filter(m -> m instanceof PrintedBook || m instanceof Magazine)
                .count();
            
            return new InventoryStats(
                materials.size(),
                averagePrice,
                medianPrice,
                uniqueTypes,
                mediaCount,
                printCount
            );
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    /**
     * Gets inventory statistics asynchronously.
     * 
     * @return CompletableFuture with the statistics
     */
    public CompletableFuture<InventoryStats> getInventoryStatsAsync() {
        return CompletableFuture.supplyAsync(
            this::getInventoryStats, 
            executorService
        );
    }
    
    @Override
    public void clearInventory() {
        ensureNotClosed();
        
        long stamp = stampedLock.writeLock();
        try {
            materials.clear();
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
    
    @Override
    public int size() {
        ensureNotClosed();
        
        // Use optimistic read for size check
        long stamp = stampedLock.tryOptimisticRead();
        int size = materials.size();
        
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                size = materials.size();
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public List<Material> findRecentMaterials(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Years cannot be negative: " + years);
        }
        ensureNotClosed();
        
        int currentYear = java.time.Year.now().getValue();
        int cutoffYear = currentYear - years;
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(material -> material.getYear() >= cutoffYear)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> findByCreators(String... creators) {
        if (creators == null || creators.length == 0) {
            return new ArrayList<>();
        }
        ensureNotClosed();
        
        Set<String> creatorSet = Arrays.stream(creators)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
        
        if (creatorSet.isEmpty()) {
            return new ArrayList<>();
        }
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(material -> creatorSet.contains(material.getCreator()))
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> findWithPredicate(Predicate<Material> condition) {
        Objects.requireNonNull(condition, "Predicate cannot be null");
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(condition)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    @Override
    public List<Material> getSorted(Comparator<Material> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    /**
     * Performs parallel search across multiple criteria.
     * 
     * @param title optional title search term
     * @param creator optional creator search term
     * @param type optional material type
     * @return CompletableFuture with combined results
     */
    public CompletableFuture<List<Material>> parallelSearchAsync(
            String title, String creator, Material.MaterialType type) {
        
        List<CompletableFuture<List<Material>>> searches = new ArrayList<>();
        
        if (title != null && !title.trim().isEmpty()) {
            searches.add(searchByTitleAsync(title));
        }
        if (creator != null && !creator.trim().isEmpty()) {
            searches.add(CompletableFuture.supplyAsync(
                () -> searchByCreator(creator), executorService));
        }
        if (type != null) {
            searches.add(CompletableFuture.supplyAsync(
                () -> getMaterialsByType(type), executorService));
        }
        
        if (searches.isEmpty()) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        
        return CompletableFuture.allOf(searches.toArray(new CompletableFuture[0]))
            .thenApply(v -> searches.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList()));
    }
    
    /**
     * Groups materials by type for reporting.
     * 
     * @return map of type to materials
     */
    public Map<Material.MaterialType, List<Material>> groupByType() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().stream()
                .collect(Collectors.groupingBy(Material::getType));
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    /**
     * Gets materials with active discounts.
     * 
     * @return list of discounted materials
     */
    public List<Material> getDiscountedMaterials() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .filter(m -> m.getDiscountRate() > 0)
                .collect(Collectors.toList());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    /**
     * Calculates total savings from discounts.
     * 
     * @return total discount amount
     */
    public double getTotalDiscountAmount() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return materials.values().parallelStream()
                .mapToDouble(m -> m.getPrice() * m.getDiscountRate())
                .sum();
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
    
    private void ensureNotClosed() {
        if (closed) {
            throw new IllegalStateException("MaterialStore has been closed");
        }
    }
    
    @Override
    public void close() {
        if (!closed) {
            closed = true;
            
            // Shutdown executors gracefully
            executorService.shutdown();
            scheduledExecutor.shutdown();
            
            try {
                // Wait for existing tasks to complete
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        LOGGER.error("ExecutorService did not terminate within timeout");
                    }
                }
                
                if (!scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduledExecutor.shutdownNow();
                    if (!scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                        LOGGER.error("ScheduledExecutor did not terminate within timeout");
                    }
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                scheduledExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    @Override
    public String toString() {
        ensureNotClosed();
        
        long stamp = stampedLock.readLock();
        try {
            return String.format("ModernConcurrentMaterialStore[Size=%d, Types=%d, Value=$%.2f]",
                size(),
                groupByType().size(),
                getTotalInventoryValue());
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }
}