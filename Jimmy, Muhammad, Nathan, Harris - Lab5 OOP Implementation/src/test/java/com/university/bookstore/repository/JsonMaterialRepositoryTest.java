package com.university.bookstore.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Media;

class JsonMaterialRepositoryTest {
    
    @TempDir
    Path tempDir;
    
    private JsonMaterialRepository repository;
    private String testFilePath;
    private String originalUserDir;
    
    @BeforeEach
    void setUp() throws IOException {
        // Save original user.dir
        originalUserDir = System.getProperty("user.dir");
        
        // Set user.dir to temp directory for testing
        System.setProperty("user.dir", tempDir.toString());
        
        // Create data directory
        Path dataDir = tempDir.resolve("data");
        Files.createDirectories(dataDir);
        
        // Use a unique file name for each test to avoid conflicts
        testFilePath = "test_materials_" + System.nanoTime() + ".json";
        
        // Delete any existing test file to ensure clean state
        Path testFile = dataDir.resolve(testFilePath);
        Files.deleteIfExists(testFile);
        
        repository = new JsonMaterialRepository(testFilePath);
    }
    
    @AfterEach
    void tearDown() {
        // Restore original user.dir
        System.setProperty("user.dir", originalUserDir);
    }
    
    @Test
    void testConstructorWithValidPath() {
        assertNotNull(repository);
        assertTrue(repository.getFilePath().contains("data"));
        assertTrue(repository.getFilePath().contains("test_materials"));
    }
    
    @Test
    void testConstructorWithNullPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonMaterialRepository(null);
        });
    }
    
    @Test
    void testConstructorWithEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonMaterialRepository("");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonMaterialRepository("   ");
        });
    }
    
    @Test
    void testConstructorWithPathTraversal() {
        assertThrows(SecurityException.class, () -> {
            new JsonMaterialRepository("../../etc/passwd");
        });
        
        assertThrows(SecurityException.class, () -> {
            new JsonMaterialRepository("../../../sensitive.json");
        });
        
        assertThrows(SecurityException.class, () -> {
            new JsonMaterialRepository("..\\..\\windows\\system32\\config.json");
        });
    }
    
    @Test
    void testConstructorWithLongPath() {
        String longPath = "a".repeat(300) + ".json";
        assertThrows(IllegalArgumentException.class, () -> {
            new JsonMaterialRepository(longPath);
        });
    }
    
    @Test
    void testSaveAndFindById() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        
        repository.save(book);
        
        Optional<Material> found = repository.findById("9781234567890");
        assertTrue(found.isPresent());
        assertEquals("Test Book", found.get().getTitle());
        assertEquals(29.99, found.get().getPrice(), 0.01);
    }
    
    @Test
    void testSaveNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }
    
    @Test
    void testSaveMultipleMaterials() {
        Material book1 = new PrintedBook("9781111111111", "Book 1", "Author1", 
                29.99, 2023, 300, "Publisher1", false);
        Material book2 = new PrintedBook("9782222222222", "Book 2", "Author2", 
                39.99, 2023, 400, "Publisher2", false);
        Material ebook = new EBook("E001", "EBook 1", "Author3", 
                19.99, 2023, "PDF", 5.5, true, 250, Media.MediaQuality.HIGH);
        
        repository.save(book1);
        repository.save(book2);
        repository.save(ebook);
        
        List<Material> all = repository.findAll();
        assertEquals(3, all.size());
    }
    
    @Test
    void testUpdateExistingMaterial() {
        Material book = new PrintedBook("9781234567890", "Original Title", "Author", 
                29.99, 2023, 300, "Publisher", false);
        repository.save(book);
        
        Material updatedBook = new PrintedBook("9781234567890", "Updated Title", "Author", 
                39.99, 2024, 350, "New Publisher", true);
        repository.save(updatedBook);
        
        List<Material> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals("Updated Title", all.get(0).getTitle());
        assertEquals(39.99, all.get(0).getPrice(), 0.01);
    }
    
    @Test
    void testFindByIdNull() {
        Optional<Material> result = repository.findById(null);
        assertFalse(result.isPresent());
    }
    
    @Test
    void testFindByIdEmpty() {
        Optional<Material> result = repository.findById("");
        assertFalse(result.isPresent());
        
        result = repository.findById("   ");
        assertFalse(result.isPresent());
    }
    
    @Test
    void testFindByIdNonExistent() {
        Optional<Material> result = repository.findById("NONEXISTENT");
        assertFalse(result.isPresent());
    }
    
    @Test
    void testFindAll() {
        List<Material> initial = repository.findAll();
        assertTrue(initial.isEmpty());
        
        Material book1 = new PrintedBook("9781111111111", "Book 1", "Author1", 
                29.99, 2023, 300, "Publisher1", false);
        Material book2 = new PrintedBook("9782222222222", "Book 2", "Author2", 
                39.99, 2023, 400, "Publisher2", false);
        
        repository.save(book1);
        repository.save(book2);
        
        List<Material> all = repository.findAll();
        assertEquals(2, all.size());
    }
    
    @Test
    void testDelete() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        repository.save(book);
        
        assertTrue(repository.exists("9781234567890"));
        
        boolean deleted = repository.delete("9781234567890");
        assertTrue(deleted);
        
        assertFalse(repository.exists("9781234567890"));
    }
    
    @Test
    void testDeleteNonExistent() {
        boolean deleted = repository.delete("NONEXISTENT");
        assertFalse(deleted);
    }
    
    @Test
    void testDeleteNull() {
        boolean deleted = repository.delete(null);
        assertFalse(deleted);
    }
    
    @Test
    void testDeleteEmpty() {
        boolean deleted = repository.delete("");
        assertFalse(deleted);
        
        deleted = repository.delete("   ");
        assertFalse(deleted);
    }
    
    @Test
    void testExists() {
        assertFalse(repository.exists("9781234567890"));
        
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        repository.save(book);
        
        assertTrue(repository.exists("9781234567890"));
    }
    
    @Test
    void testExistsNull() {
        assertFalse(repository.exists(null));
    }
    
    @Test
    void testExistsEmpty() {
        assertFalse(repository.exists(""));
        assertFalse(repository.exists("   "));
    }
    
    @Test
    void testCount() {
        assertEquals(0, repository.count());
        
        Material book1 = new PrintedBook("9781111111111", "Book 1", "Author1", 
                29.99, 2023, 300, "Publisher1", false);
        Material book2 = new PrintedBook("9782222222222", "Book 2", "Author2", 
                39.99, 2023, 400, "Publisher2", false);
        
        repository.save(book1);
        assertEquals(1, repository.count());
        
        repository.save(book2);
        assertEquals(2, repository.count());
        
        repository.delete("9781111111111");
        assertEquals(1, repository.count());
    }
    
    @Test
    void testDeleteAll() {
        Material book1 = new PrintedBook("9781111111111", "Book 1", "Author1", 
                29.99, 2023, 300, "Publisher1", false);
        Material book2 = new PrintedBook("9782222222222", "Book 2", "Author2", 
                39.99, 2023, 400, "Publisher2", false);
        
        repository.save(book1);
        repository.save(book2);
        assertEquals(2, repository.count());
        
        repository.deleteAll();
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }
    
    @Test
    void testDataFileExists() {
        assertFalse(repository.dataFileExists());
        
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        repository.save(book);
        
        assertTrue(repository.dataFileExists());
    }
    
    @Test
    void testGetDataFileSize() {
        assertEquals(0, repository.getDataFileSize());
        
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        repository.save(book);
        
        assertTrue(repository.getDataFileSize() > 0);
    }
    
    @Test
    void testGetFilePath() {
        String path = repository.getFilePath();
        assertNotNull(path);
        assertTrue(path.contains("data"));
        assertTrue(path.contains("test_materials"));
    }
    
    @Test
    void testPersistenceAcrossInstances() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        repository.save(book);
        
        // Create new repository instance with same file
        JsonMaterialRepository newRepository = new JsonMaterialRepository(testFilePath);
        
        Optional<Material> found = newRepository.findById("9781234567890");
        assertTrue(found.isPresent());
        assertEquals("Test Book", found.get().getTitle());
    }
    
    @Test
    void testComplexMaterialTypes() {
        Material book = new PrintedBook("9781234567890", "Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        Material ebook = new EBook("E001", "EBook", "Author", 
                19.99, 2023, "PDF", 5.5, true, 250, Media.MediaQuality.HIGH);
        
        repository.save(book);
        repository.save(ebook);
        
        List<Material> all = repository.findAll();
        assertEquals(2, all.size());
        
        // Verify both types are preserved
        Optional<Material> foundBook = repository.findById("9781234567890");
        Optional<Material> foundEbook = repository.findById("E001");
        
        assertTrue(foundBook.isPresent());
        assertTrue(foundEbook.isPresent());
        assertTrue(foundBook.get() instanceof PrintedBook);
        assertTrue(foundEbook.get() instanceof EBook);
    }
    
    @Test
    void testEmptyFileHandling() throws IOException {
        // Create an empty file
        Path emptyFile = tempDir.resolve("data").resolve("empty.json");
        Files.createFile(emptyFile);
        
        JsonMaterialRepository emptyRepo = new JsonMaterialRepository("empty.json");
        List<Material> materials = emptyRepo.findAll();
        assertTrue(materials.isEmpty());
        assertEquals(0, emptyRepo.count());
    }
    
    @Test
    void testCorruptedFileHandling() throws IOException {
        // Create a corrupted JSON file
        Path corruptedFile = tempDir.resolve("data").resolve("corrupted.json");
        Files.writeString(corruptedFile, "{ invalid json content ]");
        
        JsonMaterialRepository corruptedRepo = new JsonMaterialRepository("corrupted.json");
        
        // Should return empty list for corrupted file
        List<Material> materials = corruptedRepo.findAll();
        assertTrue(materials.isEmpty());
        
        // Should be able to save new data
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 
                29.99, 2023, 300, "Publisher", false);
        corruptedRepo.save(book);
        
        assertEquals(1, corruptedRepo.count());
    }
    
    @Test
    void testConcurrentModification() {
        Material book1 = new PrintedBook("9781111111111", "Book 1", "Author1", 
                29.99, 2023, 300, "Publisher1", false);
        Material book2 = new PrintedBook("9782222222222", "Book 2", "Author2", 
                39.99, 2023, 400, "Publisher2", false);
        
        repository.save(book1);
        repository.save(book2);
        
        List<Material> materials = repository.findAll();
        
        // Modify collection while iterating (should not throw)
        assertDoesNotThrow(() -> {
            for (Material m : materials) {
                if (m.getId().equals("9781111111111")) {
                    repository.delete(m.getId());
                }
            }
        });
    }
}