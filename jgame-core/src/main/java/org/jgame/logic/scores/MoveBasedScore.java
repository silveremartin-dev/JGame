package org.jgame.logic.scores;

import org.jgame.logic.ScoreInterface;

/**
 * Score based on number of moves.
 */
public class MoveBasedScore implements ScoreInterface {

    private int moves;
    private final boolean golfMode;

    public MoveBasedScore(boolean golfMode) {
        this.moves = 0;
        this.golfMode = golfMode;
    }

    public void incrementMove() {
        moves++;
    }

    @Override
    public Object getScoreValue() {
        if (golfMode) {
            return -moves;
        }
        return moves;
    }

    @Override
    public int compareTo(ScoreInterface o) {
        if (o instanceof MoveBasedScore) {
            Integer thisVal = (Integer) this.getScoreValue();
            Integer otherVal = (Integer) ((MoveBasedScore) o).getScoreValue();
            return thisVal.compareTo(otherVal);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Moves: " + moves;
    }
}
