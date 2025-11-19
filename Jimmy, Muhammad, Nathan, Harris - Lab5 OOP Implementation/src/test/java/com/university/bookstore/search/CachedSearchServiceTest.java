package com.university.bookstore.search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.repository.MaterialRepository;

class CachedSearchServiceTest {
    
    private TestMaterialRepository repository;
    private CachedSearchService searchService;
    
    // Simple test implementation of MaterialRepository
    private static class TestMaterialRepository implements MaterialRepository {
        private List<Material> materials = new ArrayList<>();
        
        TestMaterialRepository(List<Material> initialMaterials) {
            this.materials.addAll(initialMaterials);
        }
        
        @Override
        public void save(Material material) {
            materials.add(material);
        }
        
        @Override
        public Optional<Material> findById(String id) {
            return materials.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
        }
        
        @Override
        public List<Material> findAll() {
            return new ArrayList<>(materials);
        }
        
        @Override
        public boolean delete(String id) {
            return materials.removeIf(m -> m.getId().equals(id));
        }
        
        @Override
        public boolean exists(String id) {
            return materials.stream().anyMatch(m -> m.getId().equals(id));
        }
        
        @Override
        public long count() {
            return materials.size();
        }
        
        @Override
        public void deleteAll() {
            materials.clear();
        }
        
        void updateMaterials(List<Material> newMaterials) {
            materials.clear();
            materials.addAll(newMaterials);
        }
    }
    
    @BeforeEach
    void setUp() {
        Material book1 = new PrintedBook("9781111111111", "Java Programming", "Author1", 49.99, 2023, 500, "Press1", false);
        Material book2 = new PrintedBook("9782222222222", "JavaScript Guide", "Author2", 39.99, 2023, 400, "Press2", false);
        Material book3 = new PrintedBook("9783333333333", "Python Basics", "Author3", 35.99, 2023, 350, "Press3", false);
        
        repository = new TestMaterialRepository(Arrays.asList(book1, book2, book3));
        searchService = new CachedSearchService(repository, 10);
    }
    
    @Test
    void testConstructorWithNullRepository() {
        assertThrows(NullPointerException.class, () -> {
            new CachedSearchService(null, 10);
        });
    }
    
    @Test
    void testSearchByPrefix() {
        List<Material> results = searchService.searchByPrefix("java");
        assertEquals(2, results.size());
        
        // Search again - should hit cache
        List<Material> cachedResults = searchService.searchByPrefix("java");
        assertEquals(2, cachedResults.size());
        assertEquals(results, cachedResults);
    }
    
    @Test
    void testSearchByPrefixNull() {
        List<Material> results = searchService.searchByPrefix(null);
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testSearchByPrefixEmpty() {
        List<Material> results = searchService.searchByPrefix("");
        assertTrue(results.isEmpty());
        
        results = searchService.searchByPrefix("   ");
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testSearchByPrefixWithLimit() {
        List<Material> results = searchService.searchByPrefixWithLimit("java", 1);
        assertEquals(1, results.size());
        
        // Search with higher limit
        results = searchService.searchByPrefixWithLimit("java", 10);
        assertEquals(2, results.size());
    }
    
    @Test
    void testSearchByPrefixWithLimitInvalid() {
        List<Material> results = searchService.searchByPrefixWithLimit("java", 0);
        assertTrue(results.isEmpty());
        
        results = searchService.searchByPrefixWithLimit("java", -5);
        assertTrue(results.isEmpty());
        
        results = searchService.searchByPrefixWithLimit(null, 10);
        assertTrue(results.isEmpty());
        
        results = searchService.searchByPrefixWithLimit("", 10);
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testAddMaterial() {
        Material newBook = new PrintedBook("9784444444444", "C++ Programming", "Author4", 59.99, 2023, 600, "Press4", false);
        searchService.addMaterial(newBook);
        
        List<Material> results = searchService.searchByPrefix("c++");
        assertEquals(1, results.size());
        assertEquals(newBook, results.get(0));
    }
    
    @Test
    void testAddMaterialNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            searchService.addMaterial(null);
        });
    }
    
    @Test
    void testRemoveMaterial() {
        List<Material> initialResults = searchService.searchByPrefix("java");
        assertEquals(2, initialResults.size());
        
        searchService.removeMaterial(initialResults.get(0));
        
        List<Material> afterRemoval = searchService.searchByPrefix("java");
        assertEquals(1, afterRemoval.size());
    }
    
    @Test
    void testRemoveMaterialNull() {
        // Should not throw exception
        assertDoesNotThrow(() -> {
            searchService.removeMaterial(null);
        });
    }
    
    @Test
    void testRefreshIndex() {
        // Initial search
        List<Material> initialResults = searchService.searchByPrefix("java");
        assertEquals(2, initialResults.size());
        
        // Change repository data
        Material newBook = new PrintedBook("9785555555555", "Java Advanced", "Author5", 69.99, 2023, 700, "Press5", false);
        repository.updateMaterials(Arrays.asList(newBook));
        
        // Refresh index
        searchService.refreshIndex();
        
        // Search again
        List<Material> refreshedResults = searchService.searchByPrefix("java");
        assertEquals(1, refreshedResults.size());
        assertEquals(newBook, refreshedResults.get(0));
    }
    
    @Test
    void testGetCacheStats() {
        // Perform some searches to populate cache
        searchService.searchByPrefix("java");
        searchService.searchByPrefix("python");
        searchService.searchByPrefix("java"); // Hit cache
        
        SearchResultCache.CacheStats stats = searchService.getCacheStats();
        assertNotNull(stats);
        assertTrue(stats.getCurrentSize() > 0);
    }
    
    @Test
    void testGetIndexSize() {
        assertEquals(3, searchService.getIndexSize());
        
        Material newBook = new PrintedBook("9786666666666", "Ruby Guide", "Author6", 45.99, 2023, 450, "Press6", false);
        searchService.addMaterial(newBook);
        
        assertEquals(4, searchService.getIndexSize());
    }
    
    @Test
    void testIsIndexEmpty() {
        assertFalse(searchService.isIndexEmpty());
        
        searchService.clear();
        assertTrue(searchService.isIndexEmpty());
    }
    
    @Test
    void testClear() {
        // Populate cache
        searchService.searchByPrefix("java");
        searchService.searchByPrefix("python");
        
        assertFalse(searchService.isIndexEmpty());
        
        searchService.clear();
        
        assertTrue(searchService.isIndexEmpty());
        assertEquals(0, searchService.getIndexSize());
    }
    
    @Test
    void testCachingBehavior() {
        // First search - cache miss
        List<Material> firstSearch = searchService.searchByPrefix("java");
        assertEquals(2, firstSearch.size());
        
        // Second search - cache hit
        List<Material> secondSearch = searchService.searchByPrefix("java");
        assertEquals(2, secondSearch.size());
        
        // Add new material
        Material newBook = new PrintedBook("9787777777777", "JavaScript Advanced", "Author7", 55.99, 2023, 550, "Press7", false);
        searchService.addMaterial(newBook);
        
        // Search again - cache should be invalidated
        List<Material> thirdSearch = searchService.searchByPrefix("java");
        assertEquals(3, thirdSearch.size());
    }
    
    @Test
    void testSearchCaseInsensitive() {
        List<Material> lowercase = searchService.searchByPrefix("java");
        List<Material> uppercase = searchService.searchByPrefix("JAVA");
        List<Material> mixedcase = searchService.searchByPrefix("JaVa");
        
        assertEquals(lowercase.size(), uppercase.size());
        assertEquals(lowercase.size(), mixedcase.size());
    }
    
    @Test
    void testSearchNonExistentPrefix() {
        List<Material> results = searchService.searchByPrefix("nonexistent");
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testToString() {
        String str = searchService.toString();
        assertNotNull(str);
        assertTrue(str.contains("CachedSearchService"));
        assertTrue(str.contains("IndexSize"));
        assertTrue(str.contains("CacheSize"));
        assertTrue(str.contains("HitRatio"));
    }
    
    @Test
    void testSearchWithSpecialCharacters() {
        Material book = new PrintedBook("9788888888888", "C++ Programming", "Author", 49.99, 2023, 500, "Press", false);
        searchService.addMaterial(book);
        
        List<Material> results = searchService.searchByPrefix("c++");
        assertEquals(1, results.size());
    }
    
    @Test
    void testLimitWithCaching() {
        // Search with limit 1
        List<Material> limit1 = searchService.searchByPrefixWithLimit("java", 1);
        assertEquals(1, limit1.size());
        
        // Search with limit 2 - different cache key
        List<Material> limit2 = searchService.searchByPrefixWithLimit("java", 2);
        assertEquals(2, limit2.size());
        
        // Search with limit 1 again - should hit cache
        List<Material> limit1Again = searchService.searchByPrefixWithLimit("java", 1);
        assertEquals(1, limit1Again.size());
        assertEquals(limit1, limit1Again);
    }
}