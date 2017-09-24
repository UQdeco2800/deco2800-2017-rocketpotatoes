package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;


import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
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
	private static final transient float attackRange = 0.5f;
	private static final transient int attackSpeed = 1000;

	private static float speed = 0.02f;
	private static Class<?> goal = Tower.class;
	private Path path = null;
	private CollisionMask target = null;

	private static final List<Color> colours = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity progressBar = new ProgressBarEntity(colours);

	
	/**
	 * Empty constructor for serialization
	 */
	public TankEnemy() {
        this(0, 0);
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
	public TankEnemy(float posX, float posY) {
        super(new Circle2D(posX, posY, 1.414f), 1f, 1f, TEXTURE, HEALTH, speed, goal);
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
		BasicStats result = new BasicStats(HEALTH, speed, attackRange, attackSpeed, normalEvents, TEXTURE);
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


	/**
	 * Squirrel follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity
	 * moves directly towards the player once it reaches the end of it's path
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		PathManager pathManager = GameManager.get().getManager(PathManager.class);

		// check paths

		//check collision
		for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
			if (entity.isStaticCollideable() && this.getMask().overlaps(entity.getMask())) {
				//collided with wall
				path = pathManager.generatePath(this.getMask(), playerManager.getPlayer().getMask());
				target = path.pop();
				break;
			}
		}

		// check that we actually have a path
		if (path == null || path.isEmpty()) {
			path = pathManager.generatePath(this.getMask(), playerManager.getPlayer().getMask());
		}


		//check if close enough to target
		if (target != null && target.overlaps(this.getMask())) {
			target = null;
		}

		//check if the path has another node
		if (target == null && !path.isEmpty()) {
			target = path.pop();
		}

		float targetX;
		float targetY;


		if (target == null) {
			target = playerManager.getPlayer().getMask();
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

	// @Override
	// public void onTick(long i) {
	//
	// // //set the target of tankEnemy to the closest goal
	// Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(goal,
	// getPosX(), getPosY());
	//
	// // get the position of the target
	// float goalX = target.get().getPosX();
	// float goalY = target.get().getPosY();
	//
	//
	// if(this.distance(target.get()) < speed) {
	// this.setPosX(goalX);
	// this.setPosY(goalY);
	// return;
	// }
	//
	//
	// float deltaX = getPosX() - goalX;
	// float deltaY = getPosY() - goalY;
	//
	// float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);
	//
	//
	//
	// float changeX = (float)(speed * Math.cos(angle));
	// float changeY = (float)(speed * Math.sin(angle));
	//
	// Box3D newPos = getBox3D();
	//
	// newPos.setX(getPosX() + changeX);
	// newPos.setY(getPosY() + changeY);
	//
	//
	// Map<Integer, AbstractEntity> entities =
	// GameManager.get().getWorld().getEntities();
	// boolean collided = false;
	// for (AbstractEntity entity : entities.values()) {
	// if (!this.equals(entity) && !(entity instanceof Projectile) &&
	// newPos.overlaps(entity.getBox3D()) ) {
	// if(entity instanceof Tower) {
	// //soundManager.playSound("ree1.wav");
	// }
	// collided = true;
	// }
	// }
	//
	// if (!collided) {
	// setPosX(getPosX() + changeX);
	// setPosY(getPosY() + changeY);
	// // tankEnemy changes direction when moving towards tree/tower
	//
	// if(this.getPosX()>goalX){
	// this.setTexture(TEXTURE);
	// }
	// else{
	// this.setTexture(TEXTURE);
	// }
	// }
	//
	// }
}
