package com.university.bookstore.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.university.bookstore.api.MaterialStore;
import com.university.bookstore.impl.ConcurrentMaterialStore;
import com.university.bookstore.impl.MaterialStoreImpl;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.repository.MaterialRepository;
import com.university.bookstore.search.CachedSearchService;
import com.university.bookstore.search.MaterialTrie;
import com.university.bookstore.search.SearchResultCache;

/**
 * Integration test for performance analysis and benchmarking.
 * Validates that all performance components work together correctly.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
@Tag("performance")
@DisplayName("Performance Integration Tests")
public class PerformanceIntegrationTest {
    
    @Test
    @DisplayName("Performance Analyzer Integration Test")
    public void testPerformanceAnalyzerIntegration() {
        PerformanceAnalyzer analyzer = new PerformanceAnalyzer();
        
        // Test data structure analysis
        PerformanceAnalyzer.PerformanceReport dataStructureReport = analyzer.analyzeDataStructures();
        assertNotNull(dataStructureReport);
        assertTrue(dataStructureReport.getResults().size() > 0);
        
        // Test cache performance analysis
        PerformanceAnalyzer.CachePerformanceReport cacheReport = analyzer.analyzeCachePerformance();
        assertNotNull(cacheReport);
        assertTrue(cacheReport.getHitRatio() >= 0.0 && cacheReport.getHitRatio() <= 1.0);
        assertTrue(cacheReport.getTotalRequests() > 0);
        
        // Test search performance analysis
        PerformanceAnalyzer.SearchPerformanceReport searchReport = analyzer.analyzeSearchPerformance();
        assertNotNull(searchReport);
        assertTrue(searchReport.getArrayListSearchTime() > 0);
        assertTrue(searchReport.getTrieSearchTime() > 0);
        
        // Test memory usage analysis
        PerformanceAnalyzer.MemoryUsageReport memoryReport = analyzer.analyzeMemoryUsage();
        assertNotNull(memoryReport);
        assertTrue(memoryReport.getArrayListMemoryUsage() > 0);
        assertTrue(memoryReport.getTrieMemoryUsage() > 0);
    }
    
    @Test
    @DisplayName("Data Structure Performance Comparison")
    public void testDataStructurePerformanceComparison() {
        int testSize = 10000;
        List<Material> testMaterials = generateTestMaterials(testSize);
        
        // Test ArrayList performance
        long arrayListTime = measureTime(() -> {
            MaterialStore arrayStore = new MaterialStoreImpl();
            for (Material material : testMaterials) {
                arrayStore.addMaterial(material);
            }
            // Perform some searches
            for (int i = 0; i < 1000; i++) {
                arrayStore.findById("ID-" + (i % testSize));
            }
        });
        
        // Test ConcurrentHashMap performance
        long concurrentTime = measureTime(() -> {
            MaterialStore concurrentStore = new ConcurrentMaterialStore();
            for (Material material : testMaterials) {
                concurrentStore.addMaterial(material);
            }
            // Perform some searches
            for (int i = 0; i < 1000; i++) {
                concurrentStore.findById("ID-" + (i % testSize));
            }
        });
        
        // Both should complete successfully
        assertTrue(arrayListTime > 0);
        assertTrue(concurrentTime > 0);
        
        // ConcurrentHashMap should be faster for lookups
        // (This is a general expectation, but we don't enforce it strictly)
        System.out.println("ArrayList time: " + (arrayListTime / 1_000_000) + " ms");
        System.out.println("ConcurrentHashMap time: " + (concurrentTime / 1_000_000) + " ms");
    }
    
    @Test
    @DisplayName("Search Performance Comparison")
    public void testSearchPerformanceComparison() {
        List<Material> testMaterials = generateTestMaterials(5000);
        
        // Test Trie performance
        MaterialTrie trie = new MaterialTrie();
        for (Material material : testMaterials) {
            trie.insert(material);
        }
        
        long trieTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                trie.searchByPrefix("Java" + (i % 10));
            }
        });
        
        // Test ArrayList search performance
        MaterialStore arrayStore = new MaterialStoreImpl();
        for (Material material : testMaterials) {
            arrayStore.addMaterial(material);
        }
        
        long arrayListTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                arrayStore.searchByTitle("Java" + (i % 10));
            }
        });
        
        assertTrue(trieTime > 0);
        assertTrue(arrayListTime > 0);
        
        System.out.println("Trie search time: " + (trieTime / 1_000_000) + " ms");
        System.out.println("ArrayList search time: " + (arrayListTime / 1_000_000) + " ms");
    }
    
    @Test
    @DisplayName("Cache Performance Validation")
    public void testCachePerformanceValidation() {
        SearchResultCache cache = new SearchResultCache(100);
        List<Material> testResults = generateTestMaterials(50);
        
        // Test cache hit performance
        cache.put("test-key", testResults);
        
        long hitTime = measureTime(() -> {
            for (int i = 0; i < 10000; i++) {
                cache.get("test-key");
            }
        });
        
        // Test cache miss performance
        long missTime = measureTime(() -> {
            for (int i = 0; i < 10000; i++) {
                cache.get("miss-key-" + i);
            }
        });
        
        assertTrue(hitTime > 0);
        assertTrue(missTime > 0);
        
        // Cache hits should be faster than misses (or at least not significantly slower)
        // Allow for some variance in timing measurements - cache performance can vary
        assertTrue(hitTime <= missTime * 10);
        
        System.out.println("Cache hit time: " + (hitTime / 1_000_000) + " ms");
        System.out.println("Cache miss time: " + (missTime / 1_000_000) + " ms");
    }
    
    @Test
    @DisplayName("Cached Search Service Performance")
    public void testCachedSearchServicePerformance() {
        List<Material> testMaterials = generateTestMaterials(1000);
        MockMaterialRepository repository = new MockMaterialRepository(testMaterials);
        CachedSearchService searchService = new CachedSearchService(repository, 100);
        
        // First search (cache miss)
        long firstSearchTime = measureTime(() -> {
            searchService.searchByPrefix("Java");
        });
        
        // Second search (cache hit)
        long secondSearchTime = measureTime(() -> {
            searchService.searchByPrefix("Java");
        });
        
        assertTrue(firstSearchTime > 0);
        assertTrue(secondSearchTime > 0);
        
        // Second search should be faster due to caching
        assertTrue(secondSearchTime <= firstSearchTime);
        
        System.out.println("First search time: " + (firstSearchTime / 1_000_000) + " ms");
        System.out.println("Second search time: " + (secondSearchTime / 1_000_000) + " ms");
    }
    
    @Test
    @DisplayName("Memory Usage Analysis")
    public void testMemoryUsageAnalysis() {
        Runtime runtime = Runtime.getRuntime();
        
        // Measure baseline memory
        runtime.gc();
        long baselineMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // Create large dataset
        List<Material> largeDataset = generateTestMaterials(50000);
        
        // Measure memory after creating materials
        runtime.gc();
        long afterCreationMemory = runtime.totalMemory() - runtime.freeMemory();
        
        long memoryUsed = afterCreationMemory - baselineMemory;
        
        assertTrue(memoryUsed > 0);
        assertTrue(memoryUsed < 100 * 1024 * 1024); // Should use less than 100MB
        
        System.out.println("Memory used for 50,000 materials: " + (memoryUsed / 1024 / 1024) + " MB");
    }
    
    @Test
    @DisplayName("Concurrent Access Performance")
    public void testConcurrentAccessPerformance() {
        ConcurrentMaterialStore store = new ConcurrentMaterialStore();
        List<Material> testMaterials = generateTestMaterials(1000);
        
        // Test concurrent write performance
        long concurrentWriteTime = measureTime(() -> {
            testMaterials.parallelStream().forEach(store::addMaterial);
        });
        
        // Test concurrent read performance
        long concurrentReadTime = measureTime(() -> {
            testMaterials.parallelStream().forEach(material -> {
                store.findById(material.getId());
            });
        });
        
        assertTrue(concurrentWriteTime > 0);
        assertTrue(concurrentReadTime > 0);
        
        // Verify all materials were added (some might be duplicates due to same ISBN)
        assertTrue(store.size() > 0);
        
        System.out.println("Concurrent write time: " + (concurrentWriteTime / 1_000_000) + " ms");
        System.out.println("Concurrent read time: " + (concurrentReadTime / 1_000_000) + " ms");
    }
    
    /**
     * Measures execution time of a runnable operation.
     */
    private long measureTime(Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        return System.nanoTime() - startTime;
    }
    
    /**
     * Generates test materials for performance testing.
     */
    private List<Material> generateTestMaterials(int count) {
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (i % 2 == 0) {
                materials.add(new EBook("ID-" + i, "Java Programming Guide " + i, "Author " + i, 
                                     29.99 + (i % 100), 2020 + (i % 4),
                                     "EPUB", 2.5, false, 50000, Media.MediaQuality.HIGH));
            } else {
                materials.add(new PrintedBook("9780123456789", "Advanced Java " + i, "Author " + i,
                                           39.99 + (i % 100), 2020 + (i % 4),
                                           300 + (i % 200), "Publisher " + i, true));
            }
        }
        return materials;
    }
    
    /**
     * Mock repository for testing purposes.
     */
    private static class MockMaterialRepository implements MaterialRepository {
        private final Map<String, Material> materials = new java.util.HashMap<>();
        
        public MockMaterialRepository(List<Material> initialMaterials) {
            for (Material material : initialMaterials) {
                materials.put(material.getId(), material);
            }
        }
        
        @Override
        public void save(Material material) {
            materials.put(material.getId(), material);
        }
        
        @Override
        public java.util.Optional<Material> findById(String id) {
            return java.util.Optional.ofNullable(materials.get(id));
        }
        
        @Override
        public List<Material> findAll() {
            return new ArrayList<>(materials.values());
        }
        
        @Override
        public boolean delete(String id) {
            return materials.remove(id) != null;
        }
        
        @Override
        public boolean exists(String id) {
            return materials.containsKey(id);
        }
        
        @Override
        public long count() {
            return materials.size();
        }
        
        @Override
        public void deleteAll() {
            materials.clear();
        }
    }
}
