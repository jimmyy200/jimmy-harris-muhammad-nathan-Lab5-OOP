package com.university.bookstore.repository;

import com.university.bookstore.model.Material;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Wrapper class to ensure proper JSON serialization of Material polymorphic types.
 * This wrapper ensures that Jackson includes type information for each Material.
 */
public class MaterialsWrapper {
    @JsonProperty("materials")
    private List<Material> materials;
    
    public MaterialsWrapper() {
        this.materials = new ArrayList<>();
    }
    
    public MaterialsWrapper(List<Material> materials) {
        this.materials = materials != null ? new ArrayList<>(materials) : new ArrayList<>();
    }
    
    public List<Material> getMaterials() {
        return materials;
    }
    
    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
}