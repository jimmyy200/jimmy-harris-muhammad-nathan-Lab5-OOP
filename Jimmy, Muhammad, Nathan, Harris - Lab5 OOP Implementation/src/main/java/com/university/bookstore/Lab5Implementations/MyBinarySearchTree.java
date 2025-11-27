package com.university.bookstore.Lab5Implementations;

import java.util.LinkedList;
import java.util.Queue;

public class MyBinarySearchTree <T extends Comparable<T>> implements BinarySearchTree<T> {

    private TreeNode<T> root;
    private int size;

    /**
     * Constructs a new binary search tree
     */
    public MyBinarySearchTree(){
        root = null;
        size = 0;
    }

    /**
     * Calls the other insert function
     * @param key any data type that uses generics
     */
    public void insert(T key){
        root = insert(root, key);
    }

    /**
     * Inserts object into the BST
     * @param node starting point (root)
     * @param key generic object/data type to be inserted
     * @return root
     */
    @Override
    public TreeNode<T> insert(TreeNode<T> node, T key) {
        if (node == null) {
            size++;
            return new TreeNode<>(key);
        }

        if (key.compareTo(node.data) < 0){
            node.left = insert(node.left, key);
        } else if (key.compareTo(node.data) > 0){
            node.right = insert(node.right, key);
        }

        return node;
    }

    /**
     * Calls the other contains function
     * @param key any data type that uses generics
     * @return true if it contains the key, false if it does not contain the key
     */
    public boolean contains(T key){
        return contains(root, key);
    }

    /**
     * Searches the BST to see if it contains the given key
     * @param node starting point of the BST (root)
     * @param key generic object/data type to be searched for
     * @return true if it contains the key, false if it does not contain the key
     */
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

    /**
     * Helper class for delete method to find successor in BST
     * @param node any data type/object that is generic
     * @return
     */
    private TreeNode<T> findMinimum(TreeNode<T> node){
        while (node.left != null){
            node = node.left;
        }
        return node;
    }

    /**
     * Calls other delete function without needing root
     * @param key generic data type to be deleted
     */
    public void delete(T key){
        delete(root, key);
    }

    /**
     * Deletes a specified key
     * @param node starting point(root)
     * @param key generic data type to be deleted
     * @return starting point(root)
     */
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
            // if no children
            if (node.left == null && node.right == null){
                size--;
                return null;
            }
            // if it has only left/right child
            if (node.left == null){
                size--;
                return node.right;
            }
            if (node.right == null){
                size--;
                return node.left;
            }
            //if it has 2 children
            TreeNode<T> successor = findMinimum(node.right);
            node.data = successor.data;
            node.right = delete(node.right, node.data);
        }
        return node;
    }

    /**
     * Calls the other inOrderTraversal function without having the root
     */
    public void inOrderTraversal(){
        inOrderTraversal(root);
    }

    /**
     * inOrderTraversal of the BST (left, root, right)
     * @param node root
     */
    @Override
    public void inOrderTraversal(TreeNode<T> node) {
        if (node != null){
            inOrderTraversal(node.left);
            System.out.println(node.data);
            inOrderTraversal(node.right);
        }
    }

    /**
     * Calls the other preOrderTraversal function without having the root
     */
    public void preOrderTraversal(){
        preOrderTraversal(root);
    }

    /**
     * preOrderTraversal of the BST (root, left, right)
     * @param node root
     */
    @Override
    public void preOrderTraversal(TreeNode<T> node) {
        if (node != null){
            System.out.println(node.data);
            preOrderTraversal(node.left);
            preOrderTraversal(node.right);
        }
    }

    /**
     * Calls the other postOrderTraversal of the BST without having the root
     */
    public void postOrderTraversal(){
        postOrderTraversal(root);
    }

    /**
     * postOrderTraversal of the BST (left, right, root)
     * @param node root
     */
    @Override
    public void postOrderTraversal(TreeNode<T> node) {
        if (node != null){
            postOrderTraversal(node.left);
            postOrderTraversal(node.right);
            System.out.println(node.data);
        }
    }

    /**
     * Calls the other levelOrderTraversal without having the root
     */
    public void levelOrderTraversal(){
        levelOrderTraversal(root);
    }

    /**
     * Level by level traversal (left to right)
     * @param node root
     */
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

    /**
     * Calls the other height function without having the root
     * @return height
     */
    public int height(){
        return height(root);
    }

    /**
     * Gets the height of the BST
     * @param node root
     * @return height
     */
    @Override
    public int height(TreeNode<T> node) {
        if (node == null) return -1;

        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Gets the size of the BST
     * @return size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the BST is empty
     * @return true if BST is empty, false if BST is not empty
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
