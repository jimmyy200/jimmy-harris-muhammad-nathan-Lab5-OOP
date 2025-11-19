package com.university.bookstore.decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Media;

class MaterialDecoratorTest {
    
    private Material baseMaterial;
    private MaterialDecorator giftDecorator;
    private MaterialDecorator expeditedDecorator;
    private MaterialDecorator digitalDecorator;
    
    @BeforeEach
    void setUp() {
        baseMaterial = new PrintedBook("9781234567890", "Test Book", "Test Author", 
                29.99, 2023, 300, "Test Publisher", false);
        giftDecorator = new GiftWrappingDecorator(baseMaterial, "Premium");
        expeditedDecorator = new ExpeditedDeliveryDecorator(giftDecorator, 2);
        digitalDecorator = new DigitalAnnotationDecorator(expeditedDecorator);
    }
    
    @Test
    void testConstructorWithNull() {
        assertThrows(NullPointerException.class, () -> {
            new GiftWrappingDecorator(null, "Premium");
        });
    }
    
    @Test
    void testGetCreator() {
        assertEquals("Test Author", giftDecorator.getCreator());
        assertEquals("Test Author", expeditedDecorator.getCreator());
        assertEquals("Test Author", digitalDecorator.getCreator());
    }
    
    @Test
    void testGetDisplayInfo() {
        assertNotNull(giftDecorator.getDisplayInfo());
        assertNotNull(expeditedDecorator.getDisplayInfo());
        assertNotNull(digitalDecorator.getDisplayInfo());
    }
    
    @Test
    void testGetDiscountRate() {
        assertEquals(baseMaterial.getDiscountRate(), giftDecorator.getDiscountRate(), 0.001);
        assertEquals(baseMaterial.getDiscountRate(), expeditedDecorator.getDiscountRate(), 0.001);
        assertEquals(baseMaterial.getDiscountRate(), digitalDecorator.getDiscountRate(), 0.001);
    }
    
    @Test
    void testGetDecoratedMaterial() {
        assertEquals(baseMaterial, giftDecorator.getDecoratedMaterial());
        assertEquals(giftDecorator, expeditedDecorator.getDecoratedMaterial());
        assertEquals(expeditedDecorator, digitalDecorator.getDecoratedMaterial());
    }
    
    @Test
    void testGetBaseMaterial() {
        assertEquals(baseMaterial, giftDecorator.getBaseMaterial());
        assertEquals(baseMaterial, expeditedDecorator.getBaseMaterial());
        assertEquals(baseMaterial, digitalDecorator.getBaseMaterial());
    }
    
    @Test
    void testGetDecoratorCount() {
        assertEquals(0, giftDecorator.getDecoratorCount());
        assertEquals(1, expeditedDecorator.getDecoratorCount());
        assertEquals(2, digitalDecorator.getDecoratorCount());
    }
    
    @Test
    void testHasDecorators() {
        assertFalse(giftDecorator.hasDecorators());
        assertTrue(expeditedDecorator.hasDecorators());
        assertTrue(digitalDecorator.hasDecorators());
    }
    
    @Test
    void testEquals() {
        MaterialDecorator giftDecorator2 = new GiftWrappingDecorator(baseMaterial, "Premium");
        assertEquals(giftDecorator, giftDecorator2);
        
        MaterialDecorator differentBase = new GiftWrappingDecorator(
            new PrintedBook("9999999999999", "Other Book", "Other Author", 
                39.99, 2023, 400, "Other Publisher", true), "Premium");
        assertNotEquals(giftDecorator, differentBase);
        
        assertNotEquals(giftDecorator, null);
        assertNotEquals(giftDecorator, "String");
        assertEquals(giftDecorator, giftDecorator);
    }
    
    @Test
    void testHashCode() {
        MaterialDecorator giftDecorator2 = new GiftWrappingDecorator(baseMaterial, "Premium");
        assertEquals(giftDecorator.hashCode(), giftDecorator2.hashCode());
    }
    
    @Test
    void testToString() {
        String str = giftDecorator.toString();
        assertNotNull(str);
        assertTrue(str.contains("GiftWrappingDecorator"));
        assertTrue(str.contains("Test Book"));
    }
    
    @Test
    void testInheritedMethods() {
        assertEquals("9781234567890", giftDecorator.getId());
        assertEquals("Test Book", giftDecorator.getTitle());
        assertEquals(2023, giftDecorator.getYear());
        assertEquals(Material.MaterialType.BOOK, giftDecorator.getType());
    }
    
    @Test
    void testMultipleDecoratorsChain() {
        Material base = new EBook("E001", "EBook", "Author", 19.99, 2023, 
                "PDF", 5.5, true, 250, Media.MediaQuality.HIGH);
        
        MaterialDecorator dec1 = new GiftWrappingDecorator(base, "Standard");
        MaterialDecorator dec2 = new ExpeditedDeliveryDecorator(dec1, 1);
        MaterialDecorator dec3 = new DigitalAnnotationDecorator(dec2);
        MaterialDecorator dec4 = new GiftWrappingDecorator(dec3, "Deluxe");
        
        assertEquals(base, dec4.getBaseMaterial());
        assertEquals(3, dec4.getDecoratorCount());
        assertTrue(dec4.hasDecorators());
    }
    
    @Test
    void testPriceCalculation() {
        // Base price should be accessible through decorator
        assertTrue(giftDecorator.getPrice() > 0);
        assertTrue(expeditedDecorator.getPrice() > 0);
        assertTrue(digitalDecorator.getPrice() > 0);
    }
    
    @Test
    void testCompareTo() {
        Material otherBase = new PrintedBook("9999999999999", "Another Book", "Author", 
                19.99, 2023, 200, "Publisher", false);
        MaterialDecorator otherDecorator = new GiftWrappingDecorator(otherBase, "Basic");
        
        // Should compare by title
        assertTrue(giftDecorator.compareTo(otherDecorator) > 0);
        assertTrue(otherDecorator.compareTo(giftDecorator) < 0);
        assertEquals(0, giftDecorator.compareTo(giftDecorator));
    }
}