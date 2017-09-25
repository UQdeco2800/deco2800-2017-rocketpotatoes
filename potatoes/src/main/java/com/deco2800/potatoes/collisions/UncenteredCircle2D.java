package com.deco2800.potatoes.collisions;

public class UncenteredCircle2D implements CollisionMask {

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
    public UncenteredCircle2D(float x, float y, float radius) {
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
        return new UncenteredCircle2D(x, y, radius);
    }

    private Circle2D offset() {
        return new Circle2D(x + 1 - radius, y, radius);
    }

    @Override
    public boolean overlaps(CollisionMask other) {
        return offset().overlaps(other);
    }

    @Override
    public float distance(CollisionMask other) {
        return offset().distance(other);
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        return offset().distance(x1, y1, x2, y2);
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
        this.radius = radius >= 0 ? radius : -radius ;
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UncenteredCircle2D circle2D = (UncenteredCircle2D) o;

        if (Float.compare(circle2D.x, x) != 0)
            return false;
        if (Float.compare(circle2D.y, y) != 0)
            return false;
        return Float.compare(circle2D.radius, radius) == 0;
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
