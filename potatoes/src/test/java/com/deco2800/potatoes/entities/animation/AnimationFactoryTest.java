package com.deco2800.potatoes.entities.animation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AnimationFactoryTest {

	private int index;
	private static final String[] frames = { "0", "1", "2" };

	@Before
	public void setup() {
		index = 0;
	}

	private float valueFunction() {
		return index;
	}

	@Test
	public void testCreateSimpleStateAnimation() {
		Animation animation = AnimationFactory.createSimpleStateAnimation(10, 0, frames, this::valueFunction);
		index = 10;
		assertEquals("Maxium value was not first frame", "0", animation.getFrame());
		index = 0;
		assertEquals("Minimum value was not last frame", "2", animation.getFrame());
		index = 5;
		assertEquals("Middle value was not middle frame", "1", animation.getFrame());
	}

	@Test
	public void testCreateSimpleTimeAnimation() {
		TimeAnimation animation = AnimationFactory.createSimpleTimeAnimation(10, frames);
		assertEquals("Incorrect frame", "0", animation.getFrame());
		animation.decreaseProgress(5, null);
		assertEquals("Incorrect frame", "1", animation.getFrame());
		animation.decreaseProgress(4, null);
		assertEquals("Incorrect frame", "2", animation.getFrame());
	}

}
