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

import java.util.regex.Pattern;

/**
 * Input validation utilities for API requests.
 *
 * @author Google Gemini (Antigravity)
 * @version 1.0
 */
public final class InputValidator {

    // Username: 3-32 chars, alphanumeric and underscores only
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,32}$");

    // Password: 8-128 chars
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 128;

    // Email: basic pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private InputValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Validates a username.
     *
     * @param username the username to validate
     * @return validation result
     */
    public static ValidationResult validateUsername(String username) {
        if (username == null || username.isBlank()) {
            return ValidationResult.error("Username is required");
        }
        if (username.length() < 3) {
            return ValidationResult.error("Username must be at least 3 characters");
        }
        if (username.length() > 32) {
            return ValidationResult.error("Username must be at most 32 characters");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return ValidationResult.error("Username can only contain letters, numbers, and underscores");
        }
        return ValidationResult.ok();
    }

    /**
     * Validates a password.
     *
     * @param password the password to validate
     * @return validation result
     */
    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return ValidationResult.error("Password is required");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return ValidationResult.error("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            return ValidationResult.error("Password is too long");
        }

        // Check complexity (at least one letter and one number)
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c))
                hasLetter = true;
            if (Character.isDigit(c))
                hasDigit = true;
        }

        if (!hasLetter || !hasDigit) {
            return ValidationResult.error("Password must contain at least one letter and one number");
        }

        return ValidationResult.ok();
    }

    /**
     * Validates an email address.
     *
     * @param email the email to validate
     * @return validation result
     */
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return ValidationResult.ok(); // Email is optional
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ValidationResult.error("Invalid email format");
        }
        return ValidationResult.ok();
    }

    /**
     * Validation result.
     */
    public record ValidationResult(boolean valid, String message) {
        public static ValidationResult ok() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return valid;
        }
    }
}
