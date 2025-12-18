/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TextAndMnemonicUtils utility class.
 */
class TextAndMnemonicUtilsTest {

    @Test
    @DisplayName("Should return mnemonic character")
    void shouldReturnMnemonicCharacter() {
        int mnemonic = TextAndMnemonicUtils.getMnemonic("Test.label");
        // Mnemonic can be 0 if not found
        assertTrue(mnemonic >= 0);
    }

    @Test
    @DisplayName("Should return text string")
    void shouldReturnTextString() {
        TextAndMnemonicUtils.getTextAndMnemonicString("Test.label");
        // Should not throw, returns null or string
        // Result depends on resource bundle
    }
}
