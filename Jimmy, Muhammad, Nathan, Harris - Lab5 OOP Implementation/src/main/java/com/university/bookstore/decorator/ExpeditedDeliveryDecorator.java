package com.university.bookstore.decorator;

import java.util.Objects;

import com.university.bookstore.model.Material;

/**
 * Decorator that adds expedited delivery functionality to materials.
 * Increases the price and provides delivery time information.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class ExpeditedDeliveryDecorator extends MaterialDecorator {
    
    private static final double EXPEDITED_COST = 12.99;
    private final int deliveryDays;
    
    /**
     * Creates a new expedited delivery decorator.
     * 
     * @param material the material to add expedited delivery to
     * @param deliveryDays the number of days for delivery (minimum 1)
     * @throws IllegalArgumentException if deliveryDays is less than 1
     */
    public ExpeditedDeliveryDecorator(Material material, int deliveryDays) {
        super(material);
        if (deliveryDays < 1) {
            throw new IllegalArgumentException("Delivery days must be at least 1: " + deliveryDays);
        }
        this.deliveryDays = deliveryDays;
    }
    
    @Override
    public double getPrice() {
        return decoratedMaterial.getPrice() + EXPEDITED_COST;
    }
    
    @Override
    public String getDisplayInfo() {
        return decoratedMaterial.getDisplayInfo() + 
               String.format(" [Expedited Delivery: %d days (+$%.2f)]", 
                           deliveryDays, EXPEDITED_COST);
    }
    
    /**
     * Gets the delivery time in days.
     * 
     * @return the delivery days
     */
    public int getDeliveryDays() {
        return deliveryDays;
    }
    
    /**
     * Gets the expedited delivery cost.
     * 
     * @return the expedited delivery cost
     */
    public double getExpeditedDeliveryCost() {
        return EXPEDITED_COST;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        
        ExpeditedDeliveryDecorator that = (ExpeditedDeliveryDecorator) obj;
        return deliveryDays == that.deliveryDays;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deliveryDays);
    }
}
