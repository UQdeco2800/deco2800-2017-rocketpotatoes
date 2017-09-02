package com.deco2800.potatoes.entities.trees;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * Class to represent attributes for tree upgrades
 */
public class UpgradeStats {

	private static final transient Resource UPGRADE_RESOURCE = new SeedResource();

	private Map<String, Object> map = new HashMap<>();

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
		set("hp", hp);
		set("speed", speed);
		set("range", range);
		set("resourceCost", resourceCost);

		set("constructionTime", constructionTime);

		set("normalEvents", normalEvents);
		set("normalEvents", getNormalEventsCopy());
		set("constructionEvents", constructionEvents);
		set("constructionEvents", getConstructionEventsCopy());
		set("texture", texture);
	}

	/**
	 * Sets the specified property to the specified value. If it is already set, the
	 * value is overridden.
	 * 
	 * @param property
	 *            The string property to set the value to
	 * @param value
	 *            The value to set
	 * @return This, for chaining - stats.set("a", 0).set("b", 1)
	 */
	public UpgradeStats set(String property, Object value) {
		map.put(property, value);
		return this;
	}

	/**
	 * Gets the value associated with the specified property
	 * 
	 * @param property
	 *            The property to get
	 * @return The value of the property
	 */
	public Object get(String property) {
		if (map.containsKey(property)) {
			return map.get(property);
		} else {
			throw new NullPointerException("");
		}
	}

	/**
	 * Returns the set containing all properties with values.
	 */
	public Set<String> getProperties() {
		return map.keySet();
	}

	/**
	 * @return A deep copy of the normal events associated with these stats
	 */
	@SuppressWarnings("unchecked")
	public List<TimeEvent<AbstractTree>> getNormalEventsCopy() {
		List<TimeEvent<AbstractTree>> result = new LinkedList<>();
		for (TimeEvent<AbstractTree> timeEvent : (List<TimeEvent<AbstractTree>>) get("normalEvents")) {
			result.add(timeEvent.copy());
		}
		return result;
	}

	/**
	 * @return A deep copy of the construction events associated with these stats
	 */
	@SuppressWarnings("unchecked")
	public List<TimeEvent<AbstractTree>> getConstructionEventsCopy() {
		List<TimeEvent<AbstractTree>> result = new LinkedList<>();
		for (TimeEvent<AbstractTree> timeEvent : (List<TimeEvent<AbstractTree>>) get("constructionEvents")) {
			result.add(timeEvent.copy());
		}
		return result;
	}

	/**
	 * @return returns a reference to the normal events list of these stats
	 */
	@SuppressWarnings("unchecked")
	public List<TimeEvent<AbstractTree>> getNormalEventsReference() {
		return (List<TimeEvent<AbstractTree>>) get("normalEvents");
	}

	/**
	 * @return returns a reference to the construction events list of these stats
	 */
	@SuppressWarnings("unchecked")
	public List<TimeEvent<AbstractTree>> getConstructionEventsReference() {
		return (List<TimeEvent<AbstractTree>>) get("constructionEvents");
	}

	/**
	 * Returns the HP for these stats
	 */
	public int getHp() {
		return (int) get("hp");
	}

	/**
	 * Returns the shooting speed for these stats
	 */
	public int getSpeed() {
		return (int) get("speed");
	}

	/**
	 * Returns the shooting range for these stats
	 */
	public float getRange() {
		return (float) get("range");
	}

	/**
	 * Returns the texture for these stats
	 */
	public String getTexture() {
		return (String) get("texture");
	}

	/**
	 * Returns the construction time for these stats
	 */
	public int getConstructionTime() {
		return (int) get("constructionTime");
	}

	/**
	 * Removes the construction resources amount associated with these stats form
	 * the player's inventory.
	 * 
	 * @return true if the inventory had the required amount of resources, false if
	 *         not.
	 */
	public boolean removeConstructionResources() {
		Inventory inventory = GameManager.get().getManager(PlayerManager.class).getPlayer().getInventory();
		if (inventory.getQuantity(UPGRADE_RESOURCE) >= (int) get("resourceCost")) {
			inventory.updateQuantity(UPGRADE_RESOURCE, -(int) get("resourceCost"));
			return true;
		}
		return false;
	}
}