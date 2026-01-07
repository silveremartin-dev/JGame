#!/bin/bash
cd ..
mvn -pl jgame-games/jgame-game-chess exec:java -Dexec.mainClass="org.jgame.launcher.ChessLauncher"
