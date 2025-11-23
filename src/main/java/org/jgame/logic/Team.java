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

package org.jgame.logic;

import org.jetbrains.annotations.NotNull;
import org.jgame.parts.PlayerInterface;

import java.util.HashSet;
import java.util.Set;

public class Team {

    private String name;

    private Set<PlayerInterface> players;

    public Team(@NotNull String name) {
        this.name = name;
        this.players = new HashSet<PlayerInterface>();
    }

    public Team(@NotNull String name, @NotNull Set<PlayerInterface> players) {
        this.name = name;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public Set<PlayerInterface> getPlayers() {
        return players;
    }

    public void setPlayers(@NotNull Set<PlayerInterface> players) {
        this.players = players;
    }
}
