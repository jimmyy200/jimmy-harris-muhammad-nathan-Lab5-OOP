package com.university.bookstore.Lab5Implementations;

class TreeNode<T>{
    TreeNode<T> left;
    TreeNode<T> right;
    T data;
    TreeNode(T data){
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

public class MyBinarySearchTree <T extends Comparable<T>> implements BinarySearchTree<T> {

    private TreeNode<T> root;
    private int size;
    public MyBinarySearchTree(){
        root = null;
        size = 0;
    }
    @Override
    public void insert(T data) {

    }

    @Override
    public boolean contains(T data) {
        return false;
    }

    @Override
    public void delete(T data) {

    }

    @Override
    public void inOrderTraversal() {

    }

    @Override
    public void preOrderTraversal() {

    }

    @Override
    public void postOrderTraversal() {

    }

    @Override
    public void levelOrderTraversal() {

    }

    @Override
    public int height() {
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
