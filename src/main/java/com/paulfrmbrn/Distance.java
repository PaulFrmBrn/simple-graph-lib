package com.paulfrmbrn;

import javax.annotation.Nonnull;
import java.util.Objects;

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
 *
 * todo docuemtn equality and hashing
 * todo document compare
 *
 * @author Dmitry Pavlov
 * @since 31.05.2020
 */
public class Distance<T> implements Comparable<Distance<T>> {

    private final Vertex<T> vertex;
    private int value;

    public Distance(@Nonnull Vertex<T> vertex, int distance) {
        this.vertex = requireNonNull(vertex, "destination");
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

    // todo docuemnt
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
