package com.deco2800.potatoes.entities.animation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeAnimationTest {

	private TimeAnimation animation;
	private static SingleFrameAnimation[] frames = { new SingleFrameAnimation("0"), new SingleFrameAnimation("1"),
			new SingleFrameAnimation("2") };

	@Before
	public void setup() {
		animation = new TimeAnimation(10, frames);
	}

	@Test
	public void testGetFrame() {
		assertEquals("Incorrect frame", "0", animation.getFrame());
		animation.decreaseProgress(5, null);
		assertEquals("Incorrect frame", "1", animation.getFrame());
		animation.decreaseProgress(4, null);
		assertEquals("Incorrect frame", "2", animation.getFrame());
	}

	@After
	public void tearDown() {
		animation = null;
		frames = null;
	}
}
