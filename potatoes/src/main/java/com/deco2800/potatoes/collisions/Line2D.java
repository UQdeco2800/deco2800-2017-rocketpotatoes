package com.deco2800.potatoes.collisions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Line2D extends Shape2D{
    
    private Point2D point1;
    private Point2D point2;




    // ----------     Initialisation    ---------- //

    public Line2D(float x1, float y1, float x2, float y2) {
        
        point1 = new Point2D(x1, y1);
        point2 = new Point2D(x2, y2);
        
        this.x = ( x1 + x2 ) / 2;
        this.y = ( y1 + y2 ) / 2;
    }
    
    public Line2D(Point2D point1, Point2D point2) {
        this.point1 = point1;
        this.point2 = point2;

        this.x = ( point1.x + point2.x ) / 2;
        this.y = ( point1.y + point2.y ) / 2;
    }


    // ----------     Abstract Methods    ---------- //

    @Override
    public Shape2D copy() {
        return new Line2D(point1, point2);
    }

    @Override
    public float getArea() {
        return 0;
    }

    @Override
    public boolean overlaps(Shape2D other) {
        return false;
    }

    @Override
    public float distance(Shape2D other) {
        return 0;
    }

    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        return 0;
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {

    }

    @Override
    public void renderHighlight(SpriteBatch batch) {

    }



    // ----------     Generic Object Methods    ---------- //

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}
