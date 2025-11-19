package com.university.bookstore.builder;

import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;

/**
 * Builder for creating EBook instances with a fluent interface.
 * Demonstrates the Builder pattern for complex object construction.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class EBookBuilder implements MaterialBuilder<EBook> {
    
    private String id;
    private String title;
    private String author;
    private double price;
    private int year;
    private String fileFormat;
    private double fileSize;
    private boolean drmEnabled = false;
    private int wordCount;
    private Media.MediaQuality quality = Media.MediaQuality.MEDIUM;
    
    /**
     * Sets the eBook ID.
     * 
     * @param id the eBook ID
     * @return this builder for method chaining
     */
    public EBookBuilder setId(String id) {
        this.id = id;
        return this;
    }
    
    /**
     * Sets the eBook title.
     * 
     * @param title the eBook title
     * @return this builder for method chaining
     */
    public EBookBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    
    /**
     * Sets the eBook author.
     * 
     * @param author the eBook author
     * @return this builder for method chaining
     */
    public EBookBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }
    
    /**
     * Sets the eBook price.
     * 
     * @param price the eBook price
     * @return this builder for method chaining
     */
    public EBookBuilder setPrice(double price) {
        this.price = price;
        return this;
    }
    
    /**
     * Sets the eBook publication year.
     * 
     * @param year the publication year
     * @return this builder for method chaining
     */
    public EBookBuilder setYear(int year) {
        this.year = year;
        return this;
    }
    
    /**
     * Sets the eBook file format.
     * 
     * @param fileFormat the file format (e.g., "EPUB", "PDF", "MOBI")
     * @return this builder for method chaining
     */
    public EBookBuilder setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
        return this;
    }
    
    /**
     * Sets the eBook file size.
     * 
     * @param fileSize the file size in MB
     * @return this builder for method chaining
     */
    public EBookBuilder setFileSize(double fileSize) {
        this.fileSize = fileSize;
        return this;
    }
    
    /**
     * Sets the DRM enabled status.
     * 
     * @param drmEnabled true if DRM is enabled
     * @return this builder for method chaining
     */
    public EBookBuilder setDrmEnabled(boolean drmEnabled) {
        this.drmEnabled = drmEnabled;
        return this;
    }
    
    /**
     * Sets the eBook word count.
     * 
     * @param wordCount the word count
     * @return this builder for method chaining
     */
    public EBookBuilder setWordCount(int wordCount) {
        this.wordCount = wordCount;
        return this;
    }
    
    /**
     * Sets the eBook quality.
     * 
     * @param quality the media quality
     * @return this builder for method chaining
     */
    public EBookBuilder setQuality(Media.MediaQuality quality) {
        this.quality = quality;
        return this;
    }
    
    /**
     * Sets DRM enabled to true.
     * 
     * @return this builder for method chaining
     */
    public EBookBuilder enableDRM() {
        this.drmEnabled = true;
        return this;
    }
    
    /**
     * Sets DRM enabled to false.
     * 
     * @return this builder for method chaining
     */
    public EBookBuilder disableDRM() {
        this.drmEnabled = false;
        return this;
    }
    
    /**
     * Sets the quality to high.
     * 
     * @return this builder for method chaining
     */
    public EBookBuilder setHighQuality() {
        this.quality = Media.MediaQuality.HIGH;
        return this;
    }
    
    /**
     * Sets the quality to low.
     * 
     * @return this builder for method chaining
     */
    public EBookBuilder setLowQuality() {
        this.quality = Media.MediaQuality.LOW;
        return this;
    }
    
    @Override
    public EBook build() {
        validate();
        return new EBook(id, title, author, price, year, fileFormat, 
                        fileSize, drmEnabled, wordCount, quality);
    }
    
    @Override
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("ID is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalStateException("Title is required");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalStateException("Author is required");
        }
        if (price < 0) {
            throw new IllegalStateException("Price must be non-negative: " + price);
        }
        if (year < 1000 || year > 2100) {
            throw new IllegalStateException("Year must be between 1000 and 2100: " + year);
        }
        if (fileFormat == null || fileFormat.trim().isEmpty()) {
            throw new IllegalStateException("File format is required");
        }
        if (fileSize < 0) {
            throw new IllegalStateException("File size must be non-negative: " + fileSize);
        }
        if (wordCount < 0) {
            throw new IllegalStateException("Word count must be non-negative: " + wordCount);
        }
        if (quality == null) {
            throw new IllegalStateException("Quality is required");
        }
    }
    
    @Override
    public void reset() {
        this.id = null;
        this.title = null;
        this.author = null;
        this.price = 0.0;
        this.year = 0;
        this.fileFormat = null;
        this.fileSize = 0.0;
        this.drmEnabled = false;
        this.wordCount = 0;
        this.quality = Media.MediaQuality.MEDIUM;
    }
    
    /**
     * Gets the current ID.
     * 
     * @return the current ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the current title.
     * 
     * @return the current title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Gets the current author.
     * 
     * @return the current author
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * Gets the current price.
     * 
     * @return the current price
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Gets the current year.
     * 
     * @return the current year
     */
    public int getYear() {
        return year;
    }
    
    /**
     * Gets the current file format.
     * 
     * @return the current file format
     */
    public String getFileFormat() {
        return fileFormat;
    }
    
    /**
     * Gets the current file size.
     * 
     * @return the current file size
     */
    public double getFileSize() {
        return fileSize;
    }
    
    /**
     * Gets the current DRM enabled status.
     * 
     * @return the current DRM enabled status
     */
    public boolean isDrmEnabled() {
        return drmEnabled;
    }
    
    /**
     * Gets the current word count.
     * 
     * @return the current word count
     */
    public int getWordCount() {
        return wordCount;
    }
    
    /**
     * Gets the current quality.
     * 
     * @return the current quality
     */
    public Media.MediaQuality getQuality() {
        return quality;
    }
    
    @Override
    public String toString() {
        return String.format("EBookBuilder[ID=%s, Title=%s, Author=%s, Price=$%.2f, Year=%d, Format=%s, Size=%.2fMB, DRM=%s, Words=%d, Quality=%s]",
            id, title, author, price, year, fileFormat, fileSize, drmEnabled, wordCount, quality);
    }
}
