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
package org.jgame.server.persistence.dao;

import org.jgame.server.persistence.DatabaseManager;
import org.jgame.util.PasswordEncoderSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object for User entity.
 * Handles all database operations related to users.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    private final DatabaseManager dbManager;

    /**
     * Creates a new UserDAO with injected DatabaseManager.
     * 
     * @param dbManager database manager instance
     */
    public UserDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Creates a new user in the database.
     * 
     * @param username     unique username
     * @param password     raw or hashed password
     * @param email        user email
     * @return generated user ID, or -1 if failed
     */
    public long createUser(String username, String password, String email) {
        String passwordHash = password;
        if (password != null && !password.startsWith("$2a$") && !password.startsWith("$2b$")) {
            passwordHash = PasswordEncoderSingleton.encode(password);
        }

        String sql = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.setString(3, email);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long userId = rs.getLong(1);
                        LOGGER.info("Created user: " + username + " (ID: " + userId + ")");
                        return userId;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error creating user: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Finds a user by username.
     * 
     * @param username username to search for
     * @return user ID if found, -1 otherwise
     */
    public long getUserByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding user: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Verifies user credentials using BCrypt.
     * 
     * @param username username
     * @param password plaintext password to verify
     * @return user ID if credentials valid, -1 otherwise
     */
    public long verifyCredentials(String username, String password) {
        if (username == null || password == null) {
            return -1;
        }

        String sql = "SELECT id, password_hash FROM users WHERE username = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long userId = rs.getLong("id");
                    String storedHash = rs.getString("password_hash");

                    boolean matches = false;
                    if (storedHash != null) {
                        if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$")) {
                            matches = PasswordEncoderSingleton.matches(password, storedHash);
                        } else {
                            // Fallback for legacy plaintext entries with automatic upgrade
                            matches = storedHash.equals(password);
                            if (matches) {
                                updateUser(username, null, password);
                            }
                        }
                    }

                    if (matches) {
                        updateLastLogin(userId);
                        return userId;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error verifying credentials: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Updates the last login timestamp for a user.
     * 
     * @param userId user ID
     */
    public void updateLastLogin(long userId) {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.warning("Error updating last login: " + e.getMessage());
        }
    }

    /**
     * Deletes a user by ID.
     * 
     * @param userId user ID to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteUser(long userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                LOGGER.info("Deleted user ID: " + userId);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.severe("Error deleting user: " + e.getMessage());
        }

        return false;
    }

    /**
     * Gets all usernames in the database.
     * 
     * @return list of usernames
     */
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM users ORDER BY username";

        try (Connection conn = dbManager.getConnectionInternal();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting usernames: " + e.getMessage());
        }

        return usernames;
    }

    /**
     * Updates user profile (email and/or password).
     *
     * @param username username to update
     * @param email    new email (optional)
     * @param password new raw password (optional, will be hashed)
     * @return true if updated, false otherwise
     */
    public boolean updateUser(String username, String email, String password) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        List<Object> params = new ArrayList<>();

        if (email != null) {
            sql.append("email = ?");
            params.add(email);
        }

        if (password != null) {
            if (!params.isEmpty()) {
                sql.append(", ");
            }
            String passwordHash = password;
            if (!password.startsWith("$2a$") && !password.startsWith("$2b$")) {
                passwordHash = PasswordEncoderSingleton.encode(password);
            }
            sql.append("password_hash = ?");
            params.add(passwordHash);
        }

        if (params.isEmpty()) {
            return false; // Nothing to update
        }

        sql.append(" WHERE username = ?");
        params.add(username);

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error updating user: " + e.getMessage());
            return false;
        }
    }
}
