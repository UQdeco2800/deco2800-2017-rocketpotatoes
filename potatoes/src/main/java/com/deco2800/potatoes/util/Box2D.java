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

            // Calc centre to centre dist
            float distX = Math.abs(otherBox.getX() - this.x);
            float distY = Math.abs(otherBox.getY() - this.y);

            // Check dist's are lagre enough that no collision could occur
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

            // Calc centre to centre dist
            float distX = Math.abs(otherBox.getX() - this.x);
            float distY = Math.abs(otherBox.getY() - this.y);
            float distSquare = distX * distX + distY * distY;

            //TODO clip portion of line covered by rects

            return (float) Math.sqrt((double) distSquare);


        } else if (other instanceof Circle2D) {
            Circle2D circle = (Circle2D) other;

            float distX = Math.abs(circle.getX() - this.x);
            float distY = Math.abs(circle.getY() - this.y);
            float distSquare = distX * distX + distY * distY;


            //TODO clip portion of line covered by rect

            return (float) Math.sqrt((double) distSquare - circle.getRadius());

        } else if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            float distX = Math.abs(point.getX() - this.x);
            float distY = Math.abs(point.getY() - this.y);

            //TODO clip portion of line covered by rect

            return (float) Math.sqrt((double) distX * distX + distY * distY);
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

    //TODO to String
}
