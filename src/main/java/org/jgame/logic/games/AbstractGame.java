/*
 * Copyright 2022 Silvere Martin-Michiellot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
