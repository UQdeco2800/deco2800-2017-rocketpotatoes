package com.deco2800.potatoes.entities.trees;

import java.util.List;

import com.deco2800.potatoes.entities.BasicStatistics;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * Class to represent attributes for tree upgrades
 */
public class TreeStatistics extends BasicStatistics<AbstractTree> {
	private static final transient Resource UPGRADE_RESOURCE = new SeedResource();

	private final int buildCost;
	private final int buildTime;
	private final float attackRange;
	private final List<TimeEvent<AbstractTree>> buildEvents;

	public TreeStatistics(StatisticsBuilder<AbstractTree> builder) {
		super(builder);
		buildCost = builder.getBuildCost();
		buildTime = builder.getBuildTime();
		buildEvents = builder.getBuildEvents();
		attackRange = builder.getAttackRange();
	}

	public void registerBuildEvents(AbstractTree tickable) {
		unregisterEvents(tickable);
		for (TimeEvent<AbstractTree> timeEvent : buildEvents) {
			GameManager.get().getManager(EventManager.class).registerEvent(tickable, timeEvent.copy());
		}
		GameManager.get().getManager(EventManager.class).registerEvent(tickable, new ConstructionEvent(buildTime));
	}

	/**
	 * Removes the construction resources amount associated with these stats form
	 * the player's inventory.
	 * 
	 * @return true if the inventory had the required amount of resources, false if
	 *         not.
	 */
	public boolean removeConstructionResources() {
		return 1 == GameManager.get().getManager(PlayerManager.class).getPlayer().getInventory()
				.updateQuantity(UPGRADE_RESOURCE, -getBuildCost());
	}

	/**
	 * @return the buildCost
	 */
	public int getBuildCost() {
		return buildCost;
	}

	/**
	 * @return the buildTime
	 */
	public int getBuildTime() {
		return buildTime;
	}

	/**
	 * @return the attackRange
	 */
	public float getAttackRange() {
		return attackRange;
	}
}