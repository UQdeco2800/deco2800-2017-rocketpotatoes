package com.deco2800.potatoes.entities.trees;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.experimental.theories.Theories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.Inventory;

/**
 * Resource tree offer a means to collect resources over time.
 */
public class ResourceTree extends AbstractTree implements Tickable {
	
	/* Logger for all info/warning/error logs */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceTree.class);
	
	/* Resource Tree Attributes */
	private int gatherCount;	// Number of resources currently gathered
	private Resource gatherType;	// Type of resource gathered by the tree
	public boolean gatherEnabled = true; // Gathers resources default
	private int gatherCapacity; // Limit on resources held by resource tree
	public static final int DEFAULT_GATHER_CAPACITY = 32;	 // Default gather capacity, must be > 0
	
	/* Stats that apply to all resource trees */
	public static final int HP = 8; // Health of the tree
	public static final int RATE = 6000; // Rate resources are earned
	public static final float AMOUNT = 1f; // Number of resourced earned per gather
	public static final int CONSTRUCTION_TIME = 2500; // Construction time
	public static final String TEXTURE = "seed_resource_tree"; // Texture name
	
	public static final List<UpgradeStats> STATS = initStats();

	/**
	 * Default constructor for serialization
	 */
	public ResourceTree() {
	}
	
	/**
	 * Constructor for creating a basic resource tree at a given 
	 * coordinate. By default the tree will produce seed resources
	 * and have a gather capacity of 32. 
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
		this.gatherCount = 0;
		this.setGatherCapacity(DEFAULT_GATHER_CAPACITY);
		this.gatherType = new SeedResource();
	}
	
	/**
	 * Constructor for creating a resource tree at a given 
	 * coordinate with a custom resource and gather capacity.
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 * @param resourceType
	 * 			The type of resource gathered by the tree.
	 * @param gatherCapacity
	 * 			maximum number of resources that can be held by the tree.
	 */
	public ResourceTree(float posX, float posY, float posZ, Resource gatherType, int gatherCapacity) {
		super(posX, posY, posZ, 1f, 1f, 1f, null, 0);
		this.gatherCount = 0;
		this.setGatherCapacity(gatherCapacity);
		if (gatherType == null) {
			LOGGER.warn("Resource type was set to null. Deafaulting to seed resouce.");
			this.gatherType = new SeedResource();
		} else {
			this.gatherType = gatherType;
		}
	}
	
	/*
	 * Creates an inventory object based on the resource type and 
	 * resource count.
	 */
	private Inventory getInventory() {
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(this.gatherType);
		Inventory inventory = new Inventory(resources);
		inventory.updateQuantity(this.gatherType, this.gatherCount);
		return inventory;
	}
	
	@Override
	public List<UpgradeStats> getAllUpgradeStats() {
		return STATS;
	}
	
	/**
	 * Configures the stats for the resource tree. Each stat level has a 
	 * normal event action for gathering resources based on the level's
	 * speed stat.
	 * 
	 * 	@return the list of upgrade stats for the resource tree
	 */
	private static List<UpgradeStats> initStats() {
		List<UpgradeStats> result = new LinkedList<>();
		List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
		List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
		
		
		
		// Base State
		result.add(new UpgradeStats(HP, RATE, AMOUNT, CONSTRUCTION_TIME, normalEvents, constructionEvents, TEXTURE)); 
		// Upgrade 1
		result.add(new UpgradeStats(HP+12, RATE-1000, AMOUNT+1, CONSTRUCTION_TIME-500, normalEvents, constructionEvents, "food_resource_tree")); 
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
	 * Returns the number of resources gathered by the tree.
	 * 
	 *	@return the resource count
	 */
	public int getGatherCount() {
		return gatherCount;
	}
	
	/**
	 * Returns the resource type that is gathered by the tree.
	 * 
	 *	@return the resource type
	 */
	public Resource getGatherType() {
		return gatherType;
	}
	
	/**
	 * Returns the maximum number of resources that can be held.
	 * 
	 *	@return the gather capacity
	 */
	public int getGatherCapacity() {
		return this.gatherCapacity;
	}
	
	/**
	 * Sets the gather capacity for the tree.
	 * 
	 * 	@param capacity, must be greater than 0.
	 */
	public void setGatherCapacity(int capacity) {
		if (capacity > 0) {
			this.gatherCapacity = capacity;
		} else {
			LOGGER.warn("Attempted to set resource tree capacity to invalid capacity: " + capacity + ". Defaulting to " + DEFAULT_GATHER_CAPACITY);
			this.gatherCapacity = DEFAULT_GATHER_CAPACITY;
		}
	}
	
	/**
	 * Adds the specified amount to the tree's current resource gather
	 * count. The gather count will always be bounded between 0 and
	 * the gather capacity.
	 * 
	 * 	@param amount of resources to add. Can be positive or negative.
	 */
	public void gather(int amount) {
		int oldCount = this.gatherCount;
		this.gatherCount += amount;
		
		// Check that the new amount is bounded
		if (this.gatherCount > this.gatherCapacity) {
			this.gatherCount = this.gatherCapacity;
		} else if (this.gatherCount < 0) {
			this.gatherCount = 0;
		}
		
		if (this.gatherCount - oldCount != 0) {
			LOGGER.info("Added " + (this.gatherCount - oldCount) + " to " + this);
		}
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
	public void transferResources(Inventory otherInventory) {
		LOGGER.info(this + " transferred " + this.gatherCount + " resources.");
		otherInventory.updateInventory(this.getInventory());
		this.gatherCount = 0;
	}
	
	/**
	 * Toggles the tree's ability to gather resources.
	 */
	public void toggleGatherEnabled() {
		this.gatherEnabled = !this.gatherEnabled;
		LOGGER.info(this + " has gathering enabled: " + this.gatherEnabled + ".");
	}
	
	/**
	 * Returns the string representation of the resource tree.
	 * 
	 * @return string The string representation of the resource tree.
	 */
	@Override
	public String toString() {
		return "Resource tree (" + this.gatherType + ": " + this.gatherCount + ")";
	}
}