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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Graph {
	
	private final HashSet<NodeInterface> nodes;
	private final HashSet<Edge> edges;
	
	public Graph() {
		nodes = new HashSet<NodeInterface>();
		edges = new HashSet<Edge>();
	}

	public Set<NodeInterface> getNodes() {
		return nodes;
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	public boolean existsEdge(@NotNull NodeInterface source, @NotNull NodeInterface destination) {
		boolean found;
		Iterator<Edge> edgesIterator;
		Edge currentEdge;
		edgesIterator = edges.iterator();
		found = false;
		while (edgesIterator.hasNext() && !found) {
			currentEdge = edgesIterator.next();
			found = (currentEdge.getSource() == source) && (currentEdge.getDestination() == destination);
		}
		return found;
	}

	public boolean addEdge(@NotNull Edge edge) {
		if (!existsEdge(edge.getSource(), edge.getDestination())) {
			nodes.add(edge.getSource());
			nodes.add(edge.getDestination());
			return edges.add(edge);
		} else throw new IllegalArgumentException("Edge with sema source and destination already exists in graph.");
	}

	public boolean removeEdge(Edge edge) {
		//don't remove nodes even if they get not connected
		return edges.remove(edge);
	}

	public Set<Edge> getInputEdges(@NotNull NodeInterface node) {
		HashSet<Edge> resultEdges;
		resultEdges = new HashSet<>();
		if (nodes.contains(node)) {
			for(Edge e : edges) {
				if (e.getDestination().equals(node)) {
					resultEdges.add(e);
				}
			}
			return resultEdges;
		} else throw new IllegalArgumentException("Node doesn't belong to graph.");
	}

	public Set<Edge> getOutputEdges(@NotNull NodeInterface node) {
		HashSet<Edge> resultEdges;
		resultEdges = new HashSet<>();
		if (nodes.contains(node)) {
			for(Edge e : edges) {
				if (e.getSource().equals(node)) {
					resultEdges.add(e);
				}
			}
			return resultEdges;
		} else throw new IllegalArgumentException("Node doesn't belong to graph.");
	}

	public boolean addNode(@NotNull NodeInterface node) {
		return nodes.add(node);
	}

	public boolean removeNode(@NotNull NodeInterface node) {
		edges.removeAll(getInputEdges(node));
		edges.removeAll(getOutputEdges(node));
		return nodes.remove(node);
	}

}