package com.university.bookstore.api;
/**
 *A generic Doubly Linked List interface for Lab 5.
 *Students must implement all methods.
*/
public interface DoublyLinkedList<T> {
    //Basic operations
    void addFirst(T data);
    // Insert at the head
    void addLast(T data);
    // Insert at the tail
    void insertAt(int index, T data); // Insert at a given index
    //Deletion
    T removeFirst();
    // Remove from head
    T removeLast();
    T removeAt(int index);
    // Remove from tail
    // Remove from a given index

    //Access
    T getFirst();
    T getLast();
    T getAt(int index);
    // Get head element
    // Get tail element
    // Get element at index
 
    //Utility
    int size();
    boolean isEmpty();
    void clear();
    // Number of elements
    // Check if empty
    // Remove all elements

    //Search
    boolean contains(T data);
    int indexOf(T data);
}

class Node {
    T data;
    Node<T> prev;
    Node<T> next;

    Node(T data){
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}