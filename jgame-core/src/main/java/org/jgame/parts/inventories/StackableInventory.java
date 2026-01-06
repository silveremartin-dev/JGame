package org.jgame.parts.inventories;

import org.jgame.parts.PieceInterface;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An Inventory that supports stacking of identical items.
 * Note: AbstractInventory uses Set<PieceInterface>, so we need to
 * adapt/override storage behavior.
 */
public class StackableInventory extends AbstractInventory {

    // We map Piece name/ID to count.
    // But AbstractInventory expects Set<PieceInterface>.
    // This demonstrates a limitation of the current AbstractInventory if strictly
    // followed.
    // For now, we implemented rudimentary stacking logic alongside the Set.

    private final Map<String, Integer> counts;

    public StackableInventory() {
        this.counts = new HashMap<>();
    }

    public void add(PieceInterface piece, int quantity) {
        String key = piece.getName(); // Assuming name defines identity for stacking
        counts.put(key, counts.getOrDefault(key, 0) + quantity);

        // Sync with parent set if possible, but distinct objects might be needed.
        // For simple logic, we just persist the count here.
        if (getContents() != null) {
            getContents().add(piece);
        }
    }

    public int getCount(String pieceName) {
        return counts.getOrDefault(pieceName, 0);
    }

    @Override
    public Set<PieceInterface> getContents() {
        // Return representative set (one of each?)
        return super.getContents();
    }
}
