package com.deco2800.potatoes.entities.trees;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.potatoes.entities.FoodResource;
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
	public ResourceTree(float posX, float posY) {
		super(posX, posY, 1f, 1f, null);
		this.gatherCount = 0;
		this.setGatherCapacity(DEFAULT_GATHER_CAPACITY);
		this.gatherType = new SeedResource();
		this.resetStats();
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
	 * @param gatherType
	 * 			The type of resource gathered by the tree.
	 * @param gatherCapacity
	 * 			maximum number of resources that can be held by the tree.
	 */
	public ResourceTree(float posX, float posY, Resource gatherType, int gatherCapacity) {
		super(posX, posY, 1f, 1f, null);
		this.gatherCount = 0;
		this.setGatherCapacity(gatherCapacity);
		if (gatherType == null) {
			LOGGER.warn("Resource type was set to null. Defaulting to seed resource.");
			this.gatherType = new SeedResource();
		} else {
			this.gatherType = gatherType;
		}
		this.resetStats();
	}
	
	@Override
	public List<UpgradeStats> getAllUpgradeStats() {
		if (this.gatherType instanceof SeedResource) {
			return getSeedTreeStats();
		} else if (this.gatherType instanceof FoodResource) {
			return getFoodTreeStats();
		} else {
			return getSeedTreeStats();
		}
	}
	
	/**
	 *	Stats for a resource tree that gathers seeds
	 * 
	 * 	@return the list of upgrade stats for a seed resource tree
	 */
	private static List<UpgradeStats> getSeedTreeStats() {
		List<UpgradeStats> result = new LinkedList<>();
		List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
		List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
		result.add(new UpgradeStats(8, 3500, 1f, 2500, 1, normalEvents, constructionEvents, "seed_resource_tree"));
		result.add(new UpgradeStats(20, 3000, 1f, 2000, 1, normalEvents, constructionEvents, "seed_resource_tree"));
		result.add(new UpgradeStats(30, 3000, 2f, 1500, 1, normalEvents, constructionEvents, "seed_resource_tree"));
		
		// Add ResourceGatherEvent to each upgrade level
		for (UpgradeStats upgradeStats : result) {
			upgradeStats.getNormalEventsReference().add(new ResourceGatherEvent(upgradeStats.getSpeed(), 
					Math.round(upgradeStats.getRange())));
		}
		return result;
	}
	
	/**
	 *	Stats for a resource tree that gathers food
	 * 
	 * 	@return the list of upgrade stats for a food resource tree
	 */
	private static List<UpgradeStats> getFoodTreeStats() {
		List<UpgradeStats> result = new LinkedList<>();
		List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
		List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
		result.add(new UpgradeStats(5, 6000, 1f, 8000, 1, normalEvents, constructionEvents, "food_resource_tree")); 
		result.add(new UpgradeStats(10, 5500, 1f, 7000, 1, normalEvents, constructionEvents, "food_resource_tree")); 
		result.add(new UpgradeStats(15, 5000, 2f, 6500, 1, normalEvents, constructionEvents, "food_resource_tree")); 
		
		// Add ResourceGatherEvent to each upgrade level
		for (UpgradeStats upgradeStats : result) {
			upgradeStats.getNormalEventsReference().add(new ResourceGatherEvent(upgradeStats.getSpeed(), 
					Math.round(upgradeStats.getRange())));
		}
		return result;
	}
	
	/**
	 * Creates an inventory object based on the resource type and 
	 * resource count.
	 * 
	 * 	@return the inventory of gathered resources.
	 */
	private Inventory getInventory() {
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(this.gatherType);
		Inventory inventory = new Inventory(resources);
		inventory.updateQuantity(this.gatherType, this.gatherCount);
		return inventory;
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
	 * are transferred, the resourceCount of the tree will
	 * reset to zero.
	 * 
	 * 	@param otherInventory
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
		return "Resource tree (" + this.gatherType + ": " + this.gatherCount + "/" + this.gatherCapacity + ")";
	}
}