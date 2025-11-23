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

package org.jgame.server;


import org.jetbrains.annotations.NotNull;

/**
 * A data packet sent through the network between the client (your PC) and the server.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

public class GamePacket {

    private GameUser gameUser;

    public GamePacket(@NotNull GameUser gameUser) {
        setGameUser(gameUser);
    }

    public GameUser getGameUser() {
        return gameUser;
    }

    public void setGameUser(@NotNull GameUser gameUser) {
        if (gameUser != null) {
            this.gameUser = gameUser;
        } else throw new IllegalArgumentException("You cannot set a null GameUser.");
    }

}