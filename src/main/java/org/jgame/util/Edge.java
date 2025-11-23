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


import org.jetbrains.annotations.NotNull;

public class Edge {
	
    private final NodeInterface source;
	private final NodeInterface destination;
	private int weight;

    public Edge(@NotNull final NodeInterface source, @NotNull final NodeInterface destination) {
        this.source = source;
        this.destination = destination;
        this.weight = 0;
    }

    public Edge(@NotNull final NodeInterface source, @NotNull final NodeInterface destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public NodeInterface getSource() {
        return source;
    }

    public NodeInterface getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}