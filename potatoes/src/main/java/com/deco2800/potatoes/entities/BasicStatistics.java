package com.deco2800.potatoes.entities;

import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

public class BasicStatistics<T extends Tickable> {
	private final float health;
	private final List<TimeEvent<T>> events;
	private final Function<T, Animation> animation;
	private final ProgressBar progressBar;

	/**
	 * 
	 * @param health
	 *            The maximum health for these stats
	 * @param events
	 *            The TimeEvents for these stats
	 * @param animation
	 *            The animation (or single texture) for these stats
	 * @param progressBar
	 *            The progress bar
	 */
	public BasicStatistics(StatisticsBuilder<T> builder) {
		health = builder.getHealth();
		events = builder.getEvents();
		animation = builder.getAnimation();
		progressBar = builder.getProgressBar();
	}

	public void registerEvents(T tickable) {
		unregisterEvents(tickable);
		for (TimeEvent<T> timeEvent : events) {
			GameManager.get().getManager(EventManager.class).registerEvent(tickable, timeEvent.copy());
		}
	}

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
