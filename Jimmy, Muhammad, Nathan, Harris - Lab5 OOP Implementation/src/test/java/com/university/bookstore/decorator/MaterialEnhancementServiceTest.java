package com.university.bookstore.decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.AudioBook;
import com.university.bookstore.model.VideoMaterial;
import com.university.bookstore.model.Magazine;
import com.university.bookstore.model.Media;
import java.util.Arrays;

class MaterialEnhancementServiceTest {
    
    private MaterialEnhancementService service;
    private Material printedBook;
    private Material ebook;
    private Material audioBook;
    private Material video;
    private Material magazine;
    
    @BeforeEach
    void setUp() {
        service = new MaterialEnhancementService();
        
        printedBook = new PrintedBook("9781234567890", "Test Book", "Test Author", 
                29.99, 2023, 300, "Test Publisher", false);
        
        ebook = new EBook("E001", "Test EBook", "EBook Author", 
                19.99, 2023, "PDF", 5.5, true, 250, Media.MediaQuality.HIGH);
        
        audioBook = new AudioBook("9781111111111", "Test AudioBook", "Audio Author", "Narrator",
                39.99, 2023, 480, "MP3", 120.5, Media.MediaQuality.STANDARD, "English", true);
        
        video = new VideoMaterial("V001", "Test Video", "Director", 24.99, 2023,
                120, "MP4", 2048.5, Media.MediaQuality.HIGH, 
                VideoMaterial.VideoType.TUTORIAL, "PG", Arrays.asList("Actor1"),
                true, "16:9");
        
        magazine = new Magazine("12345678", "Tech Magazine", "TechPub", 9.99, 
                2023, 1, "Monthly", "Technology");
    }
    
    @Test
    void testAddGiftWrappingWithValidParameters() {
        Material enhanced = service.addGiftWrapping(printedBook, "Premium");
        assertNotNull(enhanced);
        assertTrue(enhanced instanceof GiftWrappingDecorator);
        assertEquals(printedBook.getPrice() + 5.99, enhanced.getPrice(), 0.01);
    }
    
    @Test
    void testAddGiftWrappingWithNullMaterial() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addGiftWrapping(null, "Premium");
        });
    }
    
    @Test
    void testAddGiftWrappingWithNullStyle() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addGiftWrapping(printedBook, null);
        });
    }
    
    @Test
    void testAddGiftWrappingWithEmptyStyle() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addGiftWrapping(printedBook, "");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.addGiftWrapping(printedBook, "   ");
        });
    }
    
    @Test
    void testAddExpeditedDeliveryWithValidParameters() {
        Material enhanced = service.addExpeditedDelivery(printedBook, 2);
        assertNotNull(enhanced);
        assertTrue(enhanced instanceof ExpeditedDeliveryDecorator);
        assertEquals(printedBook.getPrice() + 12.99, enhanced.getPrice(), 0.01);
    }
    
    @Test
    void testAddExpeditedDeliveryWithNullMaterial() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addExpeditedDelivery(null, 2);
        });
    }
    
    @Test
    void testAddExpeditedDeliveryWithInvalidDays() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addExpeditedDelivery(printedBook, 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.addExpeditedDelivery(printedBook, -1);
        });
    }
    
    @Test
    void testAddDigitalAnnotationsWithValidMaterial() {
        Material enhanced = service.addDigitalAnnotations(ebook);
        assertNotNull(enhanced);
        assertTrue(enhanced instanceof DigitalAnnotationDecorator);
        assertEquals(ebook.getPrice() + 2.99, enhanced.getPrice(), 0.01);
    }
    
    @Test
    void testAddDigitalAnnotationsWithNullMaterial() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addDigitalAnnotations(null);
        });
    }
    
    @Test
    void testCreatePremiumPackageWithEBook() {
        Material premium = service.createPremiumPackage(ebook, "Deluxe", 1);
        assertNotNull(premium);
        
        // Should have gift wrapping + expedited delivery + digital annotations
        double expectedPrice = ebook.getPrice() + 5.99 + 12.99 + 2.99;
        assertEquals(expectedPrice, premium.getPrice(), 0.01);
        
        // Verify it's wrapped in correct decorators
        assertTrue(premium instanceof DigitalAnnotationDecorator);
    }
    
    @Test
    void testCreatePremiumPackageWithPrintedBook() {
        Material premium = service.createPremiumPackage(printedBook, "Standard", 3);
        assertNotNull(premium);
        
        // Should have gift wrapping + expedited delivery (no digital annotations for printed)
        double expectedPrice = printedBook.getPrice() + 5.99 + 12.99;
        assertEquals(expectedPrice, premium.getPrice(), 0.01);
        
        // Verify it's wrapped in correct decorators
        assertTrue(premium instanceof ExpeditedDeliveryDecorator);
    }
    
    @Test
    void testCreatePremiumPackageWithNullMaterial() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createPremiumPackage(null, "Premium", 2);
        });
    }
    
    @Test
    void testCreatePremiumPackageWithInvalidStyle() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createPremiumPackage(printedBook, null, 2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.createPremiumPackage(printedBook, "", 2);
        });
    }
    
    @Test
    void testCreatePremiumPackageWithInvalidDays() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createPremiumPackage(printedBook, "Premium", 0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.createPremiumPackage(printedBook, "Premium", -1);
        });
    }
    
    @Test
    void testCreateGiftPackage() {
        Material giftPackage = service.createGiftPackage(audioBook, "Birthday", 2);
        assertNotNull(giftPackage);
        
        // Should have gift wrapping + expedited delivery
        double expectedPrice = audioBook.getPrice() + 5.99 + 12.99;
        assertEquals(expectedPrice, giftPackage.getPrice(), 0.01);
    }
    
    @Test
    void testCreateDigitalPackage() {
        Material digitalPackage = service.createDigitalPackage(ebook);
        assertNotNull(digitalPackage);
        
        // Should have digital annotations
        double expectedPrice = ebook.getPrice() + 2.99;
        assertEquals(expectedPrice, digitalPackage.getPrice(), 0.01);
    }
    
    @Test
    void testCalculateEnhancementCost() {
        Material enhanced = service.createPremiumPackage(ebook, "Deluxe", 1);
        double cost = service.calculateEnhancementCost(ebook, enhanced);
        
        // Gift wrapping + 1-day delivery + digital annotations
        double expectedCost = 5.99 + 12.99 + 2.99;
        assertEquals(expectedCost, cost, 0.01);
    }
    
    @Test
    void testCalculateEnhancementCostSameMaterial() {
        double cost = service.calculateEnhancementCost(printedBook, printedBook);
        assertEquals(0.0, cost, 0.01);
    }
    
    @Test
    void testGetEnhancementSummary() {
        Material enhanced = service.createPremiumPackage(ebook, "Deluxe", 2);
        String summary = service.getEnhancementSummary(enhanced);
        
        assertNotNull(summary);
        assertTrue(summary.contains("Enhancements"));
        assertTrue(summary.contains("Total Cost"));
    }
    
    @Test
    void testGetEnhancementSummaryForBaseMaterial() {
        String summary = service.getEnhancementSummary(printedBook);
        assertNotNull(summary);
        assertTrue(summary.contains("No enhancements"));
    }
    
    @Test
    void testGetBasePrice() {
        Material enhanced = service.createPremiumPackage(ebook, "Deluxe", 1);
        double basePrice = service.getBasePrice(enhanced);
        
        assertEquals(ebook.getPrice(), basePrice, 0.01);
    }
    
    @Test
    void testGetBasePriceForBaseMaterial() {
        double basePrice = service.getBasePrice(printedBook);
        assertEquals(printedBook.getPrice(), basePrice, 0.01);
    }
    
    @Test
    void testHasEnhancements() {
        Material enhanced = service.addGiftWrapping(printedBook, "Premium");
        assertTrue(service.hasEnhancements(enhanced));
        assertFalse(service.hasEnhancements(printedBook));
    }
    
    @Test
    void testHasEnhancementsWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.hasEnhancements(null);
        });
    }
    
    @Test
    void testGetEnhancementCount() {
        Material step1 = service.addGiftWrapping(printedBook, "Premium");
        assertEquals(1, service.getEnhancementCount(step1));
        
        Material step2 = service.addExpeditedDelivery(step1, 2);
        assertEquals(2, service.getEnhancementCount(step2));
        
        Material step3 = service.addDigitalAnnotations(step2);
        assertEquals(3, service.getEnhancementCount(step3));
        
        assertEquals(0, service.getEnhancementCount(printedBook));
    }
    
    @Test
    void testGetBaseMaterial() {
        Material enhanced = service.createPremiumPackage(ebook, "Deluxe", 1);
        Material base = service.getBaseMaterial(enhanced);
        
        assertEquals(ebook, base);
    }
    
    @Test
    void testGetBaseMaterialForBaseMaterial() {
        Material base = service.getBaseMaterial(printedBook);
        assertEquals(printedBook, base);
    }
    
    @Test
    void testHasEnhancement() {
        Material gifted = service.addGiftWrapping(printedBook, "Premium");
        assertTrue(service.hasEnhancement(gifted, GiftWrappingDecorator.class));
        assertFalse(service.hasEnhancement(gifted, ExpeditedDeliveryDecorator.class));
        
        Material combo = service.createPremiumPackage(ebook, "Deluxe", 1);
        assertTrue(service.hasEnhancement(combo, GiftWrappingDecorator.class));
        assertTrue(service.hasEnhancement(combo, ExpeditedDeliveryDecorator.class));
        assertTrue(service.hasEnhancement(combo, DigitalAnnotationDecorator.class));
    }
    
    @Test
    void testChainMultipleEnhancements() {
        Material step1 = service.addGiftWrapping(audioBook, "Birthday Special");
        Material step2 = service.addExpeditedDelivery(step1, 1);
        Material step3 = service.addDigitalAnnotations(step2);
        
        double expectedPrice = audioBook.getPrice() + 5.99 + 12.99 + 2.99;
        assertEquals(expectedPrice, step3.getPrice(), 0.01);
        assertEquals(3, service.getEnhancementCount(step3));
    }
    
    @Test
    void testEnhanceAllMaterialTypes() {
        // Test that all material types can be enhanced
        Material enhancedBook = service.addGiftWrapping(printedBook, "Standard");
        Material enhancedEbook = service.addDigitalAnnotations(ebook);
        Material enhancedAudio = service.addExpeditedDelivery(audioBook, 2);
        Material enhancedVideo = service.createPremiumPackage(video, "Premium", 1);
        Material enhancedMagazine = service.addGiftWrapping(magazine, "Simple");
        
        assertTrue(enhancedBook.getPrice() > printedBook.getPrice());
        assertTrue(enhancedEbook.getPrice() > ebook.getPrice());
        assertTrue(enhancedAudio.getPrice() > audioBook.getPrice());
        assertTrue(enhancedVideo.getPrice() > video.getPrice());
        assertTrue(enhancedMagazine.getPrice() > magazine.getPrice());
    }
    
    @Test
    void testToString() {
        String str = service.toString();
        assertNotNull(str);
        assertTrue(str.contains("MaterialEnhancementService"));
    }
}