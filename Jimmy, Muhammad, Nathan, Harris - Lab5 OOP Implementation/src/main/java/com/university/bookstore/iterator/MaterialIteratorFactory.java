package com.university.bookstore.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.university.bookstore.model.Material;

/**
 * Factory for creating different types of material iterators.
 * Provides a centralized way to create iterators with different traversal strategies.
 * 
 * <p>This factory demonstrates the Factory pattern by providing methods to create
 * various types of iterators without exposing the concrete iterator classes.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialIteratorFactory {
    
    /**
     * Creates a type-filtering iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param type the material type to filter for
     * @return iterator for materials of the specified type
     */
    public MaterialIterator createTypeIterator(List<Material> materials, Material.MaterialType type) {
        return new MaterialTypeIterator(materials, type);
    }
    
    /**
     * Creates a price-sorted iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param ascending true for ascending order, false for descending
     * @return iterator for materials sorted by price
     */
    public MaterialIterator createPriceSortedIterator(List<Material> materials, boolean ascending) {
        return new PriceSortedIterator(materials, ascending);
    }
    
    /**
     * Creates a price range iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param minPrice the minimum price (inclusive)
     * @param maxPrice the maximum price (inclusive)
     * @return iterator for materials within the price range
     */
    public MaterialIterator createPriceRangeIterator(List<Material> materials, double minPrice, double maxPrice) {
        return new PriceRangeIterator(materials, minPrice, maxPrice);
    }
    
    /**
     * Creates an e-book iterator.
     * 
     * @param materials the list of materials to iterate over
     * @return iterator for e-books only
     */
    public MaterialIterator createEBookIterator(List<Material> materials) {
        return new MaterialTypeIterator(materials, Material.MaterialType.E_BOOK);
    }
    
    /**
     * Creates an expensive materials iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param threshold the minimum price threshold
     * @return iterator for materials above the price threshold
     */
    public MaterialIterator createExpensiveIterator(List<Material> materials, double threshold) {
        return new PriceRangeIterator(materials, threshold, Double.MAX_VALUE);
    }
    
    /**
     * Creates a cheap materials iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param threshold the maximum price threshold
     * @return iterator for materials below the price threshold
     */
    public MaterialIterator createCheapIterator(List<Material> materials, double threshold) {
        return new PriceRangeIterator(materials, 0.0, threshold);
    }
    
    /**
     * Creates a book iterator.
     * 
     * @param materials the list of materials to iterate over
     * @return iterator for books only
     */
    public MaterialIterator createBookIterator(List<Material> materials) {
        return new MaterialTypeIterator(materials, Material.MaterialType.BOOK);
    }
    
    /**
     * Creates a magazine iterator.
     * 
     * @param materials the list of materials to iterate over
     * @return iterator for magazines only
     */
    public MaterialIterator createMagazineIterator(List<Material> materials) {
        return new MaterialTypeIterator(materials, Material.MaterialType.MAGAZINE);
    }
    
    /**
     * Creates an audio book iterator.
     * 
     * @param materials the list of materials to iterate over
     * @return iterator for audio books only
     */
    public MaterialIterator createAudioBookIterator(List<Material> materials) {
        return new MaterialTypeIterator(materials, Material.MaterialType.AUDIO_BOOK);
    }
    
    /**
     * Creates a video iterator.
     * 
     * @param materials the list of materials to iterate over
     * @return iterator for videos only
     */
    public MaterialIterator createVideoIterator(List<Material> materials) {
        return new MaterialTypeIterator(materials, Material.MaterialType.VIDEO);
    }
    
    /**
     * Collects all materials from an iterator into a list.
     * 
     * @param iterator the iterator to collect from
     * @return list of all materials from the iterator
     */
    public List<Material> collectAll(MaterialIterator iterator) {
        List<Material> result = new ArrayList<>();
        iterator.reset();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }
    
    /**
     * Finds the first material matching a predicate.
     * 
     * @param iterator the iterator to search
     * @param predicate the predicate to test
     * @return Optional containing the first matching material
     */
    public Optional<Material> findFirst(MaterialIterator iterator, Predicate<Material> predicate) {
        iterator.reset();
        while (iterator.hasNext()) {
            Material material = iterator.next();
            if (predicate.test(material)) {
                return Optional.of(material);
            }
        }
        return Optional.empty();
    }
    
    /**
     * Finds all materials matching a predicate.
     * 
     * @param iterator the iterator to search
     * @param predicate the predicate to test
     * @return list of all matching materials
     */
    public List<Material> findAll(MaterialIterator iterator, Predicate<Material> predicate) {
        List<Material> result = new ArrayList<>();
        iterator.reset();
        while (iterator.hasNext()) {
            Material material = iterator.next();
            if (predicate.test(material)) {
                result.add(material);
            }
        }
        return result;
    }
    
    /**
     * Counts materials matching a predicate.
     * 
     * @param iterator the iterator to count
     * @param predicate the predicate to test
     * @return the count of matching materials
     */
    public int count(MaterialIterator iterator, Predicate<Material> predicate) {
        int count = 0;
        iterator.reset();
        while (iterator.hasNext()) {
            Material material = iterator.next();
            if (predicate.test(material)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Checks if any material matches a predicate.
     * 
     * @param iterator the iterator to check
     * @param predicate the predicate to test
     * @return true if any material matches
     */
    public boolean anyMatch(MaterialIterator iterator, Predicate<Material> predicate) {
        iterator.reset();
        while (iterator.hasNext()) {
            Material material = iterator.next();
            if (predicate.test(material)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if all materials match a predicate.
     * 
     * @param iterator the iterator to check
     * @param predicate the predicate to test
     * @return true if all materials match
     */
    public boolean allMatch(MaterialIterator iterator, Predicate<Material> predicate) {
        iterator.reset();
        while (iterator.hasNext()) {
            Material material = iterator.next();
            if (!predicate.test(material)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets the total count of materials in an iterator.
     * 
     * @param iterator the iterator to count
     * @return the total count
     */
    public int getTotalCount(MaterialIterator iterator) {
        return iterator.getTotalCount();
    }
    
    /**
     * Gets the remaining count of materials in an iterator.
     * 
     * @param iterator the iterator to check
     * @return the remaining count
     */
    public int getRemainingCount(MaterialIterator iterator) {
        return iterator.getRemainingCount();
    }
    
    @Override
    public String toString() {
        return "MaterialIteratorFactory[Available iterators: Type, PriceSorted, PriceRange, EBook, Expensive, Cheap, Book, Magazine, AudioBook, Video]";
    }
}
