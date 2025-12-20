/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HtmlSanitizer.
 */
class HtmlSanitizerTest {

    @Test
    @DisplayName("Should escape HTML entities")
    void shouldEscapeHtmlEntities() {
        assertEquals("&lt;script&gt;", HtmlSanitizer.escapeHtml("<script>"));
        assertEquals("&amp;", HtmlSanitizer.escapeHtml("&"));
        assertEquals("&quot;", HtmlSanitizer.escapeHtml("\""));
    }

    @Test
    @DisplayName("Should remove script tags")
    void shouldRemoveScriptTags() {
        String input = "Hello<script>alert('xss')</script>World";
        String result = HtmlSanitizer.sanitize(input);
        assertFalse(result.contains("script"));
        assertFalse(result.contains("alert"));
    }

    @Test
    @DisplayName("Should remove event handlers")
    void shouldRemoveEventHandlers() {
        String input = "Click <a onclick='evil()' href='#'>here</a>";
        String result = HtmlSanitizer.sanitize(input);
        assertFalse(result.toLowerCase().contains("onclick"));
    }

    @Test
    @DisplayName("Should remove javascript URLs")
    void shouldRemoveJavascriptUrls() {
        String input = "Visit javascript:alert('xss')";
        String result = HtmlSanitizer.sanitize(input);
        assertFalse(result.toLowerCase().contains("javascript:"));
    }

    @Test
    @DisplayName("Should handle null and empty input")
    void shouldHandleNullAndEmptyInput() {
        assertEquals("", HtmlSanitizer.sanitize(null));
        assertEquals("", HtmlSanitizer.sanitize(""));
    }

    @Test
    @DisplayName("Should truncate and sanitize")
    void shouldTruncateAndSanitize() {
        String input = "<script>evil</script>This is a long message that needs truncation";
        String result = HtmlSanitizer.sanitizeAndTruncate(input, 50);
        // After removing script tag and truncating, result should be reasonable
        assertTrue(result.length() <= 100); // Escaped chars might add length
        assertFalse(result.contains("script"));
        assertFalse(result.contains("evil"));
    }

    @Test
    @DisplayName("Should sanitize for JSON")
    void shouldSanitizeForJson() {
        String input = "Line1\nLine2\tTab\"Quote";
        String result = HtmlSanitizer.sanitizeForJson(input);
        assertTrue(result.contains("\\n"));
        assertTrue(result.contains("\\t"));
        assertTrue(result.contains("\\\""));
    }
}
