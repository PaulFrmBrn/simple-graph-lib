package com.paulfrmbrn;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Represents edge of the {@link Graph}
 * Stores `to` vertex and edge weight.
 *
 * {@link Edge#weight} can not be negative
 *
 * Equality, hashing and comparision for the instance is based on {@link Edge#vertex} value only, so {@link Edge#weight} is ignored
 *
 * @author Dmitry Pavlov
 * @since 30.05.2020
 */
@Immutable
class Edge<T> implements Comparable<Edge<T>> {

    private final Vertex<T> vertex;
    private final int weight;

    Edge(@Nonnull Vertex<T> vertex, int weight) {
        this.vertex = requireNonNull(vertex, "vertex");
        if (weight < 0) {
            throw new IllegalArgumentException("Weight should not be negative");
        }
        this.weight = weight;
    }

    @Nonnull
    public Vertex<T> getVertex() {
        return vertex;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?> edge = (Edge<?>) o;
        return getVertex().equals(edge.getVertex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVertex());
    }

    @Override
    public int compareTo(Edge<T> that) {
        return (this.getVertex()).compareTo(that.getVertex());
    }

    @Override
    public String toString() {
        return "'" + vertex +  "'{" + weight + "}";
    }
}
