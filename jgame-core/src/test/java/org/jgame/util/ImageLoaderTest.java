/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot, Google Gemini (Antigravity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
