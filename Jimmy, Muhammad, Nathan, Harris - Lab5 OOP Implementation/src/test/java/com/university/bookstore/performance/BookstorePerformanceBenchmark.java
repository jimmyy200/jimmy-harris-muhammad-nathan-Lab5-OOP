package com.university.bookstore.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.university.bookstore.api.MaterialStore;
import com.university.bookstore.factory.MaterialFactory;
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
 * JMH Performance Benchmarks for the Bookstore Management System.
 * Measures performance of different data structures, search algorithms, and caching strategies.
 * 
 * <p>This benchmark suite provides concrete performance data to validate architectural
 * decisions and identify performance bottlenecks in the system.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class BookstorePerformanceBenchmark {
    
    private MaterialStore arrayListStore;
    private MaterialStore concurrentStore;
    private List<Material> testMaterials;
    private MaterialTrie trie;
    private SearchResultCache cache;
    private CachedSearchService searchService;
    private MockMaterialRepository repository;
    
    @Setup(Level.Trial)
    public void setup() {
        // Initialize stores
        arrayListStore = new MaterialStoreImpl();
        concurrentStore = new ConcurrentMaterialStore();
        trie = new MaterialTrie();
        cache = new SearchResultCache(1000);
        
        // Generate test materials
        testMaterials = generateTestMaterials(10000);
        
        // Pre-populate stores
        for (Material material : testMaterials) {
            arrayListStore.addMaterial(material);
            concurrentStore.addMaterial(material);
            trie.insert(material);
        }
        
        // Setup repository and search service
        repository = new MockMaterialRepository(testMaterials);
        searchService = new CachedSearchService(repository, 1000);
    }
    
    @Benchmark
    public void benchmarkArrayListSearch() {
        for (int i = 0; i < 1000; i++) {
            String randomId = "ID-" + (i % 1000);
            arrayListStore.findById(randomId);
        }
    }
    
    @Benchmark
    public void benchmarkConcurrentSearch() {
        for (int i = 0; i < 1000; i++) {
            String randomId = "ID-" + (i % 1000);
            concurrentStore.findById(randomId);
        }
    }
    
    @Benchmark
    public void benchmarkTriePrefixSearch() {
        for (int i = 0; i < 100; i++) {
            String prefix = "Book" + (i % 100);
            trie.searchByPrefix(prefix);
        }
    }
    
    @Benchmark
    public void benchmarkCacheHit() {
        String testKey = "test-query";
        List<Material> testResults = testMaterials.subList(0, 100);
        
        // Warm up cache
        cache.put(testKey, testResults);
        
        for (int i = 0; i < 10000; i++) {
            cache.get(testKey);
        }
    }
    
    @Benchmark
    public void benchmarkCacheMiss() {
        for (int i = 0; i < 1000; i++) {
            String key = "miss-" + i;
            cache.get(key);
        }
    }
    
    @Benchmark
    public void benchmarkCachedSearchService() {
        for (int i = 0; i < 100; i++) {
            String prefix = "Java" + (i % 10);
            searchService.searchByPrefix(prefix);
        }
    }
    
    @Benchmark
    public void benchmarkArrayListInsert() {
        MaterialStore tempStore = new MaterialStoreImpl();
        for (int i = 0; i < 1000; i++) {
            Material material = createTestMaterial("TEMP-" + i);
            tempStore.addMaterial(material);
        }
    }
    
    @Benchmark
    public void benchmarkConcurrentInsert() {
        MaterialStore tempStore = new ConcurrentMaterialStore();
        for (int i = 0; i < 1000; i++) {
            Material material = createTestMaterial("TEMP-" + i);
            tempStore.addMaterial(material);
        }
    }
    
    @Benchmark
    public void benchmarkTrieInsert() {
        MaterialTrie tempTrie = new MaterialTrie();
        for (int i = 0; i < 1000; i++) {
            Material material = createTestMaterial("TEMP-" + i);
            tempTrie.insert(material);
        }
    }
    
    @Benchmark
    public void benchmarkMaterialCreation() {
        for (int i = 0; i < 1000; i++) {
            createTestMaterial("CREATE-" + i);
        }
    }
    
    @Benchmark
    public void benchmarkFactoryCreation() {
        for (int i = 0; i < 1000; i++) {
            createTestMaterialViaFactory("FACTORY-" + i);
        }
    }
    
    @Benchmark
    public void benchmarkStreamOperations() {
        testMaterials.stream()
            .filter(m -> m.getPrice() > 50.0)
            .filter(m -> m.getYear() > 2020)
            .mapToDouble(Material::getPrice)
            .sum();
    }
    
    @Benchmark
    public void benchmarkParallelStreamOperations() {
        testMaterials.parallelStream()
            .filter(m -> m.getPrice() > 50.0)
            .filter(m -> m.getYear() > 2020)
            .mapToDouble(Material::getPrice)
            .sum();
    }
    
    /**
     * Generates test materials for benchmarking.
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
                           29.99 + (id.hashCode() % 100), 2020 + (id.hashCode() % 4),
                           "EPUB", 2.5, false, 50000, Media.MediaQuality.HIGH);
        } else {
            return new PrintedBook("9780123456789", "Advanced Java " + id, "Author " + id,
                                 39.99 + (id.hashCode() % 100), 2020 + (id.hashCode() % 4),
                                 300 + (id.hashCode() % 200), "Publisher " + id, true);
        }
    }
    
    /**
     * Creates a test material using the factory pattern.
     */
    private Material createTestMaterialViaFactory(String id) {
        java.util.Map<String, Object> props = new java.util.HashMap<>();
        props.put("id", id);
        props.put("title", "Factory Book " + id);
        props.put("author", "Factory Author " + id);
        props.put("price", 29.99);
        props.put("year", 2023);
        props.put("fileFormat", "EPUB");
        props.put("fileSize", 2.5);
        props.put("drmEnabled", false);
        props.put("wordCount", 50000);
        props.put("quality", Media.MediaQuality.HIGH);
        
        return MaterialFactory.createMaterial("EBOOK", props);
    }
    
    /**
     * Mock repository for testing purposes.
     */
    private static class MockMaterialRepository implements MaterialRepository {
        private final java.util.Map<String, Material> materials = new java.util.HashMap<>();
        
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
