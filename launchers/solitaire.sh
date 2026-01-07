#!/bin/bash
cd ..
mvn -pl jgame-games/jgame-game-solitaire exec:java -Dexec.mainClass="org.jgame.launcher.SolitaireLauncher"
