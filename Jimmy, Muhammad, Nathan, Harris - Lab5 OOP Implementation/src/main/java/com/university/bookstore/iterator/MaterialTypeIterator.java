package com.university.bookstore.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.university.bookstore.model.Material;

/**
 * Iterator that filters materials by type.
 * Only returns materials of the specified type.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialTypeIterator implements MaterialIterator {
    
    private final List<Material> materials;
    private final Material.MaterialType targetType;
    private int currentIndex;
    private final int totalCount;
    
    /**
     * Creates a new material type iterator.
     * 
     * @param materials the list of materials to iterate over
     * @param targetType the type of materials to filter for
     */
    public MaterialTypeIterator(List<Material> materials, Material.MaterialType targetType) {
        this.materials = new ArrayList<>(materials);
        this.targetType = targetType;
        this.currentIndex = 0;
        this.totalCount = (int) materials.stream()
            .filter(m -> m.getType() == targetType)
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
     * Gets the target type this iterator filters for.
     * 
     * @return the target material type
     */
    public Material.MaterialType getTargetType() {
        return targetType;
    }
    
    /**
     * Advances to the next material of the target type.
     */
    private void advanceToNext() {
        while (currentIndex < materials.size() && 
               materials.get(currentIndex).getType() != targetType) {
            currentIndex++;
        }
    }
    
    @Override
    public String toString() {
        return String.format("MaterialTypeIterator[Type=%s, Position=%d/%d]",
            targetType, getCurrentPosition(), getTotalCount());
    }
}
