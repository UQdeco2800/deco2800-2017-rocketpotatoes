package com.deco2800.potatoes.entities.health;

/**
 * Created by timhadwen on 29/7/17.
 */
public interface HasProgress {
	/**
	 * Returns the current progress
	 * @return
	 */
	int getProgress();

	/**
	 * Returns the current progress ratio
	 * @return
	 */
	float getProgressRatio();

	/**
	 * Returns the maximum possible progress
	 * @return
	 */
	int getMaxProgress();

	/**
	 * Should i show the progress
	 * @return
	 */
	boolean showProgress();
}
