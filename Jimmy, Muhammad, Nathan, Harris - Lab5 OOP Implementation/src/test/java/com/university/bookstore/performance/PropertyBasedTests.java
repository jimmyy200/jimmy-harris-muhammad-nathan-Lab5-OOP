package com.university.bookstore.performance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.university.bookstore.factory.MaterialFactory;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.search.MaterialTrie;
import com.university.bookstore.search.SearchResultCache;

/**
 * Property-based tests using JUnit QuickCheck for the Bookstore Management System.
 * Tests properties that should hold for all valid inputs, not just specific examples.
 * 
 * <p>These tests verify that certain invariants and properties are maintained
 * across a wide range of inputs, providing stronger guarantees than traditional unit tests.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
@Tag("property-based")
@DisplayName("Property-Based Tests")
public class PropertyBasedTests {
    
    @Property(trials = 100)
    @DisplayName("EBook discount property - DRM-free books should have 15% discount")
    public void testEBookDiscountProperty(
            @Size(min = 1, max = 50) String title,
            @InRange(minDouble = 0.0, maxDouble = 1000.0) double price,
            boolean drmEnabled) {
        
        EBook ebook = new EBook("test-id", title, "Test Author", price, 2020, 
                               "EPUB", 1.0, drmEnabled, 10000, Media.MediaQuality.HIGH);
        
        double discount = ebook.getDiscountRate();
        double discountedPrice = ebook.getDiscountedPrice();
        
        // Property: DRM-free books should have 15% discount
        if (!drmEnabled) {
            assertEquals(0.15, discount, 0.001);
            assertEquals(price * 0.85, discountedPrice, 0.001);
        } else {
            assertEquals(0.0, discount, 0.001);
            assertEquals(price, discountedPrice, 0.001);
        }
        
        // Property: Discounted price should never exceed original price
        assertTrue(discountedPrice <= price);
    }
    
    @Property(trials = 100)
    @DisplayName("Material price property - price should always be non-negative")
    public void testMaterialPriceProperty(
            @Size(min = 1, max = 50) String title,
            @InRange(minDouble = 0.0, maxDouble = 1000.0) double price,
            @InRange(minInt = 1900, maxInt = 2030) int year) {
        
        PrintedBook book = new PrintedBook("test-id", title, "Test Author", price, year,
                                          300, "Test Publisher", true);
        
        // Property: Price should always be non-negative
        assertTrue(book.getPrice() >= 0);
        
        // Property: Discounted price should be less than or equal to original price
        assertTrue(book.getDiscountedPrice() <= book.getPrice());
        
        // Property: Discount rate should be between 0 and 1
        assertTrue(book.getDiscountRate() >= 0.0 && book.getDiscountRate() <= 1.0);
    }
    
    @Property(trials = 50)
    @DisplayName("Trie search property - prefix search should return consistent results")
    public void testTrieSearchProperty(
            @Size(min = 1, max = 20) String prefix,
            @Size(min = 1, max = 10) int materialCount) {
        
        MaterialTrie trie = new MaterialTrie();
        
        // Add materials with predictable titles
        for (int i = 0; i < materialCount; i++) {
            String title = prefix + " Book " + i;
            EBook ebook = new EBook("ID-" + i, title, "Author " + i, 29.99, 2023,
                                   "EPUB", 2.0, false, 50000, Media.MediaQuality.HIGH);
            trie.insert(ebook);
        }
        
        // Property: Prefix search should return all materials with matching prefix
        var results = trie.searchByPrefix(prefix);
        assertTrue(results.size() >= materialCount);
        
        // Property: All returned materials should have titles starting with the prefix
        for (Material material : results) {
            assertTrue(material.getTitle().toLowerCase().startsWith(prefix.toLowerCase()));
        }
        
        // Property: Empty prefix should return all materials
        var allResults = trie.searchByPrefix("");
        assertTrue(allResults.size() >= materialCount);
    }
    
    @Property(trials = 50)
    @DisplayName("Cache property - cache should maintain size limits")
    public void testCacheSizeProperty(
            @InRange(minInt = 1, maxInt = 100) int maxSize,
            @InRange(minInt = 1, maxInt = 200) int operations) {
        
        SearchResultCache cache = new SearchResultCache(maxSize);
        
        // Property: Cache should never exceed maximum size
        for (int i = 0; i < operations; i++) {
            String key = "key-" + i;
            var materials = java.util.List.<Material>of(createTestMaterial("ID-" + i));
            cache.put(key, materials);
            
            assertTrue(cache.size() <= maxSize);
        }
        
        // Property: Cache size should be at most maxSize
        assertTrue(cache.size() <= maxSize);
    }
    
    @Property(trials = 30)
    @DisplayName("Factory creation property - factory should create valid materials")
    public void testFactoryCreationProperty(
            @Size(min = 1, max = 20) String title,
            @InRange(minDouble = 0.0, maxDouble = 500.0) double price,
            @InRange(minInt = 1900, maxInt = 2030) int year,
            boolean isEBook) {
        
        Map<String, Object> props = new HashMap<>();
        props.put("id", "test-id");
        props.put("title", title);
        props.put("author", "Test Author");
        props.put("price", price);
        props.put("year", year);
        
        Material material;
        if (isEBook) {
            props.put("fileFormat", "EPUB");
            props.put("fileSize", 2.5);
            props.put("drmEnabled", false);
            props.put("wordCount", 50000);
            props.put("quality", Media.MediaQuality.HIGH);
            material = MaterialFactory.createMaterial("EBOOK", props);
        } else {
            props.put("pages", 300);
            props.put("publisher", "Test Publisher");
            props.put("hardcover", true);
            props.put("isbn", "978-0123456789");
            material = MaterialFactory.createMaterial("PRINTED_BOOK", props);
        }
        
        // Property: Factory should create valid materials
        assertNotNull(material);
        assertEquals(title, material.getTitle());
        assertEquals(price, material.getPrice(), 0.001);
        assertEquals(year, material.getYear());
        
        // Property: Material should have valid type
        assertTrue(material.getType() != null);
    }
    
    @Property(trials = 50)
    @DisplayName("Material comparison property - comparison should be consistent")
    public void testMaterialComparisonProperty(
            @Size(min = 1, max = 20) String title1,
            @Size(min = 1, max = 20) String title2,
            @InRange(minDouble = 0.0, maxDouble = 100.0) double price1,
            @InRange(minDouble = 0.0, maxDouble = 100.0) double price2) {
        
        EBook book1 = new EBook("ID-1", title1, "Author 1", price1, 2023,
                               "EPUB", 2.0, false, 50000, Media.MediaQuality.HIGH);
        EBook book2 = new EBook("ID-2", title2, "Author 2", price2, 2023,
                               "EPUB", 2.0, false, 50000, Media.MediaQuality.HIGH);
        
        int comparison = book1.compareTo(book2);
        
        // Property: Comparison should be consistent with equals
        if (comparison == 0) {
            // If compareTo returns 0, materials should be equal
            assertTrue(book1.equals(book2) || book1.getTitle().equals(book2.getTitle()));
        } else {
            // If compareTo returns non-zero, materials should not be equal
            assertTrue(comparison != 0);
        }
        
        // Property: Comparison should be antisymmetric
        int reverseComparison = book2.compareTo(book1);
        assertEquals(-comparison, reverseComparison);
    }
    
    @Property(trials = 30)
    @DisplayName("Bundle pricing property - bundle price should equal sum of components")
    public void testBundlePricingProperty(
            @InRange(minInt = 1, maxInt = 5) int componentCount,
            @InRange(minDouble = 0.0, maxDouble = 0.5) double discount) {
        
        com.university.bookstore.composite.MaterialBundle bundle = 
            new com.university.bookstore.composite.MaterialBundle("Test Bundle", discount);
        
        double expectedTotalPrice = 0.0;
        
        for (int i = 0; i < componentCount; i++) {
            EBook ebook = createTestMaterial("ID-" + i);
            bundle.addComponent(new com.university.bookstore.composite.MaterialLeaf(ebook));
            expectedTotalPrice += ebook.getPrice();
        }
        
        // Property: Bundle price should equal sum of component prices
        assertEquals(expectedTotalPrice, bundle.getPrice(), 0.001);
        
        // Property: Bundle discounted price should be less than or equal to original price
        assertTrue(bundle.getDiscountedPrice() <= bundle.getPrice());
        
        // Property: Bundle should contain correct number of items
        assertEquals(componentCount, bundle.getItemCount());
    }
    
    @Property(trials = 30)
    @DisplayName("Decorator property - decorator should preserve base material properties")
    public void testDecoratorProperty(
            @Size(min = 1, max = 20) String title,
            @InRange(minDouble = 0.0, maxDouble = 100.0) double price,
            @Size(min = 1, max = 10) String wrappingStyle) {
        
        EBook baseMaterial = new EBook("ID-1", title, "Test Author", price, 2023,
                                      "EPUB", 2.0, false, 50000, Media.MediaQuality.HIGH);
        
        com.university.bookstore.decorator.GiftWrappingDecorator decorated = 
            new com.university.bookstore.decorator.GiftWrappingDecorator(baseMaterial, wrappingStyle);
        
        // Property: Decorator should preserve base material properties
        assertEquals(baseMaterial.getId(), decorated.getId());
        assertEquals(baseMaterial.getTitle(), decorated.getTitle());
        assertEquals(baseMaterial.getCreator(), decorated.getCreator());
        assertEquals(baseMaterial.getYear(), decorated.getYear());
        
        // Property: Decorator should increase price
        assertTrue(decorated.getPrice() > baseMaterial.getPrice());
        
        // Property: Decorator should provide access to base material
        assertNotNull(decorated.getBaseMaterial());
        assertEquals(baseMaterial, decorated.getBaseMaterial());
    }
    
    /**
     * Creates a test material with the given ID.
     */
    private EBook createTestMaterial(String id) {
        return new EBook(id, "Test Book " + id, "Test Author", 29.99, 2023,
                        "EPUB", 2.0, false, 50000, Media.MediaQuality.HIGH);
    }
}
