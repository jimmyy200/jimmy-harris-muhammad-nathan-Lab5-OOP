package com.university.bookstore.composite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;

/**
 * Comprehensive test class for MaterialLeaf.
 * Tests leaf component in composite pattern, delegation to wrapped material.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MaterialLeafTest {
    
    private MaterialLeaf leaf;
    private Material testMaterial;
    
    @BeforeEach
    void setUp() {
        testMaterial = new PrintedBook("9781234567890", "Java Programming", "John Doe", 50.00, 2020, 400, "Publisher", false);
        leaf = new MaterialLeaf(testMaterial);
    }
    
    @Test
    void testConstructorValid() {
        assertNotNull(leaf);
        assertEquals(testMaterial, leaf.getMaterial());
        assertTrue(leaf.isLeaf());
    }
    
    @Test
    void testConstructorWithNull() {
        assertThrows(NullPointerException.class, () -> new MaterialLeaf(null));
    }
    
    @Test
    void testGetTitle() {
        assertEquals("Java Programming", leaf.getTitle());
        assertEquals(testMaterial.getTitle(), leaf.getTitle());
    }
    
    @Test
    void testGetPrice() {
        assertEquals(50.00, leaf.getPrice(), 0.01);
        assertEquals(testMaterial.getPrice(), leaf.getPrice(), 0.01);
    }
    
    @Test
    void testGetDiscountedPrice() {
        assertEquals(testMaterial.getDiscountedPrice(), leaf.getDiscountedPrice(), 0.01);
        
        // Create material with discount to verify delegation
        Material discountedMaterial = new PrintedBook("9780987654321", "Old Book", "Author", 30.0, 2010, 200, "Pub", false);
        MaterialLeaf discountedLeaf = new MaterialLeaf(discountedMaterial);
        
        // Old books (< current year - 2) get 15% discount
        assertEquals(25.50, discountedLeaf.getDiscountedPrice(), 0.01); // 30 * 0.85 = 25.50
    }
    
    @Test
    void testGetDescription() {
        String description = leaf.getDescription();
        assertNotNull(description);
        assertEquals(testMaterial.getDisplayInfo(), description);
        
        assertTrue(description.contains("Java Programming"));
        assertTrue(description.contains("John Doe"));
        assertTrue(description.contains("2020"));
        assertTrue(description.contains("$50.00"));
    }
    
    @Test
    void testGetMaterials() {
        List<Material> materials = leaf.getMaterials();
        assertEquals(1, materials.size());
        assertEquals(testMaterial, materials.get(0));
        
        // Test immutability
        assertTrue(materials instanceof List);
    }
    
    @Test
    void testGetItemCount() {
        assertEquals(1, leaf.getItemCount());
    }
    
    @Test
    void testGetDiscountRate() {
        assertEquals(testMaterial.getDiscountRate(), leaf.getDiscountRate(), 0.01);
        
        // Test with material that has discount
        Material discountedMaterial = new PrintedBook("9780987654321", "Old Book", "Author", 30.0, 2010, 200, "Pub", false);
        MaterialLeaf discountedLeaf = new MaterialLeaf(discountedMaterial);
        
        assertEquals(0.15, discountedLeaf.getDiscountRate(), 0.01); // 15% discount for old books
    }
    
    @Test
    void testIsLeaf() {
        assertTrue(leaf.isLeaf());
    }
    
    @Test
    void testGetId() {
        assertEquals("9781234567890", leaf.getId());
        assertEquals(testMaterial.getId(), leaf.getId());
    }
    
    @Test
    void testGetType() {
        assertEquals(Material.MaterialType.BOOK, leaf.getType());
        assertEquals(testMaterial.getType(), leaf.getType());
    }
    
    @Test
    void testGetCreator() {
        assertEquals("John Doe", leaf.getCreator());
        assertEquals(testMaterial.getCreator(), leaf.getCreator());
    }
    
    @Test
    void testGetYear() {
        assertEquals(2020, leaf.getYear());
        assertEquals(testMaterial.getYear(), leaf.getYear());
    }
    
    @Test
    void testEquals() {
        Material sameMaterial = new PrintedBook("9781234567890", "Java Programming", "John Doe", 50.00, 2020, 400, "Publisher", false);
        MaterialLeaf sameLeaf = new MaterialLeaf(sameMaterial);
        
        assertEquals(leaf, sameLeaf);
        assertEquals(leaf.hashCode(), sameLeaf.hashCode());
        
        // Same instance
        assertEquals(leaf, leaf);
        
        // Different material
        Material differentMaterial = new PrintedBook("9780987654321", "Python Guide", "Jane Smith", 40.0, 2021, 300, "Pub", true);
        MaterialLeaf differentLeaf = new MaterialLeaf(differentMaterial);
        
        assertNotEquals(leaf, differentLeaf);
        assertNotEquals(leaf.hashCode(), differentLeaf.hashCode());
    }
    
    @Test
    void testEqualsWithNull() {
        assertNotEquals(leaf, null);
        assertNotEquals(leaf, "not a leaf");
        assertNotEquals(leaf, testMaterial);
    }
    
    @Test
    void testHashCode() {
        Material sameMaterial = new PrintedBook("9781234567890", "Java Programming", "John Doe", 50.00, 2020, 400, "Publisher", false);
        MaterialLeaf sameLeaf = new MaterialLeaf(sameMaterial);
        
        assertEquals(leaf.hashCode(), sameLeaf.hashCode());
        
        Material differentMaterial = new PrintedBook("9780987654321", "Different Book", "Different Author", 30.0, 2019, 200, "Pub", false);
        MaterialLeaf differentLeaf = new MaterialLeaf(differentMaterial);
        
        // Different materials should likely have different hash codes
        assertNotEquals(leaf.hashCode(), differentLeaf.hashCode());
    }
    
    @Test
    void testToString() {
        String str = leaf.toString();
        assertTrue(str.contains("MaterialLeaf"));
        assertTrue(str.contains("Java Programming"));
    }
    
    @Test
    void testDelegationToWrappedMaterial() {
        // Test that all methods properly delegate to the wrapped material
        assertEquals(testMaterial.getTitle(), leaf.getTitle());
        assertEquals(testMaterial.getPrice(), leaf.getPrice(), 0.01);
        assertEquals(testMaterial.getDiscountedPrice(), leaf.getDiscountedPrice(), 0.01);
        assertEquals(testMaterial.getDiscountRate(), leaf.getDiscountRate(), 0.01);
        assertEquals(testMaterial.getDisplayInfo(), leaf.getDescription());
        assertEquals(testMaterial.getId(), leaf.getId());
        assertEquals(testMaterial.getType(), leaf.getType());
        assertEquals(testMaterial.getCreator(), leaf.getCreator());
        assertEquals(testMaterial.getYear(), leaf.getYear());
    }
    
    @Test
    void testMaterialListImmutability() {
        List<Material> materials = leaf.getMaterials();
        assertEquals(1, materials.size());
        
        // Should be immutable list
        assertThrows(UnsupportedOperationException.class, () -> materials.add(testMaterial));
        assertThrows(UnsupportedOperationException.class, () -> materials.remove(0));
        assertThrows(UnsupportedOperationException.class, () -> materials.clear());
    }
    
    @Test
    void testWithDifferentMaterialTypes() {
        // Test with different material implementations to ensure proper delegation
        
        // Test with old book (has discount)
        Material oldBook = new PrintedBook("9781111111111", "Old Book", "Old Author", 20.0, 2010, 150, "Old Pub", false);
        MaterialLeaf oldLeaf = new MaterialLeaf(oldBook);
        
        assertEquals("Old Book", oldLeaf.getTitle());
        assertEquals(20.0, oldLeaf.getPrice(), 0.01);
        assertEquals(0.15, oldLeaf.getDiscountRate(), 0.01); // 15% for old books
        assertEquals(17.0, oldLeaf.getDiscountedPrice(), 0.01); // 20 * 0.85 = 17
        
        // Test with new book (no discount)
        Material newBook = new PrintedBook("9782222222222", "New Book", "New Author", 30.0, 2023, 250, "New Pub", true);
        MaterialLeaf newLeaf = new MaterialLeaf(newBook);
        
        assertEquals("New Book", newLeaf.getTitle());
        assertEquals(30.0, newLeaf.getPrice(), 0.01);
        assertEquals(0.0, newLeaf.getDiscountRate(), 0.01); // No discount for new books
        assertEquals(30.0, newLeaf.getDiscountedPrice(), 0.01); // No discount applied
    }
    
    @Test
    void testComponentInterfaceCompliance() {
        // Verify that MaterialLeaf properly implements MaterialComponent interface
        assertTrue(leaf instanceof MaterialComponent);
        
        // Test all interface methods
        assertNotNull(leaf.getTitle());
        assertTrue(leaf.getPrice() >= 0);
        assertTrue(leaf.getDiscountedPrice() >= 0);
        assertNotNull(leaf.getDescription());
        assertNotNull(leaf.getMaterials());
        assertEquals(1, leaf.getItemCount());
        assertTrue(leaf.getDiscountRate() >= 0 && leaf.getDiscountRate() <= 1);
        assertTrue(leaf.isLeaf());
    }
    
    @Test
    void testPriceCalculations() {
        // Test various price scenarios
        Material expensiveBook = new PrintedBook("9783333333333", "Expensive Book", "Author", 100.0, 2023, 500, "Premium Pub", true);
        MaterialLeaf expensiveLeaf = new MaterialLeaf(expensiveBook);
        
        assertEquals(100.0, expensiveLeaf.getPrice(), 0.01);
        assertEquals(100.0, expensiveLeaf.getDiscountedPrice(), 0.01); // New book, no discount
        
        Material cheapBook = new PrintedBook("9784444444444", "Cheap Book", "Author", 5.0, 2010, 100, "Budget Pub", false);
        MaterialLeaf cheapLeaf = new MaterialLeaf(cheapBook);
        
        assertEquals(5.0, cheapLeaf.getPrice(), 0.01);
        assertEquals(4.25, cheapLeaf.getDiscountedPrice(), 0.01); // Old book, 15% discount
    }
    
    @Test
    void testEqualityEdgeCases() {
        // Test equality with materials that have same ID but different other properties
        // (Materials are equal based on ID only)
        Material material1 = new PrintedBook("9785555555555", "Title 1", "Author 1", 10.0, 2020, 100, "Pub 1", false);
        Material material2 = new PrintedBook("9785555555555", "Title 2", "Author 2", 20.0, 2021, 200, "Pub 2", true);
        
        MaterialLeaf leaf1 = new MaterialLeaf(material1);
        MaterialLeaf leaf2 = new MaterialLeaf(material2);
        
        // Should be equal because materials have same ID
        assertEquals(leaf1, leaf2);
        assertEquals(leaf1.hashCode(), leaf2.hashCode());
    }
}