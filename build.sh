#!/bin/bash
# JGame Full Build Script
# Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}╔════════════════════════════════════╗${NC}"
echo -e "${BLUE}║       JGame Build Script           ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════╝${NC}"
echo ""

# Check prerequisites
echo "Checking prerequisites..."

if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java not found${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java found${NC}"

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}✗ Maven not found${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven found${NC}"

echo ""
echo "Building JGame Platform..."
echo ""

# Clean and build
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}╔════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║       BUILD SUCCESSFUL!            ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════╝${NC}"
    echo ""
    echo "To start the server:  ./run-server.sh"
    echo "To start the client:  ./run-client.sh"
else
    echo ""
    echo -e "${RED}Build failed!${NC}"
    exit 1
fi
