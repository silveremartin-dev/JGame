package org.jgame.parts;

/**
 * Interface representing a die.
 */
public interface DieInterface {

    /**
     * Rolls the die.
     * 
     * @return the result of the roll
     */
    int roll();

    /**
     * Gets the number of sides.
     * 
     * @return number of sides
     */
    int getSides();
}
