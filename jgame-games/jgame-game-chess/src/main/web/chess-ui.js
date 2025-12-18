/**
 * Chess UI - Canvas Rendering
 * Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot
 */

class ChessUI {
    constructor(canvasId) {
        this.canvas = document.getElementById(canvasId);
        this.ctx = this.canvas.getContext('2d');
        this.squareSize = 60;
        this.game = new ChessGame();

        this.lightColor = '#f0d9b5';
        this.darkColor = '#b58863';
        this.selectColor = '#7fc97f';

        this.pieces = {
            'K': '♔', 'Q': '♕', 'R': '♖', 'B': '♗', 'N': '♘', 'P': '♙',
            'k': '♚', 'q': '♛', 'r': '♜', 'b': '♝', 'n': '♞', 'p': '♟'
        };

        this.setupCanvas();
        this.setupEvents();
        this.render();
    }

    setupCanvas() {
        this.canvas.width = this.squareSize * 8;
        this.canvas.height = this.squareSize * 8;
    }

    setupEvents() {
        this.canvas.addEventListener('click', (e) => {
            const rect = this.canvas.getBoundingClientRect();
            const col = Math.floor((e.clientX - rect.left) / this.squareSize);
            const row = Math.floor((e.clientY - rect.top) / this.squareSize);
            this.handleClick(row, col);
        });
    }

    handleClick(row, col) {
        const result = this.game.selectSquare(row, col);
        this.render();
        this.updateStatus();

        if (result.moved) {
            // Check for game over conditions
        }
    }

    render() {
        const ctx = this.ctx;

        // Draw board
        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const isLight = (row + col) % 2 === 0;

                // Check if selected
                if (this.game.selectedSquare &&
                    this.game.selectedSquare.row === row &&
                    this.game.selectedSquare.col === col) {
                    ctx.fillStyle = this.selectColor;
                } else {
                    ctx.fillStyle = isLight ? this.lightColor : this.darkColor;
                }

                ctx.fillRect(
                    col * this.squareSize,
                    row * this.squareSize,
                    this.squareSize,
                    this.squareSize
                );
            }
        }

        // Draw pieces
        ctx.font = '42px serif';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';

        for (let row = 0; row < 8; row++) {
            for (let col = 0; col < 8; col++) {
                const piece = this.game.getPiece(row, col);
                if (piece) {
                    ctx.fillStyle = '#000';
                    ctx.fillText(
                        this.pieces[piece],
                        col * this.squareSize + this.squareSize / 2,
                        row * this.squareSize + this.squareSize / 2
                    );
                }
            }
        }

        // Draw coordinates
        ctx.font = '12px sans-serif';
        ctx.fillStyle = '#666';
        for (let i = 0; i < 8; i++) {
            // Files
            ctx.fillText(
                String.fromCharCode(97 + i),
                i * this.squareSize + this.squareSize / 2,
                8 * this.squareSize - 4
            );
            // Ranks
            ctx.textAlign = 'left';
            ctx.fillText(
                String(8 - i),
                4,
                i * this.squareSize + this.squareSize / 2
            );
            ctx.textAlign = 'center';
        }
    }

    updateStatus() {
        const statusEl = document.getElementById('status');
        if (statusEl) {
            statusEl.textContent = this.game.getStatus();
        }
    }

    newGame() {
        this.game.reset();
        this.render();
        this.updateStatus();
    }
}

// Initialize when loaded
document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('chessBoard')) {
        window.chessUI = new ChessUI('chessBoard');
    }
});
