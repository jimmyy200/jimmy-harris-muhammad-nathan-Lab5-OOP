package com.university.bookstore.Lab5Implementations;

import com.university.bookstore.Lab5Implementations.DoublyLinkedList;
import com.university.bookstore.Lab5Implementations.MaterialLinkedList;
import com.university.bookstore.Lab5Implementations.BinarySearchTree;
import com.university.bookstore.Lab5Implementations.MyBinarySearchTree;

import org.junit.jupiter.api.Test;

public class PerformanceTests {

    // p1 measures time for addLast many times
    @Test
    public void p1() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        long b = System.nanoTime();
        for (int i = 0; i < 10000; i++) a.addLast(i);
        long c = System.nanoTime();
        System.out.println("x:" + (c - b));
    }

    // p2 measures time for addFirst many times
    @Test
    public void p2() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        long b = System.nanoTime();
        for (int i = 0; i < 10000; i++) a.addFirst(i);
        long c = System.nanoTime();
        System.out.println("y:" + (c - b));
    }

    // p3 measures contains worst-case lookup speed
    @Test
    public void p3() {
        DoublyLinkedList<Integer> a = new MaterialLinkedList<>();
        for (int i = 0; i < 10000; i++) a.addLast(i);
        long b = System.nanoTime();
        boolean d = a.contains(9999);
        long c = System.nanoTime();
        System.out.println("z:" + (c - b));
    }

    // p4 measures BST insert performance on large input
    @Test
    public void p4() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        long b = System.nanoTime();
        for (int i = 0; i < 10000; i++) a.insert(i);
        long c = System.nanoTime();
        System.out.println("u:" + (c - b));
    }

    // p5 measures BST search performance near end
    @Test
    public void p5() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        for (int i = 0; i < 10000; i++) a.insert(i);
        long b = System.nanoTime();
        boolean d = a.contains(9999);
        long c = System.nanoTime();
        System.out.println("v:" + (c - b));
    }

    // p6 measures BST height calculation on skewed tree
    @Test
    public void p6() {
        BinarySearchTree<Integer> a = new MyBinarySearchTree<>();
        for (int i = 0; i < 10000; i++) a.insert(i);
        long b = System.nanoTime();
        int d = a.height();
        long c = System.nanoTime();
        System.out.println("w:" + (c - b));
    }
}
