-- JGame Database Schema
-- H2/PostgreSQL compatible for persisting game states, users, scores, and ratings

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Games table (game sessions)
CREATE TABLE IF NOT EXISTS games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_type VARCHAR(50) NOT NULL,
    state_json TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_finished BOOLEAN DEFAULT FALSE
);

-- Game Players association (many-to-many)
CREATE TABLE IF NOT EXISTS game_players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    player_number INT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Scores table (per-game session results)
CREATE TABLE IF NOT EXISTS scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    score DOUBLE,
    position INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE
);

-- User Game Stats (aggregated per user per game type)
CREATE TABLE IF NOT EXISTS user_game_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    game_type VARCHAR(50) NOT NULL,
    total_points BIGINT DEFAULT 0,
    games_played INT DEFAULT 0,
    wins INT DEFAULT 0,
    losses INT DEFAULT 0,
    total_time_seconds BIGINT DEFAULT 0,
    last_played TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(user_id, game_type)
);

-- Ratings table (user reviews of games)
CREATE TABLE IF NOT EXISTS ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    game_type VARCHAR(50) NOT NULL,
    stars INT NOT NULL CHECK (stars >= 1 AND stars <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(user_id, game_type)
);

-- Indexes
DROP INDEX IF EXISTS idx_games_type;
CREATE INDEX idx_games_type ON games(game_type);

DROP INDEX IF EXISTS idx_games_finished;
CREATE INDEX idx_games_finished ON games(is_finished);

DROP INDEX IF EXISTS idx_game_players_game;
CREATE INDEX idx_game_players_game ON game_players(game_id);

DROP INDEX IF EXISTS idx_game_players_user;
CREATE INDEX idx_game_players_user ON game_players(user_id);

DROP INDEX IF EXISTS idx_scores_user;
CREATE INDEX idx_scores_user ON scores(user_id);

DROP INDEX IF EXISTS idx_scores_game;
CREATE INDEX idx_scores_game ON scores(game_id);

DROP INDEX IF EXISTS idx_user_game_stats_user;
CREATE INDEX idx_user_game_stats_user ON user_game_stats(user_id);

DROP INDEX IF EXISTS idx_user_game_stats_game;
CREATE INDEX idx_user_game_stats_game ON user_game_stats(game_type);

DROP INDEX IF EXISTS idx_ratings_game;
CREATE INDEX idx_ratings_game ON ratings(game_type);

DROP INDEX IF EXISTS idx_ratings_user;
CREATE INDEX idx_ratings_user ON ratings(user_id);
