package com.university.bookstore.composite;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;

/**
 * Comprehensive test class for BundleService.
 * Tests bundle management, composite operations, and statistics.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BundleServiceTest {
    
    private BundleService service;
    private Material material1;
    private Material material2;
    private Material material3;
    
    @BeforeEach
    void setUp() {
        service = new BundleService();
        material1 = new PrintedBook("9781234567890", "Java Programming", "John Doe", 50.00, 2020, 400, "Publisher 1", false);
        material2 = new PrintedBook("9780987654321", "Python Guide", "Jane Smith", 40.00, 2021, 350, "Publisher 2", true);
        material3 = new PrintedBook("9781111111111", "JavaScript Basics", "Bob Johnson", 30.00, 2022, 300, "Publisher 3", false);
    }
    
    @Test
    void testInitialState() {
        assertEquals(0, service.getBundleCount());
        assertTrue(service.isEmpty());
        assertTrue(service.getAllBundles().isEmpty());
        assertTrue(service.getBundleNames().isEmpty());
        assertEquals(0.0, service.getTotalBundleValue(), 0.01);
        assertEquals(0.0, service.getTotalDiscountedBundleValue(), 0.01);
        assertEquals(0.0, service.calculateTotalSavings(), 0.01);
    }
    
    @Test
    void testCreateBundle() {
        MaterialBundle bundle = service.createBundle("Programming Bundle", 0.2);
        
        assertNotNull(bundle);
        assertEquals("Programming Bundle", bundle.getBundleName());
        assertEquals(0.2, bundle.getBundleDiscount(), 0.01);
        
        assertEquals(1, service.getBundleCount());
        assertFalse(service.isEmpty());
        assertTrue(service.getBundleNames().contains("Programming Bundle"));
    }
    
    @Test
    void testCreateBundleWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> service.createBundle(null, 0.1));
        assertThrows(IllegalArgumentException.class, () -> service.createBundle("", 0.1));
        assertThrows(IllegalArgumentException.class, () -> service.createBundle("   ", 0.1));
    }
    
    @Test
    void testCreateDuplicateBundle() {
        service.createBundle("Test Bundle", 0.1);
        
        assertThrows(IllegalArgumentException.class, () -> service.createBundle("Test Bundle", 0.2));
        assertEquals(1, service.getBundleCount());
    }
    
    @Test
    void testAddToBundle() {
        service.createBundle("Test Bundle", 0.15);
        service.addToBundle("Test Bundle", material1);
        
        Optional<MaterialBundle> bundle = service.getBundle("Test Bundle");
        assertTrue(bundle.isPresent());
        assertEquals(1, bundle.get().getItemCount());
        assertEquals(50.0, bundle.get().getPrice(), 0.01);
        assertTrue(bundle.get().containsMaterial(material1));
    }
    
    @Test
    void testAddToBundleWithNonExistentBundle() {
        assertThrows(IllegalArgumentException.class, 
            () -> service.addToBundle("Non-existent", material1));
    }
    
    @Test
    void testAddToBundleWithNullMaterial() {
        service.createBundle("Test Bundle", 0.1);
        
        assertThrows(IllegalArgumentException.class, 
            () -> service.addToBundle("Test Bundle", null));
    }
    
    @Test
    void testAddMultipleMaterialsToBundle() {
        service.createBundle("Multi Bundle", 0.1);
        service.addToBundle("Multi Bundle", material1);
        service.addToBundle("Multi Bundle", material2);
        service.addToBundle("Multi Bundle", material3);
        
        Optional<MaterialBundle> bundle = service.getBundle("Multi Bundle");
        assertTrue(bundle.isPresent());
        assertEquals(3, bundle.get().getItemCount());
        assertEquals(120.0, bundle.get().getPrice(), 0.01); // 50 + 40 + 30
        assertEquals(108.0, bundle.get().getDiscountedPrice(), 0.01); // 120 * 0.9
        assertTrue(bundle.get().containsMaterial(material1));
        assertTrue(bundle.get().containsMaterial(material2));
        assertTrue(bundle.get().containsMaterial(material3));
    }
    
    @Test
    void testAddBundleToBundle() {
        service.createBundle("Parent Bundle", 0.2);
        service.createBundle("Child Bundle", 0.1);
        
        service.addToBundle("Child Bundle", material1);
        service.addToBundle("Child Bundle", material2);
        
        service.addBundleToBundle("Parent Bundle", "Child Bundle");
        
        Optional<MaterialBundle> parentBundle = service.getBundle("Parent Bundle");
        assertTrue(parentBundle.isPresent());
        assertEquals(2, parentBundle.get().getItemCount()); // Items from child bundle
        assertEquals(90.0, parentBundle.get().getPrice(), 0.01); // 50 + 40
        assertTrue(parentBundle.get().containsMaterial(material1));
        assertTrue(parentBundle.get().containsMaterial(material2));
    }
    
    @Test
    void testAddBundleToBundleWithNonExistentBundles() {
        service.createBundle("Existing", 0.1);
        
        assertThrows(IllegalArgumentException.class, 
            () -> service.addBundleToBundle("Existing", "Non-existent"));
        
        assertThrows(IllegalArgumentException.class, 
            () -> service.addBundleToBundle("Non-existent", "Existing"));
        
        assertThrows(IllegalArgumentException.class, 
            () -> service.addBundleToBundle("Non-existent1", "Non-existent2"));
    }
    
    @Test
    void testAddComponentToBundle() {
        service.createBundle("Component Bundle", 0.15);
        MaterialLeaf leaf = new MaterialLeaf(material1);
        
        service.addComponentToBundle("Component Bundle", leaf);
        
        Optional<MaterialBundle> bundle = service.getBundle("Component Bundle");
        assertTrue(bundle.isPresent());
        assertEquals(1, bundle.get().getItemCount());
        assertTrue(bundle.get().containsMaterial(material1));
    }
    
    @Test
    void testAddComponentToBundleWithNullComponent() {
        service.createBundle("Test Bundle", 0.1);
        
        assertThrows(IllegalArgumentException.class, 
            () -> service.addComponentToBundle("Test Bundle", null));
    }
    
    @Test
    void testRemoveFromBundle() {
        service.createBundle("Remove Bundle", 0.1);
        service.addToBundle("Remove Bundle", material1);
        service.addToBundle("Remove Bundle", material2);
        
        Optional<MaterialBundle> bundle = service.getBundle("Remove Bundle");
        assertTrue(bundle.isPresent());
        assertEquals(2, bundle.get().getItemCount());
        
        boolean removed = service.removeFromBundle("Remove Bundle", material1);
        assertTrue(removed);
        assertEquals(1, bundle.get().getItemCount());
        assertFalse(bundle.get().containsMaterial(material1));
        assertTrue(bundle.get().containsMaterial(material2));
        
        // Try to remove non-existent material
        boolean notRemoved = service.removeFromBundle("Remove Bundle", material3);
        assertFalse(notRemoved);
        
        // Try to remove from non-existent bundle
        boolean invalidRemove = service.removeFromBundle("Non-existent", material2);
        assertFalse(invalidRemove);
    }
    
    @Test
    void testGetBundle() {
        service.createBundle("Test Bundle", 0.1);
        
        Optional<MaterialBundle> found = service.getBundle("Test Bundle");
        assertTrue(found.isPresent());
        assertEquals("Test Bundle", found.get().getBundleName());
        
        Optional<MaterialBundle> notFound = service.getBundle("Non-existent");
        assertFalse(notFound.isPresent());
    }
    
    @Test
    void testGetAllBundles() {
        assertTrue(service.getAllBundles().isEmpty());
        
        service.createBundle("Bundle 1", 0.1);
        service.createBundle("Bundle 2", 0.2);
        service.createBundle("Bundle 3", 0.15);
        
        List<MaterialBundle> allBundles = service.getAllBundles();
        assertEquals(3, allBundles.size());
        
        // Test defensive copy
        allBundles.clear();
        assertEquals(3, service.getBundleCount());
    }
    
    @Test
    void testGetBundleNames() {
        assertTrue(service.getBundleNames().isEmpty());
        
        service.createBundle("Alpha Bundle", 0.1);
        service.createBundle("Beta Bundle", 0.2);
        service.createBundle("Gamma Bundle", 0.15);
        
        List<String> names = service.getBundleNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("Alpha Bundle"));
        assertTrue(names.contains("Beta Bundle"));
        assertTrue(names.contains("Gamma Bundle"));
        
        // Test defensive copy
        names.clear();
        assertEquals(3, service.getBundleCount());
    }
    
    @Test
    void testCalculateBundleSavings() {
        service.createBundle("Savings Bundle", 0.25); // 25% discount
        service.addToBundle("Savings Bundle", material1); // $50
        service.addToBundle("Savings Bundle", material2); // $40
        // Total: $90, Discounted: $67.50, Savings: $22.50
        
        double savings = service.calculateBundleSavings("Savings Bundle");
        assertEquals(22.50, savings, 0.01);
    }
    
    @Test
    void testCalculateBundleSavingsWithNonExistentBundle() {
        assertThrows(IllegalArgumentException.class, 
            () -> service.calculateBundleSavings("Non-existent"));
    }
    
    @Test
    void testCalculateTotalSavings() {
        service.createBundle("Bundle 1", 0.2); // 20% discount
        service.addToBundle("Bundle 1", material1); // $50 -> $40, savings $10
        
        service.createBundle("Bundle 2", 0.15); // 15% discount  
        service.addToBundle("Bundle 2", material2); // $40 -> $34, savings $6
        
        double totalSavings = service.calculateTotalSavings();
        assertEquals(16.0, totalSavings, 0.01); // $10 + $6
    }
    
    @Test
    void testGetTotalBundleValue() {
        service.createBundle("Bundle 1", 0.1);
        service.addToBundle("Bundle 1", material1); // $50
        
        service.createBundle("Bundle 2", 0.2);
        service.addToBundle("Bundle 2", material2); // $40
        service.addToBundle("Bundle 2", material3); // $30
        
        double totalValue = service.getTotalBundleValue();
        assertEquals(120.0, totalValue, 0.01); // $50 + $40 + $30
    }
    
    @Test
    void testGetTotalDiscountedBundleValue() {
        service.createBundle("Bundle 1", 0.1); // 10% discount
        service.addToBundle("Bundle 1", material1); // $50 -> $45
        
        service.createBundle("Bundle 2", 0.2); // 20% discount
        service.addToBundle("Bundle 2", material2); // $40 -> $32
        
        double totalDiscountedValue = service.getTotalDiscountedBundleValue();
        assertEquals(77.0, totalDiscountedValue, 0.01); // $45 + $32
    }
    
    @Test
    void testGetBundlesByMaterialType() {
        service.createBundle("Book Bundle", 0.1);
        service.addToBundle("Book Bundle", material1); // BOOK
        
        service.createBundle("Mixed Bundle", 0.2);
        service.addToBundle("Mixed Bundle", material2); // BOOK
        
        service.createBundle("Empty Bundle", 0.15);
        
        List<MaterialBundle> bookBundles = service.getBundlesByMaterialType(Material.MaterialType.BOOK);
        assertEquals(2, bookBundles.size());
        
        List<MaterialBundle> magazineBundles = service.getBundlesByMaterialType(Material.MaterialType.MAGAZINE);
        assertTrue(magazineBundles.isEmpty());
    }
    
    @Test
    void testGetBundlesByDiscount() {
        service.createBundle("Low Discount", 0.05); // 5%
        service.createBundle("Medium Discount", 0.15); // 15%
        service.createBundle("High Discount", 0.25); // 25%
        
        List<MaterialBundle> minDiscountBundles = service.getBundlesByDiscount(0.1);
        assertEquals(2, minDiscountBundles.size()); // Medium and High
        
        List<MaterialBundle> highDiscountBundles = service.getBundlesByDiscount(0.2);
        assertEquals(1, highDiscountBundles.size()); // Only High
        
        List<MaterialBundle> veryHighDiscountBundles = service.getBundlesByDiscount(0.3);
        assertTrue(veryHighDiscountBundles.isEmpty());
    }
    
    @Test
    void testGetBundlesByValueRange() {
        service.createBundle("Cheap Bundle", 0.1);
        service.addToBundle("Cheap Bundle", material3); // $30
        
        service.createBundle("Medium Bundle", 0.1);
        service.addToBundle("Medium Bundle", material1); // $50
        
        service.createBundle("Expensive Bundle", 0.1);
        service.addToBundle("Expensive Bundle", material1); // $50
        service.addToBundle("Expensive Bundle", material2); // $40
        // Total: $90
        
        List<MaterialBundle> lowValueBundles = service.getBundlesByValueRange(20, 40);
        assertEquals(1, lowValueBundles.size()); // Cheap Bundle
        
        List<MaterialBundle> midValueBundles = service.getBundlesByValueRange(45, 55);
        assertEquals(1, midValueBundles.size()); // Medium Bundle
        
        List<MaterialBundle> highValueBundles = service.getBundlesByValueRange(80, 100);
        assertEquals(1, highValueBundles.size()); // Expensive Bundle
        
        List<MaterialBundle> allBundles = service.getBundlesByValueRange(0, 1000);
        assertEquals(3, allBundles.size());
        
        List<MaterialBundle> noBundles = service.getBundlesByValueRange(200, 300);
        assertTrue(noBundles.isEmpty());
    }
    
    @Test
    void testRemoveBundle() {
        service.createBundle("To Remove", 0.1);
        service.createBundle("To Keep", 0.2);
        
        assertEquals(2, service.getBundleCount());
        
        boolean removed = service.removeBundle("To Remove");
        assertTrue(removed);
        assertEquals(1, service.getBundleCount());
        assertFalse(service.getBundle("To Remove").isPresent());
        assertTrue(service.getBundle("To Keep").isPresent());
        
        boolean notRemoved = service.removeBundle("Non-existent");
        assertFalse(notRemoved);
        assertEquals(1, service.getBundleCount());
    }
    
    @Test
    void testClearAllBundles() {
        service.createBundle("Bundle 1", 0.1);
        service.createBundle("Bundle 2", 0.2);
        service.createBundle("Bundle 3", 0.15);
        
        assertEquals(3, service.getBundleCount());
        
        service.clearAllBundles();
        
        assertEquals(0, service.getBundleCount());
        assertTrue(service.isEmpty());
        assertTrue(service.getAllBundles().isEmpty());
        assertTrue(service.getBundleNames().isEmpty());
    }
    
    @Test
    void testGetBundleStats() {
        service.createBundle("Bundle 1", 0.2);
        service.addToBundle("Bundle 1", material1); // $50
        
        service.createBundle("Bundle 2", 0.15);
        service.addToBundle("Bundle 2", material2); // $40
        service.addToBundle("Bundle 2", material3); // $30
        
        BundleService.BundleStats stats = service.getBundleStats();
        
        assertEquals(2, stats.getTotalBundles());
        assertEquals(3, stats.getTotalItems());
        assertEquals(120.0, stats.getTotalValue(), 0.01); // $50 + $40 + $30
        assertEquals(99.5, stats.getTotalDiscountedValue(), 0.01); // $40 + $59.50
        assertEquals(20.5, stats.getTotalSavings(), 0.01); // $10 + $10.50
        assertEquals(0.175, stats.getAverageDiscount(), 0.01); // (0.2 + 0.15) / 2
    }
    
    @Test
    void testGetBundleStatsWithNoBundles() {
        BundleService.BundleStats stats = service.getBundleStats();
        
        assertEquals(0, stats.getTotalBundles());
        assertEquals(0, stats.getTotalItems());
        assertEquals(0.0, stats.getTotalValue(), 0.01);
        assertEquals(0.0, stats.getTotalDiscountedValue(), 0.01);
        assertEquals(0.0, stats.getTotalSavings(), 0.01);
        assertEquals(0.0, stats.getAverageDiscount(), 0.01);
    }
    
    @Test
    void testBundleStatsToString() {
        service.createBundle("Test Bundle", 0.1);
        service.addToBundle("Test Bundle", material1);
        
        BundleService.BundleStats stats = service.getBundleStats();
        String str = stats.toString();
        
        assertTrue(str.contains("BundleStats"));
        assertTrue(str.contains("Bundles=1"));
        assertTrue(str.contains("Items=1"));
        assertTrue(str.contains("Value=$50.00"));
        assertTrue(str.contains("Discounted=$45.00"));
        assertTrue(str.contains("Savings=$5.00"));
        assertTrue(str.contains("AvgDiscount=10.0%"));
    }
    
    @Test
    void testServiceToString() {
        String emptyStr = service.toString();
        assertTrue(emptyStr.contains("BundleService"));
        assertTrue(emptyStr.contains("Bundles=0"));
        assertTrue(emptyStr.contains("TotalValue=$0.00"));
        assertTrue(emptyStr.contains("TotalSavings=$0.00"));
        
        service.createBundle("Test Bundle", 0.2);
        service.addToBundle("Test Bundle", material1);
        
        String str = service.toString();
        assertTrue(str.contains("Bundles=1"));
        assertTrue(str.contains("TotalValue=$50.00"));
        assertTrue(str.contains("TotalSavings=$10.00"));
    }
    
    @Test
    void testComplexNestedScenario() {
        // Create a complex nested bundle structure
        service.createBundle("Programming Books", 0.1);
        service.addToBundle("Programming Books", material1); // Java - $50
        service.addToBundle("Programming Books", material2); // Python - $40
        
        service.createBundle("Web Development", 0.15);
        service.addToBundle("Web Development", material3); // JavaScript - $30
        
        service.createBundle("Complete Course", 0.25);
        service.addBundleToBundle("Complete Course", "Programming Books");
        service.addBundleToBundle("Complete Course", "Web Development");
        
        Optional<MaterialBundle> completeCourse = service.getBundle("Complete Course");
        assertTrue(completeCourse.isPresent());
        
        assertEquals(3, completeCourse.get().getItemCount());
        assertEquals(120.0, completeCourse.get().getPrice(), 0.01); // All materials
        assertEquals(90.0, completeCourse.get().getDiscountedPrice(), 0.01); // 25% off
        assertEquals(30.0, completeCourse.get().getTotalSavings(), 0.01);
        
        assertTrue(completeCourse.get().containsMaterial(material1));
        assertTrue(completeCourse.get().containsMaterial(material2));
        assertTrue(completeCourse.get().containsMaterial(material3));
        
        // Verify individual bundles still exist
        assertTrue(service.getBundle("Programming Books").isPresent());
        assertTrue(service.getBundle("Web Development").isPresent());
        assertEquals(3, service.getBundleCount());
    }
}