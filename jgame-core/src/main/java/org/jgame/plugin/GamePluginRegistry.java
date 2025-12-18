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

package org.jgame.plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Registry for managing game plugins.
 * 
 * <p>
 * This class is responsible for:
 * <ul>
 * <li>Registering game plugins</li>
 * <li>Retrieving plugins by ID</li>
 * <li>Listing available games</li>
 * <li>Validating plugin uniqueness</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Thread-safe singleton implementation.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class GamePluginRegistry {

    private static final Logger LOGGER = Logger.getLogger(GamePluginRegistry.class.getName());

    /** Singleton instance */
    private static final GamePluginRegistry INSTANCE = new GamePluginRegistry();

    /** Map of game ID to plugin */
    private final Map<String, GamePlugin> plugins = new ConcurrentHashMap<>();

    /** Private constructor for singleton */
    private GamePluginRegistry() {
        // Auto-register built-in games
        registerBuiltInPlugins();
    }

    /**
     * Gets the singleton registry instance.
     * 
     * @return the registry instance
     */
    public static GamePluginRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a game plugin.
     * 
     * @param plugin the plugin to register
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException    if a plugin with same ID already exists
     */
    public void registerPlugin(GamePlugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        GameDescriptor descriptor = plugin.getDescriptor();
        String gameId = descriptor.id();

        if (plugins.containsKey(gameId)) {
            throw new IllegalStateException(
                    "Plugin with ID '" + gameId + "' is already registered");
        }

        plugins.put(gameId, plugin);
        LOGGER.info("Registered plugin: " + descriptor.name() + " (" + gameId + ")");
    }

    /**
     * Gets a plugin by game ID.
     * 
     * @param gameId the game identifier
     * @return the plugin, or null if not found
     */
    public GamePlugin getPlugin(String gameId) {
        return plugins.get(gameId);
    }

    /**
     * Gets all registered plugins.
     * 
     * @return unmodifiable collection of plugins
     */
    public Collection<GamePlugin> getAllPlugins() {
        return Collections.unmodifiableCollection(plugins.values());
    }

    /**
     * Gets descriptors for all available games.
     * 
     * @return list of game descriptors
     */
    public List<GameDescriptor> getAvailableGames() {
        return plugins.values().stream()
                .map(GamePlugin::getDescriptor)
                .sorted(Comparator.comparing(GameDescriptor::name))
                .toList();
    }

    /**
     * Checks if a plugin is registered.
     * 
     * @param gameId the game identifier
     * @return true if plugin exists
     */
    public boolean hasPlugin(String gameId) {
        return plugins.containsKey(gameId);
    }

    /**
     * Gets the number of registered plugins.
     * 
     * @return plugin count
     */
    public int getPluginCount() {
        return plugins.size();
    }

    /**
     * Registers built-in game plugins.
     */
    private void registerBuiltInPlugins() {
        // Plugins will be registered here
        // This is called during initialization
        LOGGER.info("Initializing GamePluginRegistry");

        try {
            // Register built-in games via ServiceLoader to avoid circular dependencies
            ServiceLoader<GamePlugin> loader = ServiceLoader.load(GamePlugin.class);
            for (GamePlugin plugin : loader) {
                registerPlugin(plugin);
            }
            LOGGER.info("Loaded " + plugins.size() + " plugins via ServiceLoader");
        } catch (Exception e) {
            LOGGER.severe("Failed to register built-in plugins: " + e.getMessage());
        }
    }
}
