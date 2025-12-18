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
 */

package org.jgame.model;

import java.time.Duration;
import java.time.Instant;

/**
 * Represents a user's score for a specific game.
 *
 * <p>
 * Tracks points earned, games played, wins, losses, and total time.
 * </p>
 *
 * @param userId      the user's unique identifier
 * @param gameId      the game's unique identifier
 * @param points      total points earned
 * @param gamesPlayed number of games played
 * @param wins        number of games won
 * @param losses      number of games lost
 * @param totalTime   total time spent playing this game
 * @param lastPlayed  timestamp of last game session
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record GameScore(
        String userId,
        String gameId,
        long points,
        int gamesPlayed,
        int wins,
        int losses,
        Duration totalTime,
        Instant lastPlayed) {

    /**
     * Compact constructor with validation.
     */
    public GameScore {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (gameId == null || gameId.isBlank()) {
            throw new IllegalArgumentException("Game ID cannot be null or blank");
        }
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        if (gamesPlayed < 0 || wins < 0 || losses < 0) {
            throw new IllegalArgumentException("Game counts cannot be negative");
        }
        if (totalTime == null) {
            totalTime = Duration.ZERO;
        }
        if (lastPlayed == null) {
            lastPlayed = Instant.now();
        }
    }

    /**
     * Creates an initial score for a new user-game combination.
     *
     * @param userId user identifier
     * @param gameId game identifier
     * @return new GameScore with zero stats
     */
    public static GameScore initial(String userId, String gameId) {
        return new GameScore(userId, gameId, 0, 0, 0, 0, Duration.ZERO, Instant.now());
    }

    /**
     * Returns a new GameScore after recording a win.
     *
     * @param pointsEarned points earned in this game
     * @param duration     time spent playing
     * @return updated GameScore
     */
    public GameScore recordWin(long pointsEarned, Duration duration) {
        return new GameScore(
                userId, gameId,
                points + pointsEarned,
                gamesPlayed + 1,
                wins + 1,
                losses,
                totalTime.plus(duration),
                Instant.now());
    }

    /**
     * Returns a new GameScore after recording a loss.
     *
     * @param pointsEarned points earned in this game (can be 0)
     * @param duration     time spent playing
     * @return updated GameScore
     */
    public GameScore recordLoss(long pointsEarned, Duration duration) {
        return new GameScore(
                userId, gameId,
                points + pointsEarned,
                gamesPlayed + 1,
                wins,
                losses + 1,
                totalTime.plus(duration),
                Instant.now());
    }

    /**
     * Calculates the win rate as a percentage.
     *
     * @return win rate (0-100)
     */
    public double winRate() {
        if (gamesPlayed == 0)
            return 0.0;
        return (wins * 100.0) / gamesPlayed;
    }
}
