package com.deco2800.potatoes.managers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.TimeEventTest.TestTimeEvent;
import com.deco2800.potatoes.entities.Tickable;

public class EventManagerTest {
	EventManager eventManager;
	List<TestTimeEvent<Tickable>> events;
	Tickable tickable1;
	Tickable tickable2;

	@Before
	public void testSetup() {
		tickable1 = new Tickable() {
			@Override
			public void onTick(long time) {
			}
		};
		tickable2 = new Tickable() {
			@Override
			public void onTick(long time) {
			}
		};
		eventManager = GameManager.get().getManager(EventManager.class);
		events = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			events.add(new TestTimeEvent<>());
			eventManager.registerEvent(i < 3 ? tickable1 : tickable2, events.get(i));
		}
	}

	@After
	public void tearDown() {

		GameManager.get().clearManagers();
		events.clear();
		tickable1 = null;
		tickable2 = null;
	}

	@Test
	public void testUnregisterEvent() {
		eventManager.unregisterEvent(tickable1, events.get(0));
		eventManager.unregisterEvent(tickable2, events.get(3));
		eventManager.onTick(1);
		int[] results = { 0, 1, 1, 0, 1 };
		int[] actionCounts = new int[5];
		for (int i = 0; i < events.size(); i++) {
			actionCounts[i] = events.get(i).getActionCount();

		}
		assertArrayEquals("Unregistering event did", actionCounts, results);
	}

	@Test
	public void testUnregisterAll() {
		eventManager.unregisterAll(tickable1);
		eventManager.onTick(1);
		int[] results = { 0, 0, 0, 1, 1 };
		int[] actionCounts = new int[5];
		for (int i = 0; i < events.size(); i++) {
			actionCounts[i] = events.get(i).getActionCount();

		}
		// Expand on this description
		assertArrayEquals("Ticked events did not match expected", actionCounts, results);
		eventManager.unregisterAll();
	}

	@Test
	public void testTickAll() {
		eventManager.onTick(1);
		int[] results = { 1, 1, 1, 1, 1 };
		int[] actionCounts = new int[5];
		for (int i = 0; i < events.size(); i++) {
			actionCounts[i] = events.get(i).getActionCount();

		}
		assertArrayEquals("Ticking all events didn't run all actions", actionCounts, results);
	}

}
