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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory rate limiter for API endpoints.
 * 
 * <p>
 * Uses a sliding window approach to limit requests per IP/user.
 * </p>
 *
 * @author Google Gemini (Antigravity)
 * @version 1.0
 */
public class RateLimiter {

    private static final Logger logger = LogManager.getLogger(RateLimiter.class);

    private final int maxRequests;
    private final long windowSeconds;
    private final Map<String, RateLimitEntry> entries = new ConcurrentHashMap<>();

    /**
     * Creates a rate limiter.
     *
     * @param maxRequests   maximum requests allowed in window
     * @param windowSeconds time window in seconds
     */
    public RateLimiter(int maxRequests, long windowSeconds) {
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }

    /**
     * Creates a default rate limiter (10 requests per minute).
     */
    public static RateLimiter defaultLimiter() {
        return new RateLimiter(10, 60);
    }

    /**
     * Creates a strict rate limiter for login (5 attempts per minute).
     */
    public static RateLimiter loginLimiter() {
        return new RateLimiter(5, 60);
    }

    /**
     * Checks if request is allowed and records it.
     *
     * @param key identifier (IP address or user ID)
     * @return true if allowed, false if rate limited
     */
    public boolean tryAcquire(String key) {
        Instant now = Instant.now();

        RateLimitEntry entry = entries.compute(key, (k, existing) -> {
            if (existing == null) {
                return new RateLimitEntry(now, 1);
            }

            // Check if window has passed
            long elapsed = now.getEpochSecond() - existing.windowStart.getEpochSecond();
            if (elapsed >= windowSeconds) {
                // Reset window
                return new RateLimitEntry(now, 1);
            }

            // Increment count
            return new RateLimitEntry(existing.windowStart, existing.count + 1);
        });

        boolean allowed = entry.count <= maxRequests;
        if (!allowed) {
            logger.warn("Rate limit exceeded for: {}", key);
        }
        return allowed;
    }

    /**
     * Gets remaining requests for a key.
     */
    public int getRemaining(String key) {
        RateLimitEntry entry = entries.get(key);
        if (entry == null) {
            return maxRequests;
        }

        long elapsed = Instant.now().getEpochSecond() - entry.windowStart.getEpochSecond();
        if (elapsed >= windowSeconds) {
            return maxRequests;
        }

        return Math.max(0, maxRequests - entry.count);
    }

    /**
     * Clears all rate limit entries (for testing).
     */
    public void clear() {
        entries.clear();
    }

    private record RateLimitEntry(Instant windowStart, int count) {
    }
}
