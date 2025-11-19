package com.university.bookstore.demo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.university.bookstore.api.MaterialStore;
import com.university.bookstore.impl.MaterialStoreImpl;
import com.university.bookstore.model.AudioBook;
import com.university.bookstore.model.Magazine;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.VideoMaterial;

/**
 * Demonstration class showing polymorphism and OOP best practices.
 * 
 * @author Navid Mohaghegh
 * @version 2.0
 */
public class PolymorphismDemo {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PolymorphismDemo.class);
    
    public static void main(String[] args) {
        LOGGER.info("=== Material Store Polymorphism Demo ===\n");
        
        MaterialStore store = createSampleStore();
        
        demonstratePolymorphicBehavior(store);
        demonstrateInterfaceSegregation(store);
        demonstrateAbstraction(store);
        demonstrateDynamicBinding(store);
        demonstrateSOLIDPrinciples(store);
        demonstrateMediaVersatility(store);
        demonstrateStreamingVsDownload(store);
    }
    
    private static MaterialStore createSampleStore() {
        MaterialStoreImpl store = new MaterialStoreImpl();
        
        // Traditional print materials
        PrintedBook book = new PrintedBook(
            "9780134685991", "Effective Java", "Joshua Bloch",
            45.99, 2018, 412, "Addison-Wesley", true
        );
        
        Magazine magazine = new Magazine(
            "12345678", "National Geographic", "NatGeo Society",
            6.99, 2024, 3, "Monthly", "Science"
        );
        
        // Audio materials with various qualities
        AudioBook audioBook1 = new AudioBook(
            "9780143038092", "1984", "George Orwell", "Simon Prebble",
            14.99, 2020, 690, "MP3", 850.5,
            Media.MediaQuality.HIGH, "English", true
        );
        
        AudioBook audioBook2 = new AudioBook(
            "9780547928227", "The Hobbit", "J.R.R. Tolkien", "Rob Inglis",
            24.99, 2022, 660, "M4B", 450.0,
            Media.MediaQuality.STANDARD, "English", false
        );
        
        AudioBook audiobook3 = new AudioBook(
            "9781250318398", "Atomic Habits", "James Clear", "James Clear",
            19.99, 2023, 320, "FLAC", 1200.0,
            Media.MediaQuality.ULTRA_HD, "English", true
        );
        
        // Diverse video content
        VideoMaterial documentary = new VideoMaterial(
            "883929665839", "Planet Earth II", "David Attenborough",
            29.99, 2016, 300, "MP4", 4500.0,
            Media.MediaQuality.ULTRA_HD, VideoMaterial.VideoType.DOCUMENTARY,
            "G", Arrays.asList("David Attenborough"), true, "16:9"
        );
        
        VideoMaterial movie = new VideoMaterial(
            "043396544789", "The Matrix", "The Wachowskis",
            19.99, 1999, 136, "MP4", 2800.0,
            Media.MediaQuality.HD, VideoMaterial.VideoType.MOVIE,
            "R", Arrays.asList("Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"),
            true, "2.39:1"
        );
        
        VideoMaterial tutorial = new VideoMaterial(
            "TUT20240001", "Advanced Java Programming", "Tech Education Inc",
            89.99, 2024, 480, "MP4", 3200.0,
            Media.MediaQuality.HD, VideoMaterial.VideoType.TUTORIAL,
            "NR", Arrays.asList("Dr. Sarah Chen"), true, "16:9"
        );
        
        VideoMaterial tvSeries = new VideoMaterial(
            "883929696789", "Breaking Bad - Season 1", "Vince Gilligan",
            39.99, 2008, 420, "MKV", 8500.0,
            Media.MediaQuality.HD, VideoMaterial.VideoType.TV_SERIES,
            "TV-MA", Arrays.asList("Bryan Cranston", "Aaron Paul", "Anna Gunn"),
            true, "16:9"
        );
        
        VideoMaterial shortFilm = new VideoMaterial(
            "SHORT2023001", "The Red Balloon", "Albert Lamorisse",
            9.99, 1956, 34, "AVI", 250.0,
            Media.MediaQuality.STANDARD, VideoMaterial.VideoType.SHORT_FILM,
            "G", Arrays.asList("Pascal Lamorisse"), false, "4:3"
        );
        
        VideoMaterial educationalVideo = new VideoMaterial(
            "EDU2024567", "Quantum Physics Explained", "MIT OpenCourseWare",
            0.00, 2024, 90, "WEBM", 680.0,
            Media.MediaQuality.HIGH, VideoMaterial.VideoType.EDUCATIONAL,
            "NR", Arrays.asList("Prof. Walter Lewin"), true, "16:9"
        );
        
        // Add all materials to store
        store.addMaterial(book);
        store.addMaterial(magazine);
        store.addMaterial(audioBook1);
        store.addMaterial(audioBook2);
        store.addMaterial(audiobook3);
        store.addMaterial(documentary);
        store.addMaterial(movie);
        store.addMaterial(tutorial);
        store.addMaterial(tvSeries);
        store.addMaterial(shortFilm);
        store.addMaterial(educationalVideo);
        
        return store;
    }
    
    private static void demonstratePolymorphicBehavior(MaterialStore store) {
        LOGGER.info("1. POLYMORPHIC BEHAVIOR");
        LOGGER.info("------------------------");
        
        List<Material> materials = store.getAllMaterials();
        
        for (Material material : materials) {
            LOGGER.info("\nMaterial Type: {}", material.getType().getDisplayName());
            LOGGER.info("Title: {}", material.getTitle());
            LOGGER.info("Creator: {}", material.getCreator());
            LOGGER.info("Display Info: {}", material.getDisplayInfo());
            LOGGER.info("Original Price: ${}", String.format("%.2f", material.getPrice()));
            LOGGER.info("Discount Rate: {}%", (material.getDiscountRate() * 100));
            LOGGER.info("Discounted Price: ${}", String.format("%.2f", material.getDiscountedPrice()));
        }
        
        LOGGER.info("\n");
    }
    
    private static void demonstrateInterfaceSegregation(MaterialStore store) {
        LOGGER.info("2. INTERFACE SEGREGATION (Media Interface)");
        LOGGER.info("-------------------------------------------");
        
        List<Media> mediaItems = store.getMediaMaterials();
        
        LOGGER.info("Found {} media items:\n", mediaItems.size());
        
        for (Media media : mediaItems) {
            LOGGER.info("Media: {}", ((Material)media).getTitle());
            LOGGER.info("  {}", media.getPlaybackInfo());
            LOGGER.info("  Download time at 10 Mbps: {} seconds", media.estimateDownloadTime(10));
            LOGGER.info("  Streaming only: {}", media.isStreamingOnly());
        }
        
        LOGGER.info("\n");
    }
    
    private static void demonstrateAbstraction(MaterialStore store) {
        LOGGER.info("3. ABSTRACTION (Abstract Base Class)");
        LOGGER.info("-------------------------------------");
        
        LOGGER.info("Materials grouped by type:\n");
        
        for (Material.MaterialType type : Material.MaterialType.values()) {
            List<Material> ofType = store.getMaterialsByType(type);
            if (!ofType.isEmpty()) {
                LOGGER.info("{}: {} items", type.getDisplayName(), ofType.size());
                for (Material m : ofType) {
                    LOGGER.info("  - {} (${})", m.getTitle(), String.format("%.2f", m.getPrice()));
                }
            }
        }
        
        LOGGER.info("\n");
    }
    
    private static void demonstrateDynamicBinding(MaterialStore store) {
        LOGGER.info("4. DYNAMIC BINDING");
        LOGGER.info("-------------------");
        
        Material cheapest = store.filterMaterials(m -> true).stream()
            .min((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
            .orElse(null);
        
        Material mostExpensive = store.filterMaterials(m -> true).stream()
            .max((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
            .orElse(null);
        
        LOGGER.info("Cheapest item: {}", cheapest.getDisplayInfo());
        LOGGER.info("Most expensive: {}", mostExpensive.getDisplayInfo());
        
        LOGGER.info("\nDynamic method dispatch:");
        processPolymorphically(cheapest);
        processPolymorphically(mostExpensive);
        
        LOGGER.info("\n");
    }
    
    private static void processPolymorphically(Material material) {
        LOGGER.info("  Processing: {}", material.getTitle());
        
        if (material instanceof Media) {
            Media media = (Media) material;
            LOGGER.info("    -> This is media with duration: {} minutes", media.getDuration());
        }
        
        if (material instanceof PrintedBook) {
            PrintedBook book = (PrintedBook) material;
            LOGGER.info("    -> This is a book with {} pages", book.getPages());
        }
        
        if (material instanceof Magazine) {
            Magazine mag = (Magazine) material;
            LOGGER.info("    -> This is a magazine, issue #{}", mag.getIssueNumber());
        }
    }
    
    private static void demonstrateSOLIDPrinciples(MaterialStore store) {
        LOGGER.info("5. SOLID PRINCIPLES IN ACTION");
        LOGGER.info("------------------------------");
        
        MaterialStore.InventoryStats stats = store.getInventoryStats();
        
        LOGGER.info("Inventory Statistics:");
        LOGGER.info("  Total items: {}", stats.getTotalCount());
        LOGGER.info("  Average price: ${}", String.format("%.2f", stats.getAveragePrice()));
        LOGGER.info("  Median price: ${}", String.format("%.2f", stats.getMedianPrice()));
        LOGGER.info("  Unique types: {}", stats.getUniqueTypes());
        LOGGER.info("  Media items: {}", stats.getMediaCount());
        LOGGER.info("  Print items: {}", stats.getPrintCount());
        
        LOGGER.info("\nTotal inventory value: ${}", String.format("%.2f", store.getTotalInventoryValue()));
        LOGGER.info("Total with discounts: ${}", String.format("%.2f", store.getTotalDiscountedValue()));
        
        double savings = store.getTotalInventoryValue() - store.getTotalDiscountedValue();
        LOGGER.info("Total savings: ${}", String.format("%.2f", savings));
        
        LOGGER.info("\nFiltered Results (Lambda Expression):");
        List<Material> affordable = store.filterMaterials(m -> m.getPrice() < 20);
        LOGGER.info("  Items under $20: {}", affordable.size());
        for (Material m : affordable) {
            LOGGER.info("    - {} (${})", m.getTitle(), String.format("%.2f", m.getPrice()));
        }
    }
    
    private static void demonstrateMediaVersatility(MaterialStore store) {
        LOGGER.info("\n6. MEDIA VERSATILITY SHOWCASE");
        LOGGER.info("-------------------------------");
        
        LOGGER.info("\nVideo Content Types:");
        List<Material> videos = store.getMaterialsByType(Material.MaterialType.VIDEO);
        videos.addAll(store.getMaterialsByType(Material.MaterialType.DOCUMENTARY));
        
        for (Material material : videos) {
            if (material instanceof VideoMaterial) {
                VideoMaterial video = (VideoMaterial) material;
                LOGGER.info("\n  {}: {}", video.getVideoType(), video.getTitle());
                LOGGER.info("    Director: {}", video.getDirector());
                LOGGER.info("    Duration: {} minutes", video.getDuration());
                LOGGER.info("    Quality: {}", video.getQuality());
                LOGGER.info("    Format: {}", video.getFormat());
                LOGGER.info("    Rating: {}", video.getRating());
                LOGGER.info("    Aspect Ratio: {}", video.getAspectRatio());
                LOGGER.info("    Subtitles: {}", (video.hasSubtitles() ? "Yes" : "No"));
                LOGGER.info("    Streaming Bandwidth: {} Mbps", video.getStreamingBandwidth());
                
                if (!video.getCast().isEmpty()) {
                    LOGGER.info("    Cast: {}", String.join(", ", 
                        video.getCast().size() > 3 ? 
                        video.getCast().subList(0, 3) : video.getCast()));
                }
            }
        }
        
        LOGGER.info("\n\nAudio Quality Comparison:");
        List<Material> audioMaterials = store.filterMaterials(m -> m instanceof AudioBook);
        
        for (Material material : audioMaterials) {
            AudioBook audio = (AudioBook) material;
            LOGGER.info("\n  {}", audio.getTitle());
            LOGGER.info("    Quality: {}", audio.getQuality());
            LOGGER.info("    Format: {}", audio.getFormat());
            LOGGER.info("    File Size: {} MB", audio.getFileSize());
            LOGGER.info("    Duration: {} minutes", audio.getDuration());
            double estimatedBitrate = (audio.getFileSize() * 8 * 1024) / (audio.getDuration() * 60);
            LOGGER.info("    Estimated Bitrate: {} kbps", String.format("%.0f", estimatedBitrate));
            LOGGER.info("    Download Time (10 Mbps): {} seconds", audio.estimateDownloadTime(10));
        }
        
        LOGGER.info("\n");
    }
    
    private static void demonstrateStreamingVsDownload(MaterialStore store) {
        LOGGER.info("7. STREAMING VS DOWNLOAD ANALYSIS");
        LOGGER.info("-----------------------------------");
        
        List<Media> mediaItems = store.getMediaMaterials();
        
        LOGGER.info("\nStreaming-Only Content (Large Files > 4GB):");
        for (Media media : mediaItems) {
            if (media.isStreamingOnly()) {
                Material material = (Material) media;
                LOGGER.info("  - {} ({} MB)", material.getTitle(), media.getFileSize());
            }
        }
        
        LOGGER.info("\nDownloadable Content:");
        for (Media media : mediaItems) {
            if (!media.isStreamingOnly()) {
                Material material = (Material) media;
                LOGGER.info("  - {} ({} MB, Download time @ 50 Mbps: {}s)", 
                           material.getTitle(), media.getFileSize(), media.estimateDownloadTime(50));
            }
        }
        
        LOGGER.info("\nQuality vs Storage Requirements:");
        LOGGER.info("  Format Distribution:");
        
        int ultraHD = 0, highQ = 0, standard = 0;
        for (Media media : mediaItems) {
            switch (media.getQuality()) {
                case ULTRA_HD: ultraHD++; break;
                case HIGH:
                case HD: highQ++; break;
                default: standard++; break;
            }
        }
        
        LOGGER.info("    Ultra HD: {} items", ultraHD);
        LOGGER.info("    High/HD: {} items", highQ);
        LOGGER.info("    Standard/Low: {} items", standard);
        
        double totalStorage = mediaItems.stream()
            .mapToDouble(Media::getFileSize)
            .sum();
        
        LOGGER.info("\n  Total Storage Required: {} GB", String.format("%.2f", totalStorage / 1024));
        
        LOGGER.info("\n  Bandwidth Requirements by Quality:");
        for (Material material : store.getAllMaterials()) {
            if (material instanceof VideoMaterial) {
                VideoMaterial video = (VideoMaterial) material;
                if (video.getQuality() == Media.MediaQuality.ULTRA_HD || 
                    video.getQuality() == Media.MediaQuality.HD) {
                    LOGGER.info("    {} ({}): {} Mbps", video.getTitle(), video.getQuality(), video.getStreamingBandwidth());
                }
            }
        }
        
        LOGGER.info("\n");
    }
}