/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.plugin.impl;

import org.jgame.logic.engine.GameRules;
import org.jgame.logic.games.chess.ChessRules;
import org.jgame.plugin.GameDescriptor;
import org.jgame.plugin.GamePlugin;
import org.jgame.plugin.ui.GamePanel;

import java.util.Map;

/**
 * Plugin implementation for Chess.
 */
public class ChessPlugin implements GamePlugin {

    private static final GameDescriptor DESCRIPTOR = new GameDescriptor(
            "chess",
            "Chess",
            "1.0",
            "JGame Team",
            "Classic strategy board game",
            "Control your pieces to checkmate the opponent's king!",
            2,
            2,
            Map.of(
                    "difficulty", "hard",
                    "playtime", "30-60 minutes",
                    "category", "strategy"));

    @Override
    public GameDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public GameRules createRules() {
        return new ChessRules();
    }

    @Override
    public GamePanel createPanel(GameRules rules) {
        if (!(rules instanceof ChessRules)) {
            throw new IllegalArgumentException("Rules must be ChessRules for ChessPlugin");
        }
        return new ChessPanel((ChessRules) rules);
    }
}
