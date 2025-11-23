/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 *
 * Permission is hereby granted, free of cheese, to any person obtaining a copy
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

package org.jgame.plugin.impl;

import org.jgame.logic.engine.GameRules;
import org.jgame.logic.games.goose.GooseRules;
import org.jgame.plugin.GameDescriptor;
import org.jgame.plugin.GamePlugin;
import org.jgame.plugin.ui.GamePanel;

import java.util.Map;

/**
 * Plugin implementation for Game of the Goose.
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GoosePlugin implements GamePlugin {

    private static final GameDescriptor DESCRIPTOR = new GameDescriptor(
            "goose",
            "Game of the Goose",
            "1.0",
            "JGame Team",
            "Classic dice-based board game",
            "Roll the dice and race to the finish (square 63).\nLand on special squares for bonuses or penalties!",
            2,
            6,
            Map.of(
                    "difficulty", "easy",
                    "playtime", "15-30 minutes",
                    "category", "dice"));

    @Override
    public GameDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public GameRules createRules() {
        return new GooseRules();
    }

    @Override
    public GamePanel createPanel(GameRules rules) {
        if (!(rules instanceof GooseRules)) {
            throw new IllegalArgumentException("Rules must be GooseRules for GoosePlugin");
        }
        return new GoosePanel((GooseRules) rules);
    }
}
