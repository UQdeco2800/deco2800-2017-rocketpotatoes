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

	public ProjectileTree(float posX, float posY, float posZ, String texture, int reloadTime, float range) {
		super(posX, posY, posZ, 1f, 1f, 1f, texture);

		registerNormalEvent(new TreeProjectileShootEvent(this.getBox3D(), reloadTime, range));
	}
}
