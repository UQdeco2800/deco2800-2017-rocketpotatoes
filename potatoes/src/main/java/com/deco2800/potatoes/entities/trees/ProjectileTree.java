package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Tickable;

public class ProjectileTree extends AbstractTree implements Tickable {
	public int level;
	public int hp;
	public int speed;

	/**
	 * Default constructor for serialization
	 */
	public ProjectileTree() {
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
	 * @param reloadTime
	 * @param range
	 * @param maxHealth
	 *            The initial maximum health of the tower
	 */
	public ProjectileTree(float posX, float posY, float posZ, String texture, int reloadTime, 
			float range, float maxHealth) {
		super(posX, posY, posZ, 1f, 1f, 1f, texture, maxHealth);

		registerNormalEvent(new TreeProjectileShootEvent(this.getBox3D(), reloadTime, range));
	}
}
