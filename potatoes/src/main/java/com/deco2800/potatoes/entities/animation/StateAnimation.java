package com.deco2800.potatoes.entities.animation;

import java.util.function.Supplier;

/**
 * An animation where the frame is dependent on some value. <br>
 * Feel free to modify this class in any way
 */
public class StateAnimation implements Animation {
	private final transient float maxValue;
	private final transient float minValue;
	final transient Animation[] frames;
	private final transient Supplier<Float> valueFunction;

	/**
	 * Create a state animation with the given parameters.
	 * 
	 * @param maxValue
	 *            The maximum value the valueFunction will return. A value from the
	 *            valueFunction higher than this may cause unexpected behavior.
	 * @param minValue
	 *            The minimum value the valueFunction will return. A value from the
	 *            valueFunction lower than this may cause unexpected behavior.
	 * @param frames
	 *            The frames for the animation
	 * @param valueFunction
	 *            The function whose value this animation depends on. Note that a
	 *            Supplier<Integer> will also work
	 */
	public StateAnimation(float maxValue, float minValue, Animation[] frames, Supplier<Float> valueFunction) {
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.frames = frames;
		this.valueFunction = valueFunction;
	}

	/**
	 * Returns the frame based on the result from the value function associated with
	 * this object.
	 * 
	 * @return the texture for the current frame
	 */
	@Override
	public String getFrame() {
		return getAnimation().getFrame();
	}

	@Override
	public Animation getAnimation() {
		float scaledValue = 1 - (valueFunction.get() - minValue) / maxValue;
		if (scaledValue > 1) {
			scaledValue = 1;
		}
		if (scaledValue < 0) {
			scaledValue = 0;
		}
		return frames[Math.round((frames.length - 1) * scaledValue)];
		
	}

	@Override
	public Animation[] getFrames() {
		return frames;
	}

}
