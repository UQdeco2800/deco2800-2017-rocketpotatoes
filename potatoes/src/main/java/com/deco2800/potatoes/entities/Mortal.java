package com.deco2800.potatoes.entities;

/**
 * An interface to handle heatlh, damage taken, healing, and death
 * @author michaelruigrok
 */
public interface Mortal {

	/**
	 * @return the current health
	 */
	float getHealth();

	/**
	 * @return the max health
	 */
	float getMaxHealth();

	/**
	 * @return true iff health <= 0
	 */
	boolean isDead();

	/**
	 * @param amount - the amount of health to subtract
	 * @return iff the resulting damage killed the entity
	 */
	boolean damage(float amount);

	/**
	 * @param amount - the amount of health to refill
	 * @return if health is now maxed out
	 */
	boolean heal(float amount);

	/**
	 * Alters the offset of any damage dealt to the entity.
	 * Use negative values to decrease damage dealt, and negate the parameter
	 *  to revert the offset.
	 * @param amount - the amount of health damage is to be offset by
	 * @return current value of damage offset
	 */
	float addDamageOffset(float offset);

	/**
	 * Alters the scale of damage dealt to the entity
	 * @param amount - the decimal coefficient to scale damage to the entity by
	 * @return current value of damage scaling
	 */
	float addDamageScaling(float scale);

	/**
	 * Perform the required death behaviour
	 */ 
	void deathHandler();
}
