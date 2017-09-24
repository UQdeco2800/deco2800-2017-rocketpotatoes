package com.deco2800.potatoes.entities.animation;

/**
 * Represents an object the has an animation
 */
public interface Animated {
	/**
	 * Sets the animation to the given animation
	 */
	public void setAnimation(Animation animation);

	/**
	 * Returns current animation
	 */
	public Animation getAnimation();
}
