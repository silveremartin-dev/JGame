/**
 * Game of the Goose - JavaScript Version
 * Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot
 */

class GooseGame {
    constructor(numPlayers = 2) {
        this.numPlayers = numPlayers;
        this.positions = Array(numPlayers).fill(0);
        this.currentPlayer = 0;
        this.finished = false;
        this.skipTurns = Array(numPlayers).fill(0);

        // Special squares
        this.gooseSquares = [5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59];
        this.bridgeSquare = 6;
        this.innSquare = 19;
        this.wellSquare = 31;
        this.mazeSquare = 42;
        this.prisonSquare = 52;
        this.deathSquare = 58;
        this.finishSquare = 63;
    }

    rollDice() {
        return Math.floor(Math.random() * 6) + 1 + Math.floor(Math.random() * 6) + 1;
    }

    playTurn() {
        if (this.finished) return { finished: true };

        // Check skip turns
        if (this.skipTurns[this.currentPlayer] > 0) {
            this.skipTurns[this.currentPlayer]--;
            const skipped = this.currentPlayer;
            this.nextPlayer();
            return { skipped, player: skipped };
        }

        const roll = this.rollDice();
        let newPos = this.positions[this.currentPlayer] + roll;

        // Bounce back if overshoot
        if (newPos > this.finishSquare) {
            newPos = this.finishSquare - (newPos - this.finishSquare);
        }

        const result = this.handleSquare(newPos);
        this.positions[this.currentPlayer] = result.position;

        if (result.position === this.finishSquare) {
            this.finished = true;
            return { winner: this.currentPlayer, roll, position: result.position };
        }

        const player = this.currentPlayer;
        if (!result.rollAgain) {
            this.nextPlayer();
        }

        return { player, roll, position: result.position, special: result.special, rollAgain: result.rollAgain };
    }

    handleSquare(pos) {
        // Goose - move again
        if (this.gooseSquares.includes(pos)) {
            return { position: pos, special: 'goose', rollAgain: true };
        }

        // Bridge
        if (pos === this.bridgeSquare) {
            return { position: 12, special: 'bridge' };
        }

        // Inn
        if (pos === this.innSquare) {
            this.skipTurns[this.currentPlayer] = 1;
            return { position: pos, special: 'inn' };
        }

        // Well
        if (pos === this.wellSquare) {
            this.skipTurns[this.currentPlayer] = 2;
            return { position: pos, special: 'well' };
        }

        // Maze
        if (pos === this.mazeSquare) {
            return { position: 30, special: 'maze' };
        }

        // Prison
        if (pos === this.prisonSquare) {
            this.skipTurns[this.currentPlayer] = 3;
            return { position: pos, special: 'prison' };
        }

        // Death
        if (pos === this.deathSquare) {
            return { position: 0, special: 'death' };
        }

        return { position: pos };
    }

    nextPlayer() {
        this.currentPlayer = (this.currentPlayer + 1) % this.numPlayers;
    }

    getStatus() {
        if (this.finished) return `Player ${this.currentPlayer + 1} wins!`;
        return `Player ${this.currentPlayer + 1}'s turn`;
    }

    reset() {
        this.positions = Array(this.numPlayers).fill(0);
        this.currentPlayer = 0;
        this.finished = false;
        this.skipTurns = Array(this.numPlayers).fill(0);
    }
}

if (typeof module !== 'undefined') module.exports = GooseGame;
