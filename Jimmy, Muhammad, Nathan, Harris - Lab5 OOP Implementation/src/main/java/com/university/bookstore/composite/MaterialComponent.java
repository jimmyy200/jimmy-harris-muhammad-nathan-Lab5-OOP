package com.university.bookstore.composite;

import java.util.List;

import com.university.bookstore.model.Material;

/**
 * Component interface for the Composite pattern.
 * 
 * <p>Represents both individual materials and composite bundles uniformly. The Composite Pattern is a structural design
 * pattern that allows developers to treat individual objects and groups of objects uniformly. This is particularly 
 * useful when working with tree-like structures, where both leaf objects (e.g., individual books) and composite 
 * objects (e.g., bundles of books) need to share the same interface.</p>
 * 
 * <p>The pattern promotes the principle of "compose objects into tree structures to represent part-whole hierarchies."
 * With Composite, clients can operate on single objects and entire compositions through a common interface,
 * simplifying the design and improving extensibility.</p>
 * 
 * <p>This interface allows clients to treat individual materials and material bundles uniformly, enabling 
 * recursive composition and polymorphic operations.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * MaterialComponent book = new MaterialLeaf(new PrintedBook(...));
 * MaterialComponent bundle = new MaterialBundle("Back-to-School Pack", 0.1);
 * bundle.addComponent(book);
 * 
 * // Both can be treated uniformly
 * double price = component.getPrice();
 * String info = component.getDisplayInfo();
 * }</pre>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 * @see MaterialLeaf
 * @see MaterialBundle
 * @see Material
 */
public interface MaterialComponent {
    
    /**
     * Gets the title of this component.
     * For individual materials, this is the material title.
     * For bundles, this is the bundle name.
     * 
     * @return the component title
     */
    String getTitle();
    
    /**
     * Gets the total price of this component.
     * For individual materials, this is the material price.
     * For bundles, this is the sum of all contained materials.
     * 
     * @return the total price
     */
    double getPrice();
    
    /**
     * Gets the discounted price of this component.
     * For individual materials, this applies the material's discount.
     * For bundles, this applies the bundle discount to the total price.
     * 
     * @return the discounted price
     */
    double getDiscountedPrice();
    
    /**
     * Gets a description of this component.
     * Provides detailed information about the component and its contents.
     * 
     * @return the component description
     */
    String getDescription();
    
    /**
     * Gets all materials contained in this component.
     * For individual materials, returns a list containing only itself.
     * For bundles, returns all materials in the bundle (recursively).
     * 
     * @return list of all contained materials
     */
    List<Material> getMaterials();
    
    /**
     * Gets the total number of items in this component.
     * For individual materials, returns 1.
     * For bundles, returns the sum of all contained items.
     * 
     * @return the total item count
     */
    int getItemCount();
    
    /**
     * Gets the discount rate applied to this component.
     * 
     * @return the discount rate (0.0 to 1.0)
     */
    double getDiscountRate();
    
    /**
     * Checks if this component is a leaf (individual material).
     * 
     * @return true if this is a leaf component
     */
    boolean isLeaf();
    
    /**
     * Checks if this component is a composite (bundle).
     * 
     * @return true if this is a composite component
     */
    default boolean isComposite() {
        return !isLeaf();
    }
}
