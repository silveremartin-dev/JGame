/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

package org.jgame.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the I18n internationalization utility class.
 *
 * @author Silvere Martin-Michiellot
 */
@DisplayName("I18n Utility Tests")
class I18nTest {

    @BeforeEach
    void setUp() {
        // Reset to default locale before each test
        I18n.setLocale(Locale.ENGLISH);
    }

    @Test
    @DisplayName("Should get message for valid key")
    void testGetValidKey() {
        String message = I18n.get("app.name");
        assertNotNull(message);
        assertEquals("JGame Framework", message);
    }

    @Test
    @DisplayName("Should return placeholder for invalid key")
    void testGetInvalidKey() {
        String message = I18n.get("invalid.key.that.does.not.exist");
        assertTrue(message.startsWith("!"));
        assertTrue(message.endsWith("!"));
        assertTrue(message.contains("invalid.key.that.does.not.exist"));
    }

    @Test
    @DisplayName("Should format message with parameters")
    void testFormatWithParameters() {
        String formatted = I18n.format("server.started", 8080);
        assertNotNull(formatted);
        assertTrue(formatted.contains("8080"));
    }

    @Test
    @DisplayName("Should change locale successfully")
    void testSetLocale() {
        I18n.setLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, I18n.getLocale());

        I18n.setLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, I18n.getLocale());
    }

    @Test
    @DisplayName("Should throw exception for null locale")
    void testSetNullLocale() {
        assertThrows(IllegalArgumentException.class, () -> I18n.setLocale(null));
    }

    @Test
    @DisplayName("Should check if key exists")
    void testHasKey() {
        assertTrue(I18n.hasKey("app.name"));
        assertFalse(I18n.hasKey("nonexistent.key"));
    }

    @Test
    @DisplayName("Should get resource bundle")
    void testGetResourceBundle() {
        assertNotNull(I18n.getResourceBundle());
    }

    @Test
    @DisplayName("Should retrieve menu messages")
    void testMenuMessages() {
        assertEquals("File", I18n.get("menu.file"));
        assertEquals("Open Game...", I18n.get("menu.file.open"));
        assertEquals("Save Game", I18n.get("menu.file.save"));
        assertEquals("Exit", I18n.get("menu.file.exit"));
    }

    @Test
    @DisplayName("Should retrieve tooltip messages")
    void testTooltipMessages() {
        String tooltip = I18n.get("tooltip.button.start");
        assertNotNull(tooltip);
        assertTrue(tooltip.length() > 0);
    }
}
