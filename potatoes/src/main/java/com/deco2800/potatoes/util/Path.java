package com.deco2800.potatoes.util;

import java.util.ArrayList;

/**
 * Class to encapsulate positions as Box3D objects in an entities path to target. Also
 * holds the current angle the entity is traveling in.
 */
public class Path {

    private ArrayList<Box3D> nodes;
    private float angle;

    /**
     * @param nodes - List of positions in path to target.
     */
    public Path(ArrayList<Box3D> nodes) {
        this.nodes = nodes;
        this.angle = 0;

    }

    /**
     * Get the current list of nodes.
     * @return nodes as ArrayList of Box3d elements
     */
    public ArrayList<Box3D> getNodes() {
        return nodes;
    }

    /**
     * Set a new list of nodes
     * @param nodes list of positions in path
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
            // Calculate new x and y positions
            float newX = (float)(speed * Math.cos(getAngle()));
            float newY = (float)(speed * Math.sin(getAngle()));
            newPos.setX(newX);
            newPos.setY(newY);
            return newPos;
        }
        // Haven't reached the next node in list
        // Set angle to angle between _currentPosition_ and next node
        setAngle(currentPosition.angle(nodes.get(0)));
        // Calculate new x and y positions
        float newX = (float)(speed * Math.cos(getAngle()));
        float newY = (float)(speed * Math.sin(getAngle()));
        newPos.setX(newX);
        newPos.setY(newY);
        return newPos;
    }

}
