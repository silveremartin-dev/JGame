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
