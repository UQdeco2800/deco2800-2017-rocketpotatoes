package com.deco2800.potatoes.entities.enemies;

import java.util.*;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.effects.LargeFootstepEffect;
import com.deco2800.potatoes.entities.effects.StompedGroundEffect;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

import com.deco2800.potatoes.entities.player.Archer;
import com.deco2800.potatoes.entities.player.Caveman;
import com.deco2800.potatoes.entities.player.Wizard;
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
public abstract class EnemyEntity extends MortalEntity implements HasProgressBar, Tickable {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(Player.class);

	private float speed;
	private Shape2D targetPos = null;
	private Class<?> goal;
	private Map<Integer, AbstractEntity> entities;

	private static final SoundManager enemySoundManager = new SoundManager();

	private static final List<Color> COLOURS = Arrays.asList(Color.RED);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("progress_bar", COLOURS, 0, 1);

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
    public EnemyEntity(Shape2D mask, float xRenderLength, float yRenderLength, String texture, float maxHealth,
                       float speed, Class<?> goal) {
        super(mask, xRenderLength, yRenderLength, texture, maxHealth);
        getBasicStats().registerEvents(this);
		super.setMoveSpeed(speed);
		super.setSolid(true);

		this.goal = goal;
	}

	// Method of creating enemy with round number included

   /* public EnemyEntity(CollisionMask mask, float xRenderLength, float yRenderLength, String texture, float maxHealth,
                       float speed, Class<?> goal, int round_number) {
        super(mask, xRenderLength, yRenderLength, texture, maxHealth);
        getBasicStats().registerEvents(this);
        this.speed = speed + round_number;
        this.goal = goal;
        this.round_number = round_number;
    }*/

	public void pathMovement(PathAndTarget pathTarget, AbstractEntity relevantTarget) {
		Path path = pathTarget.getPath();
		Shape2D target = pathTarget.getTarget();

		PathManager pathManager = GameManager.get().getManager(PathManager.class);
		if (relevantTarget != null) {
			// check paths
			// check that we actually have a path
			if (path == null || path.isEmpty()) {
				path = pathManager.generatePath(this.getMask(), relevantTarget.getMask());
			}

			//check if last node in path matches player
			if (!path.goal().overlaps(relevantTarget.getMask())) {
				path = pathManager.generatePath(this.getMask(), relevantTarget.getMask());
			}

			//check if close enough to target
			if (target != null && target.overlaps(this.getMask())) {
				target = null;
			}

			//check if the path has another node
			if (target == null && !path.isEmpty()) {
				target = path.pop();
			}

			if (target == null) {
				target = relevantTarget.getMask();
			}

			float deltaX = target.getX() - getPosX();
			float deltaY = target.getY() - getPosY();

			super.setMoveAngle(Direction.getRadFromCoords(deltaX, deltaY));

			pathTarget.setPath(path);
			pathTarget.setTarget(target);
		}
	}

	/*Find the most relevant target to go to according to its EnemyTargets
*
* This is likely to get EnemyEntity, squirrel is being used for testing aggro at the moment
* */
	public AbstractEntity mostRelevantTarget(EnemyTargets targets) {
		entities = GameManager.get().getWorld().getEntities();
		/*Is a sight aggro-able target within range of enemy - if so, return as a target*/
		for (AbstractEntity entity : entities.values()) {
			for (Class sightTarget : targets.getSightAggroTargets()) {
				if (entity.getClass().isAssignableFrom(sightTarget)) {
					float distance = WorldUtil.distance(this.getPosX(), this.getPosY(), entity.getPosX(), entity.getPosY());
					if (distance < 10) {
						return entity;
					}
				}
			}
		}
		/*If no aggro, return 'ultimate' target*/
		for (AbstractEntity entity : entities.values()) {
			for (Class mainTarget : targets.getMainTargets()) {
				if (entity.getClass().isAssignableFrom(mainTarget)) {
					return entity;
				}
			}
		}
		return null;
	}

	/**
	 * Updates the direction of the player based on change in position.
	 */
	public void updateDirection() {
		// if not moving don't update
		if (super.getMoveSpeedModifier() == 0) {
			return;    // Not moving
		}

		// set facing Direction based on movement angle
		this.facing = Direction.getFromRad(super.getMoveAngle());

		this.updateSprites();
	}

	/**
	 * Updates the player sprite based on it's state and direction.
	 */
	public void updateSprites() {
		String type = getEnemyType();
		String direction = "_" + super.facing.name();
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
