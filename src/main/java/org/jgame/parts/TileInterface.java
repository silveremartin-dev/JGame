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

import org.jgame.util.NodeInterface;

import java.util.List;

public interface TileInterface extends NodeInterface {

    int SQUARE=1;
    int HEX=2;
    int TRIANGULAR=3;

    int getType();

    //must be a unique id within a board :
    //int getId();

    //pieces may be stacked on top of one another on the same Cell
    List<PieceInterface> getContents();

}
