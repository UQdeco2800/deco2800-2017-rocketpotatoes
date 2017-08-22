package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

/**
 * Resource tree offer a means to collect resources over time.
 * 
 */
public class ResourceTree extends AbstractTree implements Tickable {
	
	/* Resource Tree Attributes */
	public int resourceCount;	// Number of resources currently gathered
	
	// Maximum amount of resources held by resource tree at any given instance
	public static final int MAX_RESOURCE_COUNT = 99;	
	
	/* Stats that apply to all resource trees */
	public static final int HP = 8; // Health of the tree
	public static final int RATE = 5000; // Rate resources are earned
	public static final float AMOUNT = 1f; // Number of resourced earned per gather
	public static final int CONSTRUCTION_TIME = 2500; // Construction time
	public static final String TEXTURE = "tree_selected"; // Texture name
	
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
	 * @param gatherRate
	 * 			The interval in milliseconds that resources are gathered.
	 * @param gatherAmount
	 * 			The amount of resources gathered per interval.
	 * @param maxHealth
	 * 			The initial maximum health of the tower.
	 */
	public ResourceTree(float posX, float posY, float posZ, String texture, float maxHealth) {
		super(posX, posY, posZ, 1f, 1f, 1f, texture, maxHealth);
	}
	
	@Override
	public List<UpgradeStats> getAllUpgradeStats() {
		return STATS;
	}
	
	/**
	 * Configure the stats for the resource tree.
	 * 
	 */
	private static List<UpgradeStats> initStats() {
		List<UpgradeStats> result = new LinkedList<>();
		List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
		List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
		
		// Base State
		result.add(new UpgradeStats(HP, RATE, AMOUNT, CONSTRUCTION_TIME, normalEvents, constructionEvents, TEXTURE)); 
		// Upgrade 1
		result.add(new UpgradeStats(HP+12, RATE-1000, AMOUNT+1, CONSTRUCTION_TIME-500, normalEvents, constructionEvents, TEXTURE)); 
		// Upgrade 2
		result.add(new UpgradeStats(HP+22, RATE-1500, AMOUNT+2, CONSTRUCTION_TIME-1000, normalEvents, constructionEvents, TEXTURE)); 
		
		// Add ResourceGatherEvent to each upgrade level
		for (UpgradeStats upgradeStats : result) {
			upgradeStats.getNormalEventsReference().add(new ResourceGatherEvent(upgradeStats.getSpeed(), 
					Math.round(upgradeStats.getRange())));
		}

		return result;
	}

}