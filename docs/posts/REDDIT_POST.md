# Reddit Showcase Post: JGame Platform

## Recommended Subreddits
- r/java
- r/SideProject
- r/programming
- r/JavaFX

---

## Post Title
```
[Showcase/Open Source] I built a full-stack turn-based game platform in Java 25 & Javalin with pluggable AI, BCrypt, and headless TestFX tests
```

---

## Post Body (Markdown)

Hey everyone! 👋

I wanted to share a project I've been refining: **JGame Platform**, an open-source client-server platform for turn-based games (Chess, Checkers, Game of the Goose, Klondike Solitaire) built with modern **Java 25**.

Repo: https://github.com/silveremartin-dev/JGame

### 🎯 Key Architectural Choices:
1. **Multi-Module Maven Design (9 modules)**:
   - `jgame-core`: Pure domain models, board abstractions, card deck algorithms, and i18n.
   - `jgame-server`: Lightweight REST API powered by **Javalin 6**, using **HikariCP** connection pooling over H2/PostgreSQL.
   - `jgame-client-java`: Desktop UI with **JavaFX 21**.
   - `jgame-client-web`: Lightweight vanilla JS / CSS frontend.
   - `jgame-games`: Pluggable game modules with Minimax AI & Alpha-Beta pruning.

2. **Security & Concurrency Hardening**:
   - **BCrypt (log rounds 12)** for password hashing with automatic, transparent on-login migration for legacy accounts.
   - **JWT Auth with Blacklisting**: In-memory concurrent `TokenBlacklist` with active `/api/auth/logout` token revocation.
   - **Rate Limiting & Input Validation**: Sliding-window rate limiter on auth routes (`429 Too Many Requests`) + strict regex sanitization.
   - **XSS Mitigation**: Contextual HTML sanitization on game reviews and chat messages.
   - **Lock-Free Lobby Manager**: Thread-safe atomic operations via `ConcurrentHashMap.compute()` to prevent race conditions when hosts leave or players join simultaneously.

3. **Deterministic UI Testing in CI**:
   - Automated JavaFX tests running headlessly with **TestFX** and **Monocle**.
   - Solved async UI race conditions and event dispatching using `CountDownLatch` and `Platform.runLater()` synchronization.
   - 100% test pass rate across the full reactor build (`mvn clean test`).

I'd love to hear your thoughts, feedback on the architecture, and ideas for additional games or features!

⭐ Check out the repository: https://github.com/silveremartin-dev/JGame
