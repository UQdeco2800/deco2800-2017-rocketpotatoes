package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.deco2800.potatoes.entities.TimeEvent;

public class TimeEventTest {

	public static class TestTimeEvent<T> extends TimeEvent<T> {
		private int actionCount = 0;

		public TestTimeEvent() {
			setDoReset(true);
			setResetAmount(1);
			reset();
		}

		@Override
		public void action(T param) {
			actionCount++;
		}

		@Override
		public TimeEvent<T> copy() {
			TestTimeEvent<T> copy = new TestTimeEvent<>();
			copy.setDoReset(isDoReset());
			copy.setProgress(getProgress());
			copy.setResetAmount(getResetAmount());
			copy.actionCount = actionCount;
			return copy;
		}

		public int getActionCount() {
			return actionCount;
		}

	}

	@Test
	public void testReseting() {
		TestTimeEvent<Object> event = new TestTimeEvent<Object>();
		event.setDoReset(true);
		event.setResetAmount(100);
		event.setProgress(30);
		event.reset();
		assertEquals("Event progress is not at the reset amount after reset", event.getProgress(), 100);
		event.decreaseProgress(100, null);
		assertEquals("Event progress is not at the reset amount after reset", event.getProgress(), 100);
		event.setDoReset(false);
		event.decreaseProgress(100, null);
		assertEquals("Event progress is not 0 after reset", event.getProgress(), 0);
	}

	@Test
	public void testAction() {
		TestTimeEvent<Object> event = new TestTimeEvent<Object>();
		event.setProgress(1);
		event.decreaseProgress(1, null);
		assertEquals("Action has not been called after resetting", event.getActionCount(), 1);
	}

}
