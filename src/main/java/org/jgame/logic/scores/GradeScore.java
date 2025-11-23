/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */


package org.jgame.logic.scores;

import org.jetbrains.annotations.NotNull;
import org.jgame.logic.ScoreInterface;

import java.util.Map;

public class GradeScore implements ScoreInterface {

    public static String A_PLUS = "A+";
    public static String A = "A";
    public static String A_MINUS = "A-";
    public static String B_PLUS = "B+";
    public static String B = "B";
    public static String B_MINUS = "B-";
    public static String C_PLUS = "C+";
    public static String C = "C";
    public static String C_MINUS = "C-";
    public static String D_PLUS = "D+";
    public static String D = "D";
    public static String D_MINUS = "D-";
    public static String F = "F";

    public static String EXCELLENT = "excellent performance";
    public static String GOOD = "good performance";
    public static String SATISFACTORY = "satisfactory performance";
    public static String LESS_THAN_SATISFACTORY = "less than satisfactory performance";
    public static String UNSATISFACTORY = "unsatisfactory performance";

    private static Map<String, Integer> gradesOrder;

    private String score;

    public GradeScore(String score) {
        gradesOrder.put(F, 0);
        gradesOrder.put(D_MINUS, 1);
        gradesOrder.put(D, 2);
        gradesOrder.put(D_PLUS, 3);
        gradesOrder.put(C_MINUS, 4);
        gradesOrder.put(C, 5);
        gradesOrder.put(C_PLUS, 6);
        gradesOrder.put(B_MINUS, 7);
        gradesOrder.put(B, 8);
        gradesOrder.put(B_PLUS, 9);
        gradesOrder.put(A_MINUS, 10);
        gradesOrder.put(A, 11);
        gradesOrder.put(A_PLUS, 12);
        //or change to Map.ofEntries(entry(k1, v1), entry(k2, v2),...) which makes it immutable
        if (gradesOrder.containsKey(score)) {
            this.score = score;
        } else throw new IllegalArgumentException("Not a valid grad score.");
    }

    @Override
    public String getScoreValue() {
        return score;
    }

    public void setScoreValue(String score) {
        if (gradesOrder.containsKey(score)) {
            this.score = score;
        } else throw new IllegalArgumentException("Not a valid grad score.");
    }

    @Override
    public int compareTo(@NotNull Object o) {
        int thisValue;
        int thatValue;
        thisValue = gradesOrder.get(score);
        thatValue = gradesOrder.get(((GradeScore)o).getScoreValue());
        if (thisValue>thatValue)
            return 1;
        else {
            if (thisValue==thatValue)
                return 0;
            else return -1;
        }
    }
}
