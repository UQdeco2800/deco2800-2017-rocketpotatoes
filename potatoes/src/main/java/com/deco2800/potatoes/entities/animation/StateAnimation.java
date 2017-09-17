package com.deco2800.potatoes.entities.animation;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An animation where the frame is dependent on some value. <br>
 * Feel free to modify this class in any way
 */
public class StateAnimation implements Animation {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(StateAnimation.class);

	private final transient float maxValue;
	private final transient float minValue;
	final transient Animation[] frames;
	private final transient Supplier<Float> valueFunction;

	/**
	 * Construction for serialization
	 */
	public StateAnimation() {
		maxValue = 0;
		minValue = 0;
		frames = new Animation[] {};
		valueFunction = () -> 0f;
	}

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
		if ((1 - (valueFunction.get() - minValue) / maxValue) > 1) {
			LOGGER.error("Value wasn't between min and max");
			LOGGER.error("valueFunction: {}, minValue: {}, maxValue: {}", valueFunction.get(), minValue, maxValue);
		}
		return frames[Math.round((frames.length - 1) * (1 - (valueFunction.get() - minValue) / maxValue))];
	}

	@Override
	public Animation[] getFrames() {
		return frames;
	}

}
