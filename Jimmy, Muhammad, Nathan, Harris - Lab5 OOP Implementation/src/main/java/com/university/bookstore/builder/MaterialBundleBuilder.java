package com.university.bookstore.builder;

import java.util.ArrayList;
import java.util.List;

import com.university.bookstore.composite.MaterialBundle;
import com.university.bookstore.composite.MaterialComponent;
import com.university.bookstore.composite.MaterialLeaf;
import com.university.bookstore.model.Material;

/**
 * Builder for creating MaterialBundle instances with a fluent interface.
 * Demonstrates the Builder pattern for complex composite object construction.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialBundleBuilder implements ComponentBuilder<MaterialBundle> {
    
    private String bundleName;
    private double bundleDiscount = 0.0;
    private final List<MaterialComponent> components = new ArrayList<>();
    
    /**
     * Sets the bundle name.
     * 
     * @param bundleName the bundle name
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder setBundleName(String bundleName) {
        this.bundleName = bundleName;
        return this;
    }
    
    /**
     * Sets the bundle discount rate.
     * 
     * @param discount the discount rate (0.0 to 1.0)
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder setBundleDiscount(double discount) {
        this.bundleDiscount = Math.max(0.0, Math.min(1.0, discount));
        return this;
    }
    
    /**
     * Sets the bundle discount percentage.
     * 
     * @param discountPercent the discount percentage (0.0 to 100.0)
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder setBundleDiscountPercent(double discountPercent) {
        return setBundleDiscount(discountPercent / 100.0);
    }
    
    /**
     * Adds a material to the bundle.
     * 
     * @param material the material to add
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder addMaterial(Material material) {
        if (material != null) {
            components.add(new MaterialLeaf(material));
        }
        return this;
    }
    
    /**
     * Adds a bundle to this bundle (nested bundles).
     * 
     * @param bundle the bundle to add
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder addBundle(MaterialBundle bundle) {
        if (bundle != null) {
            components.add(bundle);
        }
        return this;
    }
    
    /**
     * Adds a material component to the bundle.
     * 
     * @param component the component to add
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder addComponent(MaterialComponent component) {
        if (component != null) {
            components.add(component);
        }
        return this;
    }
    
    /**
     * Adds multiple materials to the bundle.
     * 
     * @param materials the materials to add
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder addMaterials(List<Material> materials) {
        if (materials != null) {
            for (Material material : materials) {
                addMaterial(material);
            }
        }
        return this;
    }
    
    /**
     * Adds multiple components to the bundle.
     * 
     * @param components the components to add
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder addComponents(List<MaterialComponent> components) {
        if (components != null) {
            for (MaterialComponent component : components) {
                addComponent(component);
            }
        }
        return this;
    }
    
    /**
     * Removes a material from the bundle.
     * 
     * @param material the material to remove
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder removeMaterial(Material material) {
        if (material != null) {
            components.removeIf(component -> 
                component instanceof MaterialLeaf && 
                ((MaterialLeaf) component).getMaterial().equals(material));
        }
        return this;
    }
    
    /**
     * Removes a component from the bundle.
     * 
     * @param component the component to remove
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder removeComponent(MaterialComponent component) {
        components.remove(component);
        return this;
    }
    
    /**
     * Clears all components from the bundle.
     * 
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder clearComponents() {
        components.clear();
        return this;
    }
    
    /**
     * Sets a 10% discount.
     * 
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder setSmallDiscount() {
        return setBundleDiscount(0.10);
    }
    
    /**
     * Sets a 20% discount.
     * 
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder setMediumDiscount() {
        return setBundleDiscount(0.20);
    }
    
    /**
     * Sets a 30% discount.
     * 
     * @return this builder for method chaining
     */
    public MaterialBundleBuilder setLargeDiscount() {
        return setBundleDiscount(0.30);
    }
    
    @Override
    public MaterialBundle build() {
        validate();
        
        MaterialBundle bundle = new MaterialBundle(bundleName, bundleDiscount);
        for (MaterialComponent component : components) {
            bundle.addComponent(component);
        }
        return bundle;
    }
    
    @Override
    public void validate() {
        if (bundleName == null || bundleName.trim().isEmpty()) {
            throw new IllegalStateException("Bundle name is required");
        }
        if (bundleDiscount < 0.0 || bundleDiscount > 1.0) {
            throw new IllegalStateException("Bundle discount must be between 0.0 and 1.0: " + bundleDiscount);
        }
    }
    
    @Override
    public void reset() {
        this.bundleName = null;
        this.bundleDiscount = 0.0;
        this.components.clear();
    }
    
    /**
     * Gets the current bundle name.
     * 
     * @return the current bundle name
     */
    public String getBundleName() {
        return bundleName;
    }
    
    /**
     * Gets the current bundle discount.
     * 
     * @return the current bundle discount
     */
    public double getBundleDiscount() {
        return bundleDiscount;
    }
    
    /**
     * Gets the current bundle discount percentage.
     * 
     * @return the current bundle discount percentage
     */
    public double getBundleDiscountPercent() {
        return bundleDiscount * 100.0;
    }
    
    /**
     * Gets the current components.
     * 
     * @return the current components
     */
    public List<MaterialComponent> getComponents() {
        return new ArrayList<>(components);
    }
    
    /**
     * Gets the number of components.
     * 
     * @return the component count
     */
    public int getComponentCount() {
        return components.size();
    }
    
    /**
     * Checks if the bundle has any components.
     * 
     * @return true if the bundle has components
     */
    public boolean hasComponents() {
        return !components.isEmpty();
    }
    
    /**
     * Gets the total price of all components.
     * 
     * @return the total price
     */
    public double getTotalPrice() {
        return components.stream()
            .mapToDouble(MaterialComponent::getPrice)
            .sum();
    }
    
    /**
     * Gets the total discounted price of all components.
     * 
     * @return the total discounted price
     */
    public double getTotalDiscountedPrice() {
        double totalPrice = getTotalPrice();
        return totalPrice * (1.0 - bundleDiscount);
    }
    
    /**
     * Gets the total savings from the bundle discount.
     * 
     * @return the total savings
     */
    public double getTotalSavings() {
        return getTotalPrice() - getTotalDiscountedPrice();
    }
    
    @Override
    public String toString() {
        return String.format("MaterialBundleBuilder[Name=%s, Discount=%.1f%%, Components=%d, TotalPrice=$%.2f, Savings=$%.2f]",
            bundleName, getBundleDiscountPercent(), getComponentCount(), getTotalPrice(), getTotalSavings());
    }
}
