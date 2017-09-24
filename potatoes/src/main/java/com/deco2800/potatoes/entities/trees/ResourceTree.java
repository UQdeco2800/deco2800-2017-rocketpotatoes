package com.deco2800.potatoes.entities.trees;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.managers.Inventory;

/**
 * Resource tree offer a means to collect resources over time.
 */
public class ResourceTree extends AbstractTree implements Tickable {

	/* Logger for all info/warning/error logs */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceTree.class);

	/* Resource Tree Attributes */
	private int gatherCount; // Number of resources currently gathered
	private Resource gatherType; // Type of resource gathered by the tree
	private boolean gatherEnabled = true; // Gathers resources default
	private int gatherCapacity; // Limit on resources held by resource tree
	public static final int DEFAULT_GATHER_CAPACITY = 32; // Default gather capacity, must be > 0

	/**
	 * Default constructor for serialization
	 */
	public ResourceTree() {
		//Blank comment for Sonar
	}

	/**
	 * Constructor for creating a basic resource tree at a given coordinate. By
	 * default the tree will produce seed resources and have a gather capacity of
	 * 32.
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 */
	public ResourceTree(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f);
		this.gatherCount = 0;
		this.setGatherCapacity(DEFAULT_GATHER_CAPACITY);
		this.gatherType = new SeedResource();
		this.resetStats();
		this.setTexture("food_resource_tree");
	}

	/**
	 * Constructor for creating a resource tree at a given coordinate with a custom
	 * resource and gather capacity.
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 * @param gatherType
	 *            The type of resource gathered by the tree.
	 * @param gatherCapacity
	 *            maximum number of resources that can be held by the tree.
	 */
	public ResourceTree(float posX, float posY, float posZ, Resource gatherType, int gatherCapacity) {
		super(posX, posY, posZ, 1f, 1f, 1f);
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
	public ResourceTree clone() {
		return new ResourceTree(this.getPosX(), this.getPosY(), this.getPosZ(), this.gatherType, this.gatherCapacity);
	}

	@Override
	public List<TreeProperties> getAllUpgradeStats() {
		if (this.gatherType instanceof SeedResource) {
			// TODO hard coded currently but needs to be fixed
			this.setTexture("seed_resource_tree");
			return getSeedTreeStats();
		} else if (this.gatherType instanceof FoodResource) {
			this.setTexture("food_resource_tree");
			return getFoodTreeStats();
		} else {
			this.setTexture("seed_resource_tree");
			return getSeedTreeStats();
		}
	}

	/**
	 * Stats for a resource tree that gathers seeds
	 * 
	 * @return the list of upgrade stats for a seed resource tree
	 */
	private static List<TreeProperties> getSeedTreeStats() {
		List<TreeProperties> result = new LinkedList<>();
		List<PropertiesBuilder<ResourceTree>> builders = new LinkedList<>();

		String texture = "seed_resource_tree";
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(8).setBuildTime(2500).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(6000, 1)));
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(20).setBuildTime(2000).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(5500, 1)));
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(30).setBuildTime(1500).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(5000, 2)));

		for (PropertiesBuilder<ResourceTree> statisticsBuilder : builders) {
			result.add(statisticsBuilder.createTreeStatistics());
		}
		return result;
	}

	/**
	 * Stats for a resource tree that gathers food
	 * 
	 * @return the list of upgrade stats for a food resource tree
	 */
	private static List<TreeProperties> getFoodTreeStats() {
		List<TreeProperties> result = new LinkedList<>();
		List<PropertiesBuilder<ResourceTree>> builders = new LinkedList<>();

		String texture = "food_resource_tree";
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(5).setBuildTime(8000).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(6000, 1)));
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(10).setBuildTime(7000).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(5500, 1)));
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(15).setBuildTime(6500).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(5000, 2)));

		for (PropertiesBuilder<ResourceTree> statisticsBuilder : builders) {
			result.add(statisticsBuilder.createTreeStatistics());
		}
		return result;
	}

	/**
	 * Creates an inventory object based on the resource type and resource count.
	 * 
	 * @return the inventory of gathered resources.
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
	 * @return the resource count
	 */
	public int getGatherCount() {
		return gatherCount;
	}

	/**
	 * Returns the resource type that is gathered by the tree.
	 * 
	 * @return the resource type
	 */
	public Resource getGatherType() {
		return gatherType;
	}

	/**
	 * Returns the maximum number of resources that can be held.
	 * 
	 * @return the gather capacity
	 */
	public int getGatherCapacity() {
		return this.gatherCapacity;
	}

	/**
	 * Sets the gather capacity for the tree.
	 * 
	 * @param capacity,
	 *            must be greater than 0.
	 */
	public void setGatherCapacity(int capacity) {
		if (capacity > 0) {
			this.gatherCapacity = capacity;
		} else {
			LOGGER.warn("Attempted to set resource tree capacity to invalid capacity: " + capacity + ". Defaulting to "
					+ DEFAULT_GATHER_CAPACITY);
			this.gatherCapacity = DEFAULT_GATHER_CAPACITY;
		}
	}

	/**
	 * Adds the specified amount to the tree's current resource gather count. The
	 * gather count will always be bounded between 0 and the gather capacity.
	 * 
	 * @param amount
	 *            of resources to add. Can be positive or negative.
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
	 * Transfers the resources gathered from the tree into the specified inventory.
	 * Once resources are transferred, the resourceCount of the tree will reset to
	 * zero.
	 * 
	 * @param otherInventory
	 *            The inventory of the player to receive gathered resources
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
		this.setGatherEnabled(!this.isGatherEnabled());
		LOGGER.info(this + " has gathering enabled: " + this.isGatherEnabled() + ".");
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

	public boolean isGatherEnabled() {
		return gatherEnabled;
	}

	public void setGatherEnabled(boolean gatherEnabled) {
		this.gatherEnabled = gatherEnabled;
	}

}