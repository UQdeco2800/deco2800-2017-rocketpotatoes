package com.deco2800.potatoes.util;

import java.util.ArrayDeque;
import java.util.ArrayList;


/**
 * Class to encapsulate calculatePositions as Box3D objects in an entities path to target. Also
 * holds the current angle the entity is traveling in.
 */
public class Path {
    private ArrayList<Box3D> nodes;
    private float angle;

    /**
     * Creates a new path, with no nodes.
     */
    public Path() {
        this.nodes = new ArrayList<Box3D>();
        this.angle = 0;
    }

    /**
     * Creates a new path, with given nodes.
     * @param nodes - List of calculatePositions in path to target.
     */
    public Path(ArrayList<Box3D> nodes) {
        this.nodes = nodes;
        this.angle = 0;
    }

    /**
     * Get the deque of nodes.
     * @return nodes as ArrayList of Box3d elements
     */
    public ArrayList<Box3D> getNodes() {
        return nodes;
    }

    /**
     * Set a new deque of nodes
     * @param nodes list of calculatePositions in path
     */
    public void setNodes(ArrayList<Box3D> nodes) {
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
    public Box3D getTargetPosition(Box3D currentPosition, float speed) {

        // Construct new Box3D from _currentPosition_
        Box3D newPos = new Box3D(currentPosition);
        // Check if the current position overlaps the first node in list
        if (currentPosition.overlaps(nodes.get(0))) {
            // Remove first node from list as it's been reached
            nodes.remove(0);
            // Set angle to angle between _currentPosition_ and next node
            setAngle(currentPosition.angle(nodes.get(0)));
            // Calculate new x and y calculatePositions
            float newX = (float) (speed * Math.cos(getAngle()));
            float newY = (float) (speed * Math.sin(getAngle()));
            newPos.setX(newX);
            newPos.setY(newY);
            return newPos;
        }
        // Haven't reached the next node in list
        // Set angle to angle between _currentPosition_ and next node
        setAngle(currentPosition.angle(nodes.get(0)));
        // Calculate new x and y calculatePositions
        float newX = (float) (speed * Math.cos(getAngle()));
        float newY = (float) (speed * Math.sin(getAngle()));
        newPos.setX(newX);
        newPos.setY(newY);
        return newPos;
    }

    /**
     * Finds the location that the entity following this path should head to next.
     * 
     * @return A copied Box3D representation of the point.
     */
    public Box3D nextPoint() {
        return nodes.get(0);
    }

    /**
     * Finds the final goal that this path is following - ie the point that will be reached by the entity following 
     * this path when the path ends.
     * 
     * @return A copied Box3D representation of the point.
     */
    public Box3D goal() {
        return new Box3D(nodes.get(nodes.size() - 1));
    }

    /**
     * add node to end of path
     * @param node
     */
    public void addNode(Box3D node) {
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
    public Box3D pop() {
        Box3D box = nodes.get(0);
        nodes.remove(0);
        return box;
    }
}
