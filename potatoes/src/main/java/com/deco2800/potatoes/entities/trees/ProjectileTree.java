package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

public class ProjectileTree extends AbstractTree implements Tickable {
	public int level;
	public int hp;
	public int speed;
	public static final List<UpgradeStats> STATS = initStats();

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
	}

	@Override
	public List<UpgradeStats> getAllUpgradeStats() {
		return STATS;
	}
	
	private static List<UpgradeStats> initStats() {
		List<UpgradeStats> result = new LinkedList<>();
		List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
		List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
		
		result.add(new UpgradeStats(10, 1000, 8f, normalEvents, constructionEvents, "real_tree"));
		result.add(new UpgradeStats(20, 600, 8f, normalEvents, constructionEvents, "real_tree"));
		result.add(new UpgradeStats(30, 100, 8f, normalEvents, constructionEvents, "real_tree"));
		
		for (UpgradeStats upgradeStats : result) {
			upgradeStats.getNormalEventsReference().add(new TreeProjectileShootEvent(upgradeStats.getSpeed()));
		}
		
		return result;
	}
}
