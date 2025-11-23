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

package org.jgame.parts.decks;

import org.jgame.parts.DeckInterface;
import org.jgame.parts.pieces.AbstractCard;

import java.util.Collections;
import java.util.List;

public abstract class AbstractDeck implements DeckInterface {

    private List<AbstractCard> cards;

    @Override
    public List<AbstractCard> getCards() {
        return cards;
    }

    public void setCards(List<AbstractCard> cards) {
        this.cards = cards;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public int size() {
        return cards.size();
    }
}
