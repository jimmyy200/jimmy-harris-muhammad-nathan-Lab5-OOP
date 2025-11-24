package com.university.bookstore.Lab5Implementations;

 /**
  * A generic Binary Search Tree interface for Lab 5.
  * Students must implement all methods.
  */
 public interface BinarySearchTree<T extends Comparable<T>> {
     void insert(T data);
     boolean contains(T data);
     void delete(T data);
     void inOrderTraversal();
     void preOrderTraversal();
     void postOrderTraversal();
     void levelOrderTraversal();
     int height();
     int size();
     boolean isEmpty();
 }
