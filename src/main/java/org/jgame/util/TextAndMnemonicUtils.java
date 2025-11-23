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

package org.jgame.util;

import javax.swing.*;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility class for parsing text and mnemonic characters from resource bundles.
 * Supports the format "Text@M" where M is the mnemonic character.
 * 
 * <p>
 * Example: "File@F" = text "File" with mnemonic 'F'
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class TextAndMnemonicUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private TextAndMnemonicUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Gets a string from the resource bundle.
     *
     * @param key the resource key
     * @return the localized string
     */
    public static String getString(String key) {
        try {
            return I18n.get(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    /**
     * Gets the mnemonic character from a resource key.
     * Looks for pattern "text@mnemonic" in the resource value.
     *
     * @param key the resource key
     * @return the mnemonic character code, or 0 if none
     */
    public static int getMnemonic(String key) {
        String value = getString(key);
        if (value.contains("@") && value.length() > value.indexOf('@') + 1) {
            char mnemonicChar = value.charAt(value.indexOf('@') + 1);
            return Character.toUpperCase(mnemonicChar);
        }
        return 0;
    }

    /**
     * Gets the text without the mnemonic marker.
     *
     * @param key the resource key
     * @return the text without mnemonic marker
     */
    public static String getTextWithoutMnemonic(String key) {
        String value = getString(key);
        int atIndex = value.indexOf('@');
        if (atIndex >= 0) {
            return value.substring(0, atIndex);
        }
        return value;
    }

    /**
     * Configures a button or menu item with text and mnemonic from resources.
     *
     * @param component the component to configure
     * @param key       the resource key
     */
    public static void configureText(AbstractButton component, String key) {
        component.setText(getTextWithoutMnemonic(key));
        int mnemonic = getMnemonic(key);
        if (mnemonic != 0) {
            component.setMnemonic(mnemonic);
        }
    }
}
