package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.badlogic.gdx.math.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.entities.HasDirection;

/**
 * A speedy raccoon enemy that steals resources from resource trees.
 */
public class SpeedyEnemy extends EnemyEntity implements Tickable, HasDirection {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpeedyEnemy.class);
	private static final transient String TEXTURE = "speedyRaccoon";
	private static final transient float HEALTH = 80f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 2000;
	private static final transient String enemyType = "raccoon";

	private static final EnemyStatistics STATS = initStats();

	private static float speed = 0.08f;
	private static Class<?> goal = ResourceTree.class;
	private Path path = null;
	private Box3D target = null;

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
	 * @param posZ
	 */
	public SpeedyEnemy(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.50f, 0.50f, 0.50f, 0.55f, 0.55f, TEXTURE, HEALTH, speed, goal);
		 this.speed = speed;
		 this.goal = goal;
		 this.path = null;
		// resetStats();
	}

	/***
	 * Initialise EnemyStatistics belonging to this enemy which is referenced by other classes to control
	 * enemy.
	 *
	 * @return
	 */
	private static EnemyStatistics initStats() {
		EnemyStatistics result = new StatisticsBuilder<EnemyEntity>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE).createEnemyStatistics();
		// result.addEvent(new MeleeAttackEvent(500));
		return result;
	}

	/***
	 * @return the EnemyStatistics of enemy which contain various governing stats of this enemy
	 */
	@Override
	public EnemyStatistics getBasicStats() {
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
			if (entitiy instanceof ResourceTree && entitiy.distance(this) <= interactRange) {
				if (((ResourceTree) entitiy).getGatherCount() > 0) {
					((ResourceTree) entitiy).gather(-1);
				}
			}
		}
	}

	/**
	 *	@return the current Direction of raccoon
	 * */
	public Direction getDirection() { return currentDirection; }

	/**
	 * @return String of this type of enemy (ie 'raccoon').
	 * */
	public String getEnemyType() { return enemyType; }

	/**
	 * Raccoon follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity.
	 * moves directly towards the closest resource tree, once it reaches tree it finds
	 * the next and moves between the two. If trees are destroyed move to player.
	 *
	 * @param i The current game tick
	 */
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
			for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
				if (entity.isStaticCollideable() && this.getBox3D().overlaps(entity.getBox3D())) {
					// collided with wall
					path = pathManager.generatePath(this.getBox3D(), tgtGet.getBox3D());
					target = path.pop();
					break;
				}
			}

			// check that we actually have a path
			if (path == null || path.isEmpty()) {
				path = pathManager.generatePath(this.getBox3D(), tgtGet.getBox3D());
			}

			// check if close enough to target
			if (target != null && target.overlaps(this.getBox3D())) {
				target = null;
			}

			// check if the path has another node
			if (target == null && !path.isEmpty()) {
				target = path.pop();
			}

			float targetX;
			float targetY;

			if (target == null) {
				target = tgtGet.getBox3D();
			}

			targetX = target.getX();
			targetY = target.getY();

			float deltaX = getPosX() - targetX;
			float deltaY = getPosY() - targetY;

			float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

			// flip sprite
			if (deltaX + deltaY >= 0) {
				//this.setTexture(TEXTURE);
			} else {
				//this.setTexture(TEXTURE_RIGHT);
			}

			float changeX = (float) (speed * Math.cos(angle));
			float changeY = (float) (speed * Math.sin(angle));

			this.setPosX(getPosX() + changeX);
			this.setPosY(getPosY() + changeY);
		} else {
			//otherwise, set resourceTrees and move towards them
			
			AbstractEntity tgtGet = tgt.get();
			PathManager pathManager = GameManager.get().getManager(PathManager.class);

			// check paths

			// check collision
			for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
				if (entity.isStaticCollideable() && this.getBox3D().overlaps(entity.getBox3D())) {
					// collided with wall
					path = pathManager.generatePath(this.getBox3D(), tgtGet.getBox3D());
					target = path.pop();
					break;
				}
			}

			// check that we actually have a path
			if (path == null || path.isEmpty()) {
				path = pathManager.generatePath(this.getBox3D(), tgtGet.getBox3D());
			}

			// check if close enough to target
			if (target != null && target.overlaps(this.getBox3D())) {
				target = null;
			}

			// check if the path has another node
			if (target == null && !path.isEmpty()) {
				target = path.pop();
			}

			float targetX;
			float targetY;

			if (target == null) {
				target = tgtGet.getBox3D();
			}

			targetX = target.getX();
			targetY = target.getY();

			float deltaX = getPosX() - targetX;
			float deltaY = getPosY() - targetY;

			float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

			// flip sprite
			if (deltaX + deltaY >= 0) {
				//this.setTexture(TEXTURE);
			} else {
				//this.setTexture(TEXTURE_RIGHT);
			}

			float changeX = (float) (speed * Math.cos(angle));
			float changeY = (float) (speed * Math.sin(angle));

			this.setPosX(getPosX() + changeX);
			this.setPosY(getPosY() + changeY);
		}
	}
}