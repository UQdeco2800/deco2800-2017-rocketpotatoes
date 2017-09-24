package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.worlds.AbstractWorld;

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
    private Map<Point2D, Point2D> spanningTree;
    private Set<Point2D> nodes = new HashSet<>();
    private Map<DoublePoint2D, Float> edges = new HashMap<>();
    private Map<Point2D, Boolean> directNode = new HashMap<>(); //nodes which have a direct line of sight
    private Point2D lastPlayerPosition;


    /**
     *
     */
    public PathManager() {
        spanningTree = new HashMap<>();
        nodes = new HashSet<>();
        edges = new HashMap<>();
    }



    private static float nodeOffset = (float) 0.5;

    /**
     * Populates the internal graph representation of the path manager, based on the initial world state.
     * Should be run after loading the map
     */
    public void initialise(CollisionMask player) {
        this.lastPlayerPosition = new Point2D(player.getX(), player.getY());
        nodes.clear();
        edges.clear();

        AbstractWorld world = GameManager.get().getWorld();

        nodes.add(lastPlayerPosition);

        //add points in corners of map
        nodes.add(new Point2D(0, 0));
        nodes.add(new Point2D(world.getWidth(), 0));
        nodes.add(new Point2D(0, world.getLength()));
        nodes.add(new Point2D(world.getWidth(), world.getLength()));

        // this is where we would put nodes on the corners of our static entities -- if they had corners

        // make random nodes here
        Random rng = new Random();
        for (int i = 0; i < 1000; ++i) {
            nodes.add(new Point2D(rng.nextFloat() * world.getWidth(), rng.nextFloat() * world.getLength()));
        }

        //loop through all nodes and all entities, removing any nodes that intersect with the entity
        Set<Point2D> removedNodes = new HashSet<>();
        for (Point2D node : this.nodes) {
            for (AbstractEntity entity : world.getEntities().values()) {
                if (entity.isStaticCollideable() && entity.getMask().overlaps(node)) {
                    removedNodes.add(node);
                }
            }
        }

        for (Point2D node : removedNodes) {
            this.nodes.remove(node);
        }


        //loop through every combination of 2 nodes & every entity check if the edge between the two nodes is valid
        boolean doesCollide;
        float dist;
        for (Point2D node1 : this.nodes) {
            for (Point2D node2 : this.nodes) {
                if (node1 == node2) { break; }
                doesCollide = false;
                for (AbstractEntity entity : world.getEntities().values()) {
                    if (entity.isStaticCollideable() &&
                            entity.getMask().distance(node1.getX(), node1.getY(), node2.getX(), node2.getY()) < 0) {
                        doesCollide = true;
                        break;
                    }
                }
                if(!doesCollide) {
                    dist = node1.distance(node2);
                    this.edges.put(new DoublePoint2D(node1, node2), dist);
                    this.edges.put(new DoublePoint2D(node2, node1), dist);
                }
            }
        }

        // build the minimum spanning tree from the graph - and set the spanningTree variable.
        optimiseGraph(lastPlayerPosition, nodes, edges);
    }

    /**
     * To be run on every game tick, the pathManager needs to know the location of the player
     * It set the path of sqirrels on the map that have run into a wall //TODO
     *
     * @param player coordinates of the player
     */
    public void onTick(CollisionMask player) {
        // AbstractWorld world = GameManager.get().getWorld();

        // //if player hasn't moved since last tick, can skip this
        // if (!player.equals(lastPlayerPosition)) {
        //     lastPlayerPosition = player;

        //     //populates directNode, nodes which have a direct line of sight
        //     boolean doesCollide;
        //     for (Box3D node : this.nodes) {
        //         doesCollide = false;
        //         for (AbstractEntity entity : world.getEntities().values()) {
        //             if (entity.isStaticCollideable() &&
        //                     entity.getBox3D().doesIntersectLine(node.getX(), node.getY(), 0, player.getX(), player.getY(), 0)) {
        //                 doesCollide = true;
        //                 break;
        //             }
        //         }
        //         if (!doesCollide) {
        //             this.directNode.put(node, true);
        //         }
        //     }
        // }

    }

    /**
     * Takes as inputs a representation of the graph of internode connections that can be used to create paths.
     * Calculates the minimum spanning tree of the graph, and then stores this internally so it can be used to generate
     * new paths for enemies.
     *
     * @param start The initial vertex within the graph where the search starts, and where each generated path will end.
     * @param vertices The vertices of the graph of internode connections.
     * @param edges The edges of the graph of internode connections.
     */
    private void optimiseGraph(Point2D start, Set<Point2D> vertices, Map<DoublePoint2D, Float> edges) {
        List<Point2D> workQueue = new ArrayList<>();
        Map<Point2D, Float> distances = new HashMap<>();

        spanningTree.clear();
        workQueue.add(start);
        distances.put(start, new Float(0));

        while (workQueue.size() > 0) {
            // find the closest thing within the work queue
            Point2D current = workQueue.get(0);
            for (Point2D other : workQueue) {
                if (distances.get(other) < distances.get(current)) {
                    current = other;
                }
            }
            vertices.remove(current);
            workQueue.remove(current);

            // TODO make this more efficient by improving the way we store the graph
            for (Point2D other : vertices) {

                DoublePoint2D pair = new DoublePoint2D(current, other);

                if (!edges.containsKey(pair)) {
                    continue;
                }

                Float distance = edges.get(pair);

                if (!distances.containsKey(other) || distances.get(other) > distance) {
                    spanningTree.put(other, current);
                    distances.put(other, distance);
                }

                if (!workQueue.contains(other)) {
                    workQueue.add(other);
                }
            }

        }



    }

    /**
     * Allocates a path to a given entity. Not guaranteed to be the optimal path, but at the time it is created it will
     * have no collisions. Paths cannot be shared by multiple entities.
     *
     * @param start The starting point of the entity - where the path is going to begin.
     * @param goal The goal of the entity - where the path is going to end.
     * @return The path object itself, which can then be followed.
     */
    public Path generatePath(CollisionMask start, CollisionMask goal) {
        ArrayDeque<Point2D> nodes = new ArrayDeque<>();
        Point2D newGoal = new Point2D(goal.getX(), goal.getY());
        if (spanningTree.size() == 0 || !lastPlayerPosition.equals(newGoal)) {
            initialise(goal);
        }
        nodes.add(new Point2D(start.getX(), start.getY()));
        if (spanningTree.size() == 0) {
            nodes.add(newGoal);
            return new Path(nodes);
        }
        Point2D closest = null;
        for (Point2D other : spanningTree.keySet()) {
            if (closest == null || closest.distance(start) > other.distance(start)) {
                closest = other;
            }
        }
        if (closest.distance(start) > goal.distance(start)) {
            closest = newGoal;
        }
        do {
            nodes.add(closest);
            closest = spanningTree.get(closest);
        } while (closest != null);
        return new Path(nodes);
    }


    /**
     * An unordered pair of two 3D boxes. Used as the keys in the mapping from edges to weights in the interal graph
     * representation. Designed so that DoubleBox3D(A, B) will compare as equal to DoubleBox3D(B, A).
     */
    private class DoublePoint2D {

        // The two points in the pair. The names are used to represent the order in which they were passed as arguments
        // to the constructor.
        private Point2D first;
        private Point2D second;

        /**
         * Creates a new DoubleBox3D with two given points.
         *
         * @param first One of the points in the pair.
         * @param second The other point in the pair.
         */
        public DoublePoint2D(Point2D first, Point2D second) {
            this.first = first;
            this.second = second;
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
        public Point2D getFirst() {
            return new Point2D(first.getX(), first.getY());
        }

        /**
         * Gets the second Box3D of this pair.
         *
         * @return Returns a copy of the second Box3D.
         */
        public Point2D getSecond() {
            return new Point2D(second.getX(), second.getY());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof DoublePoint2D)) {
                return false;
            }

            DoublePoint2D that = (DoublePoint2D) o;

            return (this.getFirst().equals(that.getFirst()) && this.getSecond().equals(that.getSecond())) ||
                    (this.getFirst().equals(that.getSecond()) && this.getSecond().equals(that.getFirst()));
        }
    }
}
