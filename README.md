# JGame Framework

A modern Java game framework supporting multiple game types including board games, card games, puzzles, and platform games.

## 📋 Features

- **Multiple Game Types**: Board games, card games, puzzles, platform games
- **Client-Server Architecture**: Network-capable multiplayer support
- **AI Players**: Support for computer opponents
- **Tournament System**: Organize and manage game tournaments
- **Extensible Design**: Abstract base classes for easy game implementation
- **Score Tracking**: Comprehensive scoring and ranking systems

## 🎮 Supported Games

- Chess
- Checkers
- Game of the Goose
- Tantrix
- Reversi/Othello
- Go
- Dominos
- Playing cards framework

## 🛠️ Requirements

- **Java**: JDK 21 or later
- **Build Tool**: Apache Maven 3.6+
- **OS**: Windows, Linux, or macOS

## 🚀 Building the Project

```bash
# Clone the repository
git clone <repository-url>
cd JGame

# Build with Maven
mvn clean install

# Run tests
mvn test

# Generate Javadoc
mvn javadoc:javadoc
```

## 📦 Project Structure

```
JGame/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/jgame/
│   │   │       ├── client/      # Client application
│   │   │       ├── server/      # Server application
│   │   │       ├── logic/       # Game logic and rules
│   │   │       ├── parts/       # Game components (boards, pieces, players)
│   │   │       ├── ui/          # User interface
│   │   │       ├── widgets/     # UI widgets and game pieces
│   │   │       ├── io/          # Import/export functionality
│   │   │       └── util/        # Utility classes
│   │   └── resources/
│   │       └── META-INF/
│   └── test/
│       └── java/                # Test classes
├── pom.xml                      # Maven configuration
├── LICENSE                      # MIT License
└── README.md                    # This file
```

## 🎯 Quick Start

### Running the Server

```bash
java -jar target/jgame-1.0-SNAPSHOT.jar server
```

### Running the Client

```bash
java -jar target/jgame-1.0-SNAPSHOT.jar client
```

## 🧩 Creating a New Game

To create a new game, extend the appropriate abstract base class:

```java
public class MyGame extends AbstractBoardGame {
    @Override
    public void initGame(int numPlayers) {
        // Initialize game state
    }
    
    @Override
    public void startGame() {
        // Start the game
    }
    
    // Implement other required methods...
}
```

## 📚 Documentation

- **Javadoc**: Run `mvn javadoc:javadoc` and open `target/site/apidocs/index.html`
- **Developer Guide**: See `DEVELOPER_GUIDE.md` (coming soon)
- **User Guide**: See `USER_GUIDE.md` (coming soon)

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage report
mvn clean test jacoco:report
# View coverage at target/site/jacoco/index.html
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Silvere Martin-Michiellot**
- Email: silvere.martin@gmail.com

## 🙏 Acknowledgments

- Enhanced with AI assistance from Google Gemini (Antigravity)
- Game piece graphics from various open-source projects
- Inspired by classic board and card games

## 🐛 Known Issues

- UI infrastructure classes (AboutDialog, TextAndMnemonicUtils) are not yet fully implemented
- Some game rules implementations are incomplete (marked with TODO comments)
- Full compilation requires implementing missing UI components

## 🗺️ Roadmap

- [ ] Complete UI infrastructure
- [ ] Finish game rules implementations
- [ ] Add more game types
- [ ] Improve AI algorithms
- [ ] Add multiplayer matchmaking
- [ ] Create web-based UI
- [ ] Mobile app support

## 📞 Support

For issues and questions, please open an issue on the project repository.

---

**Status**: Active Development | **Version**: 1.0-SNAPSHOT | **Java**: 21
