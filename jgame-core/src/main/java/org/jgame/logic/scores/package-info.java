/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot, Google Gemini (Antigravity)
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
 */
/**
 * Scoring system implementations for games.
 * 
 * <p>
 * This package provides various scoring system implementations that can be used
 * to track player performance in games. All scoring systems implement
 * {@link org.jgame.logic.ScoreInterface}.
 * </p>
 * 
 * <h2>Available Score Types</h2>
 * <ul>
 * <li>{@link org.jgame.logic.scores.IntScore} - Integer-based scoring</li>
 * <li>{@link org.jgame.logic.scores.DoubleScore} - Floating-point scoring</li>
 * <li>{@link org.jgame.logic.scores.TimeBasedScore} - Time-based scoring (speed
 * runs)</li>
 * <li>{@link org.jgame.logic.scores.MoveBasedScore} - Move count scoring
 * (efficiency)</li>
 * <li>{@link org.jgame.logic.scores.GradeScore} - Letter grade scoring
 * (A-F)</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * ScoreInterface playerScore = new IntScore(100);
 * playerScore.add(50); // Score is now 150
 * 
 * TimeBasedScore speedScore = new TimeBasedScore();
 * speedScore.startTimer();
 * // ... gameplay ...
 * speedScore.stopTimer();
 * }</pre>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.logic.ScoreInterface
 */
package org.jgame.logic.scores;