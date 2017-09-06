package com.deco2800.potatoes.util;

public class Circle2D implements CollisionMask {
    @Override
    public boolean overlaps(CollisionMask other) {
        if (other instanceof Box2D) {
            Box2D otherBox = (Box2D) other;
            return otherBox.overlaps(this);
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;
            //TODO
        } else if (other instanceof Point2D) {
            Point2D otherPoint = (Point2D) other;
            //TODO
        }
        return false;
    }

    @Override
    public float distance(CollisionMask other) {
        if (other instanceof Box2D) {
            Box2D otherBox = (Box2D) other;
            return otherBox.distance(this);
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
        return 0;
    }

    @Override
    public void setX(float x) {

    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public void setY(float y) {

    }
}
