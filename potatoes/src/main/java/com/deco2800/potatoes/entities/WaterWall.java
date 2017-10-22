package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.collisions.Box2D;

public class WaterWall extends AbstractEntity{

    public WaterWall(int x, int y) {
        super(new Box2D(x + 1, y, 1, 1), true, true, false, 1, 1, 0, 0, null);
    }

}
