package org.jgame.parts.pieces;

import org.jgame.parts.PieceInterface;
import java.awt.Image;

/**
 * Represents a Domino tile with two values.
 */
public class DominoTile implements PieceInterface {

    private final int value1;
    private final int value2;
    private final String name;

    public DominoTile(int value1, int value2) {
        this.value1 = value1;
        this.value2 = value2;
        this.name = "d" + value1 + "-" + value2;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Image getImage(Image image) {
        return image; // Placeholder
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
        return value2;
    }

    public boolean isDouble() {
        return value1 == value2;
    }

    public int getTotalValue() {
        return value1 + value2;
    }

    public void flip() {
        // Dominos can't really flip values in memory logic, but visual orientation
        // might change.
        // For game logic, [1|6] is same as [6|1] usually, but placement matters.
    }
}
