package com.deco2800.potatoes.util;

import java.util.Objects;

public class Point2D implements CollisionMask{
    
    private float x, y;

    /**
     * Default constructor for the purporses of serialization.
     */
    public Point2D() { }

    /**
     * Constructs a new point at a given location.
     * 
     * @param x
     *              The X coordinate of the point.
     * @param y
     *              The Y coordinate of the point.
     */
    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean overlaps(CollisionMask other) {
        if (other instanceof Box2D) {
            Box2D box = (Box2D) other;
            return box.overlaps(this);
        } else if (other instanceof Circle2D) {
            Circle2D circle = (Circle2D) other;
            return circle.overlaps(this);
        } else if (other instanceof Point2D) {
            Point2D otherPoint = (Point2D) other;
            return this.equals(otherPoint);
        }
        return false;
    }

    @Override
    public float distance(CollisionMask other) {
        if (other instanceof Box2D) {
            Box2D box = (Box2D) other;
            return box.distance(this);
        } else if (other instanceof Circle2D) {
            Circle2D circle = (Circle2D) other;
            return circle.distance(this);
        } else if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            float distX = Math.abs(point.getX() - this.x);
            float distY = Math.abs(point.getY() - this.y);

            // use pythagorean theorem
            return (float) Math.sqrt((double) distX * distX + distY * distY );
        }
        return 0;
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        //TODO
        return 0;
    }

    /**
	 * Returns the x coordinate.
	 * 
	 * @return Returns the x coordinate.
	 */
    public float getX() {
        return x;
    }

	/**
	 * Sets the x coordinate.
	 * 
	 * @param x
	 *            The new x coordinate.
	 */
    public void setX(float x) {
        this.x = x;
    }

	/**
	 * Returns the y coordinate.
	 * 
	 * @return Returns the y coordinate.
	 */
    public float getY() {
        return y;
    }

	/**
	 * Sets the y coordinate.
	 * 
	 * @param y
	 *            The new y coordinate.
	 */
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        // Start with a non-zero constant prime
        int result = 17;

        // Include a hash for each field.
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }

        if (!(o instanceof Point2D)) { return false; }

        Point2D that = (Point2D) o;

        return hashCode() == that.hashCode() &&
                this.x == that.getX() &&
                this.y == that.getY();
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}