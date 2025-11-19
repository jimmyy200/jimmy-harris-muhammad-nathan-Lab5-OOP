package com.university.bookstore.builder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Media;

class EBookBuilderTest {
    
    private EBookBuilder builder;
    
    @BeforeEach
    void setUp() {
        builder = new EBookBuilder();
    }
    
    @Test
    void testBuildWithAllValidParameters() {
        EBook ebook = builder
            .setId("E001")
            .setTitle("Test EBook")
            .setAuthor("Test Author")
            .setPrice(19.99)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(5.5)
            .setDrmEnabled(true)
            .setWordCount(50000)
            .setQuality(Media.MediaQuality.HIGH)
            .build();
        
        assertNotNull(ebook);
        assertEquals("E001", ebook.getId());
        assertEquals("Test EBook", ebook.getTitle());
        assertEquals("Test Author", ebook.getCreator());
        assertEquals(19.99, ebook.getPrice(), 0.01);
        assertEquals(2023, ebook.getYear());
        assertEquals("PDF", ebook.getFileFormat());
        assertEquals(5.5, ebook.getFileSize(), 0.01);
        assertTrue(ebook.isDrmEnabled());
        assertEquals(50000, ebook.getWordCount());
        assertEquals(Media.MediaQuality.HIGH, ebook.getQuality());
    }
    
    @Test
    void testBuildWithMinimalParameters() {
        EBook ebook = builder
            .setId("E002")
            .setTitle("Minimal EBook")
            .setAuthor("Author")
            .setPrice(9.99)
            .setYear(2023)
            .setFileFormat("EPUB")
            .setFileSize(2.0)
            .setDrmEnabled(false)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertNotNull(ebook);
        assertEquals("E002", ebook.getId());
        assertEquals("Minimal EBook", ebook.getTitle());
    }
    
    @Test
    void testSetId() {
        builder.setId("E123");
        EBook ebook = builder
            .setTitle("Book")
            .setAuthor("Author")
            .setPrice(10.0)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals("E123", ebook.getId());
    }
    
    @Test
    void testSetTitle() {
        builder.setTitle("Special Title");
        EBook ebook = builder
            .setId("E001")
            .setAuthor("Author")
            .setPrice(10.0)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals("Special Title", ebook.getTitle());
    }
    
    @Test
    void testSetAuthor() {
        builder.setAuthor("Famous Author");
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setPrice(10.0)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals("Famous Author", ebook.getCreator());
    }
    
    @Test
    void testSetPrice() {
        builder.setPrice(99.99);
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setAuthor("Author")
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals(99.99, ebook.getPrice(), 0.01);
    }
    
    @Test
    void testSetYear() {
        builder.setYear(2024);
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setAuthor("Author")
            .setPrice(10.0)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals(2024, ebook.getYear());
    }
    
    @Test
    void testSetFormat() {
        builder.setFileFormat("MOBI");
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setAuthor("Author")
            .setPrice(10.0)
            .setYear(2023)
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals("MOBI", ebook.getFileFormat());
    }
    
    @Test
    void testSetFileSize() {
        builder.setFileSize(10.5);
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setAuthor("Author")
            .setPrice(10.0)
            .setYear(2023)
            .setFileFormat("PDF")
            .setDrmEnabled(true)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals(10.5, ebook.getFileSize(), 0.01);
    }
    
    @Test
    void testSetDrmEnabled() {
        builder.setDrmEnabled(false);
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setAuthor("Author")
            .setPrice(10.0)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setWordCount(25000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertFalse(ebook.isDrmEnabled());
    }
    
    @Test
    void testSetWordCount() {
        builder.setWordCount(75000);
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setAuthor("Author")
            .setPrice(10.0)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertEquals(75000, ebook.getWordCount());
    }
    
    @Test
    void testSetQuality() {
        builder.setQuality(Media.MediaQuality.LOW);
        EBook ebook = builder
            .setId("E001")
            .setTitle("Book")
            .setAuthor("Author")
            .setPrice(10.0)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(1.0)
            .setDrmEnabled(true)
            .setWordCount(25000)
            .build();
        
        assertEquals(Media.MediaQuality.LOW, ebook.getQuality());
    }
    
    @Test
    void testChainedBuilding() {
        EBook ebook = new EBookBuilder()
            .setId("E999")
            .setTitle("Chained Book")
            .setAuthor("Chained Author")
            .setPrice(29.99)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(8.8)
            .setDrmEnabled(true)
            .setWordCount(100000)
            .setQuality(Media.MediaQuality.HIGH)
            .build();
        
        assertNotNull(ebook);
        assertEquals("E999", ebook.getId());
        assertEquals("Chained Book", ebook.getTitle());
        assertEquals("Chained Author", ebook.getCreator());
    }
    
    @Test
    void testBuildMultipleEBooks() {
        EBook ebook1 = builder
            .setId("E001")
            .setTitle("First Book")
            .setAuthor("Author 1")
            .setPrice(19.99)
            .setYear(2023)
            .setFileFormat("PDF")
            .setFileSize(5.5)
            .setDrmEnabled(true)
            .setWordCount(50000)
            .setQuality(Media.MediaQuality.HIGH)
            .build();
        
        // Reset builder for second book
        builder = new EBookBuilder();
        
        EBook ebook2 = builder
            .setId("E002")
            .setTitle("Second Book")
            .setAuthor("Author 2")
            .setPrice(29.99)
            .setYear(2024)
            .setFileFormat("EPUB")
            .setFileSize(3.5)
            .setDrmEnabled(false)
            .setWordCount(85000)
            .setQuality(Media.MediaQuality.STANDARD)
            .build();
        
        assertNotEquals(ebook1.getId(), ebook2.getId());
        assertNotEquals(ebook1.getTitle(), ebook2.getTitle());
    }
    
    @Test
    void testBuildWithInvalidPrice() {
        assertThrows(IllegalStateException.class, () -> {
            builder
                .setId("E001")
                .setTitle("Book")
                .setAuthor("Author")
                .setPrice(-10.0)
                .setYear(2023)
                .setFileFormat("PDF")
                .setFileSize(1.0)
                .setDrmEnabled(true)
                .setWordCount(25000)
                .setQuality(Media.MediaQuality.STANDARD)
                .build();
        });
    }
    
    @Test
    void testBuildWithInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> {
            builder
                .setId("E001")
                .setTitle("Book")
                .setAuthor("Author")
                .setPrice(10.0)
                .setYear(1000)
                .setFileFormat("PDF")
                .setFileSize(1.0)
                .setDrmEnabled(true)
                .setWordCount(25000)
                .setQuality(Media.MediaQuality.STANDARD)
                .build();
        });
    }
    
    @Test
    void testBuildWithNullValues() {
        assertThrows(IllegalStateException.class, () -> {
            builder
                .setId(null)
                .setTitle("Book")
                .setAuthor("Author")
                .setPrice(10.0)
                .setYear(2023)
                .setFileFormat("PDF")
                .setFileSize(1.0)
                .setDrmEnabled(true)
                .setWordCount(25000)
                .setQuality(Media.MediaQuality.STANDARD)
                .build();
        });
    }
}