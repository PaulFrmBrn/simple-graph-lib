package com.paulfrmbrn;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * todo comment
 * todo implement
 * todo javadoc
 * todo test
 * todo toString()
 * todo equals, hashcode
 * todo rnn
 * todo metrics
 * <p>
 * all verticies should be reachable from root
 * should not be diconenected
 * max size is max int
 *
 * @author Dmitry Pavlov
 * @since 30.05.2020
 */
public class Graph<T> {

    private final Vertex<T> root;
    private final Set<Vertex<T>> vertices;
    private final Map<Vertex<T>, Set<Edge<T>>> edgesMap;

    public Graph(Vertex<T> vertex) {
        this.root = vertex;
        this.vertices = new HashSet<>();
        this.edgesMap = new HashMap<>();
        vertices.add(root);
    }

    public void addVertex(@Nonnull Vertex<T> vertex) {
        vertices.add(requireNonNull(vertex, "vertex")); // todo check implementation aftrer swithivng to map
    }

    public void addEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {
        validateVertex(from);
        validateVertex(to);
        addEdgeInternal(from, to, weight);
    }

    public void addUndirectedEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {
        validateVertex(from);
        validateVertex(to);
        if (from.compareTo(to) < 0) {
            addEdgeInternal(from, to, weight);
            addEdgeInternal(to, from, weight);
        } else {
            addEdgeInternal(to, from, weight);
            addEdgeInternal(from, to, weight);
        }
    }

    private void addEdgeInternal(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {

        var newEdge = new Edge<>(to, weight);

        edgesMap.compute(from, (key, value) -> {
            if (value == null) {
                var newValue = new HashSet<Edge<T>>();
                newValue.add(newEdge);
                return newValue;
            } else {
                value.add(newEdge);
                return value;
            }
        });
    }

    public void traverse(@Nonnull Consumer<T> consumer) {

        requireNonNull(consumer, "supplier");

        var visitedSet = new HashSet<Vertex<T>>();
        var toBeVisitedQueue = new LinkedList<Vertex<T>>();

        toBeVisitedQueue.add(this.root);
        while (visitedSet.size() < vertices.size()) {

            if (toBeVisitedQueue.isEmpty()) {
                throw new IllegalStateException("Some vertices are not reachable from the root");
            }

            var current = toBeVisitedQueue.remove(0);
            if (!visitedSet.contains(current)) {
                visitedSet.add(current);
                consumer.accept(current.getValue());

                var edges = edgesMap.get(current);
                if (edges != null) {
                    edges.forEach(edge -> {
                        if (!visitedSet.contains(edge.getVertex())) {
                            toBeVisitedQueue.add(edge.getVertex());
                        }
                    });
                }
            }

        }
    }

    public void findPath(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, @Nonnull Consumer<T> consumer) {

        validateVertex(from);
        validateVertex(to);
        requireNonNull(consumer, "supplier");

        if (from.equals(to)) {
            consumer.accept(from.getValue());
            return;
        }

        var visitedSet = new HashSet<Vertex<T>>();
        var toBeVisitedQueue = new LinkedList<Vertex<T>>();
        var parents = new HashMap<Vertex<T>, Vertex<T>>();

        toBeVisitedQueue.add(from);
        boolean done = false;
        while (!toBeVisitedQueue.isEmpty() && !done) {

            var current = toBeVisitedQueue.remove(0);
            if (!visitedSet.contains(current)) {
                visitedSet.add(current);

                var edges = edgesMap.get(current);
                if (edges != null) {
                    for (var edge : edges) {
                        if (!visitedSet.contains(edge.getVertex())) {
                            toBeVisitedQueue.add(edge.getVertex());
                            parents.compute(edge.getVertex(), (key, value) -> {
                                if (value != null) {
                                    return value;
                                } else {
                                    return current;
                                }
                            });
                            if (edge.getVertex().equals(to)) {
                                done = true;
                            }
                        }
                    }
                }
            }

        }

        var path = new LinkedList<Vertex<T>>();
        var current = to;
        do {
            path.addFirst(current);
            current = parents.get(current);
        } while (current != null);
        if (!path.getFirst().equals(from)) {
            throw new IllegalStateException("Vertex 'to' is not reachable from vertex 'from'");
        }
        path.forEach(vertex -> consumer.accept(vertex.getValue()));

    }

    public Set<Vertex<T>> getVertices() {
        return new HashSet<>(this.vertices);
    }

    public Set<Edge<T>> getEdges(Vertex<T> from) {
        validateVertex(from);
        return edgesMap.get(from) == null
                ? Collections.emptySet()
                : new HashSet<>(edgesMap.get(from));

    }

    private void validateVertex(@Nonnull Vertex<T> vertex) {
        requireNonNull(vertex, "vertex");
        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex do not belong to the Graph");
        }
    }

    @Override
    public String toString() {
        return vertices.stream()
                .sorted()
                .map(it -> "'" + it.getValue() + (edgesMap.get(it) == null ? "'" :
                        "'->" + edgesMap.get(it).stream()
                                .sorted()
                                .map(edge -> "'" + edge.getVertex().getValue() + "'" + "{" + edge.getWeight() + "}")
                                .collect(Collectors.joining(","))))
                .collect(Collectors.joining(" | ", "[", "]"));
    }

}
