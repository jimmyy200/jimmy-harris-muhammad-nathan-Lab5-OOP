package com.university.bookstore.ImplementationTests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MyBinarySearchTreeTest {

    private String f(Runnable a) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        PrintStream c = new PrintStream(b);
        PrintStream d = System.out;
        System.setOut(c);
        a.run();
        System.out.flush();
        System.setOut(d);
        return b.toString().trim();
    }

    // t1 tests basic insert and contains behavior
    @Test
    public void t1() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        a.insert(1);
        assertTrue(a.contains(8));
        assertTrue(a.contains(1));
        assertFalse(a.contains(99));
    }

    // t2 tests in-order traversal printed output
    @Test
    public void t2() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        a.insert(1);
        a.insert(6);
        String x = f(() -> a.inOrderTraversal());
        assertEquals("1\n3\n6\n8\n10", x);
    }

    // t3 tests pre-order traversal printed output
    @Test
    public void t3() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        a.insert(1);
        a.insert(6);
        String x = f(() -> a.preOrderTraversal());
        assertEquals("8\n3\n1\n6\n10", x);
    }

    // t4 tests post-order traversal printed output
    @Test
    public void t4() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        a.insert(1);
        a.insert(6);
        String x = f(() -> a.postOrderTraversal());
        assertEquals("1\n6\n3\n10\n8", x);
    }

    // t5 tests level-order traversal printed output
    @Test
    public void t5() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        a.insert(1);
        a.insert(6);
        a.insert(14);
        String x = f(() -> a.levelOrderTraversal());
        assertEquals("8\n3\n10\n1\n6\n14", x);
    }

    // t6 tests height on a small balanced tree
    @Test
    public void t6() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        assertEquals(1, a.height());
    }

    // t7 tests height on a long skewed tree
    @Test
    public void t7() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(1);
        a.insert(2);
        a.insert(3);
        a.insert(4);
        a.insert(5);
        assertEquals(4, a.height());
    }

    // t8 tests deleting a leaf node only
    @Test
    public void t8() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        a.insert(1);
        a.delete(1);
        String x = f(() -> a.inOrderTraversal());
        assertEquals("3\n8\n10", x);
    }

    // t9 tests deleting a missing value does nothing
    @Test
    public void t9() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(8);
        a.insert(3);
        a.insert(10);
        a.delete(999);
        String x = f(() -> a.inOrderTraversal());
        assertEquals("3\n8\n10", x);
    }

    // t10 tests size and empty status after inserts
    @Test
    public void t10() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        a.insert(5);
        a.insert(2);
        a.insert(8);
        assertEquals(3, a.size());
        assertFalse(a.isEmpty());
    }
}

