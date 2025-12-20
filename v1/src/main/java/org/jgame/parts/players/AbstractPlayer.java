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

package org.jgame.parts.players;

import org.jetbrains.annotations.NotNull;
import org.jgame.logic.ScoreInterface;
import org.jgame.parts.InventoryInterface;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.TileInterface;

public abstract class AbstractPlayer implements PlayerInterface {

    private String id;

    private int type;

    private int state;

    private TileInterface position;

    private InventoryInterface inventory;

    private ScoreInterface score;

    //or level, usable also for team members
    private int rank;

    //may be we should also implement a role if in a team

    @Override
    public String getId() {
        return id;
    }

    public void setId(@NotNull String name) {
        this.id = id;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TileInterface getPosition() {
        return position;
    }

    public void setPosition(@NotNull TileInterface position) {
        this.position = position;
    }

    public InventoryInterface getInventory() {
        return inventory;
    }

    public void setInventory(@NotNull InventoryInterface inventory) {
        this.inventory = inventory;
    }

    @Override
    public ScoreInterface getScore() {
        return score;
    }

    public void setScore(@NotNull ScoreInterface score) {
        this.score = score;
    }

    public int getRank() { return rank; }

    public void setRank(int rank) { this.rank = rank; }

    //use user interface to convert inputs into actions

}
