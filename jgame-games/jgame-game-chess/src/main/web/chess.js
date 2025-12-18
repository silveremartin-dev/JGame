/**
 * Chess Game - JavaScript Version
 * Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot
 */

class ChessGame {
    constructor() {
        this.board = this.createInitialBoard();
        this.currentPlayer = 'white';
        this.selectedSquare = null;
        this.moveHistory = [];
        this.gameOver = false;
    }

    createInitialBoard() {
        return [
            ['r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'],
            ['p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'],
            [null, null, null, null, null, null, null, null],
            [null, null, null, null, null, null, null, null],
            [null, null, null, null, null, null, null, null],
            [null, null, null, null, null, null, null, null],
            ['P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'],
            ['R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R']
        ];
    }

    getPiece(row, col) {
        return this.board[row]?.[col] || null;
    }

    isWhitePiece(piece) {
        return piece && piece === piece.toUpperCase();
    }

    isBlackPiece(piece) {
        return piece && piece === piece.toLowerCase();
    }

    isCurrentPlayerPiece(piece) {
        if (!piece) return false;
        return this.currentPlayer === 'white'
            ? this.isWhitePiece(piece)
            : this.isBlackPiece(piece);
    }

    selectSquare(row, col) {
        const piece = this.getPiece(row, col);

        if (this.selectedSquare) {
            // Try to move
            if (this.isValidMove(this.selectedSquare.row, this.selectedSquare.col, row, col)) {
                this.makeMove(this.selectedSquare.row, this.selectedSquare.col, row, col);
                this.selectedSquare = null;
                return { moved: true };
            } else if (this.isCurrentPlayerPiece(piece)) {
                // Select different piece
                this.selectedSquare = { row, col };
                return { selected: true };
            } else {
                this.selectedSquare = null;
                return { deselected: true };
            }
        } else if (this.isCurrentPlayerPiece(piece)) {
            this.selectedSquare = { row, col };
            return { selected: true };
        }
        return {};
    }

    isValidMove(fromRow, fromCol, toRow, toCol) {
        const piece = this.getPiece(fromRow, fromCol);
        const target = this.getPiece(toRow, toCol);

        // Can't capture own piece
        if (target && this.isCurrentPlayerPiece(target)) {
            return false;
        }

        const type = piece.toLowerCase();
        const dRow = toRow - fromRow;
        const dCol = toCol - fromCol;

        switch (type) {
            case 'p': return this.isValidPawnMove(fromRow, fromCol, toRow, toCol, piece);
            case 'r': return this.isValidRookMove(fromRow, fromCol, toRow, toCol);
            case 'n': return this.isValidKnightMove(dRow, dCol);
            case 'b': return this.isValidBishopMove(fromRow, fromCol, toRow, toCol);
            case 'q': return this.isValidQueenMove(fromRow, fromCol, toRow, toCol);
            case 'k': return this.isValidKingMove(dRow, dCol);
            default: return false;
        }
    }

    isValidPawnMove(fromRow, fromCol, toRow, toCol, piece) {
        const isWhite = this.isWhitePiece(piece);
        const direction = isWhite ? -1 : 1;
        const startRow = isWhite ? 6 : 1;
        const dRow = toRow - fromRow;
        const dCol = toCol - fromCol;
        const target = this.getPiece(toRow, toCol);

        // Forward move
        if (dCol === 0 && !target) {
            if (dRow === direction) return true;
            if (fromRow === startRow && dRow === 2 * direction) {
                return !this.getPiece(fromRow + direction, fromCol);
            }
        }
        // Capture
        if (Math.abs(dCol) === 1 && dRow === direction && target) {
            return true;
        }
        return false;
    }

    isValidRookMove(fromRow, fromCol, toRow, toCol) {
        if (fromRow !== toRow && fromCol !== toCol) return false;
        return this.isPathClear(fromRow, fromCol, toRow, toCol);
    }

    isValidKnightMove(dRow, dCol) {
        return (Math.abs(dRow) === 2 && Math.abs(dCol) === 1) ||
            (Math.abs(dRow) === 1 && Math.abs(dCol) === 2);
    }

    isValidBishopMove(fromRow, fromCol, toRow, toCol) {
        if (Math.abs(toRow - fromRow) !== Math.abs(toCol - fromCol)) return false;
        return this.isPathClear(fromRow, fromCol, toRow, toCol);
    }

    isValidQueenMove(fromRow, fromCol, toRow, toCol) {
        return this.isValidRookMove(fromRow, fromCol, toRow, toCol) ||
            this.isValidBishopMove(fromRow, fromCol, toRow, toCol);
    }

    isValidKingMove(dRow, dCol) {
        return Math.abs(dRow) <= 1 && Math.abs(dCol) <= 1;
    }

    isPathClear(fromRow, fromCol, toRow, toCol) {
        const dRow = Math.sign(toRow - fromRow);
        const dCol = Math.sign(toCol - fromCol);
        let row = fromRow + dRow;
        let col = fromCol + dCol;

        while (row !== toRow || col !== toCol) {
            if (this.getPiece(row, col)) return false;
            row += dRow;
            col += dCol;
        }
        return true;
    }

    makeMove(fromRow, fromCol, toRow, toCol) {
        const piece = this.board[fromRow][fromCol];
        const captured = this.board[toRow][toCol];

        this.board[toRow][toCol] = piece;
        this.board[fromRow][fromCol] = null;

        this.moveHistory.push({
            from: { row: fromRow, col: fromCol },
            to: { row: toRow, col: toCol },
            piece,
            captured
        });

        this.currentPlayer = this.currentPlayer === 'white' ? 'black' : 'white';
    }

    getStatus() {
        return `${this.currentPlayer === 'white' ? 'White' : 'Black'} to move`;
    }

    reset() {
        this.board = this.createInitialBoard();
        this.currentPlayer = 'white';
        this.selectedSquare = null;
        this.moveHistory = [];
        this.gameOver = false;
    }
}

// Export for use
if (typeof module !== 'undefined') {
    module.exports = ChessGame;
}
