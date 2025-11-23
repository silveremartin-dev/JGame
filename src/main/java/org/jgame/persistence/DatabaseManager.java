package org.jgame.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manages the H2 database connection pooling and initialization.
 * 
 * <p>
 * This class handles:
 * - HikariCP connection pooling for high performance
 * - Schema initialization from schema.sql
 * - Connection lifecycle management
 * - Configurable pool settings from performance.properties
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class DatabaseManager {

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_URL = "jdbc:h2:./data/jgame;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static HikariDataSource dataSource;
    private static boolean initialized = false;

    // Prevent instantiation
    private DatabaseManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Initializes the database connection pool and schema.
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

            // Load performance properties
            Properties props = loadPerformanceProperties();

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DB_URL);
            config.setUsername(DB_USER);
            config.setPassword(DB_PASSWORD);

            // Connection pool settings from performance.properties
            config.setMaximumPoolSize(Integer.parseInt(
                    props.getProperty("hikaricp.maximumPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(
                    props.getProperty("hikaricp.minimumIdle", "5")));
            config.setConnectionTimeout(Long.parseLong(
                    props.getProperty("hikaricp.connectionTimeout", "30000")));
            config.setIdleTimeout(Long.parseLong(
                    props.getProperty("hikaricp.idleTimeout", "600000")));
            config.setMaxLifetime(Long.parseLong(
                    props.getProperty("hikaricp.maxLifetime", "1800000")));

            // Pool name for logging
            config.setPoolName("JGamePool");

            // Performance optimizations
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            // Create data source
            dataSource = new HikariDataSource(config);
            LOGGER.info("HikariCP pool created: " + config.getPoolName());

            // Execute schema
            executeSchema();

            initialized = true;
            LOGGER.info("Database initialized successfully with HikariCP");

        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 Driver not found", e);
        } catch (IOException e) {
            LOGGER.warning("Could not load performance.properties, using defaults");
            // Initialize with defaults if properties file missing
            initializeWithDefaults();
        }
    }

    /**
     * Loads performance properties from file.
     */
    private static Properties loadPerformanceProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream is = DatabaseManager.class.getResourceAsStream("/performance.properties")) {
            if (is != null) {
                props.load(is);
            }
        }
        return props;
    }

    /**
     * Initialize with default settings if properties file not found.
     */
    private static void initializeWithDefaults() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setPoolName("JGamePool");

        dataSource = new HikariDataSource(config);
        executeSchema();
        initialized = true;
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

            try (Connection conn = dataSource.getConnection();
                    Statement stmt = conn.createStatement()) {
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
     * Gets a database connection from the pool.
     * Initializes the database if not already done.
     * 
     * @return pooled database connection
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initialize();
        }

        if (dataSource == null || dataSource.isClosed()) {
            throw new SQLException("DataSource is not available");
        }

        return dataSource.getConnection();
    }

    /**
     * Closes the connection pool and releases all resources.
     * Should be called on application shutdown.
     */
    public static synchronized void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            initialized = false;
            LOGGER.info("HikariCP pool closed");
        }
    }

    /**
     * Checks if the database is initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized && dataSource != null && !dataSource.isClosed();
    }

    /**
     * Gets pool statistics for monitoring.
     * 
     * @return formatted pool statistics string
     */
    public static String getPoolStats() {
        if (dataSource != null && !dataSource.isClosed()) {
            return String.format("Pool[active=%d, idle=%d, total=%d, waiting=%d]",
                    dataSource.getHikariPoolMXBean().getActiveConnections(),
                    dataSource.getHikariPoolMXBean().getIdleConnections(),
                    dataSource.getHikariPoolMXBean().getTotalConnections(),
                    dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        return "Pool[not initialized]";
    }
}
