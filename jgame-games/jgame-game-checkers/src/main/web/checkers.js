/**
 * Checkers Game - JavaScript Version
 * Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot
 */

class CheckersGame {
    constructor() {
        this.board = this.createInitialBoard();
        this.currentPlayer = 'red';
        this.selectedSquare = null;
        this.mustJump = false;
    }

    createInitialBoard() {
        const board = Array(8).fill(null).map(() => Array(8).fill(null));

        // Black pieces (top)
        for (let row = 0; row < 3; row++) {
            for (let col = 0; col < 8; col++) {
                if ((row + col) % 2 === 1) {
                    board[row][col] = { color: 'black', king: false };
                }
            }
        }

        // Red pieces (bottom)
        for (let row = 5; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                if ((row + col) % 2 === 1) {
                    board[row][col] = { color: 'red', king: false };
                }
            }
        }

        return board;
    }

    getPiece(row, col) {
        return this.board[row]?.[col] || null;
    }

    isCurrentPlayerPiece(piece) {
        return piece && piece.color === this.currentPlayer;
    }

    selectSquare(row, col) {
        const piece = this.getPiece(row, col);

        if (this.selectedSquare) {
            if (this.isValidMove(this.selectedSquare.row, this.selectedSquare.col, row, col)) {
                this.makeMove(this.selectedSquare.row, this.selectedSquare.col, row, col);
                this.selectedSquare = null;
                return { moved: true };
            } else if (this.isCurrentPlayerPiece(piece)) {
                this.selectedSquare = { row, col };
                return { selected: true };
            }
            this.selectedSquare = null;
            return { deselected: true };
        } else if (this.isCurrentPlayerPiece(piece)) {
            this.selectedSquare = { row, col };
            return { selected: true };
        }
        return {};
    }

    isValidMove(fromRow, fromCol, toRow, toCol) {
        const piece = this.getPiece(fromRow, fromCol);
        if (!piece) return false;

        const target = this.getPiece(toRow, toCol);
        if (target) return false;

        const dRow = toRow - fromRow;
        const dCol = Math.abs(toCol - fromCol);
        const direction = piece.color === 'red' ? -1 : 1;

        // Regular move
        if (dCol === 1 && (dRow === direction || (piece.king && Math.abs(dRow) === 1))) {
            return !this.hasJumps();
        }

        // Jump
        if (dCol === 2 && Math.abs(dRow) === 2) {
            const midRow = fromRow + dRow / 2;
            const midCol = fromCol + (toCol - fromCol) / 2;
            const jumped = this.getPiece(midRow, midCol);
            return jumped && jumped.color !== piece.color;
        }

        return false;
    }

    hasJumps() {
        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const piece = this.getPiece(row, col);
                if (piece && piece.color === this.currentPlayer) {
                    if (this.canJump(row, col)) return true;
                }
            }
        }
        return false;
    }

    canJump(row, col) {
        const piece = this.getPiece(row, col);
        const dirs = piece.king ? [-2, 2] : [piece.color === 'red' ? -2 : 2];

        for (const dRow of dirs) {
            for (const dCol of [-2, 2]) {
                if (this.isValidMove(row, col, row + dRow, col + dCol)) {
                    return true;
                }
            }
        }
        return false;
    }

    makeMove(fromRow, fromCol, toRow, toCol) {
        const piece = this.board[fromRow][fromCol];
        this.board[toRow][toCol] = piece;
        this.board[fromRow][fromCol] = null;

        // Jump - remove captured piece
        if (Math.abs(toCol - fromCol) === 2) {
            const midRow = (fromRow + toRow) / 2;
            const midCol = (fromCol + toCol) / 2;
            this.board[midRow][midCol] = null;

            // Check for more jumps
            if (this.canJump(toRow, toCol)) {
                this.selectedSquare = { row: toRow, col: toCol };
                return;
            }
        }

        // Crown
        if ((piece.color === 'red' && toRow === 0) ||
            (piece.color === 'black' && toRow === 7)) {
            piece.king = true;
        }

        this.currentPlayer = this.currentPlayer === 'red' ? 'black' : 'red';
    }

    getStatus() {
        return `${this.currentPlayer === 'red' ? 'Red' : 'Black'} to move`;
    }

    reset() {
        this.board = this.createInitialBoard();
        this.currentPlayer = 'red';
        this.selectedSquare = null;
    }
}

if (typeof module !== 'undefined') module.exports = CheckersGame;
