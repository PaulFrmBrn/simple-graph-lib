package com.paulfrmbrn;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Represent a graph
 * Use one of the available builders to construct an immutable instance of the class
 * Builders provide all available types of graph implementation.
 * Negative weighted graphs are not supported.
 *
 * Disconnected graph or graph with unreachable vertices can be constructed, but {@link Graph#traverse(Consumer)} and
 * {@link Graph#findPath(Vertex, Vertex, Consumer)} methods could fail with {@link IllegalStateException} in this case.
 *
 * Max count of vertices is {@link Integer#MAX_VALUE}
 *
 * @author Dmitry Pavlov
 * @since 30.05.2020
 */
@Immutable
public final class Graph<T> {

    public static final int INFINITY_DISTANCE_VALUE = Integer.MAX_VALUE;

    private final boolean isWeighted;
    private final Vertex<T> root;
    private final Set<Vertex<T>> vertices;
    private final Map<Vertex<T>, Set<Edge<T>>> edgesMap;

    private Graph(Vertex<T> vertex, Set<Vertex<T>> vertices, Map<Vertex<T>, Set<Edge<T>>> edgesMap, boolean isWeighted) {
        this.root = requireNonNull(vertex, "vertex");
        this.vertices = requireNonNull(vertices, "vertices");
        this.edgesMap = requireNonNull(edgesMap, "edgesMap");
        this.isWeighted = isWeighted;
        vertices.add(root);
    }

    /**
     * Traverses the graph from the {@link Graph#root} and applies specified action on each vertex
     *
     * @throws IllegalStateException if {to} is not reachable from {from}
     */
    public void traverse(@Nonnull Consumer<T> consumer) {

        requireNonNull(consumer, "consumer");

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

    /**
     * Given a pair of vertices tries to find shortest path between them.
     *
     * @param from Vertx from which path is searched. Do not has to be a root vertex of the graph
     * @param to Vertx to which path is searched
     * @param consumer action to be applied to each Vertex's value on the path from {from} Vertex to {to}
     *
     * @throws IllegalStateException if {to} is not reachable from {from}
     */
    public void findPath(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, @Nonnull Consumer<T> consumer) {

        validateVertex(from);
        validateVertex(to);
        requireNonNull(consumer, "consumer");

        if (from.equals(to)) {
            consumer.accept(from.getValue());
            return;
        }

        var parents = isWeighted ? findPathByDijkstra(from) : findPathByBfs(from, to);

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

    /**
     * BFS algorithm implementation for finding shortest path between two vertices
     * Should be used only for instances of {@link Graph} with {@link Graph#isWeighted} equals to {@link Boolean#FALSE}
     * Otherwise path found may not be the shortest one
     *
     * @return Map representing vertex to it's parent vertex mapping
     */
    private HashMap<Vertex<T>, Vertex<T>> findPathByBfs(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to) {

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
        return parents;
    }

    /**
     * Dijkstra algorithm implementation for finding shortest path between two vertices
     * Should be used only for instances of {@link Graph} with {@link Graph#isWeighted} equals to {@link Boolean#TRUE}
     * Otherwise {@link Graph#findPathByBfs(Vertex, Vertex)} is preferable due to its lower time complexity
     *
     * @return Map representing vertex to it's parent vertex mapping
     */
    private HashMap<Vertex<T>, Vertex<T>> findPathByDijkstra(@Nonnull Vertex<T> from) {

        var minHeap = new PriorityQueue<Distance<T>>();
        var distances = new HashMap<Vertex<T>, Distance<T>>();
        vertices.forEach(vertex -> {
            int value = vertex.equals(from) ? 0 : INFINITY_DISTANCE_VALUE;
            var distance = new Distance<>(vertex, value);
            minHeap.add(distance);
            distances.put(vertex, distance);
        });
        var notVisitedSet = new HashSet<>(vertices);
        var parents = new HashMap<Vertex<T>, Vertex<T>>();

        while (!minHeap.isEmpty()) {

            var current = minHeap.remove();
            notVisitedSet.remove(current.getVertex());

            var edges = edgesMap.get(current.getVertex());
            if (edges != null) {
                for (var edge : edges) {
                    if (notVisitedSet.contains(edge.getVertex())) {

                        var currentDistance = distances.get(current.getVertex());
                        var edgeDistance = distances.get(edge.getVertex());
                        int weight = edge.getWeight();
                        if (edgeDistance.getValue() > (currentDistance.getValue() + weight)) {
                            edgeDistance.setValue((currentDistance.getValue() + weight));
                            parents.put(edge.getVertex(), current.getVertex());

                            minHeap.remove(edgeDistance);
                            minHeap.add(edgeDistance);
                        }

                    }

                }
            }

        }
        return parents;

    }


    /**
     * @return all vertices of the graph
     */
    public Set<Vertex<T>> getVertices() {
        return new HashSet<>(this.vertices);
    }

    /**
     * @return all edges for the specified vertex
     */
    public Set<Edge<T>> getEdges(Vertex<T> from) {
        validateVertex(from);
        return edgesMap.get(from) == null
                ? Collections.emptySet()
                : new HashSet<>(edgesMap.get(from));

    }

    private void validateVertex(@Nonnull Vertex<T> vertex) {
        requireNonNull(vertex, "vertex");
        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex does not belong to the Graph");
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

    public static <T> UndirectedUnweightedBuilder<T> undirectedUnweightedBuilder(@Nonnull Vertex<T> root){
        return new UndirectedUnweightedBuilder<>(root);
    }

    public static <T> DirectedUnweightedBuilder<T> directedUnweightedBuilder(@Nonnull Vertex<T> root){
        return new DirectedUnweightedBuilder<>(root);
    }

    public static <T> UndirectedWeightedBuilder<T> undirectedWeightedBuilder(@Nonnull Vertex<T> root){
        return new UndirectedWeightedBuilder<>(root);
    }

    public static <T> DirectedWeightedBuilder<T> directedWeightedBuilder(@Nonnull Vertex<T> root){
        return new DirectedWeightedBuilder<>(root);
    }

    public static class Builder<T> {

        protected static final int UNWEIGHTED_WEIGHT = 1;

        private final boolean isWeighted;
        private final Vertex<T> root;
        private final Set<Vertex<T>> vertices;
        private final Map<Vertex<T>, Set<Edge<T>>> edgesMap;

        protected Builder(@Nonnull Vertex<T> root, boolean isWeighted) {
            this.root = requireNonNull(root, "root");
            this.vertices = new HashSet<>();
            this.edgesMap = new HashMap<>();
           this.isWeighted = isWeighted;
            vertices.add(root);
        }

        public Builder<T> addVertex(@Nonnull Vertex<T> vertex) {
            vertices.add(requireNonNull(vertex, "vertex"));
            return this;
        }

        protected Builder<T> addEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {
            validateVertex(from);
            validateVertex(to);
            addEdgeInternal(from, to, weight);
            return this;
        }

        protected Builder<T> addUndirectedEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {
            validateVertex(from);
            validateVertex(to);
            addEdgeInternal(from, to, weight);
            addEdgeInternal(to, from, weight);
            return this;
        }

        protected Builder<T> addEdgeInternal(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {

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
            return this;
        }

        private void validateVertex(@Nonnull Vertex<T> vertex) {
            requireNonNull(vertex, "vertex");
            if (!vertices.contains(vertex)) {
                throw new IllegalArgumentException("Vertex does not belong to the Graph");
            }
        }

        public Graph<T> build() {
            return new Graph<>(this.root, this.vertices, this.edgesMap, isWeighted);
        }

    }

    public static class UndirectedUnweightedBuilder<T> {

        private final Builder<T> builder;

        public UndirectedUnweightedBuilder(Vertex<T> root) {
            builder = new Builder<T>(root, false);
        }

        public UndirectedUnweightedBuilder<T> addVertex(@Nonnull Vertex<T> vertex) {
            builder.addVertex(vertex);
            return this;
        }

        public UndirectedUnweightedBuilder<T> addEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to) {
            builder.addUndirectedEdge(from, to, Builder.UNWEIGHTED_WEIGHT);
            return this;
        }

        public Graph<T> build() {
            return builder.build();
        }

    }

    public static class DirectedUnweightedBuilder<T>  {

        private final Builder<T> builder;

        public DirectedUnweightedBuilder(Vertex<T> root) {
            builder = new Builder<>(root, false);
        }

        public DirectedUnweightedBuilder<T> addVertex(@Nonnull Vertex<T> vertex) {
            builder.addVertex(vertex);
            return this;
        }

        public DirectedUnweightedBuilder<T> addEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to) {
            builder.addEdge(from, to, Builder.UNWEIGHTED_WEIGHT);
            return this;
        }

        public Graph<T> build() {
            return builder.build();
        }

    }

    public static class UndirectedWeightedBuilder<T> {

        private final Builder<T> builder;

        public UndirectedWeightedBuilder(Vertex<T> root) {
            builder = new Builder<>(root, true);
        }

        public UndirectedWeightedBuilder<T> addVertex(@Nonnull Vertex<T> vertex) {
            builder.addVertex(vertex);
            return this;
        }

        public UndirectedWeightedBuilder<T> addEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {
            builder.addUndirectedEdge(from, to, weight);
            return this;
        }

        public Graph<T> build() {
            return builder.build();
        }

    }

    public static class DirectedWeightedBuilder<T>  {

        private final Builder<T> builder;

        public DirectedWeightedBuilder(Vertex<T> root) {
            builder = new Builder<>(root, true);
        }

        public DirectedWeightedBuilder<T> addVertex(@Nonnull Vertex<T> vertex) {
            builder.addVertex(vertex);
            return this;
        }

        public DirectedWeightedBuilder<T> addEdge(@Nonnull Vertex<T> from, @Nonnull Vertex<T> to, int weight) {
            builder.addEdge(from, to, weight);
            return this;
        }

        public Graph<T> build() {
            return builder.build();
        }

    }

}
