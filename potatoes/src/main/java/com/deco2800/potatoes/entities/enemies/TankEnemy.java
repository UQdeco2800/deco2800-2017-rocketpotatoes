package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.enemies.enemyactions.MeleeAttackEvent;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.util.Path;

import java.util.Arrays;
import java.util.List;

/**
 * A stronger but slower enemy type, only attacks towers/trees.
 *
 * @author Team 11
 */
public class TankEnemy extends EnemyEntity implements Tickable {

	private static final EnemyProperties STATS = initStats();
	private static final transient String TEXTURE = "tankBear";
	private static final transient float HEALTH = 2000;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;
	private long sTime=0;
	private float phealth =getHealth();
	private static final transient String[] ENEMY_TYPE = new String[]{
		"bear",
		"bear",
		"bear",
		"bear",
		"bear",
		"bear",
		"bear",
		"bear",
	};

	/* Define speed, goal and path variables */
	private static float speed = 0.008f;
	private static Class<?> goal = AbstractTree.class;

	private Path path = null;
	private Shape2D target = null;
	private PathAndTarget pathTarget = new PathAndTarget(path, target);
	private EnemyTargets targets = initTargets();

	/* Define variables for the TankEnemy's progress bar */
	private static final List<Color> COLOURS = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity(COLOURS);

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
        super(new Circle2D(posX, posY, 1.414f), 2f, 2f, TEXTURE, HEALTH, speed, goal);
        this.health = health + (roundNum*250);
	}

	/***
	 * Actions to be performed on every tick of the game
	 *
	 * @param i the current game tick
	 */
	@Override
	public void onTick(long i) {
		enemyState();
		AbstractEntity relevantTarget = super.mostRelevantTarget(targets);
		if (getMoving()) {
			pathMovement(pathTarget, relevantTarget);
			super.onTickMovement();
		}
		super.updateDirection();
	}
	/**
	 * Set the enemy state
	 */
	public void enemyState(){
		//Check if attacking
		if(isAttacking()){
			sTime = System.currentTimeMillis();
			setEnemyStatus("attack");
			phealth=getHealth();
		}
		//Check if walking
		if((System.currentTimeMillis()-sTime)/1000.0>3){
			setEnemyStatus("walk");
		}
	}

	/**
	 * Determine if the tank is currently attacking.
	 *
	 * @return true if the tank is attacking
	 */
	public boolean isAttacking(){
		if((int)phealth!=(int)getHealth()){
			return true;
		}
		return false;
	};

	/**
	 * Initialize basic statistics for Tank Enemy
	 *
	 * @return basic statistics of this Tank Enemy
	 */
	private static EnemyProperties initStats() {
		return new PropertiesBuilder<>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE)
				//.addEvent(new MeleeAttackEvent(ATTACK_SPEED, AbstractTree.class))
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, Player.class))
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, BasePortal.class))
				.createEnemyStatistics();
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
	 * @return String of this type of enemy (ie 'bear').
	 */
	@Override
	public String[] getEnemyType() {
		return ENEMY_TYPE;
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
