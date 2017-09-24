package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;

/**
 * The standard / most basic enemy in the game - a squirrel.
 *
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress {

	private static final transient String TEXTURE_LEFT = "squirrel";
	private static final transient float HEALTH = 100f;
	private static final transient float ATTACK_RANGE = 8f;
	private static final transient int ATTACK_SPEED = 500;
	private static final EnemyProperties STATS = initStats();
	private static final String enemyType = "squirrel";

	private static final float SPEED = 0.12f;
	private static Class<?> goal = Player.class;
	private Path path = null;
	private Box3D target = null;

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

	private Direction currentDirection; // The direction the enemy faces
	public enum PlayerState {idle, walk, attack, damaged, death}  // useful for when sprites for different states become available

	/***
	 * Default constructor for serialization
	 */
	public Squirrel() {
		// empty for serialization
	}

	/***
	 * Constructs a new Squirrel entity with pre-defined size and rendering lengths to match.
	 *
	 * @param posX The x coordinate the created squirrel will spawn from
	 * @param posY The y coordinate the created squirrel will spawn from
	 * @param posZ The z coordinate the created squirrel will spawn from
	 */
	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.47f, 0.47f, 0.47f, 0.60f, 0.60f, TEXTURE_LEFT, HEALTH, SPEED, goal);
		this.path = null;
	}


	/**
	 * Squirrel follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity
	 * moves directly towards the player once it reaches the end of it's path
	 *
	 * @param i The current game tick
	 */
	@Override
	public void onTick(long i) {
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		PathManager pathManager = GameManager.get().getManager(PathManager.class);

		/*
		//check collision
		for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
			if (entity.isStaticCollideable() && this.getBox3D().overlaps(entity.getBox3D())) {
				//collided with wall
				path = pathManager.generatePath(this.getBox3D(), playerManager.getPlayer().getBox3D());
				target = path.pop();
				break;
			}
		}
		*/

		// check that we actually have a path
		if (path == null || path.isEmpty()) {
			path = pathManager.generatePath(this.getBox3D(), playerManager.getPlayer().getBox3D());
		}

		//check if close enough to target
		if (target != null && target.overlaps(this.getBox3D())) {
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

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		float changeX = (float) (SPEED * Math.cos(angle));
		float changeY = (float) (SPEED * Math.sin(angle));

		this.setPosX(getPosX() + changeX);
		this.setPosY(getPosY() + changeY);

		updateDirection();
	}

	/**
	 *	@return the current Direction of squirrel
	 * */
	@Override
	public Direction getDirection() { return currentDirection; }

	/**
	 * @return String of this type of enemy (ie 'squirrel').
	 * */
	public String getEnemyType() {
		return enemyType;
	}

	/**
	 * @return string representation of this class including its enemytype and x,y coordinates
	 */
	@Override
	public String toString() {
		return String.format("%s at (%d, %d)", getEnemyType (), (int) getPosX(), (int) getPosY());
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
		return new PropertiesBuilder<>().setHealth(HEALTH).setSpeed(SPEED)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE_LEFT)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, Player.class)).createEnemyStatistics();
	}

	/***
	 * @return the EnemyStatistics of enemy which contain various governing stats of this enemy
	 */
	@Override
	public EnemyProperties getBasicStats() {
		return STATS;
	}

}
