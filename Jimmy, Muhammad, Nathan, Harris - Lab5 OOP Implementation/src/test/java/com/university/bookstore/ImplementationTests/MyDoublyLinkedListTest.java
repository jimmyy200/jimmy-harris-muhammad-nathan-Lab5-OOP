package com.university.bookstore.Lab5Implementations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyDoublyLinkedListTest {

    // a1 tests addFirst makes correct head
    @Test
    public void a1() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addFirst(10);
        a.addFirst(20);
        assertEquals(20, a.getFirst());
        assertEquals(10, a.getLast());
        assertEquals(2, a.size());
    }

    // a2 tests addLast builds list in order
    @Test
    public void a2() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        assertEquals(1, a.getFirst());
        assertEquals(3, a.getLast());
        assertEquals(3, a.size());
    }

    // a3 tests insertAt in a middle position
    @Test
    public void a3() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(1);
        a.addLast(3);
        a.insertAt(1, 2);
        assertEquals(2, a.getAt(1));
        assertEquals(3, a.size());
    }

    // a4 tests removeFirst updates head properly
    @Test
    public void a4() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(5);
        a.addLast(10);
        assertEquals(5, a.removeFirst());
        assertEquals(10, a.getFirst());
        assertEquals(1, a.size());
    }

    // a5 tests removeLast updates tail properly
    @Test
    public void a5() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(7);
        a.addLast(8);
        assertEquals(8, a.removeLast());
        assertEquals(7, a.getLast());
        assertEquals(1, a.size());
    }

    // a6 tests removeAt from safe middle index
    @Test
    public void a6() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        assertEquals(2, a.removeAt(1));
        assertEquals(2, a.size());
    }

    // a7 tests getAt returns correct middle value
    @Test
    public void a7() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(10);
        a.addLast(20);
        a.addLast(30);
        assertEquals(20, a.getAt(1));
    }

    // a8 tests contains finds or misses elements
    @Test
    public void a8() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(1);
        a.addLast(2);
        assertTrue(a.contains(1));
        assertFalse(a.contains(99));
    }

    // a9 tests indexOf returns right index
    @Test
    public void a9() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(10);
        a.addLast(20);
        a.addLast(30);
        assertEquals(1, a.indexOf(20));
        assertEquals(-1, a.indexOf(100));
    }

    // a10 tests clear empties entire list
    @Test
    public void a10() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        a.clear();
        assertEquals(0, a.size());
        assertTrue(a.isEmpty());
    }

    // a11 tests isEmpty before and after insert
    @Test
    public void a11() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        assertTrue(a.isEmpty());
        a.addLast(1);
        assertFalse(a.isEmpty());
    }

    // a12 tests getFirst throws exception when empty
    @Test
    public void a12() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        assertThrows(Exception.class, () -> a.getFirst());
    }

    // a13 tests getAt throws for bad index
    @Test
    public void a13() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(1);
        assertThrows(Exception.class, () -> a.getAt(5));
    }

    // a14 tests insertAt throws for bad index
    @Test
    public void a14() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        assertThrows(Exception.class, () -> a.insertAt(2, 100));
    }

    // a15 tests removeAt throws for bad index
    @Test
    public void a15() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        a.addLast(1);
        assertThrows(Exception.class, () -> a.removeAt(5));
    }
}
