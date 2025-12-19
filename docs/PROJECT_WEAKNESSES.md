# JGame Project Weakness Review

**Date**: December 19, 2025
**Version**: 1.0-SNAPSHOT

This document provides a critical analysis of the current JGame codebase, identifying architectural weaknesses, security risks, and areas for improvement.

## 1. Security Weaknesses 🔒

### Critical: Hardcoded Database Credentials

- **File**: `org.jgame.persistence.DatabaseManager`
- **Issue**: Database credentials (`sa` / empty password) are hardcoded in static `final` fields.
- **Risk**: Credentials cannot be changed without recompiling. High security risk if code is exposed.
- **Recommendation**: Load credentials from environment variables or an external `secrets.properties` file that is excluded from version control.

### High: Permissive CORS Configuration

- **File**: `org.jgame.server.JGameServer`
- **Issue**: `config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.allowHost("*")))` allows requests from ANY origin.
- **Risk**: Cross-Site Scripting (XSS) and Cross-Site Request Forgery (CSRF) vulnerabilities.
- **Recommendation**: Restrict CORS to specific trusted domains (e.g., the web client's host).

### Medium: Static JWT Secret (Assumed)

- **Check**: Verify `JwtAuthHandler`. If the secret key is hardcoded or weak, tokens can be forged.
- **Recommendation**: Rotate secrets and load from environment.

## 2. Concurrency & Thread Safety 🧵

### Medium: Race Conditions in LobbyManager

- **File**: `org.jgame.server.lobby.LobbyManager`
- **Issue**: Methods like `joinLobby` perform "check-then-act" sequences on the `lobbies` map without atomic locking.
  - Example: `get(lobbyId)` then `addPlayer`. If the lobby is removed by another thread in between, functionality might break or inconsistent state might occur.
- **Recommendation**: Use `lobbies.compute()` or stronger synchronization when modifying lobby state.

### Low: Static Mutable State

- **File**: `DatabaseManager`
- **Issue**: Relies on static `dataSource` and `initialized` flag.
- **Risk**: difficult to isolate state between parallel tests.
- **Recommendation**: Move to instance-based `DatabaseService` managed by a dependency injection container.

## 3. Architecture & Design 🏗️

### Medium: Tight Coupling (No Dependency Injection)

- **File**: `JGameServer`
- **Issue**: Controllers (`UserApiController`, etc.) and Services are instantiated directly with `new`.
- **Impact**: Hard to unit test components in isolation (mocking dependencies is difficult). Hard to swap implementations.
- **Recommendation**: Introduce a simple Dependency Injection (DI) framework (e.g., Guice, Dagger) or factory pattern.

### Low: God Class Tendencies

- **File**: `JGameServer`
- **Issue**: Acts as HTTP server, DB initializer, Route registrar, and Configuration manager.
- **Recommendation**: Split `RouteConfig`, `ServerConfig`, and `AppLifecycle` into separate classes.

## 4. Error Handling ⚠️

### Medium: Lack of Global Exception Handler

- **File**: `JGameServer`
- **Issue**: No `app.exception(Exception.class, ...)` configured.
- **Impact**: Unhandled exceptions in controllers will likely expose stack traces to the client (security information leak) or return generic 500 errors without useful JSON payloads.
- **Recommendation**: Implement a global exception handler to return standardized JSON error responses (e.g., `{"error": "Internal Server Error", "code": 500}`).

## 5. Configuration ⚙️

### Low: Hardcoded JDBC URL

- **File**: `DatabaseManager`
- **Issue**: `jdbc:h2:./data/jgame` is hardcoded.
- **Impact**: Cannot easily switch to PostgreSQL for production or in-memory for testing without code changes.
- **Recommendation**: Externalize `jdbc.url` to `application.properties`.

## Summary of Action Items

1. **Extract Secrets**: Move DB credentials and JWT secrets to environment variables.
2. **Harden CORS**: Restrict allowed origins.
3. **Refactor DB**: Convert `DatabaseManager` to an injectable service.
4. **Global Error Handling**: Add JSON error responses for all exceptions.
5. **Thread Safety**: Review `LobbyManager` critical sections.
