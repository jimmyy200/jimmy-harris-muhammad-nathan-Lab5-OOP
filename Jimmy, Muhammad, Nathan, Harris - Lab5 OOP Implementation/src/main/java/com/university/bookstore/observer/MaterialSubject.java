package com.university.bookstore.observer;

/**
 * Subject interface for material events in the Observer pattern.
 * Defines the contract for objects that can notify observers of material events.
 * 
 * <p>This interface allows components to manage observers and broadcast events,
 * enabling the Observer pattern for loose coupling between event sources and handlers.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public interface MaterialSubject {
    
    /**
     * Adds an observer to receive material events.
     * 
     * @param observer the observer to add
     * @throws IllegalArgumentException if observer is null
     */
    void addObserver(MaterialObserver observer);
    
    /**
     * Removes an observer from receiving material events.
     * 
     * @param observer the observer to remove
     * @return true if the observer was removed, false if not found
     */
    boolean removeObserver(MaterialObserver observer);
    
    /**
     * Notifies all observers of a material event.
     * 
     * @param event the event to broadcast
     * @throws IllegalArgumentException if event is null
     */
    void notifyObservers(MaterialEvent event);
    
    /**
     * Gets the number of registered observers.
     * 
     * @return the observer count
     */
    int getObserverCount();
    
    /**
     * Checks if any observers are registered.
     * 
     * @return true if no observers are registered
     */
    boolean hasNoObservers();
    
    /**
     * Clears all observers.
     */
    void clearObservers();
}
