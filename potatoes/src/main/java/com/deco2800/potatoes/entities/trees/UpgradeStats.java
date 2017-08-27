package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.managers.Manager;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * Class to represent attributes for tree upgrades
 */
public class UpgradeStats {

	private static final transient Resource UPGRADE_RESOURCE = new SeedResource();

	private int hp = 0;
	private int speed = 0;
	private float range = 0;
	private int resourceCost = 0;

	private int constructionTime;

	private List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
	private List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
	private String texture = "";

	/**
	 * Default constructor for serialization
	 */
	public UpgradeStats() {
		/**
		 * Default constructor for serialization
		 */
	}

	/**
	 * Create this with the given parameters. A copy of the normal and construction
	 * events is stored, not the passed objects. <br>
	 * <br>
	 * This will be changed in the future
	 */
	public UpgradeStats(int hp, int speed, float range, int constructionTime, int resourceCost,
			List<TimeEvent<AbstractTree>> normalEvents, List<TimeEvent<AbstractTree>> constructionEvents,
			String texture) {
		this.hp = hp;
		this.speed = speed;
		this.range = range;
		this.resourceCost = resourceCost;

		this.constructionTime = constructionTime;

		this.normalEvents = normalEvents;
		this.normalEvents = getNormalEventsCopy();
		this.constructionEvents = constructionEvents;
		this.constructionEvents = getConstructionEventsCopy();
		this.texture = texture;
	}

	/**
	 * @return A deep copy of the normal events associated with these stats
	 */
	public List<TimeEvent<AbstractTree>> getNormalEventsCopy() {
		List<TimeEvent<AbstractTree>> result = new LinkedList<>();
		for (TimeEvent<AbstractTree> timeEvent : normalEvents) {
			result.add(timeEvent.copy());
		}
		return result;
	}

	/**
	 * @return A deep copy of the construction events associated with these stats
	 */
	public List<TimeEvent<AbstractTree>> getConstructionEventsCopy() {
		List<TimeEvent<AbstractTree>> result = new LinkedList<>();
		for (TimeEvent<AbstractTree> timeEvent : constructionEvents) {
			result.add(timeEvent.copy());
		}
		return result;
	}

	/**
	 * @return returns a reference to the normal events list of these stats
	 */
	public List<TimeEvent<AbstractTree>> getNormalEventsReference() {
		return normalEvents;
	}

	/**
	 * @return returns a reference to the construction events list of these stats
	 */
	public List<TimeEvent<AbstractTree>> getConstructionEventsReference() {
		return constructionEvents;
	}

	/**
	 * Returns the HP for these stats
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * Returns the shooting speed for these stats
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Returns the shooting range for these stats
	 */
	public float getRange() {
		return range;
	}

	/**
	 * Returns the texture for these stats
	 */
	public String getTexture() {
		return texture;
	}

	/**
	 * Returns the construction time for these stats
	 */
	public int getConstructionTime() {
		return constructionTime;
	}

	/**
	 * Removes the construction resources amount associated with these stats form
	 * the player's inventory.
	 * 
	 * @return true if the inventory had the required amount of resources, false if
	 *         not.
	 */
	public boolean removeConstructionResources() {
		Inventory inventory = ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).getPlayer()
				.getInventory();
		if (inventory.getQuantity(UPGRADE_RESOURCE) >= resourceCost) {
			inventory.updateQuantity(UPGRADE_RESOURCE, -resourceCost);			
			return true;
		}
		return false;
	}
}