# AGENTS.md - JGame Platform Developer Guide for AI Agents

> **Target Audience**: AI Agents (Antigravity, Claude, Copilot, Cursor, etc.) and Automated Assistants collaborating on the **JGame** codebase.
> **Last Updated**: 2026

---

## 1. Project Overview & Architecture

**JGame** is a modern, modular Java 25 game platform supporting turn-based multiplayer and single-player games with a client-server architecture.

### Module Map

```text
JGame/
├── pom.xml                     # Parent POM (Dependency management, Java 25, plugins)
├── jgame-core/                 # Core domain models, game abstractions, security utils, i18n
├── jgame-server/               # Javalin 6 REST API, HikariCP, DAOs, JWT auth, rate limiting
├── jgame-client-java/          # JavaFX 21 desktop client, TestFX UI tests (Monocle headless)
├── jgame-client-web/           # Modern Vanilla JavaScript / CSS web client
├── jgame-games/                # Parent module for game engine plugins
│   ├── jgame-game-chess/       # Chess engine, Minimax AI, JavaFX board
│   ├── jgame-game-checkers/    # Checkers engine, multi-jump validation, AI
│   ├── jgame-game-goose/       # Game of the Goose board game
│   └── jgame-game-solitaire/   # Klondike Solitaire card game
└── docs/                       # Architectural docs, API guide, test plan, game rules
```

### Core Technologies
- **JDK / Java**: Java 25
- **Build Tool**: Maven 3.9+
- **REST Framework**: Javalin 6.x
- **Desktop UI**: JavaFX 21 (with Monocle for headless UI tests)
- **Database / Connection Pool**: H2 (dev/embedded), PostgreSQL (production), HikariCP
- **Security**: JWT (JJWT 0.12+), BCrypt (cost factor 12)
- **JSON**: Google Gson 2.11+
- **Logging**: Log4j 2
- **Testing**: JUnit 5, Mockito, TestFX

---

## 2. Essential Commands for Agents

### Build & Package
```bash
# Full clean build and package
mvn clean install

# Fast build skipping test execution
mvn clean install -DskipTests

# Compile specific module
mvn compile -pl jgame-core
```

### Running Tests
```bash
# Run all tests across all modules
mvn clean test

# Run tests for a specific module
mvn test -pl jgame-core
mvn test -pl jgame-server
mvn test -pl jgame-client-java
mvn test -pl jgame-games/jgame-game-chess

# Run a single test class
mvn test -Dtest=ChessRulesTest

# Run a specific test method
mvn test -Dtest=ChessRulesTest#testValidPawnMove
```

### Running Applications
```bash
# Start REST API Server (default port: 8080)
cd jgame-server
mvn exec:java -Dexec.mainClass="org.jgame.server.JGameServer"

# Start JavaFX Client
cd jgame-client-java
mvn javafx:run

# Connect JavaFX client to specific server URL
mvn javafx:run -Dserver=http://localhost:8080
```

### Javadoc
```bash
# Generate aggregated Javadoc documentation
mvn javadoc:aggregate
```

---

## 3. Key Conventions & Rules for AI Agents

### 3.1 License Header
Every new Java file **must** include the standard MIT license header:

```java
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
```

### 3.2 Code Style
- **Formatting**: 4 spaces per indentation level (never tabs).
- **Line Length**: 100 to 120 characters maximum.
- **Braces**: K&R style (opening brace on the same line).
- **Naming**:
  - Classes / Interfaces / Records / Enums: `PascalCase`
  - Methods / Variables / Fields: `camelCase`
  - Constants / Enum values: `UPPER_SNAKE_CASE`
  - Package names: `lowercase.with.dots` (`org.jgame.*`)

### 3.3 Internationalization (i18n)
- **Never hardcode user-facing strings** in source code.
- Always use `I18n.get("key.name")`.
- When adding or modifying user-facing text, update all 5 resource bundle files in `src/main/resources/i18n/`:
  - `messages.properties` (English - default fallback)
  - `messages_fr.properties` (French)
  - `messages_de.properties` (German)
  - `messages_es.properties` (Spanish)
  - `messages_zh.properties` (Chinese)

### 3.4 Javadoc & Documentation
- Add descriptive Javadoc to all public and protected classes, interfaces, and methods.
- Document parameter tags (`@param`), return values (`@return`), and exceptions (`@throws`).
- Preserve existing docstrings, notes, and architectural comments unless directly refactoring that functionality.

---

## 4. Security & Concurrency Guidelines

When modifying server or core logic, agents must adhere to these strict security invariants:

1. **Password Security**:
   - Always hash passwords using `BCrypt.hashpw(password, BCrypt.gensalt(12))`.
   - Never store or log plain text passwords.
2. **Authentication & Tokens**:
   - JWT tokens use HMAC-SHA256.
   - Ensure logout calls revoke tokens via the thread-safe `TokenBlacklist`.
3. **Input Sanitization & Validation**:
   - Sanitize all user input submitted via REST API or chat with `HtmlSanitizer` to prevent XSS.
   - Validate usernames, emails, and passwords on both client and server sides.
4. **Concurrency & Thread Safety**:
   - Lobby managers and session registries must use atomic operations (e.g. `ConcurrentHashMap.compute()`, `AtomicInteger`, synchronized blocks where necessary).
   - Ensure proper dereferencing of players upon disconnect/leave to prevent memory leaks.

---

## 5. Adding a New Game Plugin

When asked to implement a new game, follow these steps:

1. **Create Submodule**: Under `jgame-games/jgame-game-<name>/` and register it in `jgame-games/pom.xml`.
2. **Extend Core Engine**:
   - Implement `AbstractGame` or the appropriate game interface in `jgame-core`.
   - Implement board state, move validation, win/loss/draw detection, and turn progression.
   - If AI is supported, implement a Minimax/Alpha-Beta search or heuristic evaluator.
3. **Create JavaFX View**: Create the corresponding `GameFXPanel` in the client or UI package.
4. **Manifest & Resources**: Add `plugin.json` manifest and locale keys in the `i18n` bundle files.
5. **Unit Tests**: Write comprehensive test suites covering valid moves, invalid moves, game-over conditions, and edge cases.

---

## 6. Agent Workflow & Quality Checklist

Before completing any task:
1. **Compile & Test**: Run `mvn clean test` (or module tests) to confirm 0 compilation errors and 0 test regressions.
2. **License Headers**: Verify that all created `.java` files have the MIT license header.
3. **No Hardcoded Strings**: Verify all UI messages use `I18n.get(...)`.
4. **Clean Commits / Diffs**: Ensure no temporary test files, logs, or unneeded IDE artifacts are committed.
5. **Commit Message Format**:
   - `Add: <description>` for new features / classes
   - `Fix: <description>` for bug fixes
   - `Update: <description>` for enhancements
   - `Refactor: <description>` for structural improvements
   - `Docs: <description>` for documentation updates
