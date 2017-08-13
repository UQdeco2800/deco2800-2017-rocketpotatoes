package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

/**
 * AbstractTree represents an upgradable tree entity. AbstractTree can have
 * registered normal events which are triggered when the tree is not under
 * construction and construction events which are triggered when the tree is
 * being constructed
 */
public abstract class AbstractTree extends AbstractEntity implements Tickable {

	private List<TimeEvent> normalEvents = new LinkedList<>();
	private List<TimeEvent> constructionEvents = new LinkedList<>();
	private int constructionLeft = 0; // TODO change to 100 once construction is implemented, or add to constructor
	private int constructionTime = 0; // TODO move this onto upgrade stats
	private int constructionPercentTime = constructionTime / 100;
	private long currentConstructionTime = constructionPercentTime;
	private int upgradeLevel = 0;

	private static UpgradeStats[] upgradeLevelStats = { new UpgradeStats() };

	/**
	 * Default constructor for serialization
	 */
	public AbstractTree() {
	}

	public AbstractTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, texture);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick(long i) {
		if (getConstructionLeft() <= 0) {
			for (TimeEvent timeEvent : normalEvents) {
				progressEvent(timeEvent, i);
			}
		} else {
			// Time event can't be used for this at the moment
			currentConstructionTime -= i;
			if (currentConstructionTime <= 0) {
				currentConstructionTime = constructionPercentTime;
				decrementConstructionLeft();
			}
			
			for (TimeEvent timeEvent : constructionEvents) {
				progressEvent(timeEvent, i);
			}
		}
	}

	private void progressEvent(TimeEvent timeEvent, long i) {
		timeEvent.decreaseProgress(i);
		if (!timeEvent.isDoReset() && timeEvent.isCompleted()) {
			normalEvents.remove(timeEvent);
		}
	}

	/**
	 * Adds an event to this tree that will trigger every tick when the tree is not
	 * being constructed, when getConstructionLeft() <= 0
	 * 
	 * @param event
	 *            the time event that will be triggered
	 */
	public void registerNormalEvent(TimeEvent event) {
		normalEvents.add(event);
	}

	/**
	 * Adds an event to this tree that will trigger every tick when the tree is
	 * being constructed, when getConstructionLeft() > 0
	 * 
	 * @param event
	 *            the time event that will be triggered
	 */
	public void registerConstructionEvent(TimeEvent event) {
		constructionEvents.add(event);
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
		if (upgradeLevel + 1 == upgradeLevelStats.length) {
			return; // Ignores upgrade if at max level
		}
		upgradeLevel++;
	}

	/**
	 * Not yet implemented
	 * 
	 * @return the default upgrade stats
	 */
	public UpgradeStats getUpgradeStats() {
		return upgradeLevelStats[upgradeLevel];
	}
}
