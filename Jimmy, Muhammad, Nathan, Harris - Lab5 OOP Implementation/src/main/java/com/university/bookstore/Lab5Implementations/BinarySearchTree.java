package com.university.bookstore.Lab5Implementations;

 /**
  * A generic Binary Search Tree interface for Lab 5.
  * Students must implement all methods.
  */
 public interface BinarySearchTree<T extends Comparable<T>> {
     void insert(T data);
     boolean contains(T data);
     void delete(T data);
     void inOrderTraversal(TreeNode<T> node);
     void preOrderTraversal(TreeNode<T> node);
     void postOrderTraversal(TreeNode<T> node);
     void levelOrderTraversal(TreeNode<T> node);
     int height(TreeNode<T> node);
     int size();
     boolean isEmpty();
 }
