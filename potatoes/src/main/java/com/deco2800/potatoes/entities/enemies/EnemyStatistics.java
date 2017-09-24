package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.BasicStatistics;
import com.deco2800.potatoes.entities.StatisticsBuilder;

/**
 * Class to represent attributes for enemy stats
 *
 * (broad implementation of enemy stats inspired by trees upgrade stats
 * (trees/UpgradeStats) - thanks trees team)
 */
public class EnemyStatistics extends BasicStatistics<EnemyEntity> {
	private final float speed;
	private final Class<?> goal;

	/**
	 * Creates this object from the properties stored in the given builder.
	 */
	public EnemyStatistics(StatisticsBuilder<EnemyEntity> builder) {
		super(builder);
		speed = builder.getSpeed();
		goal = builder.getGoal();
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
