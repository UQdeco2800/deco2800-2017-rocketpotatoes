package com.deco2800.potatoes.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
		super();
		this.setStaticCollideable(false);
	}

	public ScreenEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture);
		this.setStaticCollideable(false);
	}

	public ScreenEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			boolean centered, String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, centered, texture);
		this.setStaticCollideable(false);
		
	}
	
	@Override
	public boolean collidesWith(AbstractEntity entity) {
		return false;
	}

}
