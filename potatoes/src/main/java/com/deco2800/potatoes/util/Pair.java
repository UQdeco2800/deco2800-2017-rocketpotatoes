package com.deco2800.potatoes.util;

import java.util.Objects;

public class Pair<T> {

    private T first;
    private T second;

    /**
     * Creates a pair.
     */
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second) ^ Objects.hash(second, first);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }

        Pair other = (Pair) o;

        return (first.equals(other.first) && second.equals(other.second)) ||
            (first.equals(other.second) && second.equals(other.first));
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }
}
