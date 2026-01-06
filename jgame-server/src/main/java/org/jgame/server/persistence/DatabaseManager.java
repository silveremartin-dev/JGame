package org.jgame.server.persistence;

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
 * Singleton pattern implementation for Dependency Injection compatibility.
 * 
 * <p>
 * Loads configuration from environment variables or properties file:
 * <ul>
 * <li>JGAME_DB_URL / db.url - JDBC URL</li>
 * <li>JGAME_DB_USER / db.user - Database username</li>
 * <li>JGAME_DB_PASSWORD / db.password - Database password</li>
 * </ul>
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 3.1
 */
public class DatabaseManager {

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    // Default values for development
    private static final String DEFAULT_URL = "jdbc:h2:./data/jgame;AUTO_SERVER=TRUE";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASSWORD = "";

    // Singleton instance
    private static volatile DatabaseManager instance;

    private HikariDataSource dataSource;
    private boolean initialized = false;

    // Private constructor for Singleton
    private DatabaseManager() {
    }

    /**
     * Gets the singleton instance of DatabaseManager.
     * Thread-safe lazy initialization.
     * 
     * @return the DatabaseManager instance
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    /**
     * Initializes the database connection pool and schema.
     * Safe to call multiple times - will only initialize once.
     * 
     * @throws SQLException if database connection or schema creation fails
     */
    public synchronized void initialize() throws SQLException {
        if (initialized) {
            LOGGER.info("Database already initialized");
            return;
        }

        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");

            // Load configuration
            String url = getConfig("JGAME_DB_URL", "db.url", DEFAULT_URL);
            String user = getConfig("JGAME_DB_USER", "db.user", DEFAULT_USER);
            String password = getConfig("JGAME_DB_PASSWORD", "db.password", DEFAULT_PASSWORD);

            LOGGER.info("Database URL: " + url + " (from " +
                    (System.getenv("JGAME_DB_URL") != null ? "environment" : "config/default") + ")");

            // Load performance properties
            Properties perfProps = loadPerformanceProperties();

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);

            // Connection pool settings from performance.properties
            config.setMaximumPoolSize(Integer.parseInt(
                    perfProps.getProperty("hikaricp.maximumPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(
                    perfProps.getProperty("hikaricp.minimumIdle", "5")));
            config.setConnectionTimeout(Long.parseLong(
                    perfProps.getProperty("hikaricp.connectionTimeout", "30000")));
            config.setIdleTimeout(Long.parseLong(
                    perfProps.getProperty("hikaricp.idleTimeout", "600000")));
            config.setMaxLifetime(Long.parseLong(
                    perfProps.getProperty("hikaricp.maxLifetime", "1800000")));

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
     * Gets configuration value from environment variable, properties, or default.
     */
    private String getConfig(String envVar, String propKey, String defaultValue) {
        // Priority 1: Environment variable
        String envValue = System.getenv(envVar);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        // Priority 2: application.properties
        Properties props = loadApplicationProperties();
        String propValue = props.getProperty(propKey);
        if (propValue != null && !propValue.isBlank() && !propValue.startsWith("${")) {
            return propValue;
        }

        // Priority 3: Default
        return defaultValue;
    }

    /**
     * Loads application properties from file.
     */
    private Properties loadApplicationProperties() {
        Properties props = new Properties();
        try (InputStream is = DatabaseManager.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            LOGGER.warning("Could not load application.properties");
        }
        return props;
    }

    /**
     * Loads performance properties from file.
     */
    private Properties loadPerformanceProperties() throws IOException {
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
    private void initializeWithDefaults() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getConfig("JGAME_DB_URL", "db.url", DEFAULT_URL));
        config.setUsername(getConfig("JGAME_DB_USER", "db.user", DEFAULT_USER));
        config.setPassword(getConfig("JGAME_DB_PASSWORD", "db.password", DEFAULT_PASSWORD));
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
    private void executeSchema() throws SQLException {
        try (InputStream is = DatabaseManager.class.getResourceAsStream("/db/schema.sql")) {
            if (is == null) {
                throw new SQLException("schema.sql not found in resources");
            }

            String schema = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .filter(line -> !line.trim().startsWith("--"))
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
    public Connection getConnectionInternal() throws SQLException {
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
    public synchronized void shutdown() {
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
    public boolean isInitialized() {
        return initialized && dataSource != null && !dataSource.isClosed();
    }

    /**
     * Gets pool statistics for monitoring.
     * 
     * @return formatted pool statistics string
     */
    public String getPoolStats() {
        if (dataSource != null && !dataSource.isClosed()) {
            return String.format("Pool[active=%d, idle=%d, total=%d, waiting=%d]",
                    dataSource.getHikariPoolMXBean().getActiveConnections(),
                    dataSource.getHikariPoolMXBean().getIdleConnections(),
                    dataSource.getHikariPoolMXBean().getTotalConnections(),
                    dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        return "Pool[not initialized]";
    }

    // --- Static Delegates for Backward Compatibility ---

}
