/**
 * JGame Web Application
 * Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot
 */

// State
let currentUser = null;
const games = [
    { id: 'chess', name: 'Chess', icon: '♟️', description: 'Classic 2-player strategy', minPlayers: 2, maxPlayers: 2, rating: 4.5 },
    { id: 'checkers', name: 'Checkers', icon: '🔴', description: 'Jump and capture', minPlayers: 2, maxPlayers: 2, rating: 4.2 },
    { id: 'goose', name: 'Game of the Goose', icon: '🦆', description: 'Dice-based racing game', minPlayers: 2, maxPlayers: 6, rating: 4.0 },
    { id: 'solitaire', name: 'Solitaire', icon: '🃏', description: 'Single-player cards', minPlayers: 1, maxPlayers: 1, rating: 4.3 }
];

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    loadGames();
    setupNavigation();
    checkAuth();
});

// Navigation
function setupNavigation() {
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const sectionId = link.getAttribute('href').substring(1);
            showSection(sectionId);

            document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');
        });
    });
}

function showSection(id) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.getElementById(id)?.classList.add('active');
}

// Games
function loadGames() {
    const grid = document.getElementById('gameGrid');
    grid.innerHTML = games.map(game => createGameCard(game)).join('');
}

function createGameCard(game) {
    const players = game.minPlayers === game.maxPlayers
        ? `${game.minPlayers} player${game.minPlayers > 1 ? 's' : ''}`
        : `${game.minPlayers}-${game.maxPlayers} players`;

    return `
        <div class="game-card">
            <div class="game-icon">${game.icon}</div>
            <div class="game-name">${game.name}</div>
            <div class="game-desc">${game.description}</div>
            <div class="game-meta">
                <span>👥 ${players}</span>
                <span class="game-rating">⭐ ${game.rating}</span>
            </div>
            <button class="btn btn-success" onclick="playGame('${game.id}')">Play</button>
        </div>
    `;
}

function playGame(gameId) {
    const game = games.find(g => g.id === gameId);
    alert(`Launching ${game.name}...\n\nGame lobby would open here.`);
}

// Search
document.getElementById('searchInput')?.addEventListener('input', (e) => {
    const query = e.target.value.toLowerCase();
    const filtered = games.filter(g =>
        g.name.toLowerCase().includes(query) ||
        g.description.toLowerCase().includes(query)
    );
    document.getElementById('gameGrid').innerHTML = filtered.map(createGameCard).join('');
});

// Auth
function checkAuth() {
    const token = localStorage.getItem('jgame_token');
    const username = localStorage.getItem('jgame_username');
    if (token && username) {
        currentUser = username;
        updateAuthUI();
    }
}

function updateAuthUI() {
    const authArea = document.getElementById('authArea');
    if (currentUser) {
        authArea.innerHTML = `
            <span style="color: white;">👤 ${currentUser}</span>
            <button class="btn btn-outline" onclick="logout()">Logout</button>
        `;
    } else {
        authArea.innerHTML = `
            <button class="btn btn-primary" onclick="showLogin()">Login</button>
            <button class="btn btn-outline" onclick="showRegister()">Register</button>
        `;
    }
}

function showLogin() {
    document.getElementById('loginModal').classList.add('show');
}

function showRegister() {
    document.getElementById('registerModal').classList.add('show');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('show');
}

async function login() {
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const data = await api.login(username, password);
        currentUser = data.username;
        localStorage.setItem('jgame_username', currentUser);
        updateAuthUI();
        closeModal('loginModal');
    } catch (error) {
        alert('Login failed: ' + error.message);
    }
}

async function register() {
    const username = document.getElementById('regUsername').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;

    try {
        await api.register(username, password, email);
        alert('Registration successful! Please login.');
        closeModal('registerModal');
        showLogin();
    } catch (error) {
        alert('Registration failed: ' + error.message);
    }
}

function logout() {
    api.logout();
    currentUser = null;
    localStorage.removeItem('jgame_username');
    updateAuthUI();
}

// Settings
function saveSettings() {
    const serverUrl = document.getElementById('serverUrl').value;
    const language = document.getElementById('language').value;

    localStorage.setItem('jgame_server', serverUrl);
    localStorage.setItem('jgame_language', language);

    api.baseUrl = serverUrl;
    alert('Settings saved!');
}

// Load saved settings
window.addEventListener('load', () => {
    const savedServer = localStorage.getItem('jgame_server');
    const savedLang = localStorage.getItem('jgame_language');

    if (savedServer) {
        document.getElementById('serverUrl').value = savedServer;
        api.baseUrl = savedServer;
    }
    if (savedLang) {
        document.getElementById('language').value = savedLang;
    }
});
