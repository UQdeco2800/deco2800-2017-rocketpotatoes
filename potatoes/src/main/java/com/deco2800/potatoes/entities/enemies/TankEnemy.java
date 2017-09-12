package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;


import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;

/**
 * A stronger but slower enemy type, only attacks towers/trees 
 */
public class TankEnemy extends EnemyEntity implements Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TankEnemy.class);
	private static final BasicStats STATS = initStats();
	private static final transient String TEXTURE = "tankBear";
	private static final transient String TEXTURE_LEFT = "tankBear";
	private static final transient String TEXTURE_RIGHT = "tankBear";
	private static final transient float HEALTH = 400f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;

	/* Define speed, goal and path variables */
	private static float speed = 0.006f;
	private static Class<?> goal = Tower.class;
	private Path path = null;
	private Box3D target = null;

	/* Define variables for the TankEnemy's progress bar */
	private static final List<Color> colours = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity progressBar = new ProgressBarEntity(colours);

	
	/**
	 * Empty constructor for serialization
	 */
	public TankEnemy() {
	}

	/**
	 * Construct a new Tank Enemy at specific position
	 * @param posX
	 *            The x-coordinate of the Tank Enemy.
	 * @param posY
	 *            The y-coordinate of the Tank Enemy.
	 * @param posZ
	 *            The z-coordinate of the Tank Enemy.
	 */
	public TankEnemy(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH, speed, goal);
		//this.speed = getBasicStats().getSpeed();
		//this.goal = goal;
		//resetStats();
	}

	/**
	 * Initialize basic statistics for Tank Enemy
	 * @return basic statistics of this Tank Enemy
	 */
	private static BasicStats initStats() {
		List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
		BasicStats result = new BasicStats(HEALTH, speed, ATTACK_RANGE, ATTACK_SPEED, normalEvents, TEXTURE);
		result.getNormalEventsReference().add(new MeleeAttackEvent(result.getAttackSpeed(), Player.class));
		return result;
	}

	/**
	 * Get basic statistics of this Tank Enemy
	 * @return Get basic statistics of this Tank Enemy
	 */
	@Override
	public BasicStats getBasicStats() {
		return STATS;
	}
	
	/**
	 * String representation of this Tank Enemy and its position
	 * 
	 * @return String representation of this Tank Enemy and its position
	 */
	@Override
	public String toString() {
		return String.format("Tank Enemy at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

	/**
	 * Get the health progressBar of this Tank Enemy
	 * @return the health progressBar of this Tank Enemy
	 */
	@Override
	public ProgressBarEntity getProgressBar() {
		return progressBar;
	}
}
