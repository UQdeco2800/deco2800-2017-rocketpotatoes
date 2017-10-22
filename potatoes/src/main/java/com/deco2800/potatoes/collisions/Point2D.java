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

import java.util.Objects;
import java.util.Optional;

/**
 * A point class that implements Shape2D.
 * Can be used to check distance or overlaps with other Shape2D's.
 * Can render to isometric view.
 * Being used by AbstractEntity & descendents for collision
 *          & by PathManger to represent points in a path
 *
 * @author Tazman_Schmidt
 */
public class Point2D extends Shape2D {

    private static final String TEXTURE_STRING = "POINT_HIGHLIGHT";

    /**
     * Default constructor for the purposes of serialization.
     */
    public Point2D() {
        //Empty constructor because Sonar
    }

    /**
     * Constructs a new point at a given location.
     *
     * @param x
     *              The X coordinate of the point.
     * @param y
     *              The Y coordinate of the point.
     */
    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Makes a copy of the current Point2D.
     *
     * @return A copy of the current Point2D
     */
    @Override
    public Point2D copy() {
        return new Point2D(x, y);
    }

    @Override
    public float getArea() {
        return 0;
    }

    /**
     * Checks if this collision mask overlaps another collision masks.
     * This function is symmetric.
     * Touching the edge is not considered as overlapping.
     *
     * @param other The other collision mask.
     * @return True iff the collision masks are overlapping.
     */
    @Override
    public boolean overlaps(Shape2D other) {
        if (other instanceof Point2D) {
            return this.equals(other);
        } else {
            return other.overlaps(this);
        }
    }

    /**
     * Finds the minimum straight-line distance between this collision mask and another collision mask.
     * This function is symmetric.
     *
     * @param other     The other collision mask.
     * @return  The distance. If the collision masks overlap, a negative number is returned.
     */
    @Override
    public float distance(Shape2D other) {
        if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            float distX = point.getX() - this.x;
            float distY = point.getY() - this.y;

            // use pythagorean theorem
            return (float) Math.sqrt((double) distX * distX + distY * distY );
        } else {
            return other.distance(this);
        }
    }

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and the given line.
     * Returns 0 if intersecting.
     *
     * @param x1    The x coord of point 1 of the line
     * @param y1    The y coord of point 1 of the line
     * @param x2    The x coord of point 2 of the line
     * @param y2    The y coord of point 2 of the line
     * @return      The minimum straight-line distance
     */
    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        // don't sqrt anything you don't have to
        float segmentLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (Float.compare(segmentLength, 0) == 0) {
            return distance(new Point2D(x1, y1));
        }

        // how far along the line segment is the closest point to us?
        float unclamped = ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1)) / segmentLength;
        float clamped = Math.max(0f, Math.min(1f, unclamped));

        return distance(new Point2D(x1 + clamped * (x2 - x1), y1 + clamped * (y2 - y1)));
    }

    /**
     * Renders an X using this shape using an current shapeRenderer
     * @param shapeRenderer a shapeRenderer that has run begin() & setcolour() already
     */
    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {
        OrthographicCamera camera = GameManager.get().getManager(CameraManager.class).getCamera();

        //calculate orthagonal corners of box
        Vector2 screenWorldCoords = Render3D.worldToScreenCoordinates(x , y, 0);
        Vector3 v = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

        //render ellipse
        float rt2 = (float) Math.sqrt(2);
        float size = 5;     
        float width = 3;
                                //x1, y1, x2, y2, width
        shapeRenderer.rectLine(v.x - size, v.y - size * rt2, v.x + size, v.y + size * rt2, width);
        shapeRenderer.rectLine(v.x - size, v.y + size * rt2, v.x + size, v.y - size * rt2, width);
    }

    /**
     * Renders an X image where this shape is, in the isometric game view
     * @param batch Batch to render outline image onto
     */
    @Override
    public void renderHighlight(SpriteBatch batch) {

        Texture textureHighlight  = GameManager.get().getManager(TextureManager.class).getTexture(TEXTURE_STRING);

        Vector2 isoPosition = Render3D.worldToScreenCoordinates(x, y, 0);

        batch.draw(textureHighlight,  isoPosition.x, isoPosition.y);
    }


    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Point2D point2D = (Point2D) o;

        if (Float.compare(point2D.x, x) != 0)
            return false;
        return Float.compare(point2D.y, y) == 0;
    }

    /**
     * Returns the variables of this Point2D in the form:
     * "<x>, <y>"
     *
     * @return This Point2D's parameters
     */
    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }

    @Override
    public Optional<Box2D> getBoundingBox() {
        return Optional.empty();
    }
}
