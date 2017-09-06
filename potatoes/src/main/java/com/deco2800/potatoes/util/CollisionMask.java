package com.deco2800.potatoes.util;

public interface CollisionMask {

    /**
     * Checks if this collision mask overlaps another collision masks.
     * 
     * @param other
     *              The other collision mask.
     * @return
     *          True if the two collision masks overlap, otherwise false.
     */
    boolean overlaps(CollisionMask other);

    /**
     * Finds the minimum edge-to-edge distance between this collision mask and another collision mask.
     * 
     * @param other
     *              The other collision mask.
     * @return
     *          The distance. If the two colision masks overlap, a negative number should be returned.
     */
    float distance(CollisionMask other);

    /**
     * Finds the minimum perpendicular distance between a straight line and this collision mask. This is used primarily 
     * in path finding to see if an entity can walk past an object without colliding with it.
     * 
     * For visual examples, see the relevant wiki page.
     * 
     * @param x1
     *              The starting X coordinate of the line being checked.
     * @param y1
     *              The starting Y coordinate of the line being checked.
     * @param x2
     *              The ending X coordinate of the line being checked.
     * @param y2
     *              The ending Y coordinate of the line being checked.
     * @return
     *          If the line intersects the collision mask, then a negative number should be returned. If the line does
     *          not intersect the collision mask, then the number returned should be the length of a perpendicular line
     *          as seen above.
     */
    float distance(float x1, float y1, float x2, float y2);
}