package com.deco2800.potatoes.util;

import java.util.Objects;

public class Point2D {
    
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
        return Objects.hash(x, y);
    }

	private boolean compareFloat(float a, float b) {
		float delta = 0.00001f;
		return Math.abs(a-b) < delta;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Point)) {
            return false;
        }

        Point other = (Point) o;

        return hashCode() ==  other.hashCode() &&
            compareFloat(getX(), other.getX()) &&
            compareFloat(getY(), other.getY());
    }

    @Override
    public String toString() {
        return "Point {" + getX() + ", " + getY() + "}";
    }
}