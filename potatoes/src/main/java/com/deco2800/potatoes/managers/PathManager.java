package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Line;
import com.deco2800.potatoes.util.MinimumSpanningTree;
import com.deco2800.potatoes.util.MinimumSpanningTree.Vertex;
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
    private Map<Box3D, Box3D> spanningTree;
    private MinimumSpanningTree treeMaker;
    AbstractWorld world;
    private ArrayList<Box3D> nodes = new ArrayList<>();
    private ArrayList<Line> obstacles = new ArrayList<>();
    private int numberOfRandomNodes = 200;


    /**
     *
     */
    public PathManager() {
        spanningTree = new HashMap<>();
        nodes = new ArrayList<>();
    }


    private static float nodeOffset = (float) 0.5;

    /**
     * Populates the internal graph representation of the path manager, based on the initial world state.
     * Should be run after loading the map
     */
    public void initialise(Box3D player) {

        nodes.clear();
        world = GameManager.get().getWorld();
        //
        obstacles = createObstacleLines();

        // Initalise random points on the map to make up vertices of
        // the graph.
        for (int i = 0; i < numberOfRandomNodes; i++) {
            nodes.add(new Box3D(
                    (float) (Math.random() * world.getWidth()),      // x coordinate
                    (float) (Math.random() * world.getLength()),     // y coordinate
                    0,
                    1,
                    1,
                    1
            ));
        }


        // create a new mini spanning tree
        treeMaker = new MinimumSpanningTree(nodes.size());
        // Add the nodes to the vertexList.
        for (int i = 0; i < nodes.size(); i++) {
            treeMaker.addVertex(nodes.get(i), i);
        }

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
        ArrayDeque<Box3D> path = new ArrayDeque<>();
        Vertex startVertex;
        Vertex goalVertex;
        Box3D next;

        if (spanningTree.size() == 0) {
            initialise(goal);
        }
        // Find the closest Vertices to the start and goal points.
        startVertex = treeMaker.findClosest(start);
        goalVertex = treeMaker.findClosest(goal);
        // build the minimum spanning tree from the graph - and set the spanningTree variable
        spanningTree = treeMaker.createTree(goalVertex);
        // Add the starting point to the path.
        path.add(startVertex.getEntry());
        // If the spanning tree has only two entries
        // return a new path with the start and end point.
        if (spanningTree.size() < 2) {
            path.add(goalVertex.getEntry());
            return new Path(path);
        }
        // Add extra path points as needed.
        // Set next as the value returned from start as
        // the key to spanningTree.
        next = spanningTree.get(startVertex.getEntry());
        while (!(next.equals(goalVertex.getEntry()))) {
            path.add(next);
            next = spanningTree.get(next);
        }
        return new Path(path);
    }

    /**
     * Create line objects that represent the boarder of all {@code StaticCollidable}
     * entities on the map.
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