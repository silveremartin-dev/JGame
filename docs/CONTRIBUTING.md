# Contributing to JGame

Thank you for your interest in contributing to JGame! This guide will help you get started.

---

## Getting Started

### Prerequisites

- Java 25+
- Maven 3.6+
- Git

### Setup

1. Fork the repository
2. Clone your fork:

   ```bash
   git clone https://github.com/YOUR-USERNAME/JGame.git
   ```

3. Build the project:

   ```bash
   mvn clean install
   ```

---

## Development Workflow

### 1. Create a Branch

```bash
git checkout -b feature/your-feature-name
```

Branch naming conventions:

- `feature/` - New features
- `fix/` - Bug fixes
- `docs/` - Documentation updates
- `refactor/` - Code refactoring

### 2. Make Changes

Follow the code style guidelines below.

### 3. Test Your Changes

```bash
mvn test
```

Ensure all tests pass before committing.

### 4. Commit

```bash
git add .
git commit -m "Add: brief description of change"
```

Commit message prefixes:

- `Add:` - New feature
- `Fix:` - Bug fix
- `Update:` - Update existing feature
- `Remove:` - Remove feature/code
- `Docs:` - Documentation only
- `Refactor:` - Code refactoring

### 5. Push and Create PR

```bash
git push origin feature/your-feature-name
```

Create a Pull Request on GitHub.

---

## Code Style

### Java

- **Indentation**: 4 spaces (no tabs)
- **Line length**: 100 characters max
- **Braces**: K&R style (opening brace on same line)
- **Naming**:
  - Classes: `PascalCase`
  - Methods/variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`

### Example

```java
public class ChessRules extends AbstractGame {
    
    private static final int BOARD_SIZE = 8;
    
    private final Board board;
    
    public ChessRules() {
        this.board = new Board(BOARD_SIZE);
    }
    
    @Override
    public boolean isValidMove(String from, String to) {
        // Implementation
        return true;
    }
}
```

---

## License Headers

All Java files must include the MIT license header:

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

Use the PowerShell script to apply headers:

```powershell
.\uniformize-headers.ps1
```

---

## Javadoc

All public classes and methods must have Javadoc:

```java
/**
 * Validates a move in the chess game.
 *
 * @param from the source position (e.g., "e2")
 * @param to the target position (e.g., "e4")
 * @return {@code true} if the move is valid
 * @throws InvalidMoveException if positions are malformed
 */
public boolean isValidMove(String from, String to) {
    // ...
}
```

---

## Internationalization (i18n)

All user-facing strings must be externalized:

```java
// Don't do this:
label.setText("Game Over");

// Do this:
label.setText(I18n.get("game.over"));
```

Add keys to all 5 locale files:

- `messages.properties` (English - default)
- `messages_fr.properties` (French)
- `messages_de.properties` (German)
- `messages_es.properties` (Spanish)
- `messages_zh.properties` (Chinese)

---

## Adding a New Game

1. Create module: `jgame-games/jgame-game-yourname/`
2. Implement `AbstractGame` for rules
3. Create `YourGameFXPanel` for UI
4. Add i18n files for all locales
5. Create `plugin.json` manifest
6. Add tests

---

## Pull Request Guidelines

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] New code has tests
- [ ] Javadoc is complete
- [ ] License headers are present
- [ ] i18n keys are externalized
- [ ] No hardcoded strings

---

## Questions?

Open an issue on GitHub or contact the maintainers.
