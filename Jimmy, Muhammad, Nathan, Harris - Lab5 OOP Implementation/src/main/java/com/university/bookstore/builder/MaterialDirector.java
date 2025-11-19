package com.university.bookstore.builder;

import java.util.List;

import com.university.bookstore.composite.MaterialBundle;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;

/**
 * Director class that orchestrates the construction of complex Material objects
 * using various builders. Demonstrates the Director pattern in conjunction
 * with the Builder pattern for creating predefined configurations.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class MaterialDirector {
    
    private final EBookBuilder eBookBuilder;
    private final MaterialBundleBuilder bundleBuilder;
    
    /**
     * Constructs a MaterialDirector with the specified builders.
     * 
     * @param eBookBuilder the EBook builder
     * @param bundleBuilder the MaterialBundle builder
     */
    public MaterialDirector(EBookBuilder eBookBuilder, MaterialBundleBuilder bundleBuilder) {
        this.eBookBuilder = eBookBuilder;
        this.bundleBuilder = bundleBuilder;
    }
    
    /**
     * Constructs a premium EBook with all features enabled.
     * 
     * @param id the EBook ID
     * @param title the EBook title
     * @param author the EBook author
     * @param price the EBook price
     * @return a premium EBook
     */
    public Material buildPremiumEBook(String id, String title, String author, double price) {
        eBookBuilder.reset();
        return eBookBuilder
            .setId(id)
            .setTitle(title)
            .setAuthor(author)
            .setPrice(price)
            .setFileFormat("EPUB")
            .setFileSize(1024 * 1024 * 5) // 5MB
            .setDrmEnabled(false)
            .setWordCount(50000)
            .setQuality(Media.MediaQuality.HIGH)
            .build();
    }
    
    /**
     * Constructs a basic EBook with minimal features.
     * 
     * @param id the EBook ID
     * @param title the EBook title
     * @param author the EBook author
     * @param price the EBook price
     * @return a basic EBook
     */
    public Material buildBasicEBook(String id, String title, String author, double price) {
        eBookBuilder.reset();
        return eBookBuilder
            .setId(id)
            .setTitle(title)
            .setAuthor(author)
            .setPrice(price)
            .setFileFormat("PDF")
            .setFileSize(1024 * 1024 * 2) // 2MB
            .setDrmEnabled(true)
            .setWordCount(30000)
            .setQuality(Media.MediaQuality.MEDIUM)
            .build();
    }
    
    /**
     * Constructs a student EBook with educational features.
     * 
     * @param id the EBook ID
     * @param title the EBook title
     * @param author the EBook author
     * @param price the EBook price
     * @return a student EBook
     */
    public Material buildStudentEBook(String id, String title, String author, double price) {
        eBookBuilder.reset();
        return eBookBuilder
            .setId(id)
            .setTitle(title)
            .setAuthor(author)
            .setPrice(price)
            .setFileFormat("EPUB")
            .setFileSize(1024 * 1024 * 3) // 3MB
            .setDrmEnabled(false)
            .setWordCount(40000)
            .setQuality(Media.MediaQuality.HIGH)
            .build();
    }
    
    /**
     * Constructs a textbook bundle with a 20% discount.
     * 
     * @param bundleName the bundle name
     * @param materials the materials to include in the bundle
     * @return a textbook bundle
     */
    public MaterialBundle buildTextbookBundle(String bundleName, List<Material> materials) {
        bundleBuilder.reset();
        return bundleBuilder
            .setBundleName(bundleName)
            .setMediumDiscount() // 20% discount
            .addMaterials(materials)
            .build();
    }
    
    /**
     * Constructs a course bundle with a 25% discount.
     * 
     * @param bundleName the bundle name
     * @param materials the materials to include in the bundle
     * @return a course bundle
     */
    public MaterialBundle buildCourseBundle(String bundleName, List<Material> materials) {
        bundleBuilder.reset();
        return bundleBuilder
            .setBundleName(bundleName)
            .setBundleDiscount(0.25) // 25% discount
            .addMaterials(materials)
            .build();
    }
    
    /**
     * Constructs a premium bundle with a 30% discount.
     * 
     * @param bundleName the bundle name
     * @param materials the materials to include in the bundle
     * @return a premium bundle
     */
    public MaterialBundle buildPremiumBundle(String bundleName, List<Material> materials) {
        bundleBuilder.reset();
        return bundleBuilder
            .setBundleName(bundleName)
            .setLargeDiscount() // 30% discount
            .addMaterials(materials)
            .build();
    }
    
    /**
     * Constructs a starter bundle with a 10% discount.
     * 
     * @param bundleName the bundle name
     * @param materials the materials to include in the bundle
     * @return a starter bundle
     */
    public MaterialBundle buildStarterBundle(String bundleName, List<Material> materials) {
        bundleBuilder.reset();
        return bundleBuilder
            .setBundleName(bundleName)
            .setSmallDiscount() // 10% discount
            .addMaterials(materials)
            .build();
    }
    
    /**
     * Constructs a nested bundle (bundle containing other bundles).
     * 
     * @param bundleName the bundle name
     * @param bundles the bundles to include
     * @param discount the discount rate
     * @return a nested bundle
     */
    public MaterialBundle buildNestedBundle(String bundleName, List<MaterialBundle> bundles, double discount) {
        bundleBuilder.reset();
        MaterialBundleBuilder builder = bundleBuilder
            .setBundleName(bundleName)
            .setBundleDiscount(discount);
        
        for (MaterialBundle bundle : bundles) {
            builder.addBundle(bundle);
        }
        
        return builder.build();
    }
    
    /**
     * Constructs a custom bundle with specified parameters.
     * 
     * @param bundleName the bundle name
     * @param materials the materials to include
     * @param discount the discount rate
     * @return a custom bundle
     */
    public MaterialBundle buildCustomBundle(String bundleName, List<Material> materials, double discount) {
        bundleBuilder.reset();
        return bundleBuilder
            .setBundleName(bundleName)
            .setBundleDiscount(discount)
            .addMaterials(materials)
            .build();
    }
    
    /**
     * Gets the EBook builder for custom configurations.
     * 
     * @return the EBook builder
     */
    public EBookBuilder getEBookBuilder() {
        return eBookBuilder;
    }
    
    /**
     * Gets the MaterialBundle builder for custom configurations.
     * 
     * @return the MaterialBundle builder
     */
    public MaterialBundleBuilder getBundleBuilder() {
        return bundleBuilder;
    }
    
    /**
     * Creates a new EBook builder instance.
     * 
     * @return a new EBook builder
     */
    public EBookBuilder createEBookBuilder() {
        return new EBookBuilder();
    }
    
    /**
     * Creates a new MaterialBundle builder instance.
     * 
     * @return a new MaterialBundle builder
     */
    public MaterialBundleBuilder createBundleBuilder() {
        return new MaterialBundleBuilder();
    }
}
