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

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//mostly a tagging interface used to put the rules into
//as rules are very game dependent there is not much common method we can put into
public abstract class AbstractRuleset implements Set<Rule> {

    private HashSet<Rule> rules;

    @Override
    public int size() {
        return rules.size();
    }

    @Override
    public boolean isEmpty() {
        return rules.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return rules.contains(o);
    }

    @NotNull
    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return rules.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return rules.toArray(a);
    }

    @Override
    public boolean add(Rule rule) {
        return rules.add(rule);
    }

    @Override
    public boolean remove(Object o) {
        return rules.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return rules.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Rule> c) {
        return rules.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return rules.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return rules.removeAll(c);
    }

    @Override
    public void clear() {
        rules.clear();
    }

}
