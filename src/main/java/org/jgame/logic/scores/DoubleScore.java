/*
 * Copyright 2022 Silvere Martin-Michiellot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jgame.logic.scores;

import org.jetbrains.annotations.NotNull;
import org.jgame.logic.ScoreInterface;

public class DoubleScore implements ScoreInterface {

    private double score;

    public DoubleScore(double score) {
        this.score = score;
    }

    @Override
    public Object getScoreValue() {
        return score;
    }

    public void setScoreValue(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        double thatValue;
        thatValue = (Double)((DoubleScore)o).getScoreValue();
        if (score>thatValue)
            return 1;
        else {
            if (score == thatValue)
                return 0;
            else return -1;
        }
    }
}
