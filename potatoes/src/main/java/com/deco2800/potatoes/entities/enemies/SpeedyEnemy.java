package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

/**
 * A class for speedy enemy
 */
public class SpeedyEnemy extends EnemyEntity implements Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpeedyEnemy.class);
	private static final transient String TEXTURE = "speedyRaccoon";
	private static final transient String TEXTURE_RIGHT = "speedyRaccoon";
	private static final transient float HEALTH = 80f;
	private static final transient float ATTACK_RANGE = 0.5f;
	private static final transient int ATTACK_SPEED = 2000;

	private static final List<Color> COLOURS = Arrays.asList(Color.RED, Color.ORANGE);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity(COLOURS);
	private static final EnemyStatistics STATS = initStats();

	private static float speed = 0.08f;
	private static Class<?> goal = ResourceTree.class;
	private Path path = null;
	private Box3D target = null;

	private static final List<Color> colours = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity progressBar = new ProgressBarEntity(colours);

	public SpeedyEnemy() {
		// super(0, 0, 0, 0.50f, 0.50f, 0.50f, 0.55f, 0.55f, TEXTURE, HEALTH, speed,
		// goal);
		// this.speed = speed;
		// this.goal = goal;
		// resetStats();
	}

	public SpeedyEnemy(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.50f, 0.50f, 0.50f, 0.55f, 0.55f, TEXTURE, HEALTH, speed, goal);
		// this.steal
		// this.speed = speed;
		// this.goal = goal;
		// resetStats();
	}

	private static EnemyStatistics initStats() {
		EnemyStatistics result = new StatisticsBuilder<EnemyEntity>().setHealth(HEALTH).setSpeed(speed)
				.setAttackRange(ATTACK_RANGE).setAttackSpeed(ATTACK_SPEED).setTexture(TEXTURE).createEnemyStatistics();
		// result.addEvent(new MeleeAttackEvent(500));
		return result;
	}

	@Override
	public EnemyStatistics getBasicStats() {
		return STATS;
	}

	@Override
	public String toString() {
		return String.format("Speedy Enemy at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

	@Override
	public ProgressBarEntity getProgressBar() {
		return progressBar;
	}

	private void harvestResources() {
		double interactRange = 3f;
		Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
		for (AbstractEntity entitiy : entities) {
			if (entitiy instanceof ResourceTree && entitiy.distance(this) <= interactRange) {
				if (((ResourceTree) entitiy).getGatherCount() > 0) {
					((ResourceTree) entitiy).gather(-1);
				}
			}
		}
	}

	public void onTick(long i) {
		harvestResources();
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		PathManager pathManager = GameManager.get().getManager(PathManager.class);

		// check paths

		// check collision
		for (AbstractEntity entity : GameManager.get().getWorld().getEntities().values()) {
			if (entity.isStaticCollideable() && this.getBox3D().overlaps(entity.getBox3D())) {
				// collided with wall
				path = pathManager.generatePath(this.getBox3D(), playerManager.getPlayer().getBox3D());
				target = path.pop();
				break;
			}
		}

		// check that we actually have a path
		if (path == null || path.isEmpty()) {
			path = pathManager.generatePath(this.getBox3D(), playerManager.getPlayer().getBox3D());
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
			target = playerManager.getPlayer().getBox3D();
		}

		targetX = target.getX();
		targetY = target.getY();

		float deltaX = getPosX() - targetX;
		float deltaY = getPosY() - targetY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		// flip sprite
		if (deltaX + deltaY >= 0) {
			this.setTexture(TEXTURE);
		} else {
			this.setTexture(TEXTURE_RIGHT);
		}

		float changeX = (float) (speed * Math.cos(angle));
		float changeY = (float) (speed * Math.sin(angle));

		this.setPosX(getPosX() + changeX);
		this.setPosY(getPosY() + changeY);
	}

	/*
	 * public void onTick(long i) { double interactRange = 3f;
	 * Collection<AbstractEntity> entities =
	 * GameManager.get().getWorld().getEntities().values();
	 * 
	 * for (AbstractEntity entitiy : entities) { if (entitiy instanceof ResourceTree
	 * && entitiy.distance(this) <= interactRange) { ((ResourceTree)
	 * entitiy).gather(-2); } }
	 * 
	 * }
	 * 
	 * /* public void gather(int amount) { int oldCount = gather.gatherCount;
	 * this.gatherCount += amount;
	 * 
	 * // Check that the new amount is bounded if (this.gatherCount >
	 * this.gatherCapacity) { this.gatherCount = this.gatherCapacity; } else if
	 * (this.gatherCount < 0) { this.gatherCount = 0; }
	 * 
	 * if (this.gatherCount - oldCount != 0) { LOGGER.info("Added " +
	 * (this.gatherCount - oldCount) + " to " + this); } }
	 * 
	 * 
	 * // @Override // public void onTick(long i) { // // /** // set the target of
	 * speedy enemy to the closest tree/tower // testing for enemy set target // it
	 * might change the target of speedy enemy //
	 **/
	// Optional<AbstractEntity> target =
	// WorldUtil.getClosestEntityOfClass(ResourceTree.class, getPosX(), getPosY());
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
	// }
	// collided = true;
	// }
	// }
	//
	// if (!collided) {
	// setPosX(getPosX() + changeX);
	// setPosY(getPosY() + changeY);
	// // speedy enemy change direction if something blocked.
	//
	// if(this.getPosX()>goalX){
	// this.setTexture(TEXTURE);
	// }
	// else{
	// this.setTexture(TEXTURE);
	// }
	// }
	// }
	/**
	 * public void onTick(long i){ harvestResources(); }
	 **/

}
