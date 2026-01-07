# Solitaire (Klondike) - Game Documentation

**Module**: `jgame-game-solitaire`

---

## Overview

Classic Klondike Solitaire - a single-player card game where the goal is to build four foundation piles from Ace to King.

---

## Rules

### Objective

Move all 52 cards to four foundation piles, sorted by suit from Ace to King.

### Setup

- 7 tableau piles with 1-7 cards each (top card face-up)
- Remaining 24 cards in stock pile
- 4 empty foundation piles

### Gameplay

1. **Stock to Waste**: Draw cards from stock to waste
2. **Build Foundations**: Stack Ace → King by suit
3. **Build Tableaus**: Stack King → Ace, alternating colors
4. **Move Stacks**: Move face-up card sequences between tableaus

### Card Movement Rules

| From | To | Rule |
|------|-----|------|
| Waste/Tableau | Foundation | Same suit, ascending (A, 2, 3...) |
| Waste/Tableau | Tableau | Opposite color, descending (K, Q, J...) |
| Waste | Tableau | Valid sequence |

---

## Class Structure

### SolitaireRules

Main game logic class.

```java
public class SolitaireRules extends AbstractGame {
    
    public void initializeGame();
    public void drawToWaste();
    public boolean moveToFoundation(Card card);
    public boolean moveToTableau(Card card, int pileIndex);
    public boolean isWon();
    
    // Getters
    public Stack<Card> getDeck();
    public Stack<Card> getWaste();
    public List<List<Card>> getTableaus();
    public List<Stack<Card>> getFoundations();
}
```

### Key Methods

| Method | Description |
|--------|-------------|
| `initializeGame()` | Shuffles deck, deals to tableaus |
| `drawToWaste()` | Moves card from stock to waste |
| `moveToFoundation(card)` | Attempts to move card to foundation |
| `moveToTableau(card, pile)` | Attempts to move card to tableau |
| `isWon()` | Returns true when all cards in foundations |

---

## UI Component

### SolitaireFXPanel

JavaFX panel rendering the solitaire table.

```java
public class SolitaireFXPanel extends BorderPane {
    
    public SolitaireFXPanel(SolitaireRules rules);
    
    // CSS Classes
    // .stock-pile - Stock deck
    // .waste-pile - Waste pile
    // .foundation-pile - Foundation piles (4)
    // .tableau-pile - Tableau piles (7)
    // .card - Card element
    // .card-face-up, .card-face-down - Card states
    // .new-game-button - New game button
}
```

---

## i18n Keys

```properties
game.solitaire=Solitaire
game.solitaire.desc=Single-player cards
solitaire.new.game=New Game
solitaire.stock=Stock
solitaire.waste=Waste
solitaire.foundation=Foundation
solitaire.tableau=Tableau
solitaire.score=Score: {0}
solitaire.time=Time: {0}
solitaire.moves=Moves: {0}
solitaire.won=Congratulations! You won!
solitaire.hint=Hint
solitaire.undo=Undo
solitaire.no.moves=No valid moves
```

---

## Plugin Configuration

`plugin.json`:

```json
{
  "id": "solitaire",
  "name": "Solitaire",
  "version": "1.0",
  "author": "JGame Team",
  "description": "Klondike Solitaire",
  "minPlayers": 1,
  "maxPlayers": 1,
  "rulesClass": "org.jgame.logic.games.solitaire.SolitaireRules",
  "panelClass": "org.jgame.logic.games.solitaire.SolitaireFXPanel"
}
```

---

## Example Usage

```java
SolitaireRules solitaire = new SolitaireRules();
solitaire.addPlayer(new GameUser("Player"));
solitaire.startGame();

// Draw from stock
solitaire.drawToWaste();

// Get top waste card and try to move
Card card = solitaire.getWaste().peek();
solitaire.moveToFoundation(card);
```
