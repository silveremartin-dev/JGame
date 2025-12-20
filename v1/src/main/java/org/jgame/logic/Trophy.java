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
