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
package org.jgame.plugin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Loads game plugins from ZIP files or directories.
 *
 * <p>
 * Plugin structure:
 * </p>
 * 
 * <pre>
 * plugin.zip/
 * ├── plugin.json      # Metadata (required)
 * ├── rules.jar        # Game rules implementation (required)
 * ├── ai.jar           # AI implementation (optional)
 * └── assets/          # Images, sounds, etc.
 *     ├── images/
 *     └── sounds/
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class PluginLoader {

    private static final Logger logger = LogManager.getLogger(PluginLoader.class);
    private static final Gson gson = new Gson();

    private final Path cacheDir;
    private final Map<String, LoadedPlugin> loadedPlugins = new HashMap<>();

    /**
     * Creates a plugin loader with the specified cache directory.
     *
     * @param cacheDir directory for extracted plugins
     */
    public PluginLoader(Path cacheDir) {
        this.cacheDir = cacheDir;
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            logger.error("Failed to create cache directory: {}", cacheDir, e);
        }
    }

    /**
     * Loads a plugin from a ZIP file.
     *
     * @param zipPath path to the ZIP file
     * @return loaded plugin
     * @throws PluginLoadException if loading fails
     */
    public LoadedPlugin loadFromZip(Path zipPath) throws PluginLoadException {
        String checksum = calculateChecksum(zipPath);

        // Check if already loaded with same checksum
        for (LoadedPlugin existing : loadedPlugins.values()) {
            if (checksum.equals(existing.checksum())) {
                logger.info("Plugin already loaded: {}", existing.descriptor().id());
                return existing;
            }
        }

        // Extract to cache
        Path extractDir = cacheDir.resolve(zipPath.getFileName().toString().replace(".zip", ""));
        try {
            extractZip(zipPath, extractDir);
        } catch (IOException e) {
            throw new PluginLoadException("Failed to extract plugin: " + zipPath, e);
        }

        return loadFromDirectory(extractDir, checksum);
    }

    /**
     * Loads a plugin from a directory.
     *
     * @param pluginDir plugin directory
     * @return loaded plugin
     * @throws PluginLoadException if loading fails
     */
    public LoadedPlugin loadFromDirectory(Path pluginDir) throws PluginLoadException {
        return loadFromDirectory(pluginDir, null);
    }

    private LoadedPlugin loadFromDirectory(Path pluginDir, String checksum) throws PluginLoadException {
        // Read plugin.json
        Path metadataFile = pluginDir.resolve("plugin.json");
        if (!Files.exists(metadataFile)) {
            throw new PluginLoadException("Missing plugin.json in: " + pluginDir);
        }

        JsonObject metadata;
        try (Reader reader = Files.newBufferedReader(metadataFile)) {
            metadata = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new PluginLoadException("Failed to read plugin.json", e);
        }

        // Parse descriptor
        GameDescriptor descriptor = parseDescriptor(metadata);

        // Check for duplicate
        if (loadedPlugins.containsKey(descriptor.id())) {
            logger.warn("Replacing existing plugin: {}", descriptor.id());
        }

        // Load JAR files
        Path rulesJar = pluginDir.resolve("rules.jar");
        if (!Files.exists(rulesJar)) {
            throw new PluginLoadException("Missing rules.jar in: " + pluginDir);
        }

        // Create classloader
        List<URL> urls = new ArrayList<>();
        try {
            urls.add(rulesJar.toUri().toURL());

            Path aiJar = pluginDir.resolve("ai.jar");
            if (Files.exists(aiJar)) {
                urls.add(aiJar.toUri().toURL());
            }
        } catch (IOException e) {
            throw new PluginLoadException("Failed to create classloader URLs", e);
        }

        URLClassLoader classLoader = new URLClassLoader(
                urls.toArray(new URL[0]),
                getClass().getClassLoader());

        // Load plugin class
        String pluginClassName = metadata.has("pluginClass")
                ? metadata.get("pluginClass").getAsString()
                : "org.jgame.plugin." + descriptor.id() + ".Plugin";

        GamePlugin plugin;
        try {
            Class<?> pluginClass = classLoader.loadClass(pluginClassName);
            plugin = (GamePlugin) pluginClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new PluginLoadException("Failed to instantiate plugin: " + pluginClassName, e);
        }

        // Get assets path
        Path assetsDir = pluginDir.resolve("assets");

        LoadedPlugin loaded = new LoadedPlugin(
                descriptor,
                plugin,
                classLoader,
                assetsDir,
                checksum != null ? checksum : calculateDirectoryChecksum(pluginDir));

        loadedPlugins.put(descriptor.id(), loaded);
        logger.info("Loaded plugin: {} v{}", descriptor.name(), descriptor.version());

        return loaded;
    }

    /**
     * Gets a loaded plugin by ID.
     *
     * @param pluginId plugin identifier
     * @return loaded plugin or empty
     */
    public Optional<LoadedPlugin> getPlugin(String pluginId) {
        return Optional.ofNullable(loadedPlugins.get(pluginId));
    }

    /**
     * Gets all loaded plugins.
     *
     * @return collection of loaded plugins
     */
    public Collection<LoadedPlugin> getAllPlugins() {
        return Collections.unmodifiableCollection(loadedPlugins.values());
    }

    /**
     * Unloads a plugin.
     *
     * @param pluginId plugin identifier
     */
    public void unloadPlugin(String pluginId) {
        LoadedPlugin plugin = loadedPlugins.remove(pluginId);
        if (plugin != null) {
            try {
                plugin.classLoader().close();
            } catch (IOException e) {
                logger.warn("Failed to close classloader for: {}", pluginId, e);
            }
            logger.info("Unloaded plugin: {}", pluginId);
        }
    }

    private GameDescriptor parseDescriptor(JsonObject json) {
        Map<String, Object> metadata = new HashMap<>();
        if (json.has("metadata")) {
            json.getAsJsonObject("metadata").entrySet()
                    .forEach(e -> metadata.put(e.getKey(), gson.fromJson(e.getValue(), Object.class)));
        }

        return new GameDescriptor(
                json.get("id").getAsString(),
                json.get("name").getAsString(),
                json.has("version") ? json.get("version").getAsString() : "1.0",
                json.has("author") ? json.get("author").getAsString() : "Unknown",
                json.has("description") ? json.get("description").getAsString() : "",
                json.has("instructions") ? json.get("instructions").getAsString() : "",
                json.has("minPlayers") ? json.get("minPlayers").getAsInt() : 1,
                json.has("maxPlayers") ? json.get("maxPlayers").getAsInt() : 4,
                metadata);
    }

    private void extractZip(Path zipPath, Path destDir) throws IOException {
        Files.createDirectories(destDir);

        try (ZipFile zipFile = new ZipFile(zipPath.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                Path entryPath = destDir.resolve(entry.getName());

                // Security check for zip slip
                if (!entryPath.normalize().startsWith(destDir.normalize())) {
                    throw new IOException("Zip entry outside target directory: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        Files.copy(in, entryPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

    private String calculateChecksum(Path file) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] data = Files.readAllBytes(file);
            byte[] hash = md.digest(data);
            return bytesToHex(hash);
        } catch (Exception e) {
            logger.warn("Failed to calculate checksum for: {}", file, e);
            return "";
        }
    }

    private String calculateDirectoryChecksum(Path dir) {
        // Simple implementation - in production, would hash all files
        return dir.getFileName().toString() + "-" + System.currentTimeMillis();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Represents a loaded plugin with its resources.
     */
    public record LoadedPlugin(
            GameDescriptor descriptor,
            GamePlugin plugin,
            URLClassLoader classLoader,
            Path assetsDir,
            String checksum) {

        /**
         * Gets an asset file from this plugin.
         *
         * @param relativePath relative path within assets directory
         * @return path to the asset file
         */
        public Path getAsset(String relativePath) {
            return assetsDir.resolve(relativePath);
        }

        /**
         * Checks if an asset exists.
         *
         * @param relativePath relative path within assets directory
         * @return true if asset exists
         */
        public boolean hasAsset(String relativePath) {
            return Files.exists(getAsset(relativePath));
        }
    }

    /**
     * Exception thrown when plugin loading fails.
     */
    public static class PluginLoadException extends Exception {
        public PluginLoadException(String message) {
            super(message);
        }

        public PluginLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
