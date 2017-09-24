package com.deco2800.potatoes.entities;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.animation.TimeTriggerAnimation;
import com.deco2800.potatoes.entities.health.MortalEntity;
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
	private final Function<T, Animation> deathAnimation;
	private final Function<T, Animation> damageAnimation;

	/**
	 * Creates this object from the properties stored in the given builder.
	 */
	public BasicProperties(PropertiesBuilder<T> builder) {
		health = builder.getHealth();
		events = builder.getEvents();
		animation = builder.getAnimation();
		progressBar = builder.getProgressBar();
		deathAnimation = builder.getDeathAnimation();
		damageAnimation = builder.getDamageAnimation();
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
	 * Sets the death animation for the given tickable to be this object's death animation
	 */
	public void setDeathAnimation(T tickable) {
		Supplier<Void> completionHandler = () -> {
			((MortalEntity) tickable).setDying(false);
			return null; // ughh, void != Void
		};
		setNewTriggerAnimation(tickable, deathAnimation.apply(tickable), completionHandler);
	}

	/**
	 * Sets the damage animation for the given tickable to be this object's damage animation
	 */
	public void setDamageAnimation(T tickable) {
		if (tickable instanceof Animated) {
			Animated t = (Animated) tickable;
			Animation oldAnimation = t.getAnimation();
			Supplier<Void> completionHandler = () -> {
				t.setAnimation(oldAnimation);
				return null; // ughh, void != Void
			};
			setNewTriggerAnimation(tickable, damageAnimation.apply(tickable), completionHandler);
		}
	}

	private void setNewTriggerAnimation(T tickable, Animation normalAnimation, Supplier<Void> completionHandler) {
		if (normalAnimation instanceof TimeAnimation && tickable instanceof Animated) {
			TimeTriggerAnimation newAnimation = new TimeTriggerAnimation((TimeAnimation) normalAnimation,
					completionHandler);
			AnimationFactory.registerTimeAnimations(newAnimation, tickable);
			((Animated) tickable).setAnimation(newAnimation);
		} else {
			completionHandler.get();
		}
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
