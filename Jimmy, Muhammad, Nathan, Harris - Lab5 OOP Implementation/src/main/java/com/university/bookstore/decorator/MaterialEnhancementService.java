package com.university.bookstore.decorator;

import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Material;

/**
 * Service for managing material enhancements using the Decorator pattern.
 * Provides methods to apply various decorators to materials and create enhancement packages.
 * 
 * <p>This service demonstrates the Decorator pattern in action by providing
 * a high-level interface for dynamically adding features to materials.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialEnhancementService {
    
    /**
     * Adds gift wrapping to a material.
     * 
     * @param material the material to enhance
     * @param style the wrapping style
     * @return the enhanced material with gift wrapping
     * @throws IllegalArgumentException if material or style is null
     */
    public Material addGiftWrapping(Material material, String style) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        if (style == null || style.trim().isEmpty()) {
            throw new IllegalArgumentException("Wrapping style cannot be null or empty");
        }
        
        return new GiftWrappingDecorator(material, style);
    }
    
    /**
     * Adds expedited delivery to a material.
     * 
     * @param material the material to enhance
     * @param deliveryDays the number of days for delivery
     * @return the enhanced material with expedited delivery
     * @throws IllegalArgumentException if material is null or deliveryDays is invalid
     */
    public Material addExpeditedDelivery(Material material, int deliveryDays) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        if (deliveryDays < 1) {
            throw new IllegalArgumentException("Delivery days must be at least 1: " + deliveryDays);
        }
        
        return new ExpeditedDeliveryDecorator(material, deliveryDays);
    }
    
    /**
     * Adds digital annotations to a material.
     * 
     * @param material the material to enhance
     * @return the enhanced material with digital annotations
     * @throws IllegalArgumentException if material is null
     */
    public Material addDigitalAnnotations(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        return new DigitalAnnotationDecorator(material);
    }
    
    /**
     * Creates a premium package with multiple enhancements.
     * 
     * @param material the material to enhance
     * @param giftStyle the gift wrapping style
     * @param deliveryDays the number of days for delivery
     * @return the enhanced material with premium package
     * @throws IllegalArgumentException if material is null or parameters are invalid
     */
    public Material createPremiumPackage(Material material, String giftStyle, int deliveryDays) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        Material enhanced = material;
        enhanced = addGiftWrapping(enhanced, giftStyle);
        enhanced = addExpeditedDelivery(enhanced, deliveryDays);
        
        // Add digital annotations if it's an e-book
        if (material instanceof EBook) {
            enhanced = addDigitalAnnotations(enhanced);
        }
        
        return enhanced;
    }
    
    /**
     * Creates a gift package with gift wrapping and expedited delivery.
     * 
     * @param material the material to enhance
     * @param giftStyle the gift wrapping style
     * @param deliveryDays the number of days for delivery
     * @return the enhanced material with gift package
     */
    public Material createGiftPackage(Material material, String giftStyle, int deliveryDays) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        Material enhanced = material;
        enhanced = addGiftWrapping(enhanced, giftStyle);
        enhanced = addExpeditedDelivery(enhanced, deliveryDays);
        
        return enhanced;
    }
    
    /**
     * Creates a digital package with digital annotations (for e-books only).
     * 
     * @param material the material to enhance
     * @return the enhanced material with digital package, or original if not an e-book
     */
    public Material createDigitalPackage(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        if (material instanceof EBook) {
            return addDigitalAnnotations(material);
        }
        
        return material; // Return original if not an e-book
    }
    
    /**
     * Calculates the total enhancement cost for a material.
     * 
     * @param original the original material
     * @param enhanced the enhanced material
     * @return the total enhancement cost
     */
    public double calculateEnhancementCost(Material original, Material enhanced) {
        if (original == null || enhanced == null) {
            throw new IllegalArgumentException("Materials cannot be null");
        }
        
        return enhanced.getPrice() - original.getPrice();
    }
    
    /**
     * Gets a summary of enhancements applied to a material.
     * 
     * @param material the material to analyze
     * @return enhancement summary
     */
    public String getEnhancementSummary(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        StringBuilder summary = new StringBuilder();
        
        // Check if there are any enhancements
        if (!hasEnhancements(material)) {
            summary.append("No enhancements applied");
            return summary.toString();
        }
        
        summary.append("Enhancements:\n");
        
        // Check for decorators by examining the display info
        String displayInfo = material.getDisplayInfo();
        if (displayInfo.contains("Gift Wrapped")) {
            summary.append("// [OK] Gift Wrapping\n");
        }
        if (displayInfo.contains("Expedited Delivery")) {
            summary.append("// [OK] Expedited Delivery\n");
        }
        if (displayInfo.contains("Digital Annotations")) {
            summary.append("// [OK] Digital Annotations\n");
        }
        
        double enhancementCost = material.getPrice() - getBasePrice(material);
        summary.append("Total Cost: $").append(String.format("%.2f", material.getPrice()));
        
        return summary.toString();
    }
    
    /**
     * Gets the base price of a material (without decorators).
     * 
     * @param material the material to analyze
     * @return the base price
     */
    public double getBasePrice(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        Material current = material;
        while (current instanceof MaterialDecorator) {
            current = ((MaterialDecorator) current).getDecoratedMaterial();
        }
        
        return current.getPrice();
    }
    
    /**
     * Checks if a material has any enhancements applied.
     * 
     * @param material the material to check
     * @return true if enhancements are applied
     */
    public boolean hasEnhancements(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        return material instanceof MaterialDecorator;
    }
    
    /**
     * Gets the number of decorators applied to a material.
     * 
     * @param material the material to analyze
     * @return the decorator count
     */
    public int getEnhancementCount(Material material) {
        if (material == null) {
            return 0;
        }
        
        int count = 0;
        Material current = material;
        while (current instanceof MaterialDecorator) {
            count++;
            current = ((MaterialDecorator) current).getDecoratedMaterial();
        }
        
        return count;
    }
    
    /**
     * Gets the base material (unwraps all decorators).
     * 
     * @param material the material to unwrap
     * @return the base material
     */
    public Material getBaseMaterial(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        
        Material current = material;
        while (current instanceof MaterialDecorator) {
            current = ((MaterialDecorator) current).getDecoratedMaterial();
        }
        
        return current;
    }
    
    /**
     * Checks if a material has a specific type of enhancement.
     * 
     * @param material the material to check
     * @param enhancementType the type of enhancement to look for
     * @return true if the enhancement is applied
     */
    public boolean hasEnhancement(Material material, Class<? extends MaterialDecorator> enhancementType) {
        if (material == null || enhancementType == null) {
            return false;
        }
        
        Material current = material;
        while (current instanceof MaterialDecorator) {
            if (enhancementType.isInstance(current)) {
                return true;
            }
            current = ((MaterialDecorator) current).getDecoratedMaterial();
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return "MaterialEnhancementService[Available enhancements: Gift Wrapping, Expedited Delivery, Digital Annotations]";
    }
}
