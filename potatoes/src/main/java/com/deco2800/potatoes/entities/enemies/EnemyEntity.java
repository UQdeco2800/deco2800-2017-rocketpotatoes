package com.deco2800.potatoes.entities.enemies;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.effects.LargeFootstepEffect;
import com.deco2800.potatoes.entities.effects.StompedGroundEffect;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

import com.deco2800.potatoes.entities.portals.BasePortal;

import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.util.Path;

import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import com.deco2800.potatoes.renderering.particles.types.BasicParticleType;
import com.deco2800.potatoes.renderering.particles.types.ParticleType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.projectiles.Projectile;

import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;


import com.deco2800.potatoes.util.WorldUtil;

/**
 * An abstract class for the basic functionality of enemy entities which extend from it
 */
public abstract class EnemyEntity extends MortalEntity implements HasProgressBar, Tickable, HasDirection {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(Player.class);

	private float speed;
	private Path path;
	private CollisionMask targetPos = null;
	private Class<?> goal;

	private static final SoundManager enemySoundManager = new SoundManager();

	private static final List<Color> COLOURS = Arrays.asList(Color.RED);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("progress_bar", COLOURS, 0, 1);

	private Vector2 oldPos = Vector2.Zero; // Used to determine the player's change in direction
	private Direction currentDirection; // The direction the player faces

	private int timer = 0;
	private Map<Integer, AbstractEntity> entities;

	protected int round_number = 0;
	/**
	 * Default constructor for serialization
	 */
	public EnemyEntity() {
		// empty for serialization
		getBasicStats().registerEvents(this);	//MAY BE USELESS
	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches.
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
        getBasicStats().registerEvents(this);
		this.speed = speed;
		this.goal = goal;
	}

	// Method of creating enemy with round number included

    public EnemyEntity(CollisionMask mask, float xRenderLength, float yRenderLength, String texture, float maxHealth,
                       float speed, Class<?> goal, int round_number) {
        super(mask, xRenderLength, yRenderLength, texture, maxHealth);
        getBasicStats().registerEvents(this);
        this.speed = speed + round_number;
        this.goal = goal;
        this.round_number = round_number;
    }

	/**
	 * Move the enemy to its target. If the goal is player, use playerManager to get targeted player position for target, 
	 * otherwise get the closest targeted entity position.
	 */
	@Override
	public void onTick(long i) {
		float goalX;
		float goalY;
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

				//otherwise, move to enemy's closest goal
				AbstractEntity getTarget = target.get();
				// get the position of the target
				goalX = getTarget.getPosX(); 
				goalY = getTarget.getPosY(); 
				
				if(this.distance(getTarget) < speed) {
					this.setPosX(goalX);
					this.setPosY(goalY);
					return;
				}
		}

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float)Math.atan2(deltaY, deltaX) + (float)Math.PI;

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		CollisionMask newPos = getMask();

		newPos.setX(getPosX() + changeX);
		newPos.setY(getPosY() + changeY);

		/*
		 * Check for enemies colliding with other entities. The following entities will not stop an enemy:
		 *     -> Enemies of the same type, projectiles, resources.
		 */
		entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		boolean collidedTankEffect = false;
		timer++;
		String stompedGroundTextureString = "";

		for (AbstractEntity entity : entities.values()) {
			if (!this.equals(entity) && !(entity instanceof Projectile ) && !(entity instanceof TankEnemy) 
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

		updateDirection();
	}

	/**
	 * @return the current Direction of the enemy
	 */
	public Direction getEnemyDirection() {
		return this.currentDirection;
	}

	/**
	 * Set the direction of the enemy based on a specified direction
	 *
	 * @param direction the direction to set the enemy toward
	 */
	private void setDirection(Direction direction) {
		if (this.currentDirection != direction) {
			this.currentDirection = direction;
			updateSprites();
		}
	}

	public BasePortal findPortal() {
		entities = GameManager.get().getWorld().getEntities();
		for (AbstractEntity entity : entities.values()) {
			if (entity instanceof BasePortal) {
				return (BasePortal)entity;
			}
		}
		return null;
	}

	/**
	 * Updates the direction of the player based on change in position.
	 */
	public void updateDirection() {
		if (this.getPosX() - oldPos.x == 0 && this.getPosY() - oldPos.y == 0) {
			return;    // Not moving
		}
		double angularDirection = Math.atan2(this.getPosY() - oldPos.y, this.getPosX() - oldPos.x) * (180 / Math.PI);

		if (angularDirection >= -180 && angularDirection < -157.5) {
			this.setDirection(Direction.SW);
		} else if (angularDirection >= -157.5 && angularDirection < -112.5) {
			this.setDirection(Direction.W);
		} else if (angularDirection >= -112.5 && angularDirection < -67.5) {
			this.setDirection(Direction.NW);
		} else if (angularDirection >= -67.5 && angularDirection < -22.5) {
			this.setDirection(Direction.N);
		} else if (angularDirection >= -22.5 && angularDirection < 22.5) {
			this.setDirection(Direction.NE);
		} else if (angularDirection >= 22.5 && angularDirection < 67.5) {
			this.setDirection(Direction.E);
		} else if (angularDirection >= 67.5 && angularDirection < 112.5) {
			this.setDirection(Direction.SE);
		} else if (angularDirection >= 112.5 && angularDirection < 157.5) {
			this.setDirection(Direction.S);
		} else if (angularDirection >= 157.5 && angularDirection <= 180) {
			this.setDirection(Direction.SW);
		}
		oldPos = new Vector2(this.getPosX(), this.getPosY());
	}

	/**
	 * Updates the player sprite based on it's state and direction.
	 */
	public void updateSprites() {
		String type = getEnemyType();
		String direction = "_" + getEnemyDirection().name();

		this.setTexture(type + direction);
	}

	/***
	 * Abstract method requiring extending classes to return a string corresponding
	 * to their enemy type. Useful for selecting sprites.
	 *
	 * @return String corresponding to enemy type (e.g. squirrel)
	 */
	public abstract String getEnemyType();

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
	public abstract EnemyProperties getBasicStats();

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
	 * @param effect, the projectile shot
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
		return PROGRESS_BAR;
	}

	/***
	 * Get the enemy's current health to max health progres
	 *
	 * @return the ratio of current to maximum health
	 */
	@Override
	public float getProgressRatio() {
		return getHealth() / getMaxHealth();
	}

	/**
	 * Get the maximum health of the enemy
	 *
	 * @return the enemy's maximum health
	 */
	@Override
	public int getMaxProgress() {
		return (int) getMaxHealth();
	}

	@Override
	public boolean damage(float amount) {
		getBasicStats().setDamageAnimation(this);
		return super.damage(amount);
	}

	@Override
	public void dyingHandler() {
		getBasicStats().setDeathAnimation(this);
	}
	
	/**
	 * remove the enemy if it is dead
	 */
	@Override
	public void deathHandler() {
		LOGGER.info(this + " is dead.");

		ParticleManager p = GameManager.get().getManager(ParticleManager.class);

		ParticleType particle =  new BasicParticleType(100000, 500.0f,
				0.0f, 1024, Color.RED, 5, 5);
		particle.speed = 0.9f;

		Vector2 pos = Render3D.worldToScreenCoordinates(this.getPosX(), this.getPosY(), 0);
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");
		p.addParticleEmitter(1.0f, new ParticleEmitter(pos.x + tileWidth / 2, pos.y + tileHeight / 2,
				particle));

		// destroy the enemy
		GameManager.get().getWorld().removeEntity(this);
	}

}
