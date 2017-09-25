package com.deco2800.potatoes.entities;

/**
 * Functional interface with no parameters and void return value.
 * Alternative to Runnable as sonar says it is a bug.
 */
@FunctionalInterface
public interface Action {
	/**
	 * Runs this action
	 */
	public void run();
}
