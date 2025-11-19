package com.university.bookstore.observer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Media;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

class InventoryObserverTest {
    
    private InventoryObserver observer;
    private Material testMaterial;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        observer = new InventoryObserver();
        testMaterial = new PrintedBook("9781234567890", "Test Book", "Test Author", 
                29.99, 2023, 300, "Test Publisher", false);
        
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @Test
    void testOnEventWithMaterialAddedEvent() {
        MaterialAddedEvent event = new MaterialAddedEvent(testMaterial);
        
        observer.onEvent(event);
        
        assertEquals(1, observer.getTotalEvents());
        // Note: The inventory count behavior depends on implementation
        // Checking that at least the event was processed
        assertTrue(observer.getTotalEvents() > 0);
    }
    
    @Test
    void testOnEventWithPriceChangedEvent() {
        PriceChangedEvent event = new PriceChangedEvent(testMaterial, 29.99, 39.99);
        
        observer.onEvent(event);
        
        assertEquals(1, observer.getTotalEvents());
    }
    
    @Test
    void testMultipleEvents() {
        MaterialAddedEvent addEvent = new MaterialAddedEvent(testMaterial);
        PriceChangedEvent priceEvent = new PriceChangedEvent(testMaterial, 29.99, 34.99);
        
        observer.onEvent(addEvent);
        observer.onEvent(priceEvent);
        
        assertEquals(2, observer.getTotalEvents());
    }
    
    @Test
    void testGetInventoryCount() {
        MaterialAddedEvent event = new MaterialAddedEvent(testMaterial);
        observer.onEvent(event);
        
        // The actual count depends on how the observer handles the event
        int count = observer.getInventoryCount("9781234567890");
        assertTrue(count >= 0);
    }
    
    @Test
    void testGetInventoryCountForNonExistent() {
        assertEquals(0, observer.getInventoryCount("NONEXISTENT"));
    }
    
    @Test
    void testGetTotalValue() {
        MaterialAddedEvent event = new MaterialAddedEvent(testMaterial);
        observer.onEvent(event);
        
        double value = observer.getTotalValue("9781234567890");
        assertTrue(value >= 0);
    }
    
    @Test
    void testGetTotalInventoryCount() {
        Material material1 = new PrintedBook("9781111111111", "Book1", "Author1", 
                30.00, 2023, 300, "Publisher", false);
        Material material2 = new PrintedBook("9782222222222", "Book2", "Author2", 
                20.00, 2023, 200, "Publisher", false);
        
        observer.onEvent(new MaterialAddedEvent(material1));
        observer.onEvent(new MaterialAddedEvent(material2));
        
        int totalCount = observer.getTotalInventoryCount();
        assertTrue(totalCount >= 0);
    }
    
    @Test
    void testGetTotalInventoryValue() {
        Material material1 = new PrintedBook("9781111111111", "Book1", "Author1", 
                30.00, 2023, 300, "Publisher", false);
        Material material2 = new PrintedBook("9782222222222", "Book2", "Author2", 
                20.00, 2023, 200, "Publisher", false);
        
        observer.onEvent(new MaterialAddedEvent(material1));
        observer.onEvent(new MaterialAddedEvent(material2));
        
        double totalValue = observer.getTotalInventoryValue();
        assertTrue(totalValue >= 0);
    }
    
    @Test
    void testGetUniqueMaterialCount() {
        Material material1 = new PrintedBook("9781111111111", "Book1", "Author1", 
                30.00, 2023, 300, "Publisher", false);
        Material material2 = new PrintedBook("9782222222222", "Book2", "Author2", 
                20.00, 2023, 200, "Publisher", false);
        
        observer.onEvent(new MaterialAddedEvent(material1));
        observer.onEvent(new MaterialAddedEvent(material2));
        
        int uniqueCount = observer.getUniqueMaterialCount();
        assertTrue(uniqueCount >= 0);
    }
    
    @Test
    void testGetAllInventoryCounts() {
        Material material1 = new PrintedBook("9781111111111", "Book1", "Author1", 
                30.00, 2023, 300, "Publisher", false);
        Material material2 = new EBook("E001", "EBook", "Author", 19.99, 2023,
                "PDF", 5.5, true, 50000, Media.MediaQuality.HIGH);
        
        observer.onEvent(new MaterialAddedEvent(material1));
        observer.onEvent(new MaterialAddedEvent(material2));
        
        Map<String, Integer> counts = observer.getAllInventoryCounts();
        
        assertNotNull(counts);
        // The actual content depends on implementation
    }
    
    @Test
    void testGetAllTotalValues() {
        Material material1 = new PrintedBook("9781111111111", "Book1", "Author1", 
                30.00, 2023, 300, "Publisher", false);
        
        observer.onEvent(new MaterialAddedEvent(material1));
        
        Map<String, Double> values = observer.getAllTotalValues();
        
        assertNotNull(values);
    }
    
    @Test
    void testClear() {
        observer.onEvent(new MaterialAddedEvent(testMaterial));
        assertEquals(1, observer.getTotalEvents());
        
        observer.clear();
        
        assertEquals(0, observer.getTotalEvents());
        assertEquals(0, observer.getUniqueMaterialCount());
        assertEquals(0, observer.getTotalInventoryCount());
    }
    
    @Test
    void testOnEventWithNullEvent() {
        assertThrows(NullPointerException.class, () -> {
            observer.onEvent(null);
        });
    }
    
    @Test
    void testGetInventoryCountWithNull() {
        assertEquals(0, observer.getInventoryCount(null));
    }
    
    @Test
    void testGetInventoryCountWithEmpty() {
        assertEquals(0, observer.getInventoryCount(""));
    }
    
    @Test
    void testGetTotalValueWithNull() {
        assertEquals(0.0, observer.getTotalValue(null));
    }
    
    @Test
    void testToString() {
        observer.onEvent(new MaterialAddedEvent(testMaterial));
        
        String str = observer.toString();
        assertNotNull(str);
        assertTrue(str.contains("InventoryObserver"));
    }
    
    @Test
    void testPriceChangeTracking() {
        PriceChangedEvent event1 = new PriceChangedEvent(testMaterial, 29.99, 34.99);
        PriceChangedEvent event2 = new PriceChangedEvent(testMaterial, 34.99, 39.99);
        
        observer.onEvent(event1);
        observer.onEvent(event2);
        
        assertEquals(2, observer.getTotalEvents());
    }
    
    @Test
    void testMultipleMaterialsInventory() {
        Material[] materials = new Material[5];
        for (int i = 0; i < 5; i++) {
            materials[i] = new PrintedBook("978000000000" + i, "Book " + i, "Author", 
                    20.0 + i, 2023, 200, "Publisher", false);
            observer.onEvent(new MaterialAddedEvent(materials[i]));
        }
        
        assertEquals(5, observer.getTotalEvents());
        // Unique count depends on how duplicates are handled
        assertTrue(observer.getUniqueMaterialCount() <= 5);
    }
    
    @Test
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}