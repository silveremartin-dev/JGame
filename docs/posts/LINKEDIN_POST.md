# LinkedIn Showcase Post: JGame Platform

## Post Content (Copy & Paste Ready)

🚀 **Comment concevoir et sécuriser une plateforme de jeux multi-joueurs moderne en Java 25 & Architecture distribuée ?**

Récemment, j'ai mené un audit d'ingénierie et une refonte complète de mon projet open-source : **JGame Platform**.

💡 **Le défi** : Concevoir un écosystème robuste, modulaire et hautement performant, capable d'orchestrer des jeux au tour par tour (Échecs, Dames, Jeu de l'Oie, Solitaire) avec synchronisation client-serveur et IA intégrée (Minimax / élagage Alpha-Beta).

🛠️ **Ce que j'ai mis en place :**

🔹 **Architecture & Modularité (Maven 9 modules)** : Découpage strict en couches (Domain Core, Infrastructure Javalin 6, Desktop JavaFX 21, Web JS vanilla) pour un découplage maximal et une extensibilité simplifiée.

🔹 **Sécurité Applicative de bout en bout** :
• Hachage des mots de passe avec **BCrypt (coût 12)** et algorithme de migration transparente des comptes legacy.
• Authentification **JWT (HMAC-SHA256)** avec gestion de session stateful via une `TokenBlacklist` concurrente et endpoint de déconnexion active.
• Protection anti-brute-force par **Rate Limiting** (`HTTP 429`) et validation stricte des entrées RFC 5322.
• Défense en profondeur contre le **XSS** via assainissement HTML (`HtmlSanitizer`) et neutralisation DOM.

🔹 **Gestion de la Concurrence & Performance** :
• Élimination des courses critiques (race conditions) et verrous bloquants dans le gestionnaire de salons (*LobbyManager*) grâce aux structures atomiques `ConcurrentHashMap.compute()`.
• Pool de connexions haute performance **HikariCP** avec mise en cache des requêtes préparées (`cachePrepStmts=true`).

🔹 **Fiabilité & Qualité Logicielle (100% de tests au vert)** :
• Suite de tests automatisés JUnit 5 & TestFX en environnement headless (*Monocle*) avec synchronisation asynchrone déterministe (`CountDownLatch`).
• Couverture de code continue avec JaCoCo et conteneurisation Docker / Docker-Compose.

🔍 **Le code est open-source sur GitHub** :  
👉 https://github.com/silveremartin-dev/JGame

💼 **À propos de moi** :  
Passionné par l'écosystème Java/JVM, les architectures distribuées, la sécurité applicative et le clean code, **je suis actuellement ouvert à de nouvelles opportunités** (CDI / Freelance) en tant que **Lead Dev / Software Engineer Java**.

N'hésitez pas à me contacter par message privé pour échanger sur vos projets ou vos besoins d'ingénierie !

#Java #Java25 #SoftwareEngineering #CleanArchitecture #CyberSecurity #OpenSource #Hiring #JavaFX #BackendDevelopment #FullStack
