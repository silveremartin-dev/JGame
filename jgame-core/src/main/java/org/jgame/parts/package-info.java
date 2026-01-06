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
 * Reusable game component interfaces.
 * 
 * <p>
 * This package defines the core interfaces for reusable game components that
 * can
 * be composed to build various types of games. It follows a component-based
 * architecture
 * where games are built from modular, interchangeable parts.
 * </p>
 * 
 * <h2>Core Interfaces</h2>
 * <ul>
 * <li>{@link org.jgame.parts.BoardInterface} - Game board abstraction</li>
 * <li>{@link org.jgame.parts.PieceInterface} - Game piece abstraction</li>
 * <li>{@link org.jgame.parts.PlayerInterface} - Player abstraction</li>
 * <li>{@link org.jgame.parts.TileInterface} - Board tile/position
 * abstraction</li>
 * <li>{@link org.jgame.parts.DeckInterface} - Card deck abstraction</li>
 * <li>{@link org.jgame.parts.DieInterface} - Dice abstraction</li>
 * <li>{@link org.jgame.parts.InventoryInterface} - Item inventory
 * abstraction</li>
 * <li>{@link org.jgame.parts.PotInterface} - Resource pool abstraction</li>
 * </ul>
 * 
 * <h2>Implementation Packages</h2>
 * <ul>
 * <li>{@link org.jgame.parts.boards} - Board implementations</li>
 * <li>{@link org.jgame.parts.pieces} - Piece implementations</li>
 * <li>{@link org.jgame.parts.players} - Player implementations</li>
 * <li>{@link org.jgame.parts.tiles} - Tile implementations</li>
 * <li>{@link org.jgame.parts.decks} - Deck implementations</li>
 * <li>{@link org.jgame.parts.dice} - Dice implementations</li>
 * <li>{@link org.jgame.parts.inventories} - Inventory implementations</li>
 * <li>{@link org.jgame.parts.pots} - Pot implementations</li>
 * <li>{@link org.jgame.parts.cards} - Card components</li>
 * </ul>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.logic
 */
package org.jgame.parts;