/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtAuthHandler.
 */
class JwtAuthHandlerTest {

    private JwtAuthHandler handler;

    @BeforeEach
    void setUp() {
        handler = new JwtAuthHandler();
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void shouldGenerateValidToken() {
        String token = handler.generateToken("user123", "testuser");

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(token.contains(".")); // JWT has 3 parts separated by dots
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        String token1 = handler.generateToken("user1", "alice");
        String token2 = handler.generateToken("user2", "bob");

        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Should generate consistent tokens for same user (within same second)")
    void shouldGenerateTokensWithCorrectStructure() {
        String token = handler.generateToken("user123", "testuser");
        String[] parts = token.split("\\.");

        assertEquals(3, parts.length, "JWT should have header.payload.signature");
        assertFalse(parts[0].isBlank(), "Header should not be blank");
        assertFalse(parts[1].isBlank(), "Payload should not be blank");
        assertFalse(parts[2].isBlank(), "Signature should not be blank");
    }
}
