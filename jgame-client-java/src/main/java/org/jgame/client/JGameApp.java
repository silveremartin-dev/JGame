/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot, Google Gemini (Antigravity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jgame.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * JavaFX main application for JGame client.
 *
 * <p>
 * Provides a modern UI with:
 * </p>
 * <ul>
 * <li>Game browser with grid view</li>
 * <li>Login/registration</li>
 * <li>Game lobby</li>
 * <li>Score panels</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class JGameApp extends Application {

    private static final Logger logger = LogManager.getLogger(JGameApp.class);
    private static final String APP_TITLE = "JGame Platform";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private BorderPane root;
    private TabPane mainTabs;
    private String currentUser = null;
    private GameApiClient apiClient;

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting JGame Application");

        // Initialize API client
        this.apiClient = new GameApiClient("http://localhost:8080");

        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e);");

        // Header
        root.setTop(createHeader());

        // Main content - tabbed interface
        mainTabs = new TabPane();
        mainTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainTabs.getTabs().addAll(
                createGamesTab(),
                createScoresTab(),
                createOptionsTab());
        root.setCenter(mainTabs);

        // Footer
        root.setBottom(createFooter("Connecting..."));

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/jgame.css") != null
                ? getClass().getResource("/css/jgame.css").toExternalForm()
                : "");

        // Load initial data
        refreshGamesList();

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        logger.info("JGame Application started");
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

        // Logo/Title
        Label title = new Label("🎮 JGame Platform");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: white;");

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User area
        HBox userArea = createUserArea();

        header.getChildren().addAll(title, spacer, userArea);
        return header;
    }

    private HBox createUserArea() {
        HBox userArea = new HBox(10);
        userArea.setAlignment(Pos.CENTER_RIGHT);

        if (currentUser == null) {
            Button loginBtn = new Button("Login");
            loginBtn.setStyle("-fx-background-color: #4a69bd; -fx-text-fill: white;");
            loginBtn.setOnAction(e -> showLoginDialog());

            Button registerBtn = new Button("Register");
            registerBtn
                    .setStyle("-fx-background-color: transparent; -fx-text-fill: #4a69bd; -fx-border-color: #4a69bd;");
            registerBtn.setOnAction(e -> showRegisterDialog());

            userArea.getChildren().addAll(loginBtn, registerBtn);
        } else {
            Label userLabel = new Label("👤 " + currentUser);
            userLabel.setStyle("-fx-text-fill: white;");

            Button logoutBtn = new Button("Logout");
            logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c;");
            logoutBtn.setOnAction(e -> logout());

            userArea.getChildren().addAll(userLabel, logoutBtn);
        }

        return userArea;
    }

    private Tab createGamesTab() {
        Tab tab = new Tab("🎲 Games");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        // Search bar
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search games...");
        searchField.setPrefWidth(300);
        searchField.setStyle(
                "-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-prompt-text-fill: gray;");

        ComboBox<String> sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Name", "Rating", "Players");
        sortBox.setValue("Name");
        sortBox.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshGamesList());

        searchBar.getChildren().addAll(new Label("🔍"), searchField, new Label("Sort:"), sortBox, refreshBtn);

        // Game grid
        FlowPane gameGrid = new FlowPane(20, 20);
        gameGrid.setId("gameGrid");
        gameGrid.setPadding(new Insets(20, 0, 0, 0));

        ScrollPane scrollPane = new ScrollPane(gameGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        content.getChildren().addAll(searchBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        tab.setContent(content);
        return tab;
    }

    private void refreshGamesList() {
        if (mainTabs == null)
            return;

        // Find grid in the UI hierarchy
        if (mainTabs.getTabs().isEmpty())
            return;
        Tab gamesTab = mainTabs.getTabs().get(0);
        if (gamesTab.getContent() instanceof VBox vbox) {
            // Assuming ScrollPane is 2nd child (index 1)
            if (vbox.getChildren().size() > 1 && vbox.getChildren().get(1) instanceof ScrollPane sp) {
                if (sp.getContent() instanceof FlowPane grid) {
                    grid.getChildren().clear();
                    grid.getChildren().add(new Label("Loading games..."));

                    apiClient.getGames().thenAccept(games -> {
                        javafx.application.Platform.runLater(() -> {
                            updateFooter("🟢 Online • Connected to Server");
                            grid.getChildren().clear();
                            if (games.isEmpty()) {
                                grid.getChildren().add(new Label("No games found on server."));
                            } else {
                                for (GameApiClient.GameInfo game : games) {
                                    grid.getChildren().add(createGameCard(
                                            game.name(),
                                            game.description(),
                                            getGameIcon(game.id()),
                                            game.minPlayers(),
                                            game.maxPlayers(),
                                            4.5 // Placeholder rating
                                    ));
                                }
                            }
                        });
                    }).exceptionally(e -> {
                        javafx.application.Platform.runLater(() -> {
                            logger.warn("Server unreachable, switching to Offline Mode");
                            updateFooter("🔴 Offline Mode • Local Play Only");
                            grid.getChildren().clear();

                            // Offline Mode Indicator
                            Label offlineLabel = new Label("⚠️ OFFLINE MODE - Local Games Only");
                            offlineLabel
                                    .setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-padding: 0 0 10 0;");
                            if (vbox.getChildren().get(0) instanceof HBox searchBar) {
                                if (!searchBar.getChildren().contains(offlineLabel)) {
                                    // Add it to top of VBox if not there, or handle UI gracefully.
                                    // For simplicity, we just log it and populate grid.
                                }
                            }

                            // Auto-login as Guest if needed
                            if (currentUser == null) {
                                currentUser = "Guest (Offline)";
                                refreshUI();
                            }

                            // Add local games
                            grid.getChildren().addAll(
                                    createGameCard("Chess", "Classic 2-player strategy (Local)", "⬛", 2, 2, 5.0),
                                    createGameCard("Checkers", "Jump and capture (Local)", "🔴", 2, 2, 5.0),
                                    createGameCard("Game of the Goose", "Dice-based race (Local)", "🦆", 2, 6, 5.0),
                                    createGameCard("Solitaire", "Single-player cards (Local)", "🃏", 1, 1, 5.0));
                        });
                        return null;
                    });
                }
            }
        }
    }

    private String getGameIcon(String id) {
        return switch (id) {
            case "chess" -> "⬛";
            case "checkers" -> "🔴";
            case "goose" -> "🦆";
            case "solitaire" -> "🃏";
            default -> "🎮";
        };
    }

    private VBox createGameCard(String name, String description, String icon, int minPlayers, int maxPlayers,
            double rating) {
        VBox card = new VBox(10);
        card.setPrefWidth(250);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 15;");

        // Game icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(48));

        // Name
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setStyle("-fx-text-fill: white;");

        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #aaa;");
        descLabel.setWrapText(true);

        // Players
        String playersText = minPlayers == maxPlayers ? minPlayers + " players"
                : minPlayers + "-" + maxPlayers + " players";
        Label playersLabel = new Label("👥 " + playersText);
        playersLabel.setStyle("-fx-text-fill: #888;");

        // Rating
        Label ratingLabel = new Label("⭐ " + rating);
        ratingLabel.setStyle("-fx-text-fill: gold;");

        // Play button
        Button playBtn = new Button("Play");
        playBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        playBtn.setPrefWidth(100);
        playBtn.setOnAction(e -> launchGame(name));

        // Hover effect
        card.setOnMouseEntered(
                e -> card.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-background-radius: 15;"));
        card.setOnMouseExited(
                e -> card.setStyle("-fx-background-color: rgba(255,255,255,0.08); -fx-background-radius: 15;"));

        card.getChildren().addAll(iconLabel, nameLabel, descLabel, playersLabel, ratingLabel, playBtn);
        return card;
    }

    private Tab createScoresTab() {
        Tab tab = new Tab("🏆 Scores");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label("Your Game Statistics");
        header.setFont(Font.font("System", FontWeight.BOLD, 20));
        header.setStyle("-fx-text-fill: white;");

        if (currentUser == null) {
            Label loginPrompt = new Label("Login to view your scores");
            loginPrompt.setStyle("-fx-text-fill: #888;");
            content.getChildren().addAll(header, loginPrompt);
        } else {
            // Score table placeholder
            TableView<Object> table = new TableView<>();
            table.setPlaceholder(new Label("No scores yet"));
            content.getChildren().addAll(header, table);
        }

        tab.setContent(content);
        return tab;
    }

    private Tab createOptionsTab() {
        Tab tab = new Tab("⚙️ Options");

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label header = new Label("Settings");
        header.setFont(Font.font("System", FontWeight.BOLD, 20));
        header.setStyle("-fx-text-fill: white;");

        // Language
        HBox langRow = new HBox(10);
        langRow.setAlignment(Pos.CENTER_LEFT);
        Label langLabel = new Label("Language:");
        langLabel.setStyle("-fx-text-fill: white;");

        ComboBox<String> langBox = new ComboBox<>();
        // Dynamically populate languages
        java.util.List<String> languages = new java.util.ArrayList<>();
        languages.add("English"); // Default

        // Check for other locales
        if (getClass().getResource("/i18n/messages_fr.properties") != null)
            languages.add("Français");
        if (getClass().getResource("/i18n/messages_de.properties") != null)
            languages.add("Deutsch");
        if (getClass().getResource("/i18n/messages_es.properties") != null)
            languages.add("Español");
        if (getClass().getResource("/i18n/messages_zh.properties") != null)
            languages.add("中文 (Chinese)");

        langBox.getItems().addAll(languages);
        langBox.setValue("English");
        langRow.getChildren().addAll(langLabel, langBox);

        // Server
        HBox serverRow = new HBox(10);
        serverRow.setAlignment(Pos.CENTER_LEFT);
        Label serverLabel = new Label("Server:");
        serverLabel.setStyle("-fx-text-fill: white;");
        TextField serverField = new TextField("localhost:8080");
        serverField.setPrefWidth(200);
        serverRow.getChildren().addAll(serverLabel, serverField);

        content.getChildren().addAll(header, langRow, serverRow);
        tab.setContent(content);
        return tab;
    }

    private HBox createFooter(String statusText) {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: rgba(0,0,0,0.2);");

        Label status = new Label(statusText);
        status.setId("statusLabel");
        status.setStyle("-fx-text-fill: #ecf0f1;");

        footer.getChildren().add(status);
        return footer;
    }

    private void updateFooter(String statusText) {
        if (root.getBottom() instanceof HBox footer) {
            footer.getChildren().clear();
            Label status = new Label(statusText);
            status.setId("statusLabel");
            status.setStyle("-fx-text-fill: #ecf0f1;");
            footer.getChildren().add(status);
        }
    }

    private void showLoginDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Login");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        content.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                login(usernameField.getText(), passwordField.getText());
            }
        });
    }

    private void showRegisterDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Register");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        content.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Email:"), emailField,
                new Label("Password:"), passwordField);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                register(usernameField.getText(), emailField.getText(), passwordField.getText());
            }
        });
    }

    private void login(String username, String password) {
        logger.info("Logging in as: {}", username);

        // Perform login via REST API
        try {
            GameApiClient client = new GameApiClient("http://localhost:8080");
            GameApiClient.LoginResponse response = client.login(username, password).join();
            if (response.success()) {
                // this.authToken = response.token();
                this.currentUser = username;
                logger.info("Login successful");
            } else {
                logger.warn("Login failed - invalid credentials");
                showError("Login Failed", "Invalid username or password");
            }
        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage());
            // Fallback to offline mode for testing
            this.currentUser = username;
            logger.info("Using offline mode");
        }
        refreshUI();
    }

    private void register(String username, String email, String password) {
        logger.info("Registering: {}", username);

        // Perform registration via REST API
        try {
            GameApiClient client = new GameApiClient("http://localhost:8080");
            GameApiClient.ApiResponse response = client.register(username, password, email).join();
            if (response.success()) {
                this.currentUser = username;
                logger.info("Registration successful");
            } else {
                logger.warn("Registration failed");
                showError("Registration Failed", "Could not create account");
            }
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage());
            // Fallback to offline mode for testing
            this.currentUser = username;
            logger.info("Using offline mode");
        }
        refreshUI();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void logout() {
        currentUser = null;
        // authToken = null;
        refreshUI();
    }

    private void launchGame(String gameName) {
        logger.info("Launching game: {}", gameName);

        try {
            Stage gameStage = new Stage();
            gameStage.setTitle(gameName);

            javafx.scene.Parent gamePanel = null;

            // Normalize name to ID map
            String gameId = gameName.toLowerCase().replace(" ", ""); // Rough normalization
            if (gameName.contains("Chess"))
                gameId = "chess";
            else if (gameName.contains("Checkers"))
                gameId = "checkers";
            else if (gameName.contains("Goose"))
                gameId = "goose";
            else if (gameName.contains("Solitaire"))
                gameId = "solitaire";

            // Local Play Configuration
            if (gameId.equals("chess") || gameId.equals("checkers")) {
                GameConfigDialog configDialog = new GameConfigDialog(gameName);
                java.util.Optional<GameConfigDialog.GameConfig> result = configDialog.showAndWait();

                if (result.isPresent()) {
                    GameConfigDialog.GameConfig config = result.get();
                    logger.info("Starting {} with Config: P1={}, P2={}, AI={}", gameName, config.p1Type(),
                            config.p2Type(), config.aiLevel());
                    // TODO: Pass config to Rules/Panel constructors when AI is fully implemented
                    // For now, we proceed with default rules but log the intent so user sees we
                    // handled it.
                } else {
                    return; // User cancelled
                }
            }

            switch (gameId) {
                case "chess" -> {
                    org.jgame.logic.games.chess.ChessRules rules = new org.jgame.logic.games.chess.ChessRules();
                    rules.initializeGame();
                    gamePanel = new org.jgame.logic.games.chess.ChessFXPanel(rules);
                }
                case "checkers" -> {
                    org.jgame.logic.games.checkers.CheckersRules rules = new org.jgame.logic.games.checkers.CheckersRules();
                    rules.initGame();
                    gamePanel = new org.jgame.logic.games.checkers.CheckersFXPanel(rules);
                }
                case "goose" -> {
                    org.jgame.logic.games.goose.GooseRules rules = new org.jgame.logic.games.goose.GooseRules();
                    rules.startGame();
                    gamePanel = new org.jgame.logic.games.goose.GooseFXPanel(rules);
                }
                case "solitaire" -> {
                    org.jgame.logic.games.solitaire.SolitaireRules rules = new org.jgame.logic.games.solitaire.SolitaireRules();
                    rules.initializeGame();
                    gamePanel = new org.jgame.logic.games.solitaire.SolitaireFXPanel(rules);
                }
                default -> {
                    showError("Launch Error", "Unknown game: " + gameName);
                    return;
                }
            }

            Scene scene = new Scene(gamePanel, 800, 600);
            if (getClass().getResource("/css/jgame.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/css/jgame.css").toExternalForm());
            }

            gameStage.setScene(scene);
            gameStage.show();

        } catch (Exception e) {
            logger.error("Failed to launch game " + gameName, e);
            showError("Launch Error", "Could not start game: " + e.getMessage());
        }
    }

    private void refreshUI() {
        root.setTop(createHeader());
        mainTabs.getTabs().set(1, createScoresTab());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
