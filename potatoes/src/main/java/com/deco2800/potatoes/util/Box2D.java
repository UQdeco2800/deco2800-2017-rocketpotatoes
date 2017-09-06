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
            Box2D otherBox = <Box2D> other;
            // this x smaller
            if (this.x + this.xLength < otherBox.x ) {
                return false;
            }

            // this x larger
            if (this.x > other.x + other.xLength) {
                return false;
            }

            // this y smaller
            if (this.y + this.yLength < other.y) {
                return false;
            }

            // this y larger
            if (this.y > other.y + other.yLength) {
                return false;
            }
        } else if (other instanceof Circle2D) {

        } else if (other instanceof Point2D) {

        }



        return false;
    }

    @Override
    public float distance(CollisionMask other) {
        return 0;
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        return 0;
    }
}
