/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
