package com.university.bookstore.decorator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.university.bookstore.model.Material;

/**
 * Decorator that adds digital annotation functionality to materials.
 * Increases the price and provides annotation management capabilities.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class DigitalAnnotationDecorator extends MaterialDecorator {
    
    private static final double ANNOTATION_COST = 2.99;
    private final List<String> annotations;
    
    /**
     * Creates a new digital annotation decorator.
     * 
     * @param material the material to add annotations to
     */
    public DigitalAnnotationDecorator(Material material) {
        super(material);
        this.annotations = new ArrayList<>();
    }
    
    @Override
    public double getPrice() {
        return decoratedMaterial.getPrice() + ANNOTATION_COST;
    }
    
    @Override
    public String getDisplayInfo() {
        return decoratedMaterial.getDisplayInfo() + 
               String.format(" [Digital Annotations: %d notes (+$%.2f)]", 
                           annotations.size(), ANNOTATION_COST);
    }
    
    /**
     * Adds an annotation to the material.
     * 
     * @param annotation the annotation text
     * @throws IllegalArgumentException if annotation is null or empty
     */
    public void addAnnotation(String annotation) {
        if (annotation == null || annotation.trim().isEmpty()) {
            throw new IllegalArgumentException("Annotation cannot be null or empty");
        }
        annotations.add(annotation.trim());
    }
    
    /**
     * Removes an annotation by index.
     * 
     * @param index the index of the annotation to remove
     * @return the removed annotation
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public String removeAnnotation(int index) {
        if (index < 0 || index >= annotations.size()) {
            throw new IndexOutOfBoundsException("Invalid annotation index: " + index);
        }
        return annotations.remove(index);
    }
    
    /**
     * Gets all annotations.
     * 
     * @return unmodifiable list of annotations
     */
    public List<String> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }
    
    /**
     * Gets the number of annotations.
     * 
     * @return the annotation count
     */
    public int getAnnotationCount() {
        return annotations.size();
    }
    
    /**
     * Clears all annotations.
     */
    public void clearAnnotations() {
        annotations.clear();
    }
    
    /**
     * Gets the digital annotation cost.
     * 
     * @return the annotation cost
     */
    public double getDigitalAnnotationCost() {
        return ANNOTATION_COST;
    }
    
    /**
     * Checks if the material has any annotations.
     * 
     * @return true if annotations exist
     */
    public boolean hasAnnotations() {
        return !annotations.isEmpty();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        
        DigitalAnnotationDecorator that = (DigitalAnnotationDecorator) obj;
        return Objects.equals(annotations, that.annotations);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), annotations);
    }
}
