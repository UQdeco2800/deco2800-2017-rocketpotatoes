package com.deco2800.potatoes.entities;

import java.util.function.Supplier;

public class TimeTriggerEvent extends TimeEvent<Tickable> {
	
	private final Supplier<Void> completionHandler;

	/**
	 * Allows the creation of a timed event that has a specified 
	 * completionHandler. Can also specify the time taken and whether
	 * the event should repeat.
	 * 
	 * @param time
	 * @param repeat
	 * @param completionHandler
	 */
	public TimeTriggerEvent(int time, boolean repeat, Supplier<Void> completionHandler) {
		this.completionHandler = completionHandler;
		setDoReset(repeat);
		setResetAmount(time);
		reset();
	}

	@Override
	public void action(Tickable param) {
		if (completionHandler != null) {
			completionHandler.get();
		}
	}

}
