package com.university.bookstore.decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.AudioBook;
import com.university.bookstore.model.Media;

class GiftWrappingDecoratorTest {
    
    private Material baseMaterial;
    private GiftWrappingDecorator decorator;
    
    @BeforeEach
    void setUp() {
        baseMaterial = new PrintedBook("9781234567890", "Test Book", "Test Author", 
                29.99, 2023, 300, "Test Publisher", false);
        decorator = new GiftWrappingDecorator(baseMaterial, "Premium");
    }
    
    @Test
    void testConstructorWithValidParameters() {
        assertNotNull(decorator);
        assertEquals("Premium", decorator.getWrappingStyle());
        assertEquals(baseMaterial, decorator.getDecoratedMaterial());
    }
    
    @Test
    void testConstructorWithNullMaterial() {
        assertThrows(NullPointerException.class, () -> {
            new GiftWrappingDecorator(null, "Standard");
        });
    }
    
    @Test
    void testConstructorWithNullStyle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GiftWrappingDecorator(baseMaterial, null);
        });
    }
    
    @Test
    void testConstructorWithEmptyStyle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GiftWrappingDecorator(baseMaterial, "");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new GiftWrappingDecorator(baseMaterial, "   ");
        });
    }
    
    @Test
    void testGetPrice() {
        double expectedPrice = baseMaterial.getPrice() + 5.99;
        assertEquals(expectedPrice, decorator.getPrice(), 0.01);
    }
    
    @Test
    void testGetDisplayInfo() {
        String displayInfo = decorator.getDisplayInfo();
        assertNotNull(displayInfo);
        assertTrue(displayInfo.contains("Gift Wrapped"));
        assertTrue(displayInfo.contains("Premium"));
    }
    
    @Test
    void testGetWrappingCost() {
        assertEquals(5.99, decorator.getGiftWrappingCost(), 0.01);
    }
    
    @Test
    void testDifferentWrappingStyles() {
        GiftWrappingDecorator standard = new GiftWrappingDecorator(baseMaterial, "Standard");
        GiftWrappingDecorator deluxe = new GiftWrappingDecorator(baseMaterial, "Deluxe");
        GiftWrappingDecorator holiday = new GiftWrappingDecorator(baseMaterial, "Holiday Special");
        
        assertEquals("Standard", standard.getWrappingStyle());
        assertEquals("Deluxe", deluxe.getWrappingStyle());
        assertEquals("Holiday Special", holiday.getWrappingStyle());
    }
    
    @Test
    void testWithEBook() {
        Material ebook = new EBook("E001", "Test EBook", "Author", 19.99, 2023, 
                "PDF", 5.5, true, 250, Media.MediaQuality.HIGH);
        GiftWrappingDecorator ebookDecorator = new GiftWrappingDecorator(ebook, "Digital Gift");
        
        assertEquals(19.99 + 5.99, ebookDecorator.getPrice(), 0.01);
        assertTrue(ebookDecorator.getDisplayInfo().contains("Digital Gift"));
    }
    
    @Test
    void testWithAudioBook() {
        Material audioBook = new AudioBook("9781111111111", "Test AudioBook", "Author", "Narrator",
                39.99, 2023, 480, "MP3", 120.5, Media.MediaQuality.STANDARD, "English", true);
        GiftWrappingDecorator audioDecorator = new GiftWrappingDecorator(audioBook, "Audio Special");
        
        assertEquals(39.99 + 5.99, audioDecorator.getPrice(), 0.01);
        assertTrue(audioDecorator.getDisplayInfo().contains("Audio Special"));
    }
    
    @Test
    void testMultipleGiftWrappings() {
        // Test stacking multiple gift wrappings (e.g., inner and outer wrapping)
        GiftWrappingDecorator innerWrap = new GiftWrappingDecorator(baseMaterial, "Inner Box");
        GiftWrappingDecorator outerWrap = new GiftWrappingDecorator(innerWrap, "Outer Wrap");
        
        double expectedPrice = baseMaterial.getPrice() + 5.99 + 5.99;
        assertEquals(expectedPrice, outerWrap.getPrice(), 0.01);
        
        assertTrue(outerWrap.getDisplayInfo().contains("Outer Wrap"));
    }
    
    @Test
    void testEquals() {
        GiftWrappingDecorator decorator2 = new GiftWrappingDecorator(baseMaterial, "Premium");
        GiftWrappingDecorator decorator3 = new GiftWrappingDecorator(baseMaterial, "Standard");
        
        // Decorators with same material and wrapping type should be equal
        assertEquals(decorator, decorator2);
        assertNotEquals(decorator, decorator3); // Different wrapping type
        assertEquals(decorator, decorator); // Same instance should equal itself
    }
    
    @Test
    void testHashCode() {
        GiftWrappingDecorator decorator2 = new GiftWrappingDecorator(baseMaterial, "Premium");
        assertEquals(decorator.hashCode(), decorator2.hashCode());
    }
    
    @Test
    void testToString() {
        String str = decorator.toString();
        assertNotNull(str);
        assertTrue(str.contains("GiftWrappingDecorator"));
    }
    
    @Test
    void testInheritedProperties() {
        assertEquals(baseMaterial.getId(), decorator.getId());
        assertEquals(baseMaterial.getTitle(), decorator.getTitle());
        assertEquals(baseMaterial.getCreator(), decorator.getCreator());
        assertEquals(baseMaterial.getYear(), decorator.getYear());
        assertEquals(baseMaterial.getType(), decorator.getType());
    }
    
    @Test
    void testPriceIsHigherThanBase() {
        assertTrue(decorator.getPrice() > baseMaterial.getPrice());
        assertEquals(baseMaterial.getPrice() + 5.99, decorator.getPrice(), 0.01);
    }
}