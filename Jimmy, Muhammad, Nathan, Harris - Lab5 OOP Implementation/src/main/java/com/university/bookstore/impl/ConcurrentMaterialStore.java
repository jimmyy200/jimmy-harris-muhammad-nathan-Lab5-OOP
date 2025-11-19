package com.university.bookstore.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.university.bookstore.api.MaterialStore;
import com.university.bookstore.model.Magazine;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;
import com.university.bookstore.model.PrintedBook;

/**
 * Thread-safe implementation of MaterialStore using synchronization primitives.
 * Demonstrates concurrency patterns and thread safety in multi-threaded environments.
 * 
 * <p>This implementation uses ReentrantReadWriteLock to optimize for read-heavy workloads
 * and ConcurrentHashMap for thread-safe storage with minimal locking overhead.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class ConcurrentMaterialStore implements MaterialStore {
    
    private final Map<String, Material> materials;
    private final ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;
    
    /**
     * Creates a new thread-safe material store.
     */
    public ConcurrentMaterialStore() {
        this.materials = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }
    
    /**
     * Creates a material store with initial materials.
     * 
     * @param initialMaterials materials to add initially
     */
    public ConcurrentMaterialStore(Collection<Material> initialMaterials) {
        this();
        if (initialMaterials != null) {
            for (Material material : initialMaterials) {
                addMaterial(material);
            }
        }
    }
    
    @Override
    public boolean addMaterial(Material material) {
        if (material == null) {
            throw new NullPointerException("Cannot add null material");
        }
        
        writeLock.lock();
        try {
            return materials.putIfAbsent(material.getId(), material) == null;
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public Optional<Material> removeMaterial(String id) {
        if (id == null) {
            return Optional.empty();
        }
        
        writeLock.lock();
        try {
            return Optional.ofNullable(materials.remove(id));
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public Optional<Material> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        
        readLock.lock();
        try {
            return Optional.ofNullable(materials.get(id));
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = title.toLowerCase().trim();
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> searchByCreator(String creator) {
        if (creator == null || creator.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = creator.toLowerCase().trim();
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(m -> m.getCreator().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> getMaterialsByType(Material.MaterialType type) {
        if (type == null) {
            return new ArrayList<>();
        }
        
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(m -> m.getType() == type)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Media> getMediaMaterials() {
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(m -> m instanceof Media)
                .map(m -> (Media) m)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> filterMaterials(Predicate<Material> predicate) {
        if (predicate == null) {
            throw new NullPointerException("Predicate cannot be null");
        }
        
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> getMaterialsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            return new ArrayList<>();
        }
        
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(m -> m.getPrice() >= minPrice && m.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> getMaterialsByYear(int year) {
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(m -> m.getYear() == year)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> getAllMaterialsSorted() {
        readLock.lock();
        try {
            List<Material> sorted = new ArrayList<>(materials.values());
            Collections.sort(sorted);
            return sorted;
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> getAllMaterials() {
        readLock.lock();
        try {
            return new ArrayList<>(materials.values());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public double getTotalInventoryValue() {
        readLock.lock();
        try {
            return materials.values().stream()
                .mapToDouble(Material::getPrice)
                .sum();
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public double getTotalDiscountedValue() {
        readLock.lock();
        try {
            return materials.values().stream()
                .mapToDouble(Material::getDiscountedPrice)
                .sum();
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public InventoryStats getInventoryStats() {
        readLock.lock();
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
                .filter(m -> m instanceof Media)
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
            readLock.unlock();
        }
    }
    
    @Override
    public void clearInventory() {
        writeLock.lock();
        try {
            materials.clear();
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public int size() {
        readLock.lock();
        try {
            return materials.size();
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return materials.isEmpty();
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> findRecentMaterials(int years) {
        if (years < 0) {
            throw new IllegalArgumentException("Years cannot be negative: " + years);
        }
        
        int currentYear = java.time.Year.now().getValue();
        int cutoffYear = currentYear - years;
        
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(material -> material.getYear() >= cutoffYear)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> findByCreators(String... creators) {
        if (creators == null || creators.length == 0) {
            return new ArrayList<>();
        }
        
        Set<String> creatorSet = java.util.Arrays.stream(creators)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
        
        if (creatorSet.isEmpty()) {
            return new ArrayList<>();
        }
        
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(material -> creatorSet.contains(material.getCreator()))
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> findWithPredicate(Predicate<Material> condition) {
        if (condition == null) {
            throw new NullPointerException("Predicate cannot be null");
        }
        
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(condition)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public List<Material> getSorted(Comparator<Material> comparator) {
        if (comparator == null) {
            throw new NullPointerException("Comparator cannot be null");
        }
        
        readLock.lock();
        try {
            return materials.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Gets all display info for materials (thread-safe).
     * 
     * @return list of display strings
     */
    public List<String> getAllDisplayInfo() {
        readLock.lock();
        try {
            return materials.values().stream()
                .map(Material::getDisplayInfo)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Groups materials by type for reporting (thread-safe).
     * 
     * @return map of type to materials
     */
    public Map<Material.MaterialType, List<Material>> groupByType() {
        readLock.lock();
        try {
            return materials.values().stream()
                .collect(Collectors.groupingBy(Material::getType));
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Gets materials with active discounts (thread-safe).
     * 
     * @return list of discounted materials
     */
    public List<Material> getDiscountedMaterials() {
        readLock.lock();
        try {
            return materials.values().stream()
                .filter(m -> m.getDiscountRate() > 0)
                .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Calculates total savings from discounts (thread-safe).
     * 
     * @return total discount amount
     */
    public double getTotalDiscountAmount() {
        readLock.lock();
        try {
            return materials.values().stream()
                .mapToDouble(m -> m.getPrice() * m.getDiscountRate())
                .sum();
        } finally {
            readLock.unlock();
        }
    }
    
    @Override
    public String toString() {
        readLock.lock();
        try {
            return String.format("ConcurrentMaterialStore[Size=%d, Types=%d, Value=$%.2f]",
                size(),
                groupByType().size(),
                getTotalInventoryValue());
        } finally {
            readLock.unlock();
        }
    }
}
