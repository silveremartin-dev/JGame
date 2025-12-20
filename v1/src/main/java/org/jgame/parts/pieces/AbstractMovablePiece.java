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

package org.jgame.parts.pieces;

import org.jgame.parts.BoardInterface;
import org.jgame.parts.PieceInterface;
import org.jgame.parts.TileInterface;

import java.awt.*;

public abstract class AbstractMovablePiece implements PieceInterface {
    private String name;
    private Image image;
    private TileInterface tile;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Image getImage(Image image) {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public TileInterface getPosition(BoardInterface board) {
        return tile;
    }

    public void setPosition(TileInterface tile) {
        this.tile = tile;
    }

}
