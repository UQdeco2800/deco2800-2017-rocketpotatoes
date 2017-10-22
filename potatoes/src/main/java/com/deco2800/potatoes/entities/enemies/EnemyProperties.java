package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.BasicProperties;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.TimeEvent;

import java.util.List;

/**
 * Class to represent attributes for enemy stats
 *
 */
public class EnemyProperties extends BasicProperties<EnemyEntity> {
	private final float speed;
	private final Class<?> goal;
	PropertiesBuilder<EnemyEntity> builder;

	/**
	 * Creates this object from the properties stored in the given builder.
	 */
	public EnemyProperties(PropertiesBuilder<EnemyEntity> builder) {
		super(builder);
		speed = builder.getSpeed();
		goal = builder.getGoal();
		this.builder = builder;
	}

	/**
	 * @return the events
	 */
	public List<TimeEvent<EnemyEntity>> getEvents() {
		return this.builder.getEvents();
	}

	/**
	 * @return the SPEED
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @return the goal
	 */
	public Class<?> getGoal() {
		return goal;
	}
}
