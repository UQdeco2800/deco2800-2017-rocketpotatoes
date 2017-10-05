package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.collisions.Line2D;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.worlds.World;

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

    //private Map< AbstractEntity, Map<Point2D, Point2D>> spanningPaths = new HashMap<>(); TODO allow targets
    private Map<Point2D, Point2D> spanningPaths = new HashMap<>();
    // for each target there is a spanning tree of nodes, showing the shortest path to that target

    private Set<Point2D> nodes = new HashSet<>();               // all nodes of the graph
    private Map<Point2D, Boolean> directNode = new HashMap<>(); // nodes which have a direct line of sight
    private Map<Point2D, Float> nodeCost = new HashMap<>();     // the distance to the target from this node

    private Set<Line2D> edges = new HashSet<>();                // all the edges of the graph
    private Map<Point2D, Line2D> nodeEdges = new HashMap<>();       // the edges that each point is a part of
    private Map<Line2D, Float> edgeCost = new HashMap<>();      // the cost of the edge (at the moment distance^2)
    //TODO edge width?


    private static final int NUMBER_OF_RANDOM_NODES = 20;
    private static float nodeOffset = (float) 0.5;

    /**
     */
    public PathManager() {
        // Hi sonar!
    }



    public Set<Point2D> getNodes() {
        return nodes;
    }

    public Set<Line2D> getEdges() {
        return edges;
    }

    /**
     * Populates the internal graph representation of the path manager, based on the initial world state.
     * Should be run after loading the map
     */
    public void initialise() {
        if (! nodes.isEmpty()) return;

        nodes.clear();
        edges.clear();
        //TODO clear the other stuff

        World world = GameManager.get().getWorld();


        //add points in corners of map
        nodes.add(new Point2D(0, 0));
        nodes.add(new Point2D(world.getWidth(), 0));
        nodes.add(new Point2D(0, world.getLength()));
        nodes.add(new Point2D(world.getWidth(), world.getLength()));

        // this is where we would put nodes on the corners of our static entities -- if they had corners

        // Initialise random points on the map to add as vertices of
        // the graph.
        for (int i = 0; i < NUMBER_OF_RANDOM_NODES; i++) {
            nodes.add(new Point2D((float) (Math.random() * world.getWidth()),
                    (float) (Math.random() * world.getLength())));
        }

        //loop through all nodes and all entities, removing any nodes that intersect with the entity
        Set<Point2D> removedNodes = new HashSet<>();
        for (Point2D node : this.nodes) {
            for (AbstractEntity entity : world.getEntities().values()) {
                if (entity.isStatic() && entity.isSolid() && entity.getMask().overlaps(node)) {
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
            for (Point2D node2 : this.nodes) {      // TODO is comparing every combination twice

                if (node1 == node2) { break; }

                Line2D edge = new Line2D(node1, node2);

                doesCollide = false;
                for (AbstractEntity entity : world.getEntities().values()) {        //TODO use obstacles instead
                    if (entity.isStatic() && entity.isSolid() &&
                            edge.overlaps(entity.getMask())) {
                        doesCollide = true;
                        break;
                    }
                }

                if(!doesCollide) {

                    float distSqr = edge.getLenSqr();

                    this.edges.add(edge);
                    this.edgeCost.put(edge, distSqr);
                }
            }
        }

        // build the minimum spanning tree from the graph - and set the spanningTree variable.
        //optimiseGraph(lastPlayerPosition, nodes, edges);
    }


    public void onTick() {
        /*
        // AbstractWorld world = GameManager.get().getWorld();


         //if player hasn't moved since last tick, can skip this
         if (!player.equals(lastPlayerPosition)) {
             lastPlayerPosition = player;

             //populates directNode, nodes which have a direct line of sight
             boolean doesCollide;
             for (Box3D node : this.nodes) {
                 doesCollide = false;
                 for (AbstractEntity entity : world.getEntities().values()) {
                     if (entity.isStaticCollideable() &&
                            entity.getBox3D().doesIntersectLine(node.getX(), node.getY(), 0, player.getX(), player.getY(), 0)) {
                         doesCollide = true;
                         break;
                     }
                 }
                 if (!doesCollide) {
                     this.directNode.put(node, true);
                 }
             }
         }
        */
    }

    /**
     * Takes as inputs a representation of the graph of internode connections that can be used to create paths.
     * Calculates the minimum spanning tree of the graph, and then stores this internally so it can be used to generate
     * new paths for enemies.
     *
     * param start The initial vertex within the graph where the search starts, and where each generated path will end.
     * param vertices The vertices of the graph of internode connections.
     * param edges The edges of the graph of internode connections.
     */
    private void optimiseGraph() {          //Point2D start, Set<Point2D> vertices, Map<DoublePoint2D, Float> edges) {
        /*List<Point2D> workQueue = new ArrayList<>();
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

        */

    }

    //TODO
    public Shape2D getTargetNode(Squirrel squirrel, AbstractEntity target, Shape2D targetNode) {

        // if straight line to target (clear of static solid obstacles)
            // return target.collisionMask()

        // if targetNode reached (overlapping or centred on)
            // go to next node
            // return spanningTree.get(targetNode);

        // if straight line to targetNode (clear of static solid obstacles)
        // (should implicitly be true if no obstacles have been added)
            // targetNode is still valid
            return targetNode;

        // else
            // find new path
    }



    /*
    /**
     * An unordered pair of two 3D boxes. Used as the keys in the mapping from edges to weights in the interal graph
     * representation. Designed so that DoubleBox3D(A, B) will compare as equal to DoubleBox3D(B, A).
     * /
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
         * /
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
         * /
        public Point2D getFirst() {
            return new Point2D(first.getX(), first.getY());
        }

        /**
         * Gets the second Box3D of this pair.
         *
         * @return Returns a copy of the second Box3D.
         * /
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
    }*/
}
