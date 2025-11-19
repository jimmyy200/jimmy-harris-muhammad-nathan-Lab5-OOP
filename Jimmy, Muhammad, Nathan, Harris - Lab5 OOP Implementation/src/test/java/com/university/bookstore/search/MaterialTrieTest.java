package com.university.bookstore.search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Media;

class MaterialTrieTest {
    
    private MaterialTrie trie;
    
    @BeforeEach
    void setUp() {
        trie = new MaterialTrie();
    }
    
    @Test
    void testEmptyTrie() {
        assertTrue(trie.isEmpty());
        assertEquals(0, trie.size());
        
        List<Material> results = trie.searchByPrefix("test");
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testInsertAndSearch() {
        Material book = new PrintedBook("9781234567890", "Java Programming", "John Doe", 49.99, 2023, 500, "Tech Press", false);
        trie.insert(book);
        
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        
        List<Material> results = trie.searchByPrefix("java");
        assertEquals(1, results.size());
        assertEquals(book, results.get(0));
    }
    
    @Test
    void testInsertNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            trie.insert(null);
        });
    }
    
    @Test
    void testCaseInsensitiveSearch() {
        Material book = new PrintedBook("9781234567890", "Java Programming", "John Doe", 49.99, 2023, 500, "Tech Press", false);
        trie.insert(book);
        
        assertEquals(1, trie.searchByPrefix("JAVA").size());
        assertEquals(1, trie.searchByPrefix("Java").size());
        assertEquals(1, trie.searchByPrefix("java").size());
        assertEquals(1, trie.searchByPrefix("JaVa").size());
    }
    
    @Test
    void testPrefixSearch() {
        Material book1 = new PrintedBook("9781234567890", "Java Programming", "John Doe", 49.99, 2023, 500, "Tech Press", false);
        Material book2 = new PrintedBook("9781234567891", "JavaScript Guide", "Jane Smith", 39.99, 2023, 400, "Web Press", false);
        Material book3 = new PrintedBook("9781234567892", "Python Basics", "Bob Johnson", 35.99, 2023, 350, "Code Press", false);
        
        trie.insert(book1);
        trie.insert(book2);
        trie.insert(book3);
        
        List<Material> javaResults = trie.searchByPrefix("java");
        assertEquals(2, javaResults.size()); // Java Programming and JavaScript Guide
        
        List<Material> pythonResults = trie.searchByPrefix("python");
        assertEquals(1, pythonResults.size());
        assertEquals(book3, pythonResults.get(0));
    }
    
    @Test
    void testSearchByPrefixWithLimit() {
        for (int i = 0; i < 10; i++) {
            Material book = new PrintedBook(
                "978" + String.format("%010d", i),
                "Java Book " + i,
                "Author " + i,
                29.99 + i,
                2023,
                300 + i * 10,
                "Publisher",
                false
            );
            trie.insert(book);
        }
        
        List<Material> results = trie.searchByPrefixWithLimit("java", 5);
        assertEquals(5, results.size());
        
        results = trie.searchByPrefixWithLimit("java", 20);
        assertEquals(10, results.size()); // Only 10 books exist
    }
    
    @Test
    void testSearchWithSpaces() {
        Material book = new PrintedBook("9781234567890", "Data Structures and Algorithms", "Author", 59.99, 2023, 600, "Edu Press", false);
        trie.insert(book);
        
        List<Material> results = trie.searchByPrefix("data");
        assertEquals(1, results.size());
        
        results = trie.searchByPrefix("data ");
        assertEquals(1, results.size());
        
        results = trie.searchByPrefix("data structures");
        assertEquals(1, results.size());
    }
    
    @Test
    void testSearchNullPrefix() {
        List<Material> results = trie.searchByPrefix(null);
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testSearchEmptyPrefix() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 29.99, 2023, 300, "Press", false);
        trie.insert(book);
        
        List<Material> results = trie.searchByPrefix("");
        assertTrue(results.isEmpty());
        
        results = trie.searchByPrefix("   ");
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testHasPrefix() {
        Material book = new PrintedBook("9781234567890", "Java Programming", "Author", 49.99, 2023, 500, "Press", false);
        trie.insert(book);
        
        assertTrue(trie.hasPrefix("java"));
        assertTrue(trie.hasPrefix("jav"));
        assertTrue(trie.hasPrefix("j"));
        assertTrue(trie.hasPrefix("java programming"));
        
        assertFalse(trie.hasPrefix("python"));
        assertFalse(trie.hasPrefix("javascript"));
    }
    
    @Test
    void testHasPrefixWithNullOrEmpty() {
        assertFalse(trie.hasPrefix(null));
        assertFalse(trie.hasPrefix(""));
        assertFalse(trie.hasPrefix("   "));
    }
    
    @Test
    void testGetAllMaterials() {
        Material book1 = new PrintedBook("9781234567890", "Book 1", "Author1", 29.99, 2023, 300, "Press1", false);
        Material book2 = new EBook("E001", "EBook 1", "Author2", 19.99, 2023, "PDF", 5.5, true, 100, Media.MediaQuality.HIGH);
        Material book3 = new PrintedBook("9781234567892", "Book 3", "Author3", 39.99, 2023, 400, "Press3", false);
        
        trie.insert(book1);
        trie.insert(book2);
        trie.insert(book3);
        
        List<Material> all = trie.getAllMaterials();
        assertEquals(3, all.size());
        assertTrue(all.contains(book1));
        assertTrue(all.contains(book2));
        assertTrue(all.contains(book3));
    }
    
    @Test
    void testRemove() {
        Material book1 = new PrintedBook("9781234567890", "Java Book", "Author1", 49.99, 2023, 500, "Press1", false);
        Material book2 = new PrintedBook("9781234567891", "Python Book", "Author2", 39.99, 2023, 400, "Press2", false);
        
        trie.insert(book1);
        trie.insert(book2);
        assertEquals(2, trie.size());
        
        assertTrue(trie.remove(book1));
        assertEquals(1, trie.size());
        
        List<Material> javaResults = trie.searchByPrefix("java");
        assertTrue(javaResults.isEmpty());
        
        List<Material> pythonResults = trie.searchByPrefix("python");
        assertEquals(1, pythonResults.size());
    }
    
    @Test
    void testRemoveNonExistent() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 29.99, 2023, 300, "Press", false);
        assertFalse(trie.remove(book));
    }
    
    @Test
    void testRemoveNull() {
        assertFalse(trie.remove(null));
    }
    
    @Test
    void testClear() {
        Material book1 = new PrintedBook("9781234567890", "Book 1", "Author1", 29.99, 2023, 300, "Press1", false);
        Material book2 = new PrintedBook("9781234567891", "Book 2", "Author2", 39.99, 2023, 400, "Press2", false);
        
        trie.insert(book1);
        trie.insert(book2);
        assertEquals(2, trie.size());
        
        trie.clear();
        assertEquals(0, trie.size());
        assertTrue(trie.isEmpty());
        assertTrue(trie.searchByPrefix("book").isEmpty());
    }
    
    @Test
    void testMultipleWordsInTitle() {
        Material book = new PrintedBook("9781234567890", "Advanced Java Programming Concepts", "Expert", 69.99, 2023, 700, "Pro Press", false);
        trie.insert(book);
        
        assertEquals(1, trie.searchByPrefix("advanced").size());
        assertEquals(1, trie.searchByPrefix("advanced java").size());
        assertEquals(1, trie.searchByPrefix("advanced java programming").size());
        assertEquals(1, trie.searchByPrefix("advanced java programming concepts").size());
    }
    
    @Test
    void testDuplicateInsert() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 29.99, 2023, 300, "Press", false);
        
        trie.insert(book);
        assertEquals(1, trie.size());
        
        trie.insert(book); // Insert same book again
        assertEquals(2, trie.size()); // Size increases because it's added again
        
        List<Material> results = trie.searchByPrefix("test");
        assertEquals(2, results.size()); // Contains the book twice
    }
    
    @Test
    void testSearchWithPartialMatch() {
        Material book = new PrintedBook("9781234567890", "Complete Java Reference", "Author", 59.99, 2023, 800, "Press", false);
        trie.insert(book);
        
        assertTrue(trie.hasPrefix("com"));
        assertTrue(trie.hasPrefix("complete"));
        assertTrue(trie.hasPrefix("complete j"));
        assertTrue(trie.hasPrefix("complete java"));
        assertTrue(trie.hasPrefix("complete java r"));
        assertTrue(trie.hasPrefix("complete java reference"));
        
        assertEquals(1, trie.searchByPrefix("com").size());
        assertEquals(1, trie.searchByPrefix("complete").size());
        assertEquals(1, trie.searchByPrefix("complete java").size());
    }
    
    @Test
    void testSearchNonExistentPrefix() {
        Material book = new PrintedBook("9781234567890", "Java Book", "Author", 29.99, 2023, 300, "Press", false);
        trie.insert(book);
        
        List<Material> results = trie.searchByPrefix("python");
        assertTrue(results.isEmpty());
        
        results = trie.searchByPrefix("xyz");
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testLargeTrie() {
        // Test with many materials
        for (int i = 0; i < 1000; i++) {
            Material book = new PrintedBook(
                "978" + String.format("%010d", i),
                "Book Title " + i,
                "Author " + i,
                20.0 + (i % 50),
                2020 + (i % 5),
                200 + (i % 300),
                "Publisher " + (i % 10),
                i % 2 == 0
            );
            trie.insert(book);
        }
        
        assertEquals(1000, trie.size());
        
        // Search for specific book
        List<Material> results = trie.searchByPrefix("Book Title 500");
        assertEquals(1, results.size());
        assertEquals("Book Title 500", results.get(0).getTitle());
        
        // Search for common prefix
        results = trie.searchByPrefix("Book Title");
        assertEquals(1000, results.size());
    }
    
    @Test
    void testSpecialCharacters() {
        Material book1 = new PrintedBook("9781234567890", "C++ Programming", "Author", 49.99, 2023, 500, "Press", false);
        Material book2 = new PrintedBook("9781234567891", "C# Development", "Author", 39.99, 2023, 400, "Press", false);
        Material book3 = new PrintedBook("9781234567892", "Node.js Guide", "Author", 35.99, 2023, 350, "Press", false);
        
        trie.insert(book1);
        trie.insert(book2);
        trie.insert(book3);
        
        assertEquals(1, trie.searchByPrefix("c++").size());
        assertEquals(1, trie.searchByPrefix("c#").size());
        assertEquals(1, trie.searchByPrefix("node.js").size());
    }
    
    @Test
    void testSearchByPrefixWithLimitZero() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 29.99, 2023, 300, "Press", false);
        trie.insert(book);
        
        List<Material> results = trie.searchByPrefixWithLimit("test", 0);
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testSearchByPrefixWithNegativeLimit() {
        Material book = new PrintedBook("9781234567890", "Test Book", "Author", 29.99, 2023, 300, "Press", false);
        trie.insert(book);
        
        // Negative limit returns empty list
        List<Material> results = trie.searchByPrefixWithLimit("test", -5);
        assertTrue(results.isEmpty());
    }
}