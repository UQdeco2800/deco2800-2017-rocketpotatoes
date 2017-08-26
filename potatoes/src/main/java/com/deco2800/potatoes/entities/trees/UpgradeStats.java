package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.TimeEvent;

/**
 * Class to represent attributes for tree upgrades
 */
public class UpgradeStats {

	private int hp = 0;
	private int speed = 0;
	private float range = 0;

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
	public UpgradeStats(int hp, int speed, float range, int constructionTime,
			List<TimeEvent<AbstractTree>> normalEvents, List<TimeEvent<AbstractTree>> constructionEvents,
			String texture) {
		this.hp = hp;
		this.speed = speed;
		this.range = range;

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

	public int getHp() {
		return hp;
	}

	public int getSpeed() {
		return speed;
	}

	public float getRange() {
		return range;
	}

	public String getTexture() {
		return texture;
	}

	public int getConstructionTime() {
		return constructionTime;
	}
}