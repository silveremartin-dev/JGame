/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.security;

import org.jgame.server.security.InputValidator.ValidationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InputValidator.
 */
class InputValidatorTest {

    @Test
    @DisplayName("Should validate valid username")
    void shouldValidateValidUsername() {
        assertTrue(InputValidator.validateUsername("john_doe").isValid());
        assertTrue(InputValidator.validateUsername("User123").isValid());
        assertTrue(InputValidator.validateUsername("abc").isValid());
    }

    @Test
    @DisplayName("Should reject null or blank username")
    void shouldRejectNullOrBlankUsername() {
        assertFalse(InputValidator.validateUsername(null).isValid());
        assertFalse(InputValidator.validateUsername("").isValid());
        assertFalse(InputValidator.validateUsername("  ").isValid());
    }

    @Test
    @DisplayName("Should reject short username")
    void shouldRejectShortUsername() {
        ValidationResult result = InputValidator.validateUsername("ab");
        assertFalse(result.isValid());
        assertTrue(result.message().contains("at least 3"));
    }

    @Test
    @DisplayName("Should reject username with special characters")
    void shouldRejectUsernameWithSpecialChars() {
        assertFalse(InputValidator.validateUsername("user!name").isValid());
        assertFalse(InputValidator.validateUsername("user@name").isValid());
        assertFalse(InputValidator.validateUsername("user name").isValid());
    }

    @Test
    @DisplayName("Should validate valid password")
    void shouldValidateValidPassword() {
        assertTrue(InputValidator.validatePassword("Password123").isValid());
        assertTrue(InputValidator.validatePassword("MyP4ssw0rd").isValid());
    }

    @Test
    @DisplayName("Should reject short password")
    void shouldRejectShortPassword() {
        ValidationResult result = InputValidator.validatePassword("Pass1");
        assertFalse(result.isValid());
        assertTrue(result.message().contains("at least 8"));
    }

    @Test
    @DisplayName("Should reject password without numbers")
    void shouldRejectPasswordWithoutNumbers() {
        ValidationResult result = InputValidator.validatePassword("PasswordOnly");
        assertFalse(result.isValid());
        assertTrue(result.message().contains("letter and one number"));
    }

    @Test
    @DisplayName("Should reject password without letters")
    void shouldRejectPasswordWithoutLetters() {
        ValidationResult result = InputValidator.validatePassword("12345678");
        assertFalse(result.isValid());
    }

    @Test
    @DisplayName("Should validate valid email")
    void shouldValidateValidEmail() {
        assertTrue(InputValidator.validateEmail("user@example.com").isValid());
        assertTrue(InputValidator.validateEmail("user.name@domain.org").isValid());
    }

    @Test
    @DisplayName("Should accept null or blank email")
    void shouldAcceptNullOrBlankEmail() {
        assertTrue(InputValidator.validateEmail(null).isValid());
        assertTrue(InputValidator.validateEmail("").isValid());
    }

    @Test
    @DisplayName("Should reject invalid email format")
    void shouldRejectInvalidEmailFormat() {
        assertFalse(InputValidator.validateEmail("notanemail").isValid());
        assertFalse(InputValidator.validateEmail("missing@domain").isValid());
    }
}
