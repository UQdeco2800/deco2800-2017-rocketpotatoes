package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;

/**
 * A moose enemy for the game. Has the special ability of a healing buff to itself and those around it
 */
public class Moose extends EnemyEntity implements Tickable, HasProgress, HasDirection {

	private static final transient String TEXTURE_LEFT = "pronograde"; // TODO: MAKE MOOSE TEXTURE
	private static final transient String TEXTURE_RIGHT = "pronograde";
	private static final transient float HEALTH = 100f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;
	private static final transient String enemyType = "moose";
	private static final EnemyProperties STATS = initStats();

	private static final float moose_size = 1.5f;

	private static float speed = 0.04f;
	private static Class<?> goal = GoalPotate.class;
	private Path path = null;
	private Box3D target = null;

	private int ticksSinceRandom = 0;
	private static final int MAX_WAIT = 200;

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

	private Direction currentDirection; // The direction the enemy faces

	/**
	 * Empty constructor for serialization
	 */
	public Moose() {
		// empty for serialization
	}

	/***
	 * Constructs a new moose entity with pre-defined size and rendering lengths to match
	 *
	 * @param posX The x coordinate the created squirrel will spawn from
	 * @param posY The y coordinate the created squirrel will spawn from
	 * @param posZ The z coordinate the created squirrel will spawn from
	 */
	public Moose(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.60f, 0.60f, 0.60f, moose_size, moose_size, TEXTURE_LEFT, HEALTH, speed, goal);
		this.speed = speed;
		this.goal = goal;
		this.path = null;
		this.damageScaling = 0.8f; // 20% Damage reduction for Moose
	}

	/**
	 * Change the target for the moose to a random location
	 */
	private void randomTarget() {
		float x = (float) Math.random() * GameManager.get().getWorld().getLength();
		float y = (float) Math.random() * GameManager.get().getWorld().getWidth();
		target = new Box3D(x, y, 0, 1f, 1f, 1f);
	}

	/**
	 * @return String of this type of enemy (ie 'moose').
	 * */
	public String getEnemyType() { return enemyType; }

	/**
	 *	@return the current Direction of moose
	 * */
	public Direction getDirection() { return currentDirection; }

	/**
	 * Moose follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity
	 * moves directly towards the player once it reaches the end of it's path
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		PathManager pathManager = GameManager.get().getManager(PathManager.class);
		boolean changeLocation = false;
		if (++ticksSinceRandom == MAX_WAIT || target == null) {
			ticksSinceRandom = 0;
			changeLocation = true;
			randomTarget();
		}
		// check paths

		//check collision
		for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
			if (entity.isStaticCollideable() && this.getBox3D().overlaps(entity.getBox3D())) {
				//collided with wall
				randomTarget();
				//break;
			} else if (entity instanceof EnemyEntity && entity.getBox3D().overlaps(this.getBox3D())) {
				EnemyEntity enemy = (EnemyEntity) entity;
				//heal enemy if within range
				enemy.heal(0.1f);
			}
		}

		// check that we actually have a path
		if (path == null || path.isEmpty()) {
			path = pathManager.generatePath(this.getBox3D(), target);
		}


		//check if close enough to target
		if (target != null && playerManager.getPlayer().getBox3D().overlaps(this.getBox3D())) {
			target = playerManager.getPlayer().getBox3D();
			playerManager.getPlayer().damage(0.4f);
		} else {
			target = null;
		}

		//check if the path has another node
		if (target == null && !path.isEmpty()) {
			target = path.pop();
		}

		float targetX;
		float targetY;


		if (target == null) {
			target = playerManager.getPlayer().getBox3D();
		}

		targetX = target.getX();
		targetY = target.getY();

		float deltaX = getPosX() - targetX;
		float deltaY = getPosY() - targetY;

		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

		//flip sprite
		if (deltaX + deltaY >= 0) {
			this.setTexture(TEXTURE_LEFT);
		} else {
			this.setTexture(TEXTURE_RIGHT);
		}

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		this.setPosX(getPosX() + changeX);
		this.setPosY(getPosY() + changeY);
	}

	/**
	 * @return string representation of this class including its enemytype and x,y coordinates
	 */
	@Override
	public String toString() {
		return String.format("%s at (%d, %d)", getEnemyType(), (int) getPosX(), (int) getPosY());
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
	 * @return
	 */
	private static EnemyProperties initStats() {
		EnemyProperties result = new PropertiesBuilder<>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE_LEFT)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, GoalPotate.class)).createEnemyStatistics();
		return result;
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