package com.university.bookstore.observer;

import com.university.bookstore.model.Material;

/**
 * Base interface for material-related domain events in the Observer pattern.
 * Represents events that occur in the material management system.
 * 
 * <p>This interface defines the contract for all material events, allowing
 * the system to broadcast domain events to interested observers without
 * tight coupling between components.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public interface MaterialEvent {
    
    /**
     * Gets the material associated with this event.
     * 
     * @return the material
     */
    Material getMaterial();
    
    /**
     * Gets the timestamp when this event occurred.
     * 
     * @return the event timestamp in milliseconds
     */
    long getTimestamp();
    
    /**
     * Gets the type of this event.
     * 
     * @return the event type
     */
    String getEventType();
    
    /**
     * Gets a description of this event.
     * 
     * @return the event description
     */
    String getDescription();
}
