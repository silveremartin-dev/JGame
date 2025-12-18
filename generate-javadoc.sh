#!/bin/bash
# JGame Javadoc Generation Script
# Authors: Google Gemini (Antigravity), Silvere Martin-Michiellot

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo ""
echo "========================================"
echo "   JGame Javadoc Generator"
echo "========================================"
echo ""

# Create output directory
mkdir -p javadoc

# Generate Javadoc
echo "Generating Javadoc..."
mvn javadoc:aggregate -Ddoctitle="JGame Platform API" -Dwindowtitle="JGame API" -DoutputDirectory=javadoc

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "   Javadoc generated!"
    echo "========================================"
    echo ""
    echo "Output: javadoc/index.html"
    
    # List output
    ls -la javadoc/
else
    echo "Javadoc generation failed."
    exit 1
fi
