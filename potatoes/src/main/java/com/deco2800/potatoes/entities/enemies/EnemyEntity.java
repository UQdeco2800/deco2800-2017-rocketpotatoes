package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.*;

import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import com.deco2800.potatoes.renderering.particles.types.BasicParticleType;
import com.deco2800.potatoes.renderering.particles.types.ParticleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.deco2800.potatoes.entities.Direction.getFromRad;
import static com.deco2800.potatoes.entities.Direction.getRadFromCoords;

/**
 * An abstract class for the basic functionality of enemy entities which extend from it
 */
public abstract class EnemyEntity extends MortalEntity implements HasProgressBar, Tickable {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(EnemyEntity.class);

	private float speed;

	protected Class<?> targetClass;	//TODO add multiple target classes & weightings
	protected Integer target = null;  //the integer corresponding to the target
	protected Point2D targetPathNode = null;


	private boolean moving = true;
	private int channelTimer;

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthBarRed", 1);
	private String enemyStatus = "_walk";
	protected int roundNum = 0;
	private int texturePointer=1;
	private long sTime = System.currentTimeMillis();
	private int textureLength = 0;
	private int delayTime = 500;

	/**
	 * Default constructor for serialization
	 */
	public EnemyEntity() {
		getBasicStats().registerEvents(this);	//MAY BE USELESS
	}

	/**
	 * Constructs a new EnemyEntity with specific render lengths. Allows
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

		this.targetClass = goal;
	}

	/***
	 * Update the enemy's target that it is moving to and the path that it is following to do so with
	 * a provided goal AbstractEntity
	 */

	@Override
	public void onTick(long i) {

		// if no target, get closest, if still none, don't move
		if (target == null) {
			target = mostRelevantTarget();
			if (target == null)
				return;
		}

		// get the next node in a path to the target
		PathManager pathMan = GameManager.get().getManager(PathManager.class);
		targetPathNode = pathMan.getNextNodeToTarget((Circle2D) this.getMask(), target, targetPathNode);

		// check node not null
		if (targetPathNode == null)
			return;

		// move towards node targetPathNode
		float deltaX = targetPathNode.getX() - getPosX();
		float deltaY = targetPathNode.getY() - getPosY();

		super.setMoveAngle(Direction.getRadFromCoords(deltaX, deltaY));
		super.onTickMovement();

		this.updateDirection();
	}

	/***
	 * Determine an enemy's most relevant target according to its main entity and 'sight aggro' (entities that
	 * if close enough to an enemy cause the enemy to preferentially move to it) entity targets.
	 *
	 * @return the ID of the closest entity of type targetClass, if no entity is found null is returned
	 */
	public Integer mostRelevantTarget() {
		Optional<AbstractEntity> newTarget = GameManager.get().getWorld().getClosestEntity(getPosX(), getPosY(), targetClass);

		if (newTarget.isPresent()) {
			for (Map.Entry<Integer, AbstractEntity> entity : GameManager.get().getWorld().getEntities().entrySet()) {
				AbstractEntity e = entity.getValue();
				if (newTarget.equals(e)) {
					return entity.getKey();
				}
			}
		}
		return null;
	}

	/**
	 * set up the animation delay time
	 * @param time milliseconds
	 */
	public void setDelayTime(int time){
		this.delayTime=time;
	}

	/**
	 * set up the animation length
	 * @param length how many texture for this enemy
	 */
	public void setTextureLength(int length){
		this.textureLength=length;
	}
	/**
	 * Flag whether this enemy should be allowed to move or not
	 *
	 * @param move
	 */
	public void setMoving(boolean move) { this.moving = move; }

	/**
	 * @return whether this enemy is allowed to move or not
	 */
	public boolean getMoving() { return this.moving; }

	/**
	 * Updates the direction of the enemy based on change in position.
	 */
	public void updateDirection() {
		// if not moving don't update
		if (super.getMoveSpeedModifier() == 0) {
			return;
		}
		// set facing Direction based on movement angle
		this.facing = getFromRad(super.getMoveAngle());
		this.updateSprites();
	}

	/***
	 * Sets the direction of enemy to face a set of x, y coordinates
	 *
	 * @param xCoord x coordinate
	 * @param yCoord y coordinate
	 */
	public void setDirectionToCoords(float xCoord, float yCoord) {
		this.facing = getFromRad( getRadFromCoords( xCoord-this.getPosX(), yCoord-this.getPosY()));
		this.updateSprites();
	}

	/**
	 * Updates the player sprite based on it's state and direction.
	 */
	public void updateSprites() {
		String[] type = getEnemyType();
		String direction = "_" + super.facing.name();
		if (type.length == 1) {
			this.setTexture(type[0] + direction);
		} else {
			LOGGER.info("Texture:::"+type[0]+enemyStatus + direction + "_" + texturePointer);
			this.setTexture(type[0]+enemyStatus + direction + "_" + texturePointer);
			if(delay(delayTime)){
				texturePointer++;
				if(texturePointer>textureLength){
					texturePointer=1;
				}
			}
		}
	}

	/**
	 * the purpose of method is make a time delay for next texture
	 * @param milliSeconds i guest just millisecond

	 * @return Int the index of texture
	 */
	public boolean delay(int milliSeconds){

		if((System.currentTimeMillis()-sTime)>milliSeconds){
			sTime=System.currentTimeMillis();
			return true;
		}
		return false;
	}

	/***
	 * Abstract method requiring extending classes to return a string corresponding
	 * to their enemy type. Useful for selecting sprites.
	 *
	 * @return String corresponding to enemy type (e.g. squirrel)
	 */
	public abstract String[] getEnemyType();

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
	 *
	 * @return this enemy's goal
	 */
	public Class<?> getTargetClass() {
		return this.targetClass;
	}

	/**
	 * Set the status for an enemy.
	 *
	 * @param enemyStatus, the new status
	 */
	public void setEnemyStatus(String enemyStatus){
		this.enemyStatus=enemyStatus;
	}

	public String getEnemyStatus(){
		return this.enemyStatus;
	}
	/**
	 * Set the enemy's goal to the given entity class
	 * @param newTargetClass enemy's new goal(entity class)
	 */
	public void setTargetClass(Class<?> newTargetClass) {
		this.targetClass = newTargetClass;
	}

	/**
	 * Get the speed of this enemy
	 *
	 * @return the speed of this enemy
	 */
	public float getSpeed() {
		return this.speed;
	}

	/**
	 * Set this enemy's speed to given speed
	 *
	 * @param newSpeed enemy's new speed
	 */
	public void setSpeed(Float newSpeed) {
		this.speed = newSpeed;
	}

	/***
	 * Get the current value of the enemy's channel timer.
	 * The channel timer acts as an internal clock of enemy that can be used to see for how
	 * long an enemy has been in a channelling state for.
	 *
	 * @return channelTimer
	 */
	public int getChannelTimer() { return this.channelTimer; }

	/**
	 * Set the value of the enemy's channel timer.
	 *
	 * @param channelTime
	 */
	public void setChannellingTimer(int channelTime) { this.channelTimer = channelTime; }

	/**
	 * If the enemy get shot, reduce enemy's health. Remove the enemy if dead.
	 *
	 * @param projectile, the projectile shot
	 */
	public void getShot(Projectile projectile) {
		this.damage(projectile.getDamage());

		LOGGER.info(this + " was shot. Health now " + getHealth());
	}

	/**
	 * If the enemy get shot, reduce enemy's health. Remove the enemy if dead.
	 *
	 * @param effect, the projectile shot
	 */
	public void getShot(Effect effect) {
		this.damage(effect.getDamage());
		LOGGER.info(this + " was shot. Health now " + getHealth());
	}

	/**
	 * Returns the ProgressBar of an entity
	 *
	 * @return enemy's progress bar
	 */
	@Override
	public ProgressBarEntity getProgressBar() {
		return PROGRESS_BAR;
	}

	/***
	 * Get the enemy's current health to max health progress.
	 *
	 * @return the ratio of current to maximum health
	 */
	@Override
	public float getProgressRatio() {
		return getHealth() / getMaxHealth();
	}

	/**
	 * Get the maximum health of the enemy.
	 *
	 * @return the enemy's maximum health
	 */
	@Override
	public int getMaxProgress() {
		return (int) getMaxHealth();
	}

	/**
	 * Apply damage to this enemy and if a 'being damaged' animation exists for it, trigger
	 * the animation.
	 *
	 * @param amount - the amount of health to subtract
	 * @return true if damaged
	 */
	@Override
	public boolean damage(float amount) {
		getBasicStats().setDamageAnimation(this);
		return super.damage(amount);
	}

	/**
	 * Trigger death animation of enemy if enemy has one
	 */
	@Override
	public void dyingHandler() {
		getBasicStats().setDeathAnimation(this);
	}

	/**
	 * Trigger actions upon the death of this enemy, these being:
	 * 	- create particle blood splatter effect
	 * 	- remove enemy
	 * 	- remove it's events
	 */
	@Override
	public void deathHandler() {

		LOGGER.info(this + " is dead.");

		ParticleManager p = GameManager.get().getManager(ParticleManager.class);

		ParticleType particle =  new BasicParticleType(100000, 500.0f,
				0.0f, 512, Color.RED, 7, 7);
		particle.setSpeed(0.2f);

		Vector2 pos = Render3D.worldToScreenCoordinates(this.getPosX(), this.getPosY(), 0);
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");
		p.addParticleEmitter(2f, new ParticleEmitter(pos.x + tileWidth / 2, pos.y + tileHeight / 2,
				particle));

		// destroy the enemy & it's events
		GameManager.get().getWorld().removeEntity(this);
		GameManager.get().getManager(EventManager.class).unregisterAll(this);
		GameManager.get().getManager(WaveManager.class).getActiveWave().reduceTotalEnemiesByOne();
	}

}
