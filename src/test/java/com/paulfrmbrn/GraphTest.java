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

        // todo move to private static fabrics
        THREE_VERTICES_GRAPH = new Graph<>(VERTEX_A);
        THREE_VERTICES_GRAPH.addVertex(VERTEX_B);
        THREE_VERTICES_GRAPH.addVertex(VERTEX_C);
        THREE_VERTICES_GRAPH.addEdge(VERTEX_A, VERTEX_B, 5);
        THREE_VERTICES_GRAPH.addEdge(VERTEX_B, VERTEX_A, 2);
        THREE_VERTICES_GRAPH.addEdge(VERTEX_A, VERTEX_C, 1);
        THREE_VERTICES_GRAPH.addEdge(VERTEX_C, VERTEX_B, 1);

        FIVE_VERTICES_GRAPH = new Graph<>(VERTEX_E);
        FIVE_VERTICES_GRAPH.addVertex(VERTEX_A);
        FIVE_VERTICES_GRAPH.addVertex(VERTEX_B);
        FIVE_VERTICES_GRAPH.addVertex(VERTEX_C);
        FIVE_VERTICES_GRAPH.addVertex(VERTEX_D);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_A, VERTEX_C, 6);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_A, VERTEX_D, 6);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_B, VERTEX_A, 3);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_C, VERTEX_D, 2);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_D, VERTEX_B, 1);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_D, VERTEX_A, 6);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_D, VERTEX_C, 1);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_E, VERTEX_D, 2);
        FIVE_VERTICES_GRAPH.addEdge(VERTEX_E, VERTEX_B, 4);

        UNREACHABLE_VERTEX_GRAPH = new Graph<>(VERTEX_A);
        UNREACHABLE_VERTEX_GRAPH.addVertex(VERTEX_B);
        UNREACHABLE_VERTEX_GRAPH.addVertex(VERTEX_C);
        UNREACHABLE_VERTEX_GRAPH.addVertex(VERTEX_D);
        UNREACHABLE_VERTEX_GRAPH.addEdge(VERTEX_A, VERTEX_B, 5);
        UNREACHABLE_VERTEX_GRAPH.addEdge(VERTEX_A, VERTEX_C, 2);
        UNREACHABLE_VERTEX_GRAPH.addEdge(VERTEX_B, VERTEX_A, 2);
        UNREACHABLE_VERTEX_GRAPH.addEdge(VERTEX_C, VERTEX_A, 2);
        UNREACHABLE_VERTEX_GRAPH.addEdge(VERTEX_D, VERTEX_B, 1);
        UNREACHABLE_VERTEX_GRAPH.addEdge(VERTEX_D, VERTEX_C, 1);

        DISCONNECTED_GRAPH = new Graph<>(VERTEX_A);
        DISCONNECTED_GRAPH.addVertex(VERTEX_B);

        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH = new Graph<>(VERTEX_A);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_B);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_C);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_D);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_D);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_E);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_F);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_G);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_H);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_I);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addVertex(VERTEX_J);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_A, VERTEX_B, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_A, VERTEX_D, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_A, VERTEX_E, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_B, VERTEX_C, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_C, VERTEX_D, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_C, VERTEX_H, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_D, VERTEX_F, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_D, VERTEX_I, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_E, VERTEX_F, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_E, VERTEX_G, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_I, VERTEX_J, 1);
        SIMPLE_UNDIRECTED_10_VERTICES_GRAPH.addUndirectedEdge(VERTEX_H, VERTEX_J, 1);

    }

    @Test
    public void shouldCreateNewGraphWithSingleVertexAndNoEdges() {
        assertEquals("['A']", new Graph<>(VERTEX_A).toString());
    }

    // todo adding another edge rewrite current

    @Test
    public void shouldCreateNewGraphWithMultipleVerticesAndNoEdges() {
        // when
        var graph = new Graph<>(VERTEX_A);
        graph.addVertex(VERTEX_B);
        graph.addVertex(VERTEX_C);
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
        // given
        var graph = new Graph<>(VERTEX_A);
        graph.addVertex(VERTEX_B);
        //then
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(VERTEX_A, VERTEX_B, -1));
    }

    @Test
    public void shouldFailOnAddingEdgeWithNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new Graph<>(VERTEX_A).addEdge(VERTEX_A, VERTEX_B, -1));
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
    
}