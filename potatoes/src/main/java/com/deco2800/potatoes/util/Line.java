package com.deco2800.potatoes.util;

import java.util.Objects;

import static com.deco2800.potatoes.util.MathUtil.compareFloat;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import com.deco2800.potatoes.collisions.Point2D;

/**
 * Utility class for testing weather lines intersect on map.
 * Made up of 2 nested point classes.
 */
public class Line {
    

    //-------------------- Nested Point class ---------------

    /**
     * Nested class to represent x and y coordinates of line endpoints
     */
    public static class Point {

        private float x;
        private float y;

        private Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    //--------------------- end of nested class ------------------

    private Point endPointOne;
    private Point endPointTwo;
    // Position flags for Box3D constructor.
    public enum Position {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }


    /**
     * Constructor taking floats as positions of line endpoints.
     * @param x1 x coordinate of endPointOne.
     * @param y1 y coordinate of endPointOne.
     * @param x2 x coordinate of endPointTwo.
     * @param y2 y coordinate of endPointTwo.
     */
    public Line(float x1, float y1, float x2, float y2) {

        this.endPointOne = new Point(x1, y1);
        this.endPointTwo = new Point(x2, y2);
    }

    public Line(Point2D p1, Point2D p2) {

        this.endPointOne = new Point(p1.getX(), p1.getY());
        this.endPointTwo = new Point(p2.getX(), p2.getY());
    }

    public Point getEndPointOne() {
        return endPointOne;
    }

    public Point getEndPointTwo() {
        return endPointTwo;
    }

    /**
     * Get the true length between the two endpoints of the line.
     * @return The length of the line as a float.
     */
    public float getDistance() {
        return (float) Math.sqrt(
                Math.pow(abs(getEndPointOne().getX() - getEndPointTwo().getX()), 2) +
                Math.pow(abs(getEndPointOne().getY() - getEndPointTwo().getY()), 2)
        );
    }

    /**
     * Function to find orientation of ordered triplet (p, q, r).
     * Slope of line segment (p1, p2): σ = (y2 - y1)/(x2 - x1)
     * Slope of line segment (p2, p3): τ = (y3 - y2)/(x3 - x2)
     *
     * If  σ < τ, the orientation is counterclockwise (left turn)
     * If  σ = τ, the orientation is collinear
     * If  σ > τ, the orientation is clockwise (right turn)
     *
     * Using above values of σ and τ, we can conclude that,
     * the orientation depends on sign of  below expression:
     *
     * (y2 - y1)*(x3 - x2) - (y3 - y2)*(x2 - x1)

     * Above expression is negative when σ < τ, i.e., counterclockwise
     * Above expression is 0 when σ = τ, i.e., collinear
     * Above expression is positive when σ > τ, i.e., clockwise
     *
     * @param p point p
     * @param q point q
     * @param r point r
     * @return 0 --> p, q and r are collinear
     *         1 --> Clockwise
     *         2 --> Counterclockwise
     */
    public static int orientation(Point p, Point q, Point r) {

        float val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (compareFloat(val, 0)) {
            return 0;  // colinear
        }

        return val > 0? 1: 2; // clock or counterclock wise
    }

    /**
     * Given three colinear points p, q, r, the function checks if
     * point q lies on line segment 'pr'
     * @param p point p
     * @param q point q
     * @param r point r
     * @return true if point q lies on segment 'pr'; false otherwise.
     */
    public static boolean onSegment(Point p, Point q, Point r) {
        return  q.getX() <= max(p.getX(), r.getX()) && q.getX() >= min(p.getX(), r.getX()) &&
                q.getY() <= max(p.getY(), r.getY()) && q.getY() >= min(p.getY(), r.getY());
    }

    /**
     * Function that checks if two line segments intersect base on the
     * orientations of line endpoints to one another.
     * @param other line that is tested for intersection
     * @return true if lines intersect; false otherwise.
     */
    public boolean doIntersect(Line other) {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(this.getEndPointOne(), this.getEndPointTwo(), other.getEndPointOne());
        int o2 = orientation(this.getEndPointOne(), this.getEndPointTwo(), other.getEndPointTwo());
        int o3 = orientation(other.getEndPointOne(), other.getEndPointTwo(), this.getEndPointOne());
        int o4 = orientation(other.getEndPointOne(), other.getEndPointTwo(), this.getEndPointTwo());

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(this.getEndPointOne(), other.getEndPointOne(), this.getEndPointTwo())) {
            return true;
        }

        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(this.getEndPointOne(), other.getEndPointTwo(), this.getEndPointTwo())) {
            return true;
        }

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(other.getEndPointOne(), this.getEndPointOne(), other.getEndPointTwo())) {
            return true;
        }

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(other.getEndPointOne(), this.getEndPointTwo(), other.getEndPointTwo())) {
            return true;
        }

        return false; // Doesn't fall in any of the above cases
    }

    @Override
    public int hashCode() {
        return Objects.hash(endPointOne.x, endPointOne.y, endPointTwo.x, endPointTwo.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Line)) {
            return false;
        }

        Line that = (Line) o;

        // since equality necessitates hash equality, this equals() method does
        // effectivelly float == float. I haven't found a way around this that enforces
        // transitivity, hashCode equality, and equality between very similar values.
        return hashCode() == that.hashCode() &&
                compareFloat(that.getEndPointOne().getX(), this.getEndPointOne().getX()) &&
                compareFloat(that.getEndPointOne().getY(), this.getEndPointOne().getY()) &&
                compareFloat(that.getEndPointTwo().getX(), this.getEndPointTwo().getX()) &&
                compareFloat(that.getEndPointTwo().getY(), this.getEndPointTwo().getY());

    }
}
