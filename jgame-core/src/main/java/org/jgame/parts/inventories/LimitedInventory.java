package org.jgame.parts.inventories;

import org.jgame.parts.PieceInterface;
import java.util.Set;

/**
 * An Inventory with a slot limit.
 */
public class LimitedInventory extends AbstractInventory {

    private final int maxSlots;

    public LimitedInventory(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    @Override
    public void setContents(Set<PieceInterface> contents) {
        if (contents.size() > maxSlots) {
            throw new IllegalArgumentException("Inventory overfull: " + contents.size() + " > " + maxSlots);
        }
        super.setContents(contents);
    }

    public boolean hasSpace() {
        return getContents() == null || getContents().size() < maxSlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }
}
