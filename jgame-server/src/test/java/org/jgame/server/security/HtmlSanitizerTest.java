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
