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
package org.jgame.plugin.impl;

import org.jgame.logic.GameInterface;
import org.jgame.logic.games.checkers.CheckersRules;
import org.jgame.plugin.GameDescriptor;
import org.jgame.plugin.GamePlugin;
import org.jgame.plugin.ui.GamePanel;

import java.util.Map;

/**
 * Plugin implementation for Checkers (Draughts).
 */
public class CheckersPlugin implements GamePlugin {

    private static final GameDescriptor DESCRIPTOR = new GameDescriptor(
            "checkers",
            "Checkers",
            "1.0",
            "JGame Team",
            "Classic strategy board game",
            "Capture all opponent pieces by jumping over them diagonally.",
            2,
            2,
            Map.of(
                    "difficulty", "medium",
                    "playtime", "10-20 minutes",
                    "category", "strategy"));

    @Override
    public GameDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public GameInterface createRules() {
        return new CheckersRules();
    }

    @Override
    public GamePanel createPanel(GameInterface game) {
        if (game instanceof CheckersRules checkersRules) {
            return new CheckersPanel(checkersRules);
        }
        throw new IllegalArgumentException("Game must be CheckersRules");
    }
}
