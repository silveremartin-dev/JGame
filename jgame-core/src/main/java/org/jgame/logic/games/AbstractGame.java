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


package org.jgame.logic.games;

import org.jetbrains.annotations.NotNull;
import org.jgame.logic.GameInterface;
import org.jgame.logic.Rule;
import org.jgame.parts.PieceInterface;

import java.util.Set;

public abstract class AbstractGame implements GameInterface {
    
    private final String name;
    private final String version;
    private final String description;
    private Set<PieceInterface> pieces;
    private Set<Rule> ruleset;

    public AbstractGame(@NotNull String name, @NotNull String version, String description) {
        this.name = name;
        this.version = version;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Set<PieceInterface> getPieces() {
        return pieces;
    }

    public void setPieces(@NotNull final Set<PieceInterface> pieces) {
        this.pieces = pieces;
    }

    @Override
    public Set<Rule> getRuleset() {
        return ruleset;
    }

    public void setRuleset(@NotNull final Set<Rule> ruleset) {
        this.ruleset = ruleset;
    }
    
}
