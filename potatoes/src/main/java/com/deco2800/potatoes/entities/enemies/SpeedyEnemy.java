package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.deco2800.potatoes.entities.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * A speedy raccoon enemy that steals resources from resource trees.
 */
public class SpeedyEnemy extends EnemyEntity implements Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpeedyEnemy.class);
	private static final transient String TEXTURE = "speedyRaccoon";
	private static final transient float HEALTH = 80f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 2000;
	private static final transient String ENEMY_TYPE = "raccoon";

	private static final EnemyProperties STATS = initStats();

	private static float speed = 0.08f;
	private static Class<?> goal = ResourceTree.class;
	private Path path = null;
	private Shape2D target = null;

	private static final List<Color> COLOURS = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity PROGRESSBAR = new ProgressBarEntity(COLOURS);

	private Direction currentDirection; // The direction the enemy faces
	//public enum PlayerState {idle, walk, attack, damaged, death}  // useful for when sprites available

	/**
	 * Empty constructor for serialization
	 */
	public SpeedyEnemy() {
	}

	/***
	 * Construct a new speedy raccoon enemy at specific position with pre-defined size and render-length.
	 *
	 * @param posX
	 * @param posY
	 */
	public SpeedyEnemy(float posX, float posY) {
        super(new Circle2D(posX, posY, 0.707f), 0.55f, 0.55f, TEXTURE, HEALTH, speed, goal);
		SpeedyEnemy.speed = speed + ((speed*roundNum)/2);
		SpeedyEnemy.goal = goal;
		this.path = null;
		// resetStats();
	}

	/***
	 * Initialise EnemyStatistics belonging to this enemy which is referenced by other classes to control
	 * enemy.
	 *
	 * @return
	 */
	private static EnemyProperties initStats() {
		EnemyProperties result = new PropertiesBuilder<EnemyEntity>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE).createEnemyStatistics();
		// result.addEvent(new MeleeAttackEvent(500));
		return result;
	}

	/***
	 * @return the EnemyStatistics of enemy which contain various governing stats of this enemy
	 */
	@Override
	public EnemyProperties getBasicStats() {
		return STATS;
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
	 *
	 * @return ProgressBarEntity corresponding to enemy's health
	 */
	@Override
	public ProgressBarEntity getProgressBar() {
		return PROGRESSBAR;
	}

	/**
	 * Steal resources from ResourceTrees if within range
	 */
	public void stealResources() {
		double interactRange = 2f;
		Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
		for (AbstractEntity entitiy : entities) {
			if (entitiy instanceof ResourceTree && entitiy.distanceTo(this) <= interactRange) {
				if (((ResourceTree) entitiy).getGatherCount() > 0) {
					((ResourceTree) entitiy).gather(-1);
				}
			}
		}
	}

	/**
	 *	@return the current Direction of raccoon
	 * */
	//@Override
	public Direction getDirection() { return currentDirection; }

	/**
	 * @return String of this type of enemy (ie 'raccoon').
	 * */
	@Override
	public String getEnemyType() { return ENEMY_TYPE; }

	/**
	 * Raccoon follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity.
	 * moves directly towards the closest resource tree, once it reaches tree it finds
	 * the next and moves between the two. If trees are destroyed move to player.
	 *
	 * @param i The current game tick
	 */
	@Override
	public void onTick(long i) {
		//raccoon steals resources from resourceTrees
		stealResources();
		//found closest goal to the enemy
		Optional<AbstractEntity> tgt = WorldUtil.getClosestEntityOfClass(goal, getPosX(), getPosY());

		updateDirection();

		//if no ResourceTree in the world, set goal to player
		if (!tgt.isPresent()) {
			PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
			AbstractEntity tgtGet = playerManager.getPlayer();
			PathManager pathManager = GameManager.get().getManager(PathManager.class);

			// check paths

			// check collision

			// check that we actually have a path
			if (path == null || path.isEmpty()) {
				path = pathManager.generatePath(this.getMask(), tgtGet.getMask());
			}

			// check if close enough to target
			if (target != null && target.overlaps(this.getMask())) {
				target = null;
			}

			// check if the path has another node
			if (target == null && !path.isEmpty()) {
				target = path.pop();
			}

			float targetX;
			float targetY;

			if (target == null) {
				target = tgtGet.getMask();
			}

			targetX = target.getX();
			targetY = target.getY();

			float deltaX = targetX - getPosX();
			float deltaY = targetY - getPosY();


			//sprite direction
			super.setMoveAngle(Direction.getRadFromCoords(deltaX, deltaY));
			super.onTickMovement();

			super.updateDirection();
		} else {
			//otherwise, set resourceTrees and move towards them

			AbstractEntity tgtGet = tgt.get();
			PathManager pathManager = GameManager.get().getManager(PathManager.class);

			// check paths

			// check collision
//			for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
//				if (entity.isSolid() && this.getMask().overlaps(entity.getMask())) {
//					// collided with wall
//					path = pathManager.generatePath(this.getMask(), tgtGet.getMask());
//					target = path.pop();
//					break;
//				}
//			}

			// check that we actually have a path
			if (path == null || path.isEmpty()) {
				path = pathManager.generatePath(this.getMask(), tgtGet.getMask());
			}

			// check if close enough to target
			if (target != null && target.overlaps(this.getMask())) {
				target = null;
			}

			// check if the path has another node
			if (target == null && !path.isEmpty()) {
				target = path.pop();
			}

			float targetX;
			float targetY;

			if (target == null) {
				target = tgtGet.getMask();
			}

			targetX = target.getX();
			targetY = target.getY();

			float deltaX = targetX -getPosX();
			float deltaY = targetY - getPosY();

			//sprite direction
			super.setMoveAngle(Direction.getRadFromCoords(deltaX, deltaY));
			super.onTickMovement();

			super.updateDirection();
		}
	}
}
