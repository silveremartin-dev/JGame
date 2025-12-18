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