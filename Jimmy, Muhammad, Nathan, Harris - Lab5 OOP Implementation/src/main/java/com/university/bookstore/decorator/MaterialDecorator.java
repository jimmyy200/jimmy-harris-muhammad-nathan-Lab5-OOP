package com.university.bookstore.decorator;

import java.util.Objects;

import com.university.bookstore.model.Material;

/**
 * Abstract base class for material decorators in the Decorator pattern.
 * Provides a common interface for decorating materials with additional features.
 * 
 * <p>This class implements the Material interface and delegates all operations
 * to the wrapped material, allowing subclasses to override specific methods
 * to add new functionality without modifying the original material classes.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public abstract class MaterialDecorator extends Material {
    
    protected final Material decoratedMaterial;
    
    /**
     * Creates a new material decorator wrapping the specified material.
     * 
     * @param material the material to decorate
     * @throws IllegalArgumentException if material is null
     */
    public MaterialDecorator(Material material) {
        super(material.getId(), material.getTitle(), material.getPrice(), material.getYear(), material.getType());
        this.decoratedMaterial = Objects.requireNonNull(material, "Material cannot be null");
    }
    
    // Implement abstract methods from Material by delegating to wrapped material
    @Override
    public String getCreator() {
        return decoratedMaterial.getCreator();
    }
    
    @Override
    public String getDisplayInfo() {
        return decoratedMaterial.getDisplayInfo();
    }
    
    @Override
    public double getDiscountRate() {
        return decoratedMaterial.getDiscountRate();
    }
    
    /**
     * Gets the decorated material.
     * 
     * @return the wrapped material
     */
    public Material getDecoratedMaterial() {
        return decoratedMaterial;
    }
    
    /**
     * Gets the base material (unwraps all decorators).
     * 
     * @return the original material without any decorations
     */
    public Material getBaseMaterial() {
        Material current = decoratedMaterial;
        while (current instanceof MaterialDecorator) {
            current = ((MaterialDecorator) current).getDecoratedMaterial();
        }
        return current;
    }
    
    /**
     * Gets the number of decorators applied to this material.
     * 
     * @return the decorator count
     */
    public int getDecoratorCount() {
        int count = 0;
        Material current = decoratedMaterial;
        while (current instanceof MaterialDecorator) {
            count++;
            current = ((MaterialDecorator) current).getDecoratedMaterial();
        }
        return count;
    }
    
    /**
     * Checks if this material has any decorators applied.
     * 
     * @return true if decorators are applied
     */
    public boolean hasDecorators() {
        return getDecoratorCount() > 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MaterialDecorator that = (MaterialDecorator) obj;
        return Objects.equals(decoratedMaterial, that.decoratedMaterial);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(decoratedMaterial);
    }
    
    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), decoratedMaterial.getTitle());
    }
}
