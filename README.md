# JGame Platform

A modern Java game platform supporting multiplayer turn-based games with client-server architecture.

## 🚀 Architecture

Multi-module Maven project:

```text
JGame/
├── jgame-core/          # Core API, model, plugin system
├── jgame-persistence/   # Database access, DAOs
├── jgame-server/        # REST API server (Javalin + JWT)
├── jgame-client-java/   # JavaFX desktop client
├── jgame-client-web/    # JavaScript web client
└── jgame-games/         # Game plugins
    ├── jgame-game-chess/
    ├── jgame-game-checkers/
    ├── jgame-game-goose/
    └── jgame-game-solitaire/
```

## 🎮 Features

- **Multi-module Design**: Clean separation of concerns
- **REST API**: Javalin server with JWT authentication
- **Game Plugin System**: ZIP-based loadable game plugins
- **Cross-platform Clients**: JavaFX and JavaScript
- **Leaderboards**: User scores and ratings
- **AI Support**: Pluggable AI algorithms (Minimax, Random)
- **Localization**: Multi-language support (EN, FR)

## 🛠️ Requirements

- Java 25+
- Maven 3.6+
- PostgreSQL (optional, H2 for development)

## 🔧 Building

```bash
mvn clean install
```

## ▶️ Running

**Server:**

```bash
cd jgame-server
mvn exec:java -Dexec.mainClass="org.jgame.server.JGameServer"
```

**Client:**

```bash
cd jgame-client-java
mvn javafx:run
```

## 📡 REST API

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/register` | POST | Register user |
| `/api/auth/login` | POST | Login, get JWT |
| `/api/games` | GET | List games |
| `/api/games/{id}` | GET | Game details |
| `/api/ratings/{id}` | POST | Rate game |

## 📖 Documentation

Generate Javadoc with frame-based class navigation:

```bash
mvn javadoc:aggregate
```

Output: `javadoc/index.html`

## 📄 License

MIT License - see [LICENSE](LICENSE)

## 👥 Authors

**Google Gemini (Antigravity)** - AI Developer

- Multi-module architecture
- REST API implementation
- JavaFX client
- Database layer
- Documentation
- Code cleanup and quality improvements

**Silvere Martin-Michiellot** - Original Author

- Email: <silvere.martin@gmail.com>
- Initial concept and game implementations

---

**Version**: 1.0-SNAPSHOT | **Java**: 25 | **Status**: Active Development | **Last Updated**: December 2025 | [![JGame CI](https://github.com/silvere-martin/JGame/actions/workflows/maven.yml/badge.svg)](https://github.com/silvere-martin/JGame/actions/workflows/maven.yml)
