package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.enemies.enemyactions.MeleeAttackEvent;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.player.Archer;
import com.deco2800.potatoes.entities.player.Caveman;
import com.deco2800.potatoes.entities.player.Wizard;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.util.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A stronger but slower enemy type, only attacks towers/trees
 */
public class TankEnemy extends EnemyEntity implements Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TankEnemy.class);
	private static final EnemyProperties STATS = initStats();
	private static final transient String TEXTURE = "tankBear";
	private static final transient float HEALTH = 1500;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;
	private static final transient String[] ENEMY_TYPE = new String[]{

		"bear_walk",
		"bear_walk",
		"bear_walk",
		"bear_walk",
		"bear_walk",
		"bear_walk",
		"bear_walk",
		"bear_walk",

	};
//	private static final transient String ENEMY_TYPE = "bear";
//private static final transient String[] ENEMY_TYPE = new String[]{
//
//		"raccoon",
//
//
//
//
//};



	/* Define speed, goal and path variables */
	private static float speed = 0.006f;
	private static Class<?> goal = AbstractTree.class;


	private Path path = null;
	private Shape2D target = null;
	private PathAndTarget pathTarget = new PathAndTarget(path, target);
	private static EnemyTargets targets = initTargets();

	/* Define variables for the TankEnemy's progress bar */
	private static final List<Color> COLOURS = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity(COLOURS);
	private int timer = 0;
	private Shape2D targetPos = null;


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
        super(new Circle2D(posX, posY, 1.414f), 2f, 2f, TEXTURE, HEALTH, speed, goal);
        this.health = health + (roundNum*250);
		//this.speed = getBasicStats().getSpeed();
		//this.goal = goal;
		//resetStats();
	}

	/**
	 * Move the enemy to its target. If the goal is player, use playerManager to get targeted player position for target,
	 * otherwise get the closest targeted entity position.
	 */
//	@Override
/*	public void onTick(long i) {
		float goalX = getPosX();
		float goalY = getPosY();
		//if goal is player, use playerManager to eet position and move towards target
		if (goal == Player.class) {
			//goal = Player.class;
			PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
			PathManager pathManager = GameManager.get().getManager(PathManager.class);

			// check that we actually have a path
			if (path == null || path.isEmpty()) {
				path = pathManager.generatePath(this.getMask(), playerManager.getPlayer().getMask());
			}

			//check if close enough to target
			if (targetPos != null && targetPos.overlaps(this.getMask())) {
				targetPos = null;
			}

			//check if the path has another node
			if (targetPos == null && !path.isEmpty()) {
				targetPos = path.pop();
			}

			if (targetPos == null) {
				targetPos = playerManager.getPlayer().getMask();
			}

			goalX = targetPos.getX();
			goalY = targetPos.getY();
		} else {
			//set the target of Enemy to the closest goal
			Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(goal, getPosX(), getPosY());

			if (target.isPresent()) {
				//otherwise, move to enemy's closest goal
				AbstractEntity getTarget = target.get();
				// get the position of the target

				goalX = getTarget.getPosX();
				goalY = getTarget.getPosY();

				if(this.distanceTo(getTarget) < speed) {
					this.setPosX(goalX);
					this.setPosY(goalY);
					return;
				}
			}
		}

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float)Math.atan2(deltaY, deltaX) + (float)Math.PI;

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		Shape2D newPos = getMask();

		newPos.setX(getPosX() + changeX);
		newPos.setY(getPosY() + changeY);
/*
		/*
		 * Check for enemies colliding with other entities. The following entities will not stop an enemy:
		 *     -> Enemies of the same type, projectiles, resources.
		 */
/*		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		boolean collidedTankEffect = false;
		timer++;
		String stompedGroundTextureString = "";

		for (AbstractEntity entity : entities.values()) {
			if (!this.equals(entity) && !(entity instanceof Projectile) && !(entity instanceof TankEnemy)
					&& !(entity instanceof EnemyGate) && newPos.overlaps(entity.getMask()) ) {

				if(entity instanceof Player) {
					LOGGER.info("Ouch! a " + this + " hit the player!");
					((Player) entity).damage(1);

				}
				if (entity instanceof Effect || entity instanceof ResourceEntity) {
					if (this instanceof TankEnemy && entity instanceof StompedGroundEffect) {
						collidedTankEffect = true;
						stompedGroundTextureString = entity.getTexture();
					}
					continue;
				}
				collided = true;
			}
		}


		if (this instanceof TankEnemy) {
			if (timer % 100 == 0 && !collided) {
				GameManager.get().getManager(SoundManager.class).playSound("tankEnemyFootstep.wav");
				GameManager.get().getWorld().addEntity(
						new LargeFootstepEffect(MortalEntity.class, getPosX(), getPosY(), 1, 1));
			}
			if (stompedGroundTextureString.equals("DamagedGroundTemp2") ||
					stompedGroundTextureString.equals("DamagedGroundTemp3")) {
				GameManager.get().getWorld().addEntity(
						new StompedGroundEffect(MortalEntity.class, getPosX(), getPosY(), true, 1, 1));
			} else if (!collidedTankEffect) {
				GameManager.get().getWorld().addEntity(
						new StompedGroundEffect(MortalEntity.class, getPosX(), getPosY(), true, 1, 1));
			}
		}

		if (!collided) {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}

		super.updateDirection();
	}
*/

	@Override
	public void onTick(long i) {
		AbstractEntity relevantTarget = mostRelevantTarget(targets);
		if (getMoving() == true) {
			pathMovement(pathTarget, relevantTarget);
			super.onTickMovement();
		}
		super.updateDirection();
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

	private static EnemyTargets initTargets() {
		/*Enemy will move to these (in order) if no aggro*/
		ArrayList<Class> mainTargets = new ArrayList<>();
		mainTargets.add(BasePortal.class);
		mainTargets.add(Archer.class);
		mainTargets.add(Caveman.class);
		mainTargets.add(Wizard.class);

		/*if enemy can 'see' these, then enemy aggros to these*/
		ArrayList<Class> sightAggroTargets = new ArrayList<>();
		sightAggroTargets.add(Archer.class);
		sightAggroTargets.add(Caveman.class);
		sightAggroTargets.add(Wizard.class);

		/*Not yet implemented - concept: if enemy is attacked by these, then enemy aggros to these*/
		ArrayList<Class> damageAggroTargets = new ArrayList<>();
		damageAggroTargets.add(Archer.class);
		damageAggroTargets.add(Caveman.class);
		damageAggroTargets.add(Wizard.class);

		EnemyTargets targets = new EnemyTargets(mainTargets, sightAggroTargets, damageAggroTargets);
		return targets;
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
	//@Override
	public Direction getDirection() {
		return currentDirection;
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
