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

package org.jgame.parts.boards;

import org.jetbrains.annotations.NotNull;
import org.jgame.parts.BoardInterface;
import org.jgame.parts.tiles.AbstractSquareTile;
import org.jgame.util.Edge;
import org.jgame.util.Graph;
import org.jgame.util.NodeInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractSquareBoard implements BoardInterface {

    private static int type;

    private Graph tiles;

    @Override
    public int getType() {
        return SQUARE;
    }

    @Override
    public Graph getAllTiles() { return tiles;}

    public void setAllTiles(@NotNull Graph tiles) { this.tiles = tiles;}

    public static Graph generateBoard(int size, @NotNull Class<AbstractSquareTile> tileClass) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (size > 0) {
            Graph resultGraph ;
            NodeInterface[] nodeArray;
            Constructor[] constructors;
            AbstractSquareTile tileObject;
            Edge currentEdge;
            resultGraph = new Graph();
            nodeArray = new NodeInterface[size*size];
            constructors = tileClass.getConstructors();
            for (int i = 0; i < (size * size); i++) {
                tileObject = (AbstractSquareTile) constructors[0].newInstance();
                tileObject.setId(i);
                nodeArray[i] = tileObject;
                resultGraph.addNode(tileObject);
            }
            for (int i = 0; i < (size * (size - 1)); i++) {
                currentEdge = new Edge(nodeArray[i], nodeArray[i+size]);
                resultGraph.addEdge(currentEdge);
            }
            int i;
            i= 0;
            while (i < ((size * size) - 1)) {
                currentEdge = new Edge(nodeArray[i], nodeArray[i+1]);
                resultGraph.addEdge(currentEdge);
                if (i%size == 0) i++; else i+=2;
            }
            return resultGraph;
        } else throw new IllegalArgumentException("Board size needs to be strictly positive.");
    }

}
