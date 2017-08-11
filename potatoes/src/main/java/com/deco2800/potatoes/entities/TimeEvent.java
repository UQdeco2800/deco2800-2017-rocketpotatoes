package com.deco2800.potatoes.entities;

/**
 * Represents an event that happens after a certain amount of time
 * e.g. firing a projectile when an amount of time has passed
 * 
 * Note that HasProgress is not required
 */
public abstract class TimeEvent implements HasProgress {
	
	private long progress = 0;
	private boolean doReset = false;
	private int resetAmount = 0;
	

	public boolean showProgress() {
		return true;
	}
	
	public int getProgress() {
		return (int) progress;
	}

	/**
	 * Sets the progress of the event. The event will not trigger or reset if the amount reaches 0
	 * @param progress
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	public boolean isDoReset() {
		return doReset;
	}

	/**
	 * Sets whether the progress will reset back to the reset amount after completing
	 * @param doReset true if the event will reset, false otherwise
	 */
	public void setDoReset(boolean doReset) {
		this.doReset = doReset;
	}

	/**
	 * @return the value the event will reset to
	 */
	public int getResetAmount() {
		return resetAmount;
	}

	/**
	 * @param resetAmount the value the event will reset to
	 */
	public void setResetAmount(int resetAmount) {
		this.resetAmount = resetAmount;
	}

	/**
	 * Decreases the progress and if the it reaches 0 the event action is triggered
	 * The progress is reset if it reaches 0 and reset is true
	 * @param deltaTime
	 */
	public void decreaseProgress(long deltaTime) {
		progress -= deltaTime;
		if (isCompleted()) {
			action();
			reset();
		}
	}
	
	/**
	 * @return whether this event is completed
	 */
	public boolean isCompleted() {
		return progress <= 0;
	}
	
	/**
	 * Sets the timer to the reset amount if reseting is enabled, 0 otherwise. The action is not triggered
	 */
	public void reset() {
		if (doReset) {
			progress = resetAmount;
		} else {
			progress = 0;
		}
	}
	
	/**
	 * The action that will be triggered when the progress reaches 0
	 */
	public abstract void action();
}
