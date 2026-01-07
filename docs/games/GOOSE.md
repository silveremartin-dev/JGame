# Game of the Goose - Game Documentation

**Module**: `jgame-game-goose`

---

## Overview

Classic Game of the Goose (Jeu de l'Oie) - a dice-based racing game for 2-6 players on a 63-square spiral track.

---

## Rules

### Objective

Be the first to reach square 63 exactly.

### Gameplay

1. Roll two dice
2. Move forward by the total
3. If landing on a special square, follow its rule
4. First to reach 63 exactly wins

### Special Squares

| Square | Type | Effect |
|--------|------|--------|
| 6 | Bridge | Jump to 12 |
| 19 | Inn | Skip one turn |
| 31 | Well | Wait until another player lands here |
| 42 | Maze | Go back to 30 |
| 52 | Prison | Skip two turns |
| 58 | Death | Return to start |
| 5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59 | Goose | Roll again and move the same amount |

### Winning

Must land exactly on 63. Excess squares bounce back.

---

## Class Structure

### GooseRules

Main game logic class.

```java
public class GooseRules extends AbstractGame {
    
    public void initializeGame();
    public void rollDice();
    public void movePlayer(int squares);
    public int getPlayerPosition(GameUser player);
    public boolean isFinished();
}
```

### Key Methods

| Method | Description |
|--------|-------------|
| `initializeGame()` | Sets all players at position 0 |
| `rollDice()` | Rolls 2d6, returns sum |
| `movePlayer(squares)` | Moves current player, handles special tiles |
| `handleSpecialTile(position)` | Applies tile effects |
| `nextTurn()` | Advances to next player |

---

## UI Component

### GooseFXPanel

JavaFX panel rendering the goose board.

```java
public class GooseFXPanel extends BorderPane {
    
    public GooseFXPanel(GooseRules rules);
    
    // CSS Classes
    // .goose-board - Main board
    // .roll-dice-button - Dice button
    // .dice-result - Dice display
    // .current-player - Turn indicator
    // .player-token-{n} - Player tokens
}
```

---

## i18n Keys

```properties
game.goose=Game of the Goose
game.goose.desc=Dice-based racing game
goose.roll.dice=Roll Dice
goose.result=Result: {0}
goose.player.turn={0}'s turn
goose.special.bridge=Bridge! Jump to 12
goose.special.inn=Inn! Skip a turn
goose.special.well=Well! Wait for rescue
goose.special.maze=Maze! Back to 30
goose.special.prison=Prison! Skip 2 turns
goose.special.death=Death! Back to start
goose.special.goose=Goose! Roll again
goose.winner={0} wins!
```

---

## Plugin Configuration

`plugin.json`:

```json
{
  "id": "goose",
  "name": "Game of the Goose",
  "version": "1.0",
  "author": "JGame Team",
  "description": "Classic dice racing",
  "minPlayers": 2,
  "maxPlayers": 6,
  "rulesClass": "org.jgame.logic.games.goose.GooseRules",
  "panelClass": "org.jgame.logic.games.goose.GooseFXPanel"
}
```

---

## Example Usage

```java
GooseRules goose = new GooseRules();
goose.addPlayer(new GameUser("Alice"));
goose.addPlayer(new GameUser("Bob"));
goose.addPlayer(new GameUser("Charlie"));
goose.startGame();

// Roll and move
int roll = goose.rollDice();
goose.movePlayer(roll);
```
