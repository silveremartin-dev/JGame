#!/bin/bash
cd ..
mvn -pl jgame-games/jgame-game-checkers exec:java -Dexec.mainClass="org.jgame.launcher.CheckersLauncher"
