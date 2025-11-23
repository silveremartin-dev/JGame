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


package org.jgame.logic;

import org.jetbrains.annotations.NotNull;
import org.jgame.parts.PlayerInterface;

import java.awt.*;
import java.util.Date;
import java.util.Set;

public class Trophy {

    private String name;

    private String description;

    private Image image;

    private Date deliveryDate;

    private Set<PlayerInterface> winners;

    public Trophy(@NotNull String name, String description, Image image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    //null if never set
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    //should be set only once
    public void setDeliveryDate(@NotNull Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    //as a team or as a solo player
    public Set<PlayerInterface> getWinners () {
        return winners;
    }

    //null if never set
    //should be set only once
    //set of players should be immutable : Set<PlayerInterface> players = Collections.unmodifiableSet(players);
    //immutable but still (sadly) not unmodifiable: see https://docs.oracle.com/javase/9/core/creating-immutable-lists-sets-and-maps.htm#JSCOR-GUID-DD066F67-9C9B-444E-A3CB-820503735951
    public void setWinners(@NotNull Set<PlayerInterface> winners) {
        this.winners = winners;
    }

    //preferred over individual setters
    //set of players should be immutable : Set<PlayerInterface> players = Collections.unmodifiableSet(players);
    //immutable but still (sadly) not unmodifiable: see https://docs.oracle.com/javase/9/core/creating-immutable-lists-sets-and-maps.htm#JSCOR-GUID-DD066F67-9C9B-444E-A3CB-820503735951
    public void deliverTrophy(@NotNull Date deliveryDate, @NotNull Set<PlayerInterface> winners) {
        this.deliveryDate = deliveryDate;
        this.winners = winners;
    }
}
