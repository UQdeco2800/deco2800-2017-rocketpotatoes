package com.deco2800.potatoes.entities.trees;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.potatoes.entities.BasicProperties;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.*;

/**
 * Class to represent attributes for tree upgrades
 */
public class TreeProperties extends BasicProperties<AbstractTree> {
	private static final transient Resource UPGRADE_RESOURCE = new SeedResource();

	private final int buildCost;
	private final int buildTime;
	private final float attackRange;
	private final List<TimeEvent<AbstractTree>> buildEvents;

	/**
	 * Creates this object from the properties stored in the given builder.
	 */
	public TreeProperties(PropertiesBuilder<AbstractTree> builder) {
		super(builder);
		buildCost = builder.getBuildCost();
		buildTime = builder.getBuildTime();
		buildEvents = builder.getBuildEvents();
		attackRange = builder.getAttackRange();
	}

	/**
	 * Registers all build events stored in this object with the tickable given. All
	 * events registered with the tickable will be unregistered
	 */
	public void registerBuildEvents(AbstractTree tickable) {
		unregisterEvents(tickable);
		for (TimeEvent<AbstractTree> timeEvent : buildEvents) {
			GameManager.get().getManager(EventManager.class).registerEvent(tickable, timeEvent.copy());
		}
		GameManager.get().getManager(EventManager.class).registerEvent(tickable, new ConstructionEvent(buildTime));
		if (tickable instanceof Animated) {
			AnimationFactory.registerTimeAnimations(((Animated) tickable).getAnimation(), tickable);
		}
	}
	
	/**
	 * Removes the construction resources amount associated with these stats form
	 * the player's inventory.
	 * 
	 * @return true if the inventory had the required amount of resources, false if
	 *         not.
	 */
	public boolean removeConstructionResources(AbstractTree tree) {

		Player player = GameManager.get().getManager(PlayerManager.class).getPlayer();

		if (!player.canAfford(tree))
			return false;
		player.getInventory().subtractInventory(GameManager.get().getManager
				(GuiManager.class).getGui(TreeShopGui.class).getTreeStateByTree(tree)
				.getCost());
		return true;
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