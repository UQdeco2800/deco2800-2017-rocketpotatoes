package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;

public abstract class EnemyEntity extends AbstractEntity implements HasProgress {
	
	protected float health;

	/**
	 * Default constructor for serialization
	 */
	public EnemyEntity() { }

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
	 */
	public EnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture, float health) {
		super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture);
		this.health = health;
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
	 */
	public EnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, String texture, float health) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, texture);
		this.health = health;

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
	 */
	public EnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, boolean centered, String texture, float health) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered, texture);
		this.health = health;
	}
	
	
	

	@Override
	public int getProgress() {
		return (int)health;
	}

	@Override
	public boolean showProgress() {
		return true;
	}

	@Override
	public void setProgress(int p) { health = p; }
	
	public void getShot(Projectile projectile) {
		this.health -= projectile.getDamage();
		//System.out.println(this + " was shot. Health now " + health);
		if(health <= 0f) {
			GameManager.get().getWorld().removeEntity(this);
			//System.out.println(this + " is dead.");

		}
	}


}
