# JGame - Architecture & Deployment Scenarios

**Version**: 2.0  
**Date**: November 23, 2025

---

## Overview

JGame supports **4 deployment scenarios** to maximize flexibility:

1. **Pure Library** - For game developers
2. **Standalone Mode** - Single-player with background server
3. **Network Mode** - Multi-user across web
4. **App Mode** - Custom branded application

---

## Scenario 1: Pure Library

### Description
Use JGame as a **pure Java library** to build your own games without UI.

### Use Cases
- Embed games in existing applications
- Custom game engines
- Headless game processing
- Testing & AI development

### What You Get
- `jgame-core.jar` - Game logic only
- `jgame-persistence.jar` - Database layer (optional)

### Dependencies
```xml
<dependency>
    <groupId>org.jgame</groupId>
    <artifactId>jgame-core</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Example Usage
```java
// Import game rules
import org.jgame.logic.games.chess.ChessRules;
import org.jgame.logic.games.goose.GooseRules;

// Create game instance
GooseRules game = new GooseRules();

// Add players
game.addPlayer(new GameUser("Alice"));
game.addPlayer(new GameUser("Bob"));

// Play programmatically
while (!game.isFinished()) {
    game.nextTurn();
}

// Get winner
GameUser winner = game.getWinner();
```

### Build Configuration
```bash
# Build only core library
mvn clean package -P core-only

# Output: target/jgame-core-2.0.0.jar
```

---

## Scenario 2: Standalone Mode

### Description
**Client with embedded local server** for single-player or AI games.

### Use Cases
- Solitaire games
- Play against AI
- Offline gaming
- Practice mode

### Architecture
```
┌─────────────────────────────┐
│   JGame Client (Swing UI)   │
│                              │
│  ┌────────────────────────┐ │
│  │  Embedded GameServer   │ │
│  │  (localhost:0)         │ │
│  │                        │ │
│  │  ┌──────────────────┐ │ │
│  │  │  Game Plugins    │ │ │
│  │  │  AI Players      │ │ │
│  │  │  H2 Database     │ │ │
│  │  └──────────────────┘ │ │
│  └────────────────────────┘ │
└─────────────────────────────┘
```

### Features
- No network required
- Instant start
- Full game persistence
- AI opponents
- Save/load games

### Launch
```bash
# Standalone executable JAR
java -jar jgame-standalone.jar

# Or with specific game
java -jar jgame-standalone.jar --game=chess --ai=medium
```

### Implementation
```java
public class StandaloneGameClient extends GameClient {
    private GameServer embeddedServer;
    
    public StandaloneGameClient() {
        // Start embedded server on random port
        embeddedServer = new GameServer(0); // port 0 = auto
        embeddedServer.start();
        
        // Connect to localhost
        String serverAddress = "localhost:" + embeddedServer.getPort();
        this.connect(serverAddress);
    }
}
```

### Build Configuration
```bash
# Build standalone JAR with embedded server
mvn clean package -P standalone

# Output: target/jgame-standalone-2.0.0.jar (fatjar)
```

---

## Scenario 3: Network Mode

### Description
**Server-client architecture** with plugin distribution and multi-user support.

### Use Cases
- Online multiplayer
- Tournament hosting
- Plugin marketplace
- Web-based gaming

### Architecture
```
┌──────────────────────────────────────┐
│        GameServer (Hosting)          │
│                                       │
│  ├─ Game Plugin Registry             │
│  ├─ Plugin Distribution (HTTP)       │
│  ├─ User Authentication              │
│  ├─ Match Making                     │
│  ├─ Game State Broadcast             │
│  └─ PostgreSQL/MySQL Database        │
└──────────────────────────────────────┘
            │
            ├─────────────┬─────────────┐
            │             │             │
    ┌───────▼──────┐ ┌───▼──────┐ ┌───▼──────┐
    │ Java Client  │ │ JS Client│ │ JS Client│
    │ (Swing)      │ │ (React)  │ │ (Vue)    │
    └──────────────┘ └──────────┘ └──────────┘
```

### Server Features
- **Plugin broadcasting**: Send game .jar to clients
- **Multi-user**: Concurrent games
- **Authentication**: User accounts, BCrypt passwords
- **Matchmaking**: Find opponents
- **Leaderboards**: Global rankings
- **Chat**: In-game messaging

### Client Types

#### Java Client (Swing)
```bash
java -jar jgame-client.jar --server=game.example.com:8080
```

#### JavaScript Client (Web)
```html
<!DOCTYPE html>
<html>
<head>
    <title>JGame Web Client</title>
    <script src="jgame-web-client.js"></script>
</head>
<body>
    <div id="game-container"></div>
    <script>
        const client = new JGameWebClient('wss://game.example.com:8080');
        client.connect();
    </script>
</body>
</html>
```

### Plugin Distribution Protocol

**Server** → **Client**: Send plugin metadata
```json
{
    "pluginId": "chess",
    "version": "1.0.0",
    "downloadUrl": "https://game.example.com/plugins/chess-1.0.0.jar",
    "checksum": "sha256:abc123..."
}
```

**Client**: Download & verify plugin
```java
PluginDownloader downloader = new PluginDownloader(serverUrl);
File pluginJar = downloader.download("chess", "1.0.0");
GamePlugin plugin = PluginLoader.load(pluginJar);
GamePluginRegistry.getInstance().registerPlugin(plugin);
```

### Build Configuration
```bash
# Build server
mvn clean package -P server
# Output: target/jgame-server-2.0.0.jar

# Build Java client
mvn clean package -P client
# Output: target/jgame-client-2.0.0.jar

# Build web client
cd web-client && npm run build
# Output: dist/jgame-web-client.js
```

---

## Scenario 4: App Mode

### Description
**Branded standalone application** with custom UI hiding all technical details.

### Use Cases
- Commercial game distribution
- Educational apps
- Corporate gaming platforms
- White-label solutions

### Features
- **No server/client concept visible**
- **Custom branding** (logo, colors, name)
- **Single-click install**
- **Auto-updates**
- **Native feel** (no "JGame" branding)

### Architecture
```
┌────────────────────────────────────┐
│     MyChessGame (Branded App)      │
│                                     │
│  ┌──────────────────────────────┐ │
│  │  Custom UI (JavaFX/Swing)    │ │
│  │  - Splash screen             │ │
│  │  - Main menu                 │ │
│  │  - Game board                │ │
│  │  - Settings                  │ │
│  └──────────────────────────────┘ │
│                                     │
│  ┌──────────────────────────────┐ │
│  │  JGame Core (Hidden)         │ │
│  │  - Embedded server           │ │
│  │  - Game logic                │ │
│  │  - Persistence               │ │
│  └──────────────────────────────┘ │
└────────────────────────────────────┘
```

### Configuration (`app.properties`)
```properties
# Branding
app.name=MyChessGame
app.version=1.0.0
app.vendor=MyCompany
app.icon=resources/icon.png

# Games (only specific ones)
games.enabled=chess
games.chess.difficulty=easy,medium,hard

# UI
ui.theme=dark
ui.showTechnicalDetails=false
ui.autoUpdate=true

# Backend (hidden from user)
server.embedded=true
server.port=0
persistence.type=h2
```

### Custom Main Class
```java
public class MyChessGameApp {
    public static void main(String[] args) {
        // Load branding
        AppConfig config = new AppConfig("app.properties");
        
        // Set Look & Feel
        UIManager.setLookAndFeel(new CustomLookAndFeel(config));
        
        // Show splash
        SplashScreen splash = new CustomSplashScreen(config.getIcon());
        splash.show();
        
        // Start embedded components (hidden)
        GameServer embeddedServer = new GameServer(0);
        embeddedServer.start();
        
        // Load ONLY enabled games
        GamePluginRegistry registry = GamePluginRegistry.getInstance();
        registry.registerPlugin(new ChessPlugin()); // Only chess
        
        // Show custom UI (no "JGame" anywhere)
        CustomGameUI ui = new CustomGameUI(config);
        ui.setVisible(true);
        
        splash.close();
    }
}
```

### Build Configuration
```bash
# Build branded app with jpackage
mvn clean package -P app -Dapp.name="MyChessGame"

# Creates native installers:
# Windows: MyChessGame-1.0.0.msi
# macOS:   MyChessGame-1.0.0.dmg
# Linux:   mychessgame-1.0.0.deb
```

---

## Build Matrix

### Maven Profiles

**pom.xml** configuration:
```xml
<profiles>
    <!-- Scenario 1: Pure Library -->
    <profile>
        <id>core-only</id>
        <properties>
            <skipUI>true</skipUI>
            <skipServer>true</skipServer>
        </properties>
    </profile>
    
    <!-- Scenario 2: Standalone -->
    <profile>
        <id>standalone</id>
        <properties>
            <includeServer>true</includeServer>
            <mainClass>org.jgame.client.StandaloneGameClient</mainClass>
        </properties>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-shade-plugin</artifactId>
                    <configuration>
                        <shadedArtifactAttached>true</shadedArtifactAttached>
                        <shadedClassifierName>standalone</shadedClassifierName>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
    
    <!-- Scenario 3: Network (Server) -->
    <profile>
        <id>server</id>
        <properties>
            <mainClass>org.jgame.server.GameServerApp</mainClass>
        </properties>
    </profile>
    
    <!-- Scenario 3: Network (Client) -->
    <profile>
        <id>client</id>
        <properties>
            <mainClass>org.jgame.client.GameClient</mainClass>
        </properties>
    </profile>
    
    <!-- Scenario 4: Branded App -->
    <profile>
        <id>app</id>
        <properties>
            <app.name>MyGame</app.name>
            <mainClass>com.mycompany.MyGameApp</mainClass>
        </properties>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.panteleyev</groupId>
                    <artifactId>jpackage-maven-plugin</artifactId>
                    <configuration>
                        <name>${app.name}</name>
                        <appVersion>${project.version}</appVersion>
                        <vendor>${app.vendor}</vendor>
                        <icon>src/main/resources/${app.name}/icon.png</icon>
                        <type>INSTALLER</type>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

### Build Commands Summary

```bash
# Scenario 1: Library only
mvn clean package -P core-only

# Scenario 2: Standalone
mvn clean package -P standalone

# Scenario 3: Server
mvn clean package -P server

# Scenario 3: Java Client
mvn clean package -P client

# Scenario 4: Branded App
mvn clean package -P app -Dapp.name="MyChessGame" -Dapp.vendor="MyCompany"
```

---

## Deployment Recommendations

### Development
- Use **Standalone mode** for quick testing
- Use **Core library** for unit tests

### Production

#### Small Scale (< 100 users)
- **Standalone mode** distributed as JAR
- Optional: Central stats server

#### Medium Scale (100-10,000 users)
- **Network mode** with dedicated server
- Load balancer for multiple game servers
- PostgreSQL database

#### Large Scale (10,000+ users)
- **Kubernetes** deployment
- Microservices architecture
- Redis for session management
- CDN for plugin distribution

### Commercial Product
- **App mode** with auto-updates
- Code signing certificates
- Professional installers
- Cloud backend optional

---

## Technology Stack by Scenario

| Component | Scenario 1 | Scenario 2 | Scenario 3 | Scenario 4 |
|-----------|-----------|-----------|-----------|-----------|
| Core Logic | ✅ | ✅ | ✅ | ✅ |
| Swing UI | ❌ | ✅ | ✅ | Optional |
| JavaFX UI | ❌ | ❌ | ❌ | Optional |
| Embedded Server | ❌ | ✅ | ❌ | ✅ |
| Network Server | ❌ | ❌ | ✅ | Optional |
| H2 Database | Optional | ✅ | ❌ | ✅ |
| PostgreSQL | ❌ | ❌ | ✅ | Optional |
| Web Client | ❌ | ❌ | ✅ | ❌ |
| JPackage | ❌ | Optional | ❌ | ✅ |

---

## Next Steps for Implementation

### Phase 1: Refactor for Modularity
- [ ] Separate `jgame-core` module
- [ ] Extract `jgame-ui` module
- [ ] Create `jgame-server` module
- [ ] Create `jgame-client` module

### Phase 2: Implement Scenarios
- [ ] Add Maven profiles
- [ ] Create `StandaloneGameClient`
- [ ] Implement plugin distribution protocol
- [ ] Build jpackage configuration

### Phase 3: Documentation
- [ ] Scenario-specific README files
- [ ] Deployment guides
- [ ] API documentation

### Phase 4: Examples
- [ ] Sample branded app
- [ ] Docker compose for network mode
- [ ] Web client demo

---

**Questions to Address**:
1. Should we use modular JARs (Java 9+)?
2. WebSocket vs HTTP for network protocol?
3. Plugin sandboxing for security?
4. Support for mobile clients (Android/iOS)?

---

**Next Implementation Priority**: Finish Chess, then refactor into modules.
