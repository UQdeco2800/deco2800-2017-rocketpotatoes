package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.TimeEvent;

/**
 * Class to represent attributes for tower upgrades
 */
public class UpgradeStats {

	/* Example stats */
	private int hp = 0;
	private int speed = 0;
	
	private TimeEvent[] normalEvents = {};
	private TimeEvent[] constructionEvents = {};
	private String texture = "";

	/**
	 * Default constructor for serialization
	 */
	public UpgradeStats() {
	}

	public UpgradeStats(int hp, int speed, TimeEvent[] normalEvents, TimeEvent[] constructionEvents, String texture) {
		this.hp = hp;
		this.speed = speed;
		
		this.normalEvents = normalEvents;
		this.constructionEvents = constructionEvents;
		this.texture = texture;
	}

	public TimeEvent[] getNormalEvents() {
		return normalEvents;
	}

	public TimeEvent[] getConstructionEvents() {
		return constructionEvents;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getHp() {
		return hp;
	}

	public int getSpeed() {
		return speed;
	}
}