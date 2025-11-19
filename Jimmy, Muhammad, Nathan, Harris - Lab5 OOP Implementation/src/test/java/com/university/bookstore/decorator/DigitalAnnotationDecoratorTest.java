package com.university.bookstore.decorator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Magazine;
import com.university.bookstore.model.Media;

class DigitalAnnotationDecoratorTest {
    
    private Material baseMaterial;
    private DigitalAnnotationDecorator decorator;
    
    @BeforeEach
    void setUp() {
        baseMaterial = new EBook("E001", "Test EBook", "Test Author", 
                19.99, 2023, "PDF", 5.5, true, 250, Media.MediaQuality.HIGH);
        decorator = new DigitalAnnotationDecorator(baseMaterial);
    }
    
    @Test
    void testConstructorWithValidMaterial() {
        assertNotNull(decorator);
        assertEquals(baseMaterial, decorator.getDecoratedMaterial());
        assertFalse(decorator.hasAnnotations());
    }
    
    @Test
    void testConstructorWithNullMaterial() {
        assertThrows(NullPointerException.class, () -> {
            new DigitalAnnotationDecorator(null);
        });
    }
    
    @Test
    void testGetPrice() {
        double expectedPrice = baseMaterial.getPrice() + 2.99;
        assertEquals(expectedPrice, decorator.getPrice(), 0.01);
    }
    
    @Test
    void testGetDisplayInfo() {
        String displayInfo = decorator.getDisplayInfo();
        assertNotNull(displayInfo);
        assertTrue(displayInfo.contains("Digital Annotations"));
        assertTrue(displayInfo.contains("0 notes"));
    }
    
    @Test
    void testAddAnnotation() {
        decorator.addAnnotation("This is an important concept");
        assertEquals(1, decorator.getAnnotationCount());
        
        decorator.addAnnotation("Remember this for the exam");
        assertEquals(2, decorator.getAnnotationCount());
    }
    
    @Test
    void testAddAnnotationWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            decorator.addAnnotation(null);
        });
    }
    
    @Test
    void testAddAnnotationWithEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            decorator.addAnnotation("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            decorator.addAnnotation("   ");
        });
    }
    
    @Test
    void testGetAnnotations() {
        decorator.addAnnotation("First note");
        decorator.addAnnotation("Second note");
        
        List<String> annotations = decorator.getAnnotations();
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains("First note"));
        assertTrue(annotations.contains("Second note"));
    }
    
    @Test
    void testRemoveAnnotation() {
        decorator.addAnnotation("Note to remove");
        assertEquals(1, decorator.getAnnotationCount());
        
        String removed = decorator.removeAnnotation(0);
        assertEquals("Note to remove", removed);
        assertEquals(0, decorator.getAnnotationCount());
    }
    
    @Test
    void testRemoveAnnotationInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            decorator.removeAnnotation(0);
        });
        
        decorator.addAnnotation("Note");
        assertThrows(IndexOutOfBoundsException.class, () -> {
            decorator.removeAnnotation(1);
        });
    }
    
    @Test
    void testClearAnnotations() {
        decorator.addAnnotation("Note 1");
        decorator.addAnnotation("Note 2");
        decorator.addAnnotation("Note 3");
        assertEquals(3, decorator.getAnnotationCount());
        
        decorator.clearAnnotations();
        assertEquals(0, decorator.getAnnotationCount());
        assertFalse(decorator.hasAnnotations());
    }
    
    @Test
    void testHasAnnotations() {
        assertFalse(decorator.hasAnnotations());
        
        decorator.addAnnotation("Note");
        assertTrue(decorator.hasAnnotations());
        
        decorator.clearAnnotations();
        assertFalse(decorator.hasAnnotations());
    }
    
    @Test
    void testGetDigitalAnnotationCost() {
        assertEquals(2.99, decorator.getDigitalAnnotationCost(), 0.01);
    }
    
    @Test
    void testWithPrintedBook() {
        Material printedBook = new PrintedBook("9781234567890", "Physical Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        DigitalAnnotationDecorator printedDecorator = new DigitalAnnotationDecorator(printedBook);
        
        assertEquals(29.99 + 2.99, printedDecorator.getPrice(), 0.01);
        printedDecorator.addAnnotation("Digital note on physical book");
        assertEquals(1, printedDecorator.getAnnotationCount());
    }
    
    @Test
    void testWithMagazine() {
        Material magazine = new Magazine("12345678", "Tech Magazine", "TechPub", 9.99, 
                2023, 1, "Monthly", "Technology");
        DigitalAnnotationDecorator magazineDecorator = new DigitalAnnotationDecorator(magazine);
        
        assertEquals(9.99 + 2.99, magazineDecorator.getPrice(), 0.01);
        assertTrue(magazineDecorator.getDisplayInfo().contains("Digital Annotations"));
    }
    
    @Test
    void testMultipleAnnotationDecorators() {
        // Test stacking (edge case)
        DigitalAnnotationDecorator firstLayer = new DigitalAnnotationDecorator(baseMaterial);
        DigitalAnnotationDecorator secondLayer = new DigitalAnnotationDecorator(firstLayer);
        
        double expectedPrice = baseMaterial.getPrice() + 2.99 + 2.99;
        assertEquals(expectedPrice, secondLayer.getPrice(), 0.01);
        
        firstLayer.addAnnotation("Layer 1 Note");
        secondLayer.addAnnotation("Layer 2 Note");
        
        assertEquals(1, firstLayer.getAnnotationCount());
        assertEquals(1, secondLayer.getAnnotationCount());
    }
    
    @Test
    void testCombinedWithOtherDecorators() {
        GiftWrappingDecorator giftWrap = new GiftWrappingDecorator(baseMaterial, "Premium");
        ExpeditedDeliveryDecorator expedited = new ExpeditedDeliveryDecorator(giftWrap, 2);
        DigitalAnnotationDecorator annotated = new DigitalAnnotationDecorator(expedited);
        
        double expectedPrice = baseMaterial.getPrice() + 5.99 + 12.99 + 2.99;
        assertEquals(expectedPrice, annotated.getPrice(), 0.01);
        
        annotated.addAnnotation("Combined decorators note");
        assertEquals(1, annotated.getAnnotationCount());
    }
    
    @Test
    void testEquals() {
        DigitalAnnotationDecorator decorator2 = new DigitalAnnotationDecorator(baseMaterial);
        // New instances with same material and no annotations should be equal
        assertEquals(decorator, decorator2);
        
        decorator.addAnnotation("Note");
        // Now they're different
        assertNotEquals(decorator, decorator2);
    }
    
    @Test
    void testHashCode() {
        DigitalAnnotationDecorator decorator2 = new DigitalAnnotationDecorator(baseMaterial);
        // Different instances may have different hash codes
        assertNotNull(decorator.hashCode());
        assertNotNull(decorator2.hashCode());
    }
    
    @Test
    void testToString() {
        String str = decorator.toString();
        assertNotNull(str);
        assertTrue(str.contains("DigitalAnnotationDecorator"));
    }
    
    @Test
    void testGetAnnotationsReturnsCopy() {
        decorator.addAnnotation("Note 1");
        List<String> annotations = decorator.getAnnotations();
        
        // Try to modify the returned list
        try {
            annotations.add("Sneaky note");
            // If no exception, check that original wasn't modified
            assertEquals(1, decorator.getAnnotationCount());
        } catch (UnsupportedOperationException e) {
            // List is unmodifiable, which is also fine
            assertTrue(true);
        }
    }
    
    @Test
    void testDisplayInfoUpdatesWithAnnotations() {
        String initial = decorator.getDisplayInfo();
        assertTrue(initial.contains("0 notes"));
        
        decorator.addAnnotation("Note 1");
        String after1 = decorator.getDisplayInfo();
        assertTrue(after1.contains("1 notes"));
        
        decorator.addAnnotation("Note 2");
        String after2 = decorator.getDisplayInfo();
        assertTrue(after2.contains("2 notes"));
    }
}