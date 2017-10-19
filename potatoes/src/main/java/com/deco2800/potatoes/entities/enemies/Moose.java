package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.enemies.enemyactions.HealingWave;
import com.deco2800.potatoes.entities.enemies.enemyactions.MeleeAttackEvent;
import com.deco2800.potatoes.entities.enemies.enemyactions.Channel;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.player.Archer;
import com.deco2800.potatoes.entities.player.Caveman;
import com.deco2800.potatoes.entities.player.Wizard;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Path;

import java.util.ArrayList;

/**
 * A moose enemy for the game. Has the special ability of a healing buff to itself and those around it
 */
public class Moose extends EnemyEntity implements Tickable, HasProgress {

	private static final transient String TEXTURE_LEFT = "pronograde"; // TODO: MAKE MOOSE TEXTURE
	private static final transient String TEXTURE_RIGHT = "pronograde";
	private static final transient float HEALTH = 100f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 1000;
//	private static final transient String ENEMY_TYPE = "moose";
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

	private int ticksSinceRandom = 0;
	private static final int MAX_WAIT = 200;

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity();

	private Direction currentDirection; // The direction the enemy faces

	/**
	 * Empty constructor for serialization
	 */
	public Moose() {
		//Empty for serialization (is a code smell to not have comment here)
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
	private void randomTarget() {
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

	/**
	 * @return the current Direction of moose
	 */
	//@Override
	public Direction getDirection() {
		return currentDirection;
	}

	/**
	 * Moose follows it's path.
	 * Requests a new path whenever it collides with a staticCollideable entity
	 * moves directly towards the player once it reaches the end of it's path
	 *
	 * @param i
	 */
	@Override
	public void onTick(long i) {
/*		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
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
			if (entity.isSolid() && getMask().overlaps(entity.getMask())) {
				//collided with wall
				randomTarget();
				//break;
			} else if (entity instanceof EnemyEntity && entity.getMask().overlaps(getMask())) {
				EnemyEntity enemy = (EnemyEntity) entity;
				//heal enemy if within range
				enemy.heal(0.1f);
			}
		}

		// check that we actually have a path
		if (path == null || path.isEmpty()) {
			path = pathManager.generatePath(getMask(), target);
		}

		//check if close enough to target
		if (target != null && playerManager.getPlayer().getMask().overlaps(getMask())) {
			target = playerManager.getPlayer().getMask();
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
			target = playerManager.getPlayer().getMask();
		}

		targetX = target.getX();
		targetY = target.getY();

		float deltaX = targetX -getPosX();
		float deltaY = targetY -getPosY();

		super.setMoveAngle(Direction.getRadFromCoords(deltaX, deltaY));

		super.onTickMovement();

		super.updateDirection();
		*/

		AbstractEntity relevantTarget = super.mostRelevantTarget(targets);
		if (getMoving()) {
			pathMovement(pathTarget, relevantTarget);
			super.onTickMovement();
		}
		super.updateDirection();
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
	 * @return
	 */
	private static EnemyProperties initStats() {
		HealingWave healingWave = new HealingWave(3500, GoalPotate.class, 8f, 80f);

		EnemyProperties result = new PropertiesBuilder<>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE_LEFT)
				.addEvent(new MeleeAttackEvent(ATTACK_SPEED, GoalPotate.class))
				.addEvent(new Channel(50, 1000, healingWave))
				.addEvent(healingWave)
				.createEnemyStatistics();

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

	private EnemyTargets initTargets() {
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

		return new EnemyTargets(mainTargets, sightAggroTargets);
	}
}
