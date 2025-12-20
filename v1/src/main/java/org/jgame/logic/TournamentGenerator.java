/*
 * Copyright 2022 Silvere Martin-Michiellot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
