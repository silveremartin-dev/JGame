# JGame - Architecture & Deployment Scenarios

**Version**: 3.0  
**Date**: December 2025  
**Authors**: Google Gemini (Antigravity), Silvere Martin-Michiellot

---

## Current Implementation Status

| Phase | Status | Description |
|-------|--------|-------------|
| 1. Modularization | ✅ Complete | 10 Maven modules |
| 2. Core API | ✅ Complete | Models, plugin system |
| 3. Persistence | ✅ Complete | DAOs, PostgreSQL schema |
| 4. Server | ✅ Complete | Javalin REST API, JWT |
| 5. Java Client | ✅ Complete | JavaFX application |
| 6. Web Client | ✅ Complete | JavaScript client |
| 7. Game Plugins | ✅ Existing | Chess, Checkers, Goose |
| 8. Code Cleanup | ✅ Complete | TODOs, stubs, documentation |
| 9. Localization | ✅ Partial | EN, FR |
| 10. Documentation | ✅ Complete | Updated |

---

## Module Structure

```text
JGame/
├── jgame-core/          # Core API, models, plugin system
│   └── org.jgame.model, logic, plugin, parts, util
├── jgame-persistence/   # Database layer
│   └── org.jgame.persistence.dao (UserDAO, RatingDAO, etc.)
├── jgame-server/        # REST API server
│   └── org.jgame.server.api (JGameServer, controllers)
├── jgame-client-java/   # JavaFX desktop client
│   └── org.jgame.ui.fx (JGameApp, GameApiClient)
├── jgame-client-web/    # JavaScript web client
│   └── src/ (HTML, JS, CSS)
└── jgame-games/         # Game plugins
    ├── jgame-game-chess/
    ├── jgame-game-checkers/
    ├── jgame-game-goose/
    └── jgame-game-solitaire/
```

---

## Deployment Scenarios

### Scenario 1: Pure Library

```bash
mvn install -pl jgame-core
```

Use `jgame-core` as dependency for game logic only.

### Scenario 2: Standalone (Embedded Server)

```bash
cd jgame-client-java
mvn javafx:run
```

JavaFX client with embedded H2 database.

### Scenario 3: Network Mode

```bash
# Server
cd jgame-server
mvn exec:java -Dexec.mainClass="org.jgame.server.JGameServer"

# Client
cd jgame-client-java
mvn javafx:run -Dserver=http://localhost:8080
```

### Scenario 4: Web Client

```bash
cd jgame-client-web
npm install && npm run dev
```

---

## REST API

| Endpoint | Method | Auth | Description |
|----------|--------|------|-------------|
| `/api/auth/register` | POST | ❌ | Register user |
| `/api/auth/login` | POST | ❌ | Login, get JWT |
| `/api/games` | GET | ❌ | List games |
| `/api/games/{id}` | GET | ❌ | Game details |
| `/api/games/{id}/ratings` | GET | ❌ | Game ratings |
| `/api/user/profile` | GET | ✅ | User profile |
| `/api/user/scores` | GET | ✅ | User stats |
| `/api/ratings/{id}` | POST | ✅ | Create rating |
| `/api/scores/{id}/leaderboard` | GET | ✅ | Leaderboard |

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Core Language | Java 21 |
| Build | Maven 3.9+ |
| REST Framework | Javalin 6.5 |
| Authentication | JWT (JJWT 0.12) |
| Desktop UI | JavaFX 21 |
| Database | H2 (dev), PostgreSQL (prod) |
| Connection Pool | HikariCP |
| Password Hash | BCrypt |
| JSON | Gson |
| Logging | Log4j 2 |

---

## Build Commands

```bash
# Full build
mvn clean install -DskipTests

# Run server
cd jgame-server && mvn exec:java

# Run client
cd jgame-client-java && mvn javafx:run

# Run tests
mvn test
```
