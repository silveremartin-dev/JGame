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

package org.jgame.parts.tiles;

import org.jgame.parts.PieceInterface;
import org.jgame.parts.TileInterface;

import java.util.List;

public abstract class AbstractSquareTile implements TileInterface {

    private int id;

    private List<PieceInterface> contents;

    @Override
    public int getType() {
        return SQUARE;
    }

    @Override
    public int getId() {
        return id;
    }

    //should be set only once
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public List<PieceInterface> getContents() {
        return contents;
    }

    public void setContents(List<PieceInterface> contents) {
        this.contents = contents;
    }

}
