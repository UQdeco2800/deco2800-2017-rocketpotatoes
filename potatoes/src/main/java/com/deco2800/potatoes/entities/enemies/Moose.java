package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.enemies.enemyactions.ChannelEvent;
import com.deco2800.potatoes.entities.enemies.enemyactions.HealingWaveEvent;
import com.deco2800.potatoes.entities.enemies.enemyactions.MeleeAttackEvent;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Path;

/**
 * A moose enemy for the game. Has the special ability of a healing buff to itself and those around it
 */
public class Moose extends EnemyEntity implements Tickable, HasProgress {

	private static final transient String TEXTURE_LEFT = "pronograde";
	private static final transient float HEALTH = 100f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;
	private static final transient String[] ENEMY_TYPE = new String[]{
			"moose"
	};
	private static final EnemyProperties STATS = initStats();

	private static final float MOOSE_SIZE = 1.5f;

	private static float speed = 0.04f;
	private static Class<?> goal = GoalPotate.class;
	private Path path = null;
	private Shape2D target = null;
	private PathAndTarget pathTarget = new PathAndTarget(path, target);
	private EnemyTargets targets = initTargets();

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthBarRed", 1);

	/**
	 * Empty constructor for serialization
	 */
	public Moose() {
		//Empty for serialization
	}

	/***
	 * Constructs a new moose entity with pre-defined size and rendering lengths to match
	 *
	 * @param posX The x coordinate the created squirrel will spawn from
	 * @param posY The y coordinate the created squirrel will spawn from
	 */
	public Moose(float posX, float posY) {
		super(new Circle2D(posX, posY, 0.849f), MOOSE_SIZE, MOOSE_SIZE, TEXTURE_LEFT, HEALTH, speed, goal);
		this.path = null;
		this.damageScaling = 0.8f; // 20% Damage reduction for Moose
	}

	/**
	 * Change the target for the moose to a random location
	 */
	public void randomTarget() {
		float x = (float) Math.random() * GameManager.get().getWorld().getLength();
		float y = (float) Math.random() * GameManager.get().getWorld().getWidth();
		target = new Point2D(x, y);
	}

	/**
	 * @return String of this type of enemy (ie 'moose').
	 */
	@Override
	public String[] getEnemyType() {
		return ENEMY_TYPE;
	}

	/***
	 * Actions to be performed on every tick of the game
	 *
	 * @param i the current game tick
	 */
	@Override
	public void onTick(long i) {
		AbstractEntity relevantTarget = super.mostRelevantTarget(targets);
		if (getMoving()) {
			pathMovement(pathTarget, relevantTarget);
			super.onTickMovement();
			super.updateDirection();
		}
	}

	/**
	 * @return string representation of this class including its enemytype and x,y coordinates
	 */
	@Override
	public String toString() {
		return String.format("%s at (%d, %d)", getEnemyType()[0], (int) getPosX(), (int) getPosY());
	}

	/***
	 * Gets the progress bar that corresponds to the health of this enemy
	 * @return ProgressBarEntity corresponding to enemy's health
	 */
	@Override
	public ProgressBarEntity getProgressBar() {
		return PROGRESS_BAR;
	}

	/***
	 * Initialise EnemyStatistics belonging to this enemy which is referenced by other classes to control
	 * enemy.
	 *
	 * @return EnemyProperties
	 */
	private static EnemyProperties initStats() {
		HealingWaveEvent healingWave = new HealingWaveEvent(3500, 8f, 80f);
		return new PropertiesBuilder<>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE_LEFT)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, BasePortal.class))
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, Player.class))
				.addEvent(new ChannelEvent(50, 1000, healingWave))
				.addEvent(healingWave)
				.createEnemyStatistics();
	}

	/***
	 *
	 * @return the EnemyStatistics of enemy which contain various governing stats of this enemy
	 */
	@Override
	public EnemyProperties getBasicStats() {
		return STATS;
	}
}
