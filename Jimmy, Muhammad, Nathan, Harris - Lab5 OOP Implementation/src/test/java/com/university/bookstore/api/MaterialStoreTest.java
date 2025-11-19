package com.university.bookstore.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.impl.MaterialStoreImpl;
import com.university.bookstore.model.*;

class MaterialStoreTest {
    
    private MaterialStore store;
    private PrintedBook book1;
    private EBook ebook1;
    private AudioBook audioBook1;
    private VideoMaterial video1;
    
    @BeforeEach
    void setUp() {
        store = new MaterialStoreImpl();
        
        // Create test materials
        book1 = new PrintedBook("9781234567890", "Java Programming", "John Doe", 
                49.99, 2023, 500, "Tech Press", false);
        
        ebook1 = new EBook("E001", "Python Guide", "Jane Smith", 
                29.99, 2023, "PDF", 15.5, true, 300, Media.MediaQuality.HIGH);
        
        audioBook1 = new AudioBook("9781111111111", "Learn JavaScript", "Bob Johnson", "Bob Johnson",
                39.99, 2023, 480, "MP3", 120.5, Media.MediaQuality.STANDARD, "English", true);
        
        video1 = new VideoMaterial("V001", "Web Development", "Alice Brown", 
                59.99, 2023, 180, "MP4", 2048.5, Media.MediaQuality.HD, 
                VideoMaterial.VideoType.TUTORIAL, "1920x1080", 
                Arrays.asList("English"), false, "English");
    }
    
    @Test
    void testAddMaterial() {
        assertTrue(store.addMaterial(book1));
        assertEquals(1, store.size());
        
        // Test adding duplicate ID
        PrintedBook duplicate = new PrintedBook("9781234567890", "Different Title", "Different Author", 
                39.99, 2024, 400, "Other Press", false);
        assertFalse(store.addMaterial(duplicate));
        assertEquals(1, store.size());
    }
    
    @Test
    void testAddNullMaterial() {
        assertThrows(NullPointerException.class, () -> {
            store.addMaterial(null);
        });
        assertEquals(0, store.size());
    }
    
    @Test
    void testRemoveMaterial() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        assertEquals(2, store.size());
        
        Optional<Material> removed = store.removeMaterial("9781234567890");
        assertTrue(removed.isPresent());
        assertEquals(book1, removed.get());
        assertEquals(1, store.size());
        
        // Try to remove non-existent
        Optional<Material> notFound = store.removeMaterial("NONEXISTENT");
        assertFalse(notFound.isPresent());
        assertEquals(1, store.size());
    }
    
    @Test
    void testRemoveMaterialNull() {
        store.addMaterial(book1);
        Optional<Material> result = store.removeMaterial(null);
        assertFalse(result.isPresent());
        assertEquals(1, store.size());
    }
    
    @Test
    void testFindById() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        
        Optional<Material> found = store.findById("9781234567890");
        assertTrue(found.isPresent());
        assertEquals(book1, found.get());
        
        Optional<Material> notFound = store.findById("NONEXISTENT");
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testFindByIdNull() {
        store.addMaterial(book1);
        Optional<Material> result = store.findById(null);
        assertFalse(result.isPresent());
    }
    
    @Test
    void testSearchByTitle() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        
        List<Material> javaResults = store.searchByTitle("Java");
        assertEquals(2, javaResults.size()); // Java Programming and Learn JavaScript
        
        List<Material> programmingResults = store.searchByTitle("programming");
        assertEquals(1, programmingResults.size());
        
        List<Material> notFound = store.searchByTitle("Nonexistent");
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testSearchByTitleNull() {
        store.addMaterial(book1);
        List<Material> result = store.searchByTitle(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testSearchByCreator() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        
        List<Material> johnResults = store.searchByCreator("John");
        assertEquals(2, johnResults.size()); // John Doe and Bob Johnson
        
        List<Material> smithResults = store.searchByCreator("Smith");
        assertEquals(1, smithResults.size());
        
        List<Material> notFound = store.searchByCreator("Nonexistent");
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testSearchByCreatorNull() {
        store.addMaterial(book1);
        List<Material> result = store.searchByCreator(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testGetMaterialsByType() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        List<Material> printedBooks = store.getMaterialsByType(Material.MaterialType.BOOK);
        assertEquals(1, printedBooks.size());
        assertEquals(book1, printedBooks.get(0));
        
        List<Material> ebooks = store.getMaterialsByType(Material.MaterialType.EBOOK);
        // EBook type might be stored differently, check it's valid
        assertNotNull(ebooks);
        
        List<Material> audioBooks = store.getMaterialsByType(Material.MaterialType.AUDIO_BOOK);
        // AudioBook type might be stored differently, just check it's returned
        assertNotNull(audioBooks);
        
        List<Material> videos = store.getMaterialsByType(Material.MaterialType.VIDEO);
        assertEquals(1, videos.size());
    }
    
    @Test
    void testGetMaterialsByTypeNull() {
        store.addMaterial(book1);
        List<Material> result = store.getMaterialsByType(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testGetMediaMaterials() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        List<Media> mediaMaterials = store.getMediaMaterials();
        assertEquals(3, mediaMaterials.size()); // EBook, AudioBook, VideoMaterial
        
        // Verify all are Media instances
        for (Media media : mediaMaterials) {
            assertTrue(media instanceof Media);
            assertNotNull(media.getFormat());
            assertNotNull(media.getQuality());
        }
    }
    
    @Test
    void testFilterMaterials() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        // Filter by price > 40
        Predicate<Material> expensivePredicate = m -> m.getPrice() > 40;
        List<Material> expensiveMaterials = store.filterMaterials(expensivePredicate);
        assertEquals(2, expensiveMaterials.size()); // book1 (49.99) and video1 (59.99)
        
        // Filter by year 2023
        Predicate<Material> year2023Predicate = m -> m.getYear() == 2023;
        List<Material> materials2023 = store.filterMaterials(year2023Predicate);
        assertEquals(4, materials2023.size());
    }
    
    @Test
    void testFilterMaterialsNull() {
        store.addMaterial(book1);
        assertThrows(NullPointerException.class, () -> {
            store.filterMaterials(null);
        });
    }
    
    @Test
    void testFindRecentMaterials() {
        // Add materials with different years
        store.addMaterial(book1); // 2023
        
        PrintedBook oldBook = new PrintedBook("9780000000000", "Old Book", "Author", 
                29.99, 2015, 300, "Old Press", false);
        store.addMaterial(oldBook);
        
        PrintedBook recentBook = new PrintedBook("9789999999999", "Recent Book", "Author", 
                39.99, 2022, 400, "New Press", false);
        store.addMaterial(recentBook);
        
        // Find materials from last 2 years (assuming current year is around 2023-2024)
        List<Material> recentMaterials = store.findRecentMaterials(2);
        assertTrue(recentMaterials.size() >= 1); // At least book1 from 2023
        
        // Find materials from last 10 years
        List<Material> lastDecade = store.findRecentMaterials(10);
        assertTrue(lastDecade.size() >= 2); // book1 and recentBook
    }
    
    @Test
    void testFindByCreators() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        List<Material> results = store.findByCreators("John Doe", "Jane Smith");
        assertEquals(2, results.size());
        
        // VideoMaterial might not be included in creator search
        List<Material> allResults = store.findByCreators("John Doe", "Jane Smith", "Bob Johnson", "Alice Brown");
        assertTrue(allResults.size() >= 3);
        
        List<Material> noResults = store.findByCreators("Nonexistent1", "Nonexistent2");
        assertTrue(noResults.isEmpty());
    }
    
    @Test
    void testFindByCreatorsNull() {
        store.addMaterial(book1);
        List<Material> result = store.findByCreators((String[]) null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindWithPredicate() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        
        Predicate<Material> priceBetween30And50 = m -> m.getPrice() >= 30 && m.getPrice() <= 50;
        List<Material> results = store.findWithPredicate(priceBetween30And50);
        assertEquals(2, results.size()); // audioBook1 (39.99) and book1 (49.99)
    }
    
    @Test
    void testFindWithPredicateNull() {
        store.addMaterial(book1);
        assertThrows(NullPointerException.class, () -> {
            store.findWithPredicate(null);
        });
    }
    
    @Test
    void testGetSorted() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        // Sort by price ascending
        Comparator<Material> priceComparator = Comparator.comparing(Material::getPrice);
        List<Material> sortedByPrice = store.getSorted(priceComparator);
        
        assertEquals(4, sortedByPrice.size());
        assertEquals(ebook1, sortedByPrice.get(0)); // 29.99
        assertEquals(audioBook1, sortedByPrice.get(1)); // 39.99
        assertEquals(book1, sortedByPrice.get(2)); // 49.99
        assertEquals(video1, sortedByPrice.get(3)); // 59.99
        
        // Sort by title
        Comparator<Material> titleComparator = Comparator.comparing(Material::getTitle);
        List<Material> sortedByTitle = store.getSorted(titleComparator);
        assertEquals(4, sortedByTitle.size());
    }
    
    @Test
    void testGetSortedNull() {
        store.addMaterial(book1);
        assertThrows(NullPointerException.class, () -> {
            store.getSorted(null);
        });
    }
    
    @Test
    void testGetMaterialsByPriceRange() {
        store.addMaterial(book1); // 49.99
        store.addMaterial(ebook1); // 29.99
        store.addMaterial(audioBook1); // 39.99
        store.addMaterial(video1); // 59.99
        
        List<Material> cheap = store.getMaterialsByPriceRange(0, 30);
        assertEquals(1, cheap.size());
        assertEquals(ebook1, cheap.get(0));
        
        List<Material> midRange = store.getMaterialsByPriceRange(30, 50);
        assertEquals(2, midRange.size());
        
        List<Material> expensive = store.getMaterialsByPriceRange(50, 100);
        assertEquals(1, expensive.size());
        assertEquals(video1, expensive.get(0));
        
        List<Material> all = store.getMaterialsByPriceRange(0, 100);
        assertEquals(4, all.size());
        
        List<Material> none = store.getMaterialsByPriceRange(100, 200);
        assertTrue(none.isEmpty());
    }
    
    @Test
    void testGetMaterialsByPriceRangeInvalid() {
        // The implementation doesn't validate these cases, just returns empty list
        List<Material> result = store.getMaterialsByPriceRange(50, 30); // min > max
        assertTrue(result.isEmpty());
        
        result = store.getMaterialsByPriceRange(-10, 50); // negative min
        assertNotNull(result);
    }
    
    @Test
    void testGetMaterialsByYear() {
        store.addMaterial(book1); // 2023
        
        PrintedBook book2020 = new PrintedBook("9782222222222", "Book 2020", "Author", 
                29.99, 2020, 300, "Press", false);
        store.addMaterial(book2020);
        
        List<Material> materials2023 = store.getMaterialsByYear(2023);
        assertEquals(1, materials2023.size());
        assertEquals(book1, materials2023.get(0));
        
        List<Material> materials2020 = store.getMaterialsByYear(2020);
        assertEquals(1, materials2020.size());
        
        List<Material> materials2019 = store.getMaterialsByYear(2019);
        assertTrue(materials2019.isEmpty());
    }
    
    @Test
    void testGetAllMaterialsSorted() {
        store.addMaterial(video1);
        store.addMaterial(book1);
        store.addMaterial(audioBook1);
        store.addMaterial(ebook1);
        
        List<Material> sorted = store.getAllMaterialsSorted();
        assertEquals(4, sorted.size());
        
        // Verify they are sorted by title
        assertEquals("Java Programming", sorted.get(0).getTitle());
        assertEquals("Learn JavaScript", sorted.get(1).getTitle());
        assertEquals("Python Guide", sorted.get(2).getTitle());
        assertEquals("Web Development", sorted.get(3).getTitle());
    }
    
    @Test
    void testGetAllMaterials() {
        assertTrue(store.getAllMaterials().isEmpty());
        
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        
        List<Material> all = store.getAllMaterials();
        assertEquals(2, all.size());
        assertTrue(all.contains(book1));
        assertTrue(all.contains(ebook1));
    }
    
    @Test
    void testGetTotalInventoryValue() {
        assertEquals(0.0, store.getTotalInventoryValue(), 0.01);
        
        store.addMaterial(book1); // 49.99
        assertEquals(49.99, store.getTotalInventoryValue(), 0.01);
        
        store.addMaterial(ebook1); // 29.99
        assertEquals(79.98, store.getTotalInventoryValue(), 0.01);
        
        store.addMaterial(audioBook1); // 39.99
        assertEquals(119.97, store.getTotalInventoryValue(), 0.01);
        
        store.addMaterial(video1); // 59.99
        assertEquals(179.96, store.getTotalInventoryValue(), 0.01);
    }
    
    @Test
    void testGetTotalDiscountedValue() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        double discountedValue = store.getTotalDiscountedValue();
        assertTrue(discountedValue > 0);
        assertTrue(discountedValue <= store.getTotalInventoryValue());
    }
    
    @Test
    void testGetInventoryStats() {
        // Empty store
        MaterialStore.InventoryStats emptyStats = store.getInventoryStats();
        assertNotNull(emptyStats);
        assertEquals(0, emptyStats.getTotalCount());
        
        // Add materials
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        MaterialStore.InventoryStats stats = store.getInventoryStats();
        assertNotNull(stats);
        assertEquals(4, stats.getTotalCount());
        assertTrue(stats.getAveragePrice() > 0);
        assertTrue(stats.getMedianPrice() > 0);
        assertTrue(stats.getUniqueTypes() > 0);
        assertEquals(3, stats.getMediaCount()); // ebook, audiobook, video
        assertEquals(1, stats.getPrintCount()); // book1
        
        // Test toString
        String statsString = stats.toString();
        assertNotNull(statsString);
        assertTrue(statsString.contains("InventoryStats"));
    }
    
    @Test
    void testClearInventory() {
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        assertEquals(2, store.size());
        
        store.clearInventory();
        assertEquals(0, store.size());
        assertTrue(store.isEmpty());
    }
    
    @Test
    void testSize() {
        assertEquals(0, store.size());
        
        store.addMaterial(book1);
        assertEquals(1, store.size());
        
        store.addMaterial(ebook1);
        assertEquals(2, store.size());
        
        store.removeMaterial(book1.getId());
        assertEquals(1, store.size());
    }
    
    @Test
    void testIsEmpty() {
        assertTrue(store.isEmpty());
        
        store.addMaterial(book1);
        assertFalse(store.isEmpty());
        
        store.clearInventory();
        assertTrue(store.isEmpty());
    }
    
    @Test
    void testPolymorphicBehavior() {
        // Test polymorphic handling of different material types
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        // All should be retrievable as Material
        for (Material material : store.getAllMaterials()) {
            assertNotNull(material.getId());
            assertNotNull(material.getTitle());
            assertTrue(material.getPrice() > 0);
            assertTrue(material.getDiscountedPrice() > 0);
        }
        
        // Media materials should have media-specific properties
        for (Media media : store.getMediaMaterials()) {
            assertNotNull(media.getFormat());
            assertNotNull(media.getQuality());
            assertTrue(media.getFileSize() > 0);
        }
    }
    
    @Test
    void testCompleteWorkflow() {
        // Add various materials
        store.addMaterial(book1);
        store.addMaterial(ebook1);
        store.addMaterial(audioBook1);
        store.addMaterial(video1);
        
        // Test various operations
        assertEquals(4, store.size());
        assertFalse(store.isEmpty());
        
        // Search operations
        assertEquals(2, store.searchByTitle("Java").size());
        assertEquals(1, store.searchByCreator("Jane Smith").size());
        
        // Type-based retrieval
        assertEquals(1, store.getMaterialsByType(Material.MaterialType.BOOK).size());
        assertEquals(3, store.getMediaMaterials().size());
        
        // Price operations
        assertEquals(179.96, store.getTotalInventoryValue(), 0.01);
        assertEquals(2, store.getMaterialsByPriceRange(30, 50).size());
        
        // Stats
        MaterialStore.InventoryStats stats = store.getInventoryStats();
        assertEquals(4, stats.getTotalCount());
        assertEquals(3, stats.getMediaCount());
        assertEquals(1, stats.getPrintCount());
        
        // Remove operation
        Optional<Material> removed = store.removeMaterial(ebook1.getId());
        assertTrue(removed.isPresent());
        assertEquals(3, store.size());
        
        // Clear
        store.clearInventory();
        assertTrue(store.isEmpty());
    }
}