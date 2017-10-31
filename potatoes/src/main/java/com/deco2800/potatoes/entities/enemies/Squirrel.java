package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.enemies.enemyactions.MeleeAttackEvent;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.util.Path;


/**
 * The standard & most basic enemy in the game - a squirrel. Moves towards base portal unless within close enough
 * distance of the player at which point it will chase the player. Attacks portal & player.
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress {

	private static final transient String TEXTURE_LEFT = "squirrel";
	private static final transient float HEALTH = 50f;
	private static final transient float ATTACK_RANGE = 8f;
	private static final transient int ATTACK_SPEED = 500;
	private static final EnemyProperties STATS = initStats();
	private static final String[] ENEMY_TYPE = new String[]{
			"squirrel",
			"squirrel",
	};
	private static final float SPEED = 0.05f;

	private static Class<?> goal = Player.class;

	private EnemyTargets targets = initTargets();

	private Path path = null;
	private Shape2D target = null;
	private PathAndTarget pathTarget = new PathAndTarget(path, target);

	private long sTime = System.currentTimeMillis();
	private float phealth = getHealth();
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthBarRed", 1.5f);

	/***
	 * Default constructor for serialization
	 */
	public Squirrel() {
		this(0, 0);
	}

	/**
	 * Constructs a new Squirrel entity with pre-defined size and rendering lengths to match.
	 *
	 * @param posX The x coordinate the created squirrel will spawn from
	 * @param posY The y coordinate the created squirrel will spawn from
	 */
	public Squirrel(float posX, float posY) {
        super(new Circle2D(posX, posY, 0.5f), 0.7f, 0.7f, TEXTURE_LEFT, HEALTH, SPEED, goal);
		Squirrel.goal = goal;
		this.path = null;
		setDelayTime(100);
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
			super.updateDirection();
		}
	}
	/**
	 * Set the enemy state
	 */
	public void enemyState(){
		//Check if attacking
		if(isAttacking()){
			sTime = System.currentTimeMillis();
			setTextureLength(7);
			setEnemyStatus("_attack");
			phealth=getHealth();
		}
		//Check if walking
		if((System.currentTimeMillis()-sTime)/1000.0>3){

			setTextureLength(7);
			setEnemyStatus("_walk");
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
	 * @return String of this type of enemy (ie 'squirrel').
	 * */
	@Override
	public String[] getEnemyType() {
		return ENEMY_TYPE;
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
	 */
	private static EnemyProperties initStats() {
		return new PropertiesBuilder<>().setHealth(HEALTH).setSpeed(SPEED)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE_LEFT)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, BasePortal.class))
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, Player.class))
				.createEnemyStatistics();
	}

	/***
	 * @return the EnemyStatistics of enemy which contain various governing stats of this enemy
	 */
	@Override
	public EnemyProperties getBasicStats() {
		return STATS;
	}
}
