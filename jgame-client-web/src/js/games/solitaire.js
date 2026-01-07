/**
 * Solitaire Core for JGame Web
 */
const SolitaireGame = {
    canvas: null,
    state: null,

    init(container) {
        this.canvas = container;
        this.fetchGameState();
    },

    async fetchGameState() {
        try {
            // Check if we have a session ID
            let sessionId = localStorage.getItem('solitaire_session');
            if (!sessionId) {
                const session = await api.createSession('solitaire');
                sessionId = session.id;
                localStorage.setItem('solitaire_session', sessionId);
            }

            this.state = await api.getSession(sessionId);
            this.render();
        } catch (error) {
            console.warn('Failed to fetch game state, using mock:', error);
            this.state = this.getInitialState();
            this.render();
        }
    },

    getInitialState() {
        // Mock state matching the Java GameState record structure
        return {
            gameId: 'solitaire',
            boardState: {
                tableau: [[], [], [], [], [], [], []],
                foundation: [[], [], [], []],
                waste: [],
                deckSize: 52
            },
            availableActions: [
                { actionType: 'DRAW', parameters: {} }
            ]
        };
    },

    render() {
        if (!this.canvas) return;

        const { boardState, availableActions } = this.state;

        this.canvas.innerHTML = `
            <div class="solitaire-board">
                <div class="top-row">
                    <div class="stock-waste">
                        <div class="card-pile stock" onclick="SolitaireGame.handleAction('DRAW')">
                            <div class="card back">${boardState.deckSize}</div>
                        </div>
                        <div class="card-pile waste" style="cursor: grab;" draggable="true" ondragstart="SolitaireGame.dragStart(event, 'waste', 0)">
                            ${this.renderCardStack(boardState.waste, 'waste')}
                        </div>
                    </div>
                    <div class="foundations">
                        ${boardState.foundation.map((f, i) => `
                            <div class="card-pile foundation" 
                                 ondragover="SolitaireGame.allowDrop(event)" 
                                 ondrop="SolitaireGame.drop(event, 'foundation', ${i})"
                                 onclick="SolitaireGame.handleAction('MOVE_TO_FOUNDATION', ${i})">
                                ${this.renderCardStack(f, 'foundation')}
                            </div>
                        `).join('')}
                    </div>
                </div>
                <div class="tableau">
                    ${boardState.tableau.map((t, i) => `
                        <div class="card-pile column" 
                             style="cursor: ${t.length > 0 ? 'grab' : 'default'};"
                             draggable="${t.length > 0}"
                             ondragstart="SolitaireGame.dragStart(event, 'tableau', ${i})"
                             ondragover="SolitaireGame.allowDrop(event)" 
                             ondrop="SolitaireGame.drop(event, 'tableau', ${i})"
                             onclick="SolitaireGame.handleAction('MOVE_TO_TABLEAU', ${i})">
                            ${this.renderCardStack(t, 'tableau')}
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
    },

    renderCardStack(stack, type) {
        if (!stack || stack.length === 0) return '<div class="card-placeholder"></div>';
        const top = stack[stack.length - 1];
        // Only top card is draggable in this simplified View
        return `<div class="card face-up">${top.rank} of ${top.suit}</div>`;
    },

    dragStart(event, sourceType, sourceIndex) {
        event.dataTransfer.setData("sourceType", sourceType);
        event.dataTransfer.setData("sourceIndex", sourceIndex);
        event.target.classList.add('dragging');
    },

    allowDrop(event) {
        event.preventDefault();
    },

    async drop(event, targetType, targetIndex) {
        event.preventDefault();
        const sourceType = event.dataTransfer.getData("sourceType");
        const sourceIndex = parseInt(event.dataTransfer.getData("sourceIndex"));

        console.log(`Drop: ${sourceType}[${sourceIndex}] -> ${targetType}[${targetIndex}]`);

        let actionType = null;
        let params = { to: targetIndex };

        if (sourceType === 'waste') {
            if (targetType === 'foundation') actionType = 'MOVE_WASTE_TO_FOUNDATION';
            else if (targetType === 'tableau') actionType = 'MOVE_WASTE_TO_TABLEAU';
            params.from = 0; // Waste has single logical index
        } else if (sourceType === 'tableau') {
            if (targetType === 'foundation') actionType = 'MOVE_TABLEAU_TO_FOUNDATION';
            else if (targetType === 'tableau') actionType = 'MOVE_TABLEAU_TO_TABLEAU';
            params.from = sourceIndex; // For tableau, sourceIndex is the column index, passed from render
        }

        if (actionType) {
            await this.executeMove(actionType, params);
        }

        // Remove dragging class if it persists
        document.querySelectorAll('.dragging').forEach(el => el.classList.remove('dragging'));
    },

    async executeMove(type, parameters) {
        try {
            const sessionId = localStorage.getItem('solitaire_session');
            const newState = await api.executeAction(sessionId, type, parameters);
            this.state = newState;
            this.render();
        } catch (error) {
            console.error('Move failed:', error);
        }
    },

    async handleAction(type, target = null) {
        const parameters = {};
        if (target !== null) {
            // For clicks, we might need inference or simpler moves. 
            // Keeping original logic structure for compatibility/extension.
            parameters.target = target;
        }
        await this.executeMove(type, parameters);
    }
};

window.SolitaireGame = SolitaireGame;
