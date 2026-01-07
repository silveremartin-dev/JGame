# Chess - Game Documentation

**Module**: `jgame-game-chess`

---

## Overview

Classic Chess implementation supporting two-player gameplay with full rule enforcement, including castling, en passant, and checkmate detection.

---

## Rules

### Objective

Checkmate the opponent's King.

### Pieces

| Piece | Movement |
|-------|----------|
| King | One square in any direction |
| Queen | Any number of squares horizontally, vertically, or diagonally |
| Rook | Any number of squares horizontally or vertically |
| Bishop | Any number of squares diagonally |
| Knight | L-shape: 2 squares in one direction, 1 perpendicular |
| Pawn | Forward 1 (or 2 from start), captures diagonally |

### Special Moves

- **Castling**: King moves 2 squares toward rook, rook moves to other side
- **En Passant**: Pawn captures enemy pawn that just moved 2 squares
- **Promotion**: Pawn reaching 8th rank promotes to any piece

---

## Class Structure

### ChessRules

Main game logic class extending `AbstractGame`.

```java
public class ChessRules extends AbstractGame {
    
    public void initializeGame();
    public boolean isValidMove(String from, String to);
    public void makeMove(String from, String to);
    public boolean isCheck();
    public boolean isCheckmate();
    public boolean isStalemate();
}
```

### Key Methods

| Method | Description |
|--------|-------------|
| `initializeGame()` | Sets up board with pieces in starting positions |
| `isValidMove(from, to)` | Validates move against piece rules and game state |
| `makeMove(from, to)` | Executes move, updates board, switches turn |
| `isCheck()` | Returns true if current player's king is in check |
| `isCheckmate()` | Returns true if current player has no valid moves while in check |
| `getPiece(position)` | Gets piece at board position |

---

## UI Component

### ChessFXPanel

JavaFX panel rendering the chess board and handling user interaction.

```java
public class ChessFXPanel extends BorderPane {
    
    public ChessFXPanel(ChessRules rules);
    
    // CSS Classes for TestFX
    // .chess-board - Main board container
    // .square-{a1-h8} - Individual squares
    // .piece-king, .piece-queen, etc. - Pieces
    // .selected - Currently selected square
    // .valid-move - Highlighted valid moves
}
```

---

## i18n Keys

```properties
game.chess=Chess
game.chess.desc=Classic 2-player strategy
chess.check=Check!
chess.checkmate=Checkmate!
chess.stalemate=Stalemate
chess.draw=Draw
chess.white.turn=White's turn
chess.black.turn=Black's turn
chess.promotion=Choose promotion piece
chess.castling=Castling
```

---

## Plugin Configuration

`plugin.json`:

```json
{
  "id": "chess",
  "name": "Chess",
  "version": "1.0",
  "author": "JGame Team",
  "description": "Classic Chess",
  "minPlayers": 2,
  "maxPlayers": 2,
  "rulesClass": "org.jgame.logic.games.chess.ChessRules",
  "panelClass": "org.jgame.logic.games.chess.ChessFXPanel"
}
```

---

## Example Usage

```java
ChessRules chess = new ChessRules();
chess.addPlayer(new GameUser("White"));
chess.addPlayer(new GameUser("Black"));
chess.startGame();

// White's opening move
chess.makeMove("e2", "e4");

// Black responds
chess.makeMove("e7", "e5");
```
