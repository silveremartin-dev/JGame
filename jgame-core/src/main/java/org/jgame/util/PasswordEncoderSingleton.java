/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

package org.jgame.util;

import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;

/**
 * Singleton class for secure password encoding and verification using BCrypt.
 * 
 * <p>
 * BCrypt is a password hashing function designed to be slow and computationally
 * expensive,
 * which helps protect against brute-force attacks. It automatically handles
 * salt generation
 * and incorporates it into the hash.
 * </p>
 * 
 * <p>
 * <strong>Security Features:</strong>
 * </p>
 * <ul>
 * <li>Automatic salt generation per password</li>
 * <li>Configurable work factor (log rounds)</li>
 * <li>Resistance to rainbow table attacks</li>
 * <li>Slow hashing to prevent brute force</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public final class PasswordEncoderSingleton {

    private static final Logger LOGGER = Logger.getLogger(PasswordEncoderSingleton.class.getName());

    /**
     * BCrypt work factor (log rounds).
     * Higher values = more secure but slower.
     * 12 is a good balance as of 2025.
     */
    private static final int LOG_ROUNDS = 12;

    /**
     * Minimum password length for validation.
     */
    private static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Maximum password length to prevent DoS attacks.
     * BCrypt has a 72-byte limit, but we use 100 for user convenience.
     */
    private static final int MAX_PASSWORD_LENGTH = 100;

    // Prevent instantiation
    private PasswordEncoderSingleton() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    /**
     * Encodes a plaintext password using BCrypt.
     * 
     * @param password the plaintext password to encode
     * @return BCrypt hash of the password
     * @throws IllegalArgumentException if password is null, empty, or invalid
     *                                  length
     * @throws IllegalStateException    if BCrypt hashing fails
     */
    public static String encode(String password) {
        validatePassword(password);

        try {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS));
            LOGGER.fine("Password encoded successfully");
            return hash;
        } catch (Exception e) {
            LOGGER.severe("Failed to encode password: " + e.getMessage());
            throw new IllegalStateException("Password encoding failed", e);
        }
    }

    /**
     * Verifies if a plaintext password matches a BCrypt hash.
     * 
     * @param password       the plaintext password to verify
     * @param hashedPassword the BCrypt hash to verify against
     * @return true if password matches, false otherwise
     * @throws IllegalArgumentException if either parameter is null or empty
     */
    public static boolean matches(String password, String hashedPassword) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }

        try {
            boolean result = BCrypt.checkpw(password, hashedPassword);
            LOGGER.fine("Password verification: " + (result ? "SUCCESS" : "FAILED"));
            return result;
        } catch (Exception e) {
            LOGGER.warning("Password verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates password strength and format.
     * 
     * @param password the password to validate
     * @throws IllegalArgumentException if password is invalid
     */
    private static void validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Password must not exceed " + MAX_PASSWORD_LENGTH + " characters");
        }
    }

    /**
     * Checks if a password meets minimum strength requirements.
     * 
     * @param password the password to check
     * @return true if password is strong enough, false otherwise
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))
                hasUpper = true;
            else if (Character.isLowerCase(c))
                hasLower = true;
            else if (Character.isDigit(c))
                hasDigit = true;
            else
                hasSpecial = true;
        }

        // Require at least 3 of 4 character types
        int typeCount = (hasUpper ? 1 : 0) + (hasLower ? 1 : 0) +
                (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);

        return typeCount >= 3;
    }

    /**
     * Gets the current BCrypt work factor.
     * 
     * @return log rounds value
     */
    public static int getLogRounds() {
        return LOG_ROUNDS;
    }
}
