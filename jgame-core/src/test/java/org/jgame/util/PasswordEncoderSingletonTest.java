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
