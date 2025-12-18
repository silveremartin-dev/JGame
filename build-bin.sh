#!/bin/bash
# JGame Build and Copy JARs to /bin
# Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

VERSION="1.0-SNAPSHOT"

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}"
echo "========================================"
echo "   JGame Build to /bin"
echo "========================================"
echo -e "${NC}"

# Build project
echo "Building project..."
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

# Create bin directory structure
mkdir -p bin/lib bin/plugins bin/web

echo ""
echo "Copying JARs to bin/..."

# Copy main JARs
cp -f jgame-core/target/jgame-core-$VERSION.jar bin/ 2>/dev/null
cp -f jgame-persistence/target/jgame-persistence-$VERSION.jar bin/ 2>/dev/null
cp -f jgame-server/target/jgame-server-$VERSION.jar bin/ 2>/dev/null
cp -f jgame-client-java/target/jgame-client-java-$VERSION.jar bin/ 2>/dev/null

# Copy dependencies
echo "Copying dependencies..."
mvn dependency:copy-dependencies -DoutputDirectory=bin/lib -pl jgame-server -q 2>/dev/null

# Copy game plugins
echo "Copying game plugins..."
for game in chess checkers goose solitaire; do
    if [ -f "jgame-games/jgame-game-$game/target/jgame-game-$game-$VERSION.jar" ]; then
        cp -f "jgame-games/jgame-game-$game/target/jgame-game-$game-$VERSION.jar" bin/plugins/
        echo "  - jgame-game-$game.jar"
    fi
done

# Copy launcher scripts
cp -f run-server.sh bin/
cp -f run-client.sh bin/
cp -f run-server.bat bin/
cp -f run-client.bat bin/
chmod +x bin/*.sh

# Copy web client
cp -rf jgame-client-web/src/* bin/web/

echo ""
echo -e "${GREEN}"
echo "========================================"
echo "   Build Complete!"
echo "========================================"
echo -e "${NC}"
echo ""
echo "Output structure:"
echo "  bin/"
echo "    jgame-core-$VERSION.jar"
echo "    jgame-persistence-$VERSION.jar"
echo "    jgame-server-$VERSION.jar"
echo "    jgame-client-java-$VERSION.jar"
echo "    lib/         (dependencies)"
echo "    plugins/     (game JARs)"
echo "    web/         (web client)"
echo ""
echo "JARs in bin/:"
ls -1 bin/*.jar 2>/dev/null
echo ""
echo "Plugins:"
ls -1 bin/plugins/*.jar 2>/dev/null
