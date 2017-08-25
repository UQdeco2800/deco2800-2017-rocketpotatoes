package com.deco2800.potatoes.util;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Deque;
import java.util.Queue;

/**
 *
 */
public class Path {
    private Deque<Box3D> nodes;

    /**
     * Creates a new path, with given nodes.
     * 
     * @param nodes The nodes the created path will consist of.
     */
    public Path(List<Box3D> nodes) {
        this.nodes = new ArrayDeque<>();
        this.nodes.addAll(nodes);
    }

    /**
     * Creates a new path, with given nodes.
     * 
     * @param nodes The nodes the created path will consist of.
     */
    public Path(Queue<Box3D> nodes) {
        this.nodes = new ArrayDeque<>();
        this.nodes.addAll(nodes);
    }

    /**
     * Finds the location that the entity following this path should head to next.
     * 
     * @return A copied Box3D representation of the point.
     */
    public Box3D nextPoint() {
        return new Box3D(nodes.getFirst());
    }

    /**
     * Finds the final goal that this path is following - ie the point that will be reached by the entity following 
     * this path when the path ends.
     * 
     * @return A copied Box3D representation of the point.
     */
    public Box3D goal() {
        return new Box3D(nodes.getLast());
    }

}
