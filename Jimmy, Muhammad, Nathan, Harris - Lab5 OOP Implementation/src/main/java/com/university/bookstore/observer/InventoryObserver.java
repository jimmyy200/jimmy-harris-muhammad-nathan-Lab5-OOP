package com.university.bookstore.observer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.university.bookstore.model.Material;

/**
 * Observer that tracks inventory changes for materials.
 * Maintains counts and statistics for materials in the system.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class InventoryObserver implements MaterialObserver {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryObserver.class);
    
    private final Map<String, Integer> inventoryCounts;
    private final Map<String, Double> totalValue;
    private int totalEvents;
    
    /**
     * Creates a new inventory observer.
     */
    public InventoryObserver() {
        this.inventoryCounts = new HashMap<>();
        this.totalValue = new HashMap<>();
        this.totalEvents = 0;
    }
    
    @Override
    public void onEvent(MaterialEvent event) {
        totalEvents++;
        
        switch (event.getEventType()) {
            case "MATERIAL_ADDED":
                handleMaterialAdded(event);
                break;
            case "PRICE_CHANGED":
                handlePriceChanged(event);
                break;
            default:
                // Handle other event types if needed
                break;
        }
    }
    
    /**
     * Handles material added events.
     */
    private void handleMaterialAdded(MaterialEvent event) {
        Material material = event.getMaterial();
        String materialId = material.getId();
        
        // Update inventory count
        inventoryCounts.merge(materialId, 1, Integer::sum);
        
        // Update total value
        totalValue.merge(materialId, material.getPrice(), Double::sum);
        
        LOGGER.info("Inventory updated: {} count: {}", 
            materialId, inventoryCounts.get(materialId));
    }
    
    /**
     * Handles price changed events.
     */
    private void handlePriceChanged(MaterialEvent event) {
        if (event instanceof PriceChangedEvent) {
            PriceChangedEvent priceEvent = (PriceChangedEvent) event;
            Material material = event.getMaterial();
            String materialId = material.getId();
            
            // Update total value with the price change
            double priceChange = priceEvent.getPriceChange();
            totalValue.merge(materialId, priceChange, Double::sum);
            
            LOGGER.info("Price changed for {}: ${} -> ${}", 
                material.getTitle(), priceEvent.getOldPrice(), priceEvent.getNewPrice());
        }
    }
    
    /**
     * Gets the inventory count for a specific material.
     * 
     * @param materialId the material ID
     * @return the inventory count
     */
    public int getInventoryCount(String materialId) {
        return inventoryCounts.getOrDefault(materialId, 0);
    }
    
    /**
     * Gets the total value for a specific material.
     * 
     * @param materialId the material ID
     * @return the total value
     */
    public double getTotalValue(String materialId) {
        return totalValue.getOrDefault(materialId, 0.0);
    }
    
    /**
     * Gets the total number of materials in inventory.
     * 
     * @return the total count
     */
    public int getTotalInventoryCount() {
        return inventoryCounts.values().stream()
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    /**
     * Gets the total value of all materials in inventory.
     * 
     * @return the total value
     */
    public double getTotalInventoryValue() {
        return totalValue.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum();
    }
    
    /**
     * Gets the number of unique materials in inventory.
     * 
     * @return the unique material count
     */
    public int getUniqueMaterialCount() {
        return inventoryCounts.size();
    }
    
    /**
     * Gets the total number of events processed.
     * 
     * @return the event count
     */
    public int getTotalEvents() {
        return totalEvents;
    }
    
    /**
     * Gets all inventory counts.
     * 
     * @return map of material ID to count
     */
    public Map<String, Integer> getAllInventoryCounts() {
        return new HashMap<>(inventoryCounts);
    }
    
    /**
     * Gets all total values.
     * 
     * @return map of material ID to total value
     */
    public Map<String, Double> getAllTotalValues() {
        return new HashMap<>(totalValue);
    }
    
    /**
     * Clears all inventory data.
     */
    public void clear() {
        inventoryCounts.clear();
        totalValue.clear();
        totalEvents = 0;
    }
    
    @Override
    public String getObserverName() {
        return "InventoryObserver";
    }
    
    @Override
    public String toString() {
        return String.format("InventoryObserver[Materials=%d, TotalCount=%d, TotalValue=$%.2f, Events=%d]",
            getUniqueMaterialCount(), getTotalInventoryCount(), getTotalInventoryValue(), getTotalEvents());
    }
}
