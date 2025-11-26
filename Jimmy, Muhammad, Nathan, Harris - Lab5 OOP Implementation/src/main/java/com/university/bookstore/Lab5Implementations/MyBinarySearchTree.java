package com.university.bookstore.Lab5Implementations;

import java.util.LinkedList;
import java.util.Queue;

public class MyBinarySearchTree <T extends Comparable<T>> implements BinarySearchTree<T> {

    private TreeNode<T> root;
    private int size;
    public MyBinarySearchTree(){
        root = null;
        size = 0;
    }

    public void insert(T key){
        root = insert(root, key);
    }

    @Override
    public TreeNode<T> insert(TreeNode<T> node, T key) {
        if (node == null) {
            return new TreeNode<>(key);
        }

        if (key.compareTo(node.data) < 0){
            node.left = insert(node.left, key);
        } else if (key.compareTo(node.data) > 0){
            node.right = insert(node.right, key);
        }

        return node;
    }

    public boolean contains(T key){
        return contains(root, key);
    }
    @Override
    public boolean contains(TreeNode<T> node, T key) {
        if (node == null) {
            return false;
        }
        int cmp = key.compareTo(node.data);
        if (cmp == 0) {
            return true;
        } else if  (cmp < 0) {
            return contains(node.left, key);
        } else {
            return contains(node.right, key);
        }
    }

    private TreeNode<T> findMinimum(TreeNode<T> node){
        while (node.left != null){
            node = node.left;
        }
        return node;
    }
    public void delete(T key){
        delete(root, key);
    }
    @Override
    public TreeNode<T> delete(TreeNode<T> node, T key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.data);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0){
            node.right = delete(node.right, key);
        } else {
            if (node.left == null && node.right == null){
                return null;
            }
            if (node.left == null){
                return node.right;
            }
            if (node.right == null){
                return node.left;
            }
            TreeNode<T> successor = findMinimum(node.right);
            node.data = successor.data;
            node.right = delete(node.right, node.data);
        }
        return node;
    }

    public void inOrderTraversal(){
        inOrderTraversal(root);
    }
    @Override
    public void inOrderTraversal(TreeNode<T> node) {
        if (node != null){
            inOrderTraversal(node.left);
            System.out.println(node.data);
            inOrderTraversal(node.right);
        }
    }

    public void preOrderTraversal(){
        preOrderTraversal(root);
    }
    @Override
    public void preOrderTraversal(TreeNode<T> node) {
        if (node != null){
            System.out.println(node.data);
            preOrderTraversal(node.left);
            preOrderTraversal(node.right);
        }
    }

    public void postOrderTraversal(){
        postOrderTraversal(root);
    }
    @Override
    public void postOrderTraversal(TreeNode<T> node) {
        if (node != null){
            postOrderTraversal(node.left);
            postOrderTraversal(node.right);
            System.out.println(node.data);
        }
    }

    public void levelOrderTraversal(){
        levelOrderTraversal(root);
    }
    @Override
    public void levelOrderTraversal(TreeNode<T> node) {
        if (node == null) return;

        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.offer(node);

        while (!queue.isEmpty()){
            TreeNode<T> cur = queue.poll();
            System.out.println(cur.data);

            if (cur.left != null)queue.offer(cur.left);
            if (cur.right != null)queue.offer(cur.right);
        }
    }

    public int height(){
        return height(root);
    }
    @Override
    public int height(TreeNode<T> node) {
        if (node == null) return -1;

        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        return 1 + Math.max(height(node.left), height(node.right));
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
