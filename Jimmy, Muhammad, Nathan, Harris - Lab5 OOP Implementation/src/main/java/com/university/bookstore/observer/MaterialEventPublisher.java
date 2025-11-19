package com.university.bookstore.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.university.bookstore.model.Material;

/**
 * Concrete implementation of MaterialSubject that publishes material events to observers.
 * Provides thread-safe event publishing and observer management.
 * 
 * <p>This class demonstrates the Observer pattern by managing a list of observers
 * and broadcasting events to all registered observers when material events occur.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialEventPublisher implements MaterialSubject {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialEventPublisher.class);
    private final List<MaterialObserver> observers;
    
    /**
     * Creates a new material event publisher.
     */
    public MaterialEventPublisher() {
        // Use CopyOnWriteArrayList for thread safety
        this.observers = new CopyOnWriteArrayList<>();
    }
    
    @Override
    public void addObserver(MaterialObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        
        if (!observers.contains(observer)) {
            observers.add(observer);
            observer.onAdded(this);
        }
    }
    
    @Override
    public boolean removeObserver(MaterialObserver observer) {
        if (observer == null) {
            return false;
        }
        
        boolean removed = observers.remove(observer);
        if (removed) {
            observer.onRemoved(this);
        }
        return removed;
    }
    
    @Override
    public void notifyObservers(MaterialEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        
        for (MaterialObserver observer : observers) {
            try {
                observer.onEvent(event);
            } catch (Exception e) {
                // Log error but don't let one observer's failure affect others
                LOGGER.error("Observer {} failed to handle event: {}", 
                    observer.getObserverName(), event.getEventType(), e);
            }
        }
    }
    
    @Override
    public int getObserverCount() {
        return observers.size();
    }
    
    @Override
    public boolean hasNoObservers() {
        return observers.isEmpty();
    }
    
    @Override
    public void clearObservers() {
        // Notify observers they are being removed
        for (MaterialObserver observer : observers) {
            try {
                observer.onRemoved(this);
            } catch (Exception e) {
                LOGGER.warn("Error notifying observer of removal", e);
            }
        }
        observers.clear();
    }
    
    /**
     * Publishes a material added event.
     * 
     * @param material the material that was added
     */
    public void publishMaterialAdded(Material material) {
        notifyObservers(new MaterialAddedEvent(material));
    }
    
    /**
     * Publishes a price changed event.
     * 
     * @param material the material whose price changed
     * @param oldPrice the previous price
     * @param newPrice the new price
     */
    public void publishPriceChanged(Material material, double oldPrice, double newPrice) {
        notifyObservers(new PriceChangedEvent(material, oldPrice, newPrice));
    }
    
    /**
     * Publishes a custom material event.
     * 
     * @param event the event to publish
     */
    public void publishEvent(MaterialEvent event) {
        notifyObservers(event);
    }
    
    /**
     * Gets a list of all registered observers.
     * 
     * @return list of observers (defensive copy)
     */
    public List<MaterialObserver> getObservers() {
        return new ArrayList<>(observers);
    }
    
    /**
     * Checks if a specific observer is registered.
     * 
     * @param observer the observer to check
     * @return true if the observer is registered
     */
    public boolean hasObserver(MaterialObserver observer) {
        return observers.contains(observer);
    }
    
    /**
     * Gets observers of a specific type.
     * 
     * @param observerType the type of observers to find
     * @return list of observers of the specified type
     */
    public <T extends MaterialObserver> List<T> getObserversOfType(Class<T> observerType) {
        List<T> result = new ArrayList<>();
        for (MaterialObserver observer : observers) {
            if (observerType.isInstance(observer)) {
                result.add(observerType.cast(observer));
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("MaterialEventPublisher[Observers=%d]", getObserverCount());
    }
}
