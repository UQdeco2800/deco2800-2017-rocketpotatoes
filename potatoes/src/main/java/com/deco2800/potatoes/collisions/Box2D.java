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
     * Checks to see if a line intersects with this Box2D.
     * The line goes from point (x1,y1) to (x2,y2).
     * Uses Axis-Aligned Bounding Box (AABB) Intersection
     *
     * @param x1 The x coord of point 1 of the line
     * @param y1 The y coord of point 1 of the line
     * @param x2 The x coord of point 2 of the line
     * @param y2 The y coord of point 2 of the line
     * @return True iff this Shape2D is overlapped by the line.
     */
    public boolean overlapsLine(float x1, float y1, float x2, float y2) {
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

    /**
     * Finds the minimum straight-line distance between the edges of this collision mask and the given line.
     * returns negative if touching(distance 0) or overlapping.
     * Expects a line that does not have 0 length.
     * Currently implements this.overlaps(x1, y1, x2, y2) to check for collision
     *
     * @param x1    The starting X coordinate of the line being checked.
     * @param y1    The starting Y coordinate of the line being checked.
     * @param x2    The ending X coordinate of the line being checked.
     * @param y2    The ending Y coordinate of the line being checked.
     * @return      The minimum straight-line distance
     */
    @Override
    public float distance(float x1, float y1, float x2, float y2) {

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
