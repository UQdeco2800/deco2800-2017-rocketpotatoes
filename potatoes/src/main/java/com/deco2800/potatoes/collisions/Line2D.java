package com.deco2800.potatoes.collisions;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.trees.IceTreeType;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;

import java.util.Objects;

import static com.deco2800.potatoes.util.MathUtil.compareFloat;

public class Line2D extends Shape2D{

    // stores the point with the lowest x as point 1, if x is equal the point with the lowest y

    private Point2D point1;
    private Point2D point2;

    // processing / reused values

    private float x1; //these are just used as shorthand
    private float y1;
    private float x2;
    private float y2;

    private float maxX;
    private float minX;
    private float maxY;
    private float minY;

    private float deltaX;
    private float deltaY;

    private float lenSqr;
    //private float length;

    // equation of the line in the form y = mx + b
    private float m;
    private float b;


    private static final String textureStr = "LINE_HIGHLIGHT";


    // ----------     Initialisation    ---------- //

    public Line2D(float x1, float y1, float x2, float y2) {
        this( new Point2D(x1, y1), new Point2D(x2, y2));
    }
    
    public Line2D(Point2D point1, Point2D point2) {

        // swap the points if point 1 is not the leftmost
        // this ordering is needed for equals() and hashcode() etc.
        if (point1.getX() < point2.getX() ||
                (compareFloat(point1.getX(),point2.getX()) && point1.getY() < point2.getY())) {
            this.point1 = point1;
            this.point2 = point2;
        } else {
            this.point1 = point2;
            this.point2 = point1;
        }

        preProcessing();
    }

    private void preProcessing() {

        this.x1 = point1.x;
        this.y1 = point1.y;
        this.x2 = point2.x;
        this.y2 = point2.y;

        //centre point
        super.x = ( x1 + x2 ) / 2;
        super.y = ( y1 + y2 ) / 2;

        this.maxX = x1 >= x2 ? x1 : x2;
        this.minX = x1 <= x2 ? x1 : x2;
        this.maxY = y1 >= y2 ? y1 : y2;
        this.minY = y1 <= y2 ? y1 : y2;

        this.deltaX = x2 - x1;
        this.deltaY = y2 - y1;

        this.lenSqr = deltaX * deltaX + deltaY * deltaY;
        //this.length = Math.sqrt(lenSqr);

        // equation of the line in the form y = mx + b
        this.m = deltaY / deltaX;
        this.b = y1 - m * x1;
    }

    // ----------     Unique Methods    ---------- //

    //Shouldn't really be needing to move lines like this
    @Override
    public void setX(float x) {
        float diffX = x - this.x;
        point1 = new Point2D(point1.x + diffX, point1.y);
        point2 = new Point2D(point2.x + diffX, point2.y);
        preProcessing();
    }

    @Override
    public void setY(float y) {
        float diffY = y - this.y;
        point1 = new Point2D(point1.x, point1.y + diffY);
        point2 = new Point2D(point2.x, point2.y + diffY);
        preProcessing();
    }

    // if endpoint is not valid leftmost point will be returned
    public Point2D getOtherEndPoint(Point2D endPoint) {
        if (compareFloat(endPoint.x, point1.x)
                && compareFloat(endPoint.y, point1.y)) {
            return point2;
        } else {
            return point1;
        }
    }


    public float getLenSqr() {
        return this.lenSqr;
    }


    private boolean overlapsPoint(Point2D other) {
        return compareFloat( distanceToPoint( other ), 0);
    }

    private boolean overlapsCircle(Circle2D other) {
        return distanceToCircle( other ) <= 0;
    }

    private boolean overlapsBox(Box2D other) {

        float fMin = 0;
        float fMax = 1;

        float[] point1 = {x1, y1};
        float[] point2 = {x2, y2};
        float[] boxMin = {other.x - other.getXLength()/2, other.y - other.getYLength()/2};
        float[] boxMax = {other.x + other.getXLength()/2, other.y + other.getYLength()/2};

        for (int i = 0; i < 2; i++) {
            float lineDist = point2[i] - point1[i];

            //if the distance of the line in the given dimension is not 0
            if (!compareFloat(lineDist, 0)) {

                //get the edges of the range where the line & box overlap in the given dimension
                float intervalRange1 = (boxMin[i] - point1[i]) / lineDist;
                float intervalRange2 = (boxMax[i] - point1[i]) / lineDist;

                //order the edges in ascending order
                float intervalRangeMin;
                float intervalRangeMax;

                if (intervalRange1 < intervalRange2) {
                    intervalRangeMin = intervalRange1;
                    intervalRangeMax = intervalRange2;
                } else {
                    intervalRangeMin = intervalRange2;
                    intervalRangeMax = intervalRange1;
                }

                //update the fraction of the line that intersects
                fMin = Math.max(fMin, intervalRangeMin);
                fMax = Math.min(fMax, intervalRangeMax);

                if (fMin > fMax)
                    return false;

            } else if (point1[i] < boxMin[i] || point2[i] > boxMax[i])
                return false;
        }

        return true;
    }

    //TODO
    private boolean overlapsLine(Line2D other) {
        return false;
    }


    private float distanceToPoint(Point2D other) {
        // if len approaching 0, consider as a point
        if (compareFloat(lenSqr, 0)) {
            return other.distance(point1);
        }

        // how far along the line segment is the closest point to us?
        float unclamped = ((other.x - x1) * deltaX + (other.y - y1) * deltaY) / lenSqr;
        float clamped = Math.max(0f, Math.min(1f, unclamped));

        return new Point2D(x1 + clamped * deltaX, y1 + clamped * deltaY).distance(other);

    }


    private float distanceToCircle(Circle2D other) {
        Point2D centre = new Point2D(other.x, other.y);
        return distanceToPoint( centre ) - other.getRadius();
    }


    private float distanceToBox(Box2D other) {

        Line2D ccc = new Line2D(13.5f, 12.5f, 50, 0); //TODO deleteme
        boolean aaa = this.equals(ccc) && compareFloat(other.x, 15.5f) && compareFloat(other.y, 11.5f);
        if (aaa) { //TODO deleteme
            System.out.println("found col  1");
        }

        // if len approaching 0, consider as a point
        if (compareFloat(lenSqr, 0)) {
            return other.distance(point1);
        }


        // if overlapping the box return a negative
        if (this.overlapsBox(other)) {
        	return -1;
        }

        float distX1 = Math.abs(x1 - other.x) - other.getXLength()/2;
        float distY1 = Math.abs(y1 - other.y) - other.getYLength()/2;
        float distX2 = Math.abs(x2 - other.x) - other.getXLength()/2;
        float distY2 = Math.abs(y2 - other.y) - other.getYLength()/2;

        float maxBoxX = other.x + other.getXLength()/2;
        float minBoxX = other.x - other.getXLength()/2;
        float maxBoxY = other.y + other.getYLength()/2;
        float minBoxY = other.y - other.getYLength()/2;


        //gradient = 0 cases
        if (compareFloat(x1, x2)) {
            if (minBoxY <= maxY ) {
                if(minY <= maxBoxY) {
                    return distX1;      //line overlaps Box on Y, return X dist
                } else {
                    // the line is top left or top right of box
                    return new Line2D(minBoxX, maxBoxY, maxBoxX, maxBoxY).distanceToPoint(
                            new Point2D(x1, minY) );
                }
            } else {
                // the line is bottom left or bottom right of box
                return new Line2D(minBoxX, minBoxY, maxBoxX, minBoxY).distanceToPoint(
                        new Point2D(x1, maxY) );
            }
        }

        if (compareFloat(y1, y2)) {
            if (minBoxX <= maxX ) {
                if(minX <= maxBoxX) {
                    return distY1;      //line overlaps Box on X, return Y dist
                } else {
                    // the line is top right or bottom right of box
                    return new Line2D(maxBoxX, minBoxY, maxBoxX, maxBoxY).distanceToPoint(
                            new Point2D(minX, y1) );
                }
            } else {
                // the line is top left or bottom left of box
                return new Line2D(minBoxX, minBoxY, minBoxX, maxBoxY).distanceToPoint(
                        new Point2D(maxX, y1) );
            }
        }

        //closest point overlaps on one axis
        //e.g. point one is within the vertical bounds of the box & closer than point 2 on x axis
        if (distY1 <= 0 && (other.x < x1 && x1 < x2 || x2 < x1 && x1 < other.x))
            return distX1;
        if (distY2 <= 0 && (other.x < x2 && x2 < x1 || x1 < x2 && x2 < other.x))
            return distX2;
        if (distX1 <= 0 && (other.y < y1 && y1 < y2 || y2 < y1 && y1 < other.y))
            return distY1;
        if (distX2 <= 0 && (other.y < y2 && y2 < y1 || y1 < y2 && y2 < other.y))
            return distY2;


        //both points in one diagonal
        if (minX >= maxBoxX && minY >= maxBoxY) {     //top right
            return distanceToPoint( new Point2D(maxBoxX, maxBoxY) ); }
        if (maxX <= minBoxX && minY >= maxBoxY) {     //top left
            return distanceToPoint( new Point2D(minBoxX, maxBoxY) ); }
        if (minX >= maxBoxX && maxY <= minBoxY) {     //bot right
            return distanceToPoint( new Point2D(maxBoxX, minBoxY) ); }
        if (maxX <= minBoxX && maxY <= minBoxY) {     //bot left
            return distanceToPoint( new Point2D(minBoxX, minBoxY) ); }


        // gradient of line is preprocessed (y = mx + b)

        // The y value of the line, at the centre point of the box
        float boxCentreY = m * other.x + b;

        boolean gradRise = (m > 0);
        boolean passAbove = (boxCentreY > other.y);

        // the closest corner of the box to the line
        Point2D closestCorner = new Point2D(
                // iff (the lines gradient is rising & the line passes below)
                //  or (the lines gradient is falling & the line passes above)
                // then the line passes to the right of the box
                (gradRise ^ passAbove ? maxBoxX : minBoxX ),
                // if the line passes above the box, get the top Y of the box
                (passAbove ? maxBoxY : minBoxY));

        return distanceToPoint( closestCorner );
    }

    //TODO
    private float distanceToLine(Line2D other) {
        return 10;
    }


    // ----------     Abstract Methods    ---------- //

    @Override //TODO copy pre-processing as well
    public Shape2D copy() {
        return new Line2D( point1, point2 );
    }

    @Override
    public float getArea() {
        return 0;
    }

    @Override
    public boolean overlaps(Shape2D other) {
        if (other instanceof Point2D) {
            return overlapsPoint((Point2D)other);
        } else if (other instanceof Circle2D) {
            return overlapsCircle((Circle2D)other);
        } else if (other instanceof Box2D) {
            return overlapsBox((Box2D)other);
        } else if (other instanceof Line2D) {
            return overlapsLine((Line2D)other);
        } else {
            return other.overlaps(this);
        }
    }


    /**
     * Finds the minimum perpendicular distance between a straight line and this collision mask. This is used primarily
     * in path finding to see if an entity can walk past an object without colliding with it.
     *
     * For visual examples, see the relevant wiki page.
     *
     * @return
     *          If the line intersects the collision mask, then a negative number is returned. If the line does
     *          not intersect the collision mask, then the number returned should be the length of a perpendicular line
     *          as seen above.
     */
    @Override
    public float distance(Shape2D other) {
        if (other instanceof Point2D) {
            return distanceToPoint((Point2D)other);
        } else if (other instanceof Circle2D) {
            return distanceToCircle((Circle2D)other);
        } else if (other instanceof Box2D) {
            return distanceToBox((Box2D)other);
        } else if (other instanceof Line2D) {
            return distanceToLine((Line2D)other);
        } else {
            return other.distance(this);
        }
    }


    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {

        OrthographicCamera camera = GameManager.get().getManager(CameraManager.class).getCamera();

        //calculate orthagonal corners of box
        Vector2 screenWorldCoords = Render3D.worldToScreenCoordinates(x1, y1, 0);
        Vector3 c1 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

        screenWorldCoords = Render3D.worldToScreenCoordinates(x2, y2, 0);
        Vector3 c2 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

        shapeRenderer.line( c1.x, c1.y, c2.x, c2.y );
    }

    @Override
    public void renderHighlight(SpriteBatch batch) {

        Texture textureHighlight  = GameManager.get().getManager(TextureManager.class).getTexture(textureStr);

        //draw staight line between each node and the next
        drawTextureBetween(batch, textureHighlight, x1, y1, x2, y2);

    }

    //TODO copied from LightningEffect, clean up
    /**
     * !!!
     * Renders a line between two points
     *
     * @param batch   the SpriteBatch to render to
     * @param texture the texture to draw
     * @param xPos    start x position
     * @param yPos    start y position
     * @param fxPos   end x position
     * @param fyPos   end y position
     */
    public void drawTextureBetween(SpriteBatch batch, Texture texture, float xPos, float yPos, float fxPos,
                                   float fyPos) {
        int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");


        float lWidth = texture.getWidth();
        float lHeight = texture.getHeight();

        Vector2 startPos = Render3D.worldToScreenCoordinates(xPos, yPos, 0);
        Vector2 endPos = Render3D.worldToScreenCoordinates(fxPos, fyPos, 0);

        float l = endPos.x - startPos.x;
        float h = endPos.y - startPos.y;

        // length of line in x direction
        float lX = startPos.x - (lWidth - tileWidth) / 2;
        // length of line in y direction
        float lY = 0 - startPos.y - (lHeight - tileHeight) / 2;

        float originX = texture.getWidth() / 2;
        float originY = texture.getHeight() / 2;

        // stretch texture using x scale
        float lScaleX = (float) Math.sqrt(l * l + h * h);
        float lScaleY = 0.4f;

        int srcX = 0;
        int srcY = 0;
        int srcWidth = texture.getWidth();
        int srcHeight = texture.getHeight();
        batch.draw(texture, lX, lY, originX, originY, lWidth, lHeight, lScaleX, lScaleY,
                WorldUtil.rotation(xPos, yPos, fxPos, fyPos) - 45, srcX, srcY, srcWidth, srcHeight, false, false);

    }



    // ----------     Generic Object Methods    ---------- //

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1, x2, y2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Line2D line2D = (Line2D) o;

        return point1.equals(line2D.point1) && point2.equals(line2D.point2);
    }

    @Override
    public String toString() {
        return x1 + ", " + y1 + ", " + x2 + ", " + y2 ;
    }


}
