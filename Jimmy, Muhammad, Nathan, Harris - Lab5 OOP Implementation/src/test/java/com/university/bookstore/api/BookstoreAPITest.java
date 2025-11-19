package com.university.bookstore.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.university.bookstore.impl.BookstoreArrayList;
import com.university.bookstore.model.Book;

class BookstoreAPITest {
    
    private BookstoreAPI bookstore;
    
    @BeforeEach
    void setUp() {
        bookstore = new BookstoreArrayList();
    }
    
    @Test
    void testAddBook() {
        Book book = new Book("9781234567890", "Test Book", "Test Author", 29.99, 2023);
        
        assertTrue(bookstore.add(book));
        assertEquals(1, bookstore.size());
        
        // Test adding duplicate ISBN
        Book duplicate = new Book("9781234567890", "Different Title", "Different Author", 39.99, 2024);
        assertFalse(bookstore.add(duplicate));
        assertEquals(1, bookstore.size());
    }
    
    @Test
    void testAddNullBook() {
        assertFalse(bookstore.add(null));
        assertEquals(0, bookstore.size());
    }
    
    @Test
    void testRemoveByIsbn() {
        Book book1 = new Book("9781234567890", "Book 1", "Author 1", 29.99, 2023);
        Book book2 = new Book("9780987654321", "Book 2", "Author 2", 39.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        assertEquals(2, bookstore.size());
        
        assertTrue(bookstore.removeByIsbn("9781234567890"));
        assertEquals(1, bookstore.size());
        
        assertFalse(bookstore.removeByIsbn("9781234567890")); // Already removed
        assertEquals(1, bookstore.size());
        
        assertFalse(bookstore.removeByIsbn("NONEXISTENT"));
        assertEquals(1, bookstore.size());
    }
    
    @Test
    void testRemoveByIsbnNull() {
        Book book = new Book("9781234567890", "Test Book", "Author", 29.99, 2023);
        bookstore.add(book);
        
        assertFalse(bookstore.removeByIsbn(null));
        assertEquals(1, bookstore.size());
    }
    
    @Test
    void testFindByIsbn() {
        Book book1 = new Book("9781234567890", "Book 1", "Author 1", 29.99, 2023);
        Book book2 = new Book("9780987654321", "Book 2", "Author 2", 39.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        
        Book found = bookstore.findByIsbn("9781234567890");
        assertNotNull(found);
        assertEquals("Book 1", found.getTitle());
        
        assertNull(bookstore.findByIsbn("NONEXISTENT"));
        assertNull(bookstore.findByIsbn(null));
    }
    
    @Test
    void testFindByTitle() {
        Book book1 = new Book("9781234567890", "Java Programming", "Author 1", 49.99, 2023);
        Book book2 = new Book("9780987654321", "Python Programming", "Author 2", 39.99, 2023);
        Book book3 = new Book("9781111111111", "Java for Beginners", "Author 3", 29.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        bookstore.add(book3);
        
        List<Book> javaBooks = bookstore.findByTitle("Java");
        assertEquals(2, javaBooks.size());
        
        List<Book> programmingBooks = bookstore.findByTitle("Programming");
        assertEquals(2, programmingBooks.size());
        
        List<Book> pythonBooks = bookstore.findByTitle("python"); // Case insensitive
        assertEquals(1, pythonBooks.size());
        
        List<Book> notFound = bookstore.findByTitle("Nonexistent");
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByTitleNull() {
        List<Book> result = bookstore.findByTitle(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByAuthor() {
        Book book1 = new Book("9781234567890", "Book 1", "John Doe", 29.99, 2023);
        Book book2 = new Book("9780987654321", "Book 2", "Jane Smith", 39.99, 2023);
        Book book3 = new Book("9781111111111", "Book 3", "John Smith", 49.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        bookstore.add(book3);
        
        List<Book> johnBooks = bookstore.findByAuthor("John");
        assertEquals(2, johnBooks.size());
        
        List<Book> smithBooks = bookstore.findByAuthor("Smith");
        assertEquals(2, smithBooks.size());
        
        List<Book> janeBooks = bookstore.findByAuthor("jane"); // Case insensitive
        assertEquals(1, janeBooks.size());
        
        List<Book> notFound = bookstore.findByAuthor("Nonexistent");
        assertTrue(notFound.isEmpty());
    }
    
    @Test
    void testFindByAuthorNull() {
        List<Book> result = bookstore.findByAuthor(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testFindByPriceRange() {
        Book book1 = new Book("9781234567890", "Cheap Book", "Author", 9.99, 2023);
        Book book2 = new Book("9780987654321", "Medium Book", "Author", 29.99, 2023);
        Book book3 = new Book("9781111111111", "Expensive Book", "Author", 99.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        bookstore.add(book3);
        
        List<Book> cheapBooks = bookstore.findByPriceRange(0, 20);
        assertEquals(1, cheapBooks.size());
        
        List<Book> midRangeBooks = bookstore.findByPriceRange(20, 50);
        assertEquals(1, midRangeBooks.size());
        
        List<Book> allBooks = bookstore.findByPriceRange(0, 100);
        assertEquals(3, allBooks.size());
        
        List<Book> noneInRange = bookstore.findByPriceRange(200, 300);
        assertTrue(noneInRange.isEmpty());
    }
    
    @Test
    void testFindByPriceRangeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookstore.findByPriceRange(50, 20); // min > max
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            bookstore.findByPriceRange(-10, 50); // negative price
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            bookstore.findByPriceRange(10, -50); // negative price
        });
    }
    
    @Test
    void testFindByYear() {
        Book book1 = new Book("9781234567890", "Old Book", "Author", 29.99, 2020);
        Book book2 = new Book("9780987654321", "Recent Book 1", "Author", 39.99, 2023);
        Book book3 = new Book("9781111111111", "Recent Book 2", "Author", 49.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        bookstore.add(book3);
        
        List<Book> oldBooks = bookstore.findByYear(2020);
        assertEquals(1, oldBooks.size());
        
        List<Book> recentBooks = bookstore.findByYear(2023);
        assertEquals(2, recentBooks.size());
        
        List<Book> noBooks = bookstore.findByYear(2019);
        assertTrue(noBooks.isEmpty());
    }
    
    @Test
    void testSize() {
        assertEquals(0, bookstore.size());
        
        bookstore.add(new Book("9781234567890", "Book 1", "Author", 29.99, 2023));
        assertEquals(1, bookstore.size());
        
        bookstore.add(new Book("9780987654321", "Book 2", "Author", 39.99, 2023));
        assertEquals(2, bookstore.size());
        
        bookstore.removeByIsbn("9781234567890");
        assertEquals(1, bookstore.size());
    }
    
    @Test
    void testInventoryValue() {
        assertEquals(0.0, bookstore.inventoryValue(), 0.01);
        
        bookstore.add(new Book("9781234567890", "Book 1", "Author", 10.00, 2023));
        assertEquals(10.00, bookstore.inventoryValue(), 0.01);
        
        bookstore.add(new Book("9780987654321", "Book 2", "Author", 20.00, 2023));
        assertEquals(30.00, bookstore.inventoryValue(), 0.01);
        
        bookstore.add(new Book("9781111111111", "Book 3", "Author", 15.50, 2023));
        assertEquals(45.50, bookstore.inventoryValue(), 0.01);
    }
    
    @Test
    void testGetMostExpensive() {
        assertNull(bookstore.getMostExpensive());
        
        Book cheap = new Book("9781234567890", "Cheap", "Author", 10.00, 2023);
        Book medium = new Book("9780987654321", "Medium", "Author", 50.00, 2023);
        Book expensive = new Book("9781111111111", "Expensive", "Author", 100.00, 2023);
        
        bookstore.add(cheap);
        bookstore.add(medium);
        bookstore.add(expensive);
        
        Book mostExpensive = bookstore.getMostExpensive();
        assertNotNull(mostExpensive);
        assertEquals("Expensive", mostExpensive.getTitle());
        assertEquals(100.00, mostExpensive.getPrice(), 0.01);
    }
    
    @Test
    void testGetMostRecent() {
        assertNull(bookstore.getMostRecent());
        
        Book old = new Book("9781234567890", "Old", "Author", 29.99, 2018);
        Book medium = new Book("9780987654321", "Medium", "Author", 39.99, 2020);
        Book recent = new Book("9781111111111", "Recent", "Author", 49.99, 2024);
        
        bookstore.add(old);
        bookstore.add(medium);
        bookstore.add(recent);
        
        Book mostRecent = bookstore.getMostRecent();
        assertNotNull(mostRecent);
        assertEquals("Recent", mostRecent.getTitle());
        assertEquals(2024, mostRecent.getYear());
    }
    
    @Test
    void testSnapshotArray() {
        Book[] emptyArray = bookstore.snapshotArray();
        assertNotNull(emptyArray);
        assertEquals(0, emptyArray.length);
        
        Book book1 = new Book("9781234567890", "Book 1", "Author", 29.99, 2023);
        Book book2 = new Book("9780987654321", "Book 2", "Author", 39.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        
        Book[] snapshot = bookstore.snapshotArray();
        assertEquals(2, snapshot.length);
        
        // Verify it's a defensive copy
        snapshot[0] = null;
        assertNotNull(bookstore.findByIsbn("9781234567890"));
    }
    
    @Test
    void testGetAllBooks() {
        List<Book> emptyList = bookstore.getAllBooks();
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
        
        Book book1 = new Book("9781234567890", "Book 1", "Author", 29.99, 2023);
        Book book2 = new Book("9780987654321", "Book 2", "Author", 39.99, 2023);
        
        bookstore.add(book1);
        bookstore.add(book2);
        
        List<Book> allBooks = bookstore.getAllBooks();
        assertEquals(2, allBooks.size());
        
        // Verify it's a defensive copy
        allBooks.clear();
        assertEquals(2, bookstore.size());
    }
    
    @Test
    void testCompleteWorkflow() {
        // Add multiple books
        bookstore.add(new Book("9781234567890", "Java Guide", "John Doe", 45.99, 2023));
        bookstore.add(new Book("9780987654321", "Python Basics", "Jane Smith", 35.99, 2022));
        bookstore.add(new Book("9781111111111", "JavaScript Pro", "Bob Johnson", 55.99, 2024));
        
        // Test various operations
        assertEquals(3, bookstore.size());
        assertEquals(137.97, bookstore.inventoryValue(), 0.01);
        
        Book found = bookstore.findByIsbn("9780987654321");
        assertEquals("Python Basics", found.getTitle());
        
        List<Book> janeBooks = bookstore.findByAuthor("Jane");
        assertEquals(1, janeBooks.size());
        
        Book mostExpensive = bookstore.getMostExpensive();
        assertEquals("JavaScript Pro", mostExpensive.getTitle());
        
        Book mostRecent = bookstore.getMostRecent();
        assertEquals(2024, mostRecent.getYear());
        
        // Remove a book
        assertTrue(bookstore.removeByIsbn("9781111111111"));
        assertEquals(2, bookstore.size());
        assertEquals(81.98, bookstore.inventoryValue(), 0.01);
    }
}