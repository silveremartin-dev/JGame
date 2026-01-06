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

    // or level, usable also for team members
    private int rank;

    // may be we should also implement a role if in a team

    @Override
    public String getId() {
        return id;
    }

    public void setId(@NotNull String name) {
        this.id = name;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    // use user interface to convert inputs into actions

    @Override
    public String getName() {
        return getId();
    }

}
