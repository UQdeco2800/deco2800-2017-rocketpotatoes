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
	 * Sets the progress to the given value.
	 * @param p
	 */
	void setProgress(int p);

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
	 * Sets the maximum progress to the given value.
	 * @param p
	 */
	void setMaxProgress(int p);

	/**
	 * Should i show the progress
	 * @return
	 */
	boolean showProgress();
}
