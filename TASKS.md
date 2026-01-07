# Liste des tâches restantes et suggérées pour JGame

Voici une liste détaillée des tâches potentielles pour continuer le développement du projet, basée sur l'état actuel.

## 1. Intelligence Artificielle (Priorité Haute)

L'interface `GameAI` est définie, mais les implémentations sont vides ou basiques.

- [x] **Implémenter `MinimaxAI`** : Créer une implémentation générique ou spécifique pour les jeux à information parfaite (Échecs, Dames).
- [x] **Implémenter `RandomAI`** : Finaliser l'IA aléatoire pour servir de niveau "Facile".
- [x] **Solitaire Solver** : Créer une IA ou un système de "Hint" (Indice) capable de suggérer le prochain meilleur coup pour le Solitaire.
- [x] **Intégration** : Relier les classes d'IA aux `GamePlayer` de type `ARTIFICIAL`.

## 2. Logique du Jeu Solitaire (Priorité Haute)

La classe `SolitaireRules` est actuellement une version simplifiée.

- [x] **Règles Complètes** : Implémenter toutes les restrictions de mouvement (Roi sur case vide, alternance de couleurs, etc.).
- [x] **Fonctionnalités "Auto-Complete"** : Implémenter la logique pour finir le jeu automatiquement quand c'est gagné.
- [x] **Scoring et Timer** : Connecter l'UI (tests actuels basés sur des stubs) à une vraie logique de score et de temps.

### 3. Web Client (NexusLayer)

- [x] **Affichage Interactif** : Implémenter le rendu des jeux sur le client web (Solitaire OK). Client Java supporte les 4 jeux.
- [x] **Nexus API Integration** : Relier le client aux nouveaux endpoints du serveur pour la persistance des parties.
- [x] **Traduction Complète** : Assurer une couverture i18n à 100% sur le client web (FR, EN, DE, ES, ZH ajoutés).
- [x] **Connexion API** : Vérifier l'intégration complète avec `jgame-server` pour l'authentification et la sauvegarde des scores.
- [x] **Compléter i18n** : Vérifier que tous les messages d'erreur et états dynamiques utilisent bien `i18n.t()`.
- [x] **Découverte Langues** : Charger dynamiquement les langues disponibles depuis `messages.properties` dans le Client Java.

## 4. Améliorations UI/UX (JavaFX) (Priorité Moyenne)

- [x] **Assets Graphiques** : Remplacer les placeholders par de vraies images (Solitaire: Gradients, Chess/Checkers: Images).
- [/] **Animations** : Ajouter des animations de transition pour les déplacements de cartes/pions (Web CSS OK, JavaFX partiel).
- [x] **Feedback Visuel** : Ajouter des sons ou des effets visuels lors des victoires ou erreurs (Overlay Victoire ajouté).

## 5. Tests et Qualité (Priorité Moyenne)

- [x] **Tests Solitaire** : Nettoyer `SolitaireUITest` pour ne plus dépendre de contournements (Refactorisé avec TestFX clickOn).
- [x] **Couverture de Code** : Augmenter la couverture de tests pour `jgame-core` et `jgame-server` (Ajout ClientDiscoveryTest).
- [x] **Tests Client UI** : Créer `ClientUITest` (Mock Server) pour valider l'interface.
- [x] **Mode Hors Ligne** : Permettre de jouer aux jeux locaux sans serveur (Fallback géré).
- [x] **Config Partie Locale** : Ajouter dialogue de configuration Humain/IA pour Échecs et Dames.

## 6. Serveur et Base de Données (Priorité Basse)

- [x] **Persistance Complète** : S'assurer que les parties sauvegardées conservent l'état exact (positions des pièces) via `jgame-persistence`.
- [x] **Sécurité** : Auditer l'API REST pour s'assurer que les tokens JWT sont bien validés partout.

## 7. Documentation

- [x] **Wiki Complet** : Mettre à jour le Wiki ou les fichiers Markdown avec des guides pour ajouter un nouveau jeu (walkthrough.md mis à jour).
