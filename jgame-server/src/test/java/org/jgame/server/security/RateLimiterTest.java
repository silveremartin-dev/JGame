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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RateLimiter.
 */
class RateLimiterTest {

    private RateLimiter limiter;

    @BeforeEach
    void setUp() {
        limiter = new RateLimiter(3, 60); // 3 requests per 60 seconds
    }

    @Test
    @DisplayName("Should allow requests within limit")
    void shouldAllowRequestsWithinLimit() {
        assertTrue(limiter.tryAcquire("user1"));
        assertTrue(limiter.tryAcquire("user1"));
        assertTrue(limiter.tryAcquire("user1"));
    }

    @Test
    @DisplayName("Should block requests over limit")
    void shouldBlockRequestsOverLimit() {
        limiter.tryAcquire("user1");
        limiter.tryAcquire("user1");
        limiter.tryAcquire("user1");

        assertFalse(limiter.tryAcquire("user1"));
    }

    @Test
    @DisplayName("Should track different users separately")
    void shouldTrackDifferentUsersSeparately() {
        limiter.tryAcquire("user1");
        limiter.tryAcquire("user1");
        limiter.tryAcquire("user1");

        assertTrue(limiter.tryAcquire("user2")); // Different user
    }

    @Test
    @DisplayName("Should return correct remaining count")
    void shouldReturnCorrectRemainingCount() {
        assertEquals(3, limiter.getRemaining("user1"));

        limiter.tryAcquire("user1");
        assertEquals(2, limiter.getRemaining("user1"));
    }

    @Test
    @DisplayName("Static factory methods should work")
    void staticFactoryMethodsShouldWork() {
        RateLimiter defaultLimiter = RateLimiter.defaultLimiter();
        RateLimiter loginLimiter = RateLimiter.loginLimiter();

        assertNotNull(defaultLimiter);
        assertNotNull(loginLimiter);
    }
}
