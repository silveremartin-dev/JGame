package org.jgame.parts.pieces;

import org.jgame.parts.PieceInterface;
import java.awt.Image;

/**
 * Represents a circular token (like in Checkers, Connect 4, Othello).
 */
public class AbstractToken implements PieceInterface {

    private String colorName;
    private String name;

    public AbstractToken(String name, String colorName) {
        this.name = name;
        this.colorName = colorName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Image getImage(Image image) {
        return image; // Placeholder
    }

    public String getColorName() {
        return colorName;
    }
}
