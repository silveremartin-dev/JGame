#!/bin/bash
cd ..
mvn -pl jgame-games/jgame-game-goose exec:java -Dexec.mainClass="org.jgame.launcher.GooseLauncher"
