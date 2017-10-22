package com.deco2800.potatoes.collisions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Shape2D {

	// The centre of the shape
	float x;
	float y;

	/**
	 * Returns the x coordinate at the centre of the mask.
	 *
	 * @return Returns the x coordinate.
	 */
	public float getX() {
		return this.x;
	}

	/**
	 * Sets the x coordiante at the centre of the mask.
	 *
	 * @param x
	 *             The new x coordinate.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the y coordinate at the centre of the mask.
	 *
	 * @return Returns the y coordinate.
	 */
	public float getY() {
		return this.y;
	}

	/**
	 * Sets the y coordinate at the centre of the mask.
	 *
	 * @param y
	 *            The new y coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return A Point2D at the centre of this shape
	 */
	public Point2D getCentre() {
		return new Point2D(x,y);
	}

	/**
	 * Gets the angle of the line from the centre of this Shape2D to
	 * the other Shape2D in radians, using Math.atan2()
	 *
	 * Reminder: on a regular graph/cartesian plane
	 * 		0 is right
	 * 	 pi/2 is up
	 * 	   pi is left
	 * 3 pi/2 is down
	 *
	 * The return value might contain a positive or negative multiple of 2 pi.
	 * It is still an equivalent angle
	 *
	 * @param other The other Shape2D to get an angle to.
	 * @return
	 * 			The angle of the line from the centre of this Shape2D to
	 * 			the other Shape2D in radians.
	 */
	public float getAngle(Shape2D other) {
		return (float) Math.atan2(other.y - y, other.x - x);
	}

	/**
	 * Offset this Shape2D by a vector
	 *
	 * @param theta	The angle, in radians
	 * @param dist	The magnitude of the vector
	 */
	public void moveVector(float theta, float dist) {
		x += (float) (dist * Math.cos(theta));
		y += (float) (dist * Math.sin(theta));
	}

	// ----------     Abstract Methods     ---------- //

    /**
     * Makes a copy of the current collision mask.
     *
     * @return
     *          A copy of the current collision mask.
     */
	public abstract Shape2D copy();

	/**
	 * @return The area of this shape
	 */
	public abstract float getArea();

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
	public abstract boolean overlaps(Shape2D other);

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and another collision mask.
	 * This function is symmetric.
     * 
     * @param other
     *              The other collision mask.
     * @return
     *          The distance. If the collision masks overlap, a negative number is returned.
     */
	public abstract float distance(Shape2D other);

	/**
	 * Renders the fill of this shape using an current shapeRenderer
	 * @param shapeRenderer a shapeRenderer that has run begin() & setcolour() already
	 */
	public abstract void renderShape(ShapeRenderer shapeRenderer);

	/**
	 * Renders an outline image where this shape is, in the isometric game view
	 * @param batch Batch to render outline image onto
	 */
	public abstract void renderHighlight(SpriteBatch batch);


	//TODO smooth collision:
	//angle of Incidence
	//angle of reflection
	//distanceInDirection(Shape2D other, float angle)

	//TODO allow breaking up of distance & overlaps methods to -> distanceToPoint, overlapsPoint...


	// ----------     Generic Object Methods    ---------- //

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object o);

	@Override
	public abstract String toString();


}
