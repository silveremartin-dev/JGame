package org.jgame.logic.scores;

import org.jgame.logic.ScoreInterface;
import org.jetbrains.annotations.NotNull;

public class IntScore implements ScoreInterface {

    private int value;

    public IntScore(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void add(int amount) {
        this.value += amount;
    }

    @Override
    public Object getScoreValue() {
        return value;
    }

    @Override
    public int compareTo(@NotNull ScoreInterface o) {
        if (o instanceof IntScore) {
            return Integer.compare(this.value, ((IntScore) o).value);
        }
        // Fallback or throw exception if mixed types
        if (o.getScoreValue() instanceof Number) {
            return Double.compare(this.value, ((Number) o.getScoreValue()).doubleValue());
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
