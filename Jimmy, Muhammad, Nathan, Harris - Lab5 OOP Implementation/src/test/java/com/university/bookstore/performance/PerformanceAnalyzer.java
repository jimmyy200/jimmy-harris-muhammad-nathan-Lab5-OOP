package com.university.bookstore.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
 * Performance analysis utility for measuring and comparing different implementations.
 * Provides comprehensive performance metrics and analysis for the bookstore system.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class PerformanceAnalyzer {
    
    /**
     * Analyzes performance of different data structures.
     */
    public PerformanceReport analyzeDataStructures() {
        PerformanceReport report = new PerformanceReport();
        
        // Test different data structures
        Map<String, Material> hashMap = new HashMap<>();
        List<Material> arrayList = new ArrayList<>();
        Map<String, Material> concurrentMap = new ConcurrentHashMap<>();
        
        int testSize = 100000;
        List<Material> testData = generateTestMaterials(testSize);
        
        // Insertion performance
        long hashMapInsertTime = measureTime(() -> {
            for (Material material : testData) {
                hashMap.put(material.getId(), material);
            }
        });
        
        long arrayListInsertTime = measureTime(() -> {
            for (Material material : testData) {
                arrayList.add(material);
            }
        });
        
        long concurrentMapInsertTime = measureTime(() -> {
            for (Material material : testData) {
                concurrentMap.put(material.getId(), material);
            }
        });
        
        // Search performance
        long hashMapSearchTime = measureTime(() -> {
            for (int i = 0; i < 10000; i++) {
                String randomId = "ID-" + (i % testSize);
                hashMap.get(randomId);
            }
        });
        
        long arrayListSearchTime = measureTime(() -> {
            for (int i = 0; i < 10000; i++) {
                String randomId = "ID-" + (i % testSize);
                arrayList.stream()
                    .filter(m -> m.getId().equals(randomId))
                    .findFirst();
            }
        });
        
        long concurrentMapSearchTime = measureTime(() -> {
            for (int i = 0; i < 10000; i++) {
                String randomId = "ID-" + (i % testSize);
                concurrentMap.get(randomId);
            }
        });
        
        report.addResult("HashMap Insert", hashMapInsertTime);
        report.addResult("ArrayList Insert", arrayListInsertTime);
        report.addResult("ConcurrentHashMap Insert", concurrentMapInsertTime);
        report.addResult("HashMap Search", hashMapSearchTime);
        report.addResult("ArrayList Search", arrayListSearchTime);
        report.addResult("ConcurrentHashMap Search", concurrentMapSearchTime);
        
        return report;
    }
    
    /**
     * Analyzes cache performance.
     */
    public CachePerformanceReport analyzeCachePerformance() {
        CachePerformanceReport report = new CachePerformanceReport();
        
        SearchResultCache cache = new SearchResultCache(1000);
        List<Material> testResults = generateTestMaterials(100);
        
        // Test cache hit ratio
        int totalRequests = 10000;
        int cacheHits = 0;
        
        // Warm up cache with some data
        for (int i = 0; i < 100; i++) {
            cache.put("query-" + i, testResults);
        }
        
        long startTime = System.nanoTime();
        for (int i = 0; i < totalRequests; i++) {
            String key = "query-" + (i % 200); // 50% hit ratio
            if (cache.get(key).isPresent()) {
                cacheHits++;
            }
        }
        long endTime = System.nanoTime();
        
        double hitRatio = (double) cacheHits / totalRequests;
        double avgResponseTime = (endTime - startTime) / (double) totalRequests / 1_000_000; // ms
        
        report.setHitRatio(hitRatio);
        report.setAverageResponseTime(avgResponseTime);
        report.setTotalRequests(totalRequests);
        report.setCacheHits(cacheHits);
        
        return report;
    }
    
    /**
     * Analyzes search performance across different implementations.
     */
    public SearchPerformanceReport analyzeSearchPerformance() {
        SearchPerformanceReport report = new SearchPerformanceReport();
        
        List<Material> testMaterials = generateTestMaterials(10000);
        
        // Test ArrayList-based search
        MaterialStore arrayListStore = new MaterialStoreImpl();
        for (Material material : testMaterials) {
            arrayListStore.addMaterial(material);
        }
        
        long arrayListSearchTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                arrayListStore.searchByTitle("Java" + (i % 10));
            }
        });
        
        // Test ConcurrentHashMap-based search
        MaterialStore concurrentStore = new ConcurrentMaterialStore();
        for (Material material : testMaterials) {
            concurrentStore.addMaterial(material);
        }
        
        long concurrentSearchTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                concurrentStore.searchByTitle("Java" + (i % 10));
            }
        });
        
        // Test Trie-based search
        MaterialTrie trie = new MaterialTrie();
        for (Material material : testMaterials) {
            trie.insert(material);
        }
        
        long trieSearchTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                trie.searchByPrefix("Java" + (i % 10));
            }
        });
        
        // Test cached search
        MockMaterialRepository repository = new MockMaterialRepository(testMaterials);
        CachedSearchService cachedSearch = new CachedSearchService(repository, 1000);
        
        long cachedSearchTime = measureTime(() -> {
            for (int i = 0; i < 1000; i++) {
                cachedSearch.searchByPrefix("Java" + (i % 10));
            }
        });
        
        report.setArrayListSearchTime(arrayListSearchTime);
        report.setConcurrentSearchTime(concurrentSearchTime);
        report.setTrieSearchTime(trieSearchTime);
        report.setCachedSearchTime(cachedSearchTime);
        
        return report;
    }
    
    /**
     * Analyzes memory usage of different implementations.
     */
    public MemoryUsageReport analyzeMemoryUsage() {
        MemoryUsageReport report = new MemoryUsageReport();
        
        Runtime runtime = Runtime.getRuntime();
        
        // Measure ArrayList implementation
        runtime.gc();
        long beforeArrayList = runtime.totalMemory() - runtime.freeMemory();
        
        MaterialStore arrayListStore = new MaterialStoreImpl();
        List<Material> testMaterials = generateTestMaterials(10000);
        for (Material material : testMaterials) {
            arrayListStore.addMaterial(material);
        }
        
        runtime.gc();
        long afterArrayList = runtime.totalMemory() - runtime.freeMemory();
        
        // Measure ConcurrentHashMap implementation
        runtime.gc();
        long beforeConcurrent = runtime.totalMemory() - runtime.freeMemory();
        
        MaterialStore concurrentStore = new ConcurrentMaterialStore();
        for (Material material : testMaterials) {
            concurrentStore.addMaterial(material);
        }
        
        runtime.gc();
        long afterConcurrent = runtime.totalMemory() - runtime.freeMemory();
        
        // Measure Trie implementation
        runtime.gc();
        long beforeTrie = runtime.totalMemory() - runtime.freeMemory();
        
        MaterialTrie trie = new MaterialTrie();
        for (Material material : testMaterials) {
            trie.insert(material);
        }
        
        runtime.gc();
        long afterTrie = runtime.totalMemory() - runtime.freeMemory();
        
        report.setArrayListMemoryUsage(afterArrayList - beforeArrayList);
        report.setConcurrentMemoryUsage(afterConcurrent - beforeConcurrent);
        report.setTrieMemoryUsage(afterTrie - beforeTrie);
        
        return report;
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
            materials.add(createTestMaterial("ID-" + i));
        }
        return materials;
    }
    
    /**
     * Creates a test material with the given ID.
     */
    private Material createTestMaterial(String id) {
        if (id.hashCode() % 2 == 0) {
            return new EBook(id, "Java Programming Guide " + id, "Author " + id, 
                           29.99 + Math.abs(id.hashCode() % 100), 2020 + (id.hashCode() % 4),
                           "EPUB", 2.5, false, 50000, Media.MediaQuality.HIGH);
        } else {
            return new PrintedBook("9780123456789", "Advanced Java " + id, "Author " + id,
                                 39.99 + Math.abs(id.hashCode() % 100), 2020 + (id.hashCode() % 4),
                                 300 + Math.abs(id.hashCode() % 200), "Publisher " + id, true);
        }
    }
    
    /**
     * Performance report containing timing results.
     */
    public static class PerformanceReport {
        private final Map<String, Long> results = new HashMap<>();
        
        public void addResult(String operation, long timeNanos) {
            results.put(operation, timeNanos);
        }
        
        public Map<String, Long> getResults() {
            return new HashMap<>(results);
        }
        
        public long getResult(String operation) {
            return results.getOrDefault(operation, 0L);
        }
        
        @Override
        public String toString() {
            return results.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + (entry.getValue() / 1_000_000) + " ms")
                .collect(Collectors.joining("\n"));
        }
    }
    
    /**
     * Cache performance report.
     */
    public static class CachePerformanceReport {
        private double hitRatio;
        private double averageResponseTime;
        private int totalRequests;
        private int cacheHits;
        
        public double getHitRatio() { return hitRatio; }
        public void setHitRatio(double hitRatio) { this.hitRatio = hitRatio; }
        
        public double getAverageResponseTime() { return averageResponseTime; }
        public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
        
        public int getTotalRequests() { return totalRequests; }
        public void setTotalRequests(int totalRequests) { this.totalRequests = totalRequests; }
        
        public int getCacheHits() { return cacheHits; }
        public void setCacheHits(int cacheHits) { this.cacheHits = cacheHits; }
        
        @Override
        public String toString() {
            return String.format("CachePerformance[HitRatio=%.2f%%, AvgResponse=%.2fms, Requests=%d, Hits=%d]",
                hitRatio * 100, averageResponseTime, totalRequests, cacheHits);
        }
    }
    
    /**
     * Search performance report.
     */
    public static class SearchPerformanceReport {
        private long arrayListSearchTime;
        private long concurrentSearchTime;
        private long trieSearchTime;
        private long cachedSearchTime;
        
        public long getArrayListSearchTime() { return arrayListSearchTime; }
        public void setArrayListSearchTime(long arrayListSearchTime) { this.arrayListSearchTime = arrayListSearchTime; }
        
        public long getConcurrentSearchTime() { return concurrentSearchTime; }
        public void setConcurrentSearchTime(long concurrentSearchTime) { this.concurrentSearchTime = concurrentSearchTime; }
        
        public long getTrieSearchTime() { return trieSearchTime; }
        public void setTrieSearchTime(long trieSearchTime) { this.trieSearchTime = trieSearchTime; }
        
        public long getCachedSearchTime() { return cachedSearchTime; }
        public void setCachedSearchTime(long cachedSearchTime) { this.cachedSearchTime = cachedSearchTime; }
        
        @Override
        public String toString() {
            return String.format("SearchPerformance[ArrayList=%.2fms, Concurrent=%.2fms, Trie=%.2fms, Cached=%.2fms]",
                arrayListSearchTime / 1_000_000.0, concurrentSearchTime / 1_000_000.0,
                trieSearchTime / 1_000_000.0, cachedSearchTime / 1_000_000.0);
        }
    }
    
    /**
     * Memory usage report.
     */
    public static class MemoryUsageReport {
        private long arrayListMemoryUsage;
        private long concurrentMemoryUsage;
        private long trieMemoryUsage;
        
        public long getArrayListMemoryUsage() { return arrayListMemoryUsage; }
        public void setArrayListMemoryUsage(long arrayListMemoryUsage) { this.arrayListMemoryUsage = arrayListMemoryUsage; }
        
        public long getConcurrentMemoryUsage() { return concurrentMemoryUsage; }
        public void setConcurrentMemoryUsage(long concurrentMemoryUsage) { this.concurrentMemoryUsage = concurrentMemoryUsage; }
        
        public long getTrieMemoryUsage() { return trieMemoryUsage; }
        public void setTrieMemoryUsage(long trieMemoryUsage) { this.trieMemoryUsage = trieMemoryUsage; }
        
        @Override
        public String toString() {
            return String.format("MemoryUsage[ArrayList=%dKB, Concurrent=%dKB, Trie=%dKB]",
                arrayListMemoryUsage / 1024, concurrentMemoryUsage / 1024, trieMemoryUsage / 1024);
        }
    }
    
    /**
     * Mock repository for testing purposes.
     */
    private static class MockMaterialRepository implements MaterialRepository {
        private final Map<String, Material> materials = new HashMap<>();
        
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
