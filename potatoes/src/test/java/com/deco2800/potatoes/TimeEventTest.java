package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.deco2800.potatoes.entities.TimeEvent;

public class TimeEventTest {

	@Test
	public void testReseting() {
		TimeEvent event = new TimeEvent() {
			@Override
			public void action() {
			}
		};
		event.setDoReset(true);
		event.setResetAmount(100);
		event.setProgress(30);
		event.reset();
		assertEquals(event.getProgress(), 100);
		event.decreaseProgress(100);
		assertEquals(event.getProgress(), 100);
		event.setDoReset(false);
		event.decreaseProgress(100);
		assertEquals(event.getProgress(), 0);
	}

	@Test
	public void testAction() {
		class TimeEventActionTest extends TimeEvent {
			public boolean actionCompleted = false;
			@Override
			public void action() {
				actionCompleted = true;
			}
		};
		TimeEventActionTest event = new TimeEventActionTest();
		event.setProgress(1);
		event.decreaseProgress(1);
		assertEquals(event.actionCompleted, true);
	}

}
