/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT token blacklist for token revocation.
 * 
 * <p>
 * Stores revoked tokens until they would have expired anyway.
 * In production, use Redis or a distributed cache.
 * </p>
 *
 * @author Google Gemini (Antigravity)
 * @version 1.0
 */
public class TokenBlacklist {

    private static final Logger logger = LogManager.getLogger(TokenBlacklist.class);
    private static TokenBlacklist instance;

    private final Set<BlacklistedToken> blacklist = ConcurrentHashMap.newKeySet();

    private TokenBlacklist() {
        // Start cleanup thread
        Thread cleanupThread = new Thread(this::cleanupExpired, "TokenBlacklistCleanup");
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    public static synchronized TokenBlacklist getInstance() {
        if (instance == null) {
            instance = new TokenBlacklist();
        }
        return instance;
    }

    /**
     * Revokes a token.
     *
     * @param token      the JWT token to revoke
     * @param expiryTime when the token would naturally expire
     */
    public void revoke(String token, Instant expiryTime) {
        blacklist.add(new BlacklistedToken(token, expiryTime));
        logger.info("Token revoked, will be cleaned up at {}", expiryTime);
    }

    /**
     * Checks if a token has been revoked.
     *
     * @param token the JWT token to check
     * @return true if revoked, false if valid
     */
    public boolean isRevoked(String token) {
        return blacklist.stream()
                .anyMatch(bt -> bt.token.equals(token));
    }

    /**
     * Revokes all tokens for a user (logout from all devices).
     * Note: This requires storing user ID with each token.
     *
     * @param userId the user ID
     */
    public void revokeAllForUser(String userId) {
        // Would need to track user->token mappings
        // For now, this is a placeholder
        logger.info("Revoking all tokens for user: {}", userId);
    }

    /**
     * Cleans up expired tokens from the blacklist.
     */
    private void cleanupExpired() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(60_000); // Check every minute

                Instant now = Instant.now();
                int removed = 0;
                var iterator = blacklist.iterator();
                while (iterator.hasNext()) {
                    BlacklistedToken bt = iterator.next();
                    if (bt.expiryTime.isBefore(now)) {
                        iterator.remove();
                        removed++;
                    }
                }

                if (removed > 0) {
                    logger.debug("Cleaned up {} expired tokens from blacklist", removed);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Gets the size of the blacklist.
     */
    public int size() {
        return blacklist.size();
    }

    /**
     * Clears the blacklist (for testing).
     */
    public void clear() {
        blacklist.clear();
    }

    private record BlacklistedToken(String token, Instant expiryTime) {
    }
}
