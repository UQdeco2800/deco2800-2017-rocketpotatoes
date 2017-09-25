package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.List;
import com.deco2800.potatoes.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.util.Path;

/**
 * A stronger but slower enemy type, only attacks towers/trees
 */
public class TankEnemy extends EnemyEntity implements Tickable, HasDirection {

	private static final Logger LOGGER = LoggerFactory.getLogger(TankEnemy.class);
	private static final EnemyProperties STATS = initStats();
	private static final transient String TEXTURE = "tankBear";
	private static final transient String TEXTURE_LEFT = "tankBear";
	private static final transient String TEXTURE_RIGHT = "tankBear";
	private static final transient float HEALTH = 1000;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;
	private static final transient String enemyType = "bear";

	/* Define speed, goal and path variables */
	private static float speed = 0.006f;
	private static Class<?> goal = AbstractTree.class;
	private Path path = null;
	private CollisionMask target = null;
	/* Define variables for the TankEnemy's progress bar */
	private static final List<Color> COLOURS = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity(COLOURS);

	private Direction currentDirection; // The direction the enemy faces

	/**
	 * Empty constructor for serialization
	 */
	public TankEnemy() {
        this(0, 0);
	}

	/**
	 * Construct a new Tank Enemy at specific position with pre-defined size and render-length.
	 *
	 * @param posX The x-coordinate of the Tank Enemy.
	 * @param posY The y-coordinate of the Tank Enemy.
	 */
	public TankEnemy(float posX, float posY) {
        super(new Circle2D(posX, posY, 1.414f), 1f, 1f, TEXTURE, HEALTH, speed, goal);
		//this.speed = getBasicStats().getSpeed();
		//this.goal = goal;
		//resetStats();
	}

	/**
	 * Initialize basic statistics for Tank Enemy
	 *
	 * @return basic statistics of this Tank Enemy
	 */
	private static EnemyProperties initStats() {
		return new PropertiesBuilder<>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, AbstractTree.class)).createEnemyStatistics();
	}

	/**
	 * Get basic statistics of this Tank Enemy
	 *
	 * @return Get basic statistics of this Tank Enemy
	 */
	@Override
	public EnemyProperties getBasicStats() {
		return STATS;
	}

	/**
	 * @return the current Direction of bear
	 */
	@Override
	public Direction getDirection() {
		return currentDirection;
	}

	/**
	 * @return String of this type of enemy (ie 'bear').
	 */
	@Override
	public String getEnemyType() {
		return enemyType;
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
	 * Get the health PROGRESS_BAR of this Tank Enemy
	 *
	 * @return the health PROGRESS_BAR of this Tank Enemy
	 */
	@Override
	public ProgressBarEntity getProgressBar() {
		return PROGRESS_BAR;
	}
}
