package com.deco2800.potatoes.entities;

import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

/**
 * Class storing basic properties for a tickable entity
 */
public class BasicProperties<T extends Tickable> {
	private final float health;
	private final List<TimeEvent<T>> events;
	private final Function<T, Animation> animation;
	private final ProgressBar progressBar;

	/**
	 * Creates this object from the properties stored in the given builder.
	 */
	public BasicProperties(PropertiesBuilder<T> builder) {
		health = builder.getHealth();
		events = builder.getEvents();
		animation = builder.getAnimation();
		progressBar = builder.getProgressBar();
	}

	/**
	 * Registers all events stored in this object with the tickable given. All
	 * events registered with the tickable will be unregistered
	 */
	public void registerEvents(T tickable) {
		unregisterEvents(tickable);
		for (TimeEvent<T> timeEvent : events) {
			GameManager.get().getManager(EventManager.class).registerEvent(tickable, timeEvent.copy());
		}
		if (tickable instanceof Animated) {
			AnimationFactory.registerTimeAnimations(((Animated) tickable).getAnimation(), tickable);
		}
	}

	/**
	 * Unregisters all events for the tickable given.
	 */
	public void unregisterEvents(T tickable) {
		GameManager.get().getManager(EventManager.class).unregisterAll(tickable);
	}

	/**
	 * @return the health
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * @return the animation
	 */
	public Function<T, Animation> getAnimation() {
		return animation;
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}
}
