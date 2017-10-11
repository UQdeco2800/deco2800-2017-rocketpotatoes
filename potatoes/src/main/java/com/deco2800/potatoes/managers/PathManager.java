package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Line;
import com.deco2800.potatoes.util.MinimumSpanningTree;
import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.worlds.World;



import java.util.*;



/**
 * Object to manage the creation and allocation of paths for enemies to follow.
 */
public class PathManager extends Manager implements ForWorld {
    /* The PathManager stores a minimum spanning tree, representing all the internode connections that can be used to
     * create paths. This is represented as a hashmap in memory, where the keys are nodes, and the values are the
     * parent nodes of the keys within the minimum spanning tree.
     *
     * This tree is centered around the node storing the player's location for now, though in future that will be
     * expanded so that multiple different goals can be set.
     */
    private Map<Point2D, Point2D> spanningTree;
    private MinimumSpanningTree treeMaker;
    private ArrayDeque<Point2D> nodes;
    private ArrayDeque<Point2D> path;
    private static final int NUMBER_OF_RANDOM_NODES = 100;


    /**
     * Basic constructor.
     */
    public PathManager() {
        spanningTree = new HashMap<>();
        nodes = new ArrayDeque<>();
        path = new ArrayDeque<>();
    }

    /**
     * Populates the internal graph representation of the path manager, based on the initial world state.
     * Should be run after loading the map
     */

    public void initialise() {

        World world = GameManager.get().getWorld();

        nodes.clear();
        // Add place holder nodes at positions 0 and 1
        // so that the player position and enemy position
        // can be added later.
        nodes.add(new Point2D(0, 0));   // Position 0 => player position.
        nodes.add(new Point2D(0, 0));   // Position 1 => enemy position.

        // Create obstacles from static entities.
        // Initialise random points on the map to add as vertices of
        // the graph.
        for (int i = 0; i < NUMBER_OF_RANDOM_NODES; i++) {
            nodes.add(new Point2D((float) (Math.random() * world.getWidth()), 
                        (float) (Math.random() * world.getLength())));
        }

        // Create a new minimum spanning tree
        treeMaker = new MinimumSpanningTree(nodes.size());
        // Add the nodes to the vertexList.
        int i = 0;
        for (Point2D node: nodes) {
            treeMaker.addVertex(node, i++);
        }

        // Calculate edge weights in graph matrix
        // based on static enemies.
        treeMaker.initGraphWeightMatrix();
    }

    /**
     * Allocates a path to a given entity. Not guaranteed to be the optimal path, but at the time it is created it will
     * have no collisions. Paths cannot be shared by multiple entities.
     *
     * @param start The starting point of the entity - where the path is going to begin.
     * @param goal  The goal of the entity - where the path is going to end.
     * @return The path object itself, which can then be followed.
     */
    public Path generatePath(Shape2D start, Shape2D goal) {

        Point2D replaceStart = new Point2D(start.getX(), start.getY());
        Point2D replaceGoal = new Point2D(goal.getX(), goal.getY());

        path.clear();
        Point2D next;
        // Create line between start and goal.
        Line line = new Line(start.getX(), start.getY(), goal.getX(), goal.getY());
        // Check if this line has a clear path.
        if (!collides(line)) {
            // line is not obstructed.
            //path.add(replaceStart);
            path.add(replaceGoal);
            return new Path(path);
        }
        // Check if the spanning tree has been initialise.
        if (spanningTree.size() == 0) {
            initialise();
        }
        // build the minimum spanning tree from the graph - and set the spanningTree variable
        spanningTree = treeMaker.createTree(replaceGoal, replaceStart);
        // Add the starting point to the path.

        // Add extra path points as needed.
        // Set next as the value returned from start as
        // the key to spanningTree.
        next = spanningTree.get(start);
        while (next != null && !next.equals(goal)) {
            path.add(next);
            next = spanningTree.get(next);
        }
        path.add(replaceGoal);
        return new Path(path);
    }

    private boolean collides(Line line) {
        boolean output = false;

        for (AbstractEntity e : GameManager.get().getWorld().getEntities().values()) {
            if (e.isStatic() &&
                    0 > e.getMask().distance(line.getEndPointOne().getX(), line.getEndPointOne().getY(), 
                        line.getEndPointTwo().getX(), line.getEndPointTwo().getY())) {
                output = true;
                break;
            }
        }

        return output;
    }
}
