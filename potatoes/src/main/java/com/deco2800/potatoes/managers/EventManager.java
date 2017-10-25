package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.gui.Gui;
import com.deco2800.potatoes.gui.RespawnGui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manager for all TimeEvents associated with tickable entities. <br>
 * <br>
 * If you know a better way implement this, please change
 */
public class EventManager extends Manager implements TickableManager, ForWorld {

	private static class EventPair {
		private final Tickable tickable;
		private final TimeEvent<Tickable> event;

		public EventPair(Tickable tickable, TimeEvent<Tickable> event) {
			this.tickable = tickable;
			this.event = event;
		}
	}

	private List<EventPair> events;

	/**
	 * Initializes this manager to have no events registered.
	 */
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
	private void tickAll(long deltaTime) {
		List<EventPair> finishedEvents = new ArrayList<>();
		for (int i = 0; i < events.size(); i++) {
			EventPair eventPair = events.get(i);
			eventPair.event.decreaseProgress(deltaTime, eventPair.tickable);

			//Gets remaining time before player respawns
			if(eventPair.tickable instanceof Player){
				Gui respawnGui =GameManager.get().getManager(GuiManager.class).getGui(RespawnGui.class);
				((RespawnGui)respawnGui).setCount(eventPair.event.getProgress());
			}
			if (eventPair.event.isCompleted()) {
				finishedEvents.add(eventPair);
			}
		}
		for (EventPair eventPair : finishedEvents) {
			unregisterEvent(eventPair.tickable, eventPair.event);
		}
	}

	/**
	 * Unregisters all events registerd with this manager
	 */
	public void unregisterAll() {
		events = new ArrayList<>();
	}

	@Override
	public void onTick(long i) {
		tickAll(i);
	}
}
