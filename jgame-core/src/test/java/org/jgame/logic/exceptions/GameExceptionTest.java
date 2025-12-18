/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameException class.
 */
class GameExceptionTest {

    @Test
    @DisplayName("GameException should be throwable")
    void shouldBeThrowable() {
        GameException ex = new GameException("Test error");
        assertNotNull(ex);
        assertEquals("Test error", ex.getMessage());
    }

    @Test
    @DisplayName("GameException should support cause")
    void shouldSupportCause() {
        Throwable cause = new RuntimeException("Root cause");
        GameException ex = new GameException("Wrapped error", cause);
        assertEquals(cause, ex.getCause());
    }
}
