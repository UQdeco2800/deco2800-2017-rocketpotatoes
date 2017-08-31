package com.deco2800.potatoes.entities.animation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StateAnimationTest {

	private StateAnimation animation;
	private static final SingleFrameAnimation[] frames = { new SingleFrameAnimation("0"), new SingleFrameAnimation("1"),
			new SingleFrameAnimation("2") };
	private int index;

	@Before
	public void setup() {
		index = 0;
		animation = new StateAnimation(2 * (frames.length - 1), 0, frames, this::valueFunction);
	}

	private float valueFunction() {
		return index;
	}

	@Test
	public void testGetFrame() {
		index = 2 * (frames.length - 1);
		assertEquals("Maxium value was not first frame", "0", animation.getFrame());
		index = 0;
		assertEquals("Minimum value was not last frame", "2", animation.getFrame());
		index = frames.length - 1;
		assertEquals("Middle value was not middle frame", "1", animation.getFrame());
	}
}
