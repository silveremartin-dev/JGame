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
package org.jgame.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Graph<N extends NodeInterface> {

	private final HashSet<N> nodes;
	private final HashSet<Edge> edges;

	public Graph() {
		nodes = new HashSet<N>();
		edges = new HashSet<Edge>();
	}

	public Set<N> getNodes() {
		return nodes;
	}

	public Set<Edge> getEdges() {
		return edges;
	}

	public boolean existsEdge(@NotNull N source, @NotNull N destination) {
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

	@SuppressWarnings("unchecked")
	public boolean addEdge(@NotNull Edge edge) {
		N source = (N) edge.getSource();
		N destination = (N) edge.getDestination();
		if (!existsEdge(source, destination)) {
			nodes.add(source);
			nodes.add(destination);
			return edges.add(edge);
		} else
			throw new IllegalArgumentException("Edge with same source and destination already exists in graph.");
	}

	public boolean removeEdge(Edge edge) {
		// don't remove nodes even if they get not connected
		return edges.remove(edge);
	}

	public Set<Edge> getInputEdges(@NotNull N node) {
		HashSet<Edge> resultEdges;
		resultEdges = new HashSet<>();
		if (nodes.contains(node)) {
			for (Edge e : edges) {
				if (e.getDestination().equals(node)) {
					resultEdges.add(e);
				}
			}
			return resultEdges;
		} else
			throw new IllegalArgumentException("Node doesn't belong to graph.");
	}

	public Set<Edge> getOutputEdges(@NotNull N node) {
		HashSet<Edge> resultEdges;
		resultEdges = new HashSet<>();
		if (nodes.contains(node)) {
			for (Edge e : edges) {
				if (e.getSource().equals(node)) {
					resultEdges.add(e);
				}
			}
			return resultEdges;
		} else
			throw new IllegalArgumentException("Node doesn't belong to graph.");
	}

	public boolean addNode(@NotNull N node) {
		return nodes.add(node);
	}

	public boolean removeNode(@NotNull N node) {
		edges.removeAll(getInputEdges(node));
		edges.removeAll(getOutputEdges(node));
		return nodes.remove(node);
	}

}