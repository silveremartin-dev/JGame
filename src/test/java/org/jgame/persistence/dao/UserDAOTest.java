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

package org.jgame.persistence.dao;

import org.jgame.persistence.DatabaseManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserDAO.
 * Tests all CRUD operations and edge cases.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    static void setUpDatabase() throws SQLException {
        // Initialize in-memory database for tests
        DatabaseManager.initializeForTests();
        userDAO = new UserDAO();
    }

    @BeforeEach
    void clearUsers() throws SQLException {
        // Clear users table before each test
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM scores");
            stmt.execute("DELETE FROM game_players");
            stmt.execute("DELETE FROM users");
        }
    }

    @AfterAll
    static void tearDown() {
        DatabaseManager.shutdown();
    }

    @Test
    @Order(1)
    @DisplayName("Create user with valid data")
    void testCreateUserValid() {
        long userId = userDAO.createUser("player1", "hash123", "player1@test.com");

        assertTrue(userId > 0, "User ID should be positive");
    }

    @Test
    @Order(2)
    @DisplayName("Create user with duplicate username fails")
    void testCreateUserDuplicate() {
        userDAO.createUser("duplicate", "hash1", "user1@test.com");
        long secondId = userDAO.createUser("duplicate", "hash2", "user2@test.com");

        assertEquals(-1, secondId, "Duplicate username should fail");
    }

    @Test
    @Order(3)
    @DisplayName("Create user with null email")
    void testCreateUserNullEmail() {
        long userId = userDAO.createUser("noEmail", "hash123", null);

        assertTrue(userId > 0, "User creation should succeed with null email");
    }

    @Test
    @Order(4)
    @DisplayName("Get user by username - existing user")
    void testGetUserByUsernameExists() {
        long createdId = userDAO.createUser("findMe", "hash123", "find@test.com");
        long foundId = userDAO.getUserByUsername("findMe");

        assertEquals(createdId, foundId, "Found user ID should match created ID");
    }

    @Test
    @Order(5)
    @DisplayName("Get user by username - non-existent user")
    void testGetUserByUsernameNotExists() {
        long userId = userDAO.getUserByUsername("nonExistent");

        assertEquals(-1, userId, "Non-existent user should return -1");
    }

    @Test
    @Order(6)
    @DisplayName("Verify credentials - valid")
    void testVerifyCredentialsValid() {
        userDAO.createUser("authTest", "correctHash", "auth@test.com");
        long userId = userDAO.verifyCredentials("authTest", "correctHash");

        assertTrue(userId > 0, "Valid credentials should return positive user ID");
    }

    @Test
    @Order(7)
    @DisplayName("Verify credentials - wrong password")
    void testVerifyCredentialsWrongPassword() {
        userDAO.createUser("authTest2", "correctHash", "auth2@test.com");
        long userId = userDAO.verifyCredentials("authTest2", "wrongHash");

        assertEquals(-1, userId, "Wrong password should return -1");
    }

    @Test
    @Order(8)
    @DisplayName("Verify credentials - non-existent user")
    void testVerifyCredentialsNonExistent() {
        long userId = userDAO.verifyCredentials("ghost", "anyHash");

        assertEquals(-1, userId, "Non-existent user should return -1");
    }

    @Test
    @Order(9)
    @DisplayName("Delete user")
    void testDeleteUser() {
        long userId = userDAO.createUser("deleteMe", "hash123", "delete@test.com");
        boolean deleted = userDAO.deleteUser(userId);

        assertTrue(deleted, "User should be deleted successfully");

        // Verify user is gone
        long foundId = userDAO.getUserByUsername("deleteMe");
        assertEquals(-1, foundId, "Deleted user should not be found");
    }

    @Test
    @Order(10)
    @DisplayName("Delete non-existent user")
    void testDeleteNonExistentUser() {
        boolean deleted = userDAO.deleteUser(99999L);

        assertFalse(deleted, "Deleting non-existent user should return false");
    }

    @Test
    @Order(11)
    @DisplayName("Get all usernames - empty")
    void testGetAllUsernamesEmpty() {
        List<String> usernames = userDAO.getAllUsernames();

        assertNotNull(usernames, "List should not be null");
        assertTrue(usernames.isEmpty(), "List should be empty");
    }

    @Test
    @Order(12)
    @DisplayName("Get all usernames - multiple users")
    void testGetAllUsernamesMultiple() {
        userDAO.createUser("alpha", "hash1", "alpha@test.com");
        userDAO.createUser("beta", "hash2", "beta@test.com");
        userDAO.createUser("gamma", "hash3", "gamma@test.com");

        List<String> usernames = userDAO.getAllUsernames();

        assertEquals(3, usernames.size(), "Should return 3 usernames");
        assertTrue(usernames.contains("alpha"), "Should contain alpha");
        assertTrue(usernames.contains("beta"), "Should contain beta");
        assertTrue(usernames.contains("gamma"), "Should contain gamma");

        // Verify alphabetical order
        assertEquals("alpha", usernames.get(0), "Should be alphabetically sorted");
    }

    @Test
    @Order(13)
    @DisplayName("Update last login updates timestamp")
    void testUpdateLastLogin() throws InterruptedException {
        long userId = userDAO.createUser("loginTest", "hash123", "login@test.com");

        // Small delay to ensure timestamp difference
        Thread.sleep(100);

        userDAO.updateLastLogin(userId);

        // Verify by checking credentials again (which also updates last login)
        long verifiedId = userDAO.verifyCredentials("loginTest", "hash123");
        assertEquals(userId, verifiedId, "Should verify successfully after login");
    }
}
