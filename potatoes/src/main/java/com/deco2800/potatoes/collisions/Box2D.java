package com.deco2800.potatoes.collisions;

import java.util.Objects;

public class Box2D implements CollisionMask{

    private float x, y;
    private float xLength, yLength;

    /**
     * Create a new Box2D
     *
     * @param x centrepoint x
     * @param y centrepoint y
     * @param xLength width along x axis
     * @param yLength height along y axis
     */
    public Box2D(float x, float y, float xLength, float yLength) {
        this.x = x;
        this.y = y;
        this.xLength = xLength;
        this.yLength = yLength;
    }

    @Override
    public boolean overlaps(CollisionMask other) {
        if (other instanceof Box2D) {
            Box2D otherBox = (Box2D) other;

            // Calc centre to centre dist
            float distX = Math.abs(otherBox.getX() - this.x);
            float distY = Math.abs(otherBox.getY() - this.y);

            // Check dist's are large enough that no collision could occur
            if (distX > (this.xLength + otherBox.getXLength())/2) { return false; }
            if (distY > (this.yLength + otherBox.getYLength())/2) { return false; }

            return true;

        } else if (other instanceof Circle2D) {
            Circle2D circle = (Circle2D) other;
            // We will consider the circle to be a point
            // and the rectangle to be a rounded rectangle
            // (adding the radius to the outside of the rectangle)

            // Collapse down the dimensions, so we're considering one corner of the rectangle
            float distX = Math.abs(circle.getX() - this.x);
            float distY = Math.abs(circle.getY() - this.y);

            // Point is outside collision
            if (distX > (this.xLength/2 + circle.getRadius())) { return false; }
            if (distY > (this.yLength/2 + circle.getRadius())) { return false; }

            // Point is inside collision
            if (distX <= (this.xLength/2)) { return true; }
            if (distY <= (this.yLength/2)) { return true; }

            // May intersect corner scenario, calc oblique distance square
            float cornerX = distX - (this.xLength / 2);
            float cornerY = distY - (this.yLength / 2);
            float cornerDistSquare = cornerX * cornerX + cornerY * cornerY;

            return cornerDistSquare <= (circle.getRadius() * circle.getRadius());

        } else if (other instanceof Point2D) {
            Point2D otherPoint = (Point2D) other;

            // Check x non collision
            if ( Math.abs(this.x - otherPoint.getX()) < (this.xLength * 0.5)) {
                return false;
            }

            // Check y non collision
            if ( Math.abs(this.y - otherPoint.getY()) < (this.yLength * 0.5)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public float distance(CollisionMask other) {

        if (other instanceof Box2D) {
            Box2D otherBox = (Box2D) other;

            // Calc dist between sides on each dimension
            float distX = Math.abs(otherBox.getX() - this.x) - (this.xLength + otherBox.getXLength())/2;
            float distY = Math.abs(otherBox.getX() - this.x) - (this.yLength + otherBox.getYLength())/2;

            if ((distX >= 0) && (distY >= 0)) {
                // Boxes are diagonal to each other, calc corner point to point dist
                return (float) Math.sqrt(distX * distX + distY * distY);
            } else if (distX >= 0) {
                // Boxes overlap on x co-ord but not y
                return distX;
            } else if (distY >= 0) {
                // Boxes overlap on y co-ord but not x
                return distY;
            } else {
                // Boxes overlap, return rough negative val
                // TODO this val might be used in physics
                return Math.max(distX, distY);
            }

        } else if (other instanceof Circle2D) {
            Circle2D circle = (Circle2D) other;

            // Calc dist between sides on each dimension, considering the circle as a point
            float distPointX = Math.abs(circle.getX() - this.x) - this.xLength/2;
            float distPointY = Math.abs(circle.getY() - this.y) - this.yLength/2;

            // Calc dist between sides on each dimension
            float distX = distPointX - circle.getRadius();
            float distY = distPointY - circle.getRadius();

            if ((distX >= 0) && (distPointY < 0)) {
                // Box & circle overlap on x co-ord but not y
                return distX;
            } else if ((distY >= 0) && (distPointX < 0)) {
                // Box & circle overlap on y co-ord but not x
                return distY;
            } else if ((distX >= 0) && (distY >= 0)) {
                // Box & circle are diagonal to each other, calc corner point to point dist
                return (float) Math.sqrt(distPointX * distPointX + distPointY * distPointY) - circle.getRadius();
            } else {
                // Box & circle overlap, return rough negative val
                // TODO this val might be used in physics
                return Math.max(distX, distY);
            }

        } else if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            // Calc dist between sides on each dimension
            float distX = Math.abs(point.getX() - this.x) - this.xLength/2;
            float distY = Math.abs(point.getX() - this.x) - this.yLength/2;

            if ((distX >= 0) && (distY >= 0)) {
                // Box & point are diagonal to each other, calc corner point to point dist
                return (float) Math.sqrt(distX * distX + distY * distY);
            } else if (distX >= 0) {
                // Box & point overlap on x co-ord but not y
                return distX;
            } else if (distY >= 0) {
                // Box & point overlap on y co-ord but not x
                return distY;
            } else {
                // Box & point overlap, return rough negative val
                // TODO this val might be used in physics
                return Math.max(distX, distY);
            }
        }

        return 0;
    }

    /* //Centre to centre distance, clipped by the mask
    // made some bad assumptions, isn't minimum edge-to-edge distance
    public float distanceCentreClipped(CollisionMask other) {
        // Calc centre to centre dist
        float distX = Math.abs(other.getX() - this.x);
        float distY = Math.abs(other.getY() - this.y);
        float dist = (float) Math.sqrt((double) distX * distX + distY * distY);

        // distMin will be the initial % of the line unobstructed by this Box2D
        // e.g. the line might be first unobstructed by this Box2D 30% along
        float distMin = Math.min((distX - this.x/2)/distX,
                (distY - this.y/2)/distY);

        if (other instanceof Box2D) {
            Box2D otherBox = (Box2D) other;

            // distMax will be the final % of the line unobstructed by otherBox
            // and then become obstructed again 70% along, by otherBox
            float distMax = Math.max((distX - otherBox.getXLength()/2)/distX,
                                    (distY - otherBox.getYLength()/2)/distY);

            //scale based off of distMin & distMax
            return (dist * (distMax - distMin));

        } else if (other instanceof Circle2D) {
            Circle2D circle = (Circle2D) other;

            //scale based off of distMin & subtract the radius of the circle
            return (dist * (1 - distMin) - circle.getRadius());

        } else if (other instanceof Point2D) {

            //scale based off of distMin
            return (dist * (1 - distMin));
        }
    }
    */

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

    public float getXLength() {
        return this.xLength;
    }

    public void setXLength(float xLength) {
        this.xLength = xLength;
    }

    public float getYLength() {
        return this.yLength;
    }

    public void setYLength(float yLength) {
        this.yLength = yLength;
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
        if (this == o) { return true; }

        if (!(o instanceof Box2D)) { return false; }

        Box2D that = (Box2D) o;

        return hashCode() == that.hashCode() &&
                this.x         == that.getX() &&
                this.y         == that.getY() &&
                this.xLength   == that.getXLength() &&
                this.yLength   == that.getYLength();
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.xLength + ", " + this.yLength ;
    }

}
