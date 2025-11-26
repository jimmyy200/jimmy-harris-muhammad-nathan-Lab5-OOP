package com.university.bookstore.Lab5Implementations;

 /**
  * A generic Binary Search Tree interface for Lab 5.
  * Students must implement all methods.
  */
 public interface BinarySearchTree<T extends Comparable<T>> {
     TreeNode insert(TreeNode<Integer> node, int key);
     boolean contains(TreeNode<T> node);
     void delete(TreeNode<T> node);
     void inOrderTraversal(TreeNode<T> node);
     void preOrderTraversal(TreeNode<T> node);
     void postOrderTraversal(TreeNode<T> node);
     void levelOrderTraversal(TreeNode<T> node);
     int height(TreeNode<T> node);
     int size();
     boolean isEmpty();
 }
