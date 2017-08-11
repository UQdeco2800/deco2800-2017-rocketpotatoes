package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Tickable;

public class ProjectileTree extends AbstractTree implements Tickable {
	public int level;
	public int hp;
	public int speed;

	private transient UpgradeStats level1 = new UpgradeStats(4, 4);
	// public whateveraprojectileis projectile;

	/**
	 * Default constructor for serialization
	 */
	public ProjectileTree() {
	}

	public ProjectileTree(float posX, float posY, float posZ, String texture, int reloadTime, float range) {
		super(posX, posY, posZ, 1f, 1f, 1f, texture);

		registerTimeEvent(new TreeProjectileShootEvent(this.getBox3D(), reloadTime, range));
	}

	public void init() {
		level = 1;
		hp = 1;
		speed = 1;
	}

	public void upgrade() {
		// add upgrade numbers later
		switch (level) {
		case 1:
			hp = level1.getHp();
			speed = level1.getSpeed();
			break;
		case 2:
			hp = 10;
			break;
		}
		level++;
	}

}
