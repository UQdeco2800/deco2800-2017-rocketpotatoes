/**
 * 
 */
package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;

/**
 * @author michaelruigrok
 *
 */
public class MortalEntity extends AbstractEntity implements Mortal {

	protected float health;
	protected float maxHealth;
	protected float damageOffset = 0;
	protected float damageScaling = 1;

	/**
	 * Default constructor for serialization
	 */
	public MortalEntity() { }

	/**
	 * Constructs a new AbstractEntity. The entity will be rendered at the same size
	 * used for collision between entities.
	 * 
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param posZ
	 *            The z-coordinate of the entity.
	 * @param xLength
	 *            The length of the entity, in x. Used in rendering and collision
	 *            detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in rendering and collision
	 *            detection.
	 * @param zLength
	 *            The length of the entity, in z. Used in rendering and collision
	 *            detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 * @param maxHealth
	 *            The initial maximum health of the entity
	 */
	public MortalEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture, float maxHealth) {
		super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches.
	 * 
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param posZ
	 *            The z-coordinate of the entity.
	 * @param xLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param zLength
	 *            The length of the entity, in z. Used in collision detection.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 * @param maxHealth
	 *            The initial maximum health of the entity
	 */
	public MortalEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, String texture, float maxHealth) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, texture);
		this.maxHealth = maxHealth;
		this.health = maxHealth;

	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches. Allows rendering of entities to be centered on their
	 * coordinates if centered is true.
	 * 
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param posZ
	 *            The z-coordinate of the entity.
	 * @param xLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param zLength
	 *            The length of the entity, in z. Used in collision detection.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param centered
	 *            True if the entity is to be rendered centered, false otherwise.
	 * @param texture
	 *            The id of the texture for this entity.
	 * @param maxHealth
	 *            The initial maximum health of the entity
	 */
	public MortalEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, boolean centered, String texture, float maxHealth) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered, texture);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getHealth() {
		return health;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getMaxHealth() {
		return health;
	}

	/**
	 * @return the current damage offset
	 */
	public float getDamageOffset() {
		return damageOffset;
	}

	/**
	 * @return the current damage scaling
	 */
	public float getDamageScaling() {
		return damageScaling;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDead() {
		return health <= 0f;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean damage(float amount) {
		health -= (amount * getDamageScaling() - getDamageOffset());
		if (isDead()) {
			deathHandler();
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean heal(float amount) {
		if ((health += amount) > maxHealth) {
			health = maxHealth;
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deathHandler() {
		//System.out.println(this + " is dead.");
		GameManager.get().getWorld().removeEntity(this);
	}

	/**
	 * Alters the offset of any damage dealt to the entity.
	 * Use negative values to decrease damage dealt, and negate the parameter
	 *  to revert the offset.
	 * @param offset - the amount of health damage is to be offset by
	 * @return current value of damage offset
	 */
	public float addDamageOffset(float offset) {
		return this.damageOffset += offset;
	}

	/**
	 * Alters the scale of damage dealt to the entity
	 * @param amount - the decimal coefficient to scale damage to the entity by
	 * @return current value of damage scaling
	 */
	@Override
	public float addDamageScaling(float scale) {
		return this.damageScaling += scale;
	}

}
