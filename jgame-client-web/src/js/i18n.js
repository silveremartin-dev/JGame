/**
 * Simple i18n manager for JGame Web
 */
const i18n = {
    currentLocale: 'en',
    translations: {},

    async init() {
        this.currentLocale = localStorage.getItem('jgame_language') || 'en';
        await this.loadLocale(this.currentLocale);
        this.translatePage();
    },

    async loadLocale(locale) {
        try {
            const response = await fetch(`i18n/${locale}.json`);
            this.translations = await response.json();
            this.currentLocale = locale;
        } catch (error) {
            console.error(`Failed to load locale ${locale}:`, error);
            // Fallback to embedded basic EN if fetch fails (e.g. local file access)
            if (locale === 'en') {
                this.translations = {
                    "app_name": "JGame Platform",
                    "nav_games": "Games",
                    "nav_scores": "Scores",
                    "nav_settings": "Settings",
                    "auth_login": "Login",
                    "auth_register": "Register",
                    "auth_logout": "Logout",
                    "games_title": "🎲 Available Games",
                    "play": "Play",
                    "players": "players"
                };
            }
        }
    },

    t(key, params = []) {
        let text = this.translations[key] || key;
        params.forEach((p, i) => {
            text = text.replace(`{${i}}`, p);
        });
        return text;
    },

    translatePage() {
        document.querySelectorAll('[data-i18n]').forEach(el => {
            const key = el.getAttribute('data-i18n');
            el.textContent = this.t(key);
        });

        document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
            const key = el.getAttribute('data-i18n-placeholder');
            el.placeholder = this.t(key);
        });

        // Update games list if applicable
        if (typeof loadGames === 'function') {
            loadGames();
        }
    }
};

window.i18n = i18n;
