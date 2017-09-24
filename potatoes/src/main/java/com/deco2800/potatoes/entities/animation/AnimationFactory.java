package com.deco2800.potatoes.entities.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

/**
 * Class containing utility methods for creating simple animations
 */
public final class AnimationFactory {
	private AnimationFactory() {
		// No instances should exist, may be changed at some point
	}

	/**
	 * Creates a state animation except with an array of string textures instead of
	 * an animation array. The textures are converted into single frame animations.
	 * All other parameters have the same effect as the constructor for
	 * StateAnimation
	 *
	 * @return the state animation created
	 *
	 * @see StateAnimation
	 */
	public static StateAnimation createSimpleStateAnimation(int maxValue, int minValue, String[] frames,
			Supplier<Float> valueFunction) {
		return new StateAnimation(maxValue, minValue, stringsToAnimationArray(frames), valueFunction);
	}

	/**
	 * Creates a time animation except with an array of string textures instead of
	 * an animation array. The textures are converted into single frame animations.
	 * All other parameters have the same effect as the constructor for
	 * TimeAnimation
	 *
	 * @return the time animation created
	 *
	 * @see TimeAnimation
	 */
	public static TimeAnimation createSimpleTimeAnimation(int maxAnimationTime, String[] frames) {
		return new TimeAnimation(maxAnimationTime, stringsToAnimationArray(frames));
	}

	/**
	 * Registers the given animation with the event manager if it is a TimeAnimation
	 * and also registers all animations contained in frames for this animation and
	 * others.
	 * 
	 * @param animation
	 *            the animation to register events for
	 * @param t
	 *            the tickable to register the animations with
	 */
	public static Animation registerTimeAnimations(Animation animation, Tickable t) {
		Stack<Animation> animations = new Stack<>();
		EventManager eventManager = GameManager.get().getManager(EventManager.class);
		Animation nextAnimation = animation;
		while (!(nextAnimation instanceof SingleFrameAnimation)) {
			if (nextAnimation instanceof TimeAnimation) {
				eventManager.registerEvent(t, (TimeAnimation) nextAnimation);
			}
			for (int i = 0; i < animation.getFrames().length; i++) {
				animations.push(animation.getFrames()[i]);
			}
			nextAnimation = animations.pop();
		}
		return animation;
	}

	/**
	 * Converts the given texture string array into an array of SingleFrameAnimations
	 */
	public static Animation[] stringsToAnimationArray(String[] frames) {
		List<Animation> frameAnimations = new ArrayList<>();
		for (String string : frames) {
			frameAnimations.add(new SingleFrameAnimation(string));
		}
		return frameAnimations.toArray(new Animation[0]);
	}
}
