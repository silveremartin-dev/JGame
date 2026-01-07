# Checkers - Game Documentation

**Module**: `jgame-game-checkers`

---

## Overview

Classic Checkers (Draughts) implementation supporting two-player gameplay with jumping captures and king promotion.

---

## Rules

### Objective

Capture all opponent pieces or block them from moving.

### Movement

- Regular pieces move diagonally forward one square
- Kings can move diagonally forward or backward
- Captures are mandatory when available
- Multiple jumps in one turn when possible

### Promotion

A piece reaching the opposite end becomes a King.

---

## Class Structure

### CheckersRules

Main game logic class extending `AbstractGame`.

```java
public class CheckersRules extends AbstractGame {
    
    public void initializeGame();
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol);
    public void makeMove(int fromRow, int fromCol, int toRow, int toCol);
    public boolean canCapture();
    public boolean isGameOver();
}
```

### Key Methods

| Method | Description |
|--------|-------------|
| `initializeGame()` | Sets up 8x8 board with 12 pieces per player |
| `isValidMove(...)` | Validates move, enforces mandatory captures |
| `makeMove(...)` | Executes move, handles captures and promotion |
| `canCapture()` | Checks if current player must capture |
| `getWinner()` | Returns winner when game ends |

---

## UI Component

### CheckersFXPanel

JavaFX panel rendering the checkers board.

```java
public class CheckersFXPanel extends BorderPane {
    
    public CheckersFXPanel(CheckersRules rules);
    
    // CSS Classes
    // .checkers-board - Main board
    // .square - Board squares
    // .piece-red, .piece-black - Pieces
    // .piece-king - Crowned pieces
    // .selected - Selected piece
}
```

---

## i18n Keys

```properties
game.checkers=Checkers
game.checkers.desc=Jump and capture
checkers.red.turn=Red's turn
checkers.black.turn=Black's turn
checkers.must.capture=You must capture!
checkers.crowned=Piece crowned!
checkers.winner=Winner: {0}
checkers.draw=Draw
```

---

## Plugin Configuration

`plugin.json`:

```json
{
  "id": "checkers",
  "name": "Checkers",
  "version": "1.0",
  "author": "JGame Team",
  "description": "Classic Checkers",
  "minPlayers": 2,
  "maxPlayers": 2,
  "rulesClass": "org.jgame.logic.games.checkers.CheckersRules",
  "panelClass": "org.jgame.logic.games.checkers.CheckersFXPanel"
}
```

---

## Example Usage

```java
CheckersRules checkers = new CheckersRules();
checkers.addPlayer(new GameUser("Red"));
checkers.addPlayer(new GameUser("Black"));
checkers.startGame();

// Make a move
checkers.makeMove(5, 0, 4, 1);
```
