package org.jgame.parts.tiles;

import org.jgame.parts.PieceInterface;
import org.jgame.parts.TileInterface;
import org.jgame.parts.BoardInterface;

import java.util.List;

/**
 * Represents a hexagonal tile.
 * Uses axial coordinates (q, r) or generic ID.
 */
public abstract class AbstractHexagonalTile implements TileInterface {

    private int id;
    private int q; // Axial column
    private int r; // Axial row
    private List<PieceInterface> contents;

    @Override
    public int getType() {
        return BoardInterface.HEX; // Assuming using shared constants
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    // Derived cube coordinate s
    public int getS() {
        return -q - r;
    }

    @Override
    public List<PieceInterface> getContents() {
        return contents;
    }

    public void setContents(List<PieceInterface> contents) {
        this.contents = contents;
    }
}
