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
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for I18n utility.
 */
class I18nTest {

    @Test
    @DisplayName("Should return localized message for key")
    void shouldReturnLocalizedMessage() {
        I18n.setLocale(Locale.ENGLISH);
        String appName = I18n.get("app.name");
        assertNotNull(appName);
        assertFalse(appName.startsWith("!"));
    }

    @Test
    @DisplayName("Should return key marker for missing key")
    void shouldReturnKeyForMissingKey() {
        String result = I18n.get("nonexistent.key.12345");
        assertTrue(result.contains("nonexistent.key"));
    }

    @Test
    @DisplayName("Should change locale")
    void shouldChangeLocale() {
        I18n.setLocale(Locale.FRENCH);
        assertEquals(Locale.FRENCH, I18n.getLocale());

        I18n.setLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, I18n.getLocale());
    }

    @Test
    @DisplayName("Should format message with parameters")
    void shouldFormatMessage() {
        String formatted = I18n.format("app.name");
        assertNotNull(formatted);
    }

    @Test
    @DisplayName("Should check key existence")
    void shouldCheckKeyExists() {
        assertTrue(I18n.hasKey("app.name"));
        assertFalse(I18n.hasKey("nonexistent.key.xyz"));
    }
}
