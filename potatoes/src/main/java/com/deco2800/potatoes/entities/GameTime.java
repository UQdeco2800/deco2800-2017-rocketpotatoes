package com.deco2800.potatoes.entities;

/**
 * In game time system
 */
public abstract class GameTime<T> implements Tickable {

	private int currentTime = 0;
	private int currentDay = 0;
	private boolean dayTime = true;

	/**
	 * Default constructor
	 */
	public GameTime() {
	}

	/**
	 * @return the current in game time
	 */
	public int getCurrentTime() {
		return currentTime;
	}

	/**
	 * Resets the Current Time.
	 *
	 * @param currentTime
	 */
	public void resetCurrentTime(int currentTime) {
		this.currentTime = 0;
	}

	/**
	 * Sets the Current Game Time.
	 *
	 * @param currentTime
	 */
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * Increases the Current Game Time.
	 *
	 * @param tick
	 */
	public void onTick(int tick) {
		this.setCurrentTime((int) (this.getCurrentTime() + 0.01));
	}

	/**
	 * Transition into night time
	 */
	public void nightTime(int currentTime, boolean dayTime) {
		while (currentTime >= 12) {
			dayTime = false;
		}
	}


	/**
	 * Rolling over into next day
	 */
	public void nextDay(int currentTime, int currentDay) {
		if (currentTime == 24) {
			currentDay += 1;
			currentTime = 0;
		}
	}

	/**
	 * @return the current In Game Day.
	 */
	public int getCurrentDay() {
		return currentDay;
	}

	/**
	 * Resets the Current Day.
	 *
	 * @param currentDay
	 */
	public void resetCurrentDay(int currentDay) {
		this.currentDay = 0;
	}

	/**
	 * Sets the Current Game Day.
	 *
	 * @param currentDay
	 */
	public void setCurrentDay(int currentDay) {
		this.currentDay = currentDay;
	}


	public GameTime<T> copy() { return null; }



}
