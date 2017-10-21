package com.deco2800.potatoes.managers;

/**
 * A tickable manager.
 * Receives an onTick every game tick
 */
@FunctionalInterface
public interface TickableManager {
	void onTick(long i);
}
