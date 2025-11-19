package com.university.bookstore.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.university.bookstore.model.Material;

/**
 * Trie (Prefix Tree) data structure for efficient prefix-based material searching.
 * Provides O(m) time complexity for prefix searches where m is the length of the prefix.
 * 
 * <p>This implementation stores materials at each node along the path, allowing for
 * efficient prefix-based lookups and autocomplete functionality.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialTrie {
    
    private final TrieNode root;
    
    /**
     * Creates a new empty material trie.
     */
    public MaterialTrie() {
        this.root = new TrieNode();
    }
    
    /**
     * Inserts a material into the trie using its title as the key.
     * 
     * @param material the material to insert
     * @throws IllegalArgumentException if material is null
     */
    public void insert(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        String title = material.getTitle().toLowerCase();
        TrieNode current = root;
        
        // Add material to root for empty prefix searches
        current.materials.add(material);
        
        // Traverse the trie, adding material to each node along the path
        for (char c : title.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
            current.materials.add(material);
        }
        
        current.isEndOfWord = true;
    }
    
    /**
     * Searches for materials with titles that start with the given prefix.
     * 
     * @param prefix the prefix to search for
     * @return list of materials matching the prefix
     */
    public List<Material> searchByPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerPrefix = prefix.toLowerCase().trim();
        TrieNode current = root;
        
        // Navigate to the prefix node
        for (char c : lowerPrefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return Collections.emptyList();
            }
            current = current.children.get(c);
        }
        
        return new ArrayList<>(current.materials);
    }
    
    /**
     * Searches for materials with titles that start with the given prefix,
     * limited to a maximum number of results.
     * 
     * @param prefix the prefix to search for
     * @param limit the maximum number of results to return
     * @return list of materials matching the prefix, limited to the specified count
     */
    public List<Material> searchByPrefixWithLimit(String prefix, int limit) {
        if (limit <= 0) {
            return new ArrayList<>();
        }
        List<Material> results = searchByPrefix(prefix);
        return results.stream()
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if any materials exist with titles starting with the given prefix.
     * 
     * @param prefix the prefix to check
     * @return true if materials exist with the prefix
     */
    public boolean hasPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return false;
        }
        
        String lowerPrefix = prefix.toLowerCase().trim();
        TrieNode current = root;
        
        for (char c : lowerPrefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return false;
            }
            current = current.children.get(c);
        }
        
        return !current.materials.isEmpty();
    }
    
    /**
     * Gets all materials in the trie.
     * 
     * @return list of all materials
     */
    public List<Material> getAllMaterials() {
        return new ArrayList<>(root.materials);
    }
    
    /**
     * Removes a material from the trie.
     * 
     * @param material the material to remove
     * @return true if the material was removed, false if not found
     */
    public boolean remove(Material material) {
        if (material == null) {
            return false;
        }
        
        String title = material.getTitle().toLowerCase();
        List<TrieNode> path = new ArrayList<>();
        TrieNode current = root;
        
        // Build path to the material
        path.add(current);
        for (char c : title.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return false; // Material not found
            }
            current = current.children.get(c);
            path.add(current);
        }
        
        // Remove material from all nodes in the path
        boolean removed = false;
        for (TrieNode node : path) {
            if (node.materials.remove(material)) {
                removed = true;
            }
        }
        
        // Clean up empty nodes (optional optimization)
        cleanupEmptyNodes(path);
        
        return removed;
    }
    
    /**
     * Clears all materials from the trie.
     */
    public void clear() {
        root.children.clear();
        root.materials.clear();
        root.isEndOfWord = false;
    }
    
    /**
     * Gets the total number of materials in the trie.
     * 
     * @return the number of materials
     */
    public int size() {
        return root.materials.size();
    }
    
    /**
     * Checks if the trie is empty.
     * 
     * @return true if no materials are stored
     */
    public boolean isEmpty() {
        return root.materials.isEmpty();
    }
    
    /**
     * Internal method to clean up empty nodes after removal.
     * 
     * @param path the path of nodes to potentially clean up
     */
    private void cleanupEmptyNodes(List<TrieNode> path) {
        // Remove empty leaf nodes (optional optimization)
        for (int i = path.size() - 1; i > 0; i--) {
            TrieNode node = path.get(i);
            if (node.materials.isEmpty() && node.children.isEmpty()) {
                TrieNode parent = path.get(i - 1);
                // Find and remove the empty child
                parent.children.entrySet().removeIf(entry -> entry.getValue() == node);
            }
        }
    }
    
    /**
     * Internal node class for the trie structure.
     */
    private static class TrieNode {
        final Map<Character, TrieNode> children;
        final List<Material> materials;
        boolean isEndOfWord;
        
        TrieNode() {
            this.children = new HashMap<>();
            this.materials = new ArrayList<>();
            this.isEndOfWord = false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("MaterialTrie[Size=%d, Prefixes=%d]",
            size(),
            countPrefixes(root));
    }
    
    /**
     * Counts the total number of prefixes in the trie.
     * 
     * @param node the node to count from
     * @return the number of prefixes
     */
    private int countPrefixes(TrieNode node) {
        int count = node.isEndOfWord ? 1 : 0;
        for (TrieNode child : node.children.values()) {
            count += countPrefixes(child);
        }
        return count;
    }
}
