/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */


package org.jgame.logic;

import org.jgame.parts.PlayerInterface;

import java.util.Set;

public interface TournamentGenerator {

      int ROUND_ROBIN = 1; //https://en.wikipedia.org/wiki/Round-robin_tournament
      int KNOCKOUT = 2; //https://en.wikipedia.org/wiki/Single-elimination_tournament
      int SWISS_SYSTEM = 3; //https://en.wikipedia.org/wiki/Swiss-system_tournament
      int DOUBLE_ELIMINATION = 4; //https://en.wikipedia.org/wiki/Double-elimination_tournament
      int MCINTYRE_SYSTEM = 5; //https://en.wikipedia.org/wiki/McIntyre_System
      int SHAUGHNESSY_PLAYOFF_SYSTEM = 6; //https://en.wikipedia.org/wiki/Shaughnessy_playoff_system
      int MCMAHON_SYSTEM = 7; //https://en.wikipedia.org/wiki/McMahon_system_tournament
      int GROUP_TOURNAMENT_RANKING_SYSTEM = 8; //https://en.wikipedia.org/wiki/Group_tournament_ranking_system

      Tournament getTournament(GameInterface game, Set<PlayerInterface> players, int mode);

}
