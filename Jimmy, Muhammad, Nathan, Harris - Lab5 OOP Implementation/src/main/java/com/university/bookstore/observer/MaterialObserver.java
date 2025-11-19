package com.university.bookstore.observer;

/**
 * Observer interface for material-related events in the Observer pattern.
 * Defines the contract for objects that need to be notified of material events.
 * 
 * <p>This interface allows components to subscribe to material events and be
 * automatically notified when events occur, enabling loose coupling between
 * the event source and event handlers.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public interface MaterialObserver {
    
    /**
     * Called when a material event occurs.
     * 
     * @param event the material event that occurred
     */
    void onEvent(MaterialEvent event);
    
    /**
     * Gets the name of this observer for identification purposes.
     * 
     * @return the observer name
     */
    default String getObserverName() {
        return getClass().getSimpleName();
    }
    
    /**
     * Called when the observer is added to a subject.
     * Can be used for initialization or setup.
     * 
     * @param subject the subject this observer was added to
     */
    default void onAdded(MaterialSubject subject) {
        // Default implementation does nothing
    }
    
    /**
     * Called when the observer is removed from a subject.
     * Can be used for cleanup.
     * 
     * @param subject the subject this observer was removed from
     */
    default void onRemoved(MaterialSubject subject) {
        // Default implementation does nothing
    }
}
