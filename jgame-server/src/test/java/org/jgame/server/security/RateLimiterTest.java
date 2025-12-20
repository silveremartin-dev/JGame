/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
