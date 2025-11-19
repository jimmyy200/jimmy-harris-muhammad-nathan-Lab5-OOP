package com.university.bookstore.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.university.bookstore.model.Material;

/**
 * Iterator that filters materials by price range.
 * Only returns materials within the specified price range.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class PriceRangeIterator implements MaterialIterator {
    
    private final List<Material> materials;
    private final double minPrice;
    private final double maxPrice;
    private int currentIndex;
    private final int totalCount;
    
    /**
     * Creates a new price range iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param minPrice the minimum price (inclusive)
     * @param maxPrice the maximum price (inclusive)
     * @throws IllegalArgumentException if price range is invalid
     */
    public PriceRangeIterator(List<Material> materials, double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range: " + minPrice + " to " + maxPrice);
        }
        
        this.materials = new ArrayList<>(materials);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.currentIndex = 0;
        this.totalCount = (int) materials.stream()
            .filter(m -> m.getPrice() >= minPrice && m.getPrice() <= maxPrice)
            .count();
        advanceToNext();
    }
    
    @Override
    public boolean hasNext() {
        return currentIndex < materials.size();
    }
    
    @Override
    public Material next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        
        Material current = materials.get(currentIndex);
        currentIndex++;
        advanceToNext();
        return current;
    }
    
    @Override
    public void reset() {
        currentIndex = 0;
        advanceToNext();
    }
    
    @Override
    public int getCurrentPosition() {
        return currentIndex;
    }
    
    @Override
    public int getTotalCount() {
        return totalCount;
    }
    
    @Override
    public int getRemainingCount() {
        return totalCount - getCurrentPosition();
    }
    
    @Override
    public boolean isAtBeginning() {
        return currentIndex == 0;
    }
    
    @Override
    public boolean isAtEnd() {
        return currentIndex >= materials.size();
    }
    
    /**
     * Gets the minimum price.
     * 
     * @return the minimum price
     */
    public double getMinPrice() {
        return minPrice;
    }
    
    /**
     * Gets the maximum price.
     * 
     * @return the maximum price
     */
    public double getMaxPrice() {
        return maxPrice;
    }
    
    /**
     * Gets the price range.
     * 
     * @return the price range
     */
    public double getPriceRange() {
        return maxPrice - minPrice;
    }
    
    /**
     * Advances to the next material within the price range.
     */
    private void advanceToNext() {
        while (currentIndex < materials.size()) {
            Material material = materials.get(currentIndex);
            if (material.getPrice() >= minPrice && material.getPrice() <= maxPrice) {
                break;
            }
            currentIndex++;
        }
    }
    
    @Override
    public String toString() {
        return String.format("PriceRangeIterator[Range=$%.2f-$%.2f, Position=%d/%d]",
            minPrice, maxPrice, getCurrentPosition(), getTotalCount());
    }
}
