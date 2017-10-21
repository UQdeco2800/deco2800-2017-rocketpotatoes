package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.util.Path;

/***
 * Data class for Path and Shape2D variables used by enemies for path finding
 */
public class PathAndTarget {
    private Path path;
    private Shape2D target;

    /**
     * Constructor to initialize the class with provided Shape2D target (the target the enemy would like to move
     * toward) and the path to it.
     *
     * @param path
     * @param target
     */
    public PathAndTarget(Path path, Shape2D target){
        this.path = path;
        this.target = target;
    }

    /**
     * @return the contained path
     */
    public Path getPath(){
        return this.path;
    }

    /**
     * @return the contained target
     */
    public Shape2D getTarget(){
        return this.target;
    }

    /**
     * Set the path contained by this class
     *
     * @param path
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * Set the target contained by this class
     *
     * @param target
     */
    public void setTarget(Shape2D target) {
        this.target = target;
    }
}
