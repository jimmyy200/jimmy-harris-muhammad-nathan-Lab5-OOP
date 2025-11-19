package com.university.bookstore.observer;

import java.util.Objects;

import com.university.bookstore.model.Material;

/**
 * Event that occurs when a material is added to the system.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialAddedEvent implements MaterialEvent {
    
    private final Material material;
    private final long timestamp;
    
    /**
     * Creates a new material added event.
     * 
     * @param material the material that was added
     * @throws IllegalArgumentException if material is null
     */
    public MaterialAddedEvent(Material material) {
        this.material = Objects.requireNonNull(material, "Material cannot be null");
        this.timestamp = System.currentTimeMillis();
    }
    
    @Override
    public Material getMaterial() {
        return material;
    }
    
    @Override
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String getEventType() {
        return "MATERIAL_ADDED";
    }
    
    @Override
    public String getDescription() {
        return String.format("Material added: %s (ID: %s, Price: $%.2f)",
            material.getTitle(), material.getId(), material.getPrice());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MaterialAddedEvent that = (MaterialAddedEvent) obj;
        return timestamp == that.timestamp &&
               Objects.equals(material, that.material);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(material, timestamp);
    }
    
    @Override
    public String toString() {
        return String.format("MaterialAddedEvent[%s at %d]", material.getTitle(), timestamp);
    }
}
