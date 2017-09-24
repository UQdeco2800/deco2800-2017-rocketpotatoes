package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.collisions.Box2D;

/**
 * A special entity that acts like a gui but exists on a specific tile and moves
 * with the rest of the world. These can not collide with anything and are not
 * ticked like AbstractEntities.
 */
public abstract class ScreenEntity extends AbstractEntity implements Clickable {

	/**
	 * Default constructor for the purposes of serialization
	 */
	
	
	public ScreenEntity() {
        this(0, 0, 0, 0, "");
	}

    public ScreenEntity(float posX, float posY, float xLength, float yLength, String texture) {
        super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, texture);
    }
	
	@Override
	public boolean collidesWith(AbstractEntity entity) {
		return false;
	}

}
