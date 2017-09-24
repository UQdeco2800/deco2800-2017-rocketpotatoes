package com.deco2800.potatoes.collisions;

public class UncenteredBox2D implements CollisionMask{

    private float x, y;
    private float xLength, yLength;

    /**
     * Create a new Box2D at a specific point with a length in the x and y dimension.
     * Any negative lengths will be swapped to positive values.
     *
     * @param x Centre-point x
     * @param y Centre-point y
     * @param xLength width along x axis
     * @param yLength height along y axis
     */
    public UncenteredBox2D(float x, float y, float xLength, float yLength) {
        this.x = x;
        this.y = y;
        setXLength(xLength);
        setYLength(yLength);
    }

    /**
     * Makes a copy of the current Box2D.
     *
     * @return A copy of the current Box2D
     */
    @Override
    public CollisionMask copy() {
        return new UncenteredBox2D(x, y, xLength, yLength);
    }

    private Box2D offset() {
        // TODO - why does this need to be shifted in the X axis but not the Y
        return new Box2D(x + (1 - xLength / 2), y, xLength, yLength);
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
     * Any negative values will be swapped to positive values.
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
     * Any negative values will be swapped to positive values.
     *
     * @param y The new y coordinate.
     */
    @Override
    public void setY(float y) { this.y = y; }

    /**
     * Returns the length in the x direction.
     *
     * @return Returns the x length.
     */
    public float getXLength() {
        return this.xLength;
    }

    /**
     * Set the length in the x direction.
     *
     * @param xLength The desired x length.
     */
    public void setXLength(float xLength) {
        this.xLength = (xLength >= 0) ? xLength : -xLength ;
    }

    /**
     * Returns the length in the y direction.
     *
     * @return Returns the y length.
     */
    public float getYLength() {
        return this.yLength;
    }

    /**
     * Sets the length in the y direction.
     *
     * @param yLength The desired y length.
     */
    public void setYLength(float yLength) {
        this.yLength = (yLength >= 0) ? yLength : -yLength ;
    }


    @Override
    public int hashCode() {
        // Start with a non-zero constant prime
        int result = 17;

        // Include a hash for each field.
        result = 31 * result + Float.floatToIntBits(this.x);
        result = 31 * result + Float.floatToIntBits(this.y);
        result = 31 * result + Float.floatToIntBits(this.xLength);
        result = 31 * result + Float.floatToIntBits(this.yLength);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UncenteredBox2D box2D = (UncenteredBox2D) o;

        if (Float.compare(box2D.x, x) != 0)
            return false;
        if (Float.compare(box2D.y, y) != 0)
            return false;
        return Float.compare(box2D.xLength, xLength) == 0 && Float.compare(box2D.yLength, yLength) == 0;
    }

    /**
     * Returns the variables of this Box2D in the form:
     * "<x>, <y>, <xLength>, <yLength>"
     *
     * @return This Box2D's parameters
     */
    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.xLength + ", " + this.yLength ;
    }

}
