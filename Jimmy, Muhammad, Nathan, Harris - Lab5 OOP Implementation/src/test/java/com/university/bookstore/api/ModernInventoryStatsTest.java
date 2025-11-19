package com.university.bookstore.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.university.bookstore.api.ModernMaterialStore.ModernInventoryStats;

class ModernInventoryStatsTest {
    
    @Test
    void testValidConstruction() {
        ModernInventoryStats stats = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        
        assertEquals(10, stats.totalCount());
        assertEquals(49.99, stats.averagePrice(), 0.01);
        assertEquals(45.00, stats.medianPrice(), 0.01);
        assertEquals(3, stats.uniqueTypes());
        assertEquals(7, stats.mediaCount());
        assertEquals(3, stats.printCount());
    }
    
    @Test
    void testNegativeTotalCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernInventoryStats(-1, 49.99, 45.00, 3, 7, 3);
        });
    }
    
    @Test
    void testNegativeAveragePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernInventoryStats(10, -49.99, 45.00, 3, 7, 3);
        });
    }
    
    @Test
    void testNegativeMedianPrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernInventoryStats(10, 49.99, -45.00, 3, 7, 3);
        });
    }
    
    @Test
    void testNegativeUniqueTypes() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernInventoryStats(10, 49.99, 45.00, -3, 7, 3);
        });
    }
    
    @Test
    void testNegativeMediaCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernInventoryStats(10, 49.99, 45.00, 3, -7, 3);
        });
    }
    
    @Test
    void testNegativePrintCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ModernInventoryStats(10, 49.99, 45.00, 3, 7, -3);
        });
    }
    
    @Test
    void testEmptyStats() {
        ModernInventoryStats empty = ModernInventoryStats.empty();
        
        assertEquals(0, empty.totalCount());
        assertEquals(0.0, empty.averagePrice(), 0.01);
        assertEquals(0.0, empty.medianPrice(), 0.01);
        assertEquals(0, empty.uniqueTypes());
        assertEquals(0, empty.mediaCount());
        assertEquals(0, empty.printCount());
        
        assertTrue(empty.isEmpty());
    }
    
    @Test
    void testIsEmpty() {
        ModernInventoryStats empty = ModernInventoryStats.empty();
        assertTrue(empty.isEmpty());
        
        ModernInventoryStats nonEmpty = new ModernInventoryStats(5, 29.99, 25.00, 2, 3, 2);
        assertFalse(nonEmpty.isEmpty());
    }
    
    @Test
    void testGetMediaPercentage() {
        ModernInventoryStats stats = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        assertEquals(70.0, stats.getMediaPercentage(), 0.01);
        
        ModernInventoryStats allMedia = new ModernInventoryStats(5, 29.99, 25.00, 1, 5, 0);
        assertEquals(100.0, allMedia.getMediaPercentage(), 0.01);
        
        ModernInventoryStats noMedia = new ModernInventoryStats(5, 29.99, 25.00, 1, 0, 5);
        assertEquals(0.0, noMedia.getMediaPercentage(), 0.01);
        
        ModernInventoryStats empty = ModernInventoryStats.empty();
        assertEquals(0.0, empty.getMediaPercentage(), 0.01);
    }
    
    @Test
    void testGetPrintPercentage() {
        ModernInventoryStats stats = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        assertEquals(30.0, stats.getPrintPercentage(), 0.01);
        
        ModernInventoryStats allPrint = new ModernInventoryStats(5, 29.99, 25.00, 1, 0, 5);
        assertEquals(100.0, allPrint.getPrintPercentage(), 0.01);
        
        ModernInventoryStats noPrint = new ModernInventoryStats(5, 29.99, 25.00, 1, 5, 0);
        assertEquals(0.0, noPrint.getPrintPercentage(), 0.01);
        
        ModernInventoryStats empty = ModernInventoryStats.empty();
        assertEquals(0.0, empty.getPrintPercentage(), 0.01);
    }
    
    @Test
    void testRecordEquality() {
        ModernInventoryStats stats1 = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        ModernInventoryStats stats2 = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        ModernInventoryStats stats3 = new ModernInventoryStats(10, 49.99, 45.00, 3, 6, 4);
        
        // Records automatically implement equals
        assertEquals(stats1, stats2);
        assertNotEquals(stats1, stats3);
    }
    
    @Test
    void testRecordHashCode() {
        ModernInventoryStats stats1 = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        ModernInventoryStats stats2 = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        
        // Records automatically implement hashCode
        assertEquals(stats1.hashCode(), stats2.hashCode());
    }
    
    @Test
    void testRecordToString() {
        ModernInventoryStats stats = new ModernInventoryStats(10, 49.99, 45.00, 3, 7, 3);
        
        // Records automatically implement toString
        String str = stats.toString();
        assertNotNull(str);
        assertTrue(str.contains("ModernInventoryStats"));
        assertTrue(str.contains("10")); // totalCount
        assertTrue(str.contains("49.99")); // averagePrice
        assertTrue(str.contains("45.0")); // medianPrice
        assertTrue(str.contains("3")); // uniqueTypes
        assertTrue(str.contains("7")); // mediaCount
    }
    
    @Test
    void testBoundaryValues() {
        // Test with zero values
        ModernInventoryStats zeroStats = new ModernInventoryStats(0, 0.0, 0.0, 0, 0, 0);
        assertTrue(zeroStats.isEmpty());
        assertEquals(0.0, zeroStats.getMediaPercentage(), 0.01);
        assertEquals(0.0, zeroStats.getPrintPercentage(), 0.01);
        
        // Test with large values
        ModernInventoryStats largeStats = new ModernInventoryStats(
            Integer.MAX_VALUE, 
            Double.MAX_VALUE / 2, // Avoid overflow
            Double.MAX_VALUE / 2,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE
        );
        assertFalse(largeStats.isEmpty());
    }
    
    @Test
    void testMixedMediaAndPrintCounts() {
        // Test when media + print = total
        ModernInventoryStats balanced = new ModernInventoryStats(10, 49.99, 45.00, 2, 5, 5);
        assertEquals(50.0, balanced.getMediaPercentage(), 0.01);
        assertEquals(50.0, balanced.getPrintPercentage(), 0.01);
        
        // Test when media + print < total (some materials might be neither)
        ModernInventoryStats partial = new ModernInventoryStats(10, 49.99, 45.00, 3, 3, 4);
        assertEquals(30.0, partial.getMediaPercentage(), 0.01);
        assertEquals(40.0, partial.getPrintPercentage(), 0.01);
    }
    
    @Test
    void testStatisticsCalculations() {
        // Test various percentage calculations
        ModernInventoryStats stats = new ModernInventoryStats(100, 55.50, 50.00, 5, 75, 25);
        
        assertEquals(75.0, stats.getMediaPercentage(), 0.01);
        assertEquals(25.0, stats.getPrintPercentage(), 0.01);
        
        // Verify percentages add up to 100 when all materials are either media or print
        double totalPercentage = stats.getMediaPercentage() + stats.getPrintPercentage();
        assertEquals(100.0, totalPercentage, 0.01);
    }
}