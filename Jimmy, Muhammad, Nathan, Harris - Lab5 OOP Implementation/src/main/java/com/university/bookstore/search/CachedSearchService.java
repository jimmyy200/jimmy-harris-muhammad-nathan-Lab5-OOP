package com.university.bookstore.search;

import java.util.List;
import java.util.Objects;

import com.university.bookstore.model.Material;
import com.university.bookstore.repository.MaterialRepository;

/**
 * Service that integrates Trie-based prefix search with LRU caching for optimal performance.
 * Provides fast prefix-based material searching with intelligent result caching.
 * 
 * <p>This service combines the efficiency of Trie data structures for prefix searching
 * with LRU caching to avoid repeated computation for frequently accessed queries.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class CachedSearchService {
    
    private final MaterialTrie trie;
    private final SearchResultCache cache;
    private final MaterialRepository repository;
    
    /**
     * Creates a new cached search service.
     * 
     * @param repository the material repository to search
     * @param cacheSize the maximum number of cached search results
     */
    public CachedSearchService(MaterialRepository repository, int cacheSize) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.trie = new MaterialTrie();
        this.cache = new SearchResultCache(cacheSize);
        initializeTrie();
    }
    
    /**
     * Searches for materials by title prefix with caching.
     * 
     * @param prefix the title prefix to search for
     * @return list of materials matching the prefix
     */
    public List<Material> searchByPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }
        
        String cacheKey = "prefix:" + prefix.toLowerCase().trim();
        
        // Check cache first
        var cached = cache.get(cacheKey);
        if (cached.isPresent()) {
            return cached.get();
        }
        
        // Perform search using trie
        List<Material> results = trie.searchByPrefix(prefix);
        
        // Cache results
        cache.put(cacheKey, results);
        
        return results;
    }
    
    /**
     * Searches for materials by title prefix with result limit and caching.
     * 
     * @param prefix the title prefix to search for
     * @param limit the maximum number of results to return
     * @return list of materials matching the prefix, limited to the specified count
     */
    public List<Material> searchByPrefixWithLimit(String prefix, int limit) {
        if (prefix == null || prefix.trim().isEmpty() || limit <= 0) {
            return List.of();
        }
        
        String cacheKey = "prefix:" + prefix.toLowerCase().trim() + ":limit:" + limit;
        
        // Check cache first
        var cached = cache.get(cacheKey);
        if (cached.isPresent()) {
            return cached.get();
        }
        
        // Perform search using trie
        List<Material> results = trie.searchByPrefixWithLimit(prefix, limit);
        
        // Cache results
        cache.put(cacheKey, results);
        
        return results;
    }
    
    /**
     * Adds a material to the search index and invalidates relevant cache entries.
     * 
     * @param material the material to add
     */
    public void addMaterial(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        trie.insert(material);
        invalidateCacheForMaterial(material);
    }
    
    /**
     * Removes a material from the search index and invalidates relevant cache entries.
     * 
     * @param material the material to remove
     */
    public void removeMaterial(Material material) {
        if (material == null) {
            return;
        }
        
        trie.remove(material);
        invalidateCacheForMaterial(material);
    }
    
    /**
     * Refreshes the search index from the repository.
     * Clears the cache to ensure consistency.
     */
    public void refreshIndex() {
        trie.clear();
        cache.clear();
        initializeTrie();
    }
    
    /**
     * Gets cache statistics for performance monitoring.
     * 
     * @return cache statistics
     */
    public SearchResultCache.CacheStats getCacheStats() {
        return cache.getStats();
    }
    
    /**
     * Gets the current size of the search index.
     * 
     * @return the number of materials in the index
     */
    public int getIndexSize() {
        return trie.size();
    }
    
    /**
     * Checks if the search index is empty.
     * 
     * @return true if no materials are indexed
     */
    public boolean isIndexEmpty() {
        return trie.isEmpty();
    }
    
    /**
     * Clears the search index and cache.
     */
    public void clear() {
        trie.clear();
        cache.clear();
    }
    
    /**
     * Initializes the trie with all materials from the repository.
     */
    private void initializeTrie() {
        List<Material> materials = repository.findAll();
        for (Material material : materials) {
            trie.insert(material);
        }
    }
    
    /**
     * Invalidates cache entries that might be affected by changes to a material.
     * 
     * @param material the material that was changed
     */
    private void invalidateCacheForMaterial(Material material) {
        String title = material.getTitle().toLowerCase();
        
        // Invalidate cache entries for all possible prefixes of the material's title
        for (int i = 1; i <= title.length(); i++) {
            String prefix = title.substring(0, i);
            String cacheKey = "prefix:" + prefix;
            cache.remove(cacheKey);
            
            // Also remove limit-based cache entries
            for (int limit = 10; limit <= 100; limit += 10) {
                String limitCacheKey = cacheKey + ":limit:" + limit;
                cache.remove(limitCacheKey);
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("CachedSearchService[IndexSize=%d, CacheSize=%d/%d, HitRatio=%.2f%%]",
            getIndexSize(),
            cache.size(),
            cache.getMaxSize(),
            getCacheStats().getHitRatio() * 100);
    }
}
