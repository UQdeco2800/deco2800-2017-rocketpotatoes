package com.deco2800.potatoes.entities.trees;

import java.util.List;

import com.deco2800.potatoes.entities.HasProgress;
import com.deco2800.potatoes.entities.MortalEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

/**
 * AbstractTree represents an upgradable tree entity. AbstractTree can have
 * registered normal events which are triggered when the tree is not under
 * construction and construction events which are triggered when the tree is
 * being constructed
 */
public abstract class AbstractTree extends MortalEntity implements Tickable, HasProgress {

	// Maybe move this out
	private class ConstructionEvent extends TimeEvent<AbstractTree> {
		public ConstructionEvent() {
			setDoReset(true);
			setResetAmount(constructionTime / 100);
			reset();
		}

		@Override
		public void action(AbstractTree param) {
			decrementConstructionLeft();
		}

		@Override
		public TimeEvent<AbstractTree> copy() {
			return null;
		}
	}

	private int constructionLeft = 100;
	private int constructionTime = 0;
	private int upgradeLevel = 0;
	private boolean normalEventsRegistered = false;

	/**
	 * Default constructor for serialization
	 */
	public AbstractTree() {
		// Reseting may not be needed
		resetStats();
	}

	public AbstractTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength, String texture,
			float maxHealth) {
		super(posX, posY, posZ, xLength, yLength, zLength, texture, maxHealth);
		resetStats();
	}

	@Override
	public void onTick(long i) {
		if (getConstructionLeft() <= 0) {
			if (!normalEventsRegistered) {
				registerNewEvents(getUpgradeStats().getNormalEventsCopy());
			}
			normalEventsRegistered = true;
		} else if (normalEventsRegistered) {
			List<TimeEvent<AbstractTree>> constructionEvents = getUpgradeStats().getConstructionEventsCopy();
			constructionEvents.add(new ConstructionEvent());
			registerNewEvents(constructionEvents);
			normalEventsRegistered = false;
		}
	}

	private void registerNewEvents(List<TimeEvent<AbstractTree>> events) {
		EventManager eventManager = (EventManager) GameManager.get().getManager(EventManager.class);
		eventManager.unregisterAll(this);
		for (TimeEvent<AbstractTree> timeEvent : events) {
			eventManager.registerEvent(this, timeEvent);
		}
	}

	/**
	 * @return the percentage of construction left, from 0 to 100
	 */
	public int getConstructionLeft() {
		return constructionLeft;
	}

	/**
	 * @param constructionLeft
	 *            the percentage of construction to set, from 0 to 100
	 */
	public void setConstructionLeft(int constructionLeft) {
		this.constructionLeft = constructionLeft;
	}

	public void decrementConstructionLeft() {
		constructionLeft--;
	}

	/**
	 * The amount of time construction takes to fully complete
	 */
	public int getConstructionTime() {
		return constructionTime;
	}

	/**
	 * Sets the time construction takes to complete
	 * 
	 * @param constructionTime
	 *            the time in milliseconds
	 */
	public void setConstructionTime(int constructionTime) {
		this.constructionTime = constructionTime;
	}

	/**
	 * Upgrades to the next tree level
	 * 
	 * Not yet implemented
	 */
	public void upgrade() {
		if (upgradeLevel + 1 == getAllUpgradeStats().size()) {
			return; // Ignores upgrade if at max level
		}
		upgradeLevel++;
		this.addMaxHealth(getUpgradeStats().getHp() - this.getMaxHealth());
		resetStats();
	}

	public void resetStats() {
		constructionTime = getUpgradeStats().getConstructionTime();
		List<TimeEvent<AbstractTree>> constructionEvents = getUpgradeStats().getConstructionEventsCopy();
		constructionEvents.add(new ConstructionEvent());
		this.heal(this.getMaxHealth());
		registerNewEvents(constructionEvents);
	}

	/**
	 * Not yet implemented
	 * 
	 * @return the default upgrade stats
	 */
	public UpgradeStats getUpgradeStats() {
		return getAllUpgradeStats().get(upgradeLevel);
	}

	/**
	 * Returns a list of the stats for each upgrade level in order <br>
	 * This is called often, so it is recommend you don't create a new object every
	 * time
	 * 
	 * @return a list of all the upgrade stats for this tree
	 */
	public abstract List<UpgradeStats> getAllUpgradeStats();

	/**
	 * Returns the current progress
	 *
	 * @return
	 */
	@Override
	public int getProgress() {
		return constructionLeft;
	}

	/**
	 * Set's the progress to the given value.
	 *
	 * @param p
	 */
	@Override
	public void setProgress(int p) {
		constructionLeft = p;
	}

	/**
	 * Should i show the progress
	 *
	 * @return
	 */
	@Override
	public boolean showProgress() {
		return false;
	}
}
