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

import java.time.Instant;

/**
 * Represents a user's rating and review for a game.
 *
 * <p>
 * Users can rate games from 1-5 stars and optionally leave a comment.
 * </p>
 *
 * @param userId    the user's unique identifier
 * @param gameId    the game's unique identifier
 * @param stars     rating from 1 to 5
 * @param comment   optional text review
 * @param createdAt when the rating was created
 * @param updatedAt when the rating was last updated
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record GameRating(
        String userId,
        String gameId,
        int stars,
        String comment,
        Instant createdAt,
        Instant updatedAt) {

    /** Minimum valid star rating */
    public static final int MIN_STARS = 1;
    /** Maximum valid star rating */
    public static final int MAX_STARS = 5;

    /**
     * Compact constructor with validation.
     */
    public GameRating {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank");
        }
        if (gameId == null || gameId.isBlank()) {
            throw new IllegalArgumentException("Game ID cannot be null or blank");
        }
        if (stars < MIN_STARS || stars > MAX_STARS) {
            throw new IllegalArgumentException("Stars must be between " + MIN_STARS + " and " + MAX_STARS);
        }
        if (comment == null) {
            comment = "";
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    /**
     * Creates a new rating with the current timestamp.
     *
     * @param userId  user identifier
     * @param gameId  game identifier
     * @param stars   rating (1-5)
     * @param comment optional comment
     * @return new GameRating
     */
    public static GameRating create(String userId, String gameId, int stars, String comment) {
        Instant now = Instant.now();
        return new GameRating(userId, gameId, stars, comment, now, now);
    }

    /**
     * Returns a new GameRating with an updated star rating.
     *
     * @param newStars new star rating
     * @return updated GameRating
     */
    public GameRating withStars(int newStars) {
        return new GameRating(userId, gameId, newStars, comment, createdAt, Instant.now());
    }

    /**
     * Returns a new GameRating with an updated comment.
     *
     * @param newComment new comment text
     * @return updated GameRating
     */
    public GameRating withComment(String newComment) {
        return new GameRating(userId, gameId, stars, newComment, createdAt, Instant.now());
    }

    /**
     * Checks if this rating has a comment.
     *
     * @return true if comment is not blank
     */
    public boolean hasComment() {
        return comment != null && !comment.isBlank();
    }
}
