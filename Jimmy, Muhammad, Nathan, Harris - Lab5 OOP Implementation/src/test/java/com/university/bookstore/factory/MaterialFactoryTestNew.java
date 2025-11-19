package com.university.bookstore.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.AudioBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Magazine;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.VideoMaterial;

class MaterialFactoryTestNew {
    
    @Test
    void testCreatePrintedBook() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("isbn", "9781234567890");
        properties.put("title", "Test Book");
        properties.put("author", "Test Author");
        properties.put("price", 29.99);
        properties.put("year", 2023);
        properties.put("pages", 300);
        properties.put("publisher", "Test Publisher");
        properties.put("hardcover", false);
        
        Material material = MaterialFactory.createMaterial("BOOK", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof PrintedBook);
        assertEquals("9781234567890", material.getId());
        assertEquals("Test Book", material.getTitle());
        assertEquals(29.99, material.getPrice(), 0.01);
    }
    
    @Test
    void testCreatePrintedBookAlternativeType() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("isbn", "9781234567890");
        properties.put("title", "Test Book");
        properties.put("author", "Test Author");
        properties.put("price", 29.99);
        properties.put("year", 2023);
        properties.put("pages", 300);
        properties.put("publisher", "Test Publisher");
        properties.put("hardcover", true);
        
        Material material = MaterialFactory.createMaterial("PRINTED_BOOK", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof PrintedBook);
        assertTrue(((PrintedBook) material).isHardcover());
    }
    
    @Test
    void testCreateEBook() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", "E001");
        properties.put("title", "Test EBook");
        properties.put("author", "E-Author");
        properties.put("price", 19.99);
        properties.put("year", 2023);
        properties.put("fileFormat", "PDF");
        properties.put("fileSize", 5.5);
        properties.put("drmEnabled", true);
        properties.put("wordCount", 25000);
        properties.put("quality", Media.MediaQuality.HIGH);
        
        Material material = MaterialFactory.createMaterial("EBOOK", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof EBook);
        assertEquals("E001", material.getId());
        assertEquals("Test EBook", material.getTitle());
        assertEquals(19.99, material.getPrice(), 0.01);
    }
    
    @Test
    void testCreateEBookAlternativeType() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", "E002");
        properties.put("title", "Alternative EBook");
        properties.put("author", "Author");
        properties.put("price", 15.99);
        properties.put("year", 2023);
        properties.put("fileFormat", "EPUB");
        properties.put("fileSize", 3.0);
        properties.put("drmEnabled", false);
        properties.put("wordCount", 20000);
        properties.put("quality", Media.MediaQuality.STANDARD);
        
        Material material = MaterialFactory.createMaterial("E_BOOK", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof EBook);
        assertFalse(((EBook) material).isDrmEnabled());
    }
    
    @Test
    void testCreateAudioBook() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("isbn", "9781111111111");
        properties.put("title", "Test AudioBook");
        properties.put("author", "Audio Author");
        properties.put("narrator", "Famous Narrator");
        properties.put("price", 39.99);
        properties.put("year", 2023);
        properties.put("duration", 480);
        properties.put("format", "MP3");
        properties.put("fileSize", 120.5);
        properties.put("quality", Media.MediaQuality.STANDARD);
        properties.put("language", "English");
        properties.put("unabridged", true);
        
        Material material = MaterialFactory.createMaterial("AUDIOBOOK", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof AudioBook);
        assertEquals("9781111111111", material.getId());
        assertEquals("Test AudioBook", material.getTitle());
    }
    
    @Test
    void testCreateVideoMaterial() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", "V001");
        properties.put("title", "Test Video");
        properties.put("director", "Test Director");
        properties.put("price", 24.99);
        properties.put("year", 2023);
        properties.put("duration", 120);
        properties.put("format", "MP4");
        properties.put("fileSize", 2048.0);
        properties.put("quality", Media.MediaQuality.HIGH);
        properties.put("videoType", VideoMaterial.VideoType.TUTORIAL);
        properties.put("rating", "PG");
        properties.put("cast", Arrays.asList("Actor1", "Actor2"));
        properties.put("subtitlesAvailable", true);
        properties.put("aspectRatio", "16:9");
        
        Material material = MaterialFactory.createMaterial("VIDEO", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof VideoMaterial);
        assertEquals("V001", material.getId());
    }
    
    @Test
    void testCreateMagazine() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("issn", "12345678");
        properties.put("title", "Tech Magazine");
        properties.put("publisher", "Tech Publisher");
        properties.put("price", 9.99);
        properties.put("year", 2023);
        properties.put("issue", 42);
        properties.put("frequency", "Monthly");
        properties.put("category", "Technology");
        
        Material material = MaterialFactory.createMaterial("MAGAZINE", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof Magazine);
        assertEquals("12345678", material.getId());
        assertEquals("Tech Magazine", material.getTitle());
    }
    
    @Test
    void testCreateWithNullType() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", "ID001");
        properties.put("title", "Title");
        properties.put("price", 10.0);
        
        assertThrows(NullPointerException.class, () -> {
            MaterialFactory.createMaterial(null, properties);
        });
    }
    
    @Test
    void testCreateWithNullProperties() {
        assertThrows(NullPointerException.class, () -> {
            MaterialFactory.createMaterial("BOOK", null);
        });
    }
    
    @Test
    void testCreateWithEmptyType() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", "ID001");
        
        assertThrows(IllegalArgumentException.class, () -> {
            MaterialFactory.createMaterial("", properties);
        });
    }
    
    @Test
    void testCreateWithWhitespaceType() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", "ID001");
        
        assertThrows(IllegalArgumentException.class, () -> {
            MaterialFactory.createMaterial("   ", properties);
        });
    }
    
    @Test
    void testCreateWithInvalidType() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", "ID001");
        properties.put("title", "Title");
        properties.put("price", 10.0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            MaterialFactory.createMaterial("INVALID_TYPE", properties);
        });
    }
    
    @Test
    void testCreateWithMissingRequiredProperty() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("title", "Book Without ID");
        properties.put("price", 10.0);
        
        assertThrows(NullPointerException.class, () -> {
            MaterialFactory.createMaterial("BOOK", properties);
        });
    }
    
    @Test
    void testCreateWithInvalidPrice() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("isbn", "9781234567890");
        properties.put("title", "Book");
        properties.put("author", "Author");
        properties.put("price", -10.0);
        properties.put("year", 2023);
        properties.put("pages", 100);
        properties.put("publisher", "Publisher");
        properties.put("hardcover", false);
        
        assertThrows(IllegalArgumentException.class, () -> {
            MaterialFactory.createMaterial("BOOK", properties);
        });
    }
    
    @Test
    void testCreateWithCaseInsensitiveType() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("isbn", "9781234567890");
        properties.put("title", "Test Book");
        properties.put("author", "Author");
        properties.put("price", 29.99);
        properties.put("year", 2023);
        properties.put("pages", 300);
        properties.put("publisher", "Publisher");
        properties.put("hardcover", false);
        
        Material material1 = MaterialFactory.createMaterial("book", properties);
        Material material2 = MaterialFactory.createMaterial("Book", properties);
        Material material3 = MaterialFactory.createMaterial("BOOK", properties);
        
        assertNotNull(material1);
        assertNotNull(material2);
        assertNotNull(material3);
        assertTrue(material1 instanceof PrintedBook);
        assertTrue(material2 instanceof PrintedBook);
        assertTrue(material3 instanceof PrintedBook);
    }
    
    @Test
    void testCreateWithPartialProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("isbn", "9781234567890");
        properties.put("title", "Partial Book");
        properties.put("author", "Author");
        properties.put("price", 19.99);
        properties.put("year", 2023);
        properties.put("pages", 300);
        // Missing publisher, hardcover - should use defaults
        
        Material material = MaterialFactory.createMaterial("BOOK", properties);
        
        assertNotNull(material);
        assertTrue(material instanceof PrintedBook);
        PrintedBook book = (PrintedBook) material;
        assertEquals("9781234567890", book.getId());
        assertEquals("Partial Book", book.getTitle());
    }
    
    @Test
    void testCreateMultipleMaterials() {
        Map<String, Object> bookProps = new HashMap<>();
        bookProps.put("isbn", "9781234567890");
        bookProps.put("title", "Book");
        bookProps.put("author", "Author");
        bookProps.put("price", 29.99);
        bookProps.put("year", 2023);
        bookProps.put("pages", 300);
        bookProps.put("publisher", "Publisher");
        bookProps.put("hardcover", false);
        
        Map<String, Object> ebookProps = new HashMap<>();
        ebookProps.put("id", "E001");
        ebookProps.put("title", "EBook");
        ebookProps.put("author", "E-Author");
        ebookProps.put("price", 19.99);
        ebookProps.put("year", 2023);
        ebookProps.put("fileFormat", "PDF");
        ebookProps.put("fileSize", 5.5);
        ebookProps.put("drmEnabled", true);
        ebookProps.put("wordCount", 25000);
        ebookProps.put("quality", Media.MediaQuality.HIGH);
        
        Material book = MaterialFactory.createMaterial("BOOK", bookProps);
        Material ebook = MaterialFactory.createMaterial("EBOOK", ebookProps);
        
        assertNotNull(book);
        assertNotNull(ebook);
        assertNotEquals(book.getClass(), ebook.getClass());
        assertNotEquals(book.getId(), ebook.getId());
    }
    
    // Methods isValidMaterialType and getSupportedTypes don't exist in MaterialFactory
    // Removed these tests to fix compilation
}