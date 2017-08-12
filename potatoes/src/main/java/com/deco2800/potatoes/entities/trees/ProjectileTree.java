package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.Tickable;

public class ProjectileTree extends AbstractTree implements Tickable {
	public int level;
	public int hp;
	public int speed;
	public UpgradeStats stats;
	/**
	 * Default constructor for serialization
	 */
	public ProjectileTree() {
	}
    
	public ProjectileTree(float posX, float posY, float posZ, String texture, int reloadTime, float range) {
		super(posX, posY, posZ, 1f, 1f, 1f, texture);

		stats= new UpgradeStats(10, 1000,new TimeEvent[] {new TreeProjectileShootEvent(this.getBox3D(), 1000, range)},null,"tree");
		setUpgradeStats(stats);
		stats= new UpgradeStats(20, 600,new TimeEvent[] {new TreeProjectileShootEvent(this.getBox3D(), 600, 10f)},null,"tree");
        setUpgradeStats(stats);
        stats= new UpgradeStats(30, 300,new TimeEvent[] {new TreeProjectileShootEvent(this.getBox3D(), 300, 12f)},null,"tree");
        setUpgradeStats(stats);
		registerNormalEvent(this.getUpgradeStats().getNormalEvents()[0]);
	}
}
