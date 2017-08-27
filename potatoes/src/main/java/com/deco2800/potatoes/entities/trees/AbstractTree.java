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
	private static class ConstructionEvent extends TimeEvent<AbstractTree> {
		public ConstructionEvent(int constructionTime) {
			setDoReset(true);
			setResetAmount(constructionTime / 100);
			reset();
		}

		@Override
		public void action(AbstractTree param) {
			param.decrementConstructionLeft();
			if (param.getConstructionLeft() <= 0) {
				// Changes to the normal events since construction is over
				param.setRegisteredEvents(false);
			}
		}

		@Override
		public TimeEvent<AbstractTree> copy() {
			return null;
		}
	}

	private int constructionLeft = 100;
	private int upgradeLevel = 0;

	/**
	 * Default constructor for serialization
	 */
	public AbstractTree() {
		// Reseting may not be needed
		resetStats();
	}

	public AbstractTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, texture, 1);
		resetStats();
	}

	@Override
	public void onTick(long time) {
		// Nothing here now
	}

	/**
	 * Sets the registered events to the construction events or the normal events
	 * 
	 * @param construction
	 *            whether to set the events to construction events or not
	 */
	private void setRegisteredEvents(boolean construction) {
		if (construction) {
			List<TimeEvent<AbstractTree>> constructionEvents = getUpgradeStats().getConstructionEventsCopy();
			constructionEvents.add(new ConstructionEvent(getUpgradeStats().getConstructionTime()));
			registerNewEvents(constructionEvents);
		} else {
			registerNewEvents(getUpgradeStats().getNormalEventsCopy());
		}
	}

	/**
	 * Registers the list of events given with the event manager and unregisters all
	 * other events for this object
	 */
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
		if (constructionLeft > 0) {
			// Construction starts again
			setRegisteredEvents(true);
		}
	}

	public void decrementConstructionLeft() {
		constructionLeft--;
	}

	/**
	 * Upgrades to the next tree level
	 * 
	 * Not yet implemented
	 */
	public void upgrade() {
		if (upgradeLevel + 1 >= getAllUpgradeStats().size()) {
			return; // Ignores upgrade if at max level
		}
		upgradeLevel++;
		resetStats();
	}

	/**
	 * Resets the stats of this tree to the default for the current upgrade level
	 * and restarts tree construction
	 */
	public void resetStats() {
		this.addMaxHealth(getUpgradeStats().getHp() - this.getMaxHealth());
		this.heal(getMaxHealth());
		setTexture(getUpgradeStats().getTexture());
		setRegisteredEvents(true);
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

	@Override
	public float getProgressRatio() {
		return constructionLeft / 100f;
	}

	@Override
	public int getMaxProgress() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxProgress(int p) {
		

	}
}
