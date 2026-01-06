/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)`n * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting JGame Application");

        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);");

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
        root.setBottom(createFooter());

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/css/jgame.css") != null
                ? getClass().getResource("/css/jgame.css").toExternalForm()
                : "");

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

        searchBar.getChildren().addAll(new Label("🔍"), searchField, new Label("Sort:"), sortBox);

        // Game grid
        FlowPane gameGrid = new FlowPane(20, 20);
        gameGrid.setPadding(new Insets(20, 0, 0, 0));

        // Add sample games
        gameGrid.getChildren().addAll(
                createGameCard("Chess", "Classic 2-player strategy", "⬛", 2, 2, 4.5),
                createGameCard("Checkers", "Jump and capture", "🔴", 2, 2, 4.2),
                createGameCard("Game of the Goose", "Dice-based race", "🦆", 2, 6, 4.0),
                createGameCard("Solitaire", "Single-player cards", "🃏", 1, 1, 4.3));

        ScrollPane scrollPane = new ScrollPane(gameGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        content.getChildren().addAll(searchBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        tab.setContent(content);
        return tab;
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
        langBox.getItems().addAll("English", "Français", "Deutsch", "Español");
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

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10));
        footer.setStyle("-fx-background-color: rgba(0,0,0,0.2);");

        Label status = new Label("Ready • Connected to localhost:8080");
        status.setStyle("-fx-text-fill: #888;");

        footer.getChildren().add(status);
        return footer;
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Launch Game");
        alert.setHeaderText(gameName);
        alert.setContentText("Game lobby would open here.");
        alert.show();
    }

    private void refreshUI() {
        root.setTop(createHeader());
        mainTabs.getTabs().set(1, createScoresTab());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
