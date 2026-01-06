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
package org.jgame.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Internationalization utility class for managing localized messages.
 * Provides centralized access to resource bundles for multi-language support.
 * 
 * <p>
 * Usage example:
 * 
 * <pre>
 * String message = I18n.get("menu.file.open");
 * String formatted = I18n.format("server.started", port);
 * </pre>
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public final class I18n {

    /** Base name for resource bundles */
    private static final String BUNDLE_NAME = "i18n.messages";

    /** Current locale */
    private static Locale currentLocale = Locale.getDefault();

    /** Current resource bundle */
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);

    /**
     * Private constructor to prevent instantiation.
     */
    private I18n() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Gets a localized string for the given key.
     *
     * @param key the message key
     * @return the localized string, or the key itself if not found
     */
    public static String get(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    /**
     * Gets a localized string with parameter substitution.
     *
     * @param key    the message key
     * @param params parameters to substitute into the message
     * @return the formatted localized string
     */
    public static String format(String key, Object... params) {
        String pattern = get(key);
        return MessageFormat.format(pattern, params);
    }

    /**
     * Sets the current locale and reloads the resource bundle.
     *
     * @param locale the new locale
     */
    public static void setLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
    }

    /**
     * Gets the current locale.
     *
     * @return the current locale
     */
    public static Locale getLocale() {
        return currentLocale;
    }

    /**
     * Gets the current resource bundle.
     *
     * @return the current resource bundle
     */
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Checks if a key exists in the resource bundle.
     *
     * @param key the key to check
     * @return true if the key exists, false otherwise
     */
    public static boolean hasKey(String key) {
        try {
            resourceBundle.getString(key);
            return true;
        } catch (MissingResourceException e) {
            return false;
        }
    }
}
