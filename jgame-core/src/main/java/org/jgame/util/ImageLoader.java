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

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Utility class for loading and caching game images.
 * 
 * <p>
 * Images are loaded from the resources folder and cached to avoid
 * repeated file I/O. Provides fallback placeholder images for missing assets.
 * </p>
 * 
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * Image checkerPiece = ImageLoader.load("checkers/pieces/red-regular.png");
 * Image dice = ImageLoader.load("goose/dice/dice-3.png");
 * </pre>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ImageLoader {

    private static final Logger LOGGER = Logger.getLogger(ImageLoader.class.getName());
    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final String IMAGE_BASE_PATH = "/images/";

    // Prevent instantiation
    private ImageLoader() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Loads an image from the resources folder.
     * Images are cached after first load for performance.
     * 
     * @param path relative path from /images/ directory
     *             Example: "checkers/pieces/red-king.png"
     * @return the loaded image, or a placeholder if not found
     */
    public static Image load(String path) {
        // Check cache first
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try {
            String fullPath = IMAGE_BASE_PATH + path;
            URL url = ImageLoader.class.getResource(fullPath);

            if (url == null) {
                LOGGER.warning("Image not found: " + fullPath + " - using placeholder");
                return getPlaceholder(path);
            }

            BufferedImage image = ImageIO.read(url);
            if (image == null) {
                LOGGER.warning("Failed to load image: " + fullPath + " - using placeholder");
                return getPlaceholder(path);
            }

            // Cache for future use
            imageCache.put(path, image);
            return image;

        } catch (IOException e) {
            LOGGER.severe("Error loading image " + path + ": " + e.getMessage());
            return getPlaceholder(path);
        }
    }

    /**
     * Preloads multiple images into the cache.
     * Useful for loading all game assets at startup.
     * 
     * @param paths array of image paths to preload
     */
    public static void preload(String... paths) {
        for (String path : paths) {
            load(path);
        }
    }

    /**
     * Clears the image cache.
     * Useful for memory management or when reloading assets.
     */
    public static void clearCache() {
        imageCache.clear();
        LOGGER.info("Image cache cleared");
    }

    /**
     * Gets the number of cached images.
     * 
     * @return number of images in cache
     */
    public static int getCacheSize() {
        return imageCache.size();
    }

    /**
     * Returns a placeholder image when the requested image is not found.
     * Creates a simple colored rectangle with text indicating the missing image.
     * 
     * @param path the path that was requested
     * @return a placeholder image
     */
    private static Image getPlaceholder(String path) {
        // Create a simple placeholder - 64x64 colored square
        BufferedImage placeholder = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = placeholder.createGraphics();

        // Fill with light gray
        g.setColor(new java.awt.Color(200, 200, 200));
        g.fillRect(0, 0, 64, 64);

        // Draw border
        g.setColor(java.awt.Color.BLACK);
        g.drawRect(0, 0, 63, 63);

        // Draw "?" in center
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 32));
        g.drawString("?", 22, 42);

        g.dispose();

        // Cache placeholder to avoid recreation
        imageCache.put(path, placeholder);
        return placeholder;
    }

    /**
     * Checks if an image exists in resources.
     * 
     * @param path relative path from /images/
     * @return true if image exists, false otherwise
     */
    public static boolean exists(String path) {
        String fullPath = IMAGE_BASE_PATH + path;
        URL url = ImageLoader.class.getResource(fullPath);
        return url != null;
    }
}
