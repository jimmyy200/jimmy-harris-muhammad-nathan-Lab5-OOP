package com.university.bookstore.Lab5Implementations;

 /**
  * A generic Binary Search Tree interface for Lab 5.
  * Students must implement all methods.
  */
 public interface BinarySearchTree<T extends Comparable<T>> {

     void insert(T data);
     boolean contains(T data);
     void delete(T data);

     TreeNode<T> insert(TreeNode<T> node, T key);
     boolean contains(TreeNode<T> node, T key);
     TreeNode<T> delete(TreeNode<T> node, T key);

     void inOrderTraversal();
     void preOrderTraversal();
     void postOrderTraversal();
     void levelOrderTraversal();

     void inOrderTraversal(TreeNode<T> node);
     void preOrderTraversal(TreeNode<T> node);
     void postOrderTraversal(TreeNode<T> node);
     void levelOrderTraversal(TreeNode<T> node);

     int height();
     int height(TreeNode<T> node);
     int size();
     boolean isEmpty();
 }
