/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Image;

/**
 * Unit tests for ImageLoader utility class.
 */
class ImageLoaderTest {

    @Test
    @DisplayName("ImageLoader should load existing image or return placeholder")
    void shouldLoadExistingImageOrPlaceholder() {
        // Test with a path - returns placeholder if not found
        Image img = ImageLoader.load("chess/king_white.png");
        assertNotNull(img, "Should return image or placeholder");
    }

    @Test
    @DisplayName("ImageLoader should return placeholder for missing image")
    void shouldReturnPlaceholderForMissingImage() {
        Image img = ImageLoader.load("nonexistent/image.png");
        assertNotNull(img, "Should return placeholder for missing image");
    }

    @Test
    @DisplayName("ImageLoader should cache loaded images")
    void shouldCacheLoadedImages() {
        Image img1 = ImageLoader.load("test.png");
        Image img2 = ImageLoader.load("test.png");
        // Same instance should be returned from cache
        assertSame(img1, img2);
    }

    @Test
    @DisplayName("ImageLoader should report cache size")
    void shouldReportCacheSize() {
        ImageLoader.clearCache();
        assertEquals(0, ImageLoader.getCacheSize());
        ImageLoader.load("test1.png");
        assertEquals(1, ImageLoader.getCacheSize());
    }

    @Test
    @DisplayName("ImageLoader should check if image exists")
    void shouldCheckIfImageExists() {
        // Non-existent image should return false
        assertFalse(ImageLoader.exists("definitely_not_real.png"));
    }
}
