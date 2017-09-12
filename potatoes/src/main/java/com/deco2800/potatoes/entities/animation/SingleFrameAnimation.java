package com.deco2800.potatoes.entities.animation;

/**
 * Class for a single animation frame
 */
public class SingleFrameAnimation implements Animation {

	private final transient String frameTexture;
	
	/**
	 * Default constructor for serialization
	 */
	public SingleFrameAnimation() {
		frameTexture = "";
	}

	/**
	 * Creates a single frame animation with the given texture
	 */
	public SingleFrameAnimation(String frameTexture) {
		this.frameTexture = frameTexture;
	}
	
	@Override
	public String getFrame() {
		return frameTexture;
	}

	@Override
	public Animation getAnimation() {
		return this;
	}

	@Override
	public Animation[] getFrames() {
		return new Animation[] {this};
	}
}
