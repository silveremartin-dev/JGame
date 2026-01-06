package org.jgame.parts.pots;

import org.jgame.parts.PieceInterface;
import java.util.Set;

/**
 * A Pot with a maximum capacity.
 */
public class FinitePot extends AbstractPot {

    private final int capacity;

    public FinitePot(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void setContents(Set<PieceInterface> contents) {
        if (contents.size() > capacity) {
            throw new IllegalArgumentException("Contents exceed pot capacity of " + capacity);
        }
        super.setContents(contents);
    }

    public boolean isFull() {
        return getContents() != null && getContents().size() >= capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}
