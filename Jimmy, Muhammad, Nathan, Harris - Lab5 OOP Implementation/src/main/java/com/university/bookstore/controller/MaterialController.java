package com.university.bookstore.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.bookstore.api.MaterialStore;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Material.MaterialType;

/**
 * REST Controller for Material management operations.
 * 
 * <p>This controller demonstrates how the existing MaterialStore interface
 * can be easily exposed as REST endpoints without modifying the core
 * business logic. It showcases the extensibility of the hexagonal
 * architecture design.</p>
 * 
 * <p>All operations delegate to the existing MaterialStore implementation,
 * maintaining the same business logic and validation rules.</p>
 * 
 * @author Navid Mohaghegh
 * @version 1.0
 * @since 2024-12-19
 */
@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialStore materialStore;

    /**
     * Constructor with dependency injection of MaterialStore.
     * 
     * @param materialStore the material store implementation
     */
    public MaterialController(MaterialStore materialStore) {
        this.materialStore = materialStore;
    }

    /**
     * GET /api/materials - Retrieve all materials
     * 
     * @return list of all materials
     */
    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialStore.getAllMaterials();
        return ResponseEntity.ok(materials);
    }

    /**
     * GET /api/materials/{id} - Retrieve a specific material by ID
     * 
     * @param id the material ID
     * @return the material if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable("id") String id) {
        Optional<Material> material = materialStore.findById(id);
        return material.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/materials - Create a new material
     * 
     * @param material the material to create
     * @return the created material or 400 if invalid
     */
    @PostMapping
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) {
        try {
            boolean added = materialStore.addMaterial(material);
            if (added) {
                return ResponseEntity.status(HttpStatus.CREATED).body(material);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/materials/{id} - Update an existing material
     * 
     * @param id the material ID
     * @param material the updated material
     * @return the updated material or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable("id") String id, @RequestBody Material material) {
        // Remove existing material and add the updated one
        Optional<Material> existing = materialStore.removeMaterial(id);
        if (existing.isPresent()) {
            boolean added = materialStore.addMaterial(material);
            if (added) {
                return ResponseEntity.ok(material);
            } else {
                // Rollback: add the original material back
                materialStore.addMaterial(existing.get());
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/materials/{id} - Delete a material
     * 
     * @param id the material ID
     * @return 204 if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable("id") String id) {
        Optional<Material> removed = materialStore.removeMaterial(id);
        return removed.isPresent() ? 
            ResponseEntity.noContent().build() : 
            ResponseEntity.notFound().build();
    }

    /**
     * GET /api/materials/search/title?q={query} - Search materials by title
     * 
     * @param q the search query
     * @return list of matching materials
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<Material>> searchByTitle(@RequestParam("q") String q) {
        List<Material> materials = materialStore.searchByTitle(q);
        return ResponseEntity.ok(materials);
    }

    /**
     * GET /api/materials/search/creator?q={query} - Search materials by creator
     * 
     * @param q the search query
     * @return list of matching materials
     */
    @GetMapping("/search/creator")
    public ResponseEntity<List<Material>> searchByCreator(@RequestParam("q") String q) {
        List<Material> materials = materialStore.searchByCreator(q);
        return ResponseEntity.ok(materials);
    }

    /**
     * GET /api/materials/type/{type} - Get materials by type
     * 
     * @param type the material type
     * @return list of materials of the specified type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Material>> getMaterialsByType(@PathVariable("type") MaterialType type) {
        List<Material> materials = materialStore.getMaterialsByType(type);
        return ResponseEntity.ok(materials);
    }

    /**
     * GET /api/materials/recent?years={years} - Get recent materials
     * 
     * @param years number of years to look back
     * @return list of recent materials
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Material>> getRecentMaterials(@RequestParam(value = "years", defaultValue = "5") int years) {
        List<Material> materials = materialStore.findRecentMaterials(years);
        return ResponseEntity.ok(materials);
    }

    /**
     * Get materials by price range.
     * 
     * @param min minimum price
     * @param max maximum price
     * @return list of materials in price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Material>> getMaterialsByPriceRange(
            @RequestParam("min") double min, 
            @RequestParam("max") double max) {
        List<Material> materials = materialStore.getMaterialsByPriceRange(min, max);
        return ResponseEntity.ok(materials);
    }

    /**
     * GET /api/materials/stats - Get inventory statistics
     * 
     * @return inventory statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<MaterialStore.InventoryStats> getInventoryStats() {
        MaterialStore.InventoryStats stats = materialStore.getInventoryStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/materials/count - Get total material count
     * 
     * @return total number of materials
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getMaterialCount() {
        int count = materialStore.size();
        return ResponseEntity.ok(count);
    }
}
