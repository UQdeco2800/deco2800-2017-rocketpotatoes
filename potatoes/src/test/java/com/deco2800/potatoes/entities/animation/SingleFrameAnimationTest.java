package com.deco2800.potatoes.entities.animation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleFrameAnimationTest {

	SingleFrameAnimation animation;

	@Before
	public void setup() {
		animation = new SingleFrameAnimation();
		animation = new SingleFrameAnimation("test");
	}

	@Test
	public void testGetFrame() {
		assertEquals("Frame was not test frame", "test", animation.getFrame());
	}

	@Test
	public void testGetAnimation() {
		assertEquals("Frame was not test frame", "test", animation.getAnimation().getFrame());
	}

	@Test
	public void testGetFrames() {
		assertArrayEquals("Frames was not just animation", new Animation[] { animation }, animation.getFrames());
	}

	@After
	public void tearDown() {
		animation = null;
	}
}
