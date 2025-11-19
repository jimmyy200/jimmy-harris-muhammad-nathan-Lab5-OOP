package com.university.bookstore.composite;

import java.util.List;
import java.util.Objects;

import com.university.bookstore.model.Material;

/**
 * Leaf component in the Composite pattern.
 * Represents an individual material that cannot contain other components.
 * 
 * <p>This class wraps a Material object and implements the MaterialComponent interface,
 * allowing individual materials to be treated uniformly with composite bundles.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialLeaf implements MaterialComponent {
    
    private final Material material;
    
    /**
     * Creates a new material leaf wrapping the specified material.
     * 
     * @param material the material to wrap
     * @throws IllegalArgumentException if material is null
     */
    public MaterialLeaf(Material material) {
        this.material = Objects.requireNonNull(material, "Material cannot be null");
    }
    
    @Override
    public String getTitle() {
        return material.getTitle();
    }
    
    @Override
    public double getPrice() {
        return material.getPrice();
    }
    
    @Override
    public double getDiscountedPrice() {
        return material.getDiscountedPrice();
    }
    
    @Override
    public String getDescription() {
        return material.getDisplayInfo();
    }
    
    @Override
    public List<Material> getMaterials() {
        return List.of(material);
    }
    
    @Override
    public int getItemCount() {
        return 1;
    }
    
    @Override
    public double getDiscountRate() {
        return material.getDiscountRate();
    }
    
    @Override
    public boolean isLeaf() {
        return true;
    }
    
    /**
     * Gets the wrapped material.
     * 
     * @return the underlying material
     */
    public Material getMaterial() {
        return material;
    }
    
    /**
     * Gets the material ID.
     * 
     * @return the material ID
     */
    public String getId() {
        return material.getId();
    }
    
    /**
     * Gets the material type.
     * 
     * @return the material type
     */
    public Material.MaterialType getType() {
        return material.getType();
    }
    
    /**
     * Gets the material creator.
     * 
     * @return the material creator
     */
    public String getCreator() {
        return material.getCreator();
    }
    
    /**
     * Gets the material year.
     * 
     * @return the material year
     */
    public int getYear() {
        return material.getYear();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MaterialLeaf that = (MaterialLeaf) obj;
        return Objects.equals(material, that.material);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(material);
    }
    
    @Override
    public String toString() {
        return String.format("MaterialLeaf[%s]", material.getTitle());
    }
}
