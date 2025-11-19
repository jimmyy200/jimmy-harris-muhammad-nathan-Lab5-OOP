package com.university.bookstore.factory;

import java.util.Map;
import java.util.Objects;

import com.university.bookstore.model.AudioBook;
import com.university.bookstore.model.EBook;
import com.university.bookstore.model.Magazine;
import com.university.bookstore.model.Material;
import com.university.bookstore.model.Media;
import com.university.bookstore.model.PrintedBook;
import com.university.bookstore.model.VideoMaterial;

/**
 * Enhanced factory for creating Material instances with advanced validation and type-safe property extraction.
 * Demonstrates improved Factory pattern implementation with better error handling and validation.
 * 
 * <p>This factory extends the basic MaterialFactory with enhanced validation,
 * type-safe property extraction, and support for complex material configurations.</p>
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class AdvancedMaterialFactory {
    
    /**
     * Creates a Material instance based on the specified type and properties.
     * 
     * @param type the material type
     * @param properties map of properties required for the material
     * @return the created Material instance
     * @throws IllegalArgumentException if type is null or properties are invalid
     * @throws NullPointerException if required properties are missing
     */
    public static Material createMaterial(String type, Map<String, Object> properties) {
        Objects.requireNonNull(type, "Material type cannot be null");
        Objects.requireNonNull(properties, "Properties cannot be null");
        
        String normalizedType = type.trim().toUpperCase();
        
        switch (normalizedType) {
            case "BOOK":
            case "PRINTED_BOOK":
                return createPrintedBook(properties);
            case "EBOOK":
            case "E_BOOK":
                return createEBook(properties);
            case "AUDIOBOOK":
            case "AUDIO_BOOK":
                return createAudioBook(properties);
            case "VIDEO":
            case "VIDEO_MATERIAL":
                return createVideoMaterial(properties);
            case "MAGAZINE":
                return createMagazine(properties);
            default:
                throw new IllegalArgumentException("Unknown material type: " + type);
        }
    }
    
    /**
     * Creates a PrintedBook instance with enhanced validation.
     */
    private static PrintedBook createPrintedBook(Map<String, Object> properties) {
        return new PrintedBook(
            getRequiredString(properties, "id"),
            getRequiredString(properties, "title"),
            getRequiredString(properties, "author"),
            getRequiredDouble(properties, "price"),
            getRequiredInt(properties, "year"),
            getRequiredInt(properties, "pages"),
            getRequiredString(properties, "publisher"),
            getRequiredBoolean(properties, "hardcover")
        );
    }
    
    /**
     * Creates an EBook instance with enhanced validation.
     */
    private static EBook createEBook(Map<String, Object> properties) {
        return new EBook(
            getRequiredString(properties, "id"),
            getRequiredString(properties, "title"),
            getRequiredString(properties, "author"),
            getRequiredDouble(properties, "price"),
            getRequiredInt(properties, "year"),
            getRequiredString(properties, "fileFormat"),
            getRequiredDouble(properties, "fileSize"),
            getRequiredBoolean(properties, "drmEnabled"),
            getRequiredInt(properties, "wordCount"),
            getRequiredEnum(properties, "quality", Media.MediaQuality.class)
        );
    }
    
    /**
     * Creates an AudioBook instance with enhanced validation.
     */
    private static AudioBook createAudioBook(Map<String, Object> properties) {
        return new AudioBook(
            getRequiredString(properties, "id"),
            getRequiredString(properties, "title"),
            getRequiredString(properties, "author"),
            getRequiredString(properties, "narrator"),
            getRequiredDouble(properties, "price"),
            getRequiredInt(properties, "year"),
            getRequiredInt(properties, "duration"),
            getRequiredString(properties, "format"),
            getRequiredDouble(properties, "fileSize"),
            getRequiredEnum(properties, "quality", Media.MediaQuality.class),
            getRequiredString(properties, "language"),
            getRequiredBoolean(properties, "unabridged")
        );
    }
    
    /**
     * Creates a VideoMaterial instance with enhanced validation.
     */
    private static VideoMaterial createVideoMaterial(Map<String, Object> properties) {
        return new VideoMaterial(
            getRequiredString(properties, "id"),
            getRequiredString(properties, "title"),
            getRequiredString(properties, "director"),
            getRequiredDouble(properties, "price"),
            getRequiredInt(properties, "year"),
            getRequiredInt(properties, "duration"),
            getRequiredString(properties, "format"),
            getRequiredDouble(properties, "fileSize"),
            getRequiredEnum(properties, "quality", Media.MediaQuality.class),
            getRequiredEnum(properties, "videoType", VideoMaterial.VideoType.class),
            getRequiredString(properties, "rating"),
            getRequiredStringList(properties, "cast"),
            getRequiredBoolean(properties, "subtitlesAvailable"),
            getRequiredString(properties, "aspectRatio")
        );
    }
    
    /**
     * Creates a Magazine instance with enhanced validation.
     */
    private static Magazine createMagazine(Map<String, Object> properties) {
        return new Magazine(
            getRequiredString(properties, "id"),
            getRequiredString(properties, "title"),
            getRequiredString(properties, "publisher"),
            getRequiredDouble(properties, "price"),
            getRequiredInt(properties, "year"),
            getRequiredInt(properties, "issue"),
            getRequiredString(properties, "frequency"),
            getRequiredString(properties, "category")
        );
    }
    
    // Enhanced helper methods for type-safe property extraction
    
    /**
     * Gets a required string property with validation.
     */
    private static String getRequiredString(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Required property missing: " + key);
        }
        String str = value.toString().trim();
        if (str.isEmpty()) {
            throw new IllegalArgumentException("Required property cannot be empty: " + key);
        }
        return str;
    }
    
    /**
     * Gets a required double property with validation.
     */
    private static double getRequiredDouble(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Required property missing: " + key);
        }
        if (value instanceof Number) {
            double result = ((Number) value).doubleValue();
            if (result < 0) {
                throw new IllegalArgumentException("Property " + key + " must be non-negative: " + result);
            }
            return result;
        }
        throw new IllegalArgumentException("Property " + key + " must be a number");
    }
    
    /**
     * Gets a required integer property with validation.
     */
    private static int getRequiredInt(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Required property missing: " + key);
        }
        if (value instanceof Number) {
            int result = ((Number) value).intValue();
            if (result < 0) {
                throw new IllegalArgumentException("Property " + key + " must be non-negative: " + result);
            }
            return result;
        }
        throw new IllegalArgumentException("Property " + key + " must be a number");
    }
    
    /**
     * Gets a required boolean property with validation.
     */
    private static boolean getRequiredBoolean(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Required property missing: " + key);
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        throw new IllegalArgumentException("Property " + key + " must be a boolean");
    }
    
    /**
     * Gets a required enum property with validation.
     */
    private static <T extends Enum<T>> T getRequiredEnum(Map<String, Object> properties, 
                                                       String key, Class<T> enumClass) {
        Object value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Required property missing: " + key);
        }
        if (value instanceof String) {
            try {
                return Enum.valueOf(enumClass, value.toString().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Property " + key + " must be a valid " + 
                                                 enumClass.getSimpleName() + ": " + value);
            }
        }
        throw new IllegalArgumentException("Property " + key + " must be a string");
    }
    
    /**
     * Gets a required string list property with validation.
     */
    @SuppressWarnings("unchecked")
    private static java.util.List<String> getRequiredStringList(Map<String, Object> properties, String key) {
        Object value = properties.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Required property missing: " + key);
        }
        if (value instanceof java.util.List) {
            return (java.util.List<String>) value;
        }
        if (value instanceof String) {
            String str = value.toString().trim();
            if (str.isEmpty()) {
                return java.util.Arrays.asList();
            }
            return java.util.Arrays.asList(str.split(","));
        }
        throw new IllegalArgumentException("Property " + key + " must be a list or string");
    }
    
    /**
     * Validates that all required properties are present.
     * 
     * @param type the material type to validate
     * @param properties the properties map to validate
     */
    public static void validateRequiredProperties(String type, Map<String, Object> properties) {
        String normalizedType = type.trim().toUpperCase();
        
        switch (normalizedType) {
            case "BOOK":
            case "PRINTED_BOOK":
                validatePrintedBookProperties(properties);
                break;
            case "EBOOK":
            case "E_BOOK":
                validateEBookProperties(properties);
                break;
            case "AUDIOBOOK":
            case "AUDIO_BOOK":
                validateAudioBookProperties(properties);
                break;
            case "VIDEO":
            case "VIDEO_MATERIAL":
                validateVideoMaterialProperties(properties);
                break;
            case "MAGAZINE":
                validateMagazineProperties(properties);
                break;
            default:
                throw new IllegalArgumentException("Unknown material type: " + type);
        }
    }
    
    private static void validatePrintedBookProperties(Map<String, Object> properties) {
        String[] required = {"id", "title", "author", "price", "year", "pages", "publisher", "hardcover"};
        validateRequiredKeys(properties, required);
    }
    
    private static void validateEBookProperties(Map<String, Object> properties) {
        String[] required = {"id", "title", "author", "price", "year", "fileFormat", "fileSize", "drmEnabled", "wordCount", "quality"};
        validateRequiredKeys(properties, required);
    }
    
    private static void validateAudioBookProperties(Map<String, Object> properties) {
        String[] required = {"id", "title", "author", "narrator", "price", "year", "duration", "format", "fileSize", "quality", "language", "unabridged"};
        validateRequiredKeys(properties, required);
    }
    
    private static void validateVideoMaterialProperties(Map<String, Object> properties) {
        String[] required = {"id", "title", "director", "price", "year", "duration", "format", "fileSize", "quality", "videoType", "rating", "cast", "subtitlesAvailable", "aspectRatio"};
        validateRequiredKeys(properties, required);
    }
    
    private static void validateMagazineProperties(Map<String, Object> properties) {
        String[] required = {"id", "title", "publisher", "price", "year", "issue", "frequency", "category"};
        validateRequiredKeys(properties, required);
    }
    
    private static void validateRequiredKeys(Map<String, Object> properties, String[] requiredKeys) {
        for (String key : requiredKeys) {
            if (!properties.containsKey(key)) {
                throw new IllegalArgumentException("Required property missing: " + key);
            }
        }
    }
}
