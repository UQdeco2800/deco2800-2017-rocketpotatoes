package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.collisions.Line2D;

import com.deco2800.potatoes.worlds.World;

import com.deco2800.potatoes.util.LRUCache;
import com.deco2800.potatoes.util.RTree;
import com.deco2800.potatoes.util.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        World world = GameManager.get().getWorld();

        for (int i = 0; i < NUMBER_OF_RANDOM_NODES; ++i) {
            Point2D first = (Point2D) dots.find(i);
            for (int j = 0; j < NUMBER_OF_RANDOM_NODES; ++j) {
                Point2D second = (Point2D) dots.find(j);
                Line2D line = new Line2D(first, second);
                // check that no entities overlap the line between these paths
                if (!world.getEntitiesOverlapping(line).anyMatch(x -> true)) {
                    graph.put(new Pair(i, j), (float) Math.sqrt(line.getLenSqr()));
                }
            }
        }

    }

    /**
     * Generates a new shortes path tree centered around a given goal.
     */
    private Map<Integer, Integer> generateShortestPathTree(Shape2D goal) {
        Map<Integer, Integer> output = new HashMap<>();
        Map<Integer, Float> toVisit = new HashMap<>();
        World world = GameManager.get().getWorld();

        for (int i = 0; i < NUMBER_OF_RANDOM_NODES; ++i) {
            Point2D centre = new Point2D(goal.getX(), goal.getY());
            Line2D line = new Line2D((Point2D) dots.find(i), centre);
            if (!world.getEntitiesOverlapping(line)
                    .filter(entity -> !entity.getMask().equals(goal))
                    .anyMatch(x -> true)) {
                output.put(i, -1);
                toVisit.put(i, (float) Math.sqrt(line.getLenSqr()));
            } else {
                toVisit.put(i, Float.MAX_VALUE);
            }
        }

        while (toVisit.size() > 0) {
            int next = toVisit
                .keySet()
                .stream()
                .min(Comparator.comparingDouble(id -> toVisit.get(id)))
                .get();

            toVisit.remove(next);

            for (int other: toVisit.keySet()) {
                Float dist = graph.get(new Pair(other, next));
                if (dist != null && dist < toVisit.get(other)) {
                    toVisit.put(other, dist);
                    output.put(other, next);
                }
            }
        }

        return output;
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

        updateGraph();
    }

    /**
     * Find the next point on the path this entity should be following.
     */
    public Point2D requestPath(Shape2D goal, Shape2D self) {
        TreeKey key = new TreeKey(age, goal);
        Optional<Map<Integer, Integer>> maybeTree = trees.get(key);
        Map<Integer, Integer> tree;
        World world = GameManager.get().getWorld();

        if (maybeTree.isPresent()) {
            tree = maybeTree.get();
        } else {
            tree = generateShortestPathTree(goal);
            trees.put(key, tree);
        }

        int nearest = dots.findClosest(self);
        while (true) {
            int next = tree.get(nearest);
            Shape2D nextGoal = next == -1 ? goal : dots.find(next);
            Line2D line = new Line2D(new Point2D(self.getX(), self.getY()),
                    new Point2D(nextGoal.getX(), nextGoal.getY()));
            if (next == -1 || world.getEntitiesOverlapping(line).anyMatch(x -> true)) {
                break;
            }
            else {
                nearest = next;
            }
        }

        return nearest == -1 ? new Point2D(goal.getX(), goal.getY()) : (Point2D) dots.find(nearest);
    }
}
