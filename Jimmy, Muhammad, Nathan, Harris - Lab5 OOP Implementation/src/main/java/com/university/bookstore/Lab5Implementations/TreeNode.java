package com.university.bookstore.Lab5Implementations;

public class TreeNode<T extends Comparable<T>> {
    TreeNode<T> left;
    TreeNode<T> right;
    T data;
    TreeNode(T data){
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
