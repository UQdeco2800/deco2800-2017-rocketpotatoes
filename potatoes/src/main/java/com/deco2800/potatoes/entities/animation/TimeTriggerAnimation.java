package com.deco2800.potatoes.entities.animation;

import java.util.function.Supplier;

import com.deco2800.potatoes.entities.Tickable;

/**
 * Time animation that executes a given function every time it resets.
 */
public class TimeTriggerAnimation extends TimeAnimation {

	private final Supplier<Void> completionHandler;
	
	/**
	 * Creates an animation based on specified frames and time for 
	 * iterating over the frames. Able to set whether the animation repeats
	 * and define a completion handler for when the animation finishes or
	 * resets.
	 * 
	 * @param animationTime
	 * 			The time for one loop of the animation.
	 * @param frames
	 * 			The texture frames for the animation.
	 * @param repeat
	 * 			True if the animation should repeat.
	 * @param completionHandler
	 * 			A runnable object called when the animation ends or resets.
	 */
	public TimeTriggerAnimation(int animationTime, String[] frames, Supplier<Void> completionHandler) {
		super(animationTime, AnimationFactory.stringsToAnimationArray(frames));
		this.completionHandler = completionHandler;
	}
	
	/**
	 * Creates this animation from the given time animation, with the completion
	 * handler for when the animation finishes
	 */
	public TimeTriggerAnimation(TimeAnimation animation, Supplier<Void> completionHandler) {
		super(animation.getResetAmount(), animation.getFrames());
		this.completionHandler = completionHandler;
	}
	
	@Override
	public void action(Tickable param) {
		if (completionHandler != null) {
			completionHandler.get();
		}
	}
}
