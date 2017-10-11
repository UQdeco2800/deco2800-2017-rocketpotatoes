package com.deco2800.potatoes.entities.animation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.function.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeTriggerAnimationTest {
	private static final String[] FRAMES = { "0", "1", "2" };
	private static final SingleFrameAnimation[] ANIMATION_FRAMES = { new SingleFrameAnimation("0"),
			new SingleFrameAnimation("1"), new SingleFrameAnimation("2") };
	private Supplier<Void> action;
	private TimeTriggerAnimation animation;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		action = mock(Supplier.class);
		animation = new TimeTriggerAnimation(9, FRAMES, action);
	}

	@Test
	public void testActionTickable() {
		animation.decreaseProgress(13, null);
		verify(action).get();
	}

	@Test
	public void testCreationFromTimeAnimation() {
		TimeAnimation timeAnimation = AnimationFactory.createSimpleTimeAnimation(9, FRAMES);
		TimeTriggerAnimation newAnimation = new TimeTriggerAnimation(timeAnimation, action);
		for (int i = 0; i < ANIMATION_FRAMES.length; i++) {
			assertEquals("Frame " + i + " was not the same", ANIMATION_FRAMES[i].getFrame(),
					newAnimation.getFrames()[i].getFrame());
		}
		assertEquals("Timings were not equal", timeAnimation.getResetAmount(), newAnimation.getResetAmount());
		verify(action, times(0)).get();
	}

	@After
	public void tearDown() {
		action = null;
		animation = null;
	}
}
