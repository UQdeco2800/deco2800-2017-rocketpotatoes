package com.deco2800.potatoes.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.SingleFrameAnimation;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.EnemyStatistics;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.TreeStatistics;

public class StatisticsBuilder<T extends Tickable> {
	private float health;
	private List<TimeEvent<T>> events;
	private Function<T, Animation> animation;
	private ProgressBar progressBar;
	private int buildCost;
	private int buildTime;
	private List<TimeEvent<T>> buildEvents;
	private float speed;
	private Class<?> goal;
	private int attackSpeed;
	private float attackRange;

	public StatisticsBuilder() {
		events = new ArrayList<>();
		buildEvents = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public TreeStatistics createTreeStatistics() {
		// Checking here
		return new TreeStatistics((StatisticsBuilder<AbstractTree>) this);
	}

	@SuppressWarnings("unchecked")
	public EnemyStatistics createEnemyStatistics() {
		// Checking here
		return new EnemyStatistics((StatisticsBuilder<EnemyEntity>) this);
	}

	public StatisticsBuilder<T> setTexture(String texture) {
		animation = x -> new SingleFrameAnimation(texture);
		return this;
	}

	@SuppressWarnings("unchecked")
	public StatisticsBuilder<T> addEvent(TimeEvent<? extends T> event) {
		events.add((TimeEvent<T>) event);
		return this;
	}

	@SuppressWarnings("unchecked")
	public StatisticsBuilder<T> addBuildEvent(TimeEvent<? extends T> event) {
		buildEvents.add((TimeEvent<T>) event);
		return this;
	}

	// Automated getters and setters

	/**
	 * @return the health
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * @param health
	 *            the health to set
	 */
	public StatisticsBuilder<T> setHealth(float health) {
		this.health = health;
		return this;
	}

	/**
	 * @return the events
	 */
	public List<TimeEvent<T>> getEvents() {
		return events;
	}

	/**
	 * @return the animation
	 */
	public Function<T, Animation> getAnimation() {
		return animation;
	}

	/**
	 * @param animation
	 *            the animation to set
	 */
	public StatisticsBuilder<T> setAnimation(Function<T, Animation> animation) {
		this.animation = animation;
		return this;
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @param progressBar
	 *            the progressBar to set
	 */
	public StatisticsBuilder<T> setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
		return this;
	}

	/**
	 * @return the buildCost
	 */
	public int getBuildCost() {
		return buildCost;
	}

	/**
	 * @param buildCost
	 *            the buildCost to set
	 */
	public StatisticsBuilder<T> setBuildCost(int buildCost) {
		this.buildCost = buildCost;
		return this;
	}

	/**
	 * @return the buildTime
	 */
	public int getBuildTime() {
		return buildTime;
	}

	/**
	 * @param buildTime
	 *            the buildTime to set
	 */
	public StatisticsBuilder<T> setBuildTime(int buildTime) {
		this.buildTime = buildTime;
		return this;
	}

	/**
	 * @return the buildEvents
	 */
	public List<TimeEvent<T>> getBuildEvents() {
		return buildEvents;
	}

	/**
	 * @return the attackSpeed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param attackSpeed
	 *            the attackSpeed to set
	 */
	public StatisticsBuilder<T> setSpeed(float speed) {
		this.speed = speed;
		return this;
	}

	/**
	 * @return the goal
	 */
	public Class<?> getGoal() {
		return goal;
	}

	/**
	 * @param goal
	 *            the goal to set
	 */
	public StatisticsBuilder<T> setGoal(Class<?> goal) {
		this.goal = goal;
		return this;
	}

	/**
	 * @return the attackSpeed
	 */
	public int getAttackSpeed() {
		return attackSpeed;
	}

	/**
	 * @param attackSpeed
	 *            the attackSpeed to set
	 */
	public StatisticsBuilder<T> setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
		return this;
	}

	/**
	 * @return the attackRange
	 */
	public float getAttackRange() {
		return attackRange;
	}

	/**
	 * @param attackRange
	 *            the attackRange to set
	 */
	public StatisticsBuilder<T> setAttackRange(float attackRange) {
		this.attackRange = attackRange;
		return this;
	}
}
