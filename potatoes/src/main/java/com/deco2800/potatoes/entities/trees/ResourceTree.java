package com.deco2800.potatoes.entities.trees;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.security.auth.x500.X500Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.animation.TimeTriggerAnimation;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
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
	
	public String defaultTexture; // The standard texture to default to
	private TimeAnimation currentAnimation;

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
	 */
	public ResourceTree(float posX, float posY) {
		super(posX, posY, 1f, 1f);
		this.gatherCount = 0;
		this.setGatherCapacity(32); // Use a default value of 32
		this.gatherType = new SeedResource();
		this.resetStats();
		this.setTexture("seed_resource_tree");
	}

	/**
	 * Constructor for creating a resource tree at a given coordinate with a custom
	 * resource and gather capacity.
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param gatherType
	 *            The type of resource gathered by the tree.
	 * @param gatherCapacity
	 *            maximum number of resources that can be held by the tree.
	 */
	public ResourceTree(float posX, float posY, Resource gatherType, int gatherCapacity) {
		super(posX, posY, 1f, 1f);
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
	public ResourceTree createCopy() {
		return new ResourceTree(this.getPosX(), this.getPosY(), this.gatherType, this.gatherCapacity);
	}

	@Override
	public List<TreeProperties> getAllUpgradeStats() {
		this.setTexture("seed_resource_tree");
		return getSeedTreeStats();
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
					+ "32");
			this.gatherCapacity = 32;
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

	@Override
	public String getName() {
		return "Resource Tree";
	}
	
	/* Animation */
	
//    /**
//     * Creates a time animation based on frames provided for a resource tree
//     *
//     * @param treeType    A string representing the type of resource tree.
//     * @param treeState    A string representing the state of the tree.
//     * @param frameCount    The number of frames in the animation.
//     * @param animationTime The time per animation cycle.
//     * @param completionHandler The closure to execute upon completion.
//     * @return Time animation for the specified resource tree
//     */
//    public static TimeAnimation makeResourceTreeAnimation(String treeType, String treeState, int frameCount, int animationTime, Supplier<Void> completionHandler) {
//    		String[] frames = new String[frameCount];
//    		for (int i = 1; i <= frameCount; i++) {
//            frames[i - 1] = treeType + "_" + treeState + "_" + i;
//        }
//    		return new TimeTriggerAnimation(animationTime, frames, completionHandler);
//    }
	
	/**
     * Creates an array of frames for resource tree animations
     */
    public static String[] makeFrames(String treeType, String treeState, int frameCount) {
		String[] frames = new String[frameCount];
		for (int i = 1; i <= frameCount; i++) {
			frames[i - 1] = treeType + "_" + treeState + "_" + i;
		}
		return frames;
    }

}
