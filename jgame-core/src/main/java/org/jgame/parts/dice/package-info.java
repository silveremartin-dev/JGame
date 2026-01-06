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
 * Dice components for random number generation in games.
 * 
 * <p>
 * This package provides dice implementations for generating random numbers,
 * supporting various die types (D4, D6, D8, D10, D12, D20, D100) and custom
 * configurations.
 * </p>
 * 
 * <h2>Components</h2>
 * <ul>
 * <li>{@link org.jgame.parts.dice.Die} - Dice implementation with standard
 * types</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * Die d6 = Die.D6;
 * int roll = d6.roll(); // Returns 1-6
 * 
 * Die customDie = new Die(20); // D20
 * int result = customDie.roll(); // Returns 1-20
 * }</pre>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.parts.DieInterface
 */
package org.jgame.parts.dice;
