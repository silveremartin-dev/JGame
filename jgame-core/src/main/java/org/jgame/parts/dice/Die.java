package org.jgame.parts.dice;

import java.util.Random;
import org.jgame.parts.DieInterface;

/**
 * Represents a standard die implementation.
 */
public class Die implements DieInterface {

    /**
     * A static instance of a 6-sided die, as requested.
     */
    public static final Die D6 = new Die(6);

    private final int sides;
    private final Random random;

    public Die(int sides) {
        if (sides < 2) {
            throw new IllegalArgumentException("A die must have at least 2 sides");
        }
        this.sides = sides;
        this.random = new Random();
    }

    @Override
    public int roll() {
        return random.nextInt(sides) + 1;
    }

    @Override
    public int getSides() {
        return sides;
    }
}
