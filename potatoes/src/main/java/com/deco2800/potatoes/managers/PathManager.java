package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.collisions.Line2D;

import com.deco2800.potatoes.worlds.World;

import com.deco2800.potatoes.util.LRUCache;
import com.deco2800.potatoes.util.RTree;
import com.deco2800.potatoes.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Object to manage the allocation of paths for enemies to follow.
 */
public class PathManager extends Manager {

    private int age;
    private RTree<Integer> dots;
    private Map<Pair<Integer>, Float> graph;
    private LRUCache<TreeKey, Map<Integer, Integer>> trees;

    private static int NUMBER_OF_RANDOM_NODES = 100;

    /**
     * Used to determine which shortest-path tree an enemy should follow.
     */
    private class TreeKey {
        private final int age;
        private final Shape2D goal;

        /**
         * Creates a new tree key.
         *
         * @param age
         *      The age of the tree key.
         * @param goal
         *      The target or goal of the tree key.
         */
        public TreeKey(int age, Shape2D goal) {
            this.age = age;
            this.goal = goal;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TreeKey)) {
                return false;
            }

            TreeKey other = (TreeKey) o;
            return age == other.age && goal.equals(other.goal);
        }

        @Override
        public int hashCode() {
            return Objects.hash(age, goal);
        }
    }

    /**
     * Updates the graph, by removing lines that are already in use.
     */
    private void updateGraph() {
        age += 1;
        graph = new HashMap<>();


    }

    public PathManager() {
        age = -1;
        dots = new RTree();
        trees = new LRUCache(10); // TODO -- find a good value for this

        World world = GameManager.get().getWorld();

        for (int i = 0; i < NUMBER_OF_RANDOM_NODES; ++i) {
            dots.insert(i, new Point2D((float) (Math.random() * world.getWidth()),
                        (float) (Math.random() * world.getLength())));
        }
    }

    public Point2D requestPath(Shape2D goal) {
        return new Point2D(goal.getX(), goal.getY());
    }
}
