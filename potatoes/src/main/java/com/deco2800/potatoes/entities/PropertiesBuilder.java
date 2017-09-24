package com.deco2800.potatoes.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.SingleFrameAnimation;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.EnemyProperties;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.TreeProperties;

/**
 * Class for storing information needed to create BasicStatistics or any of its
 * subclasses
 */
public class PropertiesBuilder<T extends Tickable> {
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
	private Function<T, Animation> deathAnimation;
	private Function<T, Animation> damageAnimation;

	/**
	 * Initializes this object to have all properties with their default values
	 * (probably null) and all lists to be empty
	 */
	public PropertiesBuilder() {
		events = new ArrayList<>();
		buildEvents = new ArrayList<>();
	}

	/**
	 * Returns a TreeStatistics object based on the properties stored in this object
	 */
	@SuppressWarnings("unchecked")
	public TreeProperties createTreeStatistics() {
		// Checking here
		return new TreeProperties((PropertiesBuilder<AbstractTree>) this);
	}

	/**
	 * Returns an EnemyStatistics object based on the properties stored in this object
	 */
	@SuppressWarnings("unchecked")
	public EnemyProperties createEnemyStatistics() {
		// Checking here
		return new EnemyProperties((PropertiesBuilder<EnemyEntity>) this);
	}

	/**
	 * @param texture the texture to set
	 */
	public PropertiesBuilder<T> setTexture(String texture) {
		setAnimation(x -> new SingleFrameAnimation(texture));
		return this;
	}

	/**
	 * Adds the given event to the list of events in this object
	 */
	@SuppressWarnings("unchecked")
	public PropertiesBuilder<T> addEvent(TimeEvent<? extends T> event) {
		events.add((TimeEvent<T>) event);
		return this;
	}

	/**
	 * Add all events in the list of events given to the events in this object
	 */
	public PropertiesBuilder<T> addAllEvents(List<TimeEvent<? extends T>> events) {
		events.forEach(this::addEvent);
		return this;
	}

	/**
	 * Adds the given event to the list of build events in this object
	 */
	@SuppressWarnings("unchecked")
	public PropertiesBuilder<T> addBuildEvent(TimeEvent<? extends T> event) {
		buildEvents.add((TimeEvent<T>) event);
		return this;
	}

	/**
	 * Add all events in the list of events given to the build events in this object
	 */
	public PropertiesBuilder<T> addAllBuildEvents(List<TimeEvent<? extends T>> events) {
		events.forEach(this::addBuildEvent);
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
	public PropertiesBuilder<T> setHealth(float health) {
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
	public PropertiesBuilder<T> setAnimation(Function<T, Animation> animation) {
		this.animation = animation;
		if (deathAnimation == null) {
			deathAnimation = animation;
		}
		if (damageAnimation == null) {
			damageAnimation = animation;
		}
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
	public PropertiesBuilder<T> setProgressBar(ProgressBar progressBar) {
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
	public PropertiesBuilder<T> setBuildCost(int buildCost) {
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
	public PropertiesBuilder<T> setBuildTime(int buildTime) {
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
	public PropertiesBuilder<T> setSpeed(float speed) {
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
	public PropertiesBuilder<T> setGoal(Class<?> goal) {
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
	public PropertiesBuilder<T> setAttackSpeed(int attackSpeed) {
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
	public PropertiesBuilder<T> setAttackRange(float attackRange) {
		this.attackRange = attackRange;
		return this;
	}

	/**
	 * @return the deathAnimation
	 */
	public Function<T, Animation> getDeathAnimation() {
		return deathAnimation;
	}

	/**
	 * @param deathAnimation the deathAnimation to set
	 */
	public PropertiesBuilder<T> setDeathAnimation(Function<T, Animation> deathAnimation) {
		this.deathAnimation = deathAnimation;
		return this;
	}

	/**
	 * @return the damageAnimation
	 */
	public Function<T, Animation> getDamageAnimation() {
		return damageAnimation;
	}

	/**
	 * @param damageAnimation the damageAnimation to set
	 */
	public PropertiesBuilder<T> setDamageAnimation(Function<T, Animation> damageAnimation) {
		this.damageAnimation = damageAnimation;
		return this;
	}
}
