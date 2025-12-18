#!/bin/bash
# JGame Client Launcher
# Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Configuration
SERVER_URL=${JGAME_SERVER:-"http://localhost:8080"}
JAVA_OPTS=${JAVA_OPTS:-"-Xmx256m"}

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}╔════════════════════════════════════╗${NC}"
echo -e "${BLUE}║       JGame Client v1.0            ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════╝${NC}"
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "Error: Java not found. Please install JDK 21+."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo -e "Java version: ${GREEN}$JAVA_VERSION${NC}"
echo -e "Server: ${GREEN}$SERVER_URL${NC}"
echo ""

# Build if needed
if [ ! -d "jgame-client-java/target/classes" ]; then
    echo "Building project..."
    mvn package -DskipTests -q
fi

# Run JavaFX client
cd jgame-client-java

# Detect OS for JavaFX modules
case "$(uname -s)" in
    Linux*)     JAVAFX_PLATFORM="linux";;
    Darwin*)    JAVAFX_PLATFORM="mac";;
    CYGWIN*|MINGW*|MSYS*) JAVAFX_PLATFORM="win";;
    *)          JAVAFX_PLATFORM="linux";;
esac

echo "Launching JavaFX client..."
exec mvn javafx:run -Djavafx.platform=$JAVAFX_PLATFORM -Dserver.url=$SERVER_URL
