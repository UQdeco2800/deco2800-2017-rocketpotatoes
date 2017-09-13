package com.deco2800.potatoes.entities.animation;

/**
 * Class representing an animation
 */
public interface Animation {
	/**
	 * Returns the texture for the current frame of the animation
	 */
	public String getFrame();
	
	/**
	 * Returns the animation this animation is running(?) may not need to be public
	 */
	public Animation getAnimation();
	
	/**
	 * Returns all the frames for this animation
	 */
	Animation[] getFrames();
}
