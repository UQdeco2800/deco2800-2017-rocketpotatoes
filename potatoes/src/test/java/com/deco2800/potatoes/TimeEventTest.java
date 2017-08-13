package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.deco2800.potatoes.entities.TimeEvent;

public class TimeEventTest {

	@Test
	public void testReseting() {
		TimeEvent<Object> event = new TimeEvent<Object>() {
			@Override
			public void action(Object o) {
			}

			@Override
			public TimeEvent<Object> copy() {
				return null;
			}
		};
		event.setDoReset(true);
		event.setResetAmount(100);
		event.setProgress(30);
		event.reset();
		assertEquals(event.getProgress(), 100);
		event.decreaseProgress(100, null);
		assertEquals(event.getProgress(), 100);
		event.setDoReset(false);
		event.decreaseProgress(100, null);
		assertEquals(event.getProgress(), 0);
	}

	@Test
	public void testAction() {
		class TimeEventActionTest extends TimeEvent<Object> {
			public boolean actionCompleted = false;

			@Override
			public void action(Object o) {
				actionCompleted = true;
			}

			@Override
			public TimeEvent<Object> copy() {
				// TODO Auto-generated method stub
				return null;
			}
		}
		;
		TimeEventActionTest event = new TimeEventActionTest();
		event.setProgress(1);
		event.decreaseProgress(1, null);
		assertEquals(event.actionCompleted, true);
	}

}
