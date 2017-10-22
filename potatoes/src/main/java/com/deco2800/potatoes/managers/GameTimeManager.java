package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.Color;

/**
 * In game time system
 */
public class GameTimeManager extends Manager implements TickableManager {

	private enum TimeColour {
		SUNRISE(0xFD8632FF, 0), DAY(0xFFFFFFFF, 6), SUNSET(0xFF7E4EFF, 12), NIGHT_EARLY(0x0088FFFF, 13),
		NIGHT(0x3162ADFF, 18), NIGHT_LATE(0x0066CCFF, 23), SUNRISE2(0xFD8632FF, 24);

		private Color color;
		private int time;

		TimeColour(int colourCode, int time) {
			color = new Color(colourCode);
			this.time = time;
		}

		public Color getColor() {
			return color;
		}

		public int getTime() {
			return time;
		}
	}

	private static Color blend(Color c1, Color c2) {
		return new Color(c1).lerp(c2, 0.5f);
	}

	private static final int TIME_SCALE = 10000;

	private int currentTime = 3 * TIME_SCALE;
	private int currentDay = 0;

	// Empty constructor exists by default

	/**
	 * @return the current in game time
	 */
	public float getCurrentTime() {
		return (float) currentTime / TIME_SCALE;
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
		setUnscaledTime(currentTime * TIME_SCALE);
	}

	private void setUnscaledTime(int currentTime) {
		this.currentTime = currentTime;
		nextDay();
	}

	/**
	 * Increases the Current Game Time.
	 *
	 * @param tick
	 */
	@Override
	public void onTick(long tick) {
		this.setUnscaledTime((int) (currentTime + tick));
	}

	/**
	 * Transition into night time
	 */
	public void nightTime() {
		// very slow
		while (currentTime >= 12) {
			onTick(1);
		}
	}


	/**
	 * Rolling over into next day
	 */
	public void nextDay() {
		currentDay += currentTime / (24 * TIME_SCALE);
		currentTime %= 24 * TIME_SCALE;
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

	public Color getColour() {
		TimeColour lower = TimeColour.SUNRISE;
		TimeColour upper = TimeColour.NIGHT;
		for (int i = 0; i < TimeColour.values().length; i++) {
			if (getCurrentTime() > TimeColour.values()[i].getTime()) {
				lower = TimeColour.values()[i];
				upper = TimeColour.values()[i + 1];
			}
		}
		return new Color(lower.getColor()).lerp(upper.getColor(), (getCurrentTime() - lower.getTime()) / (upper.getTime() - lower.getTime()));
	}
}
