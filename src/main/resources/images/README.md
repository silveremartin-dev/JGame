# Game Assets - Download Instructions

Since image URLs are access-restricted, assets must be manually downloaded.

## Required

Downloads:

### 1. Checkers Pieces (Kenney Board Game Pack)

**Source**: https://kenney.nl/assets/board-game-pack  
**License**: CC0 (Public Domain)

1. Visit the URL above
2. Click "Download the entire pack"
3. Extract ZIP file
4. Copy these files to `src/main/resources/images/checkers/pieces/`:
   - Find circular checker pieces (red/white and black)
   - Rename to: `red-regular.png`, `red-king.png`, `black-regular.png`, `black-king.png`
   - King pieces should have crown or special marker

### 2. Game of the Goose Board

**Source**: https://commons.wikimedia.org/wiki/File:Game_of_the_Goose.svg  
**License**: CC0

1. Visit Wikimedia Commons
2. Download "Game_of_the_Goose.svg"
3. Use image editor to extract individual square types
4. Save to `src/main/resources/images/goose/board/`:
   - `square-normal.png` - Regular square
   - `square-goose.png` - Goose squares (9, 18, 27, 36, 45, 54)
   - `square-bridge.png` - Square 6
   - `square-inn.png` - Square 19
   - `square-well.png` - Square 31
   - `square-labyrinth.png` - Square 42
   - `square-prison.png` - Square 52
   - `square-death.png` - Square 58

### 3. Dice (from Kenney Pack)

From the same Board Game Pack:
1. Find 6-sided dice faces (numbered 1-6)
2. Copy to `src/main/resources/images/goose/dice/`:
   - `dice-1.png` through `dice-6.png`

### 4. Player Pieces

From Kenney Board Game Pack:
1. Find colored game tokens/pawns (4 different colors)
2. Copy to `src/main/resources/images/goose/pieces/`:
   - `player-1.png` (red)
   - `player-2.png` (blue)
   - `player-3.png` (green)
   - `player-4.png` (yellow)

## Alternative: Generate Placeholders

If you cannot access these resources immediately, the `ImageLoader` class will automatically generate simple placeholder images (gray squares with "?").

For a quick start, you can also use simple colored circles:
- Open any image editor
- Create 64x64 PNG images
- Use solid colors to represent pieces
- Save with the filenames above

## Verification

After adding images, verify with:
```bash
mvn clean compile
```

The `ImageLoader` class will log warnings for any missing images.

---

**All sources use CC0 or Public Domain licenses** - free for commercial use, no attribution required!
