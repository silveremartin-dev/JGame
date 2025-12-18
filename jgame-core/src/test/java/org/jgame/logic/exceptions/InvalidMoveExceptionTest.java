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
 * Unit tests for InvalidMoveException class.
 */
class InvalidMoveExceptionTest {

    @Test
    @DisplayName("InvalidMoveException should be throwable")
    void shouldBeThrowable() {
        InvalidMoveException ex = new InvalidMoveException("Invalid move: e2-e5");
        assertNotNull(ex);
        assertTrue(ex.getMessage().contains("Invalid move"));
    }

    @Test
    @DisplayName("InvalidMoveException should extend GameException")
    void shouldExtendGameException() {
        InvalidMoveException ex = new InvalidMoveException("Test");
        assertTrue(ex instanceof GameException);
    }
}
