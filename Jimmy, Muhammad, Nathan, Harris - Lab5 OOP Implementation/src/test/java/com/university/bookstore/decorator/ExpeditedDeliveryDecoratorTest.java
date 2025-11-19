package com.university.bookstore.decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.VideoMaterial;
import com.university.bookstore.model.Media;

class ExpeditedDeliveryDecoratorTest {
    
    private Material baseMaterial;
    private ExpeditedDeliveryDecorator decorator;
    
    @BeforeEach
    void setUp() {
        baseMaterial = new PrintedBook("9781234567890", "Test Book", "Test Author", 
                29.99, 2023, 300, "Test Publisher", false);
        decorator = new ExpeditedDeliveryDecorator(baseMaterial, 2);
    }
    
    @Test
    void testConstructorWithValidParameters() {
        assertNotNull(decorator);
        assertEquals(2, decorator.getDeliveryDays());
        assertEquals(baseMaterial, decorator.getDecoratedMaterial());
    }
    
    @Test
    void testConstructorWithNullMaterial() {
        assertThrows(NullPointerException.class, () -> {
            new ExpeditedDeliveryDecorator(null, 1);
        });
    }
    
    @Test
    void testConstructorWithInvalidDays() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ExpeditedDeliveryDecorator(baseMaterial, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ExpeditedDeliveryDecorator(baseMaterial, -1);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ExpeditedDeliveryDecorator(baseMaterial, -10);
        });
    }
    
    @Test
    void testGetPrice() {
        // 2-day delivery adds $15
        double expectedPrice = baseMaterial.getPrice() + 12.99;
        assertEquals(expectedPrice, decorator.getPrice(), 0.01);
    }
    
    @Test
    void testGetPriceOneDayDelivery() {
        ExpeditedDeliveryDecorator oneDayDelivery = new ExpeditedDeliveryDecorator(baseMaterial, 1);
        double expectedPrice = baseMaterial.getPrice() + 12.99;
        assertEquals(expectedPrice, oneDayDelivery.getPrice(), 0.01);
    }
    
    @Test
    void testGetPriceThreeDayDelivery() {
        ExpeditedDeliveryDecorator threeDayDelivery = new ExpeditedDeliveryDecorator(baseMaterial, 3);
        double expectedPrice = baseMaterial.getPrice() + 12.99;
        assertEquals(expectedPrice, threeDayDelivery.getPrice(), 0.01);
    }
    
    @Test
    void testGetPriceRegularDelivery() {
        ExpeditedDeliveryDecorator regularDelivery = new ExpeditedDeliveryDecorator(baseMaterial, 5);
        double expectedPrice = baseMaterial.getPrice() + 12.99;
        assertEquals(expectedPrice, regularDelivery.getPrice(), 0.01);
    }
    
    @Test
    void testGetDisplayInfo() {
        String displayInfo = decorator.getDisplayInfo();
        assertNotNull(displayInfo);
        assertTrue(displayInfo.contains("Expedited Delivery"));
        assertTrue(displayInfo.contains("2 days"));
    }
    
    @Test
    void testGetDeliveryCost() {
        assertEquals(12.99, decorator.getExpeditedDeliveryCost(), 0.01);
        
        ExpeditedDeliveryDecorator oneDayDelivery = new ExpeditedDeliveryDecorator(baseMaterial, 1);
        assertEquals(12.99, oneDayDelivery.getExpeditedDeliveryCost(), 0.01);
        
        ExpeditedDeliveryDecorator fiveDayDelivery = new ExpeditedDeliveryDecorator(baseMaterial, 7);
        assertEquals(12.99, fiveDayDelivery.getExpeditedDeliveryCost(), 0.01);
    }
    
    @Test
    void testWithVideoMaterial() {
        Material video = new VideoMaterial("V001", "Test Video", "Director", 24.99, 2023,
                120, "MP4", 2048.5, Media.MediaQuality.HIGH, 
                VideoMaterial.VideoType.TUTORIAL, "PG", java.util.Arrays.asList("Actor1"),
                true, "16:9");
        ExpeditedDeliveryDecorator videoDecorator = new ExpeditedDeliveryDecorator(video, 1);
        
        assertEquals(24.99 + 12.99, videoDecorator.getPrice(), 0.01);
        assertTrue(videoDecorator.getDisplayInfo().contains("1 day"));
    }
    
    @Test
    void testMultipleDeliveryOptions() {
        // Test stacking delivery options (shouldn't happen in real life, but testing edge case)
        ExpeditedDeliveryDecorator firstDelivery = new ExpeditedDeliveryDecorator(baseMaterial, 3);
        ExpeditedDeliveryDecorator secondDelivery = new ExpeditedDeliveryDecorator(firstDelivery, 1);
        
        double expectedPrice = baseMaterial.getPrice() + 12.99 + 12.99;
        assertEquals(expectedPrice, secondDelivery.getPrice(), 0.01);
    }
    
    @Test
    void testCombinedWithGiftWrapping() {
        GiftWrappingDecorator giftWrap = new GiftWrappingDecorator(baseMaterial, "Premium");
        ExpeditedDeliveryDecorator expedited = new ExpeditedDeliveryDecorator(giftWrap, 2);
        
        double expectedPrice = baseMaterial.getPrice() + 5.99 + 12.99;
        assertEquals(expectedPrice, expedited.getPrice(), 0.01);
    }
    
    @Test
    void testEquals() {
        ExpeditedDeliveryDecorator decorator2 = new ExpeditedDeliveryDecorator(baseMaterial, 2);
        ExpeditedDeliveryDecorator decorator3 = new ExpeditedDeliveryDecorator(baseMaterial, 3);
        
        // Decorators with same material and days should be equal
        assertEquals(decorator, decorator2);
        assertNotEquals(decorator, decorator3); // Different days
        assertEquals(decorator, decorator); // Same instance should equal itself
    }
    
    @Test
    void testHashCode() {
        ExpeditedDeliveryDecorator decorator2 = new ExpeditedDeliveryDecorator(baseMaterial, 2);
        assertEquals(decorator.hashCode(), decorator2.hashCode());
    }
    
    @Test
    void testToString() {
        String str = decorator.toString();
        assertNotNull(str);
        assertTrue(str.contains("ExpeditedDeliveryDecorator"));
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
    void testVariousDeliveryDays() {
        for (int days = 1; days <= 10; days++) {
            ExpeditedDeliveryDecorator testDecorator = new ExpeditedDeliveryDecorator(baseMaterial, days);
            assertEquals(days, testDecorator.getDeliveryDays());
            assertTrue(testDecorator.getPrice() > baseMaterial.getPrice());
        }
    }
}