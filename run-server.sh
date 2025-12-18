#!/bin/bash
# JGame Server Launcher
# Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Configuration
SERVER_PORT=${JGAME_PORT:-8080}
JAVA_OPTS=${JAVA_OPTS:-"-Xmx512m -Xms256m"}

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}╔════════════════════════════════════╗${NC}"
echo -e "${BLUE}║       JGame Server v1.0            ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════╝${NC}"
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "Error: Java not found. Please install JDK 21+."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo -e "Java version: ${GREEN}$JAVA_VERSION${NC}"

# Build if needed
if [ ! -f "jgame-server/target/jgame-server-1.0-SNAPSHOT.jar" ]; then
    echo "Building project..."
    mvn package -DskipTests -q
fi

# Run server
echo -e "Starting server on port ${GREEN}$SERVER_PORT${NC}..."
echo ""

cd jgame-server
exec java $JAVA_OPTS \
    -Dserver.port=$SERVER_PORT \
    -cp "target/classes:target/dependency/*" \
    org.jgame.server.JGameServer
