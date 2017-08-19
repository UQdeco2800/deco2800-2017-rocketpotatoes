package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

/**
 * Resource tree offer a means to collect Resources over periods of time.
 * 
 */
public class ResourceTree extends AbstractTree implements Tickable {
	
	/* Resource Tree Attributes */
	public int resourceCount;	// Number of resources currently gathered
	
	// Maximum amount of resources held by resource tree at any given instance
	public static final int MAX_RESOURCE_COUNT = 99;	
	
	/* Initial Resource Tree Stats */
	private static final int HP = 8; // Health of the tree
	private static final int SPEED = 5000; // Rate resources are earned
	private static final float RANGE = 1f; // Number of resourced earned per gather
	private static final int CONSTRUCTION_TIME = 2500; // Construction time
	private static final String TEXTURE = "resource_tree"; // Texture name
	
	public static final List<UpgradeStats> STATS = initStats();

	/**
	 * Default constructor for serialization
	 */
	public ResourceTree() {
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
	public ResourceTree(float posX, float posY, float posZ, String texture, int reloadTime, 
			float range, float maxHealth) {
		super(posX, posY, posZ, 1f, 1f, 1f, texture, maxHealth);
	}
	
	@Override
	public List<UpgradeStats> getAllUpgradeStats() {
		// TODO Auto-generated method stub
		return STATS;
	}
	
	private static List<UpgradeStats> initStats() {
		List<UpgradeStats> result = new LinkedList<>();
		List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
		List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
		
		// Base State
		result.add(new UpgradeStats(HP, SPEED, RANGE, CONSTRUCTION_TIME, normalEvents, constructionEvents, TEXTURE)); 
		// Upgrade 1
		result.add(new UpgradeStats(HP+7, SPEED-1000, RANGE+1, CONSTRUCTION_TIME-500, normalEvents, constructionEvents, TEXTURE)); 
		
		// Add ResourceGatherEvent to each upgrade level
		for (UpgradeStats upgradeStats : result) {
			upgradeStats.getNormalEventsReference().add(new ResourceGatherEvent(upgradeStats.getSpeed(), 
					Math.round(upgradeStats.getRange())));
		}

		return result;
	}

}