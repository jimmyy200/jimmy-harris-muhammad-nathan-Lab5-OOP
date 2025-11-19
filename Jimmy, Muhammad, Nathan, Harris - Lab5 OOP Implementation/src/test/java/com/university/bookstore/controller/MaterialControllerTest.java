package com.university.bookstore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.bookstore.BookstoreApplication;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Media.MediaQuality;
import com.university.bookstore.model.PrintedBook;

/**
 * Integration tests for MaterialController REST API.
 * 
 * <p>These tests demonstrate how the existing MaterialStore interface
 * can be easily tested through REST endpoints, showcasing the
 * extensibility of the hexagonal architecture design.</p>
 * 
 * <p>The tests verify that all CRUD operations work correctly through
 * the REST API without modifying the core business logic.</p>
 * 
 * @author Navid Mohaghegh
 * @version 1.0
 * @since 2024-12-19
 */
@SpringBootTest(classes = BookstoreApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.main.web-application-type=servlet",
    "server.port=0"
})
public class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test creating a new material via POST /api/materials
     */
    @Test
    public void testCreateMaterial() throws Exception {
        PrintedBook book = new PrintedBook(
            "978-1234567890", 
            "Test Book", 
            "Test Author", 
            29.99, 
            2024, 
            300, 
            "Test Publisher", 
            true
        );

        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.price").value(29.99));
    }

    /**
     * Test retrieving all materials via GET /api/materials
     */
    @Test
    public void testGetAllMaterials() throws Exception {
        mockMvc.perform(get("/api/materials"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test retrieving a specific material by ID via GET /api/materials/{id}
     */
    @Test
    public void testGetMaterialById() throws Exception {
        // First create a material
        EBook ebook = new EBook(
            "test-ebook-1",
            "Test EBook",
            "Test Author",
            19.99,
            2024,
            "EPUB",
            1.5,
            false,
            50000,
            MediaQuality.HIGH
        );

        // Create the material
        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ebook)))
                .andExpect(status().isCreated());

        // Retrieve the material
        mockMvc.perform(get("/api/materials/test-ebook-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test EBook"))
                .andExpect(jsonPath("$.price").value(19.99));
    }

    /**
     * Test searching materials by title via GET /api/materials/search/title
     */
    @Test
    public void testSearchByTitle() throws Exception {
        mockMvc.perform(get("/api/materials/search/title")
                .param("q", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test searching materials by creator via GET /api/materials/search/creator
     */
    @Test
    public void testSearchByCreator() throws Exception {
        mockMvc.perform(get("/api/materials/search/creator")
                .param("q", "Author"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test getting materials by type via GET /api/materials/type/{type}
     */
    @Test
    public void testGetMaterialsByType() throws Exception {
        mockMvc.perform(get("/api/materials/type/BOOK"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test getting recent materials via GET /api/materials/recent
     */
    @Test
    public void testGetRecentMaterials() throws Exception {
        mockMvc.perform(get("/api/materials/recent")
                .param("years", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test getting materials by price range via GET /api/materials/price-range
     */
    @Test
    public void testGetMaterialsByPriceRange() throws Exception {
        mockMvc.perform(get("/api/materials/price-range")
                .param("min", "10.0")
                .param("max", "50.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test getting inventory statistics via GET /api/materials/stats
     */
    @Test
    public void testGetInventoryStats() throws Exception {
        mockMvc.perform(get("/api/materials/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount").exists())
                .andExpect(jsonPath("$.averagePrice").exists());
    }

    /**
     * Test getting material count via GET /api/materials/count
     */
    @Test
    public void testGetMaterialCount() throws Exception {
        mockMvc.perform(get("/api/materials/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test updating a material via PUT /api/materials/{id}
     */
    @Test
    public void testUpdateMaterial() throws Exception {
        // First create a material
        PrintedBook originalBook = new PrintedBook(
            "9781234567891", 
            "Original Book", 
            "Original Author", 
            25.99, 
            2024, 
            250, 
            "Original Publisher", 
            false
        );

        // Create the material
        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(originalBook)))
                .andExpect(status().isCreated());

        // Update the material
        PrintedBook updatedBook = new PrintedBook(
            "9781234567891", 
            "Updated Book", 
            "Updated Author", 
            29.99, 
            2024, 
            300, 
            "Updated Publisher", 
            true
        );

        mockMvc.perform(put("/api/materials/9781234567891")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.price").value(29.99));
    }

    /**
     * Test deleting a material via DELETE /api/materials/{id}
     */
    @Test
    public void testDeleteMaterial() throws Exception {
        // First create a material
        EBook ebook = new EBook(
            "delete-test-ebook",
            "Delete Test EBook",
            "Delete Test Author",
            15.99,
            2024,
            "PDF",
            2.0,
            true,
            30000,
            MediaQuality.STANDARD
        );

        // Create the material
        mockMvc.perform(post("/api/materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ebook)))
                .andExpect(status().isCreated());

        // Delete the material
        mockMvc.perform(delete("/api/materials/delete-test-ebook"))
                .andExpect(status().isNoContent());

        // Verify the material is deleted
        mockMvc.perform(get("/api/materials/delete-test-ebook"))
                .andExpect(status().isNotFound());
    }
}
