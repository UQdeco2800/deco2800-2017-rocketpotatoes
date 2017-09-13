package com.deco2800.potatoes.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;

/**
 * Manager for all TimeEvents associated with tickable entities. <br>
 * <br>
 * If you know a better way implement this, please change
 */
public class EventManager extends Manager {

	private static class EventPair {
		private final Tickable tickable;
		private final TimeEvent<Tickable> event;

		public EventPair(Tickable tickable, TimeEvent<Tickable> event) {
			this.tickable = tickable;
			this.event = event;
		}
	}

	private List<EventPair> events;

	public EventManager() {
		events = new ArrayList<>();
	}

	/**
	 * Registers the given event with the given entity
	 */
	public void registerEvent(Tickable tickable, TimeEvent<? extends Tickable> event) {
		events.add(new EventPair(tickable, (TimeEvent<Tickable>) event));
	}

	/**
	 * Unregisters the given event associated with the given entity
	 */
	public void unregisterEvent(Tickable tickable, TimeEvent<? extends Tickable> event) {
		for (Iterator<EventPair> iterator = events.iterator(); iterator.hasNext();) {
			EventPair eventPair = iterator.next();
			if (eventPair.tickable == tickable && eventPair.event == event) {
				iterator.remove();
				return; // only removes 1 event
			}
		}
	}

	/**
	 * Unregisters all events associated with entity
	 */
	public void unregisterAll(Tickable tickable) {
		for (Iterator<EventPair> iterator = events.iterator(); iterator.hasNext();) {
			EventPair eventPair = iterator.next();
			if (eventPair.tickable == tickable) {
				iterator.remove();
			}
		}
	}

	/**
	 * Ticks all registered events. Completed events will be automatically unregistered
	 */
	public void tickAll(long deltaTime) {
		List<EventPair> finishedEvents = new ArrayList<>();
		// this line throws ConcurrentModificationException, not sure why
		for (int i = 0; i < events.size(); i++) {
			EventPair eventPair = events.get(i);
			eventPair.event.decreaseProgress(deltaTime, eventPair.tickable);
			if (eventPair.event.isCompleted()) {
				finishedEvents.add(eventPair);
			}
		}
		for (EventPair eventPair : finishedEvents) {
			unregisterEvent(eventPair.tickable, eventPair.event);
		}
	}

	public void unregisterAll() {
		events = new ArrayList<>();
	}
}
