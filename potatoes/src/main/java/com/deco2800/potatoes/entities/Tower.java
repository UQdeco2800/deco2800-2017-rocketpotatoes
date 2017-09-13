package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.trees.ProjectileTree;

/**
 * Tower that can do things.
 * 
 * @author leggy
 *
 */
public class Tower extends ProjectileTree {

	private static final String TEXTURE = "tower";

	/**
	 * Default constructor for serialization
	 */
	public Tower() {
		// generic constructor
	}

	/**
	 * Constructor for the base
	 * 
	 * @param world
	 *            The world of the tower.
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 */
	public Tower(float posX, float posY, float posZ) {
		super(posX, posY, posZ, TEXTURE, 1000, 8f, 100f);
		this.setStaticCollideable(true);
	}

	@Override
	public String toString() {
		return String.format("Tower at (%d, %d)", (int) getPosX(), (int) getPosY());
	}
}
