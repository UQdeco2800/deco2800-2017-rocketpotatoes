package com.deco2800.potatoes.collisions;

public class Circle2D implements CollisionMask {

    private float x;
    private float y;
    private float radius;


    /**
     * Create a new Box2D at a specific point with a given radius.
     * Any negative radius will be swapped to positive.
     *
     * @param x Centre-point x
     * @param y Centre-point y
     * @param radius The radius of the circle.
     */
    public Circle2D(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.setRadius(radius); //converts neg radius
    }

    /**
     * Makes a copy of the current Circle2D.
     *
     * @return A copy of the current Circle2D
     */
    @Override
    public CollisionMask copy() {
        return new Circle2D(x, y, radius);
    }

    /**
     * Checks if this collision mask overlaps another collision masks.
     * This function is symmetric.
     * Touching the edge is not considered as overlapping.
     *
     * @param other The other collision mask.
     * @return True iff the collision masks are overlapping.
     */
    @Override
    public boolean overlaps(CollisionMask other) {
        if (other instanceof Point2D) {
            Point2D point = (Point2D) other;
            return (distance(point) < 0);
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;
            return (distance(otherCircle) < 0);
        } else {
            return other.overlaps(this);
        }
    }

    /**
     * Finds the minimum edge-to-edge distance between this collision mask and another collision mask.
     * This function is symmetric.
     *
     * @param other The other collision mask.
     * @return The distance. If the collision masks overlap, a negative number is returned.
     */
    @Override
    public float distance(CollisionMask other) {
        if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            float distX = Math.abs(point.getX() - this.x);
            float distY = Math.abs(point.getY() - this.y);

            // use pythagorean theorem
            float dist = (float) Math.sqrt((double) distX * distX + distY * distY );

            return dist - radius;
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;

            float distX = Math.abs(otherCircle.getX() - this.x);
            float distY = Math.abs(otherCircle.getY() - this.y);

            // use pythagorean theorem
            float dist = (float) Math.sqrt((double) distX * distX + distY * distY );

            // subtract radius's
            dist -= otherCircle.getRadius() + this.radius;

            return dist;
        } else {
            return other.distance(this);
        }
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        Point2D centre = new Point2D(x, y);
        return centre.distance(x1, y1, x2, y2) - radius;
    }

    /**
     * Returns the x coordinate at the centre of the mask.
     *
     * @return Returns the x coordinate.
     */
    @Override
    public float getX() { return this.x; }

    /**
     * Sets the x coordiante at the centre of the mask.
     *
     * @param x The new x coordinate.
     */
    @Override
    public void setX(float x) { this.x = x; }

    /**
     * Returns the y coordinate at the centre of the mask.
     *
     * @return Returns the y coordinate.
     */
    @Override
    public float getY() { return this.y; }

    /**
     * Sets the y coordinate at the centre of the mask.
     *
     * @param y The new y coordinate.
     */
    @Override
    public void setY(float y) { this.y = y; }

    /**
     * Returns the radius of this Circle2D.
     *
     * @return Returns the radius.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Sets the radius of the Circle2D.
     * Any negative values will be swapped to positive values.
     *
     * @param radius The new radius.
     */
    public void setRadius(float radius) {
        this.radius = (radius >= 0) ? radius : -radius ;
    }

    //TODO maybe: public boolean centredOnPoint(Point2D) {}
    //used when following a path

    @Override
    public int hashCode() {
        // Start with a non-zero constant prime
        int result = 17;

        // Include a hash for each field.
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);
        result = 31 * result + Float.floatToIntBits(this.radius);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }

        if (!(o instanceof Circle2D)) { return false; }

        Circle2D that = (Circle2D) o;

        return hashCode() == that.hashCode() &&
                this.x      == that.getX() &&
                this.y      == that.getY() &&
                this.radius == that.getRadius();
    }

    /**
     * Returns the variables of this Circle2D in the form:
     * "<x>, <y>, <radius>"
     *
     * @return This Circle2D's parameters
     */
    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.radius;
    }
}
