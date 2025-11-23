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

package org.jgame.parts;

import org.jgame.logic.ActionInterface;
import org.jgame.logic.Gameplay;
import org.jgame.logic.ScoreInterface;

import java.util.List;

//a player is valid only for a duration of a gameplay
public interface PlayerInterface {

    int BIOLOGICAL = 1; //human, animal
    int ARTIFICIAL  = 2; //IA

    int START_STATE = 1;

    int IN_GAME_STATE = 2;

    int END_STATE = 3;

    //left before the end of the game
    int HAS_QUIT_STATE = 4;

    String getId();

    int getType();

    int getState();

    ScoreInterface getScore();

    List<ActionInterface> computeNextActions(Gameplay gameplay);

}
