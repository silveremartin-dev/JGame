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

package org.jgame.persistence;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manages the H2 database connection and initialization.
 * 
 * <p>
 * This class handles:
 * - Database connection pooling
 * - Schema initialization from schema.sql
 * - Connection lifecycle management
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class DatabaseManager {

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_URL = "jdbc:h2:./data/jgame;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static Connection connection;
    private static boolean initialized = false;

    // Prevent instantiation
    private DatabaseManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Initializes the database connection and schema.
     * Safe to call multiple times - will only initialize once.
     * 
     * @throws SQLException if database connection or schema creation fails
     */
    public static synchronized void initialize() throws SQLException {
        if (initialized) {
            LOGGER.info("Database already initialized");
            return;
        }

        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");

            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            LOGGER.info("Database connection established: " + DB_URL);

            // Execute schema
            executeSchema();

            initialized = true;
            LOGGER.info("Database initialized successfully");

        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 Driver not found", e);
        }
    }

    /**
     * Executes the schema.sql file to create tables.
     */
    private static void executeSchema() throws SQLException {
        try (InputStream is = DatabaseManager.class.getResourceAsStream("/db/schema.sql")) {
            if (is == null) {
                throw new SQLException("schema.sql not found in resources");
            }

            String schema = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Statement stmt = connection.createStatement()) {
                for (String sql : schema.split(";")) {
                    sql = sql.trim();
                    if (!sql.isEmpty() && !sql.startsWith("--")) {
                        stmt.execute(sql);
                    }
                }
            }

            LOGGER.info("Database schema initialized");

        } catch (Exception e) {
            throw new SQLException("Failed to execute schema", e);
        }
    }

    /**
     * Gets the database connection.
     * Initializes the database if not already done.
     * 
     * @return active database connection
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initialize();
        }

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }

        return connection;
    }

    /**
     * Closes the database connection.
     * Should be called on application shutdown.
     */
    public static synchronized void shutdown() {
        if (connection != null) {
            try {
                connection.close();
                initialized = false;
                LOGGER.info("Database connection closed");
            } catch (SQLException e) {
                LOGGER.severe("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Checks if the database is initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
