package org.jgame.parts.pots;

import org.jgame.parts.PieceInterface;
import java.util.HashSet;
import java.util.Set;

/**
 * A Pot that conceptually holds infinite resources.
 * In practice, it accepts any amount of items without limit,
 * or acts as a creative source (logic to be implemented by game).
 */
public class InfinitePot extends AbstractPot {

    // PotInterface implies mutable contents.
    // Infinite pot just acts as a black hole storage that never throws 'Full'
    // exception.

    public InfinitePot() {
        super();
        setContents(new HashSet<>());
    }

    @Override
    public void setContents(Set<PieceInterface> contents) {
        // No limit check
        super.setContents(contents);
    }

    public void add(PieceInterface piece) {
        getContents().add(piece);
    }
}
