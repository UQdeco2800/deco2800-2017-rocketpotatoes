package com.deco2800.potatoes.entities;

/**
 * An traditional animation where the frame changes over time <br>
 * Feel free to modify this class in any way
 */
public class TimeAnimation extends TimeEvent<Tickable> implements Animation {

	private final transient String[] frames;

	/**
	 * Construction for serialization
	 */
	public TimeAnimation() {
		frames = new String[] {};
	}

	/**
	 * Creates time animation where the frames are distributed between 
	 * 
	 * @param maxAnimationTime
	 * @param frames
	 */
	public TimeAnimation(int maxAnimationTime, String[] frames) {
		this.frames = frames;
		setResetAmount(maxAnimationTime);
		setDoReset(true);
		reset();
	}

	@Override
	public void action(Tickable param) {
		// Nothing to do here
	}

	/**
	 * Returns the frame based on the progress this TimeEvent. The frames are evenly
	 * distributed between the max time and 0
	 */
	@Override
	public String getFrame() {
		return frames[(int) (frames.length - 1 - frames.length * (float) getProgress() / getResetAmount())];
	}

}
