package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;

/**
 * A generic player instance for the game
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress, HasDirection {

	private static final transient String TEXTURE_LEFT = "squirrel";
	private static final transient String TEXTURE_RIGHT = "squirrel_right";
	private static final transient float HEALTH = 100f;
	private static final transient float ATTACK_RANGE = 8f;
	private static final transient int ATTACK_SPEED = 500;
	private static final EnemyStatistics STATS = initStats();
	private static final String enemyType = "squirrel";

	private static final float SPEED = 0.12f;
	private static Class<?> goal = Player.class;
	private Path path = null;
	private Box3D target = null;

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

	private Direction currentDirection; // The direction the enemy faces
	private Vector2 oldPos = Vector2.Zero; // Used to determine the enemy's change in direction
	public enum PlayerState {idle, walk, attack, damaged, death}  // useful for when sprites available

	public Squirrel() {
		// empty for serialization
	}

	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.47f, 0.47f, 0.47f, 0.60f, 0.60f, TEXTURE_LEFT, HEALTH, SPEED, goal);
		this.path = null;
	}


	/**
	 * Squirrel follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity
	 * moves directly towards the player once it reaches the end of it's path
	 *
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		PathManager pathManager = GameManager.get().getManager(PathManager.class);

		// check paths

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

		//flip sprite
		if (deltaX + deltaY >= 0) {
			//this.setTexture(TEXTURE_LEFT);
		} else {
			//this.setTexture(TEXTURE_RIGHT);
		}

		float changeX = (float) (SPEED * Math.cos(angle));
		float changeY = (float) (SPEED * Math.sin(angle));

		this.setPosX(getPosX() + changeX);
		this.setPosY(getPosY() + changeY);

		super.updateDirection();
	}

	@Override
	public Direction getDirection() { return currentDirection; }

	public void setDirection(Direction direction) {
		if (this.currentDirection != direction) {
			this.currentDirection = direction;
			updateSprites();
		}
	}

	/**
	 * Updates the direction of the player based on change in position.
	 */
/*	private void updateDirection() {
		if ((this.getPosX() - oldPos.x == 0) && (this.getPosY() - oldPos.y == 0)) {
			return;    // Not moving
		}
		double angularDirection = Math.atan2(this.getPosY() - oldPos.y, this.getPosX() - oldPos.x) * (180 / Math.PI);

		if (angularDirection >= -180 && angularDirection < -157.5) {
			this.setDirection(Direction.SouthWest);
		} else if (angularDirection >= -157.5 && angularDirection < -112.5) {
			this.setDirection(Direction.West);
		} else if (angularDirection >= -112.5 && angularDirection < -67.5) {
			this.setDirection(Direction.NorthWest);
		} else if (angularDirection >= -67.5 && angularDirection < -22.5) {
			this.setDirection(Direction.North);
		} else if (angularDirection >= -22.5 && angularDirection < 22.5) {
			this.setDirection(Direction.NorthEast);
		} else if (angularDirection >= 22.5 && angularDirection < 67.5) {
			this.setDirection(Direction.East);
		} else if (angularDirection >= 67.5 && angularDirection < 112.5) {
			this.setDirection(Direction.SouthEast);
		} else if (angularDirection >= 112.5 && angularDirection < 157.5) {
			this.setDirection(Direction.South);
		} else if (angularDirection >= 157.5 && angularDirection <= 180) {
			this.setDirection(Direction.SouthWest);
		}
		oldPos = new Vector2(this.getPosX(), this.getPosY());
	}
*/
	/**
	 * Updates the player sprite based on it's state and direction.
	 */
/*	public void updateSprites() {
		String type = "squirrel";
		String direction = "_" + getDirection().toString();

		this.setTexture(type + direction);
	}
*/
	public String getEnemyType() {
		return enemyType;
	}

	@Override
	public String toString() {
		return String.format("Squirrel at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

	@Override
	public ProgressBarEntity getProgressBar() {
		return PROGRESS_BAR;
	}

	private static EnemyStatistics initStats() {
		return new StatisticsBuilder<>().setHealth(HEALTH).setSpeed(SPEED)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE_LEFT)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, Player.class)).createEnemyStatistics();
	}

	@Override
	public EnemyStatistics getBasicStats() {
		return STATS;
	}

}
