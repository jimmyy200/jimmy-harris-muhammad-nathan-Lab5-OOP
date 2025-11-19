package com.university.bookstore.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import org.junit.jupiter.api.*;

import com.university.bookstore.model.*;

/**
 * Test class for ModernConcurrentMaterialStore.
 * Tests modern concurrency features, async operations, and performance.
 */
@DisplayName("Modern Concurrent Material Store Tests")
public class ModernConcurrentMaterialStoreTest {
    
    private ModernConcurrentMaterialStore store;
    
    @BeforeEach
    void setUp() {
        store = new ModernConcurrentMaterialStore();
    }
    
    @AfterEach
    void tearDown() {
        if (store != null) {
            store.close();
        }
    }
    
    @Test
    @DisplayName("Test async material addition")
    void testAsyncAddMaterial() throws Exception {
        EBook ebook = new EBook("E001", "Async Java", "Author", 29.99, 2024,
                               "PDF", 2.5, false, 100, Media.MediaQuality.HIGH);
        
        CompletableFuture<Boolean> future = store.addMaterialAsync(ebook);
        
        assertTrue(future.get(1, TimeUnit.SECONDS));
        assertTrue(store.findById("E001").isPresent());
    }
    
    @Test
    @DisplayName("Test batch material addition")
    void testBatchAddMaterials() throws Exception {
        List<Material> materials = Arrays.asList(
            new PrintedBook("9781234567890", "Book 1", "Author 1", 19.99, 2024, 200, "ABC", true),
            new PrintedBook("9789876543210", "Book 2", "Author 2", 24.99, 2024, 300, "XYZ", false),
            new EBook("E001", "EBook 1", "Author 3", 14.99, 2024, "PDF", 1.5, true, 50, Media.MediaQuality.HIGH)
        );
        
        CompletableFuture<Map<String, Boolean>> future = store.addMaterialsBatchAsync(materials);
        Map<String, Boolean> results = future.get(2, TimeUnit.SECONDS);
        
        assertEquals(3, results.size());
        assertTrue(results.values().stream().allMatch(Boolean::booleanValue));
        assertEquals(3, store.size());
    }
    
    @Test
    @DisplayName("Test async find by ID")
    void testAsyncFindById() throws Exception {
        PrintedBook book = new PrintedBook("9781234567890", "Test Book", "Author", 29.99, 2024, 300, "Publisher", true);
        store.addMaterial(book);
        
        CompletableFuture<Optional<Material>> future = store.findByIdAsync("9781234567890");
        Optional<Material> result = future.get(1, TimeUnit.SECONDS);
        
        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
    }
    
    @Test
    @DisplayName("Test async search by title")
    void testAsyncSearchByTitle() throws Exception {
        store.addMaterial(new PrintedBook("9781234567890", "Java Programming", "Author 1", 39.99, 2024, 400, "Pub1", true));
        store.addMaterial(new PrintedBook("9789876543210", "Python Programming", "Author 2", 34.99, 2024, 350, "Pub2", false));
        store.addMaterial(new EBook("E001", "Java Concurrency", "Author 3", 29.99, 2024, "PDF", 2.0, false, 80, Media.MediaQuality.HIGH));
        
        CompletableFuture<List<Material>> future = store.searchByTitleAsync("Java");
        List<Material> results = future.get(1, TimeUnit.SECONDS);
        
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(m -> m.getTitle().contains("Java")));
    }
    
    @Test
    @DisplayName("Test async inventory stats")
    void testAsyncInventoryStats() throws Exception {
        // Add test materials
        for (int i = 0; i < 10; i++) {
            store.addMaterial(new PrintedBook("978123456" + String.format("%04d", i), "Book " + i, "Author", 20.0 + i, 2024, 200, "Publisher", i % 2 == 0));
        }
        
        CompletableFuture<com.university.bookstore.api.MaterialStore.InventoryStats> future = store.getInventoryStatsAsync();
        com.university.bookstore.api.MaterialStore.InventoryStats stats = future.get(2, TimeUnit.SECONDS);
        
        assertNotNull(stats);
        assertEquals(10, stats.getTotalCount());
        assertTrue(stats.getAveragePrice() > 0);
    }
    
    @Test
    @DisplayName("Test parallel search operations")
    void testParallelSearch() throws Exception {
        // Add diverse materials
        store.addMaterial(new PrintedBook("9781234567890", "Java Book", "John Doe", 39.99, 2024, 400, "Publisher", true));
        store.addMaterial(new EBook("E001", "Python Guide", "Jane Smith", 29.99, 2024, "PDF", 2.0, false, 100, Media.MediaQuality.HIGH));
        store.addMaterial(new AudioBook("9780123456789", "Audio Learning", "John Doe", "Narrator", 19.99, 2024, 180, "MP3", 5.0, Media.MediaQuality.STANDARD, "English", true));
        
        CompletableFuture<List<Material>> future = store.parallelSearchAsync(
            "Java", "John Doe", Material.MaterialType.BOOK
        );
        
        List<Material> results = future.get(2, TimeUnit.SECONDS);
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
    
    @Test
    @DisplayName("Test concurrent operations with multiple threads")
    void testConcurrentOperations() throws Exception {
        int threadCount = 50;
        int operationsPerThread = 100;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        String id = "T" + threadId + "M" + j;
                        PrintedBook book = new PrintedBook(
                            "978" + String.format("%010d", threadId * 1000 + j), "Book " + id, "Author", 29.99, 2024, 200, "Publisher", true
                        );
                        
                        // Mix of operations
                        store.addMaterial(book);
                        store.findById(id);
                        if (j % 10 == 0) {
                            store.searchByTitle("Book");
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        assertTrue(latch.await(30, TimeUnit.SECONDS));
        executor.shutdown();
        
        // Verify data integrity
        assertEquals(threadCount * operationsPerThread, store.size());
    }
    
    @Test
    @DisplayName("Test StampedLock optimistic read performance")
    void testOptimisticReadPerformance() {
        // Add materials
        for (int i = 0; i < 1000; i++) {
            store.addMaterial(new PrintedBook("978123456" + String.format("%04d", i), "Book " + i, "Author", 29.99, 2024, 200, "Publisher", true));
        }
        
        // Measure read performance
        long startTime = System.nanoTime();
        
        IntStream.range(0, 10000).parallel().forEach(i -> {
            store.findById("978123456" + String.format("%04d", i % 1000));
        });
        
        long duration = System.nanoTime() - startTime;
        
        // Should complete quickly due to optimistic reads
        assertTrue(duration < TimeUnit.SECONDS.toNanos(1), 
            "Optimistic reads should complete in under 1 second");
    }
    
    @Test
    @DisplayName("Test multiple async operations with CompletableFuture")
    void testMultipleAsyncOperations() throws Exception {
        // Chain multiple async operations
        CompletableFuture<Boolean> addFuture1 = store.addMaterialAsync(
            new PrintedBook("9781234567890", "Book 1", "Author", 29.99, 2024, 200, "Publisher", true)
        );
        
        CompletableFuture<Boolean> addFuture2 = store.addMaterialAsync(
            new EBook("E001", "EBook 1", "Author", 19.99, 2024, "PDF", 1.5, false, 50, Media.MediaQuality.HIGH)
        );
        
        // Combine futures
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(addFuture1, addFuture2);
        combinedFuture.get(2, TimeUnit.SECONDS);
        
        // Then search
        CompletableFuture<List<Material>> searchFuture = store.searchByTitleAsync("Book");
        List<Material> results = searchFuture.get(1, TimeUnit.SECONDS);
        
        assertFalse(results.isEmpty());
    }
    
    @Test
    @DisplayName("Test find multiple materials by IDs")
    void testFindByIdsAsync() throws Exception {
        // Add materials
        for (int i = 0; i < 5; i++) {
            store.addMaterial(new PrintedBook("978123456" + String.format("%04d", i), "Book " + i, "Author", 20.0 + i, 2024, 200, "Publisher", true));
        }
        
        List<String> ids = Arrays.asList("9781234560000", "9781234560002", "9781234560004", "9999999999999"); // last one doesn't exist
        
        CompletableFuture<Map<String, Material>> future = store.findByIdsAsync(ids);
        Map<String, Material> results = future.get(2, TimeUnit.SECONDS);
        
        assertEquals(3, results.size()); // Only 3 found
        assertNotNull(results.get("9781234560000"));
        assertNotNull(results.get("9781234560002"));
        assertNotNull(results.get("9781234560004"));
        assertNull(results.get("9999999999999"));
    }
    
    @Test
    @DisplayName("Test resource cleanup on close")
    void testResourceCleanup() throws Exception {
        ModernConcurrentMaterialStore tempStore = new ModernConcurrentMaterialStore();
        
        // Add materials and perform operations
        tempStore.addMaterial(new PrintedBook("9781234567890", "Book", "Author", 29.99, 2024, 200, "Publisher", true));
        CompletableFuture<Optional<Material>> future = tempStore.findByIdAsync("9781234567890");
        
        assertTrue(future.get(1, TimeUnit.SECONDS).isPresent());
        
        // Close and verify cleanup
        tempStore.close();
        
        // Should throw IllegalStateException after close
        assertThrows(IllegalStateException.class, () -> tempStore.addMaterial(
            new PrintedBook("9789876543210", "Book 2", "Author", 29.99, 2024, 200, "Publisher", true)
        ));
    }
    
    @Test
    @DisplayName("Test total inventory value async")
    void testGetTotalInventoryValueAsync() throws Exception {
        store.addMaterial(new PrintedBook("9781234567890", "Book 1", "Author", 20.00, 2024, 200, "Publisher", true));
        store.addMaterial(new PrintedBook("9789876543210", "Book 2", "Author", 30.00, 2024, 300, "Publisher", false));
        store.addMaterial(new EBook("E001", "EBook", "Author", 15.00, 2024, "PDF", 1.0, true, 50, Media.MediaQuality.HIGH));
        
        CompletableFuture<Double> future = store.getTotalInventoryValueAsync();
        Double totalValue = future.get(1, TimeUnit.SECONDS);
        
        assertEquals(65.00, totalValue, 0.01);
    }
}