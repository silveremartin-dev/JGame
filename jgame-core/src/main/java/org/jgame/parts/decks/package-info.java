/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot, Google Gemini (Antigravity)
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
 */
/**
 * Deck implementations for card games.
 * 
 * <p>
 * This package provides deck implementations for managing collections of cards
 * including shuffling, drawing, and deck manipulation operations.
 * </p>
 * 
 * <h2>Components</h2>
 * <ul>
 * <li>{@link org.jgame.parts.decks.Deck} - Standard 52-card deck
 * implementation</li>
 * <li>{@link org.jgame.parts.decks.AbstractDeck} - Base class for custom
 * decks</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * Deck deck = new Deck();
 * deck.shuffle();
 * Card card = deck.draw();
 * }</pre>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.parts.cards
 * @see org.jgame.parts.DeckInterface
 */
package org.jgame.parts.decks;