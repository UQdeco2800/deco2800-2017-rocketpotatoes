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
import static com.deco2800.potatoes.util.MathUtil.compareFloat;

/**
 * A centred circle class that implements Shape2D.
 * Can be used to check distance or overlaps with other Shape2D's.
 * Can render to isometric view.
 * Being used by AbstractEntity & descendents for collision
 *
 * @author Tazman_Schmidt
 */
public class Circle2D extends Shape2D {

    private float radius;
    private static final String TEXTURE_STRING = "CIRCLE_HIGHLIGHT";


    /**
     * Create a new Box2D at a specific point with a given radius.
     * Any negative radius will be swapped to positive.
     *
     * @param x Centre-point x
     * @param y Centre-point y
     * @param radius The radius of the circle.
     */
    public Circle2D(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.setRadius(radius); //converts neg radius
    }

    /**
     * Makes a copy of the current Circle2D.
     *
     * @return A copy of the current Circle2D
     */
    @Override
    public Circle2D copy() {
        return new Circle2D(x, y, radius);
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
            Point2D point = (Point2D) other;
            return distance(point) < 0;
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;
            return distance(otherCircle) < 0;
        } else {
            return other.overlaps(this);
        }
    }

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and another collision mask.
     * This function is symmetric.
     *
     * @param other The other collision mask.
     * @return The distance. If the collision masks overlap, a negative number is returned.
     */
    @Override
    public float distance(Shape2D other) {
        if (other instanceof Point2D) {
            Point2D point = (Point2D) other;

            float distX = Math.abs(point.getX() - this.x);
            float distY = Math.abs(point.getY() - this.y);

            // use pythagorean theorem
            float dist = (float) Math.sqrt((double) distX * distX + distY * distY );

            return dist - radius;
        } else if (other instanceof Circle2D) {
            Circle2D otherCircle = (Circle2D) other;

            float distX = Math.abs(otherCircle.getX() - this.x);
            float distY = Math.abs(otherCircle.getY() - this.y);

            // use pythagorean theorem
            float dist = (float) Math.sqrt((double) distX * distX + distY * distY );

            // subtract radius's
            dist -= otherCircle.getRadius() + this.radius;

            return dist;
        } else {
            return other.distance(this);
        }
    }

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and the given line.
     *
     * @param x1    The x coord of point 1 of the line
     * @param y1    The y coord of point 1 of the line
     * @param x2    The x coord of point 2 of the line
     * @param y2    The y coord of point 2 of the line
     * @return      The minimum straight-line distance
     */
    @Override
    public float distance(float x1, float y1, float x2, float y2) {
        Point2D centre = new Point2D(x, y);
        return centre.distance(x1, y1, x2, y2) - radius;
    }

    /**
     * Renders the fill or outline of this shape using an current shapeRenderer
     * @param shapeRenderer a shapeRenderer that has run begin() & setcolour() already
     */
    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {
        OrthographicCamera camera = GameManager.get().getManager(CameraManager.class).getCamera();

        //calculate orthagonal corners of box
        Vector2 screenWorldCoords = Render3D.worldToScreenCoordinates(x + radius, y + radius, 0);
        Vector3 c1 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

        screenWorldCoords = Render3D.worldToScreenCoordinates(x - radius, y + radius, 0);
        Vector3 c2 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

        Vector3 c3 = new Vector3(c2.x * 2 - c1.x, c1.y, 0); //c3 is c1 reflected on x
        Vector3 c4 = new Vector3(c2.x, c1.y * 2 - c2.y, 0); //c4 is c2 reflected on y

        //render ellipse
        float rt2 = (float) Math.sqrt(2);
        shapeRenderer.ellipse( c2.x - (c2.x - c3.x)/rt2, c1.y - (c1.y - c4.y)/rt2,
                rt2 * (c1.x - c2.x), rt2  * (c2.y - c1.y));
    }

    /**
     * Renders an outline image where this shape is, in the isometric game view
     * @param batch Batch to render outline image onto
     */
    @Override
    public void renderHighlight(SpriteBatch batch) {
        Texture textureHighlight  = GameManager.get().getManager(TextureManager.class).getTexture(TEXTURE_STRING);

        Vector2 isoPosition = Render3D.worldToScreenCoordinates(x, y, 0);

        int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        float aspect = (float) textureHighlight.getWidth() / (float) tileWidth;
        float rt2 = (float) Math.sqrt(2);

        batch.draw(textureHighlight,
                isoPosition.x - tileWidth * radius / rt2, isoPosition.y - tileWidth * radius / rt2, // x, y
                tileWidth * radius * rt2, textureHighlight.getHeight() / aspect * radius * rt2);    // width, height

    }


    /**
     * Returns the radius of this Circle2D.
     *
     * @return Returns the radius.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Sets the radius of the Circle2D.
     * A negative radius will be reversed.
     *
     * @param radius The new radius.
     */
    public void setRadius(float radius) {
        this.radius = radius >= 0 ? radius : -radius ;
    }

    /**
     * intended use for following a path
     * @return if this circle is approximately centred on a point
     */
    public boolean isCentredOnPoint(Point2D point) {
        return compareFloat(x, point.getX()) && compareFloat(y, point.getY());
    }

    /**
     * @return the area of this shape
     */
    @Override
    public float getArea() {
        return (float) Math.PI *radius * radius;
    }


    @Override
    public int hashCode() {
        return Objects.hash(x, y, radius);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Circle2D circle2D = (Circle2D) o;

        if (Float.compare(circle2D.x, x) != 0)
            return false;
        if (Float.compare(circle2D.y, y) != 0)
            return false;
        return Float.compare(circle2D.radius, radius) == 0;
    }

    /**
     * Returns the variables of this Circle2D in the form:
     * "<x>, <y>, <radius>"
     *
     * @return This Circle2D's parameters
     */
    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.radius;
    }

    @Override
    public Optional<Box2D> getBoundingBox() {
        return Optional.of(new Box2D(x, y, radius * 2, radius * 2));
    }
}
