package com.university.bookstore.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RepositoryExceptionTest {
    
    @Test
    void testConstructorWithMessage() {
        String message = "Repository operation failed";
        var exception = new RepositoryException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Repository operation failed";
        Throwable cause = new IllegalStateException("Underlying error");
        RepositoryException exception = new RepositoryException(message, cause);
        
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals("Underlying error", exception.getCause().getMessage());
    }
    
    @Test
    void testConstructorWithCause() {
        Throwable cause = new IllegalStateException("Underlying error");
        RepositoryException exception = new RepositoryException(cause);
        
        assertEquals(cause, exception.getCause());
        // The message should contain the cause's class name and message
        assertTrue(exception.getMessage().contains("IllegalStateException"));
        assertTrue(exception.getMessage().contains("Underlying error"));
    }
    
    @Test
    void testExceptionInheritance() {
        RepositoryException exception = new RepositoryException("Test");
        
        // Verify it's a RuntimeException
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }
    
    @Test
    void testThrowAndCatch() {
        assertThrows(RepositoryException.class, () -> {
            throw new RepositoryException("Test exception");
        });
        
        assertThrows(RepositoryException.class, () -> {
            throw new RepositoryException("Test", new RuntimeException());
        });
        
        assertThrows(RepositoryException.class, () -> {
            throw new RepositoryException(new RuntimeException("Cause"));
        });
    }
    
    @Test
    void testNullMessage() {
        RepositoryException exception = new RepositoryException((String) null);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    void testNullCause() {
        RepositoryException exception = new RepositoryException("Message", null);
        assertEquals("Message", exception.getMessage());
        assertNull(exception.getCause());
        
        exception = new RepositoryException((Throwable) null);
        assertNull(exception.getCause());
    }
    
    @Test
    void testChainedExceptions() {
        IllegalArgumentException rootCause = new IllegalArgumentException("Root cause");
        IllegalStateException middleCause = new IllegalStateException("Middle cause", rootCause);
        RepositoryException topException = new RepositoryException("Top level", middleCause);
        
        assertEquals("Top level", topException.getMessage());
        assertEquals(middleCause, topException.getCause());
        assertEquals(rootCause, topException.getCause().getCause());
    }
    
    @Test
    void testSerialVersionUID() {
        // Verify the class has a serialVersionUID field
        try {
            RepositoryException.class.getDeclaredField("serialVersionUID");
            assertTrue(true, "serialVersionUID field exists");
        } catch (NoSuchFieldException e) {
            fail("serialVersionUID field not found");
        }
    }
    
    @Test
    void testStackTrace() {
        RepositoryException exception = new RepositoryException("Test");
        StackTraceElement[] stackTrace = exception.getStackTrace();
        
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
        // The first element should be from this test class
        assertEquals(this.getClass().getName(), stackTrace[0].getClassName());
    }
}