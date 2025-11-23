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

package org.jgame.logic.games.chess;

import org.jgame.logic.AbstractRuleset;

public class ChessRules extends AbstractRuleset {
    //https://github.com/MichaelAiz/Chess-Game/blob/master/src/Pieces/Bishop.java
    //https://sakkpalota.hu/index.php/en/chess/rules#stalemate
    //https://en.wikipedia.org/wiki/Rules_of_chess

    //excerpt:
    // The King may move one square in any direction, so long as no piece is blocking his path.
    // The King may not move to a square:
    // that is occupied by one of his own pieces,
    // where it is checked by an enemy piece
    // adjacent to the enemy King

    //TODO
}
