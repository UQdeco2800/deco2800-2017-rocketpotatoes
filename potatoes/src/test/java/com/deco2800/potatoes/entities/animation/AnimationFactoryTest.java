package com.deco2800.potatoes.entities.animation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnimationFactoryTest {

	private int index;
	private static final String[] frames = { "0", "1", "2" };
	TimeAnimation timeAnimation;
	Animation animation;

	@Before
	public void setup() {
		index = 0;
	}

	private float valueFunction() {
		return index;
	}

	@Test
	public void testCreateSimpleStateAnimation() {
		animation = AnimationFactory.createSimpleStateAnimation(10, 0, frames, this::valueFunction);
		index = 10;
		assertEquals("Maxium value was not first frame", "0", animation.getFrame());
		index = 0;
		assertEquals("Minimum value was not last frame", "2", animation.getFrame());
		index = 5;
		assertEquals("Middle value was not middle frame", "1", animation.getFrame());
	}

	@Test
	public void testCreateSimpleTimeAnimation() {
		timeAnimation = AnimationFactory.createSimpleTimeAnimation(10, frames);
		assertEquals("Incorrect frame", "0", timeAnimation.getFrame());
		timeAnimation.decreaseProgress(5, null);
		assertEquals("Incorrect frame", "1", timeAnimation.getFrame());
		timeAnimation.decreaseProgress(4, null);
		assertEquals("Incorrect frame", "2", timeAnimation.getFrame());
	}

	@After
	public void tearDown() {
		animation = null;
		timeAnimation = null;
	}

}
