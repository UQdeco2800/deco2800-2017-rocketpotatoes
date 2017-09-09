package com.deco2800.potatoes.util;

public class Circle2D implements CollisionMask {

    private float x;
    private float y;
    private float radius;

    public Circle2D(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public boolean overlaps(CollisionMask other) {
        if (other instanceof Box2D) {
            Box2D box = (Box2D) other;
            return box.overlaps(this);
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;
            return (distance(otherCircle) < 0);
        } else if (other instanceof Point2D) {
            Point2D point = (Point2D) other;
            return (distance(point) < 0);
        }
        return false;
    }

    @Override
    public float distance(CollisionMask other) {
        if (other instanceof Box2D) {
            Box2D box = (Box2D) other;
            return box.distance(this);
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;

            float distX = Math.abs(otherCircle.getX() - this.x);
            float distY = Math.abs(otherCircle.getY() - this.y);

            // use pythagorean theorem
            float dist = (float) Math.sqrt((double) distX * distX + distY * distY );

            // subtract radius's
            dist -= otherCircle.getRadius() + this.radius;

            return dist;
        } else if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            float distX = Math.abs(point.getX() - this.x);
            float distY = Math.abs(point.getY() - this.y);

            // use pythagorean theorem
            float dist = (float) Math.sqrt((double) distX * distX + distY * distY );

            // subtract radius
            dist -= - this.radius;

            return dist;
        }
        return 0;
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        //TODO
        return 0;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


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

    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.radius;
    }
}
