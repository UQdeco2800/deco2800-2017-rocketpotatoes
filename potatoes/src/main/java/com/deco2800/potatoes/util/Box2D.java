package com.deco2800.potatoes.util;

public class Box2D implements CollisionMask{
    private float x, y;

    private float xLength, yLength;

    /**
     *
     * @param x centrepoint
     * @param y
     * @param xLength width
     * @param yLength height
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
            // this x is too small -> non-collision
            if (this.x + (this.xLength * 0.5) < otherBox.x - (otherBox.xLength * 0.5)) {
                return false;
            }

            // this x is too large -> non-collision
            if (this.x - (this.xLength * 0.5) > otherBox.x + (otherBox.xLength * 0.5)) {
                return false;
            }

            // this y is too small -> non-collision
            if (this.y + (this.yLength * 0.5) < otherBox.y - (otherBox.yLength * 0.5)) {
                return false;
            }

            // this y is too large -> non-collision
            if (this.y - (this.yLength * 0.5) > otherBox.y + (otherBox.yLength * 0.5)) {
                return false;
            }
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;
            //TODO not quite sure how yet

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
            //TODO
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;
            //TODO
        } else if (other instanceof Point2D) {
            Point2D otherPoint = (Point2D) other;
            //TODO
        }

        return 0;
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
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

    //TODO equals
}
