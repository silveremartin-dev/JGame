# JGame Testing Guide

**Version**: 1.0  
**Last Updated**: January 2026

---

## Overview

JGame uses JUnit 5 for unit and integration tests, and TestFX for JavaFX UI tests.

---

## Running Tests

### All Tests

```bash
mvn test
```

### Specific Module

```bash
mvn test -pl jgame-core
mvn test -pl jgame-server
mvn test -pl jgame-games/jgame-game-chess
```

### Single Test Class

```bash
mvn test -Dtest=ChessRulesTest
```

### Skip Tests

```bash
mvn install -DskipTests
```

---

## Test Structure

```
src/test/java/
├── org/jgame/
│   ├── ai/                    # AI algorithm tests
│   │   ├── MinimaxAITest.java
│   │   └── RandomAITest.java
│   ├── integration/           # Integration tests
│   │   └── CoreIntegrationTest.java
│   ├── logic/                 # Game logic tests
│   │   ├── TournamentTest.java
│   │   ├── engine/
│   │   │   ├── GameActionTest.java
│   │   │   └── GameStateTest.java
│   │   └── exceptions/
│   ├── model/                 # Model tests
│   │   ├── GameRatingTest.java
│   │   └── GameScoreTest.java
│   └── util/                  # Utility tests
│       ├── I18nTest.java
│       └── ImageLoaderTest.java
```

---

## Test Types

### Unit Tests

Test individual classes in isolation:

```java
@Test
void testPawnMovement() {
    ChessRules game = new ChessRules();
    game.initializeGame();
    
    assertTrue(game.isValidMove("e2", "e4"));
    assertFalse(game.isValidMove("e2", "e5")); // Invalid
}
```

### Integration Tests

Test component interactions:

```java
@Test
void testGameFlow() {
    ChessRules game = new ChessRules();
    game.addPlayer(new GameUser("white"));
    game.addPlayer(new GameUser("black"));
    game.startGame();
    
    game.makeMove("e2", "e4");
    assertEquals("black", game.getCurrentPlayer().getName());
}
```

### UI Tests (TestFX)

Test JavaFX components:

```java
public class ChessUITest extends BaseUITest {
    
    @Override
    protected void setupStage(Stage stage) {
        ChessRules game = new ChessRules();
        ChessFXPanel panel = new ChessFXPanel(game);
        stage.setScene(new Scene(panel, 800, 600));
    }
    
    @Test
    void testBoardRendering() {
        assertNotNull(lookup(".chess-board").query());
    }
    
    @Test
    void testPieceClick() {
        clickOn(".square-e2");
        assertTrue(lookup(".selected").query() != null);
    }
}
```

---

## BaseUITest

All UI tests extend `BaseUITest` which provides:

- JavaFX toolkit initialization
- Stage setup lifecycle
- Helper methods for TestFX

```java
public abstract class BaseUITest extends ApplicationTest {
    
    protected abstract void setupStage(Stage stage) throws Exception;
    
    @Override
    public void start(Stage stage) throws Exception {
        setupStage(stage);
        stage.show();
    }
    
    protected void waitFor(int millis) {
        sleep(millis);
    }
}
```

---

## CSS Selectors for UI Tests

Use CSS style classes for reliable element selection:

| Selector | Component |
|----------|-----------|
| `.chess-board` | Chess board container |
| `.square-{pos}` | Board square (e.g., `.square-e4`) |
| `.piece-{type}` | Chess piece (e.g., `.piece-king`) |
| `.roll-dice-button` | Goose dice button |
| `.current-player` | Turn indicator |

---

## Test Coverage

Target: **>95% coverage** for critical logic

### Generate Coverage Report

```bash
mvn jacoco:report
```

Report location: `target/site/jacoco/index.html`

---

## Writing Good Tests

### Do's

- ✅ Test one thing per test method
- ✅ Use descriptive test names
- ✅ Set up test data in `@BeforeEach`
- ✅ Clean up resources in `@AfterEach`
- ✅ Test edge cases and error conditions

### Don'ts

- ❌ Don't test multiple behaviors in one test
- ❌ Don't depend on test execution order
- ❌ Don't use Thread.sleep() (use TestFX waitFor)
- ❌ Don't hardcode paths or URLs

---

## Mocking

Use Mockito for mocking dependencies:

```java
@Mock
private GameApiClient mockClient;

@BeforeEach
void setup() {
    MockitoAnnotations.openMocks(this);
    when(mockClient.getGames()).thenReturn(List.of(testGame));
}
```

---

## Troubleshooting

### UI Tests Failing

1. Ensure JavaFX is on module path
2. Check for headless mode: `-Dtestfx.headless=true`
3. Verify CSS selectors match actual classes

### Tests Hanging

1. Check for infinite loops
2. Ensure UI events complete
3. Use timeouts: `@Timeout(10)`
