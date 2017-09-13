package com.deco2800.potatoes.entities.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

public final class AnimationFactory {
	private AnimationFactory() {
		// No instances should exist, may be changed at some point
	}

	public static StateAnimation createSimpleStateAnimation(int maxValue, int minValue, String[] frames,
			Supplier<Float> valueFunction) {
		return new StateAnimation(maxValue, minValue, stringsToAnimationArray(frames), valueFunction);
	}

	public static TimeAnimation createSimpleTimeAnimation(int maxAnimationTime, String[] frames) {
		return new TimeAnimation(maxAnimationTime, stringsToAnimationArray(frames));
	}

	public static Animation registerTimeAnimations(Animation animation, Tickable t) {
		Stack<Animation> animations = new Stack<>();
		EventManager eventManager = GameManager.get().getManager(EventManager.class);
		Animation nextAnimation = animation;
		while(!(nextAnimation instanceof SingleFrameAnimation)) {
			if (nextAnimation instanceof TimeAnimation) {
				eventManager.registerEvent(t, (TimeAnimation)nextAnimation);
			}
			for (int i = 0; i < animation.getFrames().length; i++) {
				animations.push(animation.getFrames()[i]);
			}
			nextAnimation = animations.pop();
		}
		return animation;
	}

	private static Animation[] stringsToAnimationArray(String[] frames) {
		List<Animation> frameAnimations = new ArrayList<>();
		for (String string : frames) {
			frameAnimations.add(new SingleFrameAnimation(string));
		}
		return frameAnimations.toArray(new Animation[0]);
	}
}
