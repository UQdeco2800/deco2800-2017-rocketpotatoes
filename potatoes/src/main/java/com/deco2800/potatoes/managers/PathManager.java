package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;

import java.util.*;


/**
 * Object to manage the creation and allocation of paths for enemies to follow.
 */
public class PathManager extends Manager {
    /* The PathManager stores a minimum spanning tree, representing all the internode connections that can be used to
     * create paths. This is represented as a hashmap in memory, where the keys are nodes, and the values are the
     * parent nodes of the keys within the minimum spanning tree.
     * 
     * This tree is centered around the node storing the player's location for now, though in future that will be 
     * expanded so that multiple different goals can be set.
     */
    private HashMap<Box3D, Box3D> spanningTree;

    /**
     * Updates the internal graph representation of the path manager, based on world state.
     */
    private void updateGraph() {
        // create the graph
        Set<Box3D> vertices = new HashSet<>();
        Map<DoubleBox3D, Float> edges = new HashMap<>();

        // build the minimum spanning tree from the graph - and set the spanningTree variable.

    }

    /**
     * Allocates a path to a given entity. Not guaranteed to be the optimal path, but at the time it is created it will
     * have no collisions. Paths cannot be shared by multiple entities.
     *
     * @param start The starting point of the entity - where the path is going to begin.
     * @param goal The goal of the entity - where the path is going to end.
     * @return The path object itself, which can then be followed.
     */
    public Path generatePath(Box3D start, Box3D goal) {
        // TODO - use the spanningTree to build this path instead.
        ArrayDeque<Box3D> nodes = new ArrayDeque<>();
        nodes.add(start);
        nodes.add(goal);
        return new Path(nodes);
    }

    /**
     * An unordered pair of two 3D boxes. Used as the keys in the mapping from edges to weights in the interal graph 
     * representation. Designed so that DoubleBox3D(A, B) will compare as equal to DoubleBox3D(B, A).
     */
    private class DoubleBox3D {

        // The two points in the pair. The names are used to represent the order in which they were passed as arguments
        // to the constructor.
        private Box3D first;
        private Box3D second;

        /**
         * Creates a new DoubleBox3D with two given points.
         * 
         * @param first One of the points in the pair.
         * @param second The other point in the pair.
         */
        public DoubleBox3D(Box3D first, Box3D second) {
            this.first = new Box3D(first);
            this.second = new Box3D(second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second) ^ Objects.hash(second, first);
        }

        /**
         * Gets the first Box3D of this pair.
         * 
         * @return Returns a copy of the first Box3D.
         */
        public Box3D getFirst() {
            return new Box3D(this.first);
        }

        /**
         * Gets the second Box3D of this pair.
         * 
         * @return 
         */
        public Box3D getSecond() {
            return new Box3D(this.second);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof DoubleBox3D)) {
                return false;
            }

            DoubleBox3D that = (DoubleBox3D) o;

            return (this.getFirst().equals(that.getFirst()) && this.getSecond().equals(that.getSecond())) ||
                    (this.getFirst().equals(that.getSecond()) && this.getSecond().equals(that.getFirst()));
        }
    }
}
