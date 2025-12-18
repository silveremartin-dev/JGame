/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PasswordEncoderSingleton utility class.
 */
class PasswordEncoderSingletonTest {

    @Test
    @DisplayName("Should encode password")
    void shouldEncodePassword() {
        String password = "testPassword123";
        String encoded = PasswordEncoderSingleton.encode(password);
        assertNotNull(encoded);
        assertNotEquals(password, encoded);
    }

    @Test
    @DisplayName("Should verify correct password")
    void shouldVerifyCorrectPassword() {
        String password = "testPassword123";
        String encoded = PasswordEncoderSingleton.encode(password);
        assertTrue(PasswordEncoderSingleton.matches(password, encoded));
    }

    @Test
    @DisplayName("Should reject incorrect password")
    void shouldRejectIncorrectPassword() {
        String password = "testPassword123";
        String encoded = PasswordEncoderSingleton.encode(password);
        assertFalse(PasswordEncoderSingleton.matches("wrongPassword", encoded));
    }

    @Test
    @DisplayName("Should generate different hashes for same password")
    void shouldGenerateDifferentHashes() {
        String password = "testPassword123";
        String encoded1 = PasswordEncoderSingleton.encode(password);
        String encoded2 = PasswordEncoderSingleton.encode(password);
        // BCrypt generates different salts, so hashes should differ
        assertNotEquals(encoded1, encoded2);
    }
}
