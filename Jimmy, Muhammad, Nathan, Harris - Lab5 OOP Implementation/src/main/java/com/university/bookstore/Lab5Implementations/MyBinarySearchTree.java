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
    @Override
    public TreeNode<Integer> insert(TreeNode<Integer> node, int key) {
        if (node == null) {
            return new TreeNode<>(key);
        }

        if (key < node.data){
            node.left = insert(node.left, key);
        } else if (key > node.data){
            node.right = insert(node.right, key);
        }

        return node;
    }

    @Override
    public boolean contains(TreeNode<T> node) {
        return false;
    }

    @Override
    public void delete(TreeNode<T> node) {

    }

    @Override
    public void inOrderTraversal(TreeNode<T> node) {
        if (node != null){
            inOrderTraversal(node.left);
            System.out.println(node.data);
            inOrderTraversal(node.right);
        }
    }

    @Override
    public void preOrderTraversal(TreeNode<T> node) {
        if (node != null){
            System.out.println(node.data);
            preOrderTraversal(node.left);
            preOrderTraversal(node.right);
        }
    }

    @Override
    public void postOrderTraversal(TreeNode<T> node) {
        if (node != null){
            postOrderTraversal(node.left);
            postOrderTraversal(node.right);
            System.out.println(node.data);
        }
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
