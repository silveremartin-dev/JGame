package org.jgame.parts.boards;

import org.jgame.parts.BoardInterface;
import org.jgame.parts.PieceInterface;
import org.jgame.parts.TileInterface;
import org.jgame.util.Graph;

import java.util.Collections;
import java.util.List;

public abstract class AbstractBoard implements BoardInterface {

    @Override
    public abstract int getType();

    @Override
    public abstract Graph<? extends TileInterface> getAllTiles();

    @Override
    public List<PieceInterface> getPieces() {
        return Collections.emptyList();
    }
}
