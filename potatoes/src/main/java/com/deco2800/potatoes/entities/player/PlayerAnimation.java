package com.deco2800.potatoes.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.SingleFrameAnimation;
import com.deco2800.potatoes.entities.animation.TimeAnimation;

public class PlayerAnimation extends TimeAnimation {

	private Runnable completionHandler;
	
	/**
	 * Creates a player animation based on specified frames and time for 
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
	public PlayerAnimation(int animationTime, String[] frames, Runnable completionHandler) {
		super(animationTime, stringsToAnimationArray(frames));
		this.completionHandler = completionHandler;
	}
	
	@Override
	public void action(Tickable param) {
		if (completionHandler != null) {
			completionHandler.run();
		}
	}
	
	private static Animation[] stringsToAnimationArray(String[] frames) {
		List<Animation> frameAnimations = new ArrayList<>();
		for (String string : frames) {
			frameAnimations.add(new SingleFrameAnimation(string));
		}
		return frameAnimations.toArray(new Animation[0]);
	}
}
