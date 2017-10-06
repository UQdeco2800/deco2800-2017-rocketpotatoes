package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.util.Path;

public class PathAndTarget {
    private Path path;
    private Shape2D target;

    public PathAndTarget(Path path, Shape2D target){
        this.path = path;
        this.target = target;
    }

    public Path getPath(){
        return this.path;
    }

    public Shape2D getTarget(){
        return this.target;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setTarget(Shape2D target) {
        this.target = target;
    }
}
