package com.university.bookstore.iterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import com.university.bookstore.model.Material;

/**
 * Iterator that returns materials sorted by price.
 * Can sort in ascending or descending order.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class PriceSortedIterator implements MaterialIterator {
    
    private final List<Material> sortedMaterials;
    private int currentIndex;
    private final boolean ascending;
    
    /**
     * Creates a new price sorted iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param ascending true for ascending order, false for descending
     */
    public PriceSortedIterator(List<Material> materials, boolean ascending) {
        this.sortedMaterials = new ArrayList<>(materials);
        this.ascending = ascending;
        this.currentIndex = 0;
        
        // Sort materials by price
        Comparator<Material> priceComparator = Comparator.comparing(Material::getPrice);
        if (!ascending) {
            priceComparator = priceComparator.reversed();
        }
        this.sortedMaterials.sort(priceComparator);
    }
    
    @Override
    public boolean hasNext() {
        return currentIndex < sortedMaterials.size();
    }
    
    @Override
    public Material next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        return sortedMaterials.get(currentIndex++);
    }
    
    @Override
    public void reset() {
        currentIndex = 0;
    }
    
    @Override
    public int getCurrentPosition() {
        return currentIndex;
    }
    
    @Override
    public int getTotalCount() {
        return sortedMaterials.size();
    }
    
    @Override
    public int getRemainingCount() {
        return sortedMaterials.size() - currentIndex;
    }
    
    @Override
    public boolean isAtBeginning() {
        return currentIndex == 0;
    }
    
    @Override
    public boolean isAtEnd() {
        return currentIndex >= sortedMaterials.size();
    }
    
    /**
     * Gets the sort order.
     * 
     * @return true if ascending, false if descending
     */
    public boolean isAscending() {
        return ascending;
    }
    
    /**
     * Gets the current material without advancing the iterator.
     * 
     * @return the current material
     * @throws NoSuchElementException if at the end
     */
    public Material peek() {
        if (currentIndex >= sortedMaterials.size()) {
            throw new NoSuchElementException("No more elements");
        }
        return sortedMaterials.get(currentIndex);
    }
    
    /**
     * Gets the next material without advancing the iterator.
     * 
     * @return the next material
     * @throws NoSuchElementException if no next element
     */
    public Material peekNext() {
        if (currentIndex + 1 >= sortedMaterials.size()) {
            throw new NoSuchElementException("No next element");
        }
        return sortedMaterials.get(currentIndex + 1);
    }
    
    @Override
    public String toString() {
        return String.format("PriceSortedIterator[Order=%s, Position=%d/%d]",
            ascending ? "ASC" : "DESC", getCurrentPosition(), getTotalCount());
    }
}
