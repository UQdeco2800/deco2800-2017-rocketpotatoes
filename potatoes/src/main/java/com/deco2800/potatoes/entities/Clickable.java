package com.deco2800.potatoes.entities;

/**
 * An interface to make an Entity clickable via the MouseHandler
 */
@FunctionalInterface
public interface Clickable {
	/**
	 * Called when an entity is clicked on.
	 */
	void onClick();
}
