package com.deco2800.potatoes.collisions;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;

import java.util.Objects;

import static com.deco2800.potatoes.util.MathUtil.compareFloat;

public class Line2D extends Shape2D{
    
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private static final String textureStr = "LINE_HIGHLIGHT";


    // ----------     Initialisation    ---------- //

    public Line2D(float x1, float y1, float x2, float y2) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        //centre point
        this.x = ( x1 + x2 ) / 2;
        this.y = ( y1 + y2 ) / 2;
    }
    
    public Line2D(Point2D point1, Point2D point2) {

        this.x1 = point1.x;
        this.y1 = point1.y;
        this.x2 = point2.x;
        this.y2 = point2.y;

        //centre point
        this.x = ( point1.x + point2.x ) / 2;
        this.y = ( point1.y + point2.y ) / 2;
    }

    // ----------     Unique Methods    ---------- //

    public float getLenSqr() {
        //return lenSqr;
        return 0;
    }


    // TODO maybe get line endpoints method


    private boolean overlapsPoint(Point2D other) {
        return false;
    }

    private boolean overlapsCircle(Circle2D other) {

        return false;
    }

    private boolean overlapsBox(Box2D other) {
        return false;
    }

    /**TODOcopied from box
     * Checks to see if a line intersects with this Box2D.
     * The line goes from point (x1,y1) to (x2,y2).
     * Uses Axis-Aligned Bounding Box (AABB) Intersection
     *
     * @return True iff this Shape2D is overlapped by the line.
     */
    private boolean overlapsLine(Line2D other) {
        /*
        float fMin = 0;
        float fMax = 1;

        float[] lineMin = {Math.min(x1, x2), Math.min(y1, y2)};
        float[] lineMax = {Math.max(x1, x2), Math.max(y1, y2)};
        float[] boxMin = {this.x - this.xLength/2, this.y - this.yLength/2};
        float[] boxMax = {this.x + this.xLength/2, this.y + this.yLength/2};

        for (int i = 0; i < 2; i++) {
            float lineDist = lineMax[i] - lineMin[i];
            if (!compareFloat(lineDist, 0)) {
                fMin = Math.max(fMin, (boxMin[i] - lineMin[i]) / lineDist);
                fMax = Math.min(fMax, (boxMax[i] - lineMin[i]) / lineDist);
                if (fMin > fMax)
                    return false;

            } else if (lineMin[i] < boxMin[i] || lineMax[i] > boxMax[i])
                return false;
        }
         */
        return true;
    }

    /**TODO
     * Finds the minimum straight-line distance between the edges of this collision mask and the given line.
     * Returns 0 if intersecting.
     *
     * @return      The minimum straight-line distance
     */
    private float distanceToPoint(Point2D other) {
        /*
        // don't sqrt anything you don't have to
        float segmentLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (Float.compare(segmentLength, 0) == 0) {
            return distance(new Point2D(x1, y1));
        }

        // how far along the line segment is the closest point to us?
        float unclamped = ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1)) / segmentLength;
        float clamped = Math.max(0f, Math.min(1f, unclamped));

        return distance(new Point2D(x1 + clamped * (x2 - x1), y1 + clamped * (y2 - y1)));
        */
        return 0;
    }

    private float distanceToCircle(Circle2D other) {
        Point2D centre = new Point2D(x, y);
        //return centre.distance(x1, y1, x2, y2) - radius;
        return 0;
    }

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and the given line.
     * returns negative if touching(distance 0) or overlapping.
     * Expects a line that does not have 0 length.
     * Currently implements this.overlaps(x1, y1, x2, y2) to check for collision
     * */
    private float distanceToBox(Box2D other) {
        /*

        if (this.overlapsLine(x1, y1, x2, y2)) {
        	return -1;
        }

        float distX1 = Math.abs(x1 - this.x) - this.xLength/2;
        float distY1 = Math.abs(y1 - this.y) - this.yLength/2;
        float distX2 = Math.abs(x2 - this.x) - this.xLength/2;
        float distY2 = Math.abs(y2 - this.y) - this.yLength/2;
        float maxLineX = x1 >= x2 ? x1 : x2;
        float minLineX = x1 <= x2 ? x1 : x2;
        float maxLineY = y1 >= y2 ? y1 : y2;
        float minLineY = y1 <= y2 ? y1 : y2;
        float maxBoxX = this.x + this.xLength/2;
        float minBoxX = this.x - this.xLength/2;
        float maxBoxY = this.y + this.yLength/2;
        float minBoxY = this.y - this.yLength/2;


        //gradient = 0 cases
        if (compareFloat(x1, x2)) {
            if (minBoxY <= maxLineY ) {
                if(minLineY <= maxBoxY) {
                    return distX1;      //line overlaps Box on Y, return X dist
                } else {
                    return new Point2D(x1, minLineY).distance(  // line top left or top right of box
                            minBoxX, maxBoxY, maxBoxX, maxBoxY);
                }
            } else {
                return new Point2D(x1, maxLineY).distance(      //line bottom left or bottom right box
                        minBoxX, minBoxY, maxBoxX, minBoxY);
            }
        }

        if (compareFloat(y1, y2)) {
            if (minBoxX <= maxLineX ) {
                if(minLineX <= maxBoxX) {
                    return distY1;      //line overlaps Box on X, return Y dist
                } else {
                    return new Point2D(minLineX, y1).distance(  // line top right or bottom right of box
                            maxBoxX, minBoxY, maxBoxX, maxBoxY);
                }
            } else {
                return new Point2D(maxLineX, y1).distance(      //line top left or bottom left of box
                        minBoxX, minBoxY, minBoxX, maxBoxY);
            }
        }

        //closest point overlaps on one axis
        //e.g. point one is within the vertical bounds of the box & closer than point 2 on x axis
        if (distY1 <= 0 && (this.x < x1 && x1 < x2 || x2 < x1 && x1 < this.x))
            return distX1;
        if (distY2 <= 0 && (this.x < x2 && x2 < x1 || x1 < x2 && x2 < this.x))
            return distX2;
        if (distX1 <= 0 && (this.y < y1 && y1 < y2 || y2 < y1 && y1 < this.y))
            return distY1;
        if (distX2 <= 0 && (this.y < y2 && y2 < y1 || y1 < y2 && y2 < this.y))
            return distY2;


        //both points in one diagonal
        if (minLineX >= maxBoxX && minLineY >= maxBoxY) {     //top right
            return new Point2D(maxBoxX, maxBoxY).distance(x1, y1, x2, y2); }
        if (maxLineX <= minBoxX && minLineY >= maxBoxY) {     //top left
            return new Point2D(minBoxX, maxBoxY).distance(x1, y1, x2, y2); }
        if (minLineX >= maxBoxX && maxLineY <= minBoxY) {     //bot right
            return new Point2D(maxBoxX, minBoxY).distance(x1, y1, x2, y2); }
        if (maxLineX <= minBoxX && maxLineY <= minBoxY) {     //bot left
            return new Point2D(minBoxX, minBoxY).distance(x1, y1, x2, y2); }


        // gradient of line cannot be 0, calculate the equation of the line (y = mx + b)
        float m = (y1 - y2) / (x1 - x2);
        float b = y1 - m * x1;
        float boxCentreY = m * this.x + b;

        Point2D closestCorner;
        if (m > 0) {                //rising line
            if (boxCentreY > this.y) {  //passes above, box therefor top left
                closestCorner = new Point2D(minBoxX, maxBoxY);
            } else {                    // passes below, box therefor bot right
                closestCorner = new Point2D(maxBoxX, minBoxY);
            }
        } else {                    //decending line
            if (boxCentreY > this.y) {  //passes above box, therefor top right
                closestCorner = new Point2D(maxBoxX, maxBoxY);
            } else {                    // passes below box, therefor bot left
                closestCorner = new Point2D(minBoxX, minBoxY);
            }
        }

        return closestCorner.distance(x1, y1, x2, y2);
         */
        return 0;

    }

    private float distanceToLine(Line2D other) {
        return 0;
    }


    // ----------     Abstract Methods    ---------- //

    @Override
    public Shape2D copy() {
        return new Line2D( x1, y1, x2, y2);
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
     * TODO
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

    /**
     * !!! TODO copied from LightningEffect, clean up
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

        if (Float.compare(line2D.x1, x1) != 0)
            return false;
        if (Float.compare(line2D.y1, y1) != 0)
            return false;
        if (Float.compare(line2D.x2, x2) != 0)
            return false;
        if (Float.compare(line2D.y2, y2) != 0)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return this.x1 + ", " + this.y1 + ", " + this.x2 + ", " + this.y2 ;
    }


}
