package org.jgame.parts.boards;

import org.jgame.parts.BoardInterface;
import org.jgame.util.Graph;

/**
 * Abstract implementation of a hexagonal board.
 */
public abstract class AbstractHexagonalBoard implements BoardInterface {

    private int radius;
    private Graph<? extends org.jgame.parts.TileInterface> graph;

    public AbstractHexagonalBoard(int radius) {
        this.radius = radius;
        this.graph = new Graph<>();
        // Geometry logic for hex grid would go here
    }

    @Override
    public int getType() {
        return BoardInterface.HEX;
    }

    @Override
    public Graph<? extends org.jgame.parts.TileInterface> getAllTiles() {
        return graph;
    }

    public int getRadius() {
        return radius;
    }
}
