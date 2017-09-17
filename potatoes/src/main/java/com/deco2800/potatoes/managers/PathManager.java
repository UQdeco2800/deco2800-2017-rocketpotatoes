package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Line;
import com.deco2800.potatoes.util.MinimumSpanningTree;
import com.deco2800.potatoes.util.Path;
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
    private Map<Box3D, Box3D> spanningTree;
    private MinimumSpanningTree treeMaker;
    private World world;
    private ArrayList<Box3D> nodes;
    private ArrayList<Line> obstacles;
    private ArrayDeque<Box3D> path;
    private static final int NUMBER_OF_RANDOM_NODES = 5;
    private static final Box3D dummyBox = new Box3D(0f,0f,0f,1f,1f,1f);


    /**
     * Basic constructor.
     */
    public PathManager() {
        spanningTree = new HashMap<>();
        nodes = new ArrayList<>();
        path = new ArrayDeque<>();
        world = GameManager.get().getWorld();
    }

    /**
     * Populates the internal graph representation of the path manager, based on the initial world state.
     * Should be run after loading the map
     */

    public void initialise() {


        nodes.clear();
        // Add place holder nodes at positions 0 and 1
        // so that the player position and enemy position
        // can be added later.
        nodes.add(new Box3D(dummyBox));     // Position 0 => player position.
        nodes.add(new Box3D(dummyBox));     // Position 1 => enemy position.

        // Create obstacles from static entities.
        obstacles = createObstacleLines();

        // Initialise random points on the map to add as vertices of
        // the graph.
        for (int i = 0; i < NUMBER_OF_RANDOM_NODES; i++) {
            nodes.add(new Box3D(
                    (float) (Math.random() * world.getWidth()),      // x coordinate
                    (float) (Math.random() * world.getLength()),     // y coordinate
                    0,
                    1,
                    1,
                    1
            ));
        }

        // Create a new minimum spanning tree
        treeMaker = new MinimumSpanningTree(nodes.size());
        // Add the nodes to the vertexList.
        for (int i = 0; i < nodes.size(); i++) {
            treeMaker.addVertex(nodes.get(i), i);
        }

        // Calculate edge weights in graph matrix
        // based on static enemies.
        treeMaker.initGraphWeightMatrix(obstacles);
    }

    /**
     * Allocates a path to a given entity. Not guaranteed to be the optimal path, but at the time it is created it will
     * have no collisions. Paths cannot be shared by multiple entities.
     *
     * @param start The starting point of the entity - where the path is going to begin.
     * @param goal  The goal of the entity - where the path is going to end.
     * @return The path object itself, which can then be followed.
     */
    public Path generatePath(Box3D start, Box3D goal) {

        path.clear();
        Box3D next;
        // Create line between start and goal.
        Line line = new Line(start, goal);
        // Check if the spanning tree has been initialise.
        if (spanningTree.size() == 0) {
            initialise();
        }
        // Check if this line has a clear path.
        if(!treeMaker.checkLineClash(line, obstacles)) {
            // line is not obstructed.
            path.add(start);
            path.add(goal);
            return new Path(path);
        }
        // build the minimum spanning tree from the graph - and set the spanningTree variable
        spanningTree = treeMaker.createTree(goal, start, obstacles);
        // Add the starting point to the path.
        path.add(start);
        // If the spanning tree has only two entries
        // return a new path with the start and end point.
        if (spanningTree.size() < 2) {
            path.add(goal);
            return new Path(path);
        }
        // Add extra path points as needed.
        // Set next as the value returned from start as
        // the key to spanningTree.
        next = spanningTree.get(start);
        while (!(next.equals(goal))) {
            path.add(next);
            next = spanningTree.get(next);
        }
        path.add(next);
        return new Path(path);
    }

    /**
     * Create line objects that represent the boarder of all static collidable entities on the map.
     *
     * @return List of {@code Line} objects that represent static entity boarders.
     */
    private ArrayList<Line> createObstacleLines() {

        // Create an empty Line list.
        ArrayList<Line> lineList = new ArrayList<>();
     
        // Loop through static entities and make lines for
        // the top, bottom, left and right boarders.
        for (AbstractEntity e : world.getEntities().values()) {
            if (e.isStaticCollideable()) {
                // Iterate through
                // Position = Top => Bottom => Left => Right.
                for (Line.Position p: Line.Position.values()) {
                    lineList.add(new Line(e.getBox3D(), p));
                }
            }
        }
        return lineList;
    }

}