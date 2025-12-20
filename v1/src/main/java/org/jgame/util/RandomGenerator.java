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

package org.jgame.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator {

    private static Random random;

    private RandomGenerator() {
        random = new Random();
    }

    public static int rollDice(int faces) {
        return random.nextInt(faces) + 1;
    }

    public static List<Integer> rollDices(int faces, int numberOfDices) {
        List<Integer> resultList;
        resultList = new ArrayList<>();
        for (int i = 0; i < numberOfDices; i++) {
            resultList.add(random.nextInt(faces) + 1);
        }
        return resultList;
    }

    public static boolean flipCoin() {
        return random.nextBoolean();
    }

}
