package org.jgame.logic.scores;

import org.jgame.logic.ScoreInterface;

/**
 * Score based on time elapsed.
 */
public class TimeBasedScore implements ScoreInterface {

    private final long startTime;
    private final boolean decreasing;
    private long endTime;

    public TimeBasedScore(boolean decreasing) {
        this.startTime = System.currentTimeMillis();
        this.decreasing = decreasing;
    }

    public void stop() {
        this.endTime = System.currentTimeMillis();
    }

    @Override
    public Object getScoreValue() {
        long current = (endTime > 0) ? endTime : System.currentTimeMillis();
        long elapsedSeconds = (current - startTime) / 1000;

        if (decreasing) {
            return (double) Math.max(0, 10000 - elapsedSeconds);
        } else {
            return (double) elapsedSeconds;
        }
    }

    @Override
    public int compareTo(ScoreInterface o) {
        if (o instanceof TimeBasedScore) {
            Double thisVal = (Double) this.getScoreValue();
            Double otherVal = (Double) ((TimeBasedScore) o).getScoreValue();
            return thisVal.compareTo(otherVal);
        }
        return 0; // Or sensible default
    }

    @Override
    public String toString() {
        return String.format("Time Score: %.0f", (Double) getScoreValue());
    }
}
