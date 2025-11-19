package com.university.bookstore.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.university.bookstore.model.Material;

/**
 * Composite component in the Composite pattern.
 * Represents a bundle of materials that can contain other components (both leaves and other bundles).
 * 
 * <p>This class implements the MaterialComponent interface and can contain other MaterialComponent
 * objects, enabling recursive composition and unified treatment of individual materials and bundles.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialBundle implements MaterialComponent {
    
    private final String bundleName;
    private final List<MaterialComponent> components;
    private final double bundleDiscount;
    
    /**
     * Creates a new material bundle with the specified name and discount.
     * 
     * @param bundleName the name of the bundle
     * @param bundleDiscount the discount rate (0.0 to 1.0)
     * @throws IllegalArgumentException if bundleName is null or empty, or discount is invalid
     */
    public MaterialBundle(String bundleName, double bundleDiscount) {
        if (bundleName == null || bundleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bundle name cannot be null or empty");
        }
        
        this.bundleName = bundleName.trim();
        this.components = new ArrayList<>();
        this.bundleDiscount = Math.max(0.0, Math.min(1.0, bundleDiscount));
    }
    
    /**
     * Adds a component to this bundle.
     * 
     * @param component the component to add
     * @throws IllegalArgumentException if component is null
     */
    public void addComponent(MaterialComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        
        // Prevent adding a bundle to itself (directly or indirectly)
        if (component == this || (component instanceof MaterialBundle && containsBundle((MaterialBundle) component))) {
            throw new IllegalArgumentException("Cannot add bundle to itself or create circular references");
        }
        
        components.add(component);
    }
    
    /**
     * Removes a component from this bundle.
     * 
     * @param component the component to remove
     * @return true if the component was removed, false if not found
     */
    public boolean removeComponent(MaterialComponent component) {
        return components.remove(component);
    }
    
    /**
     * Gets all components in this bundle.
     * 
     * @return list of components (defensive copy)
     */
    public List<MaterialComponent> getComponents() {
        return new ArrayList<>(components);
    }
    
    /**
     * Gets the number of components in this bundle.
     * 
     * @return the component count
     */
    public int getComponentCount() {
        return components.size();
    }
    
    @Override
    public String getTitle() {
        return bundleName;
    }
    
    @Override
    public double getPrice() {
        return components.stream()
            .mapToDouble(MaterialComponent::getPrice)
            .sum();
    }
    
    @Override
    public double getDiscountedPrice() {
        double totalPrice = getPrice();
        return totalPrice * (1.0 - bundleDiscount);
    }
    
    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append("Bundle: ").append(bundleName)
            .append(" (").append(getItemCount()).append(" items, ")
            .append(String.format("%.1f%% discount", bundleDiscount * 100))
            .append(")\n");
        
        for (MaterialComponent component : components) {
            desc.append("  - ").append(component.getDescription()).append("\n");
        }
        
        return desc.toString();
    }
    
    @Override
    public List<Material> getMaterials() {
        return components.stream()
            .flatMap(component -> component.getMaterials().stream())
            .collect(Collectors.toList());
    }
    
    @Override
    public int getItemCount() {
        return components.stream()
            .mapToInt(MaterialComponent::getItemCount)
            .sum();
    }
    
    @Override
    public double getDiscountRate() {
        return bundleDiscount;
    }
    
    @Override
    public boolean isLeaf() {
        return false;
    }
    
    /**
     * Gets the bundle name.
     * 
     * @return the bundle name
     */
    public String getBundleName() {
        return bundleName;
    }
    
    /**
     * Gets the bundle discount rate.
     * 
     * @return the discount rate
     */
    public double getBundleDiscount() {
        return bundleDiscount;
    }
    
    /**
     * Calculates the total savings from the bundle discount.
     * 
     * @return the total savings amount
     */
    public double getTotalSavings() {
        return getPrice() - getDiscountedPrice();
    }
    
    /**
     * Checks if this bundle contains any materials of the specified type.
     * 
     * @param type the material type to check for
     * @return true if the bundle contains materials of the specified type
     */
    public boolean containsType(Material.MaterialType type) {
        return getMaterials().stream()
            .anyMatch(material -> material.getType() == type);
    }
    
    /**
     * Gets all materials of the specified type in this bundle.
     * 
     * @param type the material type to filter by
     * @return list of materials of the specified type
     */
    public List<Material> getMaterialsByType(Material.MaterialType type) {
        return getMaterials().stream()
            .filter(material -> material.getType() == type)
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if this bundle contains the specified material.
     * 
     * @param material the material to check for
     * @return true if the bundle contains the material
     */
    public boolean containsMaterial(Material material) {
        return getMaterials().contains(material);
    }
    
    /**
     * Checks if this bundle contains the specified bundle (recursively).
     * 
     * @param bundle the bundle to check for
     * @return true if this bundle contains the specified bundle
     */
    private boolean containsBundle(MaterialBundle bundle) {
        for (MaterialComponent component : components) {
            if (component == bundle) {
                return true;
            }
            if (component instanceof MaterialBundle) {
                MaterialBundle childBundle = (MaterialBundle) component;
                if (childBundle.containsBundle(bundle)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MaterialBundle that = (MaterialBundle) obj;
        return Objects.equals(bundleName, that.bundleName) &&
               Double.compare(that.bundleDiscount, bundleDiscount) == 0 &&
               Objects.equals(components, that.components);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bundleName, bundleDiscount, components);
    }
    
    @Override
    public String toString() {
        return String.format("MaterialBundle[%s, %d items, %.1f%% discount, $%.2f]",
            bundleName, getItemCount(), bundleDiscount * 100, getDiscountedPrice());
    }
}
