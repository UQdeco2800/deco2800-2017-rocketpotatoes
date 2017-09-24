package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.projectiles.Projectile;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

public abstract class EnemyEntity extends MortalEntity implements HasProgressBar, Tickable {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(Player.class);

	private transient Random random = new Random();
	private float speed;
	private Class<?> goal;

	private static final List<Color> colours = Arrays.asList(Color.RED);
	private static final ProgressBarEntity progressBar = new ProgressBarEntity("progress_bar", colours, 0, 1);

	/**
	 * Default constructor for serialization
	 */
	public EnemyEntity() {
		// empty for serialization
		registerNewEvents(getBasicStats().getNormalEventsCopy());
	}


	/**
	 * Constructs a new AbstractEntity. The entity will be rendered at the same size
	 * used for collision between entities.
	 * 
	 * @param mask
	 * 			  The collision mask of the entity.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in rendering.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in rendering.
	 * @param texture
	 *            The id of the texture for this entity.
	 * @param maxHealth
	 *            The initial maximum health of the enemy
	 * @param speed
	 * 			  The speed of the enemy
	 * @param goal
	 * 			  The attacking goal of the enemy
	 */
    public EnemyEntity(CollisionMask mask, float xRenderLength, float yRenderLength, String texture, float maxHealth, 
            float speed, Class<?> goal) {
        super(mask, xRenderLength, yRenderLength, texture, maxHealth);
		registerNewEvents(getBasicStats().getNormalEventsCopy());
		this.speed = speed;
		this.goal = goal;
	}

	/**
	 * Move the enemy to its target. If the goal is player, use playerManager to get targeted player position for target, 
	 * otherwise get the closest targeted entity position.
	 */
	@Override
	public void onTick(long i) {
		float goalX;
		float goalY;
		if (goal == Player.class) {
			PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
			SoundManager soundManager = GameManager.get().getManager(SoundManager.class);

			// The X and Y position of the player without random floats generated
			goalX = playerManager.getPlayer().getPosX() ;
			goalY = playerManager.getPlayer().getPosY() ;
		
			if(this.distance(playerManager.getPlayer()) < speed) {
				this.setPosX(goalX);
				this.setPosY(goalY);
				return;
			}
//
//			float deltaX = getPosX() - goalX;
//			float deltaY = getPosY() - goalY;
//
//			float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);
//
//			float changeX = (float)(speed * Math.cos(angle));
//			float changeY = (float)(speed * Math.sin(angle));
//
//			Box3D newPos = getBox3D();
//
//			newPos.setX(getPosX() + changeX);
//			newPos.setY(getPosY() + changeY);
//
//			/*
//			 * Check for enemies colliding with other entities. The following entities will not stop an enemy:
//			 *     -> Enemies of the same type, projectiles, resources.
//			 */
//			Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
//			boolean collided = false;
//			for (AbstractEntity entity : entities.values()) {
//				if (!this.equals(entity) && !(entity instanceof Projectile) && !(entity instanceof ResourceEntity) &&
//						newPos.overlaps(entity.getBox3D()) ) {
//					if(entity instanceof Player) {
//						//soundManager.playSound("ree1.wav");
//					}
//					collided = true;
//				}
//			}
//
//			if (!collided) {
//				setPosX(getPosX() + changeX);
//				setPosY(getPosY() + changeY);
//			}
		} else {
			// set the target of tankEnemy to the closest goal
			Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(goal, getPosX(), getPosY());
			// get the position of the target
			goalX = target.get().getPosX(); 
			goalY = target.get().getPosY(); 
			if(this.distance(target.get()) < speed) {
				this.setPosX(goalX);
				this.setPosY(goalY);
				return;
			}
		}
		
		

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		CollisionMask newPos = getMask();

		newPos.setX(getPosX() + changeX);
		newPos.setY(getPosY() + changeY);

		/*
		 * Check for enemies colliding with other entities. The following entities will not stop an enemy:
		 *     -> Enemies of the same type, projectiles, resources.
		 */
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		for (AbstractEntity entity : entities.values()) {
			if (!this.equals(entity) && !(entity instanceof Projectile) && !(entity instanceof Effect) && !(entity instanceof ResourceEntity) &&
					newPos.overlaps(entity.getMask()) ) {

				if(entity instanceof Tower) {
					//soundManager.playSound("ree1.wav");
				}

				if(entity instanceof Player) {
					LOGGER.info("Ouch! a " + this + " hit the player!");
					((Player) entity).damage(1);
				}
				collided = true;
			}
		}

		if (!collided) {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}

	}

	/**
	 * Registers the list of events given with the event manager and unregisters all
	 * other events for this object
	 */
	private void registerNewEvents(List<TimeEvent<EnemyEntity>> events) {
		EventManager eventManager = GameManager.get().getManager(EventManager.class);
		eventManager.unregisterAll(this);
		for (TimeEvent<EnemyEntity> timeEvent : events) {
		eventManager.registerEvent(this, timeEvent);
		}
	}

	/**
	 * Get the basic stats of this enemy
	 *
	 * @return the basic stats (BasicStats) for this enemy
	 * */
	public abstract BasicStats getBasicStats();

	@Override
	public int getProgress() {
		return (int) getHealth();
	}

	@Override
	public void setProgress(int p) {
		return;
	}

	@Override
	public boolean showProgress() {
		return true;
	}

	/**
	 * Get the goal of the enemy
	 * @return this enemy's goal
	 */
	public Class<?> getGoal() {
		return this.goal;
	}
	
	/**
	 * Set the enemy's goal to the given entity class
	 * @param g enemy's new goal(entity class)
	 */
	public void setGoal(Class<?> g) {
		this.goal = g;
	}
	
	/**
	 * Get the speed of this enemy
	 * @return the speed of this enemy
	 */
	public float getSpeed() {
		return this.speed;
	}
	
	/**
	 * Set this enemy's speed to given speed
	 * @param s enemy's new speed
	 */
	public void setSpeed(Float s) {
		this.speed = s;
	}

	/**
	 * If the enemy get shot, reduce enemy's health. Remove the enemy if dead. 
	 * @param projectile, the projectile shot
	 */
	public void getShot(Projectile projectile) {
		this.damage(projectile.getDamage());
		LOGGER.info(this + " was shot. Health now " + getHealth());
	}
	
	/**
	 * If the enemy get shot, reduce enemy's health. Remove the enemy if dead. 
	 * @param projectile, the projectile shot
	 */
	public void getShot(Effect effect) {
		this.damage(effect.getDamage());
		LOGGER.info(this + " was shot. Health now " + getHealth());
	}

	/**
	 * Returns the ProgressBar of an entity
	 * @return
	 */
	@Override
	public ProgressBarEntity getProgressBar() {
		return progressBar;
	}

	@Override
	public float getProgressRatio() {
		return (getHealth() / getMaxHealth());
	}

	@Override
	public int getMaxProgress() {
		return (int) getMaxHealth();
	}

	@Override
	public void setMaxProgress(int p) { return; }

}
