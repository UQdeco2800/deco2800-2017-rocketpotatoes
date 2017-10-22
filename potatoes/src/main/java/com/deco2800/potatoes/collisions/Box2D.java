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

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import static com.deco2800.potatoes.util.MathUtil.compareFloat;

/**
 * A centred box class that implements Shape2D.
 * Can be used to check distance or overlaps with other Shape2D's.
 * Can render to isometric view.
 * Being used by AbstractEntity & descendents for collision
 *
 * @author Tazman_Schmidt
 */
public class Box2D extends Shape2D {

    private float xLength;
    private float yLength;
    private static final String TEXTURE_STRING = "BOX_HIGHLIGHT";

    private Vector3 c1;//corners during screen render
    private Vector3 c2;
    private Vector3 c3;
    private Vector3 c4;

    /**
     * Create a new Box2D at a specific point with a length in the x and y dimension.
     * Any negative lengths will be swapped to positive values.
     *
     * @param x Centre-point x
     * @param y Centre-point y
     * @param xLength width along x axis
     * @param yLength height along y axis
     */
    public Box2D(float x, float y, float xLength, float yLength) {
        this.x = x;
        this.y = y;
        setXLength(xLength);
        setYLength(yLength);
    }

    /**
     * Creates a minimum bounding rectangle that surrounds all of internals.
     *
     * @param internals
     *      The shapes being surrounded by the created box.
     *
     * @return The box, if it is possible to create one from the internals, otherwise nothing.
     */
    public static Optional<Box2D> surrounding(Stream<Shape2D> internals) {
        List<Point2D> points = internals.filter(shape -> shape != null).flatMap(shape -> {
            Optional<Box2D> bounds = shape.getBoundingBox();
            if (bounds.isPresent()) {
                float x = bounds.get().x;
                float y = bounds.get().y;
                float xRadius = bounds.get().xLength / 2;
                float yRadius = bounds.get().yLength / 2;
                return Stream.of(new Point2D(x - xRadius, y - yRadius),
                        new Point2D(x + xRadius, y - yRadius),
                        new Point2D(x - xRadius, y + yRadius),
                        new Point2D(x + xRadius, y + yRadius));
            } else {
                return Stream.of(new Point2D(shape.getX(), shape.getY()));
            }
        }).collect(Collectors.toList());

        if (points.isEmpty()) {
            return Optional.empty();
        }

        float minX = points.stream().map(point -> point.getX()).min(Comparator.naturalOrder()).get();
        float minY = points.stream().map(point -> point.getY()).min(Comparator.naturalOrder()).get();
        float maxX = points.stream().map(point -> point.getX()).max(Comparator.naturalOrder()).get();
        float maxY = points.stream().map(point -> point.getY()).max(Comparator.naturalOrder()).get();

        if (compareFloat(minX, maxX) || compareFloat(minY, maxY)) {
            return Optional.empty();
        }

        return Optional.of(new Box2D(
                    (maxX + minX) / 2,
                    (maxY + minY) / 2,
                    0.01f + maxX - minX,
                    0.01f + maxY - minY));
    }

    /**
     * Makes a copy of the current Box2D.
     *
     * @return A copy of the current Box2D
     */
    @Override
    public Box2D copy() {
        return new Box2D(x, y, xLength, yLength);
    }

    @Override
    public float getArea() {
        return xLength * yLength;
    }


    /**
     * Returns the length in the x direction.
     *
     * @return Returns the x length.
     */
    public float getXLength() {
        return this.xLength;
    }

    /**
     * Set the length in the x direction.
     * A negative length will be reversed.
     *
     * @param xLength The desired x length.
     */
    public void setXLength(float xLength) {
        this.xLength = xLength >= 0 ? xLength : -xLength ;
    }

    /**
     * Returns the length in the y direction.
     *
     * @return Returns the y length.
     */
    public float getYLength() {
        return this.yLength;
    }

    /**
     * Sets the length in the y direction.
     * A negative length will be reversed.
     *
     * @param yLength The desired y length.
     */
    public void setYLength(float yLength) {
        this.yLength = yLength >= 0 ? yLength : -yLength ;
    }


    /**
     * Returns True iff this Box2D overlaps the given Point2D
     *
     * @param other The other Shape2D being checked
     * @return True iff this box2D overlaps the Shape2D
     */
    private boolean overlapsPoint(Point2D other) {
        // Check x non collision
        if ( Math.abs(this.x - other.getX()) >= this.xLength/2) {
            return false;
        }

        // Check y non collision
        if ( Math.abs(this.y - other.getY()) >= this.yLength/2) {
            return false;
        }

        return true;
    }

    /**
     * Returns True iff this Box2D overlaps the given Circle2D
     *
     * @param other The other Shape2D being checked
     * @return True iff this box2D overlaps the Shape2D
     */
    private boolean overlapsCircle(Circle2D other) {
        // We will consider the circle to be a point
        // and the rectangle to be a rounded rectangle
        // (adding the radius to the outside of the rectangle)

        // Collapse down the dimensions, so we're considering one corner of the rectangle
        float distX = Math.abs(other.getX() - this.x);
        float distY = Math.abs(other.getY() - this.y);

        // Point is outside collision
        if (distX >= this.xLength/2 + other.getRadius() ||
                distY >= this.yLength/2 + other.getRadius())
            return false;

        // Point is inside collision
        if (distX < this.xLength/2 ||
                distY < this.yLength/2)
            return true;


        // May intersect corner scenario, calc oblique distance square
        float cornerX = distX - this.xLength / 2;
        float cornerY = distY - this.yLength / 2;
        float cornerDistSquare = cornerX * cornerX + cornerY * cornerY;

        return cornerDistSquare < other.getRadius() * other.getRadius();
    }

    /**
     * Returns True iff this Box2D overlaps the given Box2D
     *
     * @param other The other Shape2D being checked
     * @return True iff this box2D overlaps the Shape2D
     */
    private boolean overlapsBox(Box2D other) {
        // Calc centre to centre dist
        float distX = Math.abs(other.getX() - this.x);
        float distY = Math.abs(other.getY() - this.y);

        // Check dist's are large enough that no collision could occur
        if (distX >= (this.xLength + other.getXLength())/2) {
        	return false;
        }
        if (distY >= (this.yLength + other.getYLength())/2) {
        	return false;
        }

        return true;
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
            return overlapsPoint((Point2D)other);
        } else if (other instanceof Circle2D) {
            return overlapsCircle((Circle2D)other);
        } else if (other instanceof Box2D) {
            return overlapsBox((Box2D)other);
        } else {
            return other.overlaps(this);
        }
    }


    /**
     * Subroutine used in both point-to-box & box-to-box distance.
     *
     * @param distX The x value of the point.
     * @param distY The y value of the point.
     * @return The minimum distance between this Box2D and the coordinates.
     */
    private float calculateDistance(float distX, float distY) {
        if (distX >= 0 && distY >= 0) {
            // Box & point are diagonal to each other, calc corner point to point dist
            return (float) Math.sqrt(distX * distX + distY * distY);
        } else if (distX >= 0) {
            // Box & point overlap on x co-ord but not y
            return distX;
        } else if (distY >= 0) {
            // Box & point overlap on y co-ord but not x
            return distY;
        } else {
            // Box & point overlap, return rough negative val
            return Math.max(distX, distY);
        }
    }

    /**
     * Finds the minimum distance between this Box2D and the given Point2D
     *
     * @param other The other Shape2D being checked
     * @return The minimum distance to the given Shape2D
     */
    private float distanceToPoint(Point2D other) {
        Point2D point = other;

        // Calc dist between sides on each dimension
        float distX = Math.abs(point.getX() - this.x) - this.xLength/2;
        float distY = Math.abs(point.getY() - this.y) - this.yLength/2;

        return calculateDistance(distX, distY);
    }

    /**
     * Finds the minimum distance between this Box2D and the given Circle2D
     *
     * @param other The other Shape2D being checked
     * @return The minimum distance to the given Shape2D
     */
    private float distanceToCircle(Circle2D other) {
        // Calc dist between sides on each dimension, considering the circle as a point
        float distPointX = Math.abs(other.getX() - this.x) - this.xLength/2;
        float distPointY = Math.abs(other.getY() - this.y) - this.yLength/2;

        // Calc dist between sides on each dimension
        float distX = distPointX - other.getRadius();
        float distY = distPointY - other.getRadius();

        if (distX >= 0 && distPointY < 0) {
            // Box & circle overlap on x co-ord but not y
            return distX;
        } else if (distY >= 0 && distPointX < 0) {
            // Box & circle overlap on y co-ord but not x
            return distY;
        } else if (distX >= 0 && distY >= 0) {
            // Box & circle are diagonal to each other, calc corner point to point dist
            return (float) Math.sqrt(distPointX * distPointX + distPointY * distPointY) - other.getRadius();
        } else {
            // Box & circle overlap, return negative val
            
            return -1;
        }
    }

    /**
     * Finds the minimum distance between this Box2D and the given Box2D
     *
     * @param other The other Shape2D being checked
     * @return The minimum distance to the given Shape2D
     */
    private float distanceToBox(Box2D other) {
        // Calc dist between sides on each dimension
        float distX = Math.abs(other.getX() - this.x) - (this.xLength + other.getXLength()) / 2;
        float distY = Math.abs(other.getY() - this.y) - (this.yLength + other.getYLength()) / 2;

        return calculateDistance(distX, distY);
    }

    /**
     * Used during rendering to get the screen coords of the corners of this shape
     */
    private void rendSetCorners() {
        OrthographicCamera camera = GameManager.get().getManager(CameraManager.class).getCamera();

        //calculate orthagonal corners of box
        Vector2 screenWorldCoords = Render3D.worldToScreenCoordinates(x + xLength/2, y + yLength/2, 0);
        c1 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

        screenWorldCoords = Render3D.worldToScreenCoordinates(x - xLength/2, y + yLength/2, 0);
        c2 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

        //if square, optimise a little
        if (compareFloat(xLength, yLength)) {
            //if square, reflect screen coords
            c3 = new Vector3(c2.x * 2 - c1.x, c1.y, 0);
            c4 = new Vector3(c2.x, c1.y * 2 - c2.y, 0);
        } else {
            screenWorldCoords = Render3D.worldToScreenCoordinates(x - xLength/2, y - yLength/2, 0);
            c3 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));
            c4 = new Vector3(c1.x - c2.x + c3.x, c1.y * 2 - c2.y, 0);
        }
    }

    /**
     * Renders the fill of this shape using an current shapeRenderer
     * @param shapeRenderer a shapeRenderer that has run begin() & setcolour() already
     */
    @Override
    public void renderShape(ShapeRenderer shapeRenderer) {
        rendSetCorners();

        //use 2 triangles to get diamond shape
        shapeRenderer.triangle(c1.x, c1.y, c2.x, c2.y, c3.x, c3.y);
        shapeRenderer.triangle(c1.x, c1.y, c4.x, c4.y, c3.x, c3.y);
    }

    /**
     * Renders the outline of this shape using an current shapeRenderer
     * @param shapeRenderer a shapeRenderer that has run begin() & setcolour() already
     */
    public void renderShapeOutline(ShapeRenderer shapeRenderer) {
        rendSetCorners();

        float[] corners = {c1.x, c1.y, c2.x, c2.y, c3.x, c3.y, c4.x, c4.y};

        //use polygon
        shapeRenderer.polygon(corners);
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

        batch.draw(textureHighlight,
                isoPosition.x - tileWidth * xLength / 2, isoPosition.y - tileWidth * yLength / 2,   // x, y
                tileWidth * xLength, textureHighlight.getHeight() / aspect * yLength);              // width, height

    }

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and another collision mask.
     * This function is symmetric.
     * Returns 0 iff on the edge.
     *
     * @param other The other collision mask.
     * @return The distance. If the collision masks overlap, a negative number is returned.
     */
    @Override
    public float distance(Shape2D other) {
        if (other instanceof Point2D) {
            return distanceToPoint((Point2D)other);
        } else if (other instanceof Circle2D) {
            return distanceToCircle((Circle2D)other);
        } else if (other instanceof Box2D) {
            return distanceToBox((Box2D)other);
        } else {
            return other.distance(this);
        }
    }



    @Override
    public int hashCode() {
        return Objects.hash(x, y, xLength, yLength);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Box2D box2D = (Box2D) o;

        if (Float.compare(box2D.x, x) != 0)
            return false;
        if (Float.compare(box2D.y, y) != 0)
            return false;
        return Float.compare(box2D.xLength, xLength) == 0 && Float.compare(box2D.yLength, yLength) == 0;
    }

    /**
     * Returns the variables of this Box2D in the form:
     * "<x>, <y>, <xLength>, <yLength>"
     *
     * @return This Box2D's parameters
     */
    @Override
    public String toString() {
        return this.x + ", " + this.y + ", " + this.xLength + ", " + this.yLength;
    }

    @Override
    public Optional<Box2D> getBoundingBox() {
        return Optional.of(this.copy());
    }
}
