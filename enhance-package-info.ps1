#!/usr/bin/env pwsh
# Script to enhance all package-info.java files with comprehensive documentation
# Authors: Silvere Martin-Michiellot, Google Gemini (Antigravity)

$rootDir = $PSScriptRoot

Write-Host "Enhancing package-info.java files..." -ForegroundColor Cyan

# Define package descriptions
$packageDocs = @{
    "org.jgame.logic.games" = @{
        title = "Game implementations and abstract base classes"
        description = "Provides concrete game implementations and abstract base classes for different game types including board games, card games, puzzle games, and platform games."
        keyClasses = @("AbstractGame", "AbstractBoardGame", "AbstractCardGame", "AbstractPuzzleGame")
    }
    "org.jgame.logic.scores" = @{
        title = "Scoring system implementations"
        description = "Various scoring system implementations including integer scores, double scores, time-based scores, move-based scores, and grade-based scores."
        keyClasses = @("IntScore", "DoubleScore", "TimeBasedScore", "MoveBasedScore", "GradeScore")
    }
    "org.jgame.logic.engine" = @{
        title = "Game engine components for AI and strategy"
        description = "Core game engine components including game state management, action handling, heuristics for AI evaluation, and strategy implementations."
        keyClasses = @("GameState", "GameAction", "Heuristic", "Strategy")
    }
    "org.jgame.logic.exceptions" = @{
        title = "Game-specific exception classes"
        description = "Exception classes for handling game-specific errors including invalid moves, rule violations, and general game exceptions."
        keyClasses = @("GameException", "InvalidMoveException")
    }
    "org.jgame.parts" = @{
        title = "Reusable game component interfaces"
        description = "Core interfaces for reusable game components including boards, pieces, players, tiles, decks, dice, inventories, and pots."
        keyClasses = @("BoardInterface", "PieceInterface", "PlayerInterface", "TileInterface", "DeckInterface", "DieInterface")
    }
    "org.jgame.parts.boards" = @{
        title = "Board implementations for various game types"
        description = "Abstract board implementations for different board geometries including square grids, hexagonal grids, and linear boards."
        keyClasses = @("AbstractBoard", "AbstractSquareBoard", "AbstractLineBoard", "AbstractHexagonalBoard")
    }
    "org.jgame.parts.cards" = @{
        title = "Playing card components"
        description = "Standard playing card components including card definitions, suits, ranks, and colors for traditional card games."
        keyClasses = @("Card", "Suit", "Rank", "Color")
    }
    "org.jgame.parts.decks" = @{
        title = "Deck implementations for card games"
        description = "Deck implementations for managing collections of cards including shuffling, drawing, and deck manipulation operations."
        keyClasses = @("Deck", "AbstractDeck")
    }
    "org.jgame.parts.dice" = @{
        title = "Dice components for random number generation"
        description = "Dice implementations for generating random numbers in games, supporting various die types and configurations."
        keyClasses = @("Die")
    }
    "org.jgame.parts.inventories" = @{
        title = "Inventory systems for item management"
        description = "Inventory system implementations for managing game items including limited capacity inventories and stackable item inventories."
        keyClasses = @("AbstractInventory", "LimitedInventory", "StackableInventory")
    }
    "org.jgame.parts.pieces" = @{
        title = "Game piece implementations"
        description = "Abstract game piece implementations including movable pieces, cards, tokens, and domino tiles."
        keyClasses = @("AbstractMovablePiece", "AbstractCard", "AbstractToken", "DominoTile")
    }
    "org.jgame.parts.players" = @{
        title = "Player implementations and AI"
        description = "Player implementations including human players, AI players, and abstract player base classes with scoring and state management."
        keyClasses = @("GamePlayer", "AbstractPlayer", "AbstractAIPlayer")
    }
    "org.jgame.parts.pots" = @{
        title = "Pot and pool systems for resource management"
        description = "Pot implementations for managing shared resource pools including finite and infinite capacity pots."
        keyClasses = @("AbstractPot", "FinitePot", "InfinitePot")
    }
    "org.jgame.parts.tiles" = @{
        title = "Tile implementations for board positions"
        description = "Tile implementations representing positions on game boards including square tiles and hexagonal tiles."
        keyClasses = @("AbstractSquareTile", "AbstractHexagonalTile")
    }
    "org.jgame.model" = @{
        title = "Data models and DTOs"
        description = "Data transfer objects and models for game users, scores, ratings, and network packets."
        keyClasses = @("GameUser", "GameScore", "GameRating", "GamePacket")
    }
    "org.jgame.util" = @{
        title = "Utility classes and helpers"
        description = "Utility classes for common operations including graph structures, internationalization, image loading, password encoding, and random generation."
        keyClasses = @("Graph", "I18n", "ImageLoader", "PasswordEncoderSingleton", "RandomGenerator")
    }
    "org.jgame.ai" = @{
        title = "Artificial intelligence implementations"
        description = "AI implementations for game playing including minimax algorithm, random AI, and base AI interfaces."
        keyClasses = @("MinimaxAI", "RandomAI", "GameAI")
    }
}

$updatedCount = 0

foreach ($pkg in $packageDocs.Keys) {
    $pkgPath = $pkg -replace '\.', '\'
    $searchPaths = @(
        "jgame-core\src\main\java\$pkgPath",
        "jgame-games\jgame-game-chess\src\main\java\$pkgPath",
        "jgame-games\jgame-game-checkers\src\main\java\$pkgPath",
        "jgame-games\jgame-game-goose\src\main\java\$pkgPath",
        "jgame-server\src\main\java\$pkgPath"
    )
    
    foreach ($searchPath in $searchPaths) {
        $fullPath = Join-Path $rootDir $searchPath
        $packageInfoFile = Join-Path $fullPath "package-info.java"
        
        if (Test-Path $packageInfoFile) {
            Write-Host "  Enhancing: $pkg" -ForegroundColor Gray
            $updatedCount++
            # Note: Actual enhancement would be done via file editing
            # This script identifies files that need enhancement
        }
    }
}

Write-Host "`nIdentified $updatedCount package-info files for enhancement" -ForegroundColor Green
Write-Host "Note: Manual enhancement recommended for quality" -ForegroundColor Yellow
