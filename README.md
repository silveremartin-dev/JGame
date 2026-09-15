# JGame Platform

A modern, robust Java game platform supporting turn-based multiplayer and single-player games with a modular client-server architecture.

[![JGame CI](https://github.com/silvere-martin/JGame/actions/workflows/maven.yml/badge.svg)](https://github.com/silvere-martin/JGame/actions/workflows/maven.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 25](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/projects/jdk/25/)

---

## 🚀 Architecture

JGame is organized as a multi-module Maven project built with Java 25:

```text
JGame/
├── jgame-core/          # Domain models, game abstractions, security utilities, i18n
├── jgame-server/        # REST API server (Javalin 6), HikariCP connection pool, DAOs, JWT auth
├── jgame-client-java/   # JavaFX 21 desktop client with TestFX automated UI tests
├── jgame-client-web/    # Vanilla JavaScript / CSS modern web client
└── jgame-games/         # Game engines and plugins
    ├── jgame-game-chess/       # Chess engine with Minimax AI and JavaFX board
    ├── jgame-game-checkers/    # Checkers engine with multi-jump validation and AI
    ├── jgame-game-goose/       # Game of the Goose board game
    └── jgame-game-solitaire/   # Klondike Solitaire card game
```

---

## 🎮 Features

- **Multi-Module Architecture**: Clean separation between domain logic, presentation, and persistence layers.
- **Enterprise-Grade Security**:
  - **BCrypt Password Hashing** (cost factor 12) with transparent legacy account upgrade on login.
  - **JWT Authentication** (HMAC-SHA256) with in-memory thread-safe `TokenBlacklist` and `/api/auth/logout` endpoint.
  - **Rate Limiting** protecting against brute-force attacks (`HTTP 429 Too Many Requests`).
  - **Input Validation** on emails, usernames, and passwords.
  - **Cross-Site Scripting (XSS) Prevention** via `HtmlSanitizer` and DOM escaping.
- **Robust Multi-User Lobbies**: Concurrency-safe lobby management using atomic `compute()` operations and clean participant dereferencing.
- **Cross-Platform Clients**: Desktop JavaFX UI (with headless Monocle/TestFX test support) and responsive Web client.
- **Leaderboards & Ratings**: Persistent game statistics, ELO/points leaderboards, and user game reviews.
- **Pluggable AI**: Minimax with Alpha-Beta pruning for Chess and Checkers.
- **Internationalization (i18n)**: Multi-language support (English, French, German, Spanish, Chinese).

---

## 🛠️ Requirements

- **Java**: JDK 25 or higher
- **Maven**: 3.9+
- **Database**: H2 (embedded, default) or PostgreSQL

---

## 🔧 Building & Testing

Run the full automated test suite across all 9 modules:

```bash
mvn clean test
```

Build the full platform and package artifacts:

```bash
mvn clean install
```

---

## ▶️ Running the Application

### 1. Start the Server

```bash
cd jgame-server
mvn exec:java -Dexec.mainClass="org.jgame.server.JGameServer"
```

The server starts on port `8080` by default.

### 2. Start the JavaFX Desktop Client

```bash
cd jgame-client-java
mvn javafx:run
```

### 3. Open the Web Client

Serve the `jgame-client-web/src/` or `bin/web/` directory using any HTTP server, or open `index.html` in your browser.

---

## 📡 REST API Reference

| Endpoint | Method | Auth Required | Description |
|---|---|---|---|
| `/api/auth/register` | `POST` | No | Register a new user account |
| `/api/auth/login` | `POST` | No | Authenticate user and obtain JWT token |
| `/api/auth/logout` | `POST` | Yes | Revoke active JWT token |
| `/api/user/profile` | `GET` | Yes | Retrieve current user profile |
| `/api/user/profile` | `PUT` | Yes | Update profile email or password |
| `/api/user/scores` | `GET` | Yes | Get game statistics for current user |
| `/api/games` | `GET` | No | List all available games |
| `/api/games/{gameId}` | `GET` | No | Get game metadata and details |
| `/api/games/{gameId}/ratings` | `GET` | No | Get ratings and comments for a game |
| `/api/ratings/{gameId}` | `POST` | Yes | Submit a game rating (1-5 stars + comment) |
| `/api/ratings/{gameId}` | `PUT` | Yes | Update existing rating |
| `/api/ratings/{gameId}` | `DELETE` | Yes | Delete rating |
| `/api/scores/{gameId}/leaderboard` | `GET` | No | Retrieve leaderboard for game |
| `/health` | `GET` | No | Health check endpoint |

For detailed payloads and examples, see [docs/API_GUIDE.md](docs/API_GUIDE.md).

---

## 📖 Documentation

- [Architecture & Design Guide](docs/ARCHITECTURE.md)
- [REST API Reference Guide](docs/API_GUIDE.md)
- [Testing & Quality Assurance Guide](docs/TESTING_GUIDE.md)
- [Contributing Guidelines](docs/CONTRIBUTING.md)
- [Game Engine Rules & Documentation](docs/games/)

Generate full aggregate Javadoc:

```bash
mvn javadoc:aggregate
```

---

## 📄 License

Distributed under the MIT License. See [LICENSE](LICENSE) for details.

## 👥 Authors & Contributors

- **Silvere Martin-Michiellot** - Original Author (<silvere.martin@gmail.com>)
- **Google Gemini (Antigravity)** - Multi-module architecture, security hardening, full test suite, REST API & JavaFX clients
