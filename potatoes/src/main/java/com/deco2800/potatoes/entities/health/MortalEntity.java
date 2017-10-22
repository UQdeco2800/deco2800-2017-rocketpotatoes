/**
 * 
 */
package com.deco2800.potatoes.entities.health;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.effects.HealingEffect;
import com.deco2800.potatoes.gui.GameOverGui;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author michaelruigrok
 *
 */
public class MortalEntity extends AbstractEntity implements Mortal, HasProgress, Tickable {

	protected float health;
	protected float maxHealth;
	protected float damageOffset = 0f;
	protected float damageScaling = 1f;
	protected boolean deathHandled = false;
	private boolean dying = false;
	protected Direction facing; 		// The direction the entity is facing

	private static final transient Logger LOGGER = LoggerFactory.getLogger(MortalEntity.class);

	/**
	 * Default constructor for serialization
	 */
	public MortalEntity() {
		// empty because serialization
	}

	/**
	 * Constructs a new AbstractEntity. The entity will be rendered at the same size
	 * used for collision between entities.
	 * 
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param xLength
	 *            The length of the entity, in x. Used in rendering and collision
	 *            detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in rendering and collision
	 *            detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 * @param maxHealth
	 *            The initial maximum health of the entity
	 */
	public MortalEntity(float posX, float posY, float xLength, float yLength, String texture, 
			float maxHealth) {
		this(posX, posY, xLength, yLength, xLength, yLength, texture, maxHealth);
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
	 * @param xLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 * @param maxHealth
	 *            The initial maximum health of the entity
	 */
	public MortalEntity(float posX, float posY, float xLength, float yLength, 
			float xRenderLength, float yRenderLength, String texture, float maxHealth) {
		this(new Box2D(posX, posY, xLength, yLength), xRenderLength, yRenderLength, texture, maxHealth);
	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches. Allows rendering of entities to be centered on their
	 * coordinates if centered is true.
	 * 
     * @param mask
     *            The collision mask used by the entity.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 * @param maxHealth
	 *            The initial maximum health of the entity
	 */
    public MortalEntity(Shape2D mask, float xRenderLength, float yRenderLength, String texture,
						float maxHealth) {

        super(mask, xRenderLength, yRenderLength, texture);
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
		return maxHealth;
	}

	/**
	 * Alters the max health of an entity
	 * Use negative values to decrease max health, and negate the parameter
	 *  to revert changes
	 * @param offset - the amount the entity's max health is to be altered by
	 * @return current value of damage offset
	 */
	@Override
	public float addMaxHealth(float offset) {
		this.maxHealth += offset;
		if (maxHealth <= 0 )
			maxHealth = 1;
		return this.maxHealth;
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
	 * Apply damage to the entity.
	 * damageOffset may not cause damage dealt to be below zero - but scaling can
	 * damageOffset does not apply when scaling causes damage to heal
	 * @param amount - the amount of health to subtract
	 * @return iff the resulting damage killed the entity
	 */
	@Override
	public boolean damage(float amount) {
		float damage = amount * getDamageScaling();

		if (damage > 0) {
			if (getDamageOffset() < damage) {
				health -= damage - getDamageOffset();
				LOGGER.info("{} has been damaged for {} points (health now {})", this, amount,
						getHealth());
			}
		} else {
			heal(-damage);
		}

		if (isDead() && !deathHandled) {
			setDying(true);
			dyingHandler();
			deathHandled = true;
			return true;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setHealth(float amount) {
		if (amount > 0) {
			deathHandled = false;
		}
		if (maxHealth <= amount) {
			health = maxHealth;
		} else {
			health = amount;
		}
		float epsilon = 0.00000001f;
		return Math.abs(health - maxHealth) < epsilon;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean heal(float amount) {
		health += amount;
		HealingEffect healAnimation = new HealingEffect(this.getClass(), this.getPosX(), this.getPosY(), true, 1f, 1);
		if (health > maxHealth) {
			health = maxHealth;
			LOGGER.info("{} has been healed for {} points (health now {})", this, amount,
					getHealth());
			return false;
		}
		LOGGER.info("{} has been healed for {} points (health now {})", this, amount, getHealth());
		try {
			GameManager.get().getWorld().addEntity(healAnimation);
		} catch (Exception e){
			LOGGER.info("{}",e);
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deathHandler() {
		LOGGER.info(this + " is dead.");
		GameManager.get().getWorld().removeEntity(this);
		GameManager.get().getManager(EventManager.class).unregisterAll(this);
		if (this instanceof GoalPotate){
			GameManager.get().getManager(GuiManager.class).getGui(GameOverGui.class).show();
		}
	}
	
	/**
	 * @return whether the entity is currently dying
	 */
	public boolean isDying() {
		return dying;
	}

	/**
	 * Sets if this tree is currently dying. If this is set to false, the death handler is called
	 * 
	 * @param dying
	 *            whether this entity is dying
	 */
	public void setDying(boolean dying) {
		if (isDying() && !dying) {
			// Dying is finished, so die
			deathHandler();
		}
		this.dying = dying;
	}
	
	public void dyingHandler() {
		GameManager.get().getManager(EventManager.class).unregisterAll(this);
		setDying(false);
	}

	/**
	 * Alters the offset of any damage dealt to the entity.
	 * Use negative values to decrease damage dealt, and negate the parameter
	 *  to revert the offset.
	 * @param offset - the amount of health damage is to be offset by
	 * @return current value of damage offset
	 */
	@Override
	public float addDamageOffset(float offset) {
		this.damageOffset += offset;
		return this.damageOffset;
	}

	/**
	 * Alters the scale of damage dealt to the entity
	 * @return current value of damage scaling
	 */
	@Override
	public float addDamageScaling(float scale) {
		this.damageScaling *= scale;
		return this.damageScaling;
	}

	/**
	 * Alters the scale of damage dealt to the entity
	 * @return current value of damage scaling
	 */
	@Override
	public float removeDamageScaling(float scale) {
		this.damageScaling /= scale;
		return this.damageScaling;
	}

	@Override
	public int getProgress() {
		return (int) getHealth();
	}

	@Override
	public float getProgressRatio() {
		return getHealth() / getMaxHealth();
	}

	@Override
	public int getMaxProgress() {
		return (int) getMaxHealth();
	}

	@Override
	public boolean showProgress() {
		return true;
	}

	/***
	 * Actions to be performed on every tick of the game
	 *
	 * @param time the current game tick
	 */
	@Override
	public void onTick(long time) {
	}

    public Direction getFacing() {
        return this.facing;
    }

}
