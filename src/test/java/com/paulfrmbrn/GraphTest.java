package com.paulfrmbrn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * todo comment
 * todo implement
 * todo javadoc
 * todo test
 * todo toString()
 * todo equals, hashcode
 * todo rnn
 * todo metrics
 *
 * todo parametrized tests for each implementation
 *
 * @author Dmitry Pavlov
 * @since 30.05.2020
 */
class GraphTest {

    private static final Vertex<String> VERTEX_A = Vertex.of("A");
    private static final Vertex<String> VERTEX_B = Vertex.of("B");
    private static final Vertex<String> VERTEX_C = Vertex.of("C");
    private static final Vertex<String> VERTEX_D = Vertex.of("D");
    private static final Vertex<String> VERTEX_E = Vertex.of("E");
    private static final Vertex<String> VERTEX_F = Vertex.of("F");
    private static final Vertex<String> VERTEX_G = Vertex.of("G");
    private static final Vertex<String> VERTEX_H = Vertex.of("H");
    private static final Vertex<String> VERTEX_I = Vertex.of("I");
    private static final Vertex<String> VERTEX_J = Vertex.of("J");

    private static final Graph<String> THREE_VERTICES_GRAPH; // todo rename primitive
    private static final Graph<String> FIVE_VERTICES_GRAPH; // todo simple
    private static final Graph<String> UNREACHABLE_VERTEX_GRAPH;
    private static final Graph<String> DISCONNECTED_GRAPH;
    private static final Graph<String> SIMPLE_UNDIRECTED_10_VERTICES_GRAPH;
    static {

        THREE_VERTICES_GRAPH = Graph.directedWeightedGraphBuilder(VERTEX_A)
                .addVertex(VERTEX_B)
                .addVertex(VERTEX_C)
                .addEdge(VERTEX_A, VERTEX_B, 5)
                .addEdge(VERTEX_B, VERTEX_A, 2)
                .addEdge(VERTEX_A, VERTEX_C, 1)
                .addEdge(VERTEX_C, VERTEX_B, 1)
                .build();

        FIVE_VERTICES_GRAPH = Graph.directedWeightedGraphBuilder(VERTEX_E)
                .addVertex(VERTEX_A)
                .addVertex(VERTEX_B)
                .addVertex(VERTEX_C)
                .addVertex(VERTEX_D)
                .addEdge(VERTEX_A, VERTEX_C, 6)
                .addEdge(VERTEX_A, VERTEX_D, 6)
                .addEdge(VERTEX_B, VERTEX_A, 3)
                .addEdge(VERTEX_C, VERTEX_D, 2)
                .addEdge(VERTEX_D, VERTEX_B, 1)
                .addEdge(VERTEX_D, VERTEX_A, 6)
                .addEdge(VERTEX_D, VERTEX_C, 1)
                .addEdge(VERTEX_E, VERTEX_D, 2)
                .addEdge(VERTEX_E, VERTEX_B, 4)
                .build();

        UNREACHABLE_VERTEX_GRAPH = Graph.directedWeightedGraphBuilder(VERTEX_A)
                .addVertex(VERTEX_B)
                .addVertex(VERTEX_C)
                .addVertex(VERTEX_D)
                .addEdge(VERTEX_A, VERTEX_B, 5)
                .addEdge(VERTEX_A, VERTEX_C, 2)
                .addEdge(VERTEX_B, VERTEX_A, 2)
                .addEdge(VERTEX_C, VERTEX_A, 2)
                .addEdge(VERTEX_D, VERTEX_B, 1)
                .addEdge(VERTEX_D, VERTEX_C, 1)
                .build();

        DISCONNECTED_GRAPH = Graph.directedWeightedGraphBuilder(VERTEX_A).addVertex(VERTEX_B).build();

        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH = Graph.undirectedUnweightedGraphBuilder(VERTEX_A)
                .addVertex(VERTEX_B)
                .addVertex(VERTEX_C)
                .addVertex(VERTEX_D)
                .addVertex(VERTEX_D)
                .addVertex(VERTEX_E)
                .addVertex(VERTEX_F)
                .addVertex(VERTEX_G)
                .addVertex(VERTEX_H)
                .addVertex(VERTEX_I)
                .addVertex(VERTEX_J)
                .addEdge(VERTEX_A, VERTEX_B)
                .addEdge(VERTEX_A, VERTEX_D)
                .addEdge(VERTEX_A, VERTEX_E)
                .addEdge(VERTEX_B, VERTEX_C)
                .addEdge(VERTEX_C, VERTEX_D)
                .addEdge(VERTEX_C, VERTEX_H)
                .addEdge(VERTEX_D, VERTEX_F)
                .addEdge(VERTEX_D, VERTEX_I)
                .addEdge(VERTEX_E, VERTEX_F)
                .addEdge(VERTEX_E, VERTEX_G)
                .addEdge(VERTEX_I, VERTEX_J)
                .addEdge(VERTEX_H, VERTEX_J)
                .build();

    }

    @Test
    public void shouldCreateNewGraphWithSingleVertexAndNoEdges() {
        assertEquals("['A']", Graph.directedWeightedGraphBuilder(VERTEX_A).build().toString());
    }

    // todo adding another edge rewrite current

    @Test
    public void shouldCreateNewGraphWithMultipleVerticesAndNoEdges() {
        // when
        var graph = Graph.directedWeightedGraphBuilder(VERTEX_A)
                .addVertex(VERTEX_B)
                .addVertex(VERTEX_C).build();
        // then
        assertEquals("['A' | 'B' | 'C']", graph.toString());
    }

    @Test
    public void shouldReturnAllVertices() {
        assertEquals(new HashSet<>(asList(VERTEX_A, VERTEX_B, VERTEX_C)), THREE_VERTICES_GRAPH.getVertices());
    }

    @Test
    public void shouldReturnAllEdges() {
        assertEquals(new HashSet<>(asList(new Edge<>(VERTEX_B, 5), new Edge<>(VERTEX_C, 1))), THREE_VERTICES_GRAPH.getEdges(VERTEX_A));
    }

    @Test
    public void shouldFailOnAddingEdgeToVertexThatDoNotBelongToTheGraph() {
        assertThrows(IllegalArgumentException.class, () -> Graph.directedWeightedGraphBuilder(VERTEX_A).addVertex(VERTEX_B).addEdge(VERTEX_A, VERTEX_B, -1));
    }

    @Test
    public void shouldFailOnAddingEdgeWithNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> Graph.directedWeightedGraphBuilder(VERTEX_A).addEdge(VERTEX_A, VERTEX_B, -1));
    }

    @Test
    public void shouldCreateNewPrimitiveGraphWithMultipleVerticesAndMultipleEdges() {
        assertEquals("['A'->'B'{5},'C'{1} | 'B'->'A'{2} | 'C'->'B'{1}]", THREE_VERTICES_GRAPH.toString());
    }

    @Test
    public void shouldCreateNewSimpleGraphWithMultipleVerticesAndMultipleEdges() {
        assertEquals(
                "['A'->'C'{6},'D'{6} | 'B'->'A'{3} | 'C'->'D'{2} | 'D'->'A'{6},'B'{1},'C'{1} | 'E'->'B'{4},'D'{2}]",
                FIVE_VERTICES_GRAPH.toString());
    }

    @Test
    public void shouldTraversePrimitiveGraphWithMultipleVerticesAndMultipleEdges() {
        //given
        var builder = new StringBuilder();
        // when
        THREE_VERTICES_GRAPH.traverse(builder::append);
        //then
        assertEquals("ABC", builder.toString());
    }

    @Test
    public void shouldTraverseSimpleGraphWithMultipleVerticesAndMultipleEdges() {
        //given
        var builder = new StringBuilder();
        // when
        FIVE_VERTICES_GRAPH.traverse(builder::append);
        //then
        assertEquals("EBDAC", builder.toString());
    }

    @Test
    public void shouldFailOnTraversingGraphWithNotReachableVertices() {
        assertThrows(IllegalStateException.class, () -> UNREACHABLE_VERTEX_GRAPH.traverse((it) -> {}));
    }

    @Test
    public void shouldFailOnTraversingDisconnectedGraph() {
        assertThrows(IllegalStateException.class, () -> DISCONNECTED_GRAPH.traverse((it) -> {}));
    }

    private static Stream<Arguments> shouldFindPathInUndirectedUnweightedGraph() {
        return Stream.of(
                of(VERTEX_A, VERTEX_J, "ADIJ"),
                of(VERTEX_J, VERTEX_A, "JIDA"),
                of(VERTEX_G, VERTEX_J, "GEFDIJ"),
                of(VERTEX_A, VERTEX_H, "ABCH"),
                of(VERTEX_G, VERTEX_B, "GEAB"),
                of(VERTEX_G, VERTEX_E, "GE"),
                of(VERTEX_G, VERTEX_G, "G"),
                of(VERTEX_B, VERTEX_F, "BCDF"),
                of(VERTEX_A, VERTEX_C, "ABC")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void shouldFindPathInUndirectedUnweightedGraph(Vertex<String> from, Vertex<String> to, String expectedPath) {
        //given
        var builder = new StringBuilder();
        // when
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.findPath(from, to, builder::append);
        //then
        assertEquals(expectedPath, builder.toString());

    }

    private static Stream<Arguments> shouldFindPathInDirectedWeightedGraph() {
        return Stream.of(
                of(VERTEX_E, VERTEX_A, "EBA"),
                of(VERTEX_B, VERTEX_D, "BAD"),
                of(VERTEX_A, VERTEX_B, "ADB")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void shouldFindPathInDirectedWeightedGraph(Vertex<String> from, Vertex<String> to, String expectedPath) {
        //given
        var builder = new StringBuilder();
        // when
        FIVE_VERTICES_GRAPH.findPath(from, to, builder::append);
        //then
        assertEquals(expectedPath, builder.toString());
    }

    private static Stream<Arguments> shouldFailOnFindingPathToNotReachableVertex() {
        return Stream.of(
                of(VERTEX_A, VERTEX_E, "EBA"),
                of(VERTEX_B, VERTEX_E, "EBA"),
                of(VERTEX_C, VERTEX_E, "EBA")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void shouldFailOnFindingPathToNotReachableVertex(Vertex<String> from, Vertex<String> to) {
        assertThrows(IllegalStateException.class, () -> FIVE_VERTICES_GRAPH.findPath(from, to, (value) -> {}));
    }

    @Test
    public void shouldFailOnFindingPathInDisconnectedGraph() {
        assertThrows(IllegalStateException.class, () -> DISCONNECTED_GRAPH.findPath(VERTEX_A, VERTEX_B, (value) -> {}));
    }

    private static Stream<Arguments> shouldFindPathInDirectedWeightedGraph2() { // todo fix name
        return Stream.of(
                of(VERTEX_E, VERTEX_A, "EDBA"),
                of(VERTEX_E, VERTEX_B, "EDB"),
                of(VERTEX_E, VERTEX_C, "EDC"),
                of(VERTEX_A, VERTEX_E, "EDC")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void shouldFindPathInDirectedWeightedGraph2(Vertex<String> from, Vertex<String> to, String expectedPath) {
        //given
        var builder = new StringBuilder();
        // when
        FIVE_VERTICES_GRAPH.findPathByDijkstra(from, to, builder::append);
        //then
        assertEquals(expectedPath, builder.toString());

    }


}