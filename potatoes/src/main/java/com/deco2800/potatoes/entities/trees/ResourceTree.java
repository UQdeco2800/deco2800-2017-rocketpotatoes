package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.Inventory;

/**
 * Resource tree offer a means to collect resources over time.
 * 
 */
public class ResourceTree extends AbstractTree implements Tickable {
	
	/* Resource Tree Attributes */
	public int resourceCount;	// Number of resources currently gathered
	public Resource resourceType;	// Type of resource gathered by the tree
	
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
	 * Constructor for creating a basic resource tree at a given 
	 * coordinate. The resource produced by the tree will default to
	 * the seed resource.
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 */
	public ResourceTree(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, null, 0);
		this.resourceCount = 0;
		this.resourceType = new SeedResource();
	}
	
	/**
	 * Constructor for creating a resource tree at a given 
	 * coordinate that gathers a specified resource.
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 * @param resourceType
	 * 			The type of resource gathered by the tree.
	 */
	public ResourceTree(float posX, float posY, float posZ, Resource resourceType) {
		super(posX, posY, posZ, 1f, 1f, 1f, null, 0);
		this.resourceCount = 0;
		this.resourceType = resourceType;
	}
	
	@Override
	public List<UpgradeStats> getAllUpgradeStats() {
		return STATS;
	}
	
	/**
	 * Configures the stats for the resource tree. Each stat level has a 
	 * normal event action for gathering resources based on the level's
	 * speed stat.
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

	/**
	 * @return the percentage of construction left, from 0 to 100
	 */
	public int getResourceAmount() {
		return resourceCount;
	}


	/**
	 * Transfers the resources gathered from the tree
	 * into the specified inventory. Once resources
	 * are transfered, the resourceCount of the tree will 
	 * reset to zero.
	 * 
	 * 	@param inventory
	 * 		The inventory of the player to receive gathered resources
	 */
	public void transferResources(Inventory inventory) {
		try {
			inventory.updateQuantity(this.resourceType, resourceCount);
		} catch (Exception e) {
			// throws if resourceCount is negative
		}
		resourceCount = 0;
	}
	
	@Override
	public String toString() {
		String pos = this.getPosX() + ", " + this.getPosY() + ", " + this.getPosZ();
		return "Resource Tree at (" + pos + ") has " + this.resourceCount 
				+ " " + this.resourceType.toString() + " resources.";
	}
}