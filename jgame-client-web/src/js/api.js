/**
 * JGame API Client
 * Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot
 */

class JGameAPI {
    constructor(baseUrl = 'http://localhost:8080') {
        this.baseUrl = baseUrl;
        this.token = localStorage.getItem('jgame_token');
    }

    setToken(token) {
        this.token = token;
        if (token) {
            localStorage.setItem('jgame_token', token);
        } else {
            localStorage.removeItem('jgame_token');
        }
    }

    async request(endpoint, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        try {
            const response = await fetch(`${this.baseUrl}${endpoint}`, {
                ...options,
                headers
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || 'Request failed');
            }

            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    // Auth
    async register(username, password, email = '') {
        return this.request('/api/auth/register', {
            method: 'POST',
            body: JSON.stringify({ username, password, email })
        });
    }

    async login(username, password) {
        const data = await this.request('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
        if (data.token) {
            this.setToken(data.token);
        }
        return data;
    }

    logout() {
        this.setToken(null);
    }

    // Games
    async getGames(search = '', sort = 'name') {
        const params = new URLSearchParams();
        if (search) params.set('q', search);
        if (sort) params.set('sort', sort);
        return this.request(`/api/games?${params}`);
    }

    async getGame(gameId) {
        return this.request(`/api/games/${gameId}`);
    }

    async getGameRatings(gameId) {
        return this.request(`/api/games/${gameId}/ratings`);
    }

    // User
    async getProfile() {
        return this.request('/api/user/profile');
    }

    async getScores() {
        return this.request('/api/user/scores');
    }

    // Ratings
    async createRating(gameId, stars, comment = '') {
        return this.request(`/api/ratings/${gameId}`, {
            method: 'POST',
            body: JSON.stringify({ stars, comment })
        });
    }

    // Leaderboard
    async getLeaderboard(gameId, limit = 10) {
        return this.request(`/api/scores/${gameId}/leaderboard?limit=${limit}`);
    }

    // Sessions (Gameplay)
    async createSession(gameId) {
        return this.request('/api/sessions', {
            method: 'POST',
            body: JSON.stringify({ gameId })
        });
    }

    async getSession(sessionId) {
        return this.request(`/api/sessions/${sessionId}`);
    }

    async executeAction(sessionId, actionType, parameters = {}) {
        return this.request(`/api/sessions/${sessionId}/actions`, {
            method: 'POST',
            body: JSON.stringify({ actionType, parameters })
        });
    }
}

// Global instance
const api = new JGameAPI();
