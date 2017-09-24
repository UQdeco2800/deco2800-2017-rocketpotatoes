package com.deco2800.potatoes.collisions;

public interface CollisionMask {

    /**
     * Makes a copy of the current collision mask.
     *
     * @return
     *          A copy of the current collision mask.
     */
    CollisionMask copy();

    /**
     * Checks if this collision mask overlaps another collision masks.
     * This function is symmetric.
	 * Touching the edge is not considered as overlapping.
	 *
     * @param other
     *              The other collision mask.
     * @return
     *          True iff the two collision masks overlap.
     */
    boolean overlaps(CollisionMask other);

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and another collision mask.
	 * This function is symmetric.
     * 
     * @param other
     *              The other collision mask.
     * @return
     *          The distance. If the collision masks overlap, a negative number is returned.
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
     *          If the line intersects the collision mask, then a negative number is returned. If the line does
     *          not intersect the collision mask, then the number returned should be the length of a perpendicular line
     *          as seen above.
     */
    float distance(float x1, float y1, float x2, float y2);

	/**
	 * Returns the x coordinate at the centre of the mask.
	 * 
	 * @return Returns the x coordinate.
	 */
    float getX();

    /**
     * Sets the x coordiante at the centre of the mask.
     * 
     * @param x
     *             The new x coordinate.
     */
    public void setX(float x);

	/**
	 * Returns the y coordinate at the centre of the mask.
	 * 
	 * @return Returns the y coordinate.
	 */
    public float getY();


	/**
	 * Sets the y coordinate at the centre of the mask.
	 * 
	 * @param y
	 *            The new y coordinate.
	 */
    public void setY(float y);
}
