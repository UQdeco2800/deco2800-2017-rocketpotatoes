package com.deco2800.potatoes.entities;

/**
 * Created by timhadwen on 29/7/17.
 */
public interface HasProgress {
	/**
	 * Returns the current progress out of 100
	 * @return
	 */
	int getProgress();

	/**
	 * Should i show the progress
	 * @return
	 */
	boolean showProgress();
}
