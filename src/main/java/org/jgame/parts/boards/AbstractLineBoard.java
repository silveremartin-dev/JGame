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


package org.jgame.parts.boards;

import org.jetbrains.annotations.NotNull;
import org.jgame.parts.BoardInterface;
import org.jgame.parts.tiles.AbstractSquareTile;
import org.jgame.util.Edge;
import org.jgame.util.Graph;
import org.jgame.util.NodeInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractLineBoard implements BoardInterface {

    private static int type;

    private Graph tiles;

    public static Graph generateBoard(int length, @NotNull Class<AbstractSquareTile> tileClass) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (length > 0) {
            Graph resultGraph;
            NodeInterface[] nodeArray;
            Constructor[] constructors;
            AbstractSquareTile tileObject;
            Edge currentEdge;
            resultGraph = new Graph();
            nodeArray = new NodeInterface[length];
            constructors = tileClass.getConstructors();
            for (int i = 0; i < length; i++) {
                tileObject = (AbstractSquareTile) constructors[0].newInstance();
                tileObject.setId(i);
                nodeArray[i] = tileObject;
                resultGraph.addNode(tileObject);
            }
            for (int i = 0; i < (length - 1); i++) {
                currentEdge = new Edge(nodeArray[i], nodeArray[i + 1]);
                resultGraph.addEdge(currentEdge);
            }
            return resultGraph;
        } else throw new IllegalArgumentException("Board length needs to be strictly positive.");
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public Graph getAllTiles() {
        return tiles;
    }

    public void setAllTiles(@NotNull Graph tiles) {
        this.tiles = tiles;
    }

}
