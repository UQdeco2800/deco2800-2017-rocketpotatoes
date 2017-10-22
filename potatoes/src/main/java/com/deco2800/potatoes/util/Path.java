package com.deco2800.potatoes.util;

import java.util.ArrayDeque;
import com.deco2800.potatoes.collisions.Point2D;


/**
 * Class to encapsulate calculatePositions as Box3D objects in an entities path to target. Also
 * holds the current angle the entity is traveling in.
 */
public class Path {
    private ArrayDeque<Point2D> nodes;
    private float angle;

    /**
     * Creates a new path, with no nodes.
     */
    public Path() {
        this.nodes = new ArrayDeque<>();
        this.angle = 0;
    }

    /**
     * Creates a new path, with given nodes.
     * @param nodes - List of calculatePositions in path to target.
     */
    public Path(ArrayDeque<Point2D> nodes) {
        this.nodes = nodes;
        this.angle = 0;
    }

    /**
     * Get the deque of nodes.
     * @return nodes as ArrayList of Box3d elements
     */
    public ArrayDeque<Point2D> getNodes() {
        return nodes;
    }

    /**
     * Set a new deque of nodes
     * @param nodes list of calculatePositions in path
     */
    public void setNodes(ArrayDeque<Point2D> nodes) {
        this.nodes = nodes;
    }

    /**
     * Get the current angle of path based on entity trajectory.
     * @return angle in radians
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Set the current angle of path based on entity trajectory.
     * @param angle in radians
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }


    /**
     * Get Box3D of target position calculated between current position
     * and next target node in list.
     * @param currentPosition current Box3D of entity
     * @param speed distance/tick the entity moves
     * @return new Box3D for the entity per tick cycle
     */

    /**
     * Finds the location that the entity following this path should head to next.
     * 
     * @return A copied Box3D representation of the point.
     */
    public Point2D nextPoint() {
        return new Point2D(nodes.getFirst().getX(), nodes.getFirst().getY());
    }

    /**
     * Finds the final goal that this path is following - ie the point that will be reached by the entity following 
     * this path when the path ends.
     * 
     * @return A copied Box3D representation of the point.
     */
    public Point2D goal() {
        return new Point2D(nodes.getLast().getX(), nodes.getLast().getY());
    }

    /**
     * add node to end of path
     * @param node
     */
    public void addNode(Point2D node) {
        nodes.add(node);
    }

    /**
     * Checks if there are any more nodes left on the path
     *
      * @return true iff there are no more nodes in the path
     */
    public boolean isEmpty() { return nodes.isEmpty(); }

    /**
     * Gets the next point and removes it from the que
     *
     * @return A Box3D representation of the point.
     */
    public Point2D pop() {
        return nodes.pop();
    }
}
