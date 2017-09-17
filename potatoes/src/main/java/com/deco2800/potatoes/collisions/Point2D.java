package com.deco2800.potatoes.collisions;

public class Point2D implements CollisionMask{
    
    private float x, y;

    /**
     * Default constructor for the purporses of serialization.
     */
    public Point2D() {
        //Empty constructor because Sonar
    }

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
        if (other instanceof Point2D) {
            return this.equals(other);
        } else {
            return other.overlaps(this);
        }
    }

    @Override
    public float distance(CollisionMask other) {
        if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            float distX = point.getX() - this.x;
            float distY = point.getY() - this.y;

            // use pythagorean theorem
            return (float) Math.sqrt((double) distX * distX + distY * distY );
        } else {
            return other.distance(this);
        }
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        // don't sqrt anything you don't have to
        float segmentLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (Float.compare(segmentLength, 0) == 0) {
            return distance(new Point2D(x1, y1));
        }

        // how far along the line segment is the closest point to us?
        float unclamped = ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1)) / segmentLength;
        float clamped = Math.max(0f, Math.min(1f, unclamped));

        return distance(new Point2D(x1 + clamped * (x2 - x1), y1 + clamped * (y2 - y1)));
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Point2D point2D = (Point2D) o;

        if (Float.compare(point2D.x, x) != 0)
            return false;
        return Float.compare(point2D.y, y) == 0;
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}
