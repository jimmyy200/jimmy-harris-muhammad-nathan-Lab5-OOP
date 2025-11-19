package com.university.bookstore.api;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;

/**
 * Modern interface for polymorphic material store with async operations.
 * Extends the base MaterialStore with modern Java features.
 * 
 * @author Navid Mohaghegh
 * @version 4.0
 * @since 2024-09-15
 */
public interface ModernMaterialStore extends MaterialStore {
    
    /**
     * Modern InventoryStats using Java record for immutability.
     * Records provide built-in equals(), hashCode(), and toString() methods.
     * 
     * @param totalCount total number of materials
     * @param averagePrice average price of all materials
     * @param medianPrice median price of all materials
     * @param uniqueTypes number of unique material types
     * @param mediaCount number of media materials
     * @param printCount number of printed materials
     */
    record ModernInventoryStats(
        int totalCount,
        double averagePrice,
        double medianPrice,
        int uniqueTypes,
        int mediaCount,
        int printCount
    ) {
        /**
         * Compact constructor for validation.
         */
        public ModernInventoryStats {
            if (totalCount < 0) {
                throw new IllegalArgumentException("Total count cannot be negative");
            }
            if (averagePrice < 0) {
                throw new IllegalArgumentException("Average price cannot be negative");
            }
            if (medianPrice < 0) {
                throw new IllegalArgumentException("Median price cannot be negative");
            }
            if (uniqueTypes < 0) {
                throw new IllegalArgumentException("Unique types cannot be negative");
            }
            if (mediaCount < 0) {
                throw new IllegalArgumentException("Media count cannot be negative");
            }
            if (printCount < 0) {
                throw new IllegalArgumentException("Print count cannot be negative");
            }
        }
        
        /**
         * Creates an empty stats instance.
         * 
         * @return stats with all zero values
         */
        public static ModernInventoryStats empty() {
            return new ModernInventoryStats(0, 0, 0, 0, 0, 0);
        }
        
        /**
         * Checks if the inventory is empty.
         * 
         * @return true if total count is zero
         */
        public boolean isEmpty() {
            return totalCount == 0;
        }
        
        /**
         * Gets the percentage of media materials.
         * 
         * @return media percentage (0-100)
         */
        public double getMediaPercentage() {
            return totalCount > 0 ? (mediaCount * 100.0) / totalCount : 0;
        }
        
        /**
         * Gets the percentage of print materials.
         * 
         * @return print percentage (0-100)
         */
        public double getPrintPercentage() {
            return totalCount > 0 ? (printCount * 100.0) / totalCount : 0;
        }
        
        /**
         * Creates a summary string for reporting.
         * 
         * @return formatted summary
         */
        public String getSummary() {
            return String.format(
                """
                Inventory Statistics:
                - Total Items: %d
                - Average Price: $%.2f
                - Median Price: $%.2f
                - Unique Types: %d
                - Media Materials: %d (%.1f%%)
                - Print Materials: %d (%.1f%%)
                """,
                totalCount, averagePrice, medianPrice, uniqueTypes,
                mediaCount, getMediaPercentage(),
                printCount, getPrintPercentage()
            );
        }
    }
    
    /**
     * Batch operation result record.
     * 
     * @param successful number of successful operations
     * @param failed number of failed operations
     * @param errors list of error messages
     */
    record BatchOperationResult(
        int successful,
        int failed,
        List<String> errors
    ) {
        /**
         * Checks if all operations were successful.
         * 
         * @return true if no failures
         */
        public boolean isCompleteSuccess() {
            return failed == 0 && (errors == null || errors.isEmpty());
        }
        
        /**
         * Gets the total number of operations.
         * 
         * @return sum of successful and failed
         */
        public int totalOperations() {
            return successful + failed;
        }
        
        /**
         * Gets the success rate.
         * 
         * @return success percentage (0-100)
         */
        public double successRate() {
            int total = totalOperations();
            return total > 0 ? (successful * 100.0) / total : 0;
        }
    }
    
    /**
     * Search criteria record for complex searches.
     * 
     * @param title optional title search term
     * @param creator optional creator search term
     * @param type optional material type filter
     * @param minPrice optional minimum price
     * @param maxPrice optional maximum price
     * @param yearFrom optional start year
     * @param yearTo optional end year
     */
    record SearchCriteria(
        Optional<String> title,
        Optional<String> creator,
        Optional<Material.MaterialType> type,
        Optional<Double> minPrice,
        Optional<Double> maxPrice,
        Optional<Integer> yearFrom,
        Optional<Integer> yearTo
    ) {
        /**
         * Builder for SearchCriteria.
         */
        public static class Builder {
            private Optional<String> title = Optional.empty();
            private Optional<String> creator = Optional.empty();
            private Optional<Material.MaterialType> type = Optional.empty();
            private Optional<Double> minPrice = Optional.empty();
            private Optional<Double> maxPrice = Optional.empty();
            private Optional<Integer> yearFrom = Optional.empty();
            private Optional<Integer> yearTo = Optional.empty();
            
            public Builder withTitle(String title) {
                this.title = Optional.ofNullable(title);
                return this;
            }
            
            public Builder withCreator(String creator) {
                this.creator = Optional.ofNullable(creator);
                return this;
            }
            
            public Builder withType(Material.MaterialType type) {
                this.type = Optional.ofNullable(type);
                return this;
            }
            
            public Builder withPriceRange(Double min, Double max) {
                this.minPrice = Optional.ofNullable(min);
                this.maxPrice = Optional.ofNullable(max);
                return this;
            }
            
            public Builder withYearRange(Integer from, Integer to) {
                this.yearFrom = Optional.ofNullable(from);
                this.yearTo = Optional.ofNullable(to);
                return this;
            }
            
            public SearchCriteria build() {
                return new SearchCriteria(
                    title, creator, type, 
                    minPrice, maxPrice, 
                    yearFrom, yearTo
                );
            }
        }
        
        /**
         * Creates a new builder.
         * 
         * @return new builder instance
         */
        public static Builder builder() {
            return new Builder();
        }
        
        /**
         * Checks if any criteria is specified.
         * 
         * @return true if at least one criterion is present
         */
        public boolean hasAnyCriteria() {
            return title.isPresent() || creator.isPresent() || type.isPresent() ||
                   minPrice.isPresent() || maxPrice.isPresent() ||
                   yearFrom.isPresent() || yearTo.isPresent();
        }
    }
    
    // Async operation methods
    
    /**
     * Adds material asynchronously.
     * 
     * @param material the material to add
     * @return CompletableFuture with the result
     */
    CompletableFuture<Boolean> addMaterialAsync(Material material);
    
    /**
     * Finds material by ID asynchronously.
     * 
     * @param id the material ID
     * @return CompletableFuture with the result
     */
    CompletableFuture<Optional<Material>> findByIdAsync(String id);
    
    /**
     * Searches by title asynchronously.
     * 
     * @param title the title to search for
     * @return CompletableFuture with the results
     */
    CompletableFuture<List<Material>> searchByTitleAsync(String title);
    
    /**
     * Gets inventory statistics asynchronously.
     * 
     * @return CompletableFuture with the statistics
     */
    CompletableFuture<ModernInventoryStats> getModernInventoryStatsAsync();
    
    /**
     * Performs advanced search with multiple criteria.
     * 
     * @param criteria the search criteria
     * @return CompletableFuture with matching materials
     */
    CompletableFuture<List<Material>> advancedSearchAsync(SearchCriteria criteria);
    
    /**
     * Adds multiple materials in batch.
     * 
     * @param materials collection of materials to add
     * @return CompletableFuture with batch operation result
     */
    CompletableFuture<BatchOperationResult> addMaterialsBatchAsync(List<Material> materials);
    
    /**
     * Removes multiple materials in batch.
     * 
     * @param ids collection of IDs to remove
     * @return CompletableFuture with batch operation result
     */
    CompletableFuture<BatchOperationResult> removeMaterialsBatchAsync(List<String> ids);
}