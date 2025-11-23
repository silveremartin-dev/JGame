-- JGame Database Schema
-- H2 Database implementation for persisting game states, users, and scores

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Games table
CREATE TABLE IF NOT EXISTS games (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_type VARCHAR(50) NOT NULL,  -- 'GOOSE', 'CHECKERS', 'CHESS'
    state_json TEXT,                 -- Serialized game state (Gson)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_finished BOOLEAN DEFAULT FALSE
);

-- Game Players association (many-to-many)
CREATE TABLE IF NOT EXISTS game_players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    player_number INT NOT NULL,      -- 1, 2, 3, 4
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Scores table
CREATE TABLE IF NOT EXISTS scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    score DOUBLE,
    position INT,                     -- Final position (1st, 2nd, etc.)
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE
);

-- Indexes for better performance
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
