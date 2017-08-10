package com.deco2800.potatoes.entities;

/**
 * Represents an event that happens after a certain amount of time
 * e.g. firing a projectile when an amount of time has passed
 * 
 * Implementing HasProgress may be redundant
 */
public abstract class TimeEvent implements HasProgress {
	
	private int progress;
	private boolean reset;
	private int resetAmount;
	

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public int getResetAmount() {
		return resetAmount;
	}

	public void setResetAmount(int resetAmount) {
		this.resetAmount = resetAmount;
	}

	public void decreaseProgress(int amount) {
		// TODO stub
	}
	
	public abstract void action();
}
