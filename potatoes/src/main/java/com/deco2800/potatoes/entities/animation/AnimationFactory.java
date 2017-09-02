package com.deco2800.potatoes.entities.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AnimationFactory {
	public static StateAnimation createSimpleStateAnimation(int maxValue, int minValue, String[] frames,
			Supplier<Float> valueFunction) {
		return new StateAnimation(maxValue, minValue, stringsToAnimationArray(frames), valueFunction);
	}

	public static TimeAnimation createSimpleTimeAnimation(int maxAnimationTime, String[] frames) {
		return new TimeAnimation(maxAnimationTime, stringsToAnimationArray(frames));
	}

	private static Animation[] stringsToAnimationArray(String[] frames) {
		List<Animation> frameAnimations = new ArrayList<>();
		for (String string : frames) {
			frameAnimations.add(new SingleFrameAnimation(string));
		}
		return frameAnimations.toArray(new Animation[0]);
	}
}
