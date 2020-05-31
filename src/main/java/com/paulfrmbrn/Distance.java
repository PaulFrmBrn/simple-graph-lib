package com.paulfrmbrn;

import javax.annotation.Nonnull;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Represents distance from one vertex to another via one or several edges
 * Stores `to` vertex and distance value
 *
 * {@link Distance#value} can not be negative
 *
 * Instance is mutable
 *
 * Equality, hashing and is based on {@link Distance#vertex} and {@link Distance#value} is ignored
 * Comparision for the instance is based on {@link Distance#value} value only, so {@link Distance#vertex} is ignored
 *
 * @author Dmitry Pavlov
 * @since 31.05.2020
 */
public class Distance<T> implements Comparable<Distance<T>> {

    private final Vertex<T> vertex;
    private int value;

    public Distance(@Nonnull Vertex<T> vertex, int distance) {
        this.vertex = requireNonNull(vertex, "destination");
        if (distance < 0) {
            throw new IllegalArgumentException("distance can not be negative");
        }
        this.value = distance;
    }

    public Vertex<T> getVertex() {
        return vertex;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Distance)) return false;
        Distance<?> distance = (Distance<?>) o;
        return getVertex().equals(distance.getVertex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVertex());
    }

    @Override
    public int compareTo(Distance<T> that) {
        return Integer.compare(this.getValue(), that.getValue());
    }

    @Override
    public String toString() {
        return "'" + vertex +  "'{" + value + "}";
    }
}
